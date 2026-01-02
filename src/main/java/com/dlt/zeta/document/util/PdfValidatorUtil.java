package com.dlt.zeta.document.util;

import com.dlt.zeta.document.exception.CustomAPIException;
import com.dlt.zeta.document.model.PdfFilePayload;
import com.dlt.zeta.document.model.PdfUploadForm;
import com.dlt.zeta.document.model.PdfUploadWorkflowRequest;
import org.jboss.resteasy.reactive.multipart.FileUpload;

import java.io.InputStream;
import java.nio.file.Files;

public class PdfValidatorUtil {
	
	private static final long MAX_TOTAL_SIZE = 10L * 1024 * 1024; // 10 MB
	private static final int CHUNK_SIZE = 1 * 1024 * 1024;        // 1 MB
	private static final int MAX_PAGES = 100;
	
	public static void validateUpload(PdfUploadForm form) {
		if(form.getSessionId() == null) {
			throw new CustomAPIException("Cannot upload documents without creating a session",
					413, "DOCQA-1004");
		}
		
		long totalSize = 0;
		for (FileUpload upload : form.getFiles()) {
			try (InputStream is = Files.newInputStream(upload.filePath())) {
				byte[] buffer = new byte[CHUNK_SIZE];
				int bytesRead;
				while ((bytesRead = is.read(buffer)) != -1) {
					totalSize += bytesRead;
					
					if(totalSize > MAX_TOTAL_SIZE) {
						throw new CustomAPIException("File size exceeds 10MB. Please upload a file of size 10MB or lesser",
								413, "DOCQA-1002");
					}
				}
			} catch (Exception e) {
				throw new RuntimeException("Failed to read uploaded file", e);
			}
			
			if(upload.fileName().toLowerCase().endsWith(".pdf")) {
				int pageCount = PdfUtil.getPdfPageCount(upload);
				if(pageCount > MAX_PAGES) {
					throw new CustomAPIException("PDF with more than 100 pages uploaded. System accepts files with lesser than 100 pages",
							413, "DOCQA-1003");
				}
			}
		}
	}
	
	
	public static void validateUpload(PdfUploadWorkflowRequest request) {
		if(request.getSessionId() == null) {
			throw new CustomAPIException("Cannot upload documents without creating a session",
					413, "DOCQA-1004");
		}
		
		long totalSize = 0;
		for (PdfFilePayload upload : request.getFiles()) {
			totalSize += upload.getPdfBytes().length;
					
			if(totalSize > MAX_TOTAL_SIZE) {
				throw new CustomAPIException("File size exceeds 10MB. Please upload a file of size 10MB or lesser",
						413, "DOCQA-1002");
			}
			
			if(upload.getFileName().toLowerCase().endsWith(".pdf")) {
				int pageCount = PdfUtil.getPdfPageCount(upload);
				if(pageCount > MAX_PAGES) {
					throw new CustomAPIException("PDF with more than 100 pages uploaded. System accepts files with lesser than 100 pages",
							413, "DOCQA-1003");
				}
			}
		}
	}
}
