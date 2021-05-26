package com.lenny.my_shop_web_backend.ejb;

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
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Stateless
public class SaleBean {

    @EJB
    TransactionProvider provider;

    @EJB
    private UserDatabaseBean userDbBean;

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
            if(!provider.createEntity(sale)){
                throw new PersistenceException("Sale was not added successfully");
            }
            List<SaleDetail> saleDetails = newSale.getSaleDetails();
            if(!saleDetails.isEmpty()){
                saveSalesDetails(sale,saleDetails,provider);
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

    private void saveSalesDetails(Sale sale,List<SaleDetail> saleDetails,TransactionProvider provider){
        Date currentDate = new Date();
        for(SaleDetail saleDetail:saleDetails){
            saleDetail.setSale(sale);
            saleDetail.setModifiedOn(currentDate);
            saleDetail.setDate(currentDate);
        }
        if(!provider.createMultipleEntities(saleDetails)){
            throw new PersistenceException("Sale details were not added successfully");
        }
    }
}
