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

}
