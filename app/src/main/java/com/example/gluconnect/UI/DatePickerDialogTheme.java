package com.example.gluconnect.UI;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;

import com.example.gluconnect.R;

import java.util.Calendar;

public class DatePickerDialogTheme extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datepickerdialog = new DatePickerDialog(getActivity(),
                AlertDialog.THEME_HOLO_LIGHT, this, 2000, 0, 1);

        return datepickerdialog;
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        TextView dob =  getActivity().findViewById(R.id.dobtxt);
        dob.setText(day + "/" + (month + 1) + "/" + year);
    }
}