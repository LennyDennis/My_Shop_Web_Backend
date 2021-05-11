/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lenny.my_shop_web_backend.config;

import com.google.common.base.Strings;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author lenny
 */
public class NoCachingFilter implements Filter {

    private FilterConfig filterConfig = null;

    @Override
    public void init(FilterConfig filterConfig) {
        this.filterConfig = filterConfig;
    }

    @Override
    public void destroy() {
        this.filterConfig = null;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        try {
            MyServletRequestWrapper httpRequest = new MyServletRequestWrapper((HttpServletRequest) request);
            HttpServletResponse httpResponse = (HttpServletResponse) response;
            String url = httpRequest.getRequestURI();

            if (!Strings.isNullOrEmpty(url)) {
                url = url.trim();
                List<String> no_cache_suffixes = new ArrayList();
                no_cache_suffixes.add("/");
                no_cache_suffixes.add("/l");
                no_cache_suffixes.add("/l/");

                no_cache_suffixes.add("/index.html");
                no_cache_suffixes.add("/home.html");

                no_cache_suffixes.add("/l/index.html");
                no_cache_suffixes.add("/l/home.html");

                String url_without_last_slash = null;
                if (url.endsWith("/")) {
                    url_without_last_slash = url.substring(0, url.length() - 1);
                }

                String context_path = "";
                if (httpRequest.getContextPath() != null) {
                    context_path = httpRequest.getContextPath().trim();
                }

                if (url.isEmpty() || no_cache_suffixes.contains(url) || (url_without_last_slash != null && url_without_last_slash.equals(context_path))) {
                    httpRequest.addHeader("Cache-Control", "no-cache, no-store, must-revalidate");
                    httpRequest.addHeader("Cache-control", "no-cache, no-store, must-revalidate");
                    httpRequest.addHeader("Pragma", "no-cache");

                    httpResponse.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
                    httpResponse.setDateHeader("Expires", 0);
                    httpResponse.setHeader("Pragma", "no-cache");

                    //System.out.println("no cache set: " + httpRequest.getRequestURI());
                }
            }

            chain.doFilter(httpRequest, httpResponse);
        } catch (IOException ioe) {
            System.out.println("doFilter IOException");
            chain.doFilter(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            chain.doFilter(request, response);
        }
    }
}
