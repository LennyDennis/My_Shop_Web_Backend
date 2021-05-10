/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lenny.my_shop_web_backend.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author lenny
 */
@Entity
@Table(name = "product", catalog = "my_shop", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Product.findAll", query = "SELECT p FROM Product p")
    , @NamedQuery(name = "Product.findById", query = "SELECT p FROM Product p WHERE p.id = :id")
    , @NamedQuery(name = "Product.findByBuyingPrice", query = "SELECT p FROM Product p WHERE p.buyingPrice = :buyingPrice")
    , @NamedQuery(name = "Product.findBySellingPrice", query = "SELECT p FROM Product p WHERE p.sellingPrice = :sellingPrice")
    , @NamedQuery(name = "Product.findByMaxDiscount", query = "SELECT p FROM Product p WHERE p.maxDiscount = :maxDiscount")
    , @NamedQuery(name = "Product.findByStockQuantity", query = "SELECT p FROM Product p WHERE p.stockQuantity = :stockQuantity")
    , @NamedQuery(name = "Product.findByActivationStatus", query = "SELECT p FROM Product p WHERE p.activationStatus = :activationStatus")
    , @NamedQuery(name = "Product.findByDeletionStatus", query = "SELECT p FROM Product p WHERE p.deletionStatus = :deletionStatus")
    , @NamedQuery(name = "Product.findByRestockStatus", query = "SELECT p FROM Product p WHERE p.restockStatus = :restockStatus")
    , @NamedQuery(name = "Product.findByModifiedOn", query = "SELECT p FROM Product p WHERE p.modifiedOn = :modifiedOn")})
public class Product implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id", nullable = false)
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Lob
    @Size(min = 1, max = 65535)
    @Column(name = "name", nullable = false, length = 65535)
    private String name;
    @Basic(optional = false)
    @NotNull
    @Column(name = "buying_price", nullable = false)
    private float buyingPrice;
    @Basic(optional = false)
    @NotNull
    @Column(name = "selling_price", nullable = false)
    private float sellingPrice;
    @Basic(optional = false)
    @NotNull
    @Column(name = "max_discount", nullable = false)
    private float maxDiscount;
    @Basic(optional = false)
    @NotNull
    @Column(name = "stock_quantity", nullable = false)
    private int stockQuantity;
    @Basic(optional = false)
    @NotNull
    @Column(name = "activation_status", nullable = false)
    private int activationStatus;
    @Basic(optional = false)
    @NotNull
    @Column(name = "deletion_status", nullable = false)
    private int deletionStatus;
    @Basic(optional = false)
    @NotNull
    @Column(name = "restock_status", nullable = false)
    private int restockStatus;
    @Basic(optional = false)
    @NotNull
    @Column(name = "modified_on", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifiedOn;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "product")
    private List<Notification> notificationList;
    @JoinColumn(name = "category", referencedColumnName = "id", nullable = false)
    @ManyToOne(optional = false)
    private Category category;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "product")
    private List<NewStock> newStockList;

    public Product() {
    }

    public Product(Integer id) {
        this.id = id;
    }

    public Product(Integer id, String name, float buyingPrice, float sellingPrice, float maxDiscount, int stockQuantity, int activationStatus, int deletionStatus, int restockStatus, Date modifiedOn) {
        this.id = id;
        this.name = name;
        this.buyingPrice = buyingPrice;
        this.sellingPrice = sellingPrice;
        this.maxDiscount = maxDiscount;
        this.stockQuantity = stockQuantity;
        this.activationStatus = activationStatus;
        this.deletionStatus = deletionStatus;
        this.restockStatus = restockStatus;
        this.modifiedOn = modifiedOn;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getBuyingPrice() {
        return buyingPrice;
    }

    public void setBuyingPrice(float buyingPrice) {
        this.buyingPrice = buyingPrice;
    }

    public float getSellingPrice() {
        return sellingPrice;
    }

    public void setSellingPrice(float sellingPrice) {
        this.sellingPrice = sellingPrice;
    }

    public float getMaxDiscount() {
        return maxDiscount;
    }

    public void setMaxDiscount(float maxDiscount) {
        this.maxDiscount = maxDiscount;
    }

    public int getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(int stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public int getActivationStatus() {
        return activationStatus;
    }

    public void setActivationStatus(int activationStatus) {
        this.activationStatus = activationStatus;
    }

    public int getDeletionStatus() {
        return deletionStatus;
    }

    public void setDeletionStatus(int deletionStatus) {
        this.deletionStatus = deletionStatus;
    }

    public int getRestockStatus() {
        return restockStatus;
    }

    public void setRestockStatus(int restockStatus) {
        this.restockStatus = restockStatus;
    }

    public Date getModifiedOn() {
        return modifiedOn;
    }

    public void setModifiedOn(Date modifiedOn) {
        this.modifiedOn = modifiedOn;
    }

    @XmlTransient
    public List<Notification> getNotificationList() {
        return notificationList;
    }

    public void setNotificationList(List<Notification> notificationList) {
        this.notificationList = notificationList;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    @XmlTransient
    public List<NewStock> getNewStockList() {
        return newStockList;
    }

    public void setNewStockList(List<NewStock> newStockList) {
        this.newStockList = newStockList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Product)) {
            return false;
        }
        Product other = (Product) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.lenny.my_shop_web_backend.entities.Product[ id=" + id + " ]";
    }
    
}
