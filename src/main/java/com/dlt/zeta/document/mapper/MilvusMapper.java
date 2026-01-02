package com.dlt.zeta.document.mapper;

import com.dlt.zeta.document.entity.MilvusPage;
import com.dlt.zeta.document.model.PdfUploadForm;
import com.dlt.zeta.document.model.milvus.MilvusPageDTO;
import com.dlt.zeta.document.model.milvus.PageSearchResult;
import com.dlt.zeta.document.model.milvus.PageSummaryView;
import io.milvus.grpc.FieldData;
import io.milvus.grpc.QueryResults;
import io.milvus.grpc.SearchResults;
import jakarta.enterprise.context.ApplicationScoped;
import org.jboss.resteasy.reactive.multipart.FileUpload;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@ApplicationScoped
public class MilvusMapper {
	
	public static List<PageSearchResult> toSearchResults(SearchResults searchResults) {
		List<PageSearchResult> results = new ArrayList<>();
		
		if (searchResults == null || searchResults.getResults() == null) {
			return results;
		}
		
		var result = searchResults.getResults();
		
		Map<String, FieldData> fieldMap = result
				.getFieldsDataList()
				.stream()
				.collect(Collectors.toMap(FieldData::getFieldName, Function.identity()));
		
		FieldData pageIdField = fieldMap.get("page_id");
		FieldData documentNameField = fieldMap.get("document_name");
		FieldData pageNumberField = fieldMap.get("page_number");
		FieldData pageSummaryField = fieldMap.get("page_summary");
		
		if (pageIdField == null || documentNameField == null ||
				pageNumberField == null || pageSummaryField == null) {
			return results;
		}
		
		int rowCount = pageIdField.getScalars()
				.getStringData()
				.getDataCount();
		
		for (int i = 0; i < rowCount; i++) {
			UUID pageId = UUID.fromString(pageIdField.getScalars()
							.getStringData()
							.getData(i));
			
			String documentName = documentNameField.getScalars()
							.getStringData()
							.getData(i);
			
			int pageNumber = pageNumberField.getScalars()
							.getIntData()
							.getData(i);
			
			String pageSummary = pageSummaryField.getScalars()
							.getStringData()
							.getData(i);
			
			results.add(new PageSearchResult(pageId, documentName, pageNumber, pageSummary));
		}
		
		return results;
	}
	
	public static MilvusPageDTO toMilvusPage(QueryResults queryResults) {
		
		Map<String, FieldData> fieldMap = new HashMap<>();
		for (FieldData field : queryResults.getFieldsDataList()) {
			fieldMap.put(field.getFieldName(), field);
		}

		UUID id = UUID.fromString(fieldMap.get("id")
				.getScalars()
				.getStringData()
				.getData(0));
		
		UUID sessionId = UUID.fromString(fieldMap.get("session_id")
						.getScalars()
						.getStringData()
						.getData(0));
		
		UUID documentId = UUID.fromString(fieldMap.get("document_id")
						.getScalars()
						.getStringData()
						.getData(0));
		
		String documentName = fieldMap.get("document_name")
						.getScalars()
						.getStringData()
						.getData(0);
		
		UUID pageId = UUID.fromString(fieldMap.get("page_id")
						.getScalars()
						.getStringData()
						.getData(0));
		
		int pageNumber = fieldMap.get("page_number")
						.getScalars()
						.getIntData()
						.getData(0);
		
		String pageSummary = fieldMap.get("page_summary")
						.getScalars()
						.getStringData()
						.getData(0);
		
		List<Float> pageVector = null;
		if (fieldMap.containsKey("page_vector")) {
			pageVector = fieldMap.get("page_vector")
							.getVectors()
							.getFloatVector()
							.getDataList();
		}
		
		return MilvusPageDTO.builder()
				.id(id)
				.sessionId(sessionId)
				.documentId(documentId)
				.documentName(documentName)
				.pageId(pageId)
				.pageNumber(pageNumber)
				.pageSummary(pageSummary)
				.pageVector(pageVector)
				.build();
	}
	
	public static List<PageSummaryView> toPageSummaryView(QueryResults queryResults) {
		
		Map<String, FieldData> fieldMap = new HashMap<>();
		for (FieldData field : queryResults.getFieldsDataList()) {
			fieldMap.put(field.getFieldName(), field);
		}
		
		FieldData pageNumberField = fieldMap.get("page_number");
		FieldData pageSummaryField = fieldMap.get("page_summary");
		
		int rowCount = pageSummaryField.getScalars()
				.getStringData()
				.getDataCount();
		System.out.println(rowCount);
		List<PageSummaryView> results = new ArrayList<>();
		
		for (int i = 0; i < rowCount; i++) {
			int pageNumber = pageNumberField.getScalars()
					.getIntData()
					.getData(i);
			
			String pageSummary = pageSummaryField.getScalars()
					.getStringData()
					.getData(i);
			
			results.add(new PageSummaryView(pageNumber, pageSummary));
		}
		
		return results;
	}
	
	public MilvusPageDTO toMilvusPageDTO(PdfUploadForm form, FileUpload file, String markdown, int pageNumber, List<Float> embedding, UUID docId) {
		MilvusPageDTO dto = new MilvusPageDTO();
		dto.setPageSummary(markdown);
		dto.setPageNumber(pageNumber);
		dto.setPageVector(embedding);
		dto.setPageId(UUID.randomUUID());
		dto.setDocumentName(file.fileName());
		dto.setDocumentId(docId);
		dto.setSessionId(form.getSessionId());
		dto.setBase64String("");
		return dto;
	}
	
	public MilvusPage toMilvusPage(UUID sessionId, String fileName, String markdown, int pageNumber, List<Float> embedding, UUID docId) {
		MilvusPage milvusPage = new MilvusPage();
		milvusPage.setId(UUID.randomUUID());
		milvusPage.setPageSummary(markdown);
		milvusPage.setPageNumber(pageNumber);
		milvusPage.setPageVector(embedding);
		milvusPage.setPageId(UUID.randomUUID());
		milvusPage.setDocumentName(fileName);
		milvusPage.setDocumentId(docId);
		milvusPage.setSessionId(sessionId);
		milvusPage.setBase64String("");
		return milvusPage;
	}
}
