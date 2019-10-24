package com.example.gluconnect.UI;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gluconnect.Models.BloodGlucose;
import com.example.gluconnect.Models.BloodGlucoseResponse;
import com.example.gluconnect.R;
import com.example.gluconnect.Utils.LaravelAPI;
import com.example.gluconnect.Utils.LaravelAPIRetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class DailyLogsFragment extends Fragment implements View.OnClickListener {

    private RadioGroup radioGroup;
    private View myView;
    private Button saveDailyLogsBtn;
    private LaravelAPI laravelAPI;
    private TextView bgLeveltextView;
    private TextView showSavedbgLeveltextView;
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
//        bgLeveltextView = myView.findViewById(R.id.all_log_txtview);
//        showSavedbgLeveltextView = myView.findViewById(R.id.saved_log_txtview);
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
//            case R.id.responseButton2:
//                break;
//            default:
//                break;
        }
    }


    private void recordBloodGlucoseLevel() {
        Double bgValue = Double.parseDouble(bloodGlucoseLevelEditText.getText().toString());
        String bgTime = selectedBloodGlucoseTime();
        Call<BloodGlucose> bloodGlucoseCall = laravelAPI.recordBloodGlucoseLevel(new BloodGlucose(
                bgValue, bgTime,1));
        bloodGlucoseCall.enqueue(new Callback<BloodGlucose>() {
            @Override
            public void onResponse(Call<BloodGlucose> call, Response<BloodGlucose> response) {
                if (!response.isSuccessful()) {
                    showSavedbgLeveltextView.setText("Code: " + response.code() + "\n" + "Message" + response.message());
                    return;
                } else {
                    Toast.makeText(getContext(), "Record saved", Toast.LENGTH_LONG).show();
                    bloodGlucoseLevelEditText.getText().clear();
                    bglevelRadiogroup.clearCheck();
                }
            }

            @Override
            public void onFailure(Call<BloodGlucose> call, Throwable t) {
                showSavedbgLeveltextView.setText(t.getMessage());
            }
        });
    }

    private String selectedBloodGlucoseTime() {
        int selectedRadioBtn = bglevelRadiogroup.getCheckedRadioButtonId();
        RadioButton radioTimeButton = myView.findViewById(selectedRadioBtn);
        return radioTimeButton.getText().toString();
    }

    public static void resetFields(ViewGroup group) {
        for (int i = 0, count = group.getChildCount(); i < count; ++i) {
            View view = group.getChildAt(i);
            if (view instanceof EditText) {
                ((EditText) view).getText().clear();
            }

            if (view instanceof RadioGroup) {
                ((RadioButton)((RadioGroup) view).getChildAt(0)).setChecked(true);
            }

            if (view instanceof Spinner) {
                ((Spinner) view).setSelection(0);
            }

            if (view instanceof ViewGroup && (((ViewGroup) view).getChildCount() > 0))
                resetFields((ViewGroup) view);
        }
    }


    @Override
    public void onPause() {
        super.onPause();
    }


}
