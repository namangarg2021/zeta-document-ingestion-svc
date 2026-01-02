package com.dlt.zeta.document.temporal.activity;

import com.dlt.zeta.document.model.PdfUploadWorkflowRequest;
import com.dlt.zeta.document.util.PdfValidatorUtil;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.extern.jbosslog.JBossLog;

@ApplicationScoped
@JBossLog
public class ValidateUploadActivityImpl implements ValidateUploadActivity {
	
	@Override
	public void validateUpload(PdfUploadWorkflowRequest request) {
		PdfValidatorUtil.validateUpload(request);
	}
}
