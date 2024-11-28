package com.example.voucherservice.controller;

import com.example.voucherservice.dto.BalanceResponse;
import com.example.voucherservice.dto.TopUpRequest;
import com.example.voucherservice.dto.TopUpResponse;
import com.example.voucherservice.service.VoucherService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/voucher")
public class VoucherController {

    private final VoucherService voucherService;

    public VoucherController(VoucherService voucherService) {
        this.voucherService = voucherService;
    }

    @GetMapping("/balance")
    public BalanceResponse getAccountBalance() {
        return voucherService.getAccountBalance();
    }

    @PostMapping("/topup")
    public TopUpResponse performTopUp(@RequestBody TopUpRequest request) {
        return voucherService.performTopUp(request);
    }
}
