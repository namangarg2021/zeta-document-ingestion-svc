package com.dlt.zeta.document.temporal.activity;

import io.temporal.activity.ActivityInterface;
import io.temporal.activity.ActivityMethod;

@ActivityInterface
public interface ReadFileFromS3Activity {
	
	@ActivityMethod
	byte[] readFileFromS3(String bucketKey);
}
