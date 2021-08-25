package com.lenny.my_shop_web_backend.models;

import com.lenny.my_shop_web_backend.entities.Sale;
import com.lenny.my_shop_web_backend.entities.SaleDetail;

import java.util.List;

public class CreateSale {

    private Sale sale;
    private List<CartProduct> saleProductDetails;

    public Sale getSale() {
        return sale;
    }

    public void setSale(Sale sale) {
        this.sale = sale;
    }

    public List<CartProduct> getSaleProductDetails() {
        return saleProductDetails;
    }

    public void setSaleProductDetails(List<CartProduct> saleProductDetails) {
        this.saleProductDetails = saleProductDetails;
    }
}
