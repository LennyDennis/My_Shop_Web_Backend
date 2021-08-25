package com.lenny.my_shop_web_backend.ejb_db;

import com.lenny.my_shop_web_backend.entities.Sale;
import com.lenny.my_shop_web_backend.entities.SaleDetail;
import com.lenny.my_shop_web_backend.jpa.TransactionProvider;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.lenny.my_shop_web_backend.utilities.ConstantVariables.*;
import static com.lenny.my_shop_web_backend.utilities.Utils.*;

@Stateless
public class DashBoardDatabaseBean {

    @EJB
    TransactionProvider transactionProvider;

    public List<SaleDetail> getAllSaleDetailsStats(Integer statsType) {
        List saleDetails = new ArrayList();
        try {
            EntityManager em = transactionProvider.getEM();
            Query q;
            if(statsType ==  null){
                q = em.createQuery("SELECT s FROM SaleDetail s");
            }else{
                q = em.createQuery("SELECT s FROM SaleDetail s WHERE s.date >= :date");
                Date date = getDate_ByStatType(statsType);
                q.setParameter("date", date);
            }
            saleDetails = transactionProvider.getManyFromQuery(q);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            return saleDetails;
        }
    }

    public List<Sale> getAllSalesStats(Integer statsType) {
        List<Sale> salesList = new ArrayList<>();
        try {
            EntityManager em = transactionProvider.getEM();
            Query q;
            if(statsType ==  null){
                q = em.createQuery("SELECT s FROM Sale s");
            }else{
                q = em.createQuery("SELECT s FROM Sale s WHERE s.sellDate>=:date");
                Date date = getDate_ByStatType(statsType);
                q.setParameter("date", date);
            }
            salesList = transactionProvider.getManyFromQuery(q);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return salesList;
        }
    }

    public List<SaleDetail> getAllSaleDetailsStats_ByUserId(Integer statsType,Integer userId) {
        List saleDetails = new ArrayList();
        try {
            EntityManager em = transactionProvider.getEM();
            Query q;
            if(statsType ==  null){
                q = em.createQuery("SELECT s FROM SaleDetail s WHERE s.sale.seller.id = :userId");
                q.setParameter("userId", userId);
            }else{
                q = em.createQuery("SELECT s FROM SaleDetail s WHERE s.date >= :date AND s.sale.seller.id = :userId");
                Date date = getDate_ByStatType(statsType);
                q.setParameter("userId", userId);
                q.setParameter("date", date);
            }
            saleDetails = transactionProvider.getManyFromQuery(q);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            return saleDetails;
        }
    }

    public List<Sale> getAllSalesStats_ByUserId(Integer statsType,Integer userId) {
        List<Sale> salesList = new ArrayList<>();
        try {
            EntityManager em = transactionProvider.getEM();
            Query q;
            if(statsType ==  null){
                q = em.createQuery("SELECT s FROM Sale s WHERE s.seller.id = :userId");
                q.setParameter("userId", userId);
            }else{
                q = em.createQuery("SELECT s FROM Sale s WHERE s.sellDate>=:date AND s.seller.id = :userId");
                Date date = getDate_ByStatType(statsType);
                q.setParameter("userId", userId);
                q.setParameter("date", date);
            }
            salesList = transactionProvider.getManyFromQuery(q);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return salesList;
        }
    }

    private Date getDate_ByStatType(Integer statsType) throws ParseException {
        String date = "";
        if(statsType == TODAY){
            date = getToday();
        }else if(statsType == MONTH){
            date = getCurrentMonth();
        }else if(statsType == YEAR){
            date = getCurrentYear();
        }
        Date saleDate =new SimpleDateFormat("dd/MM/yyyy").parse(date);
        return saleDate;
    }

}
