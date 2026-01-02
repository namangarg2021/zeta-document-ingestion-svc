package com.dlt.zeta.document.service;

import com.dlt.zeta.document.model.ThoughtsAndResponse;
import com.dlt.zeta.document.model.milvus.PageSearchResult;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.data.message.UserMessage;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.extern.jbosslog.JBossLog;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@ApplicationScoped
@JBossLog
public class PdfQueryService {
	
	@Inject
	ChatService chatService;
	
	@Inject
	MilvusService milvusService;
	
	public String search(UUID sessionId, String userQuery, int topK) {
		List<PageSearchResult> pageSearchResults = milvusService
				.semanticSearch(sessionId, userQuery, topK);
		String context = pageSearchResults.stream()
				.map(PageSearchResult::getPageSummary)
				.collect(Collectors.joining("\n"));
		String systemMessage = """
				Answer the question using the context below.
				Context:
				%s
				""".formatted(context);
		List<ChatMessage> chatMessages = List.of(new UserMessage(userQuery),
				new SystemMessage(systemMessage));
		ThoughtsAndResponse thoughtsAndResponse = chatService.chat(chatMessages);
		return thoughtsAndResponse.response();
	}
}
