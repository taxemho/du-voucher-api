package com.example.voucherservice.dto;

public class TopUpRequest {
    private long externalId;
    private String id; // Always "40000" for voucher top-up
    private String countryCode;
    private String msisdn;
    private double amount;

    // Getters and Setters
    public long getExternalId() { return externalId; }
    public void setExternalId(long externalId) { this.externalId = externalId; }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getCountryCode() { return countryCode; }
    public void setCountryCode(String countryCode) { this.countryCode = countryCode; }

    public String getMsisdn() { return msisdn; }
    public void setMsisdn(String msisdn) { this.msisdn = msisdn; }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }
}
