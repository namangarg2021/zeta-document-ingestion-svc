package com.dlt.zeta.document.config;

import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.model.openai.OpenAiEmbeddingModel;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.milvus.MilvusEmbeddingStore;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@ApplicationScoped
public class VectorStoreConfig {
	
	@ConfigProperty(name = "quarkus.vault-config.openai-base-url")
	String baseUrl;
	
	@ConfigProperty(name = "quarkus.vault-config.openai-api-key")
	String apiKey;
	
	@Produces
	EmbeddingModel embeddingModel() {
		return OpenAiEmbeddingModel.builder()
				.baseUrl(baseUrl)
				.apiKey(apiKey)
				.modelName("sentence-transformers/all-MiniLM-L6-v2")
				.build();
	}
	
	@Produces
	EmbeddingStore<TextSegment> embeddingStore() {
		return MilvusEmbeddingStore.builder()
				.host("localhost")
				.port(19530)
				.collectionName("document_qa_collection_5")
				.dimension(384)
				.build();
	}
	
	@Produces
	@ApplicationScoped
	ChatModel chatModel() {
		return OpenAiChatModel.builder()
				.baseUrl(baseUrl)
				.apiKey(apiKey)
				.modelName("Qwen/Qwen3-32B")
				.build();
	}
}
