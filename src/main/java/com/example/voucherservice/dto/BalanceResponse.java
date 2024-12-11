package com.example.voucherservice.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "BalanceResponse")
@XmlAccessorType(XmlAccessType.FIELD)
public class BalanceResponse {

    @XmlElement(name = "status", nillable = true, required = true)
    private boolean status;

    @XmlElement(name = "code", nillable = true, required = true)
    private String code;

    @XmlElement(name = "dialog", nillable = true, required = true)
    private String dialog;

    @XmlElement(name = "data", nillable = true, required = true)
    private Data data;

    @XmlAccessorType(XmlAccessType.FIELD)
    public static class Data {

        @XmlElement(name = "balanceDetails", nillable = true, required = true)
        private BalanceDetails balanceDetails;

        @XmlAccessorType(XmlAccessType.FIELD)
        public static class BalanceDetails {

            @XmlElement(name = "balance", nillable = true, required = true)
            private double balance;

            public double getBalance() {
                return balance;
            }

            public void setBalance(double balance) {
                this.balance = balance;
            }
        }

        public BalanceDetails getBalanceDetails() {
            return balanceDetails;
        }

        public void setBalanceDetails(BalanceDetails balanceDetails) {
            this.balanceDetails = balanceDetails;
        }
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDialog() {
        return dialog;
    }

    public void setDialog(String dialog) {
        this.dialog = dialog;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "BalanceResponse [status=" + status + ", code=" + code + ", dialog=" + dialog + ", data=" + data + "]";
    }
}
