package com.dlt.zeta.document.model.documentChatSession;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateChatSessionNameRequest {
	private String sessionName;
}
