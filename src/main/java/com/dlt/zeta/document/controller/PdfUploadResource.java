package com.dlt.zeta.document.controller;

import com.dlt.zeta.document.model.PdfUploadForm;
import com.dlt.zeta.document.service.PdfIngestionService;
import com.dlt.zeta.document.service.PdfQueryService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.UUID;

@Path("/documents")
@Produces(MediaType.APPLICATION_JSON)
public class PdfUploadResource {
	
	@Inject
	PdfIngestionService ingestionService;
	
	@Inject
	PdfQueryService pdfQueryService;
	
	@POST
	@Path("/upload")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Response upload(PdfUploadForm form) {
		ingestionService.processPdfs(form);
		return Response.ok().build();
	}
	
	@GET
	@Path("/search/{sessionId}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response search(@PathParam("sessionId") UUID sessionId,
	                       @QueryParam("query") String query) {
		return Response.ok(pdfQueryService.search(sessionId, query, 2)).build();
	}
}
