package com.example.gluconnect.UI;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.example.gluconnect.Models.UserDetailsN;
import com.example.gluconnect.R;
import com.example.gluconnect.Utils.LaravelAPI;
import com.example.gluconnect.Utils.LaravelAPIRetrofitClient;
import com.google.android.material.textfield.TextInputEditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.DialogFragment;

import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Calendar;

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
    Spinner spinner_hospital;
    LaravelAPI laravelAPI;
    ProgressBar progressBar;
    TextView errorMsg;
    SharedPreferences sharedPreferences;
    ConstraintLayout constraintLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Retrofit retrofit = LaravelAPIRetrofitClient.getRetrofitClient();
        laravelAPI = retrofit.create(LaravelAPI.class);
        sharedPreferences= getApplicationContext().getSharedPreferences("MyPreferences", 0); // 0 - for private mode

        constraintLayout = findViewById(R.id.details_cl);
        btn = findViewById(R.id.button);
        dob = findViewById(R.id.dobtxt);
        weight = findViewById(R.id.weighttxt);
        height = findViewById(R.id.heighttxt);
        spinner_gender = findViewById(R.id.gender_spinner);
        spinner_activity_level = findViewById(R.id.activity_level_spinner);
        spinner_hospital = findViewById(R.id.hospital_spinner);
        progressBar = findViewById(R.id.progressBar);
        errorMsg = findViewById(R.id.error_msg);

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
                String hospital = spinner_hospital.getSelectedItem().toString();
                try {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(constraintLayout.getWindowToken(), 0);
                }catch (Exception e) {

                }
                if (weightU.isEmpty()||heightU.isEmpty()||dobU.isEmpty()||gender.isEmpty()||activity_level.isEmpty()||hospital.isEmpty()){
                    errorMsg.setText("Please fill in all the details");
                    errorMsg.setVisibility(View.VISIBLE);
                }else {
                    Log.e("Activity",activity_level);
                    Log.e("DOB",dobU);
                    saveUserDetails(weightU,heightU,dobU,gender,activity_level,hospital);
                }
            }
        });

    }

    private void setDOB() {
        dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment dialogfragment = new DatePickerDialogTheme();
                dialogfragment.show(getSupportFragmentManager(),"Theme");
            }
        });
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

        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this,
                R.array.hospital_array, android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_hospital.setAdapter(adapter2);
        spinner_hospital.setOnItemSelectedListener(this);
    }

    private void saveUserDetails(String weightU, String heightU, String dobU, String gender, String activity_level,String hospital) {
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        progressBar.setVisibility(View.VISIBLE);
        Integer userID = sharedPreferences.getInt("UserID", 1);
        Call<UserDetailsN> userDetailsCall = laravelAPI.saveUserDetails(new UserDetailsN(Long.parseLong(userID.toString()),activity_level,dobU,gender,heightU,weightU,hospital));
        userDetailsCall.enqueue(new Callback<UserDetailsN>() {
            @Override
            public void onResponse(Call<UserDetailsN> call, Response<UserDetailsN> response) {
                if (!response.isSuccessful()){
                    progressBar.setVisibility(View.GONE);
                    Log.e("Not Successful:", "Not Details not saved");
                }
                else {
                    progressBar.setVisibility(View.GONE);
                    UserDetailsN userDetailsN = response.body();

                    editor.putString("Gender", userDetailsN.getGender());
                    editor.putString("Activity Level", userDetailsN.getActivityLevel());
                    editor.putString("Weight", userDetailsN.getWeight());
                    editor.putString("Height", userDetailsN.getHeight());
                    editor.putString("Date of Birth", userDetailsN.getDateOfBirth());
                    editor.putString("Hospital", userDetailsN.getmHospital());
                    editor.putLong("Daily Calorie Requirement", userDetailsN.getDailyCalories().longValue());
                    editor.commit();
                    Intent intent = new Intent(DetailsActivity.this,DailyLogsActivity.class);
                    startActivity(intent);
                }
            }
            @Override
            public void onFailure(Call<UserDetailsN> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Log.e("Error here:", t.getMessage());
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String item = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

}
