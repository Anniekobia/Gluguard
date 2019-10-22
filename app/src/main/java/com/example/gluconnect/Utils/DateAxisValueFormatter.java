package com.example.gluconnect.Utils;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateAxisValueFormatter implements IAxisValueFormatter {
    private String[] mValues;

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd:hh:mm:ss");

    public DateAxisValueFormatter(String[] values) {
        this.mValues = values; }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        Date d = new Date(Float.valueOf(value).longValue());
        String date = new SimpleDateFormat("dd-MM", Locale.ENGLISH).format(d);
        return  date;
    }
}
