package com.tashop.appbanhang.model;

import java.io.Serializable;

public class SanPhamMoi implements Serializable {
    int id;
    String tensp;
    String hinhanhnew;
    String giasp;
    String mota;
    int loai;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTensp() {
        return tensp;
    }

    public void setTensp(String tensp) {
        this.tensp = tensp;
    }

    public String getHinhanhnew() {
        return hinhanhnew;
    }

    public void setHinhanhnew(String hinhanhnew) {
        this.hinhanhnew = hinhanhnew;
    }

    public String getGiasp() {
        return giasp;
    }

    public void setGiasp(String giasp) {
        this.giasp = giasp;
    }

    public String getMota() {
        return mota;
    }

    public void setMota(String mota) {
        this.mota = mota;
    }

    public int getLoai() {
        return loai;
    }

    public void setLoai(int loai) {
        this.loai = loai;
    }
}
