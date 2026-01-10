package com.dlt.zeta.document.repository;


import com.dlt.zeta.document.entity.DocumentRegistry;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class DocumentRegistryRepository implements PanacheRepositoryBase<DocumentRegistry, UUID> {
	
	public List<DocumentRegistry> findBySessionId(UUID sessionId) {
		return find("sessionId = ?1", sessionId).list();
	}
}
