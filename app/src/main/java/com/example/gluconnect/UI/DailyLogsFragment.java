package com.example.gluconnect.UI;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.gluconnect.R;



public class DailyLogsFragment extends Fragment {

    private RadioGroup radioGroup;
    private View myView;
    private Button saveDailyLogsBtn;

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
        saveDailyLogsBtn = myView.findViewById(R.id.save_logs_btn);

        bloodGlucoseRecordListener();
        return myView;
    }

    private void bloodGlucoseRecordListener() {
        radioGroup = myView.findViewById(R.id.blood_glucose_level_radiogroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton bGTimeSelected = myView.findViewById(checkedId);
                switch(checkedId) {
                    case R.id.fasting_selected_btn:
                        Toast.makeText(getActivity(), bGTimeSelected.getText(), Toast.LENGTH_LONG).show();
                        break;
                    case R.id.beforemeal_selected_btn:
                        Toast.makeText(getActivity(), bGTimeSelected.getText(), Toast.LENGTH_LONG).show();
                        break;
                    case R.id.aftermeal_selected_btn:
                        Toast.makeText(getActivity(), bGTimeSelected.getText(), Toast.LENGTH_LONG).show();
                        break;
                    case R.id.atnight_selected_btn:
                        Toast.makeText(getActivity(), bGTimeSelected.getText(), Toast.LENGTH_LONG).show();
                        break;
                }
            }
        });
    }

    public void saveDailyLogs(View view) {
        
    }
    @Override
    public void onPause() {
        super.onPause();
    }


}
