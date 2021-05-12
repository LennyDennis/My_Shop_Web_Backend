/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lenny.my_shop_web_backend.ejb;

import com.google.common.base.Strings;
import com.lenny.my_shop_web_backend.ejb_db.User_DbBean;
import com.lenny.my_shop_web_backend.entities.User;
import com.lenny.my_shop_web_backend.jpa.TransactionProvider;
import com.lenny.my_shop_web_backend.utilities.ConstantVariables;
import com.lenny.my_shop_web_backend.utilities.JsonResponse;
import com.lenny.my_shop_web_backend.utilities.Utils;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 *
 * @author lenny
 */
@Stateless
public class UserBean {

    //registration status
    public static final int ACCOUNT_ACTIVATED = 1;
    public static final int ACCOUNT_NOT_ACTIVATED = 0;

    @EJB
    TransactionProvider provider;

    @EJB
    private User_DbBean userDbBean;

    @EJB
    private MailBean mailBean;

    @Context
    private HttpServletRequest httpRequest;

    public JsonResponse registerUser(User user) {
        JsonResponse response = new JsonResponse(500, "An error occured");
        Date currentDate = new Date();
        try {
            if (user != null) {
                Integer role = user.getRole();
                if (role == ConstantVariables.ADMIN_ROLE || role == ConstantVariables.EMPLOYEE_ROLE) {
                    User userExist = userDbBean.getUser_ByEmail(user.getEmail());
                    if (userExist == null) {
                        String email = user.getEmail();
                        String at_symbol = "@";
                        String dot = ".";

                        if (Strings.isNullOrEmpty(email) || !email.contains(at_symbol) || !email.contains(dot) || email.lastIndexOf(dot) < email.lastIndexOf(at_symbol)) {
                            response.setMessage("Please specify a valid email");
                        } else {
                            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
                            String encodedPassword = encoder.encode(user.getPassword());
                            user.setPassword(encodedPassword);
                            user.setRegisteredDate(currentDate);
                            UUID authKey = UUID.randomUUID();
                            user.setAuthKey(authKey.toString());
                            user.setRegistrationStatus(ACCOUNT_NOT_ACTIVATED);
                            user.setDeletionStatus(ConstantVariables.NOT_DELETED);
                            if (role == ConstantVariables.ADMIN_ROLE) {
                                user.setRole(ConstantVariables.ADMIN_ROLE);
                            } else {
                                user.setRole(ConstantVariables.EMPLOYEE_ROLE);
                            }

                            if (provider.createEntity(user)) {
                                mailBean.sendActivationEmail(this.getUrlPrefix(), user.getEmail(), user.getName(), user.getAuthKey());
                                response.setMessage("Check your email to activate your account");
                                response.setResponseCode(200);
                            }
                        }
                    } else {
                        response.setMessage("This email is already registered under another user");
                    }
                }
            } else {
                response.setMessage("User is empty. Try Again");
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return response;
        }
    }

    public JsonResponse activateAccount(String authKey) {
        JsonResponse response = new JsonResponse(500, "An error has occured");
        try {
            if (authKey != null) {
                User userExists = userDbBean.getUser_ByAuthKey(authKey);
                if (userExists != null) {
                    if (userExists.getRegistrationStatus() != ACCOUNT_ACTIVATED) {
                        userExists.setRegistrationStatus(ACCOUNT_ACTIVATED);
                        if (provider.updateEntity(userExists)) {
                            response.setResponseCode(200);
                            response.setMessage("Account activated");
                        }
                    } else {
                        response.setResponseCode(200);
                        response.setMessage("Account already been activated");
                    }
                } else {
                    response.setMessage("User does not exists.");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return response;
        }
    }

    public String getUrlPrefix() {
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
}
