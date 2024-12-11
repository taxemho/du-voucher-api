package com.example.voucherservice.controller;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.voucherservice.dto.BalanceResponse;
import com.example.voucherservice.dto.TopUpRequest;
import com.example.voucherservice.service.VoucherService;

@RestController
@RequestMapping("/api/voucher")
public class VoucherController {

    private static final ExecutorService executorService = Executors.newCachedThreadPool();
    private static final Logger log = LoggerFactory.getLogger(VoucherController.class);

    @Autowired
    private VoucherService voucherService;

    @PostMapping("/topup")
    public ResponseEntity<String> performTopUp(@RequestBody TopUpRequest request) {
        log.info("New request for Voucher TopUp: {}", request);

        // Execute top-up operation asynchronously
        executorService.submit(() -> {
            voucherService.performTopUp(request);
        });

        return ResponseEntity.ok("Request Received.");
    }

    @GetMapping("/balance")
    public ResponseEntity<BalanceResponse> getAccountBalance() {
        log.info("Fetching account balance in controller.");
        BalanceResponse response = voucherService.getAccountBalance();
        return ResponseEntity.ok(response);
    }
}
