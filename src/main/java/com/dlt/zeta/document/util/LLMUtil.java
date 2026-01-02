package com.dlt.zeta.document.util;

import com.dlt.zeta.document.model.ThoughtsAndResponse;
import lombok.extern.jbosslog.JBossLog;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@JBossLog
public class LLMUtil {
	
	public static ThoughtsAndResponse extractThoughtsAndResponse(String llmResponse) {
		Pattern pattern = Pattern.compile("<think>(.*?)</think>", Pattern.DOTALL);
		
		Matcher matcher = pattern.matcher(llmResponse);
		String thoughts = "";
		String response = "";
		
		if(matcher.find()) {
			thoughts = matcher.group(1).trim();
			log.info("Agent thinking - " + thoughts);
			response = llmResponse.substring(matcher.end()).trim();
		}
		
		return new ThoughtsAndResponse(thoughts, response);
	}
}

