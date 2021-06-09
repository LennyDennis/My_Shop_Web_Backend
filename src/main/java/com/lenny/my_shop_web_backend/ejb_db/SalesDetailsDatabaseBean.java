package com.lenny.my_shop_web_backend.ejb_db;

import com.lenny.my_shop_web_backend.entities.Sale;
import com.lenny.my_shop_web_backend.entities.SaleDetail;
import com.lenny.my_shop_web_backend.jpa.TransactionProvider;
import org.apache.poi.ss.formula.functions.Now;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static java.time.temporal.TemporalAdjusters.firstDayOfYear;
import static jdk.nashorn.internal.parser.DateParser.DAY;
import static jdk.nashorn.internal.runtime.regexp.joni.constants.TokenType.INTERVAL;

@Stateless
public class SalesDetailsDatabaseBean {

    @EJB
    TransactionProvider transactionProvider;

    public List<SaleDetail> getSalesDetail_BySale(Integer saleId) {
        List saleDetails = new ArrayList<>();
        try {
            if (saleId != null) {
                EntityManager em = transactionProvider.getEM();
                Query q = em.createQuery("SELECT s FROM SaleDetail s WHERE s.sale.id =:saleId");
                q.setParameter("saleId",saleId);
                saleDetails = transactionProvider.getManyFromQuery(q);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return saleDetails;
        }
    }

    public List<SaleDetail> getAllSaleDetails() {
        List saleDetails;
        EntityManager em = transactionProvider.getEM();
        Query q = em.createQuery("SELECT s FROM SaleDetail s");
        saleDetails = transactionProvider.getManyFromQuery(q);
//        getAllSaleDetails_ByTime();
        return saleDetails;
    }

    public List<SaleDetail> getAllSaleDetails_ByTime() {
        List saleDetails;
        EntityManager em = transactionProvider.getEM();
        Query q = em.createQuery("SELECT s FROM SaleDetail s WHERE DATEDIFF(s.date= :time");

        saleDetails = transactionProvider.getManyFromQuery(q);
        return saleDetails;
    }
}
