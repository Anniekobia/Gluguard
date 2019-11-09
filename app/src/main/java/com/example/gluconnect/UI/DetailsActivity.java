package com.example.gluconnect.UI;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;

import com.example.gluconnect.Models.UserDetails;
import com.example.gluconnect.R;
import com.example.gluconnect.Utils.LaravelAPI;
import com.example.gluconnect.Utils.LaravelAPIRetrofitClient;
import com.google.android.material.textfield.TextInputEditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class DetailsActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private TextInputEditText dob;
    private TextInputEditText weight;
    private TextInputEditText height;
    private Button btn;
    Calendar myCalendar;
    Spinner spinner_gender;
    Spinner spinner_activity_level;
    LaravelAPI laravelAPI;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Retrofit retrofit = LaravelAPIRetrofitClient.getRetrofitClient();
        laravelAPI = retrofit.create(LaravelAPI.class);

        btn = findViewById(R.id.button);
        dob = findViewById(R.id.dobtxt);
        weight = findViewById(R.id.weighttxt);
        height = findViewById(R.id.heighttxt);
        spinner_gender = findViewById(R.id.gender_spinner);
        spinner_activity_level = findViewById(R.id.activity_level_spinner);
        progressBar = findViewById(R.id.progressBar);

        setDOB();
        setSpinners();
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String weightU = weight.getText().toString();
                String heightU = height.getText().toString();
                String dobU = dob.getText().toString();
                String gender = spinner_gender.getSelectedItem().toString();
                String activity_level = spinner_activity_level.getSelectedItem().toString();
                if (weightU.isEmpty()||heightU.isEmpty()||dobU.isEmpty()||gender.isEmpty()||activity_level.isEmpty()){
                    Toast.makeText(getApplicationContext(),"Please fill in all the details",Toast.LENGTH_LONG).show();
                }else {
                    Log.e("Activity",activity_level);
                    Log.e("DOB",dobU);
                    saveUserDetails(weightU,heightU,dobU,gender,activity_level);
                }
            }
        });
    }

    private void setDOB() {
        myCalendar = Calendar.getInstance();
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };
        dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(DetailsActivity.this, date, 2000, 0,
                        1).show();
            }
        });
    }

    private void updateLabel() {
        String myFormat = "dd/MM/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.ENGLISH);
        dob.setText(sdf.format(myCalendar.getTime()));
    }

    private void setSpinners() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.gender_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_gender.setAdapter(adapter);
        spinner_gender.setOnItemSelectedListener(this);

        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this,
                R.array.activity_level_array, android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_activity_level.setAdapter(adapter1);
        spinner_activity_level.setOnItemSelectedListener(this);
    }

    private void saveUserDetails(String weightU, String heightU, String dobU, String gender, String activity_level) {
        progressBar.setVisibility(View.VISIBLE);
        Call<UserDetails> userDetailsCall = laravelAPI.saveUserDetails(new UserDetails(1L,activity_level,dobU,gender,heightU,weightU));
        userDetailsCall.enqueue(new Callback<UserDetails>() {
            @Override
            public void onResponse(Call<UserDetails> call, Response<UserDetails> response) {
                if (!response.isSuccessful()){
                    progressBar.setVisibility(View.GONE);
                    Log.e("Not Successful:", "Not Details not saved");
                }
                else {
                    progressBar.setVisibility(View.GONE);
                    Intent intent = new Intent(DetailsActivity.this,DailyLogsActivity.class);
                    startActivity(intent);
                }
            }
            @Override
            public void onFailure(Call<UserDetails> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Log.e("Error here:", t.getMessage());
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String item = parent.getItemAtPosition(position).toString();
        Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

}
