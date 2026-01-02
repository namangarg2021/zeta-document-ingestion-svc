package com.dlt.zeta.document.temporal.activity;

import io.temporal.activity.ActivityInterface;
import io.temporal.activity.ActivityMethod;

@ActivityInterface
public interface SummarizePageActivity {
	
	@ActivityMethod
	String generateSummary(byte[] pageBytes);
}
