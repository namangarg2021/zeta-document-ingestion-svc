package com.dlt.zeta.document.repository;

import com.dlt.zeta.document.entity.MilvusPage;
import com.dlt.zeta.document.mapper.MilvusMapper;
import com.dlt.zeta.document.model.milvus.MilvusPageDTO;
import com.dlt.zeta.document.model.milvus.PageSummaryView;
import io.milvus.client.MilvusServiceClient;
import io.milvus.grpc.FieldData;
import io.milvus.grpc.QueryResults;
import io.milvus.grpc.SearchResults;
import io.milvus.param.MetricType;
import io.milvus.param.dml.DeleteParam;
import io.milvus.param.dml.InsertParam;
import io.milvus.param.dml.QueryParam;
import io.milvus.param.dml.SearchParam;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.extern.jbosslog.JBossLog;

import java.util.*;

@ApplicationScoped
@JBossLog
public class MilvusRepository {
	
	private static final String COLLECTION = "milvus_pages";
	
	@Inject
	MilvusServiceClient client;
	
	public void insertPage(MilvusPage milvusPage) {
		InsertParam insertParam = InsertParam.newBuilder()
				.withCollectionName(COLLECTION)
				.withFields(List.of(
						new InsertParam.Field("id", List.of(milvusPage.getId().toString())),
						new InsertParam.Field("session_id", List.of(milvusPage.getSessionId().toString())),
						new InsertParam.Field("document_id", List.of(milvusPage.getDocumentId().toString())),
						new InsertParam.Field("document_name", List.of(milvusPage.getDocumentName())),
						new InsertParam.Field("page_id", List.of(milvusPage.getPageId().toString())),
						new InsertParam.Field("page_number", List.of(milvusPage.getPageNumber())),
						new InsertParam.Field("page_summary", List.of(milvusPage.getPageSummary())),
						new InsertParam.Field("page_vector", List.of(milvusPage.getPageVector())),
						new InsertParam.Field("base64_string", List.of(""))))
				.build();
		
		client.insert(insertParam);
	}
	
	public Optional<MilvusPageDTO> findByPageId(UUID pageId) {
		
		QueryParam queryParam = QueryParam.newBuilder()
				.withCollectionName(COLLECTION)
				.withExpr("page_id == \"" + pageId + "\"")
				.withOutFields(List.of(
						"session_id",
						"document_id",
						"document_name",
						"page_id",
						"page_number",
						"page_summary",
						"page_vector"
				))
				.build();
		
		QueryResults queryResults = client.query(queryParam).getData();
		if (queryResults == null || queryResults.getFieldsDataList().isEmpty()) {
			return Optional.empty();
		}
		return Optional.of(MilvusMapper.toMilvusPage(queryResults));
	}
	
	public List<String> findDistinctDocumentNamesBySession(UUID sessionId) {
		QueryParam queryParam = QueryParam.newBuilder()
				.withCollectionName(COLLECTION)
				.withExpr("session_id == \"" + sessionId + "\"")
				.withOutFields(List.of("document_name"))
				.build();
		
		QueryResults queryResults = client.query(queryParam).getData();
		
		Map<String, FieldData> fieldMap = new HashMap<>();
		for (FieldData field : queryResults.getFieldsDataList()) {
			fieldMap.put(field.getFieldName(), field);
		}
		
		return fieldMap.get("document_name")
				.getScalars()
				.getStringData()
				.getDataList()
				.stream()
				.distinct()
				.toList();
	}
	
	public List<PageSummaryView> findPageSummaries(String documentName, UUID sessionId) {
		QueryParam queryParam = QueryParam.newBuilder()
				.withCollectionName(COLLECTION)
				.withExpr("document_name == \"" + documentName + "\" && " +
								"session_id == \"" + sessionId + "\"")
				.withOutFields(List.of("page_number", "page_summary"))
				.build();
		
		QueryResults queryResults = client.query(queryParam).getData();
		System.out.println(queryResults);
		List<PageSummaryView> results = MilvusMapper.toPageSummaryView(queryResults);
		results.sort(Comparator.comparingInt(PageSummaryView::getPageNumber));
		return results;
	}
	
	public SearchResults semanticSearch(UUID sessionId, List<Float> queryVector, int topK) {
		SearchParam searchParam = SearchParam.newBuilder()
				.withCollectionName(COLLECTION)
				.withExpr("session_id == \"" + sessionId + "\"")
				.withVectorFieldName("page_vector")
				.withVectors(List.of(queryVector))
				.withTopK(topK)
				.withMetricType(MetricType.COSINE)
				.withOutFields(List.of("page_id", "document_name", "page_number", "page_summary"))
				.build();
		
		SearchResults searchResults = client.search(searchParam).getData();
		log.infof("Search results: %s",  searchResults);
		return searchResults;
	}
	
	public Optional<MilvusPageDTO> findById(UUID id) {
		QueryParam queryParam = QueryParam.newBuilder()
				.withCollectionName(COLLECTION)
				.withExpr("id == \"" + id + "\"")
				.withOutFields(List.of(
						"id",
						"session_id",
						"document_id",
						"document_name",
						"page_id",
						"page_number",
						"page_summary",
						"page_vector"
				))
				.build();
		
		QueryResults queryResults = client.query(queryParam).getData();
		return Optional.of(MilvusMapper.toMilvusPage(queryResults));
	}
	
	public boolean deleteById(UUID id) {
		DeleteParam deleteParam = DeleteParam.newBuilder()
				.withCollectionName(COLLECTION)
				.withExpr("id in [\"" + id + "\"]")
				.build();
		
		var response = client.delete(deleteParam);
		
		return response.getData() != null && response.getData().getDeleteCnt() > 0;
	}
}

