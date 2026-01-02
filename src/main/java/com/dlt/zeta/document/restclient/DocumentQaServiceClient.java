package com.dlt.zeta.document.restclient;

import com.dlt.zeta.document.model.markdown.MarkdownRequest;
import com.dlt.zeta.document.model.markdown.MarkdownResponse;
import io.quarkus.oidc.client.reactive.filter.OidcClientRequestReactiveFilter;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.rest.client.annotation.RegisterProvider;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@RegisterRestClient(configKey = "document-qa-service")
@RegisterProvider(OidcClientRequestReactiveFilter.class)
public interface DocumentQaServiceClient {
	
	@POST
	@Path("/document/retrieve-context")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Retry(maxRetries = 1, delay = 100)
	MarkdownResponse convertPageBytesToMarkdown(@RequestBody MarkdownRequest markdownRequest);
}