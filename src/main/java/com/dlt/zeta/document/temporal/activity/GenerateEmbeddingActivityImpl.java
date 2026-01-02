package com.dlt.zeta.document.temporal.activity;

import com.dlt.zeta.document.service.ChatService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.extern.jbosslog.JBossLog;

import java.util.List;

@ApplicationScoped
@JBossLog
public class GenerateEmbeddingActivityImpl implements GenerateEmbeddingActivity{
	
	@Inject
	ChatService chatService;
	
	@Override
	public List<Float> generateEmbedding(String markdown) {
		return chatService.generateEmbeddings(markdown);
	}
}
