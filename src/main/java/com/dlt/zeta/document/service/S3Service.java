package com.dlt.zeta.document.service;

import com.dlt.zeta.quarkus.common.lib.exception.MessageProcessorError;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.extern.jbosslog.JBossLog;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

@JBossLog
@ApplicationScoped
public class S3Service {

    @Inject
    private S3Client awsClient;

    public String saveDocument(String bucketName, String bucketKey, byte[] bytes) {
        PutObjectRequest objectMetadata = PutObjectRequest
                .builder()
                .bucket(bucketName)
                .key(bucketKey)
                .contentLength((long) bytes.length)
                .build();
        InputStream inputStream = new ByteArrayInputStream(bytes);
        PutObjectResponse putObjectResponse = awsClient.putObject(objectMetadata, RequestBody.fromInputStream(inputStream, (long) bytes.length));
        if (putObjectResponse != null) {
            log.info(putObjectResponse.toString());
            return bucketKey;
        } else {
            return null;
        }
    }

    public void deleteDocument(String bucketName, String bucketKey) {
        log.infof("deleteDocument {start} for %s", bucketKey);
        try {
            DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest
                    .builder()
                    .bucket(bucketName)
                    .key(bucketKey)
                    .build();
            DeleteObjectResponse deleteObject = awsClient.deleteObject(deleteObjectRequest);
        } catch (Exception e) {
            log.errorf(e,"Error deleting document for %s", bucketKey);
            throw new MessageProcessorError("Error deleting document","FIN10006");
        }
        log.infof("deleteDocument {start} for %s", bucketKey);
    }

    public byte[] getDocument(String bucketName, String bucketKey) {
        log.info("fetching from the AmazonS3 bucket");
        try {
            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket(bucketName).key(bucketKey).build();
            ResponseBytes<GetObjectResponse> objectAsBytes = awsClient.getObjectAsBytes(getObjectRequest);
            return objectAsBytes.asByteArray();
        } catch (Exception e) {
            log.errorf("Failed fetching document from S3 %s", e);
            throw new MessageProcessorError("Failed fetching document from S3","FIN10005");
        }
    }

}
