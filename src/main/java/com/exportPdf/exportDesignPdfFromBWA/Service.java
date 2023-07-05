package com.exportPdf.exportDesignPdfFromBWA;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;


public class Service {

	private final float PDF_PADDING_WIDTH = 100;
	private final float PDF_PADDING_HEIGHT = 100;

	public Service() {
	}

	public Optional<byte[]> convertImageToPdf(byte[] binaryImageData) throws IOException {
		PDDocument document = new PDDocument();
		try {
			PDImageXObject image = PDImageXObject.createFromByteArray(document, binaryImageData, "image");
			float imageWidth = image.getWidth();
			float imageHeight = image.getHeight();
			float documentWidth = imageWidth + PDF_PADDING_WIDTH;
			float documentHeight = imageHeight + PDF_PADDING_HEIGHT;

			PDPage page = new PDPage(new PDRectangle(documentWidth, documentHeight));
			document.addPage(page);

			PDPageContentStream contentStream = new PDPageContentStream(document, page);
			contentStream.drawImage(image, 10, 10, imageWidth, imageHeight);
			contentStream.close();

			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			document.save(outputStream);
			document.close();
			return Optional.ofNullable(outputStream.toByteArray());
		} catch (IllegalArgumentException e) {
			return Optional.empty();
		} catch (IOException e) {
			return Optional.empty();
		}
	}

	public String convertAuthPayloadToJson(String username, String password) {
		Gson gson = new GsonBuilder().create();
		Map<String, String> authPayload = new HashMap<>();
		authPayload.put("username", username);
		authPayload.put("password", password);
		return gson.toJson(authPayload);
	}

	public String getEncodedCredentials(String username, String password) {
		String credentials = username + ":" + password;
		byte[] credentialsBytes = credentials.getBytes(StandardCharsets.UTF_8);
		return Base64.getEncoder().encodeToString(credentialsBytes);
	}

	public HttpEntity<String> getRequestEntity(String url, HttpHeaders headers, String username, String password) {
		String requestBody = convertAuthPayloadToJson(username, password);
		String encodedCredentials = getEncodedCredentials(username, password);
		headers.set("Authorization", "Basic " + encodedCredentials);
		HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);

		return requestEntity;
	}

}
