package com.example.voucherservice.dto;

public class TopUpResponse {
    private boolean status;
    private String code;
    private Dialog dialog;
    private String actions;
    private String extra;
    private boolean retrieved;
    private String data;

    // Getters and Setters
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

    public Dialog getDialog() {
        return dialog;
    }

    public void setDialog(Dialog dialog) {
        this.dialog = dialog;
    }

    public String getActions() {
        return actions;
    }

    public void setActions(String actions) {
        this.actions = actions;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    public boolean isRetrieved() {
        return retrieved;
    }

    public void setRetrieved(boolean retrieved) {
        this.retrieved = retrieved;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "TopUpResponse [status=" + status + ", code=" + code + ", dialog=" + dialog + ", actions=" + actions
                + ", extra=" + extra + ", retrieved=" + retrieved + ", data=" + data + "]";
    }

    // Inner Dialog class
    public static class Dialog {
        private String title;
        private String message;

        // Getters and Setters
        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        @Override
        public String toString() {
            return "Dialog [title=" + title + ", message=" + message + "]";
        }
    }
}
