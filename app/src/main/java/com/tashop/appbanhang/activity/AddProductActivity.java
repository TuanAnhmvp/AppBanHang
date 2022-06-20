package com.tashop.appbanhang.activity;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.appbanhang.R;
import com.example.appbanhang.databinding.ActivityAddProductBinding;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.tashop.appbanhang.model.MessageModel;
import com.tashop.appbanhang.model.SanPhamMoi;
import com.tashop.appbanhang.retrofit.ApiBanHang;
import com.tashop.appbanhang.retrofit.RetrofitClient;
import com.tashop.appbanhang.utils.Utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddProductActivity extends AppCompatActivity {
    Spinner spinner;
    int loai = 0;
    ActivityAddProductBinding binding;
    ApiBanHang apiBanHang;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    String mediaPath;
    SanPhamMoi sanPhamSua;
    boolean flag = false;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setStatusBarColor(getColor(R.color.white));
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        super.onCreate(savedInstanceState);
        binding = ActivityAddProductBinding.inflate(getLayoutInflater());
        apiBanHang = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiBanHang.class);
        setContentView(binding.getRoot());
        initView();
        initData();
        ActionToolBar();


        Intent intent = getIntent();
        sanPhamSua = (SanPhamMoi) intent.getSerializableExtra("sua");


        if (sanPhamSua == null) {
            // them
            flag = false;
        }else {
            // sua


            flag = true;
            binding.btnAddpr.setText("Sửa sản phẩm");
            // show data
            binding.motaAddpr.setText(sanPhamSua.getMota());
            binding.giaspAddpr.setText(sanPhamSua.getGiasp()+"");
            binding.tenspAddpr.setText(sanPhamSua.getTensp());
            binding.hinhanhAddpr.setText(sanPhamSua.getHinhanhnew());
            binding.spinerAddPr.setSelection(sanPhamSua.getLoai());

        }

    }

    private void ActionToolBar() {
        setTitle("");
        setSupportActionBar(binding.toolbarAddProduct);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        binding.toolbarAddProduct.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }


    private void initData() {
        List<String> stringList = new ArrayList<>();
        stringList.add("Vui lòng chọn loại");
        stringList.add("Điện thoại");
        stringList.add("Laptop");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, stringList);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                loai = i;

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        binding.btnAddpr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (flag == false){
                    themsanpham();
                }else {
                    suaSanPham();
                }

            }
        });

        binding.cameraAddpr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImagePicker.with(AddProductActivity.this)
                        .crop()	    			//Crop image(Optional), Check Customization for more option
                        .compress(1024)			//Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                        .start();

            }
        });

    }

    private void suaSanPham() {
        String str_ten_addpr =  binding.tenspAddpr.getText().toString().trim();
        String str_gia_addpr =  binding.giaspAddpr.getText().toString().trim();
        String str_mota_addpr =  binding.motaAddpr.getText().toString().trim();
        String str_hinhanh_addpr =  binding.hinhanhAddpr.getText().toString().trim();
        if (TextUtils.isEmpty(str_ten_addpr) || TextUtils.isEmpty(str_gia_addpr) || TextUtils.isEmpty(str_mota_addpr) || TextUtils.isEmpty(str_hinhanh_addpr) || loai == 0){
            Toast.makeText(getApplicationContext(), "Vui lòng nhập đủ thông tin", Toast.LENGTH_SHORT).show();
        }else {
            compositeDisposable.add(apiBanHang.updatesp(str_ten_addpr, str_gia_addpr, str_hinhanh_addpr, str_mota_addpr, loai, sanPhamSua.getId())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            messageModel -> {
                                if(messageModel.isSuccess()){
                                    Toast.makeText(getApplicationContext(), "Sửa thành công", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(getApplicationContext(), ManageActivity.class);
                                    startActivity(intent);
                                    finish();
                                }else {
                                    Toast.makeText(getApplicationContext(), messageModel.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            },
                            throwable -> {
                                Toast.makeText(getApplicationContext(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                    ));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mediaPath = data.getDataString();
        uploadMultipleFiles();
        Log.d("log", "onActivityResult: "+mediaPath);
    }

    private void themsanpham() {
        String str_ten_addpr =  binding.tenspAddpr.getText().toString().trim();
        String str_gia_addpr =  binding.giaspAddpr.getText().toString().trim();
        String str_mota_addpr =  binding.motaAddpr.getText().toString().trim();
        String str_hinhanh_addpr =  binding.hinhanhAddpr.getText().toString().trim();
        if (TextUtils.isEmpty(str_ten_addpr) || TextUtils.isEmpty(str_gia_addpr) || TextUtils.isEmpty(str_mota_addpr) || TextUtils.isEmpty(str_hinhanh_addpr) || loai == 0){
            Toast.makeText(getApplicationContext(), "Vui lòng nhập đủ thông tin", Toast.LENGTH_SHORT).show();
        }else {
            compositeDisposable.add(apiBanHang.insertSp(str_ten_addpr, str_gia_addpr, str_hinhanh_addpr, str_mota_addpr, (loai))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                    messageModel -> {
                        if(messageModel.isSuccess()){
                            Toast.makeText(getApplicationContext(), "Thêm thành công", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), ManageActivity.class);
                            startActivity(intent);
                            finish();
                        }else {
                            Toast.makeText(getApplicationContext(), messageModel.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    },
                    throwable -> {
                        Toast.makeText(getApplicationContext(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
                    }
            ));
        }
    }
    private String getPath(Uri uri){
        String result;
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        if (cursor == null){
            result = uri.getPath();
        }else {
            cursor.moveToFirst();
            int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(index);
            cursor.close();
        }
        return result;
    }


    // Uploading Image/Video
    private void uploadMultipleFiles() {
        Uri uri = Uri.parse(mediaPath);


        File file = new File(getPath(uri));
        RequestBody requestBody1 = RequestBody.create(MediaType.parse("*/*"), file);
        MultipartBody.Part fileToUpload1 = MultipartBody.Part.createFormData("file", file.getName(), requestBody1);
        Call<MessageModel> call = apiBanHang.uploadFile(fileToUpload1);
        call.enqueue(new Callback< MessageModel >() {
            @Override
            public void onResponse(Call < MessageModel > call, Response< MessageModel > response) {
                MessageModel serverResponse = response.body();
                if (serverResponse != null) {
                    if (serverResponse.isSuccess()) {
                        binding.hinhanhAddpr.setText(serverResponse.getName());

                    } else {
                        Toast.makeText(getApplicationContext(), serverResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.v("Response", serverResponse.toString());
                }
            }
            @Override
            public void onFailure(Call < MessageModel > call, Throwable t) {
                Log.d("log", t.getMessage());

            }
        });
    }

    private void initView() {
        spinner = findViewById(R.id.spiner_add_pr);
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}