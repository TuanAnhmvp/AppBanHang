package com.tashop.appbanhang.model;

import java.util.List;

public class LoaiSpModel {
    boolean success;
    String message;
    List<LoaiSP> result;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<LoaiSP> getResult() {
        return result;
    }

    public void setResult(List<LoaiSP> result) {
        this.result = result;
    }
}
