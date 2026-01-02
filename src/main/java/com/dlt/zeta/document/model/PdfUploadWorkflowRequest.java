package com.dlt.zeta.document.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PdfUploadWorkflowRequest implements Serializable {
	private UUID sessionId;
	private UUID clientId;
	private UUID advisorId;
	private String chatType;
	private List<PdfFilePayload> files;
}

