package com.dlt.zeta.document.service;

import com.dlt.zeta.document.entity.MilvusPage;
import com.dlt.zeta.document.mapper.MilvusMapper;
import com.dlt.zeta.document.model.milvus.MilvusPageDTO;
import com.dlt.zeta.document.model.milvus.PageSearchResult;
import com.dlt.zeta.document.model.milvus.PageSummaryView;
import com.dlt.zeta.document.repository.MilvusRepository;
import io.milvus.grpc.SearchResults;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class MilvusService {
	
	@Inject
	MilvusRepository repository;
	
	@Inject
	ChatService chatService;
	
	public Optional<MilvusPageDTO> getById(UUID id) {
		return repository.findById(id);
	}
	
	public Optional<MilvusPageDTO> getByPageId(UUID pageId) {
		return repository.findByPageId(pageId);
	}
	
	@Transactional
	public MilvusPage insert(MilvusPage milvusPage) {
		repository.insertPage(milvusPage);
		return milvusPage;
	}
	
	@Transactional
	public void bulkInsert(List<MilvusPage> pages) {
		pages.forEach(repository::insertPage);
	}
	
	@Transactional
	public MilvusPageDTO update(UUID id, MilvusPageDTO updated) {
		MilvusPageDTO existing = repository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Not found"));
		
		existing.setPageSummary(updated.getPageSummary());
		existing.setBase64String(updated.getBase64String());
		return existing;
	}
	
	@Transactional
	public boolean delete(UUID id) {
		return repository.deleteById(id);
	}
	
	@Transactional
	public List<PageSearchResult> semanticSearch(String query, int topK) {
		SearchResults searchResults = repository
				.semanticSearch(chatService.generateEmbeddings(query), topK);
		return MilvusMapper.toSearchResults(searchResults);
	}
	
	@Transactional
	public List<String> findDistinctDocumentNamesBySession(UUID sessionId){
		return repository.findDistinctDocumentNamesBySession(sessionId);
	}
	
	@Transactional
	public List<PageSummaryView> findPageSummaries(String documentName, UUID sessionId){
		return repository.findPageSummaries(documentName, sessionId);
	}
}

