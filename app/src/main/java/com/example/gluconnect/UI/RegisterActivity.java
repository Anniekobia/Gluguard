package com.example.gluconnect.UI;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.gluconnect.Models.Error;
import com.example.gluconnect.Models.RegisterPOST;
import com.example.gluconnect.Models.RegisterResponse;
import com.example.gluconnect.R;
import com.example.gluconnect.Utils.LaravelAPI;
import com.example.gluconnect.Utils.LaravelAPIRetrofitClient;
import com.google.android.material.textfield.TextInputEditText;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static android.view.View.GONE;


public class RegisterActivity extends AppCompatActivity {

    View myview;
    private Button registerBtn;
    private TextInputEditText username;
    private TextInputEditText email;
    TextInputEditText password;
    TextView errorMsg;
    TextView loginTxtview;
    ConstraintLayout constraintLayout;
    private LaravelAPI laravelAPI;
    private ProgressBar progressBar;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Retrofit retrofit = LaravelAPIRetrofitClient.getRetrofitClient();
        laravelAPI = retrofit.create(LaravelAPI.class);
        sharedPreferences = getApplicationContext().getSharedPreferences("MyPreferences", 0); // 0 - for private mode
        SharedPreferences.Editor editor = sharedPreferences.edit();

        constraintLayout = findViewById(R.id.register_cl);
        loginTxtview = findViewById(R.id.login_textview);
        registerBtn = findViewById(R.id.signup_button);
        username = findViewById(R.id.username);
        email = findViewById(R.id.signup_email);
        errorMsg = findViewById(R.id.error_msg);
        password = findViewById(R.id.signup_password);
        progressBar = findViewById(R.id.progressBar);

        handleClicks();
    }


    public void handleClicks() {
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(constraintLayout.getWindowToken(), 0);
                } catch (Exception e) {

                }
                validateDetails();
            }
        });

        loginTxtview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent go = new Intent(RegisterActivity.this,MainActivity.class);
                go.putExtra("viewpager_position", 4);
                startActivity(go);
            }
        });
    }

    private void validateDetails() {
        String usrname = username.getText().toString();
        String emailu = email.getText().toString();
        String pwd = password.getText().toString();
        String userType = "Patient";
        if (TextUtils.isEmpty(usrname) || TextUtils.isEmpty(emailu) || TextUtils.isEmpty(pwd) ) {
            errorMsg.setText("Please fill in all the fields");
            errorMsg.setVisibility(View.VISIBLE);
        } else {
            RegisterPOST registerPOST = new RegisterPOST(emailu, pwd, pwd, userType, usrname);
            RegisterUser(registerPOST);
            errorMsg.setText("");
            errorMsg.setVisibility(View.GONE);
        }

    }

    public void RegisterUser(final RegisterPOST registerPOST) {
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        progressBar.setVisibility(View.VISIBLE);
        Call<RegisterResponse> registerResponseCall = laravelAPI.register(registerPOST);
        registerResponseCall.enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                if (!response.isSuccessful()) {
                    progressBar.setVisibility(GONE);
                    errorMsg.setText("Something happened, please try again later");
                    errorMsg.setVisibility(View.VISIBLE);
                } else {
                    RegisterResponse registerResponse = response.body();
                    progressBar.setVisibility(GONE);
                    Log.e("Res", registerResponse.getStatus().toString());
                    if (registerResponse.getStatus() == 0) {
                        Error error = registerResponse.getError();
                        List email = error.getEmail();
                        errorMsg.setText(email.get(0).toString());
                        errorMsg.setVisibility(View.VISIBLE);
                    } else {
                        String usrname = username.getText().toString();
                        String emailu = email.getText().toString();
                        String pwd = password.getText().toString();

                        editor.putInt("UserID", registerResponse.getStatus());
                        editor.putString("Username", usrname);
                        editor.putString("Email", emailu);
                        editor.putString("Password", pwd);
                        editor.commit();

                        Intent intent = new Intent(getApplicationContext(), DetailsActivity.class);
                        startActivity(intent);
                        password.setText("");
                        username.setText("");
                        email.setText("");
                    }
                }
            }

            @Override
            public void onFailure(Call<RegisterResponse> call, Throwable t) {
                progressBar.setVisibility(GONE);
                Log.e("Register", t.getMessage());
                errorMsg.setText("Something happened, please try again later");
                errorMsg.setVisibility(View.VISIBLE);
            }
        });
    }

}
