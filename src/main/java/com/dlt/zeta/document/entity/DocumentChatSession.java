package com.dlt.zeta.document.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "document_chat_sessions", schema = "document_qa_processor")
@Data
@EqualsAndHashCode(callSuper = false)
public class DocumentChatSession {
	
	@Id
	@Column(name = "session_id", nullable = false)
	private UUID sessionId;
	
	@Column(name = "advisor_id")
	private UUID advisorId;
	
	@Column(name = "client_id")
	private UUID clientId;
	
	@Column(name = "doc_id")
	private UUID docId;
	
	@Column(name = "chat_session")
	@JdbcTypeCode(SqlTypes.JSON)
	private String chatSession;
	
	@Column(name = "chat_type")
	private String chatType;
	
	@Column(name = "session_name")
	private String sessionName;
	
	@Column(name = "created_by")
	private String createdBy;
	
	@Column(name = "created_at", nullable = false)
	private LocalDateTime createdAt;
	
	@Column(name = "updated_at")
	private LocalDateTime updatedAt;
}

