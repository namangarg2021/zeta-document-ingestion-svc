package com.dlt.zeta.document.temporal.activity;

import io.temporal.activity.ActivityInterface;
import io.temporal.activity.ActivityMethod;

import java.util.List;
import java.util.UUID;

@ActivityInterface
public interface InsertMilvusPageActivity {
	
	@ActivityMethod
	void insertMilvusPage(UUID sessionId, String fileName, String markdown, int pageNumber, List<Float> embedding, UUID docId);
}
