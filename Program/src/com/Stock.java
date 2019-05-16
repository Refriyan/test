/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com;

/**
 *
 * @author Kefuoe
 */
import java.math.BigDecimal;
import java.sql.Date;

/**
 *
 * @author Kefuoe
 */
public class Stock {

    private int id;
    private String name;
    private int qte;
    private BigDecimal price;
    private byte[] Image;
    private int cod;

    public Stock() {
    }

    public Stock(int Id, int code, String Name, int Qte, BigDecimal Price, byte[] image) {

        this.id = Id;
        this.name = Name;
        this.qte = Qte;
        this.price = Price;
        this.Image = image;
        this.cod = code;

    }

    public int getID() {
        return id;
    }

    public void setID(int ID) {
        this.id = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String Name) {
        this.name = Name;
    }

    public int getQte() {
        return qte;
    }

    public void setQte(int Qte) {
        this.qte = Qte;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal Price) {
        this.price = Price;
    }

    public int getCode() {
        return cod;
    }
     public void getCode(int code) {
       this.cod=code;
    }

    public byte[] getMyImage() {
        return Image;
    }
}
