package com.lenny.my_shop_web_backend.ejb;

import com.lenny.my_shop_web_backend.ejb_db.SaleDatabaseBean;
import com.lenny.my_shop_web_backend.ejb_db.SalesDetailsDatabaseBean;
import com.lenny.my_shop_web_backend.ejb_db.UserDatabaseBean;
import com.lenny.my_shop_web_backend.entities.Sale;
import com.lenny.my_shop_web_backend.entities.SaleDetail;
import com.lenny.my_shop_web_backend.entities.User;
import com.lenny.my_shop_web_backend.jpa.TransactionProvider;
import com.lenny.my_shop_web_backend.models.CreateSale;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.PersistenceException;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static com.lenny.my_shop_web_backend.utilities.Utils.assignTwoDecimal;

@Stateless
public class SaleBean {

    @EJB
    TransactionProvider provider;

    @EJB
    private UserDatabaseBean userDbBean;

    @EJB
    private SaleDatabaseBean saleDatabaseBean;

    @EJB
    private SalesDetailsDatabaseBean salesDetailsDatabaseBean;

    public Response createSell(CreateSale newSale) {
        try {
            if (newSale == null) {
                throw new BadRequestException("Sell is null");
            }
            Sale sale = newSale.getSale();
            int seller = sale.getSeller().getId();
            User sellerExist = userDbBean.getSeller_ById(seller);
            if (sellerExist == null) {
                throw new BadRequestException("You cannot do a sell. Contact Admin");
            }
            if (sale.getCustomer() != null) {
                int customer = sale.getCustomer().getId();
                User customerExist = userDbBean.getUser_ById(customer);
                if (customerExist == null) {
                    throw new BadRequestException("Customer does not exist");
                }
            }
            Date currentDate = new Date();
            sale.setModifiedOn(currentDate);
            sale.setSellDate(currentDate);
            if (!provider.createEntity(sale)) {
                throw new PersistenceException("Sale was not added successfully");
            }
            List<SaleDetail> saleDetails = newSale.getSaleDetails();
            if (!saleDetails.isEmpty()) {
                saveSalesDetails(sale, saleDetails, provider);
            }

            HashMap<String, Object> res = new HashMap<>();
            res.put("message", "Sale successful");
            return Response.status(Response.Status.OK).entity(res).build();
        } catch (BadRequestException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        } catch (PersistenceException e) {
            return Response.status(Response.Status.FORBIDDEN).entity(e.getMessage()).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("An error occurred").build();
        }
    }

    private void saveSalesDetails(Sale sale, List<SaleDetail> saleDetails, TransactionProvider provider) {
        Date currentDate = new Date();
        for (SaleDetail saleDetail : saleDetails) {
            saleDetail.setSale(sale);
            saleDetail.setModifiedOn(currentDate);
            saleDetail.setDate(currentDate);
        }
        if (!provider.createMultipleEntities(saleDetails)) {
            throw new PersistenceException("Sale details were not added successfully");
        }
    }

    public Response getAllSales() {
        try {
            List<Sale> sales = saleDatabaseBean.getAllSales();
            HashMap<String, Object> res = new HashMap<>();
            if (sales.isEmpty()) {
                res.put("message", "No sales exists");
            } else {
                List<HashMap<String, Object>> saleInfoList = new ArrayList<>();
                getAllSaleInfo(sales,saleInfoList);
                res.put("sales", saleInfoList);
                res.put("message", "Fetched all sales successfully");
            }
            return Response.status(Response.Status.OK).entity(res).build();
        } catch (BadRequestException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        } catch (PersistenceException e) {
            return Response.status(Response.Status.FORBIDDEN).entity(e.getMessage()).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("An error occurred").build();
        }
    }

    public Response getAllBalances(){
        try {
            List<Sale> balances = saleDatabaseBean.getAllBalances();
            HashMap<String, Object> res = new HashMap<>();
            if(balances.isEmpty()){
                res.put("message", "No balance exists");
            }else{
                List<HashMap<String, Object>> balanceInfoList = new ArrayList<>();
                getAllSaleInfo(balances,balanceInfoList);
                res.put("balances", balanceInfoList);
                res.put("message", "Fetched all balances successfully");
            }
            return Response.status(Response.Status.OK).entity(res).build();
        } catch (BadRequestException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        } catch (PersistenceException e) {
            return Response.status(Response.Status.FORBIDDEN).entity(e.getMessage()).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("An error occurred").build();
        }
    }

    public Response getBalance_ById(Integer balanceId){
        try{
            if(balanceId == null){
                throw new BadRequestException("Balance is null");
            }
            Sale balance = saleDatabaseBean.getSaleBalance_ById(balanceId);
            if(balance == null){
                throw new BadRequestException("This sale does not exist");
            }
            List<Sale> balances = new ArrayList<>();
            balances.add(balance);
            List<HashMap<String, Object>> balanceInfoList = new ArrayList<>();
            getAllSaleInfo(balances,balanceInfoList);
            HashMap<String, Object> res = new HashMap<>();
            res.put("balance", balanceInfoList);
            res.put("message", "Balance fetched successfully");
            return Response.status(Response.Status.OK).entity(res).build();
        } catch (BadRequestException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        } catch (PersistenceException e) {
            return Response.status(Response.Status.FORBIDDEN).entity(e.getMessage()).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("An error occurred").build();
        }
    }

    public Response clearBalance(Sale sale){
        try{
            if(sale == null){
                throw new BadRequestException("Balance is null");
            }
            Sale balanceToClear = saleDatabaseBean.getSaleBalance_ById(sale.getId());
            if(balanceToClear == null){
                throw new BadRequestException("This sale does not have any balance");
            }
            float originalBalance = balanceToClear.getBalance();
            float balancePaid = sale.getCashPaid();
            if(balancePaid > originalBalance){
                throw new BadRequestException("Error! Cash paid is greater than existing balance");
            }
            float totalCashPaid = balanceToClear.getCashPaid() + balancePaid;
            float remainingBalance = balanceToClear.getTotalCost() - totalCashPaid;
            Float newBalance = null;
            if(remainingBalance > 0){
                newBalance = remainingBalance;
            }
            Date currentDate = new Date();
            balanceToClear.setCashPaid(totalCashPaid);
            balanceToClear.setBalance(newBalance);
            balanceToClear.setModifiedOn(currentDate);

            if(!provider.createEntity(balanceToClear)){
                throw new PersistenceException("Balance not paid successfully");
            }
            HashMap<String, Object> res = new HashMap<>();
            res.put("message", "Balance paid successfully");
            return Response.status(Response.Status.OK).entity(res).build();
        } catch (BadRequestException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        } catch (PersistenceException e) {
            return Response.status(Response.Status.FORBIDDEN).entity(e.getMessage()).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("An error occurred").build();
        }
    }

    private void getAllSaleInfo(List<Sale> sales,List<HashMap<String, Object>> saleInfoList ){
        for(Sale sale:sales){
            HashMap<String,Object> saleInfoHashMap = new HashMap<>();
            HashMap<String, Object> saleHashMap = new HashMap<>();
            saleHashMap.put("id",sale.getId());
            saleHashMap.put("seller",sale.getSeller().getName());
            String customerName = null;
            if(sale.getCustomer() != null){
                customerName = sale.getCustomer().getName();
            }
            saleHashMap.put("customer",customerName);
            saleHashMap.put("totalCost", assignTwoDecimal(sale.getTotalCost()));
            saleHashMap.put("cashPaid", assignTwoDecimal(sale.getCashPaid()));
            float balance = 0.0F;
            if(sale.getBalance() != null){
                balance = sale.getBalance();
            }
            saleHashMap.put("balance", assignTwoDecimal(balance));
            saleHashMap.put("sellDate", sale.getSellDate());
            saleInfoHashMap.put("sale",saleHashMap);
            List<SaleDetail> saleDetails = salesDetailsDatabaseBean.getSalesDetail_BySale(sale.getId());
            if(!saleDetails.isEmpty()){
                List<HashMap<String, Object>> saleDetailsList = new ArrayList<>();
                for(SaleDetail saleDetail:saleDetails) {
                    HashMap<String, Object> salesDetailHashMap = new HashMap<>();
                    salesDetailHashMap.put("id",saleDetail.getId());
                    salesDetailHashMap.put("sale",saleDetail.getSale().getId());
                    salesDetailHashMap.put("product",saleDetail.getProduct().getName());
                    salesDetailHashMap.put("sellingPrice",assignTwoDecimal(saleDetail.getProduct().getSellingPrice()));
                    salesDetailHashMap.put("cashPaid",assignTwoDecimal(saleDetail.getSellingPrice()));
                    salesDetailHashMap.put("soldQuantity",saleDetail.getSoldQuantity());
                    saleDetailsList.add(salesDetailHashMap);
                }
                saleInfoHashMap.put("saleDetails",saleDetailsList);
            }
            saleInfoList.add(saleInfoHashMap);
        }
    }
}
