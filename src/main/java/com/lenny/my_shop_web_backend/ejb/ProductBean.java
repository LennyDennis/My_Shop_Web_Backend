/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lenny.my_shop_web_backend.ejb;

import com.lenny.my_shop_web_backend.ejb_db.ProductDatabaseBean;
import com.lenny.my_shop_web_backend.entities.Product;
import com.lenny.my_shop_web_backend.jpa.TransactionProvider;

import static com.lenny.my_shop_web_backend.utilities.ConstantVariables.ACTIVE;
import static com.lenny.my_shop_web_backend.utilities.ConstantVariables.DELETED;
import static com.lenny.my_shop_web_backend.utilities.ConstantVariables.ERROR_CODE;
import static com.lenny.my_shop_web_backend.utilities.ConstantVariables.ERROR_MESSAGE;
import static com.lenny.my_shop_web_backend.utilities.ConstantVariables.NOT_DELETED;
import static com.lenny.my_shop_web_backend.utilities.ConstantVariables.SUCCESS_CODE;

import com.lenny.my_shop_web_backend.utilities.JsonResponse;

import java.util.Date;
import java.util.HashMap;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.PersistenceException;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.core.Response;

/**
 * @author lenny
 */
@Stateless
public class ProductBean {

    public static final int RESTOCK_STATUS_ON = 1;
    public static final int RESTOCK_STATUS_OFF = 0;
    public static final int ZERO_STOCK = 0;

    @EJB
    TransactionProvider transactionProvider;

    @EJB
    ProductDatabaseBean productDatabaseBean;

    public Response addProduct(Product product) {
        try {
            if (product == null) {
                throw new BadRequestException("Product is equal to null");
            }
            String productName = product.getName();
            Integer categoryId = product.getCategory().getId();
            Product existingProduct = productDatabaseBean.getProduct_ByCategory(productName, categoryId);
            if (existingProduct != null && existingProduct.getDeletionStatus() == NOT_DELETED) {
                throw new BadRequestException("Product " + productName + " already exists in this category");
            }
            Date currentDate = new Date();
            if (existingProduct != null && existingProduct.getDeletionStatus() == DELETED) {
                existingProduct.setActivationStatus(ACTIVE);
                existingProduct.setRestockStatus(RESTOCK_STATUS_OFF);
                existingProduct.setDeletionStatus(NOT_DELETED);
                existingProduct.setModifiedOn(currentDate);
                existingProduct.setAddedDate(currentDate);
                saveNewProduct(existingProduct);
            } else {
                product.setStockQuantity(ZERO_STOCK);
                product.setActivationStatus(ACTIVE);
                product.setRestockStatus(RESTOCK_STATUS_OFF);
                product.setDeletionStatus(NOT_DELETED);
                product.setModifiedOn(currentDate);
                product.setAddedDate(currentDate);
                saveNewProduct(product);
            }
            HashMap<String, Object> res = new HashMap<>();
            res.put("Message", "Product " + productName + " has been added successfully");
            return Response.status(Response.Status.OK).entity(res).build();
        } catch (
                BadRequestException e) {
            return Response.status(Response.Status.UNAUTHORIZED).entity(e.getMessage()).build();
        } catch (
                PersistenceException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        } catch (
                Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("An error occurred").build();
        }
    }

    private void saveNewProduct(Product newProduct) {
        if (!transactionProvider.createEntity(newProduct)) {
            throw new PersistenceException("Product was not added successfully");
        }
    }

    public JsonResponse editProduct(Product editProduct) {
        JsonResponse response = new JsonResponse(ERROR_CODE, ERROR_MESSAGE);
        Date currentDate = new Date();
        try {
            if (editProduct != null) {
                Product retrievedProduct = productDatabaseBean.getProduct_ById(editProduct.getId());
                if (retrievedProduct != null) {
                    if (retrievedProduct == editProduct) {
                        response.setMessage("All the products fields are equal");
                    } else {
                        editProduct.setModifiedOn(currentDate);
                        if (transactionProvider.updateEntity(retrievedProduct)) {
                            response.setResponseCode(SUCCESS_CODE);
                            response.setMessage("Product added successfully");
                        }
                    }
                } else {
                    response.setMessage("Product does not exist");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return response;
        }
    }
}
