package com.tashop.appbanhang.activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Build;
import android.os.Bundle;
import android.view.View;

import com.example.appbanhang.R;

public class ContactActivity extends AppCompatActivity {
    Toolbar toolbar_contact;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setStatusBarColor(getColor(R.color.white));
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        Anhxa();
        ActionToolBar();
    }

    private void Anhxa() {
        toolbar_contact = findViewById(R.id.toolbar_contact);
    }

    private void ActionToolBar() {
        setTitle("Liên hệ");
        setSupportActionBar(toolbar_contact);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar_contact.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}