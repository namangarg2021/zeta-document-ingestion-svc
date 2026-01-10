package com.dlt.zeta.document.model.documentChatSession;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateSessionRequest {
	private UUID clientId;
	private UUID advisorId;
	private String chatType;
	private String createdBy;
}
