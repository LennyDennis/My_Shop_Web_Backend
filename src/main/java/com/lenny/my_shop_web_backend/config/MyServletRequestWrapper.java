/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lenny.my_shop_web_backend.config;

/**
 *
 * @author lenny
 */
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.util.*;

public class MyServletRequestWrapper extends HttpServletRequestWrapper {

    private Map headerMap;

    public void addHeader(String name, String value) {
        headerMap.put(name, value);
    }

    public MyServletRequestWrapper(HttpServletRequest request) {
        super(request);
        headerMap = new HashMap();
    }

    @Override
    public Enumeration getHeaderNames() {
        HttpServletRequest request = (HttpServletRequest) getRequest();
        List list = new ArrayList();
        for (Enumeration e = request.getHeaderNames(); e.hasMoreElements();) {
            String text = e.nextElement().toString();
            if (!text.contains("Cache-Control")) {
                list.add(text);
            }
        }
        for (Iterator i = headerMap.keySet().iterator(); i.hasNext();) {
            list.add(i.next());
        }
        return Collections.enumeration(list);
    }

    @Override
    public String getHeader(String name) {
        Object value;
        if ((value = headerMap.get("" + name)) != null) {
            return value.toString();
        } else {
            return ((HttpServletRequest) getRequest()).getHeader(name);
        }
    }
}
