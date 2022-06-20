package com.tashop.appbanhang.model.EventBus;

import com.tashop.appbanhang.model.SanPhamMoi;

public class EditDeleteEvent {
    SanPhamMoi sanPhamMoi;

    public EditDeleteEvent(SanPhamMoi sanPhamMoi) {
        this.sanPhamMoi = sanPhamMoi;
    }

    public SanPhamMoi getSanPhamMoi() {
        return sanPhamMoi;
    }

    public void setSanPhamMoi(SanPhamMoi sanPhamMoi) {
        this.sanPhamMoi = sanPhamMoi;
    }
}
