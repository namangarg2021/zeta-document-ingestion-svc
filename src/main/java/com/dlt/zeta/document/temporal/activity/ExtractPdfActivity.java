package com.dlt.zeta.document.temporal.activity;

import io.temporal.activity.ActivityInterface;
import io.temporal.activity.ActivityMethod;

import java.util.List;

@ActivityInterface
public interface ExtractPdfActivity {
	
	@ActivityMethod
	List<byte[]> extractPages(byte[] pdfBytes);
}
