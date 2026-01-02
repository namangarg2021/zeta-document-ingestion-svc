package com.dlt.zeta.document.service;

import com.dlt.zeta.document.model.ThoughtsAndResponse;
import com.dlt.zeta.document.util.LLMUtil;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.chat.request.ChatRequest;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.model.embedding.EmbeddingModel;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.extern.jbosslog.JBossLog;

import java.util.Collections;
import java.util.List;

@ApplicationScoped
@JBossLog
public class ChatService {
	
	@Inject
	ChatModel chatModel;
	
	@Inject
	EmbeddingModel embeddingModel;
	
	public ThoughtsAndResponse chat(List<ChatMessage> messages) {
		ChatResponse chatResponse = chatModel.chat(ChatRequest.builder()
				.messages(messages)
				.build());
		ThoughtsAndResponse thoughtsAndResponse = LLMUtil
				.extractThoughtsAndResponse(chatResponse.aiMessage().text());
		return thoughtsAndResponse;
	}
	
	public List<Float> generateEmbeddings(String text) {
		try {
			return embeddingModel.embed(text).content().vectorAsList();
		} catch (Exception e) {
			log.errorf(e, "Embedding generation failed for input: %.30s", text);
			return Collections.nCopies(384, 0.0f);
		}
	}
}
