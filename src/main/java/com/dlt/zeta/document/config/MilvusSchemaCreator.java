package com.dlt.zeta.document.config;

import io.milvus.client.MilvusServiceClient;
import io.milvus.grpc.DataType;
import io.milvus.param.IndexType;
import io.milvus.param.MetricType;
import io.milvus.param.collection.CreateCollectionParam;
import io.milvus.param.collection.FieldType;
import io.milvus.param.collection.LoadCollectionParam;
import io.milvus.param.index.CreateIndexParam;
import io.quarkus.runtime.Startup;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@Startup
@ApplicationScoped
public class MilvusSchemaCreator {
	
	@Inject
	MilvusServiceClient client;
	
	@PostConstruct
	public void createMilvusPagesCollection() {
		String collectionName = "milvus_pages";
		FieldType idField = FieldType.newBuilder()
				.withName("id")
				.withDataType(DataType.VarChar)
				.withMaxLength(36)
				.withPrimaryKey(true)
				.withAutoID(false)
				.build();
		
		FieldType sessionIdField = FieldType.newBuilder()
				.withName("session_id")
				.withDataType(DataType.VarChar)
				.withMaxLength(36)
				.build();
		
		FieldType documentIdField = FieldType.newBuilder()
				.withName("document_id")
				.withDataType(DataType.VarChar)
				.withMaxLength(36)
				.build();
		
		FieldType documentNameField = FieldType.newBuilder()
				.withName("document_name")
				.withDataType(DataType.VarChar)
				.withMaxLength(255)
				.build();
		
		FieldType base64Field = FieldType.newBuilder()
				.withName("base64_string")
				.withDataType(DataType.VarChar)
				.withMaxLength(65535)
				.build();
		
		FieldType pageIdField = FieldType.newBuilder()
				.withName("page_id")
				.withDataType(DataType.VarChar)
				.withMaxLength(36)
				.build();
		
		FieldType pageNumberField = FieldType.newBuilder()
				.withName("page_number")
				.withDataType(DataType.Int32)
				.build();
		
		FieldType pageSummaryField = FieldType.newBuilder()
				.withName("page_summary")
				.withDataType(DataType.VarChar)
				.withMaxLength(65535)
				.build();
		
		FieldType pageVectorField = FieldType.newBuilder()
				.withName("page_vector")
				.withDataType(DataType.FloatVector)
				.withDimension(384)
				.build();
		
		CreateCollectionParam createParam =
				CreateCollectionParam.newBuilder()
						.withCollectionName(collectionName)
						.withDescription("Milvus records for PDF pages")
						.withShardsNum(2)
						.addFieldType(idField)
						.addFieldType(sessionIdField)
						.addFieldType(documentIdField)
						.addFieldType(documentNameField)
						.addFieldType(base64Field)
						.addFieldType(pageIdField)
						.addFieldType(pageNumberField)
						.addFieldType(pageSummaryField)
						.addFieldType(pageVectorField)
						.build();
		
		client.createCollection(createParam);
		
		client.createIndex(
				CreateIndexParam.newBuilder()
						.withCollectionName(collectionName)
						.withFieldName("page_vector")
						.withIndexType(IndexType.IVF_FLAT)
						.withMetricType(MetricType.COSINE)
						.withExtraParam("{\"nlist\":128}")
						.build()
		);
		
		client.loadCollection(LoadCollectionParam.newBuilder()
				.withCollectionName(collectionName)
				.build()
		);
	}
}

