/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lenny.my_shop_web_backend.ejb;

import com.google.common.base.Strings;
import com.lenny.my_shop_web_backend.ejb_db.CategoryDatabaseBean;
import com.lenny.my_shop_web_backend.ejb_db.ProductDatabaseBean;
import com.lenny.my_shop_web_backend.entities.Category;
import com.lenny.my_shop_web_backend.entities.NewStock;
import com.lenny.my_shop_web_backend.entities.Product;
import com.lenny.my_shop_web_backend.entities.SaleDetail;
import com.lenny.my_shop_web_backend.jpa.TransactionProvider;
import com.lenny.my_shop_web_backend.utilities.ValuesFromHashMap;

import static com.lenny.my_shop_web_backend.utilities.ConstantVariables.*;
import static com.lenny.my_shop_web_backend.utilities.Utils.assignTwoDecimal;
import static com.lenny.my_shop_web_backend.utilities.Utils.getDateFromMilliSeconds;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.PersistenceException;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.core.Response;

/**
 * @author lenny
 */
@Stateless
public class ProductBean {

    public static final int RESTOCK_STATUS_ON = 1;
    public static final int RESTOCK_STATUS_OFF = 0;
    public static final int ZERO_STOCK = 0;

    @EJB
    TransactionProvider transactionProvider;

    @EJB
    ProductDatabaseBean productDatabaseBean;

    @EJB
    CategoryDatabaseBean categoryDatabaseBean;

    public Response addProduct(Product product) {
        try {
            if (product == null) {
                throw new BadRequestException("Product is equal to null");
            }

            String productName = product.getName();
            Integer categoryId = product.getCategory().getId();
            Category existingCategory = categoryDatabaseBean.getCategory_ById(categoryId);
            if (existingCategory == null) {
                throw new BadRequestException("Category selected does not exist");
            }
            Product existingProduct = productDatabaseBean.getProduct_ByCategory(productName, categoryId);
            if (existingProduct != null && existingProduct.getDeletionStatus() == NOT_DELETED) {
                throw new BadRequestException("Product " + productName + " already exists in this category");
            }

            Float sellingPrice = product.getSellingPrice();
            Float buyingPrice = product.getBuyingPrice();
            Float maxDiscountMark = sellingPrice - buyingPrice;
            Float maxDiscountGiven = product.getMaxDiscount();
            SellingPriceGreater(sellingPrice, buyingPrice);
            CheckMaxDiscount(maxDiscountMark, maxDiscountGiven);

            Date currentDate = new Date();
            if (existingProduct != null && existingProduct.getDeletionStatus() == DELETED) {
                existingProduct.setActivationStatus(ACTIVATE);
                existingProduct.setRestockStatus(RESTOCK_STATUS_OFF);
                existingProduct.setDeletionStatus(NOT_DELETED);
                existingProduct.setModifiedOn(currentDate);
                existingProduct.setAddedDate(currentDate);
                saveNewProduct(existingProduct);
            } else {
                product.setStockQuantity(ZERO_STOCK);
                product.setActivationStatus(ACTIVATE);
                product.setRestockStatus(RESTOCK_STATUS_OFF);
                product.setDeletionStatus(NOT_DELETED);
                product.setModifiedOn(currentDate);
                product.setAddedDate(currentDate);
                saveNewProduct(product);
            }
            HashMap<String, Object> res = new HashMap<>();
            res.put("Message", "Product " + productName + " has been added successfully");
            return Response.status(Response.Status.OK).entity(res).build();
        } catch (
                BadRequestException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        } catch (
                PersistenceException e) {
            return Response.status(Response.Status.FORBIDDEN).entity(e.getMessage()).build();
        } catch (
                Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("An error occurred").build();
        }
    }

    private void saveNewProduct(Product newProduct) {
        if (!transactionProvider.createEntity(newProduct)) {
            throw new PersistenceException("Product was not added successfully");
        }
    }

    public Response editProduct(Product editProduct) {
        try {
            if (editProduct == null) {
                throw new BadRequestException("Product is null");
            }
            float sellingPrice = editProduct.getSellingPrice();
            float buyingPrice = editProduct.getBuyingPrice();
            float maxDiscountMark = sellingPrice - buyingPrice;
            float maxDiscountGiven = editProduct.getMaxDiscount();
            SellingPriceGreater(sellingPrice, buyingPrice);
            CheckMaxDiscount(maxDiscountMark, maxDiscountGiven);
            Integer categoryId = editProduct.getCategory().getId();
            Category existingCategory = categoryDatabaseBean.getCategory_ById(categoryId);
            if (existingCategory == null) {
                throw new BadRequestException("Category selected does not exist");
            }

            Product retrievedProduct = productDatabaseBean.getProduct_ById(editProduct.getId());

            if (!Strings.isNullOrEmpty(editProduct.getName()) && !editProduct.getName().equals(retrievedProduct.getName())) {
                Product existingProduct = productDatabaseBean.getProduct_ByCategory(editProduct.getName(), categoryId);
                if (existingProduct != null && existingProduct.getDeletionStatus() == NOT_DELETED) {
                    throw new BadRequestException("Product " + existingProduct.getName() + " already exists in this category");
                }
                retrievedProduct.setName(editProduct.getName());
            }
            if (editProduct.getCategory() != null) {
                retrievedProduct.setCategory(editProduct.getCategory());
            }

            if (buyingPrice != 0.0f) {
                retrievedProduct.setBuyingPrice(buyingPrice);
            }

            if (sellingPrice != 0.0f) {
                retrievedProduct.setSellingPrice(sellingPrice);
            }

            if (maxDiscountGiven != 0.0f) {
                retrievedProduct.setMaxDiscount(maxDiscountGiven);
            }

//            if (editProduct.getActivationStatus() != null) {
//                retrievedProduct.setActivationStatus(editProduct.getActivationStatus());
//            }

            Date currentDate = new Date();
            retrievedProduct.setModifiedOn(currentDate);

            if (!transactionProvider.updateEntity(retrievedProduct)) {
                throw new PersistenceException("Product was not updated successfully");
            }
            HashMap<String, Object> res = new HashMap<>();
            res.put("Message", "Product updated successfully");
            return Response.status(Response.Status.OK).entity(res).build();
        } catch (
                BadRequestException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        } catch (
                PersistenceException e) {
            return Response.status(Response.Status.FORBIDDEN).entity(e.getMessage()).build();
        } catch (
                Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("An error occurred").build();
        }
    }

    //check whether selling price is greater than buying price
    private void SellingPriceGreater(Float sellingPrice, Float buyingPrice) {
        if (sellingPrice < buyingPrice) {
            throw new BadRequestException("Selling price should be greater than buying price");
        }
    }

    private void CheckMaxDiscount(Float maxDiscountMark, Float maxDiscountGiven) {
        if (maxDiscountGiven > maxDiscountMark) {
            throw new BadRequestException("Maximum discount should not exceed Ksh" + maxDiscountMark + " for this product");
        }
    }

    public Response deleteProduct(Integer productId) {
        try {
            if (productId == null) {
                throw new BadRequestException("Product is empty");
            }
            Product product = productDatabaseBean.getProduct_ById(productId);
            if (product == null) {
                throw new BadRequestException("This product does not exist");
            }
            if (product.getStockQuantity() > 0) {
                throw new BadRequestException("You can not delete a product that has existing stock");
            }
            if (product.getSaleDetailList().size() > 0) {
                throw new BadRequestException("This product is tied to a sale. You can not delete it.");
            }
            product.setDeletionStatus(DELETED);
            if (!transactionProvider.updateEntity(product)) {
                throw new PersistenceException("Product was not deleted successfully");
            }

            HashMap<String, Object> res = new HashMap<>();
            res.put("Message", "Product deleted successfully");
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

    public Response getProducts_ByCategory(Integer categoryId) {
        try {
            if (categoryId == null) {
                throw new BadRequestException("Category selected is empty.Try again!");
            }
            Category category = categoryDatabaseBean.getCategory_ById(categoryId);
            if (category == null) {
                throw new BadRequestException("Category selected does not exist");
            }

            HashMap<String, Object> res = new HashMap<>();
            List<Product> productList = productDatabaseBean.getProduct_ByCategoryId(categoryId);
            if (productList.isEmpty()) {
                res.put("Message", "No product currently exist in this product");
            } else {
                List<Object> products = new ArrayList<>();
                saveProductInHashMap(productList, products);
                res.put("categoryName",category.getName());
                res.put("products", products);
                res.put("Message", "Products in " + category.getName() + " fetched successfully");
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

    private void saveProductInHashMap(List<Product> productList, List<Object> productsFetched) {
        for (Product product : productList) {
            HashMap<String, Object> productHashMap = new HashMap<>();
            productDetailsHashMap(product, productHashMap);
            productsFetched.add(productHashMap);
        }
    }

    private void productDetailsHashMap(Product product, HashMap<String, Object> productHashMap) {
        productHashMap.put("productId", product.getId());
        productHashMap.put("name", product.getName());
        productHashMap.put("category", product.getCategory().getName());
        productHashMap.put("categoryId", product.getCategory().getId());
        productHashMap.put("buyingPrice", assignTwoDecimal(product.getBuyingPrice()));
        productHashMap.put("sellingPrice", assignTwoDecimal(product.getSellingPrice()));
        productHashMap.put("maxDiscount", assignTwoDecimal(product.getMaxDiscount()));
        productHashMap.put("stockQuantity", product.getStockQuantity());
        productHashMap.put("activationStatus", product.getActivationStatus());
        productHashMap.put("restockStatus", product.getRestockStatus());
        productHashMap.put("modifiedOn", product.getModifiedOn());
        productHashMap.put("addedDate", product.getAddedDate());
    }

    public Response getAllProducts() {
        try {
            List<Product> productList = productDatabaseBean.getAllProducts();
            HashMap<String, Object> res = new HashMap<>();
            if (productList.isEmpty()) {
                res.put("products", productList);
                res.put("Message", "No products exist");
            } else {
                List<Object> products = new ArrayList<>();
                saveProductInHashMap(productList, products);
                res.put("products", products);
                res.put("Message", "All products fetched successfully");
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

    public Response getMostSoldProducts() {
        try {
            List<SaleDetail> saleDetailList = productDatabaseBean.getProductSaleDetail();
            HashMap<String, Object> res = new HashMap<>();
            if (saleDetailList.isEmpty()) {
                res.put("mostSold", saleDetailList);
                res.put("Message", "No products sold");
            } else {
                List<Object> products = new ArrayList<>();
                List<Object> mostSoldProducts = new ArrayList<>();
                for(SaleDetail saleDetail:saleDetailList){
                    HashMap<String, Object> mostSoldProduct = new HashMap<>();
                    if(!products.contains(saleDetail.getProduct())){
                        products.add(saleDetail.getProduct());
                        mostSoldProduct.put("product",saleDetail.getProduct().getName());
                        List<SaleDetail> productSaleDetails = productDatabaseBean.getProductSaleDetail_ByProduct(saleDetail.getProduct());
                        Integer soldQuantity = 0;
                        for(SaleDetail productSaleDetail: productSaleDetails){
                            soldQuantity += productSaleDetail.getSoldQuantity();
                        }
                        mostSoldProduct.put("soldProducts",soldQuantity);
                        mostSoldProduct.put("category",saleDetail.getProduct().getCategory().getName());
                        mostSoldProduct.put("stockQuantity",saleDetail.getProduct().getStockQuantity());
                        mostSoldProduct.put("lastRestock", getDateFromMilliSeconds(saleDetail.getProduct().getModifiedOn()));
                        mostSoldProducts.add(mostSoldProduct);
                    }
                }
                res.put("mostSold", mostSoldProducts);
                res.put("Message", "Fetched successfully");
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

    public Response getProduct_ById(Integer productId) {
        try {
            if (productId == null) {
                throw new BadRequestException("ProductId is null");
            }
            Product product = productDatabaseBean.getProduct_ById(productId);
            if (product == null) {
                throw new BadRequestException("This product does not exist");
            }
            HashMap<String, Object> productDetail = new HashMap<String, Object>();
            productDetailsHashMap(product, productDetail);
            HashMap<String, Object> res = new HashMap<>();
            res.put("product",productDetail);
            res.put("message", "Product deleted successfully");
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

    public Response getOutOfStockProducts(){
        try{
            List<Product> outOfStockProducts = productDatabaseBean.getOutOfStockProducts();
            HashMap<String, Object> res = new HashMap<>();
            if(outOfStockProducts.isEmpty()){
                res.put("message", "No out of stock products exists");
            }else{
                List<Object> outOfStockList = new ArrayList<>();
                saveProductInHashMap(outOfStockProducts, outOfStockList);
                res.put("products", outOfStockList);
                res.put("message", "Out of stock products fetched successfully");
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

    public Response restockProduct(Integer productId,Integer newStockQuantity){
        try {
            if(Objects.isNull(productId)){
                throw new BadRequestException("Product is null");
            }
            if(Objects.isNull(newStockQuantity)){
                throw new BadRequestException("New stock quantity is null");
            }
            Product product = productDatabaseBean.getProduct_ById(productId);
            if(Objects.isNull(product)){
                throw new BadRequestException("Product does not exist");
            }
            Date currentDate = new Date();
            int currentStock = product.getStockQuantity();
            Integer newStock = currentStock + newStockQuantity;
            Integer restockStatus = product.getRestockStatus();
            product.setStockQuantity(newStock);
            product.setModifiedOn(currentDate);
            if(newStock >= MIN_RESTOCK_ACTIVATION_NUMBER && restockStatus.equals(RESTOCK_STATUS_ON)){
                product.setRestockStatus(RESTOCK_STATUS_OFF);
            }
            NewStock newStockRecord = new NewStock();
            newStockRecord.setProduct(product);
            newStockRecord.setStock(newStockQuantity);
            newStockRecord.setDate(currentDate);

            if(!transactionProvider.createEntity(newStockRecord)){
                throw new PersistenceException("Stock record added successfully");
            }

            if(!transactionProvider.updateEntity(product)){
                throw new PersistenceException("Product stock was not updated successfully");
            }
            HashMap<String, Object> res = new HashMap<>();
            res.put("message", "Stock updated successfully");
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
