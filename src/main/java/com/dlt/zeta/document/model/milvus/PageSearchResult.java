package com.dlt.zeta.document.model.milvus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageSearchResult {
	private UUID pageId;
	private String documentName;
	private int pageNumber;
	private String pageSummary;
}
