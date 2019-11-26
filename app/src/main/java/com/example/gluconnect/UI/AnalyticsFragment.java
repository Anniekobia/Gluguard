package com.example.gluconnect.UI;


import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gluconnect.Models.BloodGlucose;
import com.example.gluconnect.Models.BloodGlucoseResponse;
import com.example.gluconnect.R;
import com.example.gluconnect.Utils.LaravelAPI;
import com.example.gluconnect.Utils.LaravelAPIRetrofitClient;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static android.content.ContentValues.TAG;


/**
 * A simple {@link Fragment} subclass.
 */
public class AnalyticsFragment extends Fragment {

    private View myview;
    private LaravelAPI laravelAPI;
    private List<Entry> lineChartEntries = new ArrayList<Entry>();
    private LineChart mLineChart;
    private TextView day;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    Integer userID;
    DatePickerDialog.OnDateSetListener datePickerDate;
    ProgressBar progressBar;

    public AnalyticsFragment() {
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myview = inflater.inflate(R.layout.fragment_analytics, container, false);
        Retrofit retrofit = LaravelAPIRetrofitClient.getRetrofitClient();
        laravelAPI = retrofit.create(LaravelAPI.class);
        sharedPreferences = getContext().getSharedPreferences("MyPreferences", 0);
        editor = sharedPreferences.edit();

        mLineChart = myview.findViewById(R.id.linechart);
        progressBar = myview.findViewById(R.id.progressBar);
        day = myview.findViewById(R.id.analytics_filter_day);
        day.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDatePicker();
            }
        });

        final Calendar myCalendar = Calendar.getInstance();
        datePickerDate = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                String myFormat = "EEEE, MMM d"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                day.setText(sdf.format(myCalendar.getTime()));

                SimpleDateFormat f = new SimpleDateFormat("YYYY-MM-dd");
                String datePicked = f.format(myCalendar.getTime());
                Log.e("Date Formatted", datePicked);
                progressBar.setVisibility(View.VISIBLE);
                getBloodGlucoseLevels(datePicked);
            }

        };

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE, MMM d");
        String date = sdf.format(cal.getTime());
        day.setText(date);

        SimpleDateFormat f = new SimpleDateFormat("YYYY-MM-dd");
        String datePicked = f.format(cal.getTime());
        userID = sharedPreferences.getInt("UserID", 6);
        getBloodGlucoseLevels(datePicked);
        return myview;
    }

    public void openDatePicker(){
        final Calendar myCalendar = Calendar.getInstance();
        new DatePickerDialog(getContext(), datePickerDate, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void getBloodGlucoseLevels(final String selectedDay) {
        Call<BloodGlucoseResponse> bloodGlucoseResponseCall = laravelAPI.getBloodGlucoseLevel();
        bloodGlucoseResponseCall.enqueue(new Callback<BloodGlucoseResponse>() {
            @Override
            public void onResponse(Call<BloodGlucoseResponse> call, Response<BloodGlucoseResponse> response) {
                progressBar.setVisibility(View.GONE);
                mLineChart.notifyDataSetChanged();
                mLineChart.invalidate();
                ArrayList<String> dateString = new ArrayList<>();
                ArrayList<Long> timeInMillis = new ArrayList<>();
                ArrayList<Float> bgLeveValues = new ArrayList<>();
                String records = "My records: ";
                Long minValue, maxValue;
                BloodGlucoseResponse bloodGlucoseResponse = response.body();
                for (BloodGlucose bloodGlucose : bloodGlucoseResponse.getBloodGlucoseRecords()) {
                    records = records.concat(bloodGlucose.getBloodGlucoseValue() + bloodGlucose.getDay() + bloodGlucose.getCreatedAt() + "\t");
                    String day = bloodGlucose.getDay();
                    if (day.equals(selectedDay)&&userID==bloodGlucose.getUserId().intValue()) {
                        dateString.add(bloodGlucose.getCreatedAt());
                        bgLeveValues.add(bloodGlucose.getBloodGlucoseValue());
                    }
                }
                if (!bgLeveValues.isEmpty()) {
                    try {
                        timeInMillis = TimeToMilliseconds(dateString);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    for (long t : timeInMillis) {
                        Log.e("MyTimeMilli", String.valueOf(t));
                        Log.e("MyTimeMilliSize", String.valueOf(timeInMillis.size()));
                    }


                    minValue = getMinValue(timeInMillis);
                    maxValue = getMaxValue(timeInMillis);
                    timeInMillis = changeToSmallerNumbers(minValue, timeInMillis);

                    mLineChart.setDragEnabled(true);
                    mLineChart.setScaleEnabled(true);

                    ArrayList<Entry> entries = new ArrayList<>();
                    for (int i = 0; i < timeInMillis.size(); i++) {
                        System.err.println("Graph BG: " + bgLeveValues.get(i));
                        System.err.println("Graph TimeMilli: " + timeInMillis.get(i));
                        System.err.println("Graph TimeMilli Float: " + Float.parseFloat(timeInMillis.get(i).toString()));
                        entries.add(new Entry(Float.parseFloat(timeInMillis.get(i).toString()), bgLeveValues.get(i)));
                    }
                    for (Entry entry : entries) {
                        System.out.println("Entry" + entry);
                    }

                    LimitLine upper_limit = new LimitLine(198f, "Too High");
                    upper_limit.setLineWidth(2f);
                    upper_limit.enableDashedLine(10f, 10f, 0f);
                    upper_limit.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
                    upper_limit.setTextSize(15f);
                    upper_limit.setTextColor(Color.RED);

                    LimitLine lower_limit = new LimitLine(70.2f, "Too Low");
                    lower_limit.setLineWidth(2f);
                    lower_limit.enableDashedLine(10f, 10f, 0f);
                    lower_limit.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_BOTTOM);
                    lower_limit.setTextSize(15f);
                    lower_limit.setTextColor(Color.RED);

                    YAxis leftAxis = mLineChart.getAxisLeft();
                    leftAxis.removeAllLimitLines();
                    leftAxis.addLimitLine(upper_limit);
                    leftAxis.addLimitLine(lower_limit);
                    leftAxis.setAxisMaximum(220f);
                    leftAxis.setAxisMinimum(50f);
                    leftAxis.enableGridDashedLine(10f, 10f, 0);
                    leftAxis.setDrawLimitLinesBehindData(true);


                    mLineChart.getAxisRight().setEnabled(false);
                    mLineChart.setVisibleXRangeMaximum(7);
                    LineDataSet lineDataSet = new LineDataSet(entries, "Blood Glucose Levels");
                    lineDataSet.setFillAlpha(110);
                    lineDataSet.setColor(Color.BLUE);
                    lineDataSet.setValueTextColor(Color.BLUE);
                    lineDataSet.setValueTextSize(10f);


                    ArrayList<ILineDataSet> iLineDataSets = new ArrayList<>();
                    iLineDataSets.add(lineDataSet);

                    LineData lineData = new LineData(iLineDataSets);
                    mLineChart.zoom(0.7f, 0.7f, 0.7f, 0.7f);
                    mLineChart.setData(lineData);

                    IAxisValueFormatter xAxisFormatter = new HourAxisValueFormatter(minValue);
                    XAxis xAxis = mLineChart.getXAxis();
                    xAxis.setValueFormatter(xAxisFormatter);

                    xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                } else {
                    drawExampleGraph();
                }
            }

            @Override
            public void onFailure(Call<BloodGlucoseResponse> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_LONG);
                Log.e(TAG, "Failed" + t.getMessage());
            }
        });
    }

    public ArrayList<Long> TimeToMilliseconds(ArrayList<String> theStringDates) throws ParseException {
        ArrayList<Long> timeInMillis = new ArrayList<Long>();
        for (String theDate : theStringDates) {
            Log.e("Date sent: ", theDate);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            Date date = sdf.parse(theDate);
            Log.e("Date specific HHMMSS Value: ", new SimpleDateFormat("HH:mm:ss").format(date));
            Log.e("Date converted: ", date.toString());
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            long timeMilli = calendar.getTimeInMillis();
            Log.e("Date Specific Time in milliseconds: ", String.valueOf(timeMilli));
            timeInMillis.add(timeMilli);
        }
        return timeInMillis;
    }

    public long getMinValue(ArrayList<Long> arrayList) {
        long minValue = arrayList.get(0);
        for (int i = 0; i < arrayList.size(); i++) {
            if (arrayList.get(i) < minValue) {
                minValue = arrayList.get(i);
            } else {
                minValue = minValue;
            }
        }
        Log.e("MyMinVal", String.valueOf(minValue));
        return minValue;
    }


    public long getMaxValue(ArrayList<Long> arrayList) {
        long maxValue = arrayList.get(0);
        for (int i = 0; i < arrayList.size(); i++) {
            if (arrayList.get(i) > maxValue) {
                maxValue = arrayList.get(i);
            } else {
                maxValue = maxValue;
            }
        }
        return maxValue;
    }

    public ArrayList<Long> changeToSmallerNumbers(Long minValue, ArrayList<Long> allValues) {
        ArrayList<Long> smallerTimeValues = new ArrayList<Long>();
        for (Long value : allValues) {
            Long newSmallValue = value - minValue;
            Long smallestValue = newSmallValue / 1000;
            smallerTimeValues.add(smallestValue);
            System.err.println("Old Value: " + value);
            System.err.println("Min Value: " + minValue);
            System.err.println("New Value: " + newSmallValue);
        }
        for (int i = 0; i < smallerTimeValues.size(); i++) {
            System.err.println("Smaller Value: " + smallerTimeValues.get(i));
        }
        return smallerTimeValues;
    }


    public class HourAxisValueFormatter implements IAxisValueFormatter {

        private long referenceTimestamp;
        private DateFormat mDataFormat;
        private Date mDate;

        public HourAxisValueFormatter(long referenceTimestamp) {
            this.referenceTimestamp = referenceTimestamp;
            this.mDataFormat = new SimpleDateFormat("HH:mm", Locale.ENGLISH);
            this.mDate = new Date();
        }

        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            long newValue = Math.round(value);
            Log.e("Converter Value",String.valueOf(newValue));
            long convertedTimestamp = (long) newValue*1000;
            long originalTimestamp = (referenceTimestamp + convertedTimestamp);
            Log.e("Converter Min",String.valueOf(referenceTimestamp));
            Log.e("Converter Original",String.valueOf(originalTimestamp));
            return getHour(originalTimestamp);
        }

        private String getHour(long timestamp) {
            Calendar calendar =  Calendar.getInstance();
            calendar.setTimeInMillis(timestamp);
            DateFormat formatter = new SimpleDateFormat("HH:mm:ss");
            formatter.setCalendar(calendar);
            formatter.setTimeZone(TimeZone.getTimeZone("Africa/Nairobi"));
            String formattedDate = formatter.format(calendar.getTime());
            return formattedDate;
        }
    }

    public void drawExampleGraph() {
        mLineChart.setDragEnabled(true);
        mLineChart.setScaleEnabled(false);

        LimitLine upper_limit = new LimitLine(198f, "Too High");
        upper_limit.setLineWidth(2f);
        upper_limit.enableDashedLine(10f, 10f, 0f);
        upper_limit.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
        upper_limit.setTextSize(15f);
        upper_limit.setTextColor(Color.RED);

        LimitLine lower_limit = new LimitLine(70.2f, "Too Low");
        lower_limit.setLineWidth(2f);
        lower_limit.enableDashedLine(10f, 10f, 0f);
        lower_limit.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_BOTTOM);
        lower_limit.setTextSize(15f);
        lower_limit.setTextColor(Color.RED);

        YAxis leftAxis = mLineChart.getAxisLeft();
        leftAxis.removeAllLimitLines();
        leftAxis.addLimitLine(upper_limit);
        leftAxis.addLimitLine(lower_limit);
        leftAxis.setAxisMaximum(220f);
        leftAxis.setAxisMinimum(50f);
        leftAxis.enableGridDashedLine(10f, 10f, 0);
        leftAxis.setDrawLimitLinesBehindData(true);


        mLineChart.getAxisRight().setEnabled(false);
        mLineChart.setVisibleXRangeMaximum(7);

        ArrayList<ILineDataSet> iLineDataSets = new ArrayList<>();

        LineData lineData = new LineData(iLineDataSets);
        mLineChart.setData(lineData);
    }


}