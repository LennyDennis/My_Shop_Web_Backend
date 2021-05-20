/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lenny.my_shop_web_backend.api;

import com.lenny.my_shop_web_backend.ejb.ProductBean;
import com.lenny.my_shop_web_backend.entities.Category;
import com.lenny.my_shop_web_backend.entities.Product;
import com.lenny.my_shop_web_backend.utilities.JsonResponse;
import javax.ejb.EJB;
import javax.ejb.Stateless;
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
@Path("/product")
@Produces({MediaType.APPLICATION_JSON})
public class ProductAPI {

    @EJB
    ProductBean productBean;

    @POST
    public Response addProduct(Product product) {
        JsonResponse response = productBean.addProduct(product);
        return Response.ok(response.getResponseCode()).entity(response).build();
    }
    
    @PUT
    public Response editProduct(Product product){
        JsonResponse response = productBean.editProduct(product);
        return Response.ok(response.getResponseCode()).entity(response).build();
    }

}
