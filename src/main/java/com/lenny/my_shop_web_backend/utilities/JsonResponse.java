/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lenny.my_shop_web_backend.utilities;

/**
 *
 * @author lenny
 */
public class JsonResponse {
    
    private Integer responseCode;
    private String message;

    public JsonResponse() {
    }

    public JsonResponse(Integer code, String message) {
        this.responseCode = code;
        this.message = message;
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
    
}
