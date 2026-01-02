package com.dlt.zeta.document.temporal;

import com.dlt.zeta.document.temporal.activity.*;
import com.dlt.zeta.document.temporal.workflow.PdfIngestionWorkflowImpl;
import com.google.protobuf.Duration;
import io.quarkus.runtime.StartupEvent;
import io.temporal.api.workflowservice.v1.RegisterNamespaceRequest;
import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowClientOptions;
import io.temporal.serviceclient.WorkflowServiceStubs;
import io.temporal.serviceclient.WorkflowServiceStubsOptions;
import io.temporal.worker.Worker;
import io.temporal.worker.WorkerFactory;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;

@ApplicationScoped
public class TemporalInitializer {
	
	public static final String PDF_INGESTION_QUEUE = "PDF_INGESTION_QUEUE";
	
	private static final int THIRTY_DAYS = 2592000;
	
	private static final Logger LOG = Logger.getLogger(TemporalInitializer.class);
	
	@ConfigProperty(name = "quarkus.temporal.host")
	String temporalHost;
	
	@ConfigProperty(name = "quarkus.temporal.port")
	int temporalPort;
	
	@ConfigProperty(name = "quarkus.temporal.namespace")
	String namespace;
	
	@Inject
	ValidateUploadActivity validateUploadActivity;
	
	@Inject
	ExtractPdfActivity extractPdfActivity;
	
	@Inject
	GenerateEmbeddingActivity generateEmbeddingActivity;
	
	@Inject
	InsertMilvusPageActivity insertMilvusPageActivity;
	
	@Inject
	SummarizePageActivity summarizePageActivityActivity;
	
	private WorkerFactory workerFactory;
	
	public void onStart(@Observes StartupEvent ev) {
		try {
			WorkflowServiceStubs service = WorkflowServiceStubs
					.newServiceStubs(WorkflowServiceStubsOptions
							.newBuilder()
							.setTarget(temporalHost + ":" + temporalPort)
							.setEnableHttps(true)
							.build());
			
			try {
				service.blockingStub().registerNamespace(
						RegisterNamespaceRequest.newBuilder()
								.setNamespace(namespace)
								.setWorkflowExecutionRetentionPeriod(Duration.newBuilder().setSeconds(THIRTY_DAYS).build())
								.setDescription("namespace for Zete workflows")
								.build()
				);
				
				LOG.info("[TemporalInitializer] - onStart | Namespace 'Zeta' registered.");
			} catch (Exception e) {
				LOG.warn("[TemporalInitializer] - onStart | Namespace 'Zeta' may already exist: " + e.getMessage());
			}
			
			WorkflowClient client = WorkflowClient.newInstance(
					service, WorkflowClientOptions.newBuilder()
							.setNamespace(namespace)
							.build()
			);
			
			workerFactory = WorkerFactory.newInstance(client);
			
			Worker parentWorker = workerFactory.newWorker(PDF_INGESTION_QUEUE);
			parentWorker.registerWorkflowImplementationTypes(PdfIngestionWorkflowImpl.class);
			parentWorker.registerActivitiesImplementations(validateUploadActivity, extractPdfActivity,
					generateEmbeddingActivity, insertMilvusPageActivity, summarizePageActivityActivity);
			
			workerFactory.start();
			LOG.info("[TemporalInitializer] - onStart | Zeta Worker started on task queue: " + PDF_INGESTION_QUEUE);
			
		} catch (Exception e) {
			LOG.error("[TemporalInitializer] - onStart | Failed to initialize Zeta worker", e);
		}
	}
	
	public void onStop(@Observes io.quarkus.runtime.ShutdownEvent ev) {
		if(workerFactory != null) {
			try {
				workerFactory.shutdown();
				LOG.info("[TemporalInitializer] - onStop | Zeta WorkflowFactory shut down.");
			} catch (Exception e) {
				LOG.warn("[TemporalInitializer] - onStop | Failed to shut down WorkerFactory cleanly", e);
			}
		}
	}
}