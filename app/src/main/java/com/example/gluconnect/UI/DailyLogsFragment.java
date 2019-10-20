package com.example.gluconnect.UI;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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

    public DailyLogsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        myView = inflater.inflate(R.layout.fragment_daily_logs, container, false);
        Retrofit retrofit = LaravelAPIRetrofitClient.getRetrofitClient();
        laravelAPI = retrofit.create(LaravelAPI.class);

        saveDailyLogsBtn = myView.findViewById(R.id.save_logs_btn);
        bgLeveltextView = myView.findViewById(R.id.all_log_txtview);
        showSavedbgLeveltextView = myView.findViewById(R.id.saved_log_txtview);
        bloodGlucoseLevelEditText = myView.findViewById(R.id.blood_glucose_level_edittext);


        saveDailyLogsBtn.setOnClickListener(this);
//        getBloodGlucoseLevels();



//        recordBloodGlucoseLevel();

//        bloodGlucoseRecordListener();
        return myView;


    }

    public void onClick(View v){
        switch (v.getId()) {
            case R.id.save_logs_btn:
                recordBloodGlucoseLevel();
                break;
//            case R.id.responseButton2:
//                break;
//            default:
//                break;
        }
    }



    private void recordBloodGlucoseLevel() {
//        String blood_glucose_value = bloodGlucoseLevelEditText.getText().toString();
//        Double value=null;
//        String[] thiss=null;
//        try {
//            value = Double.parseDouble(blood_glucose_value);
//            thiss = bloodGlucoseRecordListener();
//        }catch (Exception e){
//            showSavedbgLeveltextView.setText("Please fill in the details");
//            Toast.makeText(getActivity(), "Please fill in all the logs", Toast.LENGTH_LONG).show();
//        }
//        if (value!=null&&thiss!=null){
//            Call<BloodGlucose> bloodGlucoseCall = laravelAPI.recordBloodGlucoseLevel(new BloodGlucose(
//                    value, thiss[0]));
        Call<BloodGlucose> bloodGlucoseCall = laravelAPI.recordBloodGlucoseLevel(new BloodGlucose(
                45.6, "At night"));
            bloodGlucoseCall.enqueue(new Callback<BloodGlucose>() {
                @Override
                public void onResponse(Call<BloodGlucose> call, Response<BloodGlucose> response) {
                    if (!response.isSuccessful()) {
                        showSavedbgLeveltextView.setText("Code: " + response.code() + "\n" + "Message" + response.message());
                        return;
                    } else {
                        showSavedbgLeveltextView.setText("Worked");
                        getBloodGlucoseLevels();
                    }
                }

                @Override
                public void onFailure(Call<BloodGlucose> call, Throwable t) {
                    showSavedbgLeveltextView.setText(t.getMessage());
                }
            });
//        }else{
//            showSavedbgLeveltextView.setText("Please fill in the details TWO");
//            Toast.makeText(getActivity(), "Please fill in all the logs TWO", Toast.LENGTH_LONG).show();
//        }
    }

    private void getBloodGlucoseLevels() {
        Call<BloodGlucoseResponse> bloodGlucoseResponseCall = laravelAPI.getBloodGlucoseLevel();
        bloodGlucoseResponseCall.enqueue(new Callback<BloodGlucoseResponse>() {
            @Override
            public void onResponse(Call<BloodGlucoseResponse> call, Response<BloodGlucoseResponse> response) {
                if (!response.isSuccessful()){
                    bgLeveltextView.setText("Code: " + response.code() + "\n" + "Message" + response.message());
                    return;
                }else{
                    String bg = "";
                    BloodGlucoseResponse bloodGlucoseResponse = response.body();
                    for (BloodGlucose bloodGlucose : bloodGlucoseResponse.getBloodGlucoseRecords()) {
                        bg = bg.concat(bloodGlucose.toString());
                    }
                    bgLeveltextView.setText(bg);
                }
            }

            @Override
            public void onFailure(Call<BloodGlucoseResponse> call, Throwable t) {
                bgLeveltextView.setText(t.getMessage());
            }
        });
    }

    private String[] bloodGlucoseRecordListener() {
        final String[] blood_glucose_record_time = new String[1];
        radioGroup = myView.findViewById(R.id.blood_glucose_level_radiogroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton bGTimeSelected = myView.findViewById(checkedId);

                switch(checkedId) {
                    case R.id.fasting_selected_btn:
                        blood_glucose_record_time[0] = bGTimeSelected.getText().toString();
                        Toast.makeText(getActivity(), bGTimeSelected.getText(), Toast.LENGTH_LONG).show();
                        break;
                    case R.id.beforemeal_selected_btn:
                        blood_glucose_record_time[0] = bGTimeSelected.getText().toString();
                        Toast.makeText(getActivity(), bGTimeSelected.getText(), Toast.LENGTH_LONG).show();
                        break;
                    case R.id.aftermeal_selected_btn:
                        blood_glucose_record_time[0] = bGTimeSelected.getText().toString();
                        Toast.makeText(getActivity(), bGTimeSelected.getText(), Toast.LENGTH_LONG).show();
                        break;
                    case R.id.atnight_selected_btn:
                        blood_glucose_record_time[0] = bGTimeSelected.getText().toString();
                        Toast.makeText(getActivity(), bGTimeSelected.getText(), Toast.LENGTH_LONG).show();
                        break;
                }
            }
        });
        return blood_glucose_record_time;
    }

    public void saveDailyLogs(View view) {
        
    }
    @Override
    public void onPause() {
        super.onPause();
    }


}
