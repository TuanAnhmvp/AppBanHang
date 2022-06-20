package com.tashop.appbanhang.activity;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
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


public class PhoneActivity extends AppCompatActivity {
    Toolbar toolbar;
    RecyclerView recyclerView;
    ApiBanHang apiBanHang;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    int page =1;
    int loai;
    PhoneAdapter adapterPhone;
    List<SanPhamMoi> sanPhamMoiList;
    LinearLayoutManager linearLayoutManager;
    Handler handler = new Handler();
    boolean isLoading = false;


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setStatusBarColor(getColor(R.color.white));
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone);
        apiBanHang = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiBanHang.class);
        loai = getIntent().getIntExtra("loai", 1);

        AnhXa();
        ActionToolBar();
        getData(page);
        addEventLoad();
    }

    private void addEventLoad() {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (isLoading == false){
                    if (linearLayoutManager.findLastCompletelyVisibleItemPosition() == sanPhamMoiList.size()-1){
                        isLoading = true;
                        loadMore();
                    }
                }
            }
        });
    }

    private void loadMore() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                // add null
                sanPhamMoiList.add(null);
                adapterPhone.notifyItemInserted(sanPhamMoiList.size()-1);
            }
        });
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // remover null
                sanPhamMoiList.remove(sanPhamMoiList.size()-1);
                adapterPhone.notifyItemRemoved(sanPhamMoiList.size());
                page = page+1;
                getData(page);
                adapterPhone.notifyDataSetChanged();
                isLoading = false;
            }
        }, 2000);
    }

    private void getData(int page) {
        compositeDisposable.add(apiBanHang.getProduct_phone(page, loai)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(
                sanPhamMoiModel -> {
                    if (sanPhamMoiModel.isSuccess()){
                        if (adapterPhone == null) {
                            sanPhamMoiList = sanPhamMoiModel.getResult();
                            adapterPhone = new PhoneAdapter(getApplicationContext(), sanPhamMoiList);
                            recyclerView.setAdapter(adapterPhone);
                        }else {
                            int vitri = sanPhamMoiList.size()-1;
                            int soluongadd = sanPhamMoiModel.getResult().size();
                            for (int i = 0; i<soluongadd; i++){
                                sanPhamMoiList.add(sanPhamMoiModel.getResult().get(i));
                            }
                            adapterPhone.notifyItemRangeInserted(vitri, soluongadd);

                        }
                    }else {
                        Toast.makeText(getApplicationContext(), "hết dữ liệu", Toast.LENGTH_LONG).show();
                        isLoading = true;
                    }

                },
                throwable -> {
                    Toast.makeText(getApplicationContext(), "Không kết nối được với sever", Toast.LENGTH_LONG).show();

                }
        ));
    }

    private void ActionToolBar() {
        setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }


    private void AnhXa() {
        toolbar = findViewById(R.id.toolbar_phone);
        recyclerView = findViewById(R.id.recycleview_phone);
        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        sanPhamMoiList = new ArrayList<>();

    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}