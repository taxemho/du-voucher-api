package com.example.voucherservice.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.corereqbeans.BillPayRequestForAll;
import com.corereqbeans.BillPayResponseForAll;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.example.voucherservice.exception.CustomVoucherException;
import com.example.voucherservice.helper.VoucherAsyncProcessor;
import com.example.voucherservice.helper.SendRequest;
import com.example.voucherservice.dto.TopUpRequest;
import com.example.voucherservice.dto.TopUpResponse;
import com.example.voucherservice.repository.VoucherRepository;

@Service
public abstract class VoucherServiceImpl implements VoucherService {

    private static Logger log = LoggerFactory.getLogger(VoucherServiceImpl.class);

    @Autowired
    private VoucherRepository repository;

    @Autowired
    private SendRequest sendRequest;

    @Value("${country.code}")
    private String countryCode;

    @Value("${denomination.id}")
    private String denominationId;

    @Value("${voucher.airtime.url}")
    private String airtimeURL;

    @Value("${voucher.status.url}")
    private String statusURL;

    private final ObjectMapper mapper = new ObjectMapper();
    private final Gson gson = new Gson();

    @Override
    public void topup(BillPayRequestForAll billreq, String reqString) {
        BillPayResponseForAll response = new BillPayResponseForAll();

        if (billreq == null) {
            log.warn("Received null BillPayRequestForAll object.");
            setResponse(response, "01", "Request is null", "FAILED", "PYRO2000", null, null);
            sendAsyncResponse(billreq, response);
            return;
        }

        try {
            // Step 1: Frame the request
        	TopUpRequest topupRequest = frameRequest(billreq);
            if (topupRequest == null) {
                log.error("Failed to frame the TopupRequest from BillPayRequestForAll.");
                setResponse(response, "01", "Failed to frame the TopupRequest", "FAILED", "PYRO2000", null, null);
                sendAsyncResponse(billreq, response);
                return;
            }

            log.info("Topup API Request: {}", topupRequest);

            // Step 2: Perform the top-up
            TopUpResponse topupResponse = performTopup(topupRequest);
            log.info("Topup API Response: {}", topupResponse);

            // Step 3: Check transaction status if required
            if (shouldCheckTransactionStatus(topupResponse)) {
            	TopUpResponse statusResponse = checkTransactionStatus(topupRequest);
                log.info("Transaction Status Response: {}", statusResponse);

                // Handle unknown transaction status
                if (isUnknownTransactionStatus(statusResponse)) {
                    log.error("Unknown transaction status. Please contact support.");
                    setResponse(response, "02", "Transaction is Pending", "PENDING", "PYRO1050", billreq.getTransId(), null);
                } else {
                    // Success scenario
                    setResponse(response, "00", "Transaction Successful", "SUCCESS", "PYRO1000", billreq.getTransId(), billreq.getTransId());
                }
            } else {
                setResponse(response, "01", "Top-up failed", "FAILED", "PYRO2000", billreq.getTransId(), null);
            }
        } catch (Exception e) {
            log.error("Unexpected error during the top-up process: {}", e.getMessage(), e);
            setResponse(response, "01", "Internal error", "FAILED", "PYRO2000", billreq.getTransId(), null);
        } finally {
            sendAsyncResponse(billreq, response);
        }
    }

    private TopUpRequest frameRequest(BillPayRequestForAll billreq) {
        if (!validateRequest(billreq)) {
            log.error("Validation failed for BillPayRequestForAll: {}", billreq);
            return null;
        }

        return new TopUpRequest()
                .setAmount(Double.parseDouble(billreq.getAmount()))
                .setExternalId(Long.parseLong(billreq.getTransId()))
                .setId(denominationId)
                .setMsisdn("+971" + billreq.getSubscriberno())
                .setCountryCode(countryCode);
    }

    private boolean validateRequest(BillPayRequestForAll billreq) {
        if (billreq.getSubscriberno() == null || billreq.getSubscriberno().isEmpty()) {
            log.warn("Subscriber number is invalid.");
            return false;
        }
        if (billreq.getAmount() == null || billreq.getAmount().isEmpty()) {
            log.warn("Amount is invalid.");
            return false;
        }
        return true;
    }

    private TopUpResponse performTopup(TopUpRequest request) {
        try {
            return sendRequest.callApi(airtimeURL, request, TopUpResponse.class);
        } catch (Exception e) {
            log.error("Error occurred while performing the top-up: {}", e.getMessage(), e);
            throw new CustomVoucherException("Failed to perform top-up. Please try again later.", false);
        }
    }

    private TopUpResponse checkTransactionStatus(TopUpRequest request) {
        try {
            return sendRequest.callApi(statusURL, request, TopUpResponse.class);
        } catch (Exception e) {
            log.error("Error occurred while checking transaction status: {}", e.getMessage(), e);
            throw new CustomVoucherException("Failed to check transaction status. Please try again later.", false);
        }
    }

    private void setResponse(BillPayResponseForAll response, String responseCode, String description, String error,
                              String result, String transactionId, String operatorRefNo) {
        response.setResponsecode(responseCode);
        response.setDescription(description);
        response.setError(error);
        response.setResult(result);
        response.setRes_trid(transactionId);
        response.setOperatorrefno(operatorRefNo);
    }

    private boolean shouldCheckTransactionStatus(TopUpResponse response) {
        return response != null && response.isStatus();
    }

    private boolean isUnknownTransactionStatus(TopUpResponse response) {
        return response != null && response.isStatus() && "unknown".equalsIgnoreCase(response.getCode());
    }

    private void sendAsyncResponse(BillPayRequestForAll request, BillPayResponseForAll response) {
        new VoucherAsyncProcessor().sendAsyncResponse(request, response);
    }
}
