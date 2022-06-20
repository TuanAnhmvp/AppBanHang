package com.tashop.appbanhang.activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.appbanhang.R;
import com.tashop.appbanhang.adapter.PhoneAdapter;
import com.tashop.appbanhang.model.SanPhamMoi;
import com.tashop.appbanhang.retrofit.ApiBanHang;
import com.tashop.appbanhang.retrofit.RetrofitClient;
import com.tashop.appbanhang.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class SearchActivity extends AppCompatActivity {
    Toolbar toolbar_search;
    RecyclerView recyclerView;
    EditText edt_search;
    PhoneAdapter adapterPhone;
    List<SanPhamMoi> sanPhamMoiList;
    ApiBanHang apiBanHang;
    CompositeDisposable compositeDisposable = new CompositeDisposable();

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setStatusBarColor(getColor(R.color.white));
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        initView();
        ActionToolBar();
    }

    private void initView() {
        sanPhamMoiList = new ArrayList<>();
        apiBanHang = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiBanHang.class);
        toolbar_search = findViewById(R.id.toolbar_search);
        edt_search = findViewById(R.id.edt_search);
        recyclerView = findViewById(R.id.recycleview_search);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

        edt_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() ==0 ){
                    sanPhamMoiList.clear();
                    adapterPhone = new PhoneAdapter(getApplicationContext(), sanPhamMoiList);
                    recyclerView.setAdapter(adapterPhone);
                }else {
                    getDataSearch(charSequence.toString());

                }

            }

            @Override
            public void afterTextChanged(Editable editable) {


            }
        });


    }

    private void getDataSearch(String s) {
        sanPhamMoiList.clear();

        compositeDisposable.add(apiBanHang.search(s)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(
                sanPhamMoiModel -> {
                    if(sanPhamMoiModel.isSuccess()){
                        sanPhamMoiList = sanPhamMoiModel.getResult();
                        adapterPhone = new PhoneAdapter(getApplicationContext(), sanPhamMoiList);
                        recyclerView.setAdapter(adapterPhone);
                    }

                },
                throwable -> {
                    Toast.makeText(getApplicationContext(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
                }
        ));
    }

    private void ActionToolBar() {
        setTitle("Tìm kiếm");
        setSupportActionBar(toolbar_search);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar_search.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}