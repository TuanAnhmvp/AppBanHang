package com.tashop.appbanhang.activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import com.example.appbanhang.R;

import io.paperdb.Paper;

public class SplashActivity extends AppCompatActivity {

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setStatusBarColor(getColor(R.color.white));
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Paper.init(this);
        Thread thread = new Thread(){
            public void run (){
                try {
                    sleep(3000);
                }catch (Exception ex){

                }finally {
                    if(Paper.book().read("user") == null){
                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }else {
                        Intent home = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(home);
                        finish();
                    }


                }
            }
        };
        thread.start();
    }
}