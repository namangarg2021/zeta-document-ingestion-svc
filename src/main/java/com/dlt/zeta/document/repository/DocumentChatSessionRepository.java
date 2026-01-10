package com.dlt.zeta.document.repository;

import com.dlt.zeta.document.entity.DocumentChatSession;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class DocumentChatSessionRepository implements PanacheRepositoryBase<DocumentChatSession, UUID> {
	
	public List<DocumentChatSession> findByClientId(UUID clientId) {
		return find("clientId = ?1 order by createdAt desc", clientId).list();
	}
	
	public List<DocumentChatSession> findByAdvisorId(UUID advisorId) {
		return find("advisorId = ?1", advisorId).list();
	}
	
	public List<DocumentChatSession> findByUserIdAndAdvisorId(UUID userId, UUID advisorId) {
		return find("createdBy = ?1 and advisorId = ?2", userId, advisorId).list();
	}
	
	public List<DocumentChatSession> findByUserIdAndClientId(UUID userId, UUID clientId) {
		return find("createdBy = ?1 and clientId = ?2", userId, clientId).list();
	}
	
	public Optional<DocumentChatSession> updateSessionName(UUID sessionId, String sessionName) {
		return find("sessionId = ?1 and sessionName = ?2", sessionId, sessionName)
				.stream()
				.findAny();
	}
}
