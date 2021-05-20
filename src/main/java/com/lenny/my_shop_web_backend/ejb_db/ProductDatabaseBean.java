/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lenny.my_shop_web_backend.ejb_db;

import com.lenny.my_shop_web_backend.entities.Category;
import com.lenny.my_shop_web_backend.entities.Product;
import com.lenny.my_shop_web_backend.jpa.TransactionProvider;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 *
 * @author lenny
 */
@Stateless
public class ProductDatabaseBean {

    @EJB
    TransactionProvider transactionProvider;

    public Product getProduct_ByCategory(String productName, Integer categoryId) {
        Product responseProduct = null;
        try {
            if (productName != null && categoryId != null) {
                EntityManager entityManager = transactionProvider.getEM();
                Query query = entityManager.createQuery("SELECT p FROM Product p WHERE p.category.id = :categoryId AND p.name = :productName");
                query.setParameter("categoryId", categoryId);
                query.setParameter("productName", productName);
                responseProduct = transactionProvider.getSingleResult(query);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return responseProduct;
        }
    }

}
