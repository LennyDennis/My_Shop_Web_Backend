/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lenny.my_shop_web_backend.ejb;

import com.lenny.my_shop_web_backend.ejb_db.CategoryDatabaseBean;
import com.lenny.my_shop_web_backend.ejb_db.ProductDatabaseBean;
import com.lenny.my_shop_web_backend.entities.Category;
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

    @EJB
    CategoryDatabaseBean categoryDatabaseBean;

    public Response addProduct(Product product) {
        try {
            if (product == null) {
                throw new BadRequestException("Product is equal to null");
            }

            String productName = product.getName();
            Integer categoryId = product.getCategory().getId();
            Category existingCategory = categoryDatabaseBean.getCategory_ById(categoryId);
            if(existingCategory == null){
                throw new BadRequestException("Category selected does not exist");
            }
            Product existingProduct = productDatabaseBean.getProduct_ByCategory(productName, categoryId);
            if (existingProduct != null && existingProduct.getDeletionStatus() == NOT_DELETED) {
                throw new BadRequestException("Product " + productName + " already exists in this category");
            }

            Float sellingPrice = product.getSellingPrice();
            Float buyingPrice = product.getBuyingPrice();
            Float maxDiscountMark = sellingPrice - buyingPrice;
            Float maxDiscountGiven = product.getMaxDiscount();
            SellingPriceGreater(sellingPrice, buyingPrice);
            CheckMaxDiscount(maxDiscountMark, maxDiscountGiven);

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
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        } catch (
                PersistenceException e) {
            return Response.status(Response.Status.FORBIDDEN).entity(e.getMessage()).build();
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

    public Response editProduct(Product editProduct) {
        try {
            if (editProduct == null) {
                throw new BadRequestException("Product is null");
            }
            Float sellingPrice = editProduct.getSellingPrice();
            Float buyingPrice = editProduct.getBuyingPrice();
            Float maxDiscountMark = sellingPrice - buyingPrice;
            Float maxDiscountGiven = editProduct.getMaxDiscount();
            SellingPriceGreater(sellingPrice, buyingPrice);
            CheckMaxDiscount(maxDiscountMark, maxDiscountGiven);

            Product retrievedProduct = productDatabaseBean.getProduct_ById(editProduct.getId());
            if (editProduct.getName() != null) {
                retrievedProduct.setName(editProduct.getName());
            }
            if (editProduct.getCategory() != null) {
                retrievedProduct.setCategory(editProduct.getCategory());
            }

            if (buyingPrice != 0.0f) {
                retrievedProduct.setBuyingPrice(buyingPrice);
            }

            if (sellingPrice != 0.0f) {
                retrievedProduct.setSellingPrice(sellingPrice);
            }

            if (maxDiscountGiven != 0.0f) {
                retrievedProduct.setMaxDiscount(maxDiscountGiven);
            }
            Date currentDate = new Date();
            retrievedProduct.setModifiedOn(currentDate);

            if (!transactionProvider.updateEntity(retrievedProduct)) {
                throw new PersistenceException("Product was not updated successfully");
            }
            HashMap<String, Object> res = new HashMap<>();
            res.put("Message", "Product updated successfully");
            return Response.status(Response.Status.OK).entity(res).build();
        } catch (
                BadRequestException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        } catch (
                PersistenceException e) {
            return Response.status(Response.Status.FORBIDDEN).entity(e.getMessage()).build();
        } catch (
                Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("An error occurred").build();
        }
    }

    //check whether selling price is greater than buying price
    private void SellingPriceGreater(Float sellingPrice, Float buyingPrice) {
        if (sellingPrice < buyingPrice) {
            throw new BadRequestException("Selling price should be greater than buying price");
        }
    }

    private void CheckMaxDiscount(Float maxDiscountMark, Float maxDiscountGiven) {
        if (maxDiscountGiven > maxDiscountMark) {
            throw new BadRequestException("Maximum discount should not exceed Ksh" + maxDiscountMark + " for this product");
        }
    }


}
