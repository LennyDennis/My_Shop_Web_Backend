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

import java.util.*;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.PersistenceException;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static com.lenny.my_shop_web_backend.utilities.ConstantVariables.CUSTOMER_ROLE;

/**
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

    public Response registerUser(User user) {
        try {
            if (user == null) {
                throw new BadRequestException("User is empty");
            }
            int userRole = user.getRole();
            HashMap<String, Object> res = new HashMap<>();
            Date currentDate = new Date();
            if (userRole == ConstantVariables.ADMIN_ROLE || userRole == ConstantVariables.EMPLOYEE_ROLE) {
                User userExist = userDbBean.getUser_ByEmail(user.getEmail());
                if (userExist != null) {
                    throw new BadRequestException("This email is already registered under another user");
                }
                String email = user.getEmail();
                String at_symbol = "@";
                String dot = ".";
                boolean isValidEmail = Strings.isNullOrEmpty(email) || !email.contains(at_symbol) || !email.contains(dot) || email.lastIndexOf(dot) < email.lastIndexOf(at_symbol);
                if (isValidEmail) {
                    throw new BadRequestException("Please specify a valid email");
                }
                BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
                String encodedPassword = encoder.encode(user.getPassword());
                user.setPassword(encodedPassword);
                user.setRegisteredDate(currentDate);
                UUID authKey = UUID.randomUUID();
                user.setAuthKey(authKey.toString());
                user.setRegistrationStatus(ACCOUNT_NOT_ACTIVATED);
                user.setDeletionStatus(ConstantVariables.NOT_DELETED);
                if (userRole == ConstantVariables.ADMIN_ROLE) {
                    user.setRole(ConstantVariables.ADMIN_ROLE);
                } else {
                    user.setRole(ConstantVariables.EMPLOYEE_ROLE);
                }

                if (!provider.createEntity(user)) {
                    throw new PersistenceException("User not saved succefully");
                }
                mailBean.sendActivationEmail(this.getUrlPrefix(), user.getEmail(), user.getName(), user.getAuthKey());
                res.put("message", "User added succefully. User check email used to activate to the account");
            }else{
                User customerByPhone = userDbBean.getCustomer_ByPhone(user.getPhone());
                if(customerByPhone != null){
                    throw new BadRequestException("There's already a customer registered to this number. Try again");
                }
                user.setRole(CUSTOMER_ROLE);
                user.setRegisteredDate(currentDate);
                user.setRegistrationStatus(ACCOUNT_NOT_ACTIVATED);
                user.setDeletionStatus(ConstantVariables.NOT_DELETED);
                if (!provider.createEntity(user)) {
                    throw new PersistenceException("Customer not saved successfully");
                }
                res.put("message", "Customer added successfully");
            }
            return Response.status(Response.Status.OK).entity(res).build();
        } catch (BadRequestException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        } catch (PersistenceException e) {
            return Response.status(Response.Status.FORBIDDEN).entity(e.getMessage()).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("An error occurred").build();
        }
    }

        public JsonResponse activateAccount (String authKey){
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

        public JsonResponse resendActivationEmail (String email){
            JsonResponse response = new JsonResponse(500, "An error occured");
            try {
                if (email != null) {
                    User user = userDbBean.getUser_ByEmail(email);
                    if (user != null) {
                        mailBean.sendActivationEmail(this.getUrlPrefix(), email, null, null);
                        response.setResponseCode(200);
                        response.setMessage("The activation email has been resent");
                    } else {
                        response.setMessage("User with this email does not exist.");
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                return response;
            }
        }

        public String getUrlPrefix () {
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
