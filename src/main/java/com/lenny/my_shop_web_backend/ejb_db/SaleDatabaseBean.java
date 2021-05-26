package com.lenny.my_shop_web_backend.ejb_db;

import com.lenny.my_shop_web_backend.jpa.TransactionProvider;

import javax.ejb.EJB;
import javax.ejb.Stateless;

@Stateless
public class SaleDatabaseBean {

    @EJB
    TransactionProvider transactionProvider;


}
