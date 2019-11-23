package com.example.gluconnect.UI;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.example.gluconnect.Models.Error;
import com.example.gluconnect.Models.LoginPost;
import com.example.gluconnect.Models.LoginResponse;
import com.example.gluconnect.Models.RegisterPOST;
import com.example.gluconnect.Models.RegisterResponse;
import com.example.gluconnect.Models.UserDetailResponse;
import com.example.gluconnect.R;
import com.example.gluconnect.Utils.LaravelAPI;
import com.example.gluconnect.Utils.LaravelAPIRetrofitClient;
import com.google.android.material.textfield.TextInputEditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static android.view.View.GONE;

public class LoginActivity extends AppCompatActivity {

    private Button signinbtn;
    private TextInputEditText signinemail;
    private TextInputEditText signpass;
    private LaravelAPI laravelAPI;
    private TextView errormsg;
    private SharedPreferences sharedPreferences;
    private ProgressBar progressBar;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Retrofit retrofit = LaravelAPIRetrofitClient.getRetrofitClient();
        laravelAPI = retrofit.create(LaravelAPI.class);
        sharedPreferences= getApplicationContext().getSharedPreferences("MyPreferences", 0);
        editor = sharedPreferences.edit();

        signinbtn = findViewById(R.id.signin_button);
        signinemail = findViewById(R.id.signin_email);
        signpass = findViewById(R.id.signin_password);
        errormsg = findViewById(R.id.error_msg);
        progressBar = findViewById(R.id.progressBar);


        signinbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateDetails();
            }
        });

    }

    private void validateDetails() {
        String emailu = signinemail.getText().toString();
        String pwd = signpass.getText().toString();
        if (TextUtils.isEmpty(emailu)||TextUtils.isEmpty(pwd)){
            errormsg.setText("Please fill in all the fields");
            errormsg.setVisibility(View.VISIBLE);
        } else {
            LoginPost loginPost = new LoginPost(emailu,pwd);
            LoginUser(loginPost);
            errormsg.setText("");
            errormsg.setVisibility(View.GONE);
        }

    }

    public void LoginUser(final LoginPost loginPost) {
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        progressBar.setVisibility(View.VISIBLE);
        Call<LoginResponse> loginResponseCall = laravelAPI.login(loginPost);
        loginResponseCall.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                LoginResponse loginResponse = response.body();
                Log.e("log",loginResponse.getStatus().toString());
                if (loginResponse.getStatus()==0) {
                    errormsg.setText(loginResponse.getMessage());
                    errormsg.setVisibility(View.VISIBLE);
                } else {
                    int userid = loginResponse.getUser().getId().intValue();
                    editor.putString("Username",loginResponse.getUser().getUsername());
                    editor.putString("Email",loginResponse.getUser().getEmail());
                    editor.putString("Password",signpass.getText().toString());

                    getUserDetails(userid);
                    signinemail.setText("");
                    signpass.setText("");
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                progressBar.setVisibility(GONE);
                Log.e("Register", t.getMessage());
            }
        });
    }

    public  void getUserDetails(Integer userid){
        Call<UserDetailResponse> userDetailResponseCall = laravelAPI.getUserDetails(userid);
        userDetailResponseCall.enqueue(new Callback<UserDetailResponse>() {
            @Override
            public void onResponse(Call<UserDetailResponse> call, Response<UserDetailResponse> response) {
                UserDetailResponse userDetailResponse = response.body();
                progressBar.setVisibility(GONE);
                if (userDetailResponse.getSuccess()!=0) {
                    errormsg.setText("Something went wrong, try again later");
                    errormsg.setVisibility(View.VISIBLE);
                } else {

                    editor.putInt("UserID",userDetailResponse.getUserDetails().getUserId().intValue());
                    editor.putString("Hospital",userDetailResponse.getUserDetails().getmHospital());
                    editor.commit();

                    Intent intent = new Intent(getApplicationContext(), DailyLogsActivity.class);
                    startActivity(intent);
                    signinemail.setText("");
                    signpass.setText("");
                }
            }

            @Override
            public void onFailure(Call<UserDetailResponse> call, Throwable t) {
                progressBar.setVisibility(GONE);
                Log.e("Register", t.getMessage());
            }
        });
    }

    public void openRegisterActivity(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
