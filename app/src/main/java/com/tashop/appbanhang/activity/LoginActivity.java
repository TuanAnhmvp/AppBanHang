package com.tashop.appbanhang.activity;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appbanhang.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.tashop.appbanhang.retrofit.ApiBanHang;
import com.tashop.appbanhang.retrofit.RetrofitClient;
import com.tashop.appbanhang.utils.Utils;

import io.paperdb.Paper;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class LoginActivity extends AppCompatActivity {
    TextView txtdangky, txtresetpass;
    EditText email_login, pass_login;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    AppCompatButton btndangnhap;
    ApiBanHang apiBanHang;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    boolean isLogin = false;


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setStatusBarColor(getColor(R.color.white));
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        initControll();
    }

    private void initControll() {
        txtdangky.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
                startActivity(intent);
            }
        });

        txtresetpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ResetPassActivity.class);
                startActivity(intent);
            }
        });

        btndangnhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str_email_login = email_login.getText().toString().trim();
                String str_pass_login = pass_login.getText().toString().trim();
                if (TextUtils.isEmpty(str_email_login)) {
                    Toast.makeText(getApplicationContext(), "Bạn chưa nhập Email", Toast.LENGTH_SHORT).show();
                }else if (TextUtils.isEmpty(str_pass_login)) {
                    Toast.makeText(getApplicationContext(), "Bạn chưa nhập Password", Toast.LENGTH_SHORT).show();
                }else {
                    //save tk mk
                    Paper.book().write("email", str_email_login);
                    Paper.book().write("pass", str_pass_login);
                    if (user != null){
                        // user da co dang nhap firebase
                        dangNhap(str_email_login, str_pass_login);
                    }else {
                        // user da sigout
                        firebaseAuth.signInWithEmailAndPassword(str_email_login, str_pass_login)
                                .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()){
                                            dangNhap(str_email_login, str_pass_login);
                                        }
                                    }
                                });

                    }
                }
            }
        });
    }

    private void initView() {
        Paper.init(this);
        apiBanHang = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiBanHang.class);
        txtdangky = findViewById(R.id.txtdangky);
        txtresetpass = findViewById(R.id.txtresetpass);
        email_login = findViewById(R.id.txtEmail_login);
        pass_login = findViewById(R.id.txt_pass_login);
        btndangnhap = findViewById(R.id.btndangnhap);
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();

        // read data
        if (Paper.book().read("email") != null && Paper.book().read("pass") !=null){
            email_login.setText(Paper.book().read("email"));
            pass_login.setText(Paper.book().read("pass"));
            if (Paper.book().read("islogin") !=null){
                boolean flag = Paper.book().read("islogin");
                if (flag){
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            //dangNhap(Paper.book().read("email"), Paper.book().read("pass"));

                        }
                    },1000);
                }
            }
        }
    }

    private void dangNhap(String email, String pass) {
        compositeDisposable.add(apiBanHang.dangnhap(email, pass)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        userModel -> {
                            if (userModel.isSuccess()){
                                isLogin = true;
                                Paper.book().write("islogin", isLogin);

                                Utils.user_current = userModel.getResult().get(0);
                                //luu lai thong tin nguoi dung
                                Paper.book().write("user", userModel.getResult().get(0));
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(intent);
                                finish();
                            }else{
                                Toast.makeText(getApplicationContext(), "Email hoặc Password không chính xác", Toast.LENGTH_SHORT).show();
                            }

                        },
                        throwable -> {
                            Toast.makeText(getApplicationContext(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                ));

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Utils.user_current.getEmail() != null && Utils.user_current.getPass() !=null){
            email_login.setText(Utils.user_current.getEmail());
            pass_login.setText(Utils.user_current.getPass());

        }
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}