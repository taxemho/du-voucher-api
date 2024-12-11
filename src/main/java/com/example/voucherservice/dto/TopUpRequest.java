package com.example.voucherservice.dto;

import javax.validation.constraints.NotNull;

public class TopUpRequest {

    private double amount;
    private long externalId;
    private String id; // Always "40000" for voucher top-up
    private String msisdn;
    private String countryCode;

    // Constructor with @NotNull annotations
    public TopUpRequest(@NotNull Long externalId, @NotNull String id, @NotNull String countryCode,
                        @NotNull String msisdn, @NotNull Double amount) {
        this.externalId = externalId;
        this.id = id;
        this.countryCode = countryCode;
        this.msisdn = msisdn;
        this.amount = amount;
    }

    // Default constructor
    public TopUpRequest() {
    }

    // Fluent setters
    public TopUpRequest setAmount(double amount) {
        this.amount = amount;
        return this;
    }

    public TopUpRequest setExternalId(long externalId) {
        this.externalId = externalId;
        return this;
    }

    public TopUpRequest setId(String id) {
        this.id = id;
        return this;
    }

    public TopUpRequest setMsisdn(String msisdn) {
        this.msisdn = msisdn;
        return this;
    }

    public TopUpRequest setCountryCode(String countryCode) {
        this.countryCode = countryCode;
        return this;
    }

    // Getters
    public double getAmount() {
        return amount;
    }

    public long getExternalId() {
        return externalId;
    }

    public String getId() {
        return id;
    }

    public String getMsisdn() {
        return msisdn;
    }

    public String getCountryCode() {
        return countryCode;
    }

    // Override toString for better logging/debugging
    @Override
    public String toString() {
        return "TopUpRequest{" +
                "externalId=" + externalId +
                ", id='" + id + '\'' +
                ", countryCode='" + countryCode + '\'' +
                ", msisdn='" + msisdn + '\'' +
                ", amount=" + amount +
                '}';
    }
}
