/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lenny.my_shop_web_backend.ejb_db;

import com.lenny.my_shop_web_backend.entities.Category;
import com.lenny.my_shop_web_backend.entities.User;
import com.lenny.my_shop_web_backend.jpa.TransactionProvider;
import com.lenny.my_shop_web_backend.utilities.ConstantVariables;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import static com.lenny.my_shop_web_backend.utilities.ConstantVariables.NOT_DELETED;

/**
 *
 * @author lenny
 */
@Stateless
public class CategoryDatabaseBean {

    @EJB
    private TransactionProvider provider;

    public Category getCategory_ByName(String categoryName) {
        Category category = null;
        try {
            if (categoryName != null) {
                EntityManager em = provider.getEM();
                Query q = em.createQuery("SELECT c FROM Category c WHERE c.name = :categoryName");
                q.setParameter("categoryName", categoryName);
                category = provider.getSingleResult(q);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return category;
        }
    }

    public Category getCategory_ById(Integer categoryId) {
        Category category = null;
        try {
            if (categoryId != null) {
                EntityManager em = provider.getEM();
                Query q = em.createQuery("SELECT c FROM Category c WHERE c.id = :categoryId AND c.deletionStatus = :deletionStatus");
                q.setParameter("categoryId", categoryId);
                q.setParameter("deletionStatus", NOT_DELETED);
                category = provider.getSingleResult(q);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return category;
        }
    }

    public List<Category> getAllCategories() {
        List<Category> categories = new ArrayList();
        try {
            EntityManager em = provider.getEM();
            Query q = em.createQuery("SELECT c FROM Category c WHERE c.deletionStatus = :deletionStatus");
            q.setParameter("deletionStatus", NOT_DELETED);
            categories = provider.getManyFromQuery(q);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return categories;
        }
    }

}
