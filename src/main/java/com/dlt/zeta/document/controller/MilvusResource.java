package com.dlt.zeta.document.controller;

import com.dlt.zeta.document.model.PdfUploadForm;
import com.dlt.zeta.document.model.milvus.PageSummaryView;
import com.dlt.zeta.document.service.MilvusService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.UUID;

@Path("/milvus")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MilvusResource {
	
	@Inject
	MilvusService milvusService;
	
	@GET
	@Path("/{id}")
	public Response getById(@PathParam("id") UUID id) {
		return Response.ok(milvusService.getById(id)).build();
	}
	
	@GET
	@Path("/pages/{pageId}")
	public Response getByPageId(@PathParam("pageId") UUID pageId) {
		return Response.ok(milvusService.getByPageId(pageId)).build();
	}
	
	@DELETE
	@Path("/{id}")
	public Response deleteById(@PathParam("id") UUID id) {
		return Response.ok(milvusService.delete(id)).build();
	}
	
	@GET
	@Path("/pages/sessions/{sessionId}")
	public Response getDistinctDocumentNames(@PathParam("sessionId") UUID sessionId) {
		return Response.ok(milvusService.findDistinctDocumentNamesBySession(sessionId))
				.build();
	}
	
	@GET
	@Path("/pages/sessions/{sessionId}/documents/{documentName}")
	public Response getPageSummaries(@PathParam("sessionId") UUID sessionId,
	                                 @PathParam("documentName") String documentName) {
		return Response.ok(	milvusService.findPageSummaries(documentName, sessionId))
				.build();
	}
}
