package com.example.gluconnect.UI;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.gluconnect.Models.BloodGlucose;
import com.example.gluconnect.R;
import com.example.gluconnect.Utils.LaravelAPI;
import com.example.gluconnect.Utils.LaravelAPIRetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class DailyLogsFragment extends Fragment implements View.OnClickListener {

    private static final String BGERROR = "";
    private View myView;
    private Button saveDailyLogsBtn;
    private LaravelAPI laravelAPI;
    private EditText bloodGlucoseLevelEditText;
    private RadioGroup bglevelRadiogroup;

    public DailyLogsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.fragment_daily_logs, container, false);
        Retrofit retrofit = LaravelAPIRetrofitClient.getRetrofitClient();
        laravelAPI = retrofit.create(LaravelAPI.class);

        saveDailyLogsBtn = myView.findViewById(R.id.save_logs_btn);
        bloodGlucoseLevelEditText = myView.findViewById(R.id.blood_glucose_level_edittext);
        bglevelRadiogroup = myView.findViewById(R.id.blood_glucose_level_radiogroup);


        saveDailyLogsBtn.setOnClickListener(this);
        return myView;


    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.save_logs_btn:
                String bglevel = bloodGlucoseLevelEditText.getText().toString();
                if (TextUtils.isEmpty(bglevel) || bglevelRadiogroup.getCheckedRadioButtonId() == -1) {
                    Toast.makeText(getContext(), "Please fill in all the details", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    recordBloodGlucoseLevel();
                }
                break;

        }
    }


    private void recordBloodGlucoseLevel() {
        final Float bgValue = Float.parseFloat(bloodGlucoseLevelEditText.getText().toString());
        String bgTime = selectedBloodGlucoseTime();
        Call<BloodGlucose> bloodGlucoseCall = laravelAPI.recordBloodGlucoseLevel(new BloodGlucose(
                bgTime, bgValue,1L));
        bloodGlucoseCall.enqueue(new Callback<BloodGlucose>() {
            @Override
            public void onResponse(Call<BloodGlucose> call, Response<BloodGlucose> response) {
                if (!response.isSuccessful()) {
                    Log.e(BGERROR,response.message());
                    Toast.makeText(getContext(),"Record not saved",Toast.LENGTH_LONG).show();
                    return;
                } else {
                    Toast.makeText(getContext(), "Record saved", Toast.LENGTH_LONG).show();

                    bloodGlucoseLevelEditText.getText().clear();
                    bglevelRadiogroup.clearCheck();
                }
            }
            @Override
            public void onFailure(Call<BloodGlucose> call, Throwable t) {
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private String selectedBloodGlucoseTime() {
        int selectedRadioBtn = bglevelRadiogroup.getCheckedRadioButtonId();
        RadioButton radioTimeButton = myView.findViewById(selectedRadioBtn);
        return radioTimeButton.getText().toString();
    }


    @Override
    public void onPause() {
        super.onPause();
    }


}
