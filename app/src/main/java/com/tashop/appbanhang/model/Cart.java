package com.tashop.appbanhang.model;

public class Cart {
    int idproduct;
    String nameproduct;
    long priceproduct;
    String imgeproduct;
    int soluong;

    public Cart() {
    }

    public int getIdproduct() {
        return idproduct;
    }

    public void setIdproduct(int idproduct) {
        this.idproduct = idproduct;
    }

    public String getNameproduct() {
        return nameproduct;
    }

    public void setNameproduct(String nameproduct) {
        this.nameproduct = nameproduct;
    }

    public long getPriceproduct() {
        return priceproduct;
    }

    public void setPriceproduct(long priceproduct) {
        this.priceproduct = priceproduct;
    }

    public String getImgeproduct() {
        return imgeproduct;
    }

    public void setImgeproduct(String imgeproduct) {
        this.imgeproduct = imgeproduct;
    }

    public int getSoluong() {
        return soluong;
    }

    public void setSoluong(int soluong) {
        this.soluong = soluong;
    }
}
