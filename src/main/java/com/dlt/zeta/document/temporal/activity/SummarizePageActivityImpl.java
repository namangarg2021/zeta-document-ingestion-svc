package com.dlt.zeta.document.temporal.activity;

import com.dlt.zeta.document.service.PageSummarisationService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.extern.jbosslog.JBossLog;

@ApplicationScoped
@JBossLog
public class SummarizePageActivityImpl implements SummarizePageActivity {
	
	@Inject
	PageSummarisationService pageSummarisationService;
	
	@Override
	public String generateSummary(byte[] pageBytes) {
		return pageSummarisationService.generateSummary(pageBytes);
	}
}
