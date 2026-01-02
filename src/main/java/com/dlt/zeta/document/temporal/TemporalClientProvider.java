package com.dlt.zeta.document.temporal;

import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowClientOptions;
import io.temporal.serviceclient.WorkflowServiceStubs;
import io.temporal.serviceclient.WorkflowServiceStubsOptions;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.ws.rs.Produces;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;

@ApplicationScoped
public class TemporalClientProvider {

    private static final Logger LOG = Logger.getLogger(TemporalClientProvider.class);
    private final WorkflowClient workflowClient;
    
    @Inject
    public TemporalClientProvider(@ConfigProperty(name = "quarkus.temporal.host") String host,
                                  @ConfigProperty(name = "quarkus.temporal.port") int port,
                                  @ConfigProperty(name = "quarkus.temporal.namespace") String namespace) {
        LOG.info("[TemporalClientProvider] - TemporalClientProvider | Initializing Temporal client with host: " + host + ", port: " + port);
        WorkflowServiceStubs service = WorkflowServiceStubs.newServiceStubs(
                WorkflowServiceStubsOptions.newBuilder()
                        .setEnableHttps(true)
                        .setTarget(host + ":" + port)
                        .build());

        this.workflowClient = WorkflowClient.newInstance(service, WorkflowClientOptions.newBuilder().setNamespace(namespace).build());
        LOG.info("[TemporalClientProvider] - TemporalClientProvider | Workflow client successfully initialized for namespace: " + namespace);
    }

    @Produces
    @Named("temporal")
    public WorkflowClient getWorkflowClient() {
        return workflowClient;
    }
}