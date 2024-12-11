package com.example.voucherservice.helper;

import java.sql.Types;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import org.apache.cxf.jaxrs.client.WebClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Service;

import com.corereqbeans.BillPayRequestForAll;
import com.corereqbeans.BillPayResponseForAll;
import com.corereqbeans.Corerequest;
import com.example.voucherservice.dto.BalanceResponse;

@Service
public class VoucherAsyncProcessor {

    @Value("${hub.callback.url}")
    private String hubUrl;

    @Value("${service.wallet.key}")
    private String key;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final Logger log = LoggerFactory.getLogger(VoucherAsyncProcessor.class);

    public void processAsyncResponse(BillPayRequestForAll request, BillPayResponseForAll response) {
        log.info("Processing response for Voucher asynchronously...");

        BalanceResponse callbackResponse = null;

        try {
            callbackResponse = prepareCallbackResponse(request, response);

            if (request.getObj() instanceof Corerequest corereq) {
                revertServiceWallet(corereq, key);
            } else {
                log.error("Invalid or null object in request.getObj(). Expected type: Corerequest");
                throw new IllegalArgumentException("Invalid object type in request.getObj()");
            }
        } catch (Exception e) {
            log.error("Error while processing response asynchronously: {}", e.getMessage(), e);
        } finally {
            sendResponseToHub(callbackResponse);
        }
    }

    public void sendResponseToHub(BalanceResponse callbackResponse) {
        try {
            log.info("Sending callback response to HUB: {}", callbackResponse);

            WebClient wc = WebClient.create(hubUrl);

            WebClient.getConfig(wc).getHttpConduit().getClient().setConnectionTimeout(200000L);
            WebClient.getConfig(wc).getHttpConduit().getClient().setReceiveTimeout(200000L);

            wc.post(callbackResponse, Void.class);
        } catch (Exception e) {
            log.error("Error while sending response to HUB: {}", e.getMessage(), e);
        }
    }

    public static CompletableFuture<Void> sendAsyncRequest(String payload, String url) {
        return CompletableFuture.runAsync(() -> {
            try {
                log.info("Sending asynchronous request to URL: {} | Payload: {}", url, payload);

                WebClient wc = WebClient.create(url);

                WebClient.getConfig(wc).getHttpConduit().getClient().setConnectionTimeout(200000L);
                WebClient.getConfig(wc).getHttpConduit().getClient().setReceiveTimeout(200000L);

                wc.post(payload, Void.class);
            } catch (Exception e) {
                log.error("Error while sending asynchronous request: {}", e.getMessage(), e);
            }
        });
    }

    public BalanceResponse prepareCallbackResponse(BillPayRequestForAll request, BillPayResponseForAll response) {
        BalanceResponse callbackResponse = new BalanceResponse();
        Corerequest corereq = (Corerequest) request.getObj();
        try {
            String status = "FAILED";
            if ("PYRO1000".equalsIgnoreCase(response.getResult())) {
                status = "COMPLETED";
            }

            callbackResponse.setTransactionId(request.getTransId());
            callbackResponse.setStatus(status);
            callbackResponse.setAmount(request.getAmount());
            callbackResponse.setDescription(response.getDescription());
            callbackResponse.setOperatorId(response.getOperatorrefno());
            callbackResponse.setDestMsisdn(request.getSubscriberno());
            callbackResponse.setSrcMsisdn(corereq.getSrcmsisdn());

            log.info("Prepared callback response: {}", callbackResponse);
        } catch (Exception e) {
            log.error("Error while preparing callback response: {}", e.getMessage(), e);
        }
        return callbackResponse;
    }

    public void revertServiceWallet(Corerequest corereq, String key) {
        log.info("Reverting wallet transaction for Corerequest: {}", corereq);

        try {
            SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate)
                    .withSchemaName("ch_mgmt")
                    .withProcedureName("SP_SERVICEWALLET_UPD")
                    .declareParameters(
                            new SqlOutParameter("P_CODE", Types.INTEGER),
                            new SqlOutParameter("P_DESC", Types.VARCHAR),
                            new SqlOutParameter("P_BEFOREBAL", Types.INTEGER),
                            new SqlOutParameter("P_AFTERBAL", Types.INTEGER)
                    );

            Map<String, Object> inParams = new HashMap<>();
            inParams.put("P_SPID", corereq.getSpid());
            inParams.put("P_AMOUNT", corereq.getAmount());
            inParams.put("P_KEY", key);
            inParams.put("P_TYPE", 1);
            inParams.put("P_TRID", corereq.getRequesttrid());

            log.info("Input Parameters for wallet reversal: {}", inParams);

            Map<String, Object> outParams = simpleJdbcCall.execute(inParams);

            log.info("Wallet reversal procedure executed successfully. Output: {}", outParams);
        } catch (Exception e) {
            log.error("Error while reverting wallet transaction: {}", e.getMessage(), e);
        }
    }
}