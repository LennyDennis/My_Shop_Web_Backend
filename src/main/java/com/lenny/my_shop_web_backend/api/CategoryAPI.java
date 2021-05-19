/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lenny.my_shop_web_backend.api;

import com.lenny.my_shop_web_backend.ejb.CategoryBean;
import com.lenny.my_shop_web_backend.entities.Category;
import com.lenny.my_shop_web_backend.entities.User;
import com.lenny.my_shop_web_backend.utilities.JsonResponse;
import java.util.HashMap;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author lenny
 */
@Stateless
@Path("/category")
@Produces({MediaType.APPLICATION_JSON})
public class CategoryAPI {

    @EJB
    CategoryBean categoryBean;

    @POST
    public Response addCategory(Category category) {
        JsonResponse response = categoryBean.addCategory(category);
        return Response.ok(response.getResponseCode()).entity(response).build();
    }

    @PUT
    @Path("edit")
    public Response editCategory(@QueryParam("categoryId") Integer categoryId, HashMap hashMap) {
        JsonResponse response = categoryBean.editCategory(categoryId, hashMap);
        return Response.ok(response.getResponseCode()).entity(response).build();
    }

    @PUT
    @Path("delete")
    public Response deleteCategory(@QueryParam("categoryId") Integer categoryId) {
        JsonResponse response = categoryBean.deleteCategory(categoryId);
        return Response.ok(response.getResponseCode()).entity(response).build();
    }

}