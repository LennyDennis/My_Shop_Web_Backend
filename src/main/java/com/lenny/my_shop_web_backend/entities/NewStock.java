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
@Table(name = "new_stock", catalog = "my_shop", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "NewStock.findAll", query = "SELECT n FROM NewStock n")
    , @NamedQuery(name = "NewStock.findById", query = "SELECT n FROM NewStock n WHERE n.id = :id")
    , @NamedQuery(name = "NewStock.findByStock", query = "SELECT n FROM NewStock n WHERE n.stock = :stock")
    , @NamedQuery(name = "NewStock.findByDate", query = "SELECT n FROM NewStock n WHERE n.date = :date")})
public class NewStock implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id", nullable = false)
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "stock", nullable = false)
    private int stock;
    @Basic(optional = false)
    @NotNull
    @Column(name = "date", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;
    @JoinColumn(name = "product", referencedColumnName = "id", nullable = false)
    @ManyToOne(optional = false)
    private Product product;

    public NewStock() {
    }

    public NewStock(Integer id) {
        this.id = id;
    }

    public NewStock(Integer id, int stock, Date date) {
        this.id = id;
        this.stock = stock;
        this.date = date;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof NewStock)) {
            return false;
        }
        NewStock other = (NewStock) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.lenny.my_shop_web_backend.entities.NewStock[ id=" + id + " ]";
    }
    
}
