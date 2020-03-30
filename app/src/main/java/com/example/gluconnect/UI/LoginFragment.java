package com.example.gluconnect.UI;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.example.gluconnect.Models.LoginPost;
import com.example.gluconnect.Models.LoginResponse;
import com.example.gluconnect.Models.UserDetailResponse;
import com.example.gluconnect.R;
import com.example.gluconnect.Utils.LaravelAPI;
import com.example.gluconnect.Utils.LaravelAPIRetrofitClient;
import com.google.android.material.textfield.TextInputEditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static android.view.View.GONE;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment {

    View myview;
    private Button signinbtn;
    private TextInputEditText signinemail;
    private TextInputEditText signpass;
    private TextView register_textview;
    private LaravelAPI laravelAPI;
    private TextView errormsg;
    private SharedPreferences sharedPreferences;
    private ProgressBar progressBar;
    SharedPreferences.Editor editor;
    private Bundle extras;
    private ConstraintLayout constraintLayout;
    private static final String IS_LOGIN = "IsLoggedIn";


    public LoginFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myview = inflater.inflate(R.layout.fragment_login, container, false);

        Retrofit retrofit = LaravelAPIRetrofitClient.getRetrofitClient();
        laravelAPI = retrofit.create(LaravelAPI.class);
        sharedPreferences = getContext().getSharedPreferences("MyPreferences", 0);
        editor = sharedPreferences.edit();


        constraintLayout = myview.findViewById(R.id.login_cl);
        signinbtn = myview.findViewById(R.id.signin_button);
        signinemail = myview.findViewById(R.id.signin_email);
        signpass = myview.findViewById(R.id.signin_password);
        errormsg = myview.findViewById(R.id.error_msg);
        progressBar = myview.findViewById(R.id.progressBar);
        register_textview = myview.findViewById(R.id.register_textview);
//        extras = getIntent().getExtras();


        signinbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(constraintLayout.getWindowToken(), 0);
                } catch (Exception e) {

                }

                validateDetails();
            }
        });

        register_textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),RegisterActivity.class);
                startActivity(intent);
            }
        });
        return myview;
    }

    private void validateDetails() {
        String emailu = signinemail.getText().toString();
        String pwd = signpass.getText().toString();
        if (TextUtils.isEmpty(emailu) || TextUtils.isEmpty(pwd)) {
            errormsg.setText("Please fill in all the fields");
            errormsg.setVisibility(View.VISIBLE);
        } else {
            LoginPost loginPost = new LoginPost(pwd, emailu);
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
                if (!response.isSuccessful()) {
                    progressBar.setVisibility(GONE);
                    errormsg.setText("Something happened, please try again later");
                    errormsg.setVisibility(View.VISIBLE);
                } else {
                    LoginResponse loginResponse = response.body();
                    if (loginResponse.getStatus() == 0) {
                        errormsg.setText(loginResponse.getMessage());
                        errormsg.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(GONE);
                    } else {
                        int userid = loginResponse.getUser().getId().intValue();
                        editor.putBoolean(IS_LOGIN, true);
                        editor.putString("Username", loginResponse.getUser().getUsername());
                        editor.putString("Email", loginResponse.getUser().getEmail());
                        editor.putString("Password", signpass.getText().toString());
                        editor.commit();

                        getUserDetails(userid);
                    }
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                progressBar.setVisibility(GONE);
                Log.e("Register", t.getMessage());
                errormsg.setText("Something happened, please try again later");
                errormsg.setVisibility(View.VISIBLE);
            }
        });
    }

    public void getUserDetails(Integer userid) {
//        UserID userID = new UserID(userid);
        Call<UserDetailResponse> userDetailResponseCall = laravelAPI.getUserDetails(userid);
        userDetailResponseCall.enqueue(new Callback<UserDetailResponse>() {
            @Override
            public void onResponse(Call<UserDetailResponse> call, Response<UserDetailResponse> response) {
                if (!response.isSuccessful()) {
                    progressBar.setVisibility(GONE);
                    errormsg.setText("Something happened, please try again later");
                    errormsg.setVisibility(View.VISIBLE);
                } else {
                    UserDetailResponse userDetailResponse = response.body();
                    progressBar.setVisibility(GONE);
                    if (userDetailResponse.getSuccess() == 0) {
                        errormsg.setText("Something went wrong, try again later");
                        errormsg.setVisibility(View.VISIBLE);
                    } else {

//                        Log.e("UserDets",userDetailResponse.getUserDetails().toString());
                        editor.putInt("UserID", userDetailResponse.getUserDetails().getUserId().intValue());
                        editor.putString("Hospital", userDetailResponse.getUserDetails().getHospital());
                        editor.commit();

                        Intent intent = new Intent(getContext(), DailyLogsActivity.class);
                        startActivity(intent);
                        signinemail.setText("");
                        signpass.setText("");
                    }
                }
            }

            @Override
            public void onFailure(Call<UserDetailResponse> call, Throwable t) {
                progressBar.setVisibility(GONE);
                Log.e("Register", t.getMessage());
                errormsg.setText("Something happened, please try again later");
                errormsg.setVisibility(View.VISIBLE);
            }
        });
    }


}
