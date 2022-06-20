package com.tashop.appbanhang.activity;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;


import com.example.appbanhang.R;
import com.tashop.appbanhang.adapter.SanPhamMoiAdapter;
import com.tashop.appbanhang.model.EventBus.EditDeleteEvent;
import com.tashop.appbanhang.model.SanPhamMoi;
import com.tashop.appbanhang.retrofit.ApiBanHang;
import com.tashop.appbanhang.retrofit.RetrofitClient;
import com.tashop.appbanhang.utils.Utils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ManageActivity extends AppCompatActivity {
    ImageView img_add_mange;
    Toolbar toolbar_manage_home;
    RecyclerView recyclerView;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    ApiBanHang apiBanHang;
    List<SanPhamMoi> list;
    SanPhamMoiAdapter adapter;
    SanPhamMoi sanPhamSuaXoa;


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setStatusBarColor(getColor(R.color.white));
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage);
        apiBanHang = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiBanHang.class);
        intiView();
        initControll();
        getSpMoi();
        initToolbar();
    }

    private void initToolbar() {
        setTitle("Quản lý sản phẩm");
        setSupportActionBar(toolbar_manage_home);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar_manage_home.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }


    private void initControll() {
        img_add_mange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AddProductActivity.class);
                startActivity(intent);

            }
        });
    }

    private void getSpMoi() {
        compositeDisposable.add(apiBanHang.getSpMoi()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        sanPhamMoiModel -> {
                            if (sanPhamMoiModel.isSuccess()){
                                list = sanPhamMoiModel.getResult();
                                adapter = new SanPhamMoiAdapter(getApplicationContext(), list);
                                recyclerView.setAdapter(adapter);
                            }
                        },
                        throwable -> {
                            Toast.makeText(getApplicationContext(), "Không kết nối được với sever"+throwable.getMessage(), Toast.LENGTH_LONG).show();

                        }
                ));
    }

    private void intiView() {
        img_add_mange = findViewById(R.id.img_add_product);
        toolbar_manage_home = findViewById(R.id.toolbar_manage_home);
        recyclerView = findViewById(R.id.recycleview_manage);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        if (item.getTitle().equals("Sửa")){
            suaSanPham();
        }else if (item.getTitle().equals("Xóa")){
            xoaSanPham();
        }


        return super.onContextItemSelected(item);
    }

    private void xoaSanPham() {
        compositeDisposable.add(apiBanHang.xoaSanPham(sanPhamSuaXoa.getId())
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(
                messageModel -> {
                    if(messageModel.isSuccess()){
                        Toast.makeText(getApplicationContext(), "Xóa thành công", Toast.LENGTH_SHORT).show();
                        getSpMoi();
                    }else {
                        Toast.makeText(getApplicationContext(), messageModel.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                },
                throwable -> {
                    Log.d("log", throwable.getMessage());
                }

        ));
    }

    private void suaSanPham() {
        Intent intent = new Intent(getApplicationContext(), AddProductActivity.class);
        intent.putExtra("sua", sanPhamSuaXoa);
        startActivity(intent);
    }


    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void evenSuaXoa(EditDeleteEvent event){
        if (event != null){
            sanPhamSuaXoa = event.getSanPhamMoi();
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}