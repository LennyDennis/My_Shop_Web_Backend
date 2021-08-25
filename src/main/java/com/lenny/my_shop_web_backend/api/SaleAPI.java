package com.lenny.my_shop_web_backend.api;

import com.lenny.my_shop_web_backend.ejb.SaleBean;
import com.lenny.my_shop_web_backend.models.CreateSale;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Stateless
@Path("/sell")
@Produces({MediaType.APPLICATION_JSON})
public class SaleAPI {

    @EJB
    SaleBean saleBean;

    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    public Response createSell(CreateSale sell) {
        return saleBean.createSell(sell);
    }

    @GET
    public Response getAllSales(){
        return saleBean.getAllSales();
    }
}

