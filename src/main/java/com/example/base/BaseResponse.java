package com.example.base;

public class BaseResponse {
    private String errorCode;
    private String errorMessage;
    private String hintMessage;

    public BaseResponse() {
        this.errorCode = "0000";
        this.errorMessage = "OK";
        this.hintMessage = "";
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getHintMessage() {
        return hintMessage;
    }

    public void setHintMessage(String hintMessage) {
        this.hintMessage = hintMessage;
    }
}
