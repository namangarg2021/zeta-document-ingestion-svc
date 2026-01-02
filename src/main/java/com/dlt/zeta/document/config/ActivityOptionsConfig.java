package com.dlt.zeta.document.config;

import io.temporal.activity.ActivityOptions;
import io.temporal.common.RetryOptions;

import java.time.Duration;

public class ActivityOptionsConfig {
	
	public static ActivityOptions defaultActivityOptions() {
		return createActivityOptions(Duration.ofMinutes(2), 5);
	}
	
	public static ActivityOptions llmActivityOptions() {
		return createActivityOptions(Duration.ofMinutes(4), 10);
	}
	
	private static ActivityOptions createActivityOptions(Duration timeout, int maxAttempts) {
		return ActivityOptions.newBuilder()
				.setStartToCloseTimeout(timeout)
				.setRetryOptions(
						RetryOptions.newBuilder()
								.setInitialInterval(Duration.ofSeconds(5))
								.setMaximumInterval(Duration.ofSeconds(30))
								.setBackoffCoefficient(2.0)
								.setMaximumAttempts(maxAttempts)
								.build()
				)
				.build();
	}
}
