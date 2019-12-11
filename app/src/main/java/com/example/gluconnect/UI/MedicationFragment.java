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
import com.example.gluconnect.Models.BrandedFoodItemSuggestions;
import com.example.gluconnect.Models.Food;
import com.example.gluconnect.Models.FoodItemSuggestionsList;
import com.example.gluconnect.Models.Meal;
import com.example.gluconnect.Models.Medication;
import com.example.gluconnect.Models.MedicationResponse;
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


public class MedicationFragment extends Fragment implements AdapterView.OnItemSelectedListener{

    private View myView;
    private LaravelAPI laravelAPI;
    ProgressBar progressBar;
    private SharedPreferences sharedPreferences;

    private Spinner medSpinner;
    private EditText medicationUnits;
    private Button medSaveBtn;

    RelativeLayout relativeLayout;
    TextView errormsg;

    public MedicationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        myView = inflater.inflate(R.layout.fragment_medication, container, false);

        Retrofit retrofit = LaravelAPIRetrofitClient.getRetrofitClient();
        laravelAPI = retrofit.create(LaravelAPI.class);
        sharedPreferences = getContext().getSharedPreferences("MyPreferences", 0);
        errormsg = myView.findViewById(R.id.error_msg);

        relativeLayout = myView.findViewById(R.id.medication_rl);

        progressBar = myView.findViewById(R.id.progressBar);
        progressBar.bringToFront();

        medSpinner = myView.findViewById(R.id.medication_spinner);
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(getContext(),
                R.array.medicine_array, android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        medSpinner.setAdapter(adapter1);
        medicationUnits = myView.findViewById(R.id.medication_units);
        medSaveBtn = myView.findViewById(R.id.save_medication_btn);

        medSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String medication = medSpinner.getSelectedItem().toString();
                String medunits = medicationUnits.getText().toString();
                if (TextUtils.isEmpty(medication) || TextUtils.isEmpty(medunits)) {

                } else {
                    Integer userID = sharedPreferences.getInt("UserID", 1);
                    Medication medication1 = new Medication(Float.parseFloat(medunits),medication,userID.longValue());
                    recordMedicationData(medication1);
                }
            }
        });
        medicationUnits.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                errormsg.setVisibility(GONE);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        return myView;
    }

    private  void recordMedicationData(Medication medication){
        try {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(relativeLayout.getWindowToken(), 0);
        }catch (Exception e) {

        }
        Log.e("Med recorded", medication.toString());
        progressBar.setVisibility(View.VISIBLE);
        Call<MedicationResponse> medicationResponseCall = laravelAPI.recordMedication(medication);
        medicationResponseCall.enqueue(new Callback<MedicationResponse>() {
            @Override
            public void onResponse(Call<MedicationResponse> call, Response<MedicationResponse> response) {
                if (!response.isSuccessful()) {
                    progressBar.setVisibility(GONE);
                    Log.e("Med Saved Unsuccessful", "Code: " + response.code() + "\n" + "Message" + response.message());
                    medicationUnits.getText().clear();
                    errormsg.setText("Medication not saved, please try again later");
                    errormsg.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                    return;
                } else {
                    medicationUnits.getText().clear();
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getContext(),"Medication Successfully Saved",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<MedicationResponse> call, Throwable t) {
                Log.e("Exe Saved Failed", t.getMessage());
                progressBar.setVisibility(GONE);
                medicationUnits.getText().clear();
                errormsg.setText("Medication not saved, please try again later");
                errormsg.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
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
