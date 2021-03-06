/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lenny.my_shop_web_backend.api;

import com.lenny.my_shop_web_backend.ejb.ProductBean;
import com.lenny.my_shop_web_backend.entities.Product;
import com.lenny.my_shop_web_backend.models.OutOfStockProduct;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

/**
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
        return productBean.addProduct(product);
    }

    @PUT
    public Response editProduct(Product product) {
        return productBean.editProduct(product);
    }

    @PUT
    @Path("delete")
    public Response deleteProduct(@QueryParam("productId") Integer productId) {
        return productBean.deleteProduct(productId);
    }

    @GET
    @Path("category/products")
    public Response getAllProduct_InCategory(@QueryParam("categoryId") Integer categoryId) {
        return productBean.getProducts_ByCategory(categoryId);
    }

    @GET
    @Path("/all")
    public Response getAllProducts() {
        return productBean.getAllProducts();
    }

    @GET
    @Path("/mostSold")
    public Response getMostSoldProducts() {
        return productBean.getMostSoldProducts();
    }

    @GET
    public Response getProduct(@QueryParam("productId") Integer productId) {
        return productBean.getProduct_ById(productId);
    }

    @GET
    @Path("outOfStock")
    public Response getOutOfStockProducts() {
        return productBean.getOutOfStockProducts();
    }

    @GET
    @Path("/notification")
    public Response getOutOfStockNotifications() {
        return productBean.getOutOfStockNotifications();
    }

    @PUT
    @Path("/notification")
    public Response clearOutOfStockNotifications(List<Product> products) {
        return productBean.clearOutOfStockNotifications(products);
    }

    @PUT
    @Path("restock")
    public Response restockProduct(@QueryParam("productId") Integer productId, @QueryParam("newStock") Integer newStock) {
        return productBean.restockProduct(productId, newStock);
    }

}
