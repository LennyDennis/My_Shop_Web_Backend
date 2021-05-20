/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lenny.my_shop_web_backend.ejb;

import com.lenny.my_shop_web_backend.ejb_db.ProductDatabaseBean;
import com.lenny.my_shop_web_backend.entities.Product;
import com.lenny.my_shop_web_backend.jpa.TransactionProvider;
import com.lenny.my_shop_web_backend.utilities.ConstantVariables;
import static com.lenny.my_shop_web_backend.utilities.ConstantVariables.ACTIVE;
import static com.lenny.my_shop_web_backend.utilities.ConstantVariables.DELETED;
import static com.lenny.my_shop_web_backend.utilities.ConstantVariables.ERROR_CODE;
import static com.lenny.my_shop_web_backend.utilities.ConstantVariables.ERROR_MESSAGE;
import static com.lenny.my_shop_web_backend.utilities.ConstantVariables.NOT_DELETED;
import static com.lenny.my_shop_web_backend.utilities.ConstantVariables.SUCCESS_CODE;
import com.lenny.my_shop_web_backend.utilities.JsonResponse;
import java.util.Date;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
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

    public JsonResponse addProduct(Product product) {
        JsonResponse response = new JsonResponse(ERROR_CODE, ERROR_MESSAGE);
        Date currentDate = new Date();
        try {
            if (product != null) {
                String productName = product.getName();
                Integer categoryId = product.getCategory().getId();
                Product existingProduct = productDatabaseBean.getProduct_ByCategory(productName, categoryId);
                if (existingProduct == null) {
                    product.setStockQuantity(ZERO_STOCK);
                    product.setActivationStatus(ACTIVE);
                    product.setRestockStatus(RESTOCK_STATUS_OFF);
                    product.setDeletionStatus(NOT_DELETED);
                    product.setModifiedOn(currentDate);
                    product.setAddedDate(currentDate);
                    if (transactionProvider.createEntity(product)) {
                        response.setResponseCode(SUCCESS_CODE);
                        response.setMessage("Product added successfully");
                    }
                } else if (existingProduct.getDeletionStatus() == DELETED) {
                    existingProduct.setActivationStatus(ACTIVE);
                    existingProduct.setRestockStatus(RESTOCK_STATUS_OFF);
                    existingProduct.setDeletionStatus(NOT_DELETED);
                    existingProduct.setModifiedOn(currentDate);
                    if (transactionProvider.createEntity(existingProduct)) {
                        response.setResponseCode(SUCCESS_CODE);
                        response.setMessage("Product added successfully");
                    }
                } else {
                    response.setMessage("Product " + productName + " already exists in this category");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return response;
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
