package com.lenny.my_shop_web_backend.api;

import com.lenny.my_shop_web_backend.ejb.DashBoardBean;
import com.lenny.my_shop_web_backend.ejb.ProductBean;
import com.lenny.my_shop_web_backend.ejb.SaleBean;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Stateless
@Path("/dashboard")
@Produces({MediaType.APPLICATION_JSON})
public class DashboardAPI {

    @EJB
    DashBoardBean dashBoardBean;

    @GET
    @Path("/user")
    public Response getTotalStats_ByUser(@QueryParam("userId") Integer userId){
        return dashBoardBean.getTotalStats(userId);
    }

    @GET
    @Path("/all")
    public Response getTotalStats(){
        return dashBoardBean.getTotalStats(null);
    }

}
