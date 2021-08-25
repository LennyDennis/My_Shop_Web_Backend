/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lenny.my_shop_web_backend.utilities;

import com.google.common.base.Strings;
import sun.security.util.Password;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import static com.lenny.my_shop_web_backend.utilities.Credentials.EMAIL_PASSWORD;

/**
 *
 * @author lenny
 */

public class Mail {

    public boolean sendGmail(String to, String subject, String msg, String reply_email) {
        String at_symbol = "@";
        String dot = ".";

        if (!Strings.isNullOrEmpty(to) && to.contains(at_symbol) && to.contains(dot) && to.lastIndexOf(dot) > to.lastIndexOf(at_symbol)) {
            System.out.println("SENDING EMAIL TO: " + to);
            boolean res = false;

            final String username = "lennydennisdev@gmail.com";
            final String password = EMAIL_PASSWORD;

            Properties props = new Properties();
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.host", "smtp.gmail.com");
            props.put("mail.smtp.port", "587");
            props.put("mail.smtp.auth", "true");

            Session session = Session.getInstance(props, new javax.mail.Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(username, password);
                }
            });

            try {
                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress("lennydennisdev@gmail.com"));

//                if (!Strings.isNullOrEmpty(reply_email)) {
//                    InternetAddress[] addresses = InternetAddress.parse(reply_email);
//
//                    if (addresses != null && addresses.length > 0) {
//                        message.setReplyTo(addresses);
//                        message.setFrom(addresses[0]);
//                    }
//                }
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
                message.setSubject(subject);
                message.setContent(msg, "text/html");

                Transport.send(message);
                res = true;
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                return res;
            }
        } else {
            return false;
        }
    }
}
