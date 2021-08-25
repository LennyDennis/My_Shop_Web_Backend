/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lenny.my_shop_web_backend.utilities;

import java.util.HashMap;

/**
 *
 * @author lenny
 */
public class JsonResponse {
    
    private Integer responseCode;
    private String message;
    private HashMap data;

    public JsonResponse() {
    }

    public JsonResponse(Integer code, String message) {
        this.responseCode = code;
        this.message = message;
    }

    public JsonResponse(Integer responseCode, String message, HashMap data) {
        this.responseCode = responseCode;
        this.message = message;
        this.data = data;
    }

    public Integer getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(Integer responseCode) {
        this.responseCode = responseCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public HashMap getData() {
        return data;
    }

    public void setData(HashMap data) {
        this.data = data;
    }
    
}
