package com.example.gluconnect.UI;

import android.content.Intent;
import android.os.Bundle;

import com.example.gluconnect.Models.RegisterPost;
import com.example.gluconnect.R;
import com.example.gluconnect.Utils.LaravelAPI;
import com.example.gluconnect.Utils.LaravelAPIRetrofitClient;
import com.google.android.material.textfield.TextInputEditText;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class RegisterActivity extends AppCompatActivity {

    private Button submitBtn;
    private TextInputEditText email ;
    private TextInputEditText pass;
    private LaravelAPI laravelAPI;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        Retrofit retrofit = LaravelAPIRetrofitClient.getRetrofitClient();
         laravelAPI = retrofit.create(LaravelAPI.class);

         submitBtn =  findViewById(R.id.signup_button);
         email =  findViewById(R.id.signup_email);
         pass = findViewById(R.id.signup_password);



    }



    public void openDetailsActivity(View view) {
        Intent intent = new Intent(this, DetailsActivity.class);
        startActivity(intent);
    }
}
