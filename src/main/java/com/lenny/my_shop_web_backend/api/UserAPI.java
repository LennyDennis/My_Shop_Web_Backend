/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lenny.my_shop_web_backend.api;

import com.lenny.my_shop_web_backend.ejb.UserBean;
import com.lenny.my_shop_web_backend.entities.User;
import com.lenny.my_shop_web_backend.utilities.JsonResponse;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
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

    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    public Response registerUser(User user) {
        JsonResponse response = userBean.registerUser(user);
        return Response.ok(response.getResponseCode()).entity(response).build();

    }

}
