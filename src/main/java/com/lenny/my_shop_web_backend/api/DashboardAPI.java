package com.lenny.my_shop_web_backend.api;

import com.lenny.my_shop_web_backend.ejb.ProductBean;
import com.lenny.my_shop_web_backend.ejb.SaleBean;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Stateless
@Path("/dashboard")
@Produces({MediaType.APPLICATION_JSON})
public class DashboardAPI {

    @EJB
    SaleBean saleBean;

    @GET
    @Path("/all")
    public Response getTotalStats(){
        return saleBean.getTotalStats();
    }
}
