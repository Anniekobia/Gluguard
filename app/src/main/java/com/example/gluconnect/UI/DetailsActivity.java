package com.example.gluconnect.UI;

import android.content.Intent;
import android.os.Bundle;

import com.example.gluconnect.Models.UserDetails;
import com.example.gluconnect.R;
import com.google.android.material.textfield.TextInputEditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.Button;

public class DetailsActivity extends AppCompatActivity {

    private TextInputEditText age;
    private TextInputEditText gender;
    private TextInputEditText weight;
    private TextInputEditText height;
    private TextInputEditText activitylevel;
    private Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        age = findViewById(R.id.agetxt);
        gender = findViewById(R.id.gendertxt);
        weight = findViewById(R.id.weighttxt);
        height = findViewById(R.id.heighttxt);
        activitylevel = findViewById(R.id.activityleveltxt);

    }


    public void openDailyLogsActivity(View view) {
        Intent intent = new Intent(DetailsActivity.this,DailyLogsActivity.class);
        startActivity(intent);
    }
}
