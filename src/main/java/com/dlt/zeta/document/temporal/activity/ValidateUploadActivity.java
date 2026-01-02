package com.dlt.zeta.document.temporal.activity;

import com.dlt.zeta.document.model.PdfUploadWorkflowRequest;
import io.temporal.activity.ActivityInterface;
import io.temporal.activity.ActivityMethod;

@ActivityInterface
public interface ValidateUploadActivity {
	
	@ActivityMethod
	void validateUpload(PdfUploadWorkflowRequest request);
}