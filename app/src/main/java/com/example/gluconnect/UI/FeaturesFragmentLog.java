package com.example.gluconnect.UI;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.gluconnect.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class FeaturesFragmentLog extends Fragment {


    public FeaturesFragmentLog() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_features_log, container, false);
    }

}
