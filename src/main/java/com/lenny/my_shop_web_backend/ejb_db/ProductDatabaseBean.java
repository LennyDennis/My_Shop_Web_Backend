/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lenny.my_shop_web_backend.ejb_db;

import com.lenny.my_shop_web_backend.entities.Category;
import com.lenny.my_shop_web_backend.entities.Product;
import com.lenny.my_shop_web_backend.entities.SaleDetail;
import com.lenny.my_shop_web_backend.jpa.TransactionProvider;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

import static com.lenny.my_shop_web_backend.utilities.ConstantVariables.NOT_DELETED;
import static com.lenny.my_shop_web_backend.utilities.ConstantVariables.RESTOCK_ON;

/**
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

    public Product getProduct_ById(Integer productId) {
        Product responseProduct = null;
        try {
            if (productId != null) {
                EntityManager entityManager = transactionProvider.getEM();
                Query query = entityManager.createQuery("SELECT p FROM Product p WHERE p.id = :productId AND p.deletionStatus = :deletionStatus");
                query.setParameter("productId", productId);
                query.setParameter("deletionStatus", NOT_DELETED);
                responseProduct = transactionProvider.getSingleResult(query);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return responseProduct;
        }
    }

    public List<Product> getProduct_ByCategoryId(Integer categoryId) {
        List categoryProducts = new ArrayList();
        try {
            if (categoryId != null) {
                EntityManager entityManager = transactionProvider.getEM();
                Query query = entityManager.createQuery("SELECT p FROM Product p WHERE p.category.id = :categoryId AND p.deletionStatus = :deletionStatus");
                query.setParameter("categoryId", categoryId);
                query.setParameter("deletionStatus", NOT_DELETED);
                categoryProducts = transactionProvider.getManyFromQuery(query);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return categoryProducts;
        }
    }

    public List<Product> getAllProducts() {
        List productList = new ArrayList();
        try {
            EntityManager entityManager = transactionProvider.getEM();
            Query query = entityManager.createQuery("SELECT p FROM Product p WHERE p.deletionStatus = :deletionStatus");
            query.setParameter("deletionStatus", NOT_DELETED);
            productList = transactionProvider.getManyFromQuery(query);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return productList;
        }
    }

    public List<Product> getOutOfStockProducts(){
        EntityManager entityManager = transactionProvider.getEM();
        Query query = entityManager.createQuery("SELECT p FROM Product p WHERE p.restockStatus = :restockStatus");
        query.setParameter("restockStatus", RESTOCK_ON);
        return transactionProvider.getManyFromQuery(query);
    }

    public List<SaleDetail> getProductSaleDetail(){
        EntityManager entityManager = transactionProvider.getEM();
        Query query = entityManager.createQuery("SELECT s FROM SaleDetail s");
        return transactionProvider.getManyFromQuery(query);
    }

    public List<SaleDetail> getProductSaleDetail_ByProduct(Product product){
        EntityManager entityManager = transactionProvider.getEM();
        Query query = entityManager.createQuery("SELECT s FROM SaleDetail s WHERE s.product = :product");
        query.setParameter("product", product);
        return transactionProvider.getManyFromQuery(query);
    }

}
