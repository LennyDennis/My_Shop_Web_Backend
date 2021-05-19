/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lenny.my_shop_web_backend.ejb_db;

import com.lenny.my_shop_web_backend.entities.Category;
import com.lenny.my_shop_web_backend.entities.User;
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
    
    public Category getCategory_ById(Integer categoryId){
        Category category = null;
        try {
            if(categoryId != null){
                EntityManager em = provider.getEM();
                Query q = em.createQuery("SELECT c FROM Category c WHERE c.id = :categoryId");
                q.setParameter("categoryId", categoryId);
                category = provider.getSingleResult(q);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            return category;
        }
    }

}
