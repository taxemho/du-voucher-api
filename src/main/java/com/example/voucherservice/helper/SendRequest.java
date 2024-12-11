package com.example.voucherservice.helper;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.example.voucherservice.exception.CustomVoucherException;

@Component
public class SendRequest {

    private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(SendRequest.class);

    @Value("${voucher.api.base-url}")
    private String baseUrl;

    @Value("${voucher.api.token}")
    private String token;

    @Value("${voucher.api.sessionId}")
    private String sessionId;

    private final RestTemplate restTemplate;

    public SendRequest(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public <T> T callApi(String endpoint, Object requestBody, Class<T> responseType) {
        try {
            String url = baseUrl + endpoint;

            HttpHeaders headers = new HttpHeaders();
            headers.set("sessionId", sessionId);
            headers.set("token", token);
            headers.set("language", "en");
            headers.set("Content-Type", "application/json");

            HttpEntity<Object> entity = new HttpEntity<>(requestBody, headers);

            ResponseEntity<T> response = restTemplate.exchange(url, HttpMethod.POST, entity, responseType);

            if (!response.getStatusCode().is2xxSuccessful()) {
                throw new CustomVoucherException("API call failed with status: " + response.getStatusCode(), false);
            }

            return response.getBody();
        } catch (Exception ex) {
            log.error("Failed to call API: " + ex.getMessage(), ex);
            throw new CustomVoucherException("Failed to call API: " + ex.getMessage(), false);
        }
    }
}
