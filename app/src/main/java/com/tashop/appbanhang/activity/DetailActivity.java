package com.tashop.appbanhang.activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.appbanhang.R;
import com.tashop.appbanhang.model.Cart;
import com.tashop.appbanhang.model.SanPhamMoi;
import com.tashop.appbanhang.utils.Utils;
import com.nex3z.notificationbadge.NotificationBadge;

import java.text.DecimalFormat;

public class DetailActivity extends AppCompatActivity {
    TextView namedetail, pricedetail, descriptiondetail;
    Button btaddcart;
    ImageView image_detail;
    Spinner spinner;
    Toolbar toolbar;
    SanPhamMoi sanPhamMoi;
    NotificationBadge badge;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setStatusBarColor(getColor(R.color.white));
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        initView();
        ActionToolBar();
        intitData();
        initControl();
    }

    private void initControl() {
        btaddcart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                themGioHang();
            }
        });
    }

    private void themGioHang() {
        if (Utils.manggiohang.size() > 0){
            boolean flag = false;
            int soluong = Integer.parseInt(spinner.getSelectedItem().toString());
            for (int i = 0; i< Utils.manggiohang.size(); i++){
                if (Utils.manggiohang.get(i).getIdproduct() == sanPhamMoi.getId()){
                    Utils.manggiohang.get(i).setSoluong(soluong + Utils.manggiohang.get(i).getSoluong());

                    flag = true;
                }
            }
            if (flag == false){

                long gia = Long.parseLong(sanPhamMoi.getGiasp());
                Cart gioHang = new Cart();
                gioHang.setPriceproduct(gia);
                gioHang.setSoluong(soluong);
                gioHang.setIdproduct(sanPhamMoi.getId());
                gioHang.setNameproduct(sanPhamMoi.getTensp());
                gioHang.setImgeproduct(sanPhamMoi.getHinhanhnew());
                Utils.manggiohang.add(gioHang);
            }

        }else {
            int soluong = Integer.parseInt(spinner.getSelectedItem().toString());
            long gia = Long.parseLong(sanPhamMoi.getGiasp());
            Cart gioHang = new Cart();
            gioHang.setPriceproduct(gia);
            gioHang.setSoluong(soluong);
            gioHang.setIdproduct(sanPhamMoi.getId());
            gioHang.setNameproduct(sanPhamMoi.getTensp());
            gioHang.setImgeproduct(sanPhamMoi.getHinhanhnew());
            Utils.manggiohang.add(gioHang);
        }
        int totalItem = 0;
        for (int i=0; i<Utils.manggiohang.size(); i++){
            totalItem = totalItem+ Utils.manggiohang.get(i).getSoluong();
        }
        badge.setText(String.valueOf(totalItem));
    }


    private void intitData() {
        sanPhamMoi = (SanPhamMoi) getIntent().getSerializableExtra("chitiet");
        namedetail.setText(sanPhamMoi.getTensp());
        descriptiondetail.setText(sanPhamMoi.getMota());
        Glide.with(getApplicationContext()).load(sanPhamMoi.getHinhanhnew()).into(image_detail);
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        pricedetail.setText("Giá: "+decimalFormat.format(Double.parseDouble(sanPhamMoi.getGiasp()))+ "Đ" );
        Integer[] so = new Integer[]{1,2,3,4,5,6,7,8,9,10};
        ArrayAdapter<Integer> adapterspin = new ArrayAdapter<>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, so);
        spinner.setAdapter(adapterspin);

    }

    private void initView() {
        namedetail = findViewById(R.id.tx_name_detail);
        pricedetail = findViewById(R.id.tx_detail_price);
        descriptiondetail = findViewById(R.id.tx_detail_description);
        btaddcart = findViewById(R.id.bt_detail_add_cart);
        spinner = findViewById(R.id.spiner_detail);
        image_detail = findViewById(R.id.image_detail);
        toolbar = findViewById(R.id.toobar_detail);
        badge = findViewById(R.id.menu_soluong);
        FrameLayout frameLayoutgiohang = findViewById(R.id.framegiohang);
        frameLayoutgiohang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent giohang = new Intent(getApplicationContext(), CartActivity.class);
                startActivity(giohang);
            }
        });

        if (Utils.manggiohang != null){
            int totalItem = 0;
            for (int i=0; i<Utils.manggiohang.size(); i++){
                totalItem = totalItem+ Utils.manggiohang.get(i).getSoluong();
            }
            badge.setText(String.valueOf(totalItem));
        }
    }

    private void ActionToolBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Utils.manggiohang != null){
            int totalItem = 0;
            for (int i=0; i<Utils.manggiohang.size(); i++){
                totalItem = totalItem+ Utils.manggiohang.get(i).getSoluong();
            }
            badge.setText(String.valueOf(totalItem));
        }
    }
}