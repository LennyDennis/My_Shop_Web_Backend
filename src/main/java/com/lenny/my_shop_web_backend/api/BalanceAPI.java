package com.lenny.my_shop_web_backend.api;

import com.lenny.my_shop_web_backend.ejb.SaleBean;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Stateless
@Path("/balance")
@Produces({MediaType.APPLICATION_JSON})
public class BalanceAPI {

    @EJB
    SaleBean saleBean;

    @GET
    public Response getAllBalances() {
        return saleBean.getAllBalances();
    }

    @GET
    @Path("detail")
    public Response getBalance_ById(@QueryParam("balanceId")Integer balanceId) {
        return saleBean.getBalance_ById(balanceId);
    }
}

