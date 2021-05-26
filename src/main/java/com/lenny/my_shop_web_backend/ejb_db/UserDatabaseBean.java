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

import java.util.ArrayList;
import java.util.List;

import static com.lenny.my_shop_web_backend.utilities.ConstantVariables.*;

/**
 *
 * @author lenny
 */
@Stateless
public class UserDatabaseBean {

    @EJB
    private TransactionProvider provider;

    public User getUser_ByEmail(String userEmail) {
        User res = null;
        try {
            if (userEmail != null) {
                EntityManager em = provider.getEM();

                Query q = em.createQuery("SELECT u FROM User u WHERE u.email = :userEmail AND u.deletionStatus = :deletionStatus");
                q.setParameter("userEmail", userEmail);
                q.setParameter("deletionStatus", NOT_DELETED);
                res = provider.getSingleResult(q);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return res;
        }
    }

    public User getCustomer_ByPhone(String userPhone) {
        User res = null;
        try {
            if (userPhone != null) {
                EntityManager em = provider.getEM();

                Query q = em.createQuery("SELECT u FROM User u WHERE u.phone = :userPhone AND u.role = :role");
                q.setParameter("userPhone", userPhone);
                q.setParameter("role", CUSTOMER_ROLE);
                res = provider.getSingleResult(q);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return res;
        }
    }

    public User getUser_ById(Integer userId) {
        User res = null;
        try {
            if (userId != null) {
                EntityManager em = provider.getEM();

                Query q = em.createQuery("SELECT u FROM User u WHERE u.id = :userId AND u.deletionStatus = :deletionStatus");
                q.setParameter("userId", userId);
                q.setParameter("deletionStatus", NOT_DELETED);
                res = provider.getSingleResult(q);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return res;
        }
    }

    public User getSeller_ById(Integer userId) {
        User res = null;
        try {
            if (userId != null) {
                EntityManager em = provider.getEM();

                Query q = em.createQuery("SELECT u FROM User u WHERE u.id = :userId AND u.activationStatus = :activationStatus AND u.deletionStatus = :deletionStatus");
                q.setParameter("userId", userId);
                q.setParameter("activationStatus", ACTIVATE);
                q.setParameter("deletionStatus", NOT_DELETED);
                res = provider.getSingleResult(q);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return res;
        }
    }
    
    public User getUser_ByAuthKey(String authKey){
        User userResponse = null;
        try{
            if(authKey != null){
                EntityManager em = provider.getEM();
                
                Query q = em.createQuery("SELECT u From User u WHERE u.authKey = :authKey");
                q.setParameter("authKey", authKey);
                userResponse = provider.getSingleResult(q);
            }
        }catch(Exception e){
            e.printStackTrace();
        }finally{
           return userResponse;
        }
    }

    public List<User> getAllCustomers(){
        List customers = new ArrayList<>();
        try {
            EntityManager em = provider.getEM();
            Query q = em.createQuery("SELECT u From User u WHERE u.role = :role AND u.deletionStatus = :deletionStatus");
            q.setParameter("role", CUSTOMER_ROLE);
            q.setParameter("deletionStatus", NOT_DELETED);
            customers = provider.getManyFromQuery(q);
        }catch (Exception e){

        }finally {
            return customers;
        }
    }

    public List<User> getAllEmployees(){
        List employees = new ArrayList<>();
        try {
            EntityManager em = provider.getEM();
            Query q = em.createQuery("SELECT u From User u WHERE u.role = :adminRole OR u.role = :employeeRole AND u.deletionStatus = :deletionStatus");
            q.setParameter("adminRole", ADMIN_ROLE);
            q.setParameter("employeeRole", EMPLOYEE_ROLE);
            q.setParameter("deletionStatus", NOT_DELETED);
            employees = provider.getManyFromQuery(q);
        }catch (Exception e){

        }finally {
            return employees;
        }
    }

}
