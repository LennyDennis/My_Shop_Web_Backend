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
public final class ConstantVariables {

    //roles
    public static final int ADMIN_ROLE = 1;
    public static final int EMPLOYEE_ROLE = 2;
    public static final int CUSTOMER_ROLE = 3;

    //deletion status
    public static final int DELETED = 1;
    public static final int NOT_DELETED = 0;

    //response code status
    public static final int SUCCESS_CODE = 200;
    public static final int ERROR_CODE = 500;
    
    //active status 
    public static final int ACTIVATE = 1;
    public static final int DEACTIVATE = 0;

    //restock status
    public static final int RESTOCK_ON = 1;
    public static final int RESTOCK_OFF = 0;

    //response message 
    public static final String ERROR_MESSAGE = "An error has occured";

}
