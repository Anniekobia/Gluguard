package com.example.gluconnect.UI;


import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.gluconnect.Models.BloodGlucose;
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
public class RegisterFragment extends Fragment {

    View myview;
    private Button registerBtn;
    private TextInputEditText username;
    private TextInputEditText email;
    TextInputEditText password;
    TextInputEditText confirmPassword;
    TextView errorMsg;
    TextView loginTxtview;
    private LaravelAPI laravelAPI;

    public RegisterFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myview = inflater.inflate(R.layout.fragment_register, container, false);

        Retrofit retrofit = LaravelAPIRetrofitClient.getRetrofitClient();
        laravelAPI = retrofit.create(LaravelAPI.class);

        loginTxtview = myview.findViewById(R.id.login_textview);
        registerBtn =  myview.findViewById(R.id.signup_button);
        username = myview.findViewById(R.id.username);
        email= myview.findViewById(R.id.signup_email);
        errorMsg = myview.findViewById(R.id.error_msg);
        password = myview.findViewById(R.id.signup_password);
        confirmPassword = myview.findViewById(R.id.confirm_signup_password);

        handleClicks();
        return myview;
    }
    public void handleClicks(){
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateDetails();
            }
        });

        loginTxtview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    private void validateDetails() {
        String usrname = username.getText().toString();
        String emailu = email.getText().toString();
        String pwd = password.getText().toString();
        String cpwd = confirmPassword.getText().toString();
        if (TextUtils.isEmpty(usrname)||TextUtils.isEmpty(emailu)||TextUtils.isEmpty(pwd)||TextUtils.isEmpty(cpwd)){
            errorMsg.setText("Please fill in all the fields");
            errorMsg.setVisibility(View.VISIBLE);
        }else if (!pwd.equals(cpwd)){
            errorMsg.setText("Passwords do not match");
            errorMsg.setVisibility(View.VISIBLE);
        } else {
            RegisterUser();
            errorMsg.setText("");
            errorMsg.setVisibility(View.GONE);
        }

    }
    public void RegisterUser() {
//        Call<BloodGlucose> bloodGlucoseCall = laravelAPI.recordBloodGlucoseLevel(new BloodGlucose(
//                bgTime, bgValue, 1L));
//        bloodGlucoseCall.enqueue(new Callback<BloodGlucose>() {
//            @Override
//            public void onResponse(Call<BloodGlucose> call, Response<BloodGlucose> response) {
//                if (!response.isSuccessful()) {
//                    progressBar.setVisibility(GONE);
//                    Log.e("Blood Glucose", response.message());
//                    return;
//                } else {
//                    progressBar.setVisibility(GONE);
//                    Log.e("Blood Glucose","Record saved");
//                    bloodGlucoseLevelEditText.getText().clear();
//                    bglevelRadiogroup.clearCheck();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<BloodGlucose> call, Throwable t) {
//                progressBar.setVisibility(GONE);
//                Log.e("Blood Glucose", t.getMessage());
//            }
//        });
    }
}
