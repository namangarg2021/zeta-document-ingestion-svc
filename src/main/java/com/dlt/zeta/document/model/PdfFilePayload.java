package com.dlt.zeta.document.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PdfFilePayload implements Serializable {
	private String fileName;
	private byte[] pdfBytes;
}

