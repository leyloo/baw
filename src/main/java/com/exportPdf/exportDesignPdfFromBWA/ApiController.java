
package com.exportPdf.exportDesignPdfFromBWA;

import java.io.IOException;

import javax.net.ssl.*;
import java.lang.reflect.Field;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;

import java.util.Base64;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

@RestController
@RequestMapping("/api")
public class ApiController {

	@Value("${config.username}")
	private String username;

	@Value("${config.password}")
	private String password;
	
	@Value("${config.baseUrl}")
	private String baseUrl;
	
	@Autowired
	private RestTemplate restTemplateByPassSSL;
	
	private final Service service = new Service();
	private final RestTemplate restTemplate = new RestTemplate();
	
	
	
	private final HttpHeaders headers = new HttpHeaders();

	@GetMapping(value = "/process", produces = MediaType.APPLICATION_JSON_VALUE)
	
	public Optional<List<Process>> processPiid() throws JsonMappingException, JsonProcessingException {
	    String url = baseUrl + "/processes/search";
	    HttpHeaders headers = new HttpHeaders();
	    headers.setContentType(MediaType.APPLICATION_JSON);

	    ResponseEntity<ProcessData> response = restTemplateByPassSSL.exchange(url, HttpMethod.GET,
	            service.getRequestEntity(url, headers, username, password),
	            new ParameterizedTypeReference<ProcessData>() {
	            });

	    if (response.getStatusCode().is2xxSuccessful() && Objects.nonNull(response.getBody())
	            && Objects.nonNull(response.getBody().getData())) {
	        List<Process> processes = response.getBody().getData().getProcesses();
	        if (!processes.isEmpty()) {
	            return Optional.of(processes);
	        }
	    }

	    return Optional.empty();
	}

//	public Optional<List<Process>> processPiid() throws JsonMappingException, JsonProcessingException, KeyManagementException, NoSuchAlgorithmException {
//	    SSLUtil.turnOffSslChecking(); // Disable SSL certificate checking
//	    RestTemplate rest = new RestTemplate();
//	    try {
//	        String url = baseUrl + "/processes/search";
//	        HttpHeaders headers = new HttpHeaders();
//	        headers.setContentType(MediaType.APPLICATION_JSON);
//
//	        ResponseEntity<ProcessData> response = rest.exchange(url, HttpMethod.GET,
//	                service.getRequestEntity(url, headers, username, password),
//	                new ParameterizedTypeReference<ProcessData>() {
//	                });
//
//	        if (response.getStatusCode().is2xxSuccessful() && Objects.nonNull(response.getBody())
//	                && Objects.nonNull(response.getBody().getData())) {
//	            List<Process> processes = response.getBody().getData().getProcesses();
//	            if (!processes.isEmpty()) {
//	                return Optional.of(processes);
//	            }
//	        }
//
//	        return Optional.empty();
//	    } finally {
//	    	System.out.println("Heloo");
//	        SSLUtil.turnOnSslChecking(); // Re-enable SSL certificate checking
//	    }
//	}

	


	@GetMapping(value = "/process/{piid}", produces = MediaType.APPLICATION_JSON_VALUE)
	public Process getProcessByPiid(@PathVariable("piid") String piid) throws IOException, KeyManagementException, NoSuchAlgorithmException {
		Optional<List<Process>> response = processPiid();

		if (response.isPresent()) {
			List<Process> processes = response.get();

			Optional<Process> matchingProcess = processes.stream()
					.filter(process -> piid.equalsIgnoreCase(process.getPiid())).findFirst();

			return matchingProcess.orElse(new Process());
		}

		return new Process();
	}

	@GetMapping("/processModelImage/{piid}")
	public ResponseEntity<ByteArrayResource> getProcessModelImageByPiid(@PathVariable("piid") String piid)
			throws IOException {
		String url = baseUrl + "/visual/processModel/instances?instanceIds=[" + piid + "]&image=true";
		headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
		ResponseEntity<byte[]> response = restTemplateByPassSSL.exchange(url, HttpMethod.GET,
				service.getRequestEntity(url, headers, username, password), byte[].class);

		byte[] imageData = response.getBody();

		if (imageData != null) {
			Optional<byte[]> pdfBytesOptional = service.convertImageToPdf(imageData);

			if (pdfBytesOptional.isPresent()) {
				byte[] pdfBytes = pdfBytesOptional.get();
				ByteArrayResource resource = new ByteArrayResource(pdfBytes);
				headers.setContentType(MediaType.APPLICATION_PDF);
				headers.setContentDispositionFormData("attachment", piid + ".pdf");

				return ResponseEntity.ok().headers(headers).contentLength(pdfBytes.length).body(resource);
			}
		}

		return ResponseEntity.noContent().build();
	}

	@GetMapping(value = "/base64ProcessModelImage/{piid}",produces = MediaType.APPLICATION_JSON_VALUE)
	public Optional<String> getBase64ProcessModelImageByPiid(@PathVariable("piid") String piid) throws IOException {
		String url = baseUrl + "/visual/processModel/instances?instanceIds=[" + piid + "]&image=true";
		headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
		ResponseEntity<byte[]> response = restTemplateByPassSSL.exchange(url, HttpMethod.GET,
				service.getRequestEntity(url, headers, username, password), byte[].class);

		if (response.getStatusCode().is2xxSuccessful() && Objects.nonNull( response.getBody())) {
			byte[] imageData = response.getBody();
			Optional<byte[]> pdfBytes = service.convertImageToPdf(imageData);

			if (pdfBytes.isPresent()) {
				return Optional.ofNullable(Base64.getEncoder().encodeToString(pdfBytes.get()));
			} else {
				return Optional.empty();
			}
		} else {
			return Optional.empty();
		}

	}

}