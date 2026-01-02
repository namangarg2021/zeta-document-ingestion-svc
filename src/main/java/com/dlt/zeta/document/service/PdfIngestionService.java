package com.dlt.zeta.document.service;

import com.dlt.zeta.document.entity.MilvusPage;
import com.dlt.zeta.document.mapper.MilvusMapper;
import com.dlt.zeta.document.model.PdfUploadForm;
import com.dlt.zeta.document.model.milvus.MilvusPageDTO;
import com.dlt.zeta.document.restclient.DocumentQaServiceClient;
import com.dlt.zeta.document.util.PdfUtil;
import com.dlt.zeta.document.util.PdfValidatorUtil;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.extern.jbosslog.JBossLog;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.resteasy.reactive.multipart.FileUpload;

import java.util.List;
import java.util.UUID;

@ApplicationScoped
@JBossLog
public class PdfIngestionService {
	
	@Inject
	PageSummarisationService pageSummarisationService;
	
	@Inject
	@RestClient
	DocumentQaServiceClient documentQaServiceClient;
	
	@Inject
	MilvusService milvusService;
	
	@Inject
	MilvusMapper milvusMapper;
	
	@Inject
	ChatService chatService;
	
	public void processPdfs(PdfUploadForm form) {
		PdfValidatorUtil.validateUpload(form);
		for (FileUpload file : form.getFiles()) {
			UUID docId = UUID.randomUUID();
			byte[] pdfBytes = PdfUtil.pdfToBytes(file);
			List<byte[]> pages = PdfUtil.extractPages(pdfBytes);
			
			int pageNumber = 1;
			for (byte[] pageBytes : pages) {
				
				// TODO will call Python service once API ready
//				String markdown = documentQaServiceClient
//						.convertPageBytesToMarkdown(new MarkdownRequest(pageBytes))
//						.getMarkdown();
				
				String markdown = pageSummarisationService.generateSummary(pageBytes);
				log.infof("Summary generated for page %d", pageNumber);
				
				List<Float> embedding = chatService.generateEmbeddings(markdown);
				
				MilvusPage milvusPage = milvusMapper.toMilvusPage(form.getSessionId(), file.fileName(),
						markdown, pageNumber, embedding, docId);
				milvusService.insert(milvusPage);
				pageNumber++;
			}
		}
	}
}

