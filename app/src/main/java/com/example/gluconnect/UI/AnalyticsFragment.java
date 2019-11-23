package com.example.gluconnect.UI;


import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
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
    private TextView recordtxt;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public AnalyticsFragment() {
        // Required empty public constructor
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
        sharedPreferences= getContext().getSharedPreferences("MyPreferences", 0);
        editor = sharedPreferences.edit();

        recordtxt = myview.findViewById(R.id.bgrecords);
        mLineChart = myview.findViewById(R.id.linechart);


        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE, MMM d");
        String date = sdf.format(cal.getTime());
        Log.e("Date ", date);

        SimpleDateFormat f = new SimpleDateFormat("YYYY-MM-dd");
        String datePicked = f.format(cal.getTime());
        Log.e("Date Formatted", datePicked);

        Log.e("UserID", String.valueOf(sharedPreferences.getInt("UserID",6)));
//        getBloodGlucoseLevels(datePicked);
//        getBloodGlucoseLevels();
        drawExampleGraph();
        return myview;
    }

    private void getBloodGlucoseLevels(final String selectedDay) {

        Call<BloodGlucoseResponse> bloodGlucoseResponseCall = laravelAPI.getBloodGlucoseLevel();
        bloodGlucoseResponseCall.enqueue(new Callback<BloodGlucoseResponse>() {
            @Override
            public void onResponse(Call<BloodGlucoseResponse> call, Response<BloodGlucoseResponse> response) {
                ArrayList<String> dateString = new ArrayList<>();
                ArrayList<Long> timeInMillis = new ArrayList<>();
                ArrayList<Float> bgLeveValues = new ArrayList<>();
                String records = "My records: ";
                Long minValue, maxValue;
                BloodGlucoseResponse bloodGlucoseResponse = response.body();
                for (BloodGlucose bloodGlucose : bloodGlucoseResponse.getBloodGlucoseRecords()) {
                    records = records.concat(bloodGlucose.getBloodGlucoseValue() + bloodGlucose.getDay() + bloodGlucose.getCreatedAt() + "\t");
                    String day = bloodGlucose.getDay();
                    if (day.equals(selectedDay)){
                        dateString.add(bloodGlucose.getCreatedAt());
                        bgLeveValues.add(bloodGlucose.getBloodGlucoseValue());
                    }
                }
                try {
                    timeInMillis = TimeToMilliseconds(dateString);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                minValue = getMinValue(timeInMillis);
                maxValue = getMaxValue(timeInMillis);
                timeInMillis = changeToSmallerNumbers(minValue, timeInMillis);

                mLineChart.setDragEnabled(true);
                mLineChart.setScaleEnabled(false);

                ArrayList<Entry> entries = new ArrayList<>();
                for (int i = 0; i < timeInMillis.size(); i++) {
                    System.err.println("Graph BG: " + bgLeveValues.get(i));
                    System.err.println("Graph TimeMilli: " + timeInMillis.get(i));
                    entries.add(new Entry(Float.parseFloat(timeInMillis.get(i).toString()), bgLeveValues.get(i)));
                }
                for (Entry entry : entries) {
                    System.out.println("Entry" + entry);
                }

                LimitLine upper_limit = new LimitLine(65f, "Too High");
                upper_limit.setLineWidth(2f);
                upper_limit.enableDashedLine(10f, 10f, 0f);
                upper_limit.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
                upper_limit.setTextSize(15f);
                upper_limit.setTextColor(Color.RED);

                LimitLine lower_limit = new LimitLine(35f, "Too Low");
                lower_limit.setLineWidth(2f);
                lower_limit.enableDashedLine(10f, 10f, 0f);
                lower_limit.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_BOTTOM);
                lower_limit.setTextSize(15f);
                lower_limit.setTextColor(Color.RED);

                YAxis leftAxis = mLineChart.getAxisLeft();
                leftAxis.removeAllLimitLines();
                leftAxis.addLimitLine(upper_limit);
                leftAxis.addLimitLine(lower_limit);
                leftAxis.setAxisMaximum(100f);
                leftAxis.setAxisMinimum(0f);
                leftAxis.enableGridDashedLine(10f, 10f, 0);
                leftAxis.setDrawLimitLinesBehindData(true);

                mLineChart.getAxisRight().setEnabled(false);
                mLineChart.setVisibleXRangeMaximum(7);
                LineDataSet lineDataSet = new LineDataSet(entries, "Blood Glucose Levels");
                lineDataSet.setFillAlpha(110);
                lineDataSet.setColor(Color.BLUE);
                lineDataSet.setValueTextColor(Color.BLUE);
                lineDataSet.setValueTextSize(10f);
//        lineDataSet.setLineWidth(3f);
//        lineDataSet.setCircleRadius(5f);

                ArrayList<ILineDataSet> iLineDataSets = new ArrayList<>();
                iLineDataSets.add(lineDataSet);

                LineData lineData = new LineData(iLineDataSets);
                mLineChart.setData(lineData);

                XAxis xAxis = mLineChart.getXAxis();
//                xAxis.setValueFormatter(new myXAxisValueFormatter(value));
//                xAxis.setGranularity(1f);
                xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
//                ;

            }

            @Override
            public void onFailure(Call<BloodGlucoseResponse> call, Throwable t) {
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_LONG);
                Log.e(TAG, "Failed" + t.getMessage());
            }
        });
    }

    public void drawExampleGraph() {
        mLineChart.setDragEnabled(true);
        mLineChart.setScaleEnabled(false);

        ArrayList<Entry> entries = new ArrayList<>();
        entries.add(new Entry(0, 60f));
        entries.add(new Entry(1, 10f));
        entries.add(new Entry(2, 40f));
        entries.add(new Entry(3, 90f));
        entries.add(new Entry(4, 40f));
        entries.add(new Entry(5, 10f));
        entries.add(new Entry(6, 70f));

        LimitLine upper_limit = new LimitLine(65f, "Too High");
        upper_limit.setLineWidth(2f);
        upper_limit.enableDashedLine(10f, 10f, 0f);
        upper_limit.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
        upper_limit.setTextSize(15f);
        upper_limit.setTextColor(Color.RED);

        LimitLine lower_limit = new LimitLine(35f, "Too Low");
        lower_limit.setLineWidth(2f);
        lower_limit.enableDashedLine(10f, 10f, 0f);
        lower_limit.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_BOTTOM);
        lower_limit.setTextSize(15f);
        lower_limit.setTextColor(Color.RED);

        YAxis leftAxis = mLineChart.getAxisLeft();
        leftAxis.removeAllLimitLines();
        leftAxis.addLimitLine(upper_limit);
        leftAxis.addLimitLine(lower_limit);
        leftAxis.setAxisMaximum(100f);
        leftAxis.setAxisMinimum(0f);
        leftAxis.enableGridDashedLine(10f, 10f, 0);
        leftAxis.setDrawLimitLinesBehindData(true);

        mLineChart.getAxisRight().setEnabled(false);
        LineDataSet lineDataSet = new LineDataSet(entries, "Blood Glucose Levels");
        lineDataSet.setFillAlpha(110);
        lineDataSet.setColor(Color.BLUE);
        lineDataSet.setValueTextColor(Color.BLUE);
        lineDataSet.setValueTextSize(10f);
//        lineDataSet.setLineWidth(3f);
//        lineDataSet.setCircleRadius(5f);

        ArrayList<ILineDataSet> iLineDataSets = new ArrayList<>();
        iLineDataSets.add(lineDataSet);

        LineData lineData = new LineData(iLineDataSets);
        mLineChart.setData(lineData);

        String[] value = new String[]{"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul"};
        XAxis xAxis = mLineChart.getXAxis();
        xAxis.setValueFormatter(new myXAxisValueFormatter(value));
        xAxis.setGranularity(1f);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

    }



    public class myXAxisValueFormatter implements IAxisValueFormatter {
        private String[] values;

        public myXAxisValueFormatter(String[] values) {
            this.values = values;
        }

        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            return values[(int) value];
        }
    }

    public ArrayList<Long> TimeToMilliseconds(ArrayList<String> theStringDates) throws ParseException {
        ArrayList<Long> timeInMillis = new ArrayList<Long>();
        for (String theDate : theStringDates) {
            System.err.println("Sent date: " + theDate);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            Date date = sdf.parse(theDate);
            System.out.println("HHMMSS Value: " + new SimpleDateFormat("HH:mm:ss").format(date));
            System.err.println("Converted date: " + date);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            long timeMilli = calendar.getTimeInMillis();
            System.out.println("Time in milliseconds: " + timeMilli);
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
            smallerTimeValues.add(newSmallValue);
            System.err.println("Old Value: " + value);
            System.err.println("Min Value: " + minValue);
            System.err.println("New Value: " + newSmallValue);
        }
        for (int i = 0; i < smallerTimeValues.size(); i++) {
            System.err.println("Smaller Value: " + smallerTimeValues.get(i));
        }
        return smallerTimeValues;
    }


}