package com.dlt.zeta.document.model.documentChatSession;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateSessionResponse {
	private UUID sessionId;
	private String message;
	private String chatType;
	private UUID clientId;
	private UUID advisorId;
}

