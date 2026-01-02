package com.dlt.zeta.document.model.milvus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageSummaryView {
	private Integer pageNumber;
	private String pageSummary;
}

