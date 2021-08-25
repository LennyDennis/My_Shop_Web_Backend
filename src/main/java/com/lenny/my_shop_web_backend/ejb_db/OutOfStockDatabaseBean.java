package com.lenny.my_shop_web_backend.ejb_db;

import com.lenny.my_shop_web_backend.entities.Notification;
import com.lenny.my_shop_web_backend.entities.Product;
import com.lenny.my_shop_web_backend.entities.User;
import com.lenny.my_shop_web_backend.jpa.TransactionProvider;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

import static com.lenny.my_shop_web_backend.utilities.ConstantVariables.NOT_VIEWED;
import static com.lenny.my_shop_web_backend.utilities.ConstantVariables.RESTOCK_ON;

@Stateless
public class OutOfStockDatabaseBean {

    @EJB
    TransactionProvider transactionProvider;

    public Notification getNotification_ByProductAndUser(User user, Product product){
        EntityManager entityManager = transactionProvider.getEM();
        Query query = entityManager.createQuery("SELECT n FROM Notification n WHERE n.user = :user AND n.product = :product");
        query.setParameter("user", user);
        query.setParameter("product", product);
        return transactionProvider.getSingleResult(query);
    }

    public List<Notification> getNotViewedNotifications_ByProduct(Product product){
        EntityManager entityManager = transactionProvider.getEM();
        Query query = entityManager.createQuery("SELECT n FROM Notification n WHERE n.product = :product AND n.viewStatus = :viewStatus");
        query.setParameter("product", product);
        query.setParameter("viewStatus", NOT_VIEWED);
        return transactionProvider.getManyFromQuery(query);
    }

    public List<Product> getOutOfStockProducts(){
        EntityManager entityManager = transactionProvider.getEM();
        Query query = entityManager.createQuery("SELECT p FROM Product p WHERE p.restockStatus = :restockStatus");
        query.setParameter("restockStatus", RESTOCK_ON);
        return transactionProvider.getManyFromQuery(query);
    }

    public List getOutOfStockNotification(){
        EntityManager entityManager = transactionProvider.getEM();
        Query query = entityManager.createQuery("SELECT n FROM Notification n WHERE n.viewStatus = :viewStatus");
        query.setParameter("viewStatus", NOT_VIEWED);
        return transactionProvider.getManyFromQuery(query);
    }
}
