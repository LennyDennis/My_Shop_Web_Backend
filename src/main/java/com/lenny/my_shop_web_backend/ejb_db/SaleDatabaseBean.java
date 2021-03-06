package com.lenny.my_shop_web_backend.ejb_db;

import com.lenny.my_shop_web_backend.entities.Sale;
import com.lenny.my_shop_web_backend.entities.User;
import com.lenny.my_shop_web_backend.jpa.TransactionProvider;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

@Stateless
public class SaleDatabaseBean {

    @EJB
    TransactionProvider transactionProvider;

    public List<Sale> getAllSales() {
        List<Sale> salesList = new ArrayList<>();
        try {
            EntityManager em = transactionProvider.getEM();
            Query q = em.createQuery("SELECT s FROM Sale s");
            salesList = transactionProvider.getManyFromQuery(q);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return salesList;
        }
    }

    public List<Sale> getAllBalances() {
        List balanceList = new ArrayList<>();
        try {
            EntityManager em = transactionProvider.getEM();
            Query q = em.createQuery("SELECT s FROM Sale s WHERE s.balance > :balance");
            q.setParameter("balance",0);
            balanceList = transactionProvider.getManyFromQuery(q);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return balanceList;
        }
    }

    public Sale getSaleBalance_ById(Integer saleBalanceId) {
        Sale saleBalance = null;
        try {
            if (saleBalanceId != null) {
                EntityManager em = transactionProvider.getEM();
                Query q = em.createQuery("SELECT s FROM Sale s WHERE s.id = :saleId AND s.balance > :balance");
                q.setParameter("balance",0);
                q.setParameter("saleId", saleBalanceId);
                saleBalance = transactionProvider.getSingleResult(q);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return saleBalance;
        }
    }

    public List getSale_ByCustomer(User customer) {
        List sale = null;
        if (customer != null) {
            EntityManager em = transactionProvider.getEM();
            Query q = em.createQuery("SELECT s FROM SaleDetail s WHERE s.sale.customer = :customer");
            q.setParameter("customer", customer);
            sale = transactionProvider.getManyFromQuery(q);
        }
        return sale;
    }
}
