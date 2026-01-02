package com.dlt.zeta.document.controller;

import com.dlt.zeta.document.model.PdfFilePayload;
import com.dlt.zeta.document.model.PdfUploadForm;
import com.dlt.zeta.document.model.PdfUploadWorkflowRequest;
import com.dlt.zeta.document.temporal.workflow.PdfIngestionWorkflow;
import com.dlt.zeta.document.util.PdfUtil;
import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowOptions;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import java.util.List;

import static com.dlt.zeta.document.temporal.TemporalInitializer.PDF_INGESTION_QUEUE;

@Path("/workflow")
@Produces(MediaType.APPLICATION_JSON)
public class WorkflowResource {
	
	@Inject
	WorkflowClient workflowClient;
	
	@POST
	@Path("/start")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public void processPdfs(PdfUploadForm form) {
		
		List<PdfFilePayload> files = form.getFiles().stream()
				.map(file -> new PdfFilePayload(
						file.fileName(),
						PdfUtil.pdfToBytes(file)
				))
				.toList();
		
		PdfIngestionWorkflow workflow = workflowClient.newWorkflowStub(
						PdfIngestionWorkflow.class,
						WorkflowOptions.newBuilder()
								.setTaskQueue(PDF_INGESTION_QUEUE)
								.build()
				);
		
		WorkflowClient.start(workflow::processPdfs,
				new PdfUploadWorkflowRequest(form.getSessionId(),
						form.getClientId(),
						form.getAdvisorId(),
						form.getChatType(),
						files));
	}
	
}
