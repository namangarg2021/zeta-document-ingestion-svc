package com.dlt.zeta.document.temporal.activity;

import com.dlt.zeta.document.util.PdfUtil;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.extern.jbosslog.JBossLog;

import java.util.List;

@ApplicationScoped
@JBossLog
public class ExtractPdfActivityImpl implements ExtractPdfActivity {
	
	@Override
	public List<byte[]> extractPages(byte[] pdfBytes) {
		return  PdfUtil.extractPages(pdfBytes);
	}
}
