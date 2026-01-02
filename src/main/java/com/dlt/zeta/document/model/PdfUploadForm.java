package com.dlt.zeta.document.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jboss.resteasy.reactive.RestForm;
import org.jboss.resteasy.reactive.multipart.FileUpload;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PdfUploadForm {
	
	@RestForm("session_id")
	private UUID sessionId;
	
	@RestForm("client_id")
	private UUID clientId;
	
	@RestForm("advisor_id")
	private UUID advisorId;
	
	@RestForm("chat_type")
	private String chatType;
	
	@RestForm("files")
	private List<FileUpload> files;
}

