/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lenny.my_shop_web_backend.api;

import com.lenny.my_shop_web_backend.ejb.MailBean;
import com.lenny.my_shop_web_backend.ejb.UserBean;
import com.lenny.my_shop_web_backend.entities.User;
import com.lenny.my_shop_web_backend.utilities.JsonResponse;
import java.net.URI;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author lenny
 */
@Stateless
@Path("/user")
@Produces({MediaType.APPLICATION_JSON})
public class UserAPI {
    
    @EJB
    UserBean userBean;
    
    @EJB
    MailBean mailBean;

    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    public Response registerUser(User user) {
        return userBean.registerUser(user);
    }
    
    @GET
    @Path("activate/{auth}")
    public Response activateAccount(@PathParam("auth") String auth) {
        JsonResponse response = userBean.activateAccount(auth);
        return Response.ok(response.getResponseCode()).entity(response).build();
    }
    
    @POST
    @Path("resend/{email}")
    public Response sendActivationEmail(@PathParam("email") String email) {
        JsonResponse response = userBean.resendActivationEmail(email);
        return Response.ok(response.getResponseCode()).entity(response).build();
    }

    @GET
    @Path("user")
    public Response getUserDetail(@QueryParam("userId") Integer userId){
      return userBean.getUserDetail(userId);
    }

    @GET
    @Path("role")
    public Response getAllEmployees(@QueryParam("userRole") Integer userRole){
        return userBean.getUser_ByRole(userRole);
    }

//    @GET
//    @Path("customer")
//    public Response getAllCustomers(){
//        return userBean.getAllCustomers();
//    }

}
