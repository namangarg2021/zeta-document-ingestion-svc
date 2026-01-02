package com.dlt.zeta.document.temporal.workflow;

import com.dlt.zeta.document.model.PdfUploadWorkflowRequest;
import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;

@WorkflowInterface
public interface PdfIngestionWorkflow {
	
	@WorkflowMethod
	void processPdfs(PdfUploadWorkflowRequest request);
}