package com.example.gluconnect.UI;

import android.content.Intent;
import android.os.Bundle;

import com.example.gluconnect.R;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;

public class DetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    public void openDailyLogsActivity(View view) {
        Intent intent = new Intent(this, DailyLogsActivity.class);
        startActivity(intent);
    }
}
