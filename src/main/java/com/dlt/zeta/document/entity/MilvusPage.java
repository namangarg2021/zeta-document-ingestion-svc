package com.dlt.zeta.document.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MilvusPage {
	private UUID id;
	private UUID sessionId;
	private UUID documentId;
	private String documentName;
	private UUID pageId;
	private int pageNumber;
	private String pageSummary;
	private List<Float> pageVector;
	private String base64String;
}
