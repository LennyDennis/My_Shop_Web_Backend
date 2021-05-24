/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lenny.my_shop_web_backend.entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author lenny
 */
@Entity
@Table(name = "sale_detail", catalog = "my_shop", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SaleDetail.findAll", query = "SELECT s FROM SaleDetail s")
    , @NamedQuery(name = "SaleDetail.findById", query = "SELECT s FROM SaleDetail s WHERE s.id = :id")
    , @NamedQuery(name = "SaleDetail.findBySellingPrice", query = "SELECT s FROM SaleDetail s WHERE s.sellingPrice = :sellingPrice")
    , @NamedQuery(name = "SaleDetail.findBySoldQuantity", query = "SELECT s FROM SaleDetail s WHERE s.soldQuantity = :soldQuantity")
    , @NamedQuery(name = "SaleDetail.findByModifiedOn", query = "SELECT s FROM SaleDetail s WHERE s.modifiedOn = :modifiedOn")
    , @NamedQuery(name = "SaleDetail.findByDate", query = "SELECT s FROM SaleDetail s WHERE s.date = :date")})
public class SaleDetail implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id", nullable = false)
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "selling_price", nullable = false)
    private float sellingPrice;
    @Basic(optional = false)
    @NotNull
    @Column(name = "sold_quantity", nullable = false)
    private int soldQuantity;
    @Basic(optional = false)
    @NotNull
    @Column(name = "modified_on", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifiedOn;
    @Basic(optional = false)
    @NotNull
    @Column(name = "date", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;
    @JoinColumn(name = "product", referencedColumnName = "id", nullable = false)
    @ManyToOne(optional = false)
    private Product product;
    @JoinColumn(name = "sale", referencedColumnName = "id", nullable = false)
    @ManyToOne(optional = false)
    private Sale sale;

    public SaleDetail() {
    }

    public SaleDetail(Integer id) {
        this.id = id;
    }

    public SaleDetail(Integer id, float sellingPrice, int soldQuantity, Date modifiedOn, Date date) {
        this.id = id;
        this.sellingPrice = sellingPrice;
        this.soldQuantity = soldQuantity;
        this.modifiedOn = modifiedOn;
        this.date = date;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public float getSellingPrice() {
        return sellingPrice;
    }

    public void setSellingPrice(float sellingPrice) {
        this.sellingPrice = sellingPrice;
    }

    public int getSoldQuantity() {
        return soldQuantity;
    }

    public void setSoldQuantity(int soldQuantity) {
        this.soldQuantity = soldQuantity;
    }

    public Date getModifiedOn() {
        return modifiedOn;
    }

    public void setModifiedOn(Date modifiedOn) {
        this.modifiedOn = modifiedOn;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Sale getSale() {
        return sale;
    }

    public void setSale(Sale sale) {
        this.sale = sale;
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
        if (!(object instanceof SaleDetail)) {
            return false;
        }
        SaleDetail other = (SaleDetail) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.lenny.my_shop_web_backend.entities.SaleDetail[ id=" + id + " ]";
    }
    
}
