package com.example.voucherservice.dto;

public class BalanceResponse {
    private boolean status;
    private String code;
    private String dialog;
    private Data data;

    public static class Data {
        private BalanceDetails balanceDetails;

        public static class BalanceDetails {
            private double balance;

            // Getters and Setters
            public double getBalance() { return balance; }
            public void setBalance(double balance) { this.balance = balance; }
        }

        // Getters and Setters
        public BalanceDetails getBalanceDetails() { return balanceDetails; }
        public void setBalanceDetails(BalanceDetails balanceDetails) { this.balanceDetails = balanceDetails; }
    }

    // Getters and Setters
    public boolean isStatus() { return status; }
    public void setStatus(boolean status) { this.status = status; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public String getDialog() { return dialog; }
    public void setDialog(String dialog) { this.dialog = dialog; }

    public Data getData() { return data; }
    public void setData(Data data) { this.data = data; }
}
