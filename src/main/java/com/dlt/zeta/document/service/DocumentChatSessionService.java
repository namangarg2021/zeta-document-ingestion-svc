package com.dlt.zeta.document.service;

import com.dlt.zeta.document.entity.DocumentChatSession;
import com.dlt.zeta.document.model.documentChatSession.*;
import com.dlt.zeta.document.repository.DocumentChatSessionRepository;
import com.dlt.zeta.quarkus.common.lib.util.DateTimeUtils;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.NotFoundException;
import lombok.extern.jbosslog.JBossLog;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
@JBossLog
public class DocumentChatSessionService {
	
	@Inject
	DocumentChatSessionRepository repository;
	
	public Optional<DocumentChatSession> getChatSession(UUID sessionId) {
		return repository.findByIdOptional(sessionId);
	}
	
	public List<DocumentChatSession> getChatSessionsByClient(UUID clientId) {
		return repository.findByClientId(clientId);
	}
	
	public List<DocumentChatSession> getChatSessionsByAdvisor(UUID advisorId) {
		return repository.findByAdvisorId(advisorId);
	}
	
	public void deleteChatSession(UUID sessionId) {
		boolean deleted = repository.deleteById(sessionId);
		if(!deleted) {
			throw new NotFoundException("Chat session not found");
		}
	}
	
	public List<DocumentChatSession> getChatSessionsByUser(UserClientRequest request) {
		if(request.getAdvisorId() != null) {
			return repository.findByUserIdAndAdvisorId(request.getUserId(), request.getAdvisorId());
		}
		if(request.getClientId() != null) {
			return repository.findByUserIdAndClientId(request.getUserId(), request.getClientId());
		}
		throw new BadRequestException("Either advisorId or clientId must be provided");
	}
	
	@Transactional
	public CreateSessionResponse createChatSession(CreateSessionRequest request) {
		String welcomeMessage = "Welcome, How can I help you analyze your documents?";
		
		DocumentChatSession session = new DocumentChatSession();
		session.setSessionId(UUID.randomUUID());
		session.setClientId(request.getClientId());
		session.setAdvisorId(request.getAdvisorId());
		session.setSessionName("Document Assistant");
		session.setChatType(request.getChatType());
		session.setCreatedBy(request.getCreatedBy());
		session.setCreatedAt(DateTimeUtils.getCurrentUKDateTime());
		
		repository.persist(session);
		
		return new CreateSessionResponse(session.getSessionId(), welcomeMessage,
				session.getChatType(), session.getClientId(), session.getAdvisorId());
	}
	
	@Transactional
	public DocumentChatSession updateSessionName(UUID sessionId, UpdateChatSessionNameRequest request) {
		Optional<DocumentChatSession> sessionOptional = repository.findByIdOptional(sessionId);
		if(sessionOptional.isEmpty()) {
			throw new NotFoundException("Chat with sessionId " + sessionId + " not found");
		}
		DocumentChatSession session = sessionOptional.get();
		session.setSessionName(request.getSessionName());
		repository.persist(session);
		return session;
	}
}