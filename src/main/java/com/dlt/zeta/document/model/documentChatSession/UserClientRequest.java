package com.dlt.zeta.document.model.documentChatSession;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserClientRequest {
	private UUID clientId;
	private UUID advisorId;
	private UUID userId; // injected from security context
}

