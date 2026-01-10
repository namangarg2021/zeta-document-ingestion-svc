package com.dlt.zeta.document.controller;

import com.dlt.zeta.document.model.documentChatSession.*;
import com.dlt.zeta.document.service.DocumentChatSessionService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.Map;
import java.util.UUID;

@Path("/chat_sessions")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class DocumentChatSessionResource {
	
	@Inject
	DocumentChatSessionService documentChatSessionService;
	
	@GET
	@Path("/{sessionId}")
	public Response getChatSession(@PathParam("sessionId") UUID sessionId) {
		return documentChatSessionService.getChatSession(sessionId)
				.map(Response::ok)
				.orElseThrow(() -> new NotFoundException("Chat session not found"))
				.build();
	}
	
	@GET
	@Path("/client/{clientId}")
	public Response getChatSessionsByClientId(@PathParam("clientId") UUID clientId) {
		return Response.ok(documentChatSessionService.getChatSessionsByClient(clientId))
				.build();
	}
	
	@GET
	@Path("/advisor/{advisorId}")
	public Response getByAdvisor(@PathParam("advisorId") UUID advisorId) {
		return Response.ok(documentChatSessionService.getChatSessionsByAdvisor(advisorId))
				.build();
	}
	
	
	@GET
	@Path("/sessions")
	public Response getAllChatSessions(UserClientRequest request) {
		return Response.ok(documentChatSessionService.getChatSessionsByUser(request))
				.build();
	}
	
	@POST
	@Path("/create_session")
	public Response createSession(CreateSessionRequest request) {
		return Response.ok(documentChatSessionService.createChatSession(request)).build();
	}
	
	@PATCH
	@Path("/{sessionId}")
	public Response updateSessionName(@PathParam("sessionId") UUID sessionId,
	                                             UpdateChatSessionNameRequest request) {
		return Response.ok(documentChatSessionService.updateSessionName(sessionId, request))
				.build();
	}
	
	@DELETE
	@Path("/{sessionId}")
	public Response deleteChatSession(@PathParam("sessionId") UUID sessionId) {
		documentChatSessionService.deleteChatSession(sessionId);
		return Response.ok(Map.of("message", "Chat session deleted")).build();
	}
}
