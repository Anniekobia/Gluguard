package com.example.gluconnect.UI;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.example.gluconnect.Adapters.FoodAutoSuggestAdapter;
import com.example.gluconnect.Adapters.FoodAutoSuggestAdapterSET;
import com.example.gluconnect.Models.BloodGlucose;
import com.example.gluconnect.Models.BrandedFoodItemSuggestions;
import com.example.gluconnect.Models.Food;
import com.example.gluconnect.Models.FoodItemSuggestionsList;
import com.example.gluconnect.Models.Meal;
import com.example.gluconnect.R;
import com.example.gluconnect.Utils.LaravelAPI;
import com.example.gluconnect.Utils.LaravelAPIRetrofitClient;
import com.example.gluconnect.Utils.NutritionixAPI;
import com.example.gluconnect.Utils.NutritionixAPIRetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static android.view.View.GONE;


public class BloodGlucoseFragment extends Fragment implements AdapterView.OnItemSelectedListener{

    private View myView;
    private LaravelAPI laravelAPI;
    private EditText bloodGlucoseLevelEditText;
    ProgressBar progressBar;
    private Button bgSaveBtn;
    Spinner bgSpinner;
    private SharedPreferences sharedPreferences;

    RelativeLayout relativeLayout;
    TextView errormsg;


    public BloodGlucoseFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        myView = inflater.inflate(R.layout.fragment_blood_glucose, container, false);

        Retrofit retrofit = LaravelAPIRetrofitClient.getRetrofitClient();
        laravelAPI = retrofit.create(LaravelAPI.class);
        sharedPreferences = getContext().getSharedPreferences("MyPreferences", 0);
        errormsg = myView.findViewById(R.id.error_msg);

        relativeLayout = myView.findViewById(R.id.blood_glucose_rl);
        bgSaveBtn = myView.findViewById(R.id.save_blood_glucose_btn);
        bloodGlucoseLevelEditText = myView.findViewById(R.id.blood_glucose_level_edittext);
        bgSpinner = myView.findViewById(R.id.bg_spinner);
        progressBar = myView.findViewById(R.id.progressBar);
        progressBar.bringToFront();


        bgSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String bglevel = bloodGlucoseLevelEditText.getText().toString();
                if (TextUtils.isEmpty(bglevel) ) {
                    return;
                } else {
                    recordBloodGlucoseLevel();
                }
            }
        });
        bloodGlucoseLevelEditText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                errormsg.setVisibility(GONE);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
        });
        setSpinner();
        return myView;
    }

    private void recordBloodGlucoseLevel() {
        try {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(relativeLayout.getWindowToken(), 0);
        }catch (Exception e) {

        }
        Integer userID = sharedPreferences.getInt("UserID", 1);
        progressBar.setVisibility(View.VISIBLE);
        final Float bgValue = Float.parseFloat(bloodGlucoseLevelEditText.getText().toString());
        String bgTime = bgSpinner.getSelectedItem().toString();
        Call<BloodGlucose> bloodGlucoseCall = laravelAPI.recordBloodGlucoseLevel(new BloodGlucose(
                bgTime, bgValue, userID.longValue()));
        bloodGlucoseCall.enqueue(new Callback<BloodGlucose>() {
            @Override
            public void onResponse(Call<BloodGlucose> call, Response<BloodGlucose> response) {
                if (!response.isSuccessful()) {
                    progressBar.setVisibility(GONE);
                    Log.e("Blood Glucose", response.message());
                    bloodGlucoseLevelEditText.getText().clear();
                    errormsg.setText("Blood glucose not saved, please try again later");
                    errormsg.setVisibility(View.VISIBLE);
                    return;
                } else {
                    progressBar.setVisibility(GONE);
                    Log.e("Blood Glucose", "Record saved");
                    bloodGlucoseLevelEditText.getText().clear();
                    Toast.makeText(getContext(),"Blood Glucose Level Successfully Saved",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<BloodGlucose> call, Throwable t) {
                progressBar.setVisibility(GONE);
                Log.e("Blood Glucose", t.getMessage());
                bloodGlucoseLevelEditText.getText().clear();
                errormsg.setText("Blood glucose not saved, please try again later");
                errormsg.setVisibility(View.VISIBLE);

            }
        });
    }

    private void setSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.bg_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        bgSpinner.setAdapter(adapter);
        bgSpinner.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String item = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


}
