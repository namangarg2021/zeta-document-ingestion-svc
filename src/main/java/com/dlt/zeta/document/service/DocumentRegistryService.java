package com.dlt.zeta.document.service;

import com.dlt.zeta.document.entity.DocumentRegistry;
import com.dlt.zeta.document.repository.DocumentRegistryRepository;
import com.dlt.zeta.quarkus.common.lib.util.DateTimeUtils;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import lombok.extern.jbosslog.JBossLog;

import java.util.List;
import java.util.UUID;

@ApplicationScoped
@JBossLog
public class DocumentRegistryService {
	
	@Inject
	DocumentRegistryRepository documentRegistryRepository;
	
	@Transactional
	public void insertOrAppendDoc(UUID sessionId, UUID docId, UUID clientId, UUID advisorId,
	                              String filename, String s3FilePath, String createdBy) {
		DocumentRegistry registry = new DocumentRegistry();
		registry.setDocId(docId);
		registry.setClientId(clientId);
		registry.setAdvisorId(advisorId);
		registry.setFilename(filename);
		registry.setS3FilePath(s3FilePath);
		registry.setCreatedBy(createdBy);
		registry.setSessionId(sessionId);
		registry.setCreatedAt(DateTimeUtils.getCurrentUKDateTime());
		documentRegistryRepository.persist(registry);
	}
	
	@Transactional
	public List<DocumentRegistry> displayDocuments(UUID sessionId) {
		return documentRegistryRepository.findBySessionId(sessionId);
	}
}
