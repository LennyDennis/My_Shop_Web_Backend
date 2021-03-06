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
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author lenny
 */
@Entity
@Table(name = "sale", catalog = "my_shop", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Sale.findAll", query = "SELECT s FROM Sale s")
    , @NamedQuery(name = "Sale.findById", query = "SELECT s FROM Sale s WHERE s.id = :id")
    , @NamedQuery(name = "Sale.findByTotalCost", query = "SELECT s FROM Sale s WHERE s.totalCost = :totalCost")
    , @NamedQuery(name = "Sale.findByCashPaid", query = "SELECT s FROM Sale s WHERE s.cashPaid = :cashPaid")
    , @NamedQuery(name = "Sale.findByBalance", query = "SELECT s FROM Sale s WHERE s.balance = :balance")
    , @NamedQuery(name = "Sale.findByModifiedOn", query = "SELECT s FROM Sale s WHERE s.modifiedOn = :modifiedOn")
    , @NamedQuery(name = "Sale.findBySellDate", query = "SELECT s FROM Sale s WHERE s.sellDate = :sellDate")})
public class Sale implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id", nullable = false)
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "total_cost", nullable = false)
    private float totalCost;
    @Basic(optional = false)
    @NotNull
    @Column(name = "cash_paid", nullable = false)
    private float cashPaid;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "balance", precision = 12)
    private Float balance;
    @Basic(optional = false)
    @NotNull
    @Column(name = "modified_on", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifiedOn;
    @Basic(optional = false)
    @NotNull
    @Column(name = "sell_date", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date sellDate;
    @JoinColumn(name = "seller", referencedColumnName = "id", nullable = false)
    @ManyToOne(optional = false)
    private User seller;
    @JoinColumn(name = "customer", referencedColumnName = "id")
    @ManyToOne
    private User customer;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "sale")
    private List<SaleDetail> saleDetailList;

    public Sale() {
    }

    public Sale(Integer id) {
        this.id = id;
    }

    public Sale(Integer id, float totalCost, float cashPaid, Date modifiedOn, Date sellDate) {
        this.id = id;
        this.totalCost = totalCost;
        this.cashPaid = cashPaid;
        this.modifiedOn = modifiedOn;
        this.sellDate = sellDate;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public float getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(float totalCost) {
        this.totalCost = totalCost;
    }

    public float getCashPaid() {
        return cashPaid;
    }

    public void setCashPaid(float cashPaid) {
        this.cashPaid = cashPaid;
    }

    public Float getBalance() {
        return balance;
    }

    public void setBalance(Float balance) {
        this.balance = balance;
    }

    public Date getModifiedOn() {
        return modifiedOn;
    }

    public void setModifiedOn(Date modifiedOn) {
        this.modifiedOn = modifiedOn;
    }

    public Date getSellDate() {
        return sellDate;
    }

    public void setSellDate(Date sellDate) {
        this.sellDate = sellDate;
    }

    public User getSeller() {
        return seller;
    }

    public void setSeller(User seller) {
        this.seller = seller;
    }

    public User getCustomer() {
        return customer;
    }

    public void setCustomer(User customer) {
        this.customer = customer;
    }

    @XmlTransient
    public List<SaleDetail> getSaleDetailList() {
        return saleDetailList;
    }

    public void setSaleDetailList(List<SaleDetail> saleDetailList) {
        this.saleDetailList = saleDetailList;
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
        if (!(object instanceof Sale)) {
            return false;
        }
        Sale other = (Sale) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.lenny.my_shop_web_backend.entities.Sale[ id=" + id + " ]";
    }
    
}
