package com.dlt.zeta.document.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class CustomAPIException extends RuntimeException{
	private String message;
	private Integer statusCode;
	private String errorCode;
}
