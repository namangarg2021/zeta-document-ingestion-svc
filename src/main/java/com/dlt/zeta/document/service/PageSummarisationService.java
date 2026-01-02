package com.dlt.zeta.document.service;

import com.dlt.zeta.document.model.ThoughtsAndResponse;
import com.dlt.zeta.document.util.PdfUtil;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.data.message.UserMessage;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.extern.jbosslog.JBossLog;

import java.util.List;

@ApplicationScoped
@JBossLog
public class PageSummarisationService {
	
	@Inject
	ChatService chatService;
	
	public String generateSummary(byte[] pdfBytes) {
		String pdfText = PdfUtil.extractText(pdfBytes);
		log.infof("pdfText = %s", pdfText);
		List<ChatMessage> chatMessages = List.of(
				new SystemMessage("""
						You are a helpful assistant.
						Summarise the content clearly and concisely,
						so that it can used to perform similarity search efficiently.
						"""),
				new UserMessage("""
						Content:
						%s
						""".formatted(pdfText))
		);
		ThoughtsAndResponse thoughtsAndResponse = chatService.chat(chatMessages);
		return thoughtsAndResponse.response();
	}
}

