package com.dlt.zeta.document.temporal.activity;

import com.dlt.zeta.document.service.S3Service;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.extern.jbosslog.JBossLog;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@ApplicationScoped
@JBossLog
public class ReadFileFromS3ActivityImpl implements ReadFileFromS3Activity {
	
	@Inject
	S3Service s3Service;
	
	@ConfigProperty(name = "quarkus.vault-config.bucketName")
	String bucketName;
	
	@Override
	public byte[] readFileFromS3(String bucketKey) {
		return s3Service.getDocument(bucketName, bucketKey);
	}
}
