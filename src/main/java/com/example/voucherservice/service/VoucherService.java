package com.example.voucherservice.service;

import com.example.voucherservice.dto.BalanceResponse;
import com.example.voucherservice.dto.TopUpRequest;
import com.example.voucherservice.dto.TopUpResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class VoucherService {

    private final RestTemplate restTemplate;

    @Value("${whish.api.production-base-url}")
    private String productionBaseUrl;

    @Value("${whish.api.sandbox-base-url}")
    private String sandboxBaseUrl;

    public VoucherService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    private HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("sessionId", "10193800"); // Replace with actual sessionId
        headers.set("token", "d6029c45540040f4aa60fe89a65efb05");         // Replace with actual token
        headers.set("language", "en");
        return headers;
    }

    public BalanceResponse getAccountBalance() {
        String url = productionBaseUrl + "/account/balance";
        HttpEntity<Void> entity = new HttpEntity<>(createHeaders());

        ResponseEntity<BalanceResponse> response = restTemplate.exchange(
                url, HttpMethod.GET, entity, BalanceResponse.class);

        return response.getBody();
    }

    public TopUpResponse performTopUp(TopUpRequest request) {
        String url = sandboxBaseUrl + "/transfer/airtime";
        HttpEntity<TopUpRequest> entity = new HttpEntity<>(request, createHeaders());

        ResponseEntity<TopUpResponse> response = restTemplate.exchange(
                url, HttpMethod.POST, entity, TopUpResponse.class);

        return response.getBody();
    }
}
