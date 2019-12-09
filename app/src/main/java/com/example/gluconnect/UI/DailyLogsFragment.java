package com.example.gluconnect.UI;


import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.gluconnect.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class DailyLogsFragment extends Fragment {

    private Toolbar toolbar;
    private View myView;
    private CardView meal, exercise, bloodGlucose, medicine;


    public DailyLogsFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.fragment_daily_logs, container, false);

        toolbar = myView.findViewById(R.id.toolbar);




        meal = myView.findViewById(R.id.mealCardView);
        exercise = myView.findViewById(R.id.exerciseCardView);
        bloodGlucose = myView.findViewById(R.id.bloodGlucoseCardView);
        medicine = myView.findViewById(R.id.medicationCardView);

        meal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new DailyLogsFragment();
                loadFragment(fragment);
            }
        });

        return  myView;
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container_cl, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }


}
