package com.lenny.my_shop_web_backend.models;

import com.lenny.my_shop_web_backend.entities.Product;

public class CartProduct {

    private Product cartProductId;
    private float cartSellingPrice;
    private int cartQuantityToSell;

    public Product getCartProductId() {
        return cartProductId;
    }

    public void setCartProductId(Product cartProductId) {
        this.cartProductId = cartProductId;
    }

    public float getCartSellingPrice() {
        return cartSellingPrice;
    }

    public void setCartSellingPrice(float cartSellingPrice) {
        this.cartSellingPrice = cartSellingPrice;
    }

    public int getCartQuantityToSell() {
        return cartQuantityToSell;
    }

    public void setCartQuantityToSell(int cartQuantityToSell) {
        this.cartQuantityToSell = cartQuantityToSell;
    }
}
