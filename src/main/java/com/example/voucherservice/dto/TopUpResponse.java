package com.example.voucherservice.dto;

public class TopUpResponse {
    private boolean status;
    private String code;
    private Dialog dialog;
    private Object actions;
    private Object extra;
    private Object data;

    public static class Dialog {
        private String title;
        private String message;

        // Getters and Setters
        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }

        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
    }

    // Getters and Setters
    public boolean isStatus() { return status; }
    public void setStatus(boolean status) { this.status = status; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public Dialog getDialog() { return dialog; }
    public void setDialog(Dialog dialog) { this.dialog = dialog; }

    public Object getActions() { return actions; }
    public void setActions(Object actions) { this.actions = actions; }

    public Object getExtra() { return extra; }
    public void setExtra(Object extra) { this.extra = extra; }

    public Object getData() { return data; }
    public void setData(Object data) { this.data = data; }
}
