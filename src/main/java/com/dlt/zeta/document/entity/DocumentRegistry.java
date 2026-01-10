package com.dlt.zeta.document.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "document_registry", schema = "document_qa_processor")
@Data
@EqualsAndHashCode(callSuper = false)
public class DocumentRegistry extends BaseEntity {
	
	@Column(name = "session_id", nullable = false)
	private UUID sessionId;
	
	@Column(name = "client_id")
	private UUID clientId;
	
	@Column(name = "advisor_id")
	private UUID advisorId;
	
	@Column(name = "doc_id", nullable = false)
	private UUID docId;
	
	@Column(name = "filename", nullable = false)
	private String filename;
	
	@Column(name = "s3_file_path", nullable = false)
	private String s3FilePath;
	
	@Column(name = "created_by", nullable = false)
	private String createdBy;
	
	@Column(name = "created_at", nullable = false)
	private LocalDateTime createdAt;
	
	@Column(name = "updated_at")
	private LocalDateTime updatedAt;
}

