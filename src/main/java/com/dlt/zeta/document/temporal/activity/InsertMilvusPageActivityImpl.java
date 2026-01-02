package com.dlt.zeta.document.temporal.activity;

import com.dlt.zeta.document.entity.MilvusPage;
import com.dlt.zeta.document.mapper.MilvusMapper;
import com.dlt.zeta.document.service.MilvusService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.extern.jbosslog.JBossLog;

import java.util.List;
import java.util.UUID;

@ApplicationScoped
@JBossLog
public class InsertMilvusPageActivityImpl implements InsertMilvusPageActivity {
	
	@Inject
	MilvusMapper milvusMapper;
	
	@Inject
	MilvusService milvusService;
	
	@Override
	public void insertMilvusPage(UUID sessionId, String fileName, String markdown, int pageNumber, List<Float> embedding, UUID docId) {
		MilvusPage milvusPage = milvusMapper
				.toMilvusPage(sessionId, fileName, markdown, pageNumber, embedding, docId);
		milvusService.insert(milvusPage);
	}
}
