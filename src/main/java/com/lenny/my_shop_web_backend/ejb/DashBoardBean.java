package com.lenny.my_shop_web_backend.ejb;

import com.lenny.my_shop_web_backend.ejb_db.DashBoardDatabaseBean;
import com.lenny.my_shop_web_backend.ejb_db.SaleDatabaseBean;
import com.lenny.my_shop_web_backend.ejb_db.SalesDetailsDatabaseBean;
import com.lenny.my_shop_web_backend.ejb_db.UserDatabaseBean;
import com.lenny.my_shop_web_backend.entities.Sale;
import com.lenny.my_shop_web_backend.entities.SaleDetail;
import com.lenny.my_shop_web_backend.entities.User;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.PersistenceException;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.lenny.my_shop_web_backend.utilities.ConstantVariables.*;
import static com.lenny.my_shop_web_backend.utilities.Utils.assignTwoDecimal;

@Stateless
public class DashBoardBean {

    @EJB
    SalesDetailsDatabaseBean salesDetailsDatabaseBean;

    @EJB
    DashBoardDatabaseBean dashBoardDatabaseBean;

    @EJB
    SaleDatabaseBean saleDatabaseBean;

    @EJB
    UserDatabaseBean userDatabaseBean;

    public Response getTotalStats(Integer userId) {
        try {
            HashMap<String, Object> res = new HashMap<>();
            List<Integer> statsType = new ArrayList<>();
            List statsList = new ArrayList<>();
            statsType.add(TODAY);
            statsType.add(MONTH);
            statsType.add(YEAR);
            for(Integer stat : statsType){
                getStatsByStatsType(statsList,stat,userId);
            }
            getStats(res,userId);
            res.put("stats", statsList);
            res.put("message", "Stats gotten successfully");
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

    public void getStats(HashMap<String, Object> res,Integer userId){
        HashMap<String, Object> totalStats = new HashMap<>();
        getAllBalancesStats(totalStats);
        Integer user = null;
        if(userId != null){
            User retrievedUser = userDatabaseBean.getUser_ById(userId);
            if(retrievedUser == null){
                throw new BadRequestException("User does not exist");
            }else{
                user = userId;
            }
        }
        getAllProductsSoldStats(totalStats, null,user);
        getAllSalesStats(totalStats,null,user);
        getSalesProfitsStats(totalStats,null,user);

        res.put("totalStats",totalStats);
    }

    public void getStatsByStatsType(List statsList,Integer statsType,Integer userId){
        HashMap<String, Object> statsGroup = new HashMap<>();
        HashMap<String, Object> stats = new HashMap<>();
        if(userId != null){
            User user = userDatabaseBean.getUser_ById(userId);
            if(user == null){
                throw new BadRequestException("User does not exist");
            }
        }
        getAllProductsSoldStats(statsGroup, statsType,userId);
        getAllSalesStats(statsGroup,statsType,userId);
        getSalesProfitsStats(statsGroup,statsType,userId);
        if(statsType == TODAY){
            stats.put("today", statsGroup);
            statsList.add(stats);
        }else if(statsType == MONTH){
            stats.put("month", statsGroup);
            statsList.add(stats);
        }else if(statsType == YEAR){
            stats.put("year", statsGroup);
            statsList.add(stats);
        }
    }

    public void getAllProductsSoldStats(HashMap<String, Object> res,Integer statsType,Integer userId) {
        List<SaleDetail> salesDetails;
        if(userId == null){
            salesDetails = dashBoardDatabaseBean.getAllSaleDetailsStats(statsType);
        }else{
            salesDetails = dashBoardDatabaseBean.getAllSaleDetailsStats_ByUserId(statsType,userId);
        }
        Integer productsSold = 0;
        if (!salesDetails.isEmpty()) {
            for (SaleDetail saleDetail : salesDetails) {
                productsSold += saleDetail.getSoldQuantity();
            }
        }
        res.put("productsSold", productsSold);
    }

    public void getAllSalesStats(HashMap<String, Object> res,Integer statsType,Integer userId) {
        List<Sale> sales;
        if(userId == null){
            sales = dashBoardDatabaseBean.getAllSalesStats(statsType);
        }else{
            sales = dashBoardDatabaseBean.getAllSalesStats_ByUserId(statsType,userId);
        }
        float totalSales = 0;
        if (!sales.isEmpty()) {
            for (Sale sale : sales) {
                totalSales += sale.getTotalCost();
            }
        }
        res.put("sales", assignTwoDecimal(totalSales));
    }

    public void getSalesProfitsStats(HashMap<String, Object> res,Integer statsType,Integer userId) {
        List<Sale> sales;
        if(userId == null){
            sales = dashBoardDatabaseBean.getAllSalesStats(statsType);
        }else{
            sales = dashBoardDatabaseBean.getAllSalesStats_ByUserId(statsType,userId);
        }
        float totalProfits = 0;
        if (!sales.isEmpty()) {
            for (Sale sale : sales) {
                List<SaleDetail> saleDetails = salesDetailsDatabaseBean.getSalesDetail_BySale(sale.getId());
                for (SaleDetail saleDetail : saleDetails) {
                    float buyingPrice = saleDetail.getProduct().getBuyingPrice();
                    float actualSellingPrice = saleDetail.getSellingPrice();
                    int productsSold = saleDetail.getSoldQuantity();
                    float profit = (actualSellingPrice - buyingPrice) * productsSold;
                    totalProfits += profit;
                }
            }
        }
        res.put("profits", assignTwoDecimal(totalProfits));
    }

    public Response getAllBalancesStats(HashMap<String, Object> res) {
        try {
            List<Sale> balances = saleDatabaseBean.getAllBalances();
            float totalBalances = 0;
            if (!balances.isEmpty()) {
                for(Sale balance:balances){
                    totalBalances += balance.getBalance();
                }
            }
            res.put("balances", totalBalances);
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

}
