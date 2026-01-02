package com.dlt.zeta.document.model.markdown;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MarkdownRequest {
	private byte[] pageBytes;
}
