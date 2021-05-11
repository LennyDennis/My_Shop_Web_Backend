/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lenny.my_shop_web_backend.ejb_db;

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
public class User_DbBean {

    @EJB
    private TransactionProvider provider;

    public User getUser_ByEmail(String userEmail) {
        User res = null;
        try {
            if (userEmail != null) {
                EntityManager em = provider.getEM();

                Query q = em.createQuery("SELECT u FROM User u WHERE u.email = :userEmail");
                q.setParameter("userEmail", userEmail);
                res = provider.getSingleResult(q);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return res;
        }
    }

    public User getUser_ByPhone(String userPhone) {
        User res = null;
        try {
            if (userPhone != null) {
                EntityManager em = provider.getEM();

                Query q = em.createQuery("SELECT u FROM User u WHERE u.phone = :userPhone");
                q.setParameter("userPhone", userPhone);
                res = provider.getSingleResult(q);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return res;
        }
    }

    public User getUser_ById(String userId) {
        User res = null;
        try {
            if (userId != null) {
                EntityManager em = provider.getEM();

                Query q = em.createQuery("SELECT u FROM User u WHERE u.id = :userId");
                q.setParameter("userId", userId);
                res = provider.getSingleResult(q);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return res;
        }
    }

}
