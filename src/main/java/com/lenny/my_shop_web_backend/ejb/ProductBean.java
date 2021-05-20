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
                if (existingProduct != null) {
                    response.setMessage("Product " + productName + " already exists in this category");
                } else {
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
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return response;
        }
    }
}
