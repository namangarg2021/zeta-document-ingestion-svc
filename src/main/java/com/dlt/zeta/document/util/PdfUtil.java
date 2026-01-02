package com.dlt.zeta.document.util;

import com.dlt.zeta.document.model.PdfFilePayload;
import lombok.SneakyThrows;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.text.PDFTextStripper;
import org.jboss.resteasy.reactive.multipart.FileUpload;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class PdfUtil {
	
	// Converts page bytes to String
	@SneakyThrows
	public static String extractText(byte[] pdfBytes) {
		try (PDDocument document = PDDocument.load(new ByteArrayInputStream(pdfBytes))) {
			PDFTextStripper stripper = new PDFTextStripper();
			return stripper.getText(document);
		}
	}
	
	// Converts pdf to page bytes
	@SneakyThrows
	public static byte[] pdfToBytes(FileUpload file) {
		return Files.readAllBytes(file.uploadedFile());
	}
	
	// Reads a pdf and converts each page to byte[]
	@SneakyThrows
	public static List<byte[]> extractPages(byte[] pdfBytes) {
		List<byte[]> pages = new ArrayList<>();
		
		try (PDDocument document = PDDocument.load(pdfBytes)) {
			for (PDPage page : document.getPages()) {
				try (PDDocument single = new PDDocument()) {
					single.addPage(page);
					
					ByteArrayOutputStream out = new ByteArrayOutputStream();
					single.save(out);
					pages.add(out.toByteArray());
				}
			}
		}
		return pages;
	}
	
	// returns number of pages in pdf
	public static int getPdfPageCount(FileUpload upload) {
		try (PDDocument document = PDDocument.load(Files.newInputStream(upload.filePath()))) {
			return document.getNumberOfPages();
		} catch (Exception e) {
			throw new RuntimeException("Failed to read PDF page count", e);
		}
	}
	
	public static int getPdfPageCount(PdfFilePayload upload) {
		try (PDDocument document =
				     PDDocument.load(new ByteArrayInputStream(upload.getPdfBytes()))) {
			return document.getNumberOfPages();
		} catch (Exception e) {
			throw new RuntimeException("Failed to read PDF page count", e);
		}
	}
}

