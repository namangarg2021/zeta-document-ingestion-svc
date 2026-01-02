package com.dlt.zeta.document.config;

import io.milvus.client.MilvusServiceClient;
import io.milvus.param.ConnectParam;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;

@ApplicationScoped
public class MilvusConfig {
	
	@Produces
	public MilvusServiceClient milvusClient() {
		return new MilvusServiceClient(
				ConnectParam.newBuilder()
						.withHost("localhost")
						.withPort(19530)
						.build()
		);
	}
}
