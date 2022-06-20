package com.tashop.appbanhang.activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appbanhang.R;
import com.tashop.appbanhang.model.NotiSendData;
import com.tashop.appbanhang.retrofit.ApiBanHang;
import com.tashop.appbanhang.retrofit.ApiPushNofication;
import com.tashop.appbanhang.retrofit.RetrofitClient;
import com.tashop.appbanhang.retrofit.RetrofitClientNoti;
import com.tashop.appbanhang.utils.Utils;
import com.google.gson.Gson;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class OrderActivity extends AppCompatActivity {
    Toolbar toolbar;
    TextView txtongtien_order, txsodt_order, txemail_order;
    EditText edtdiachi;
    AppCompatButton btndathangorder;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    ApiBanHang apiBanHang;
    long tongtien;
    int totalItem;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setStatusBarColor(getColor(R.color.white));
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        initView();
        countItem();
        initControll();
    }

    private void countItem() {
        totalItem = 0;
        for (int i=0; i<Utils.mangmuahang.size(); i++){
            totalItem = totalItem+ Utils.mangmuahang.get(i).getSoluong();
        }
    }

    private void initControll() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###"+ "Đ");
        tongtien = getIntent().getLongExtra("tongtien", 0);
        txtongtien_order.setText(decimalFormat.format(tongtien));
        txemail_order.setText(Utils.user_current.getEmail());
        txsodt_order.setText(Utils.user_current.getMobile());


        btndathangorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str_diachi = edtdiachi.getText().toString().trim();
                if (TextUtils.isEmpty(str_diachi)){
                    Toast.makeText(getApplicationContext(), "Bạn chưa nhập địa chỉ", Toast.LENGTH_SHORT).show();

                }else {
                    // post data
                    String str_email = Utils.user_current.getEmail();
                    String str_sdt = Utils.user_current.getMobile();
                    int id = Utils.user_current.getId();
                    Log.d("Test", new Gson().toJson(Utils.mangmuahang));
                    compositeDisposable.add(apiBanHang.createOrder(str_email, str_sdt, String.valueOf(tongtien), id, str_diachi, totalItem, new Gson().toJson(Utils.mangmuahang))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            userModel -> {
                                pushNotiToUser();
                                Toast.makeText(getApplicationContext(), "Mua hàng thành công", Toast.LENGTH_SHORT).show();
                                Utils.mangmuahang.clear();
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(intent);
                                finish();

                            },
                            throwable -> {
                                Toast.makeText(getApplicationContext(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                    ));
                }
            }
        });
    }

    private void pushNotiToUser() {
        // gettoken

        compositeDisposable.add(apiBanHang.gettoken(1)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(
                userModel -> {
                    if(userModel.isSuccess()){
                        for(int i=0; i<userModel.getResult().size();i++){
                            Map<String, String > data = new HashMap<>();
                            data.put("title", "Thông báo");
                            data.put("body", "Bạn có đơn hàng mới");
                            NotiSendData notiSendData = new NotiSendData(userModel.getResult().get(i).getToken(), data);
                            ApiPushNofication apiPushNofication = RetrofitClientNoti.getInstance().create(ApiPushNofication.class);
                            compositeDisposable.add(apiPushNofication.sendNofitication(notiSendData)
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(
                                            notiResponse -> {

                                            },
                                            throwable -> {
                                                Log.d("log", throwable.getMessage());

                                            }

                                    ));
                        }
                    }

                },
                throwable -> {
                    Log.d("log", throwable.getMessage());

                }
        ));


    }

    private void initView() {
        apiBanHang = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiBanHang.class);
        toolbar = findViewById(R.id.toolbar_order);
        txtongtien_order = findViewById(R.id.tongtien_order);
        txemail_order = findViewById(R.id.email_thanhtoan);
        txsodt_order = findViewById(R.id.sodienthoai_order);
        edtdiachi = findViewById(R.id.edt_diachi);
        btndathangorder = findViewById(R.id.btndathang_order);

    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}