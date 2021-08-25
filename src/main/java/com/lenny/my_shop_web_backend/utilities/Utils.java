/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lenny.my_shop_web_backend.utilities;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;

/**
 *
 * @author lenny
 */
public class Utils {

    @Context
    private static HttpServletRequest httpRequest;

    public static String getUrlPrefix() {
        String hostname = httpRequest.getServerName();
        Integer port_number = httpRequest.getServerPort();
        String context_path = httpRequest.getContextPath();
        String port;

        List<Integer> ignored_ports = new ArrayList();
        ignored_ports.add(80);
        ignored_ports.add(8080);
        ignored_ports.add(443);

        if (ignored_ports.contains(port_number)) {
            port = "";
        } else {
            port = ":" + String.valueOf(port_number);
        }

        String url_prefix = "https://" + hostname + port + context_path + "/";
        return url_prefix;
    }

    public static String assignTwoDecimal(float floatNumber) {
        String stringNumber = String.format("%.2f", floatNumber);
        return stringNumber;
    }

    public static String getDateFromMilliSeconds(Date date){
        Date milliSeconds = date;
        Date currentDate = new Date(milliSeconds.getTime());
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        return  df.format(currentDate);
    }

    public static String getToday() {
        Date currentDate = new Date();
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        return df.format(currentDate);
    }

    public static String getCurrentYear() {
        String date = "1/1"+"/"+Calendar.getInstance().get(Calendar.YEAR);
        return date;
    }

    public static String getCurrentMonth() {
        String date = "1/"+(Calendar.getInstance().get(Calendar.MONTH)+1)+"/"+Calendar.getInstance().get(Calendar.YEAR);
        return date;
    }

}
