package com.dlt.zeta.document.temporal.workflow;

import com.dlt.zeta.document.config.ActivityOptionsConfig;
import com.dlt.zeta.document.model.PdfFilePayload;
import com.dlt.zeta.document.model.PdfUploadWorkflowRequest;
import com.dlt.zeta.document.temporal.activity.*;
import io.temporal.workflow.Async;
import io.temporal.workflow.Promise;
import io.temporal.workflow.Workflow;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PdfIngestionWorkflowImpl implements PdfIngestionWorkflow {
	
	private static final Logger LOG = Workflow.getLogger(PdfIngestionWorkflowImpl.class);
	
	private final ValidateUploadActivity validateUploadActivity = Workflow
			.newActivityStub(ValidateUploadActivity.class, ActivityOptionsConfig.defaultActivityOptions());
	
	private final ExtractPdfActivity extractPdfActivity = Workflow
			.newActivityStub(ExtractPdfActivity.class, ActivityOptionsConfig.defaultActivityOptions());
	
	private final GenerateEmbeddingActivity generateEmbeddingActivity = Workflow
			.newActivityStub(GenerateEmbeddingActivity.class, ActivityOptionsConfig.llmActivityOptions());
	
	private final InsertMilvusPageActivity insertMilvusPageActivity = Workflow
			.newActivityStub(InsertMilvusPageActivity.class, ActivityOptionsConfig.defaultActivityOptions());
	
	private final SummarizePageActivity summarizePageActivity = Workflow
			.newActivityStub(SummarizePageActivity.class, ActivityOptionsConfig.llmActivityOptions());
	
	@Override
	public void processPdfs(PdfUploadWorkflowRequest request) {
		UUID sessionId = request.getSessionId();
		LOG.info("Starting PDF processing for sessionId={} with {} file(s)",
				sessionId, request.getFiles().size());
		
		LOG.info("Validating uploaded PDFs...");
		validateUploadActivity.validateUpload(request);
		LOG.info("Validation completed.");
		
		List<Promise<Void>> filePromises = new ArrayList<>();
		
		for (PdfFilePayload file : request.getFiles()) {
			UUID docId = Workflow.randomUUID();
			final byte[] pdfBytes = file.getPdfBytes();
			final String fileName = file.getFileName();
			
			LOG.info("Starting processing for file {}, docId={}", fileName, docId);
			
			Promise<Void> filePromise = Async.procedure(() -> {
				LOG.info("Extracting pages from file {}", fileName);
				List<byte[]> pages = extractPdfActivity.extractPages(pdfBytes);
				LOG.info("Extracted {} page(s) from file {}", pages.size(), fileName);
				
				List<Promise<Void>> pagePromises = getPagePromises(pages, fileName, sessionId, docId);
				
				LOG.info("Waiting for all {} page(s) of file {} to complete", pages.size(), fileName);
				Promise.allOf(pagePromises).get();
				LOG.info("All pages of file {} completed", fileName);
			});
			
			filePromises.add(filePromise);
		}
		
		LOG.info("Waiting for all files to complete...");
		Promise.allOf(filePromises).get();
		LOG.info("All PDF files processed for sessionId={}", sessionId);
	}
	
	private List<Promise<Void>> getPagePromises(List<byte[]> pages, String fileName, UUID sessionId, UUID docId) {
		List<Promise<Void>> pagePromises = new ArrayList<>();
		
		for (int i = 0; i < pages.size(); i++) {
			final int pageNumber = i + 1;
			final byte[] pageBytes = pages.get(i);
			
			LOG.info("Submitting async processing for file {}, page {}", fileName, pageNumber);
			// Process each page asynchronously
			Promise<Void> pagePromise = Async.procedure(() -> {
				LOG.info("Processing file {}, page {}: generating markdown", fileName, pageNumber);
				String markdown = summarizePageActivity.generateSummary(pageBytes);
				
				LOG.info("Processing file {}, page {}: generating embedding", fileName, pageNumber);
				List<Float> embedding = generateEmbeddingActivity.generateEmbedding(markdown);
				
				LOG.info("Processing file {}, page {}: inserting into Milvus", fileName, pageNumber);
				insertMilvusPageActivity.insertMilvusPage(sessionId, fileName, markdown,
															pageNumber, embedding, docId);
				
				LOG.info("Completed processing for file {}, page {}", fileName, pageNumber);
			});
			
			pagePromises.add(pagePromise);
		}
		return pagePromises;
	}
	
	
}
