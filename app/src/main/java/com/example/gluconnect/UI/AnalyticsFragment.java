package com.example.gluconnect.UI;


import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.gluconnect.Models.BloodGlucose;
import com.example.gluconnect.Models.BloodGlucoseResponse;
import com.example.gluconnect.R;
import com.example.gluconnect.Utils.LaravelAPI;
import com.example.gluconnect.Utils.LaravelAPIRetrofitClient;
import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.sql.Time;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
    private GraphView graphView_t;
    private ArrayList<BloodGlucose> datalist = new ArrayList<>();
    private ArrayList<Long> bg_value = new ArrayList<>();
    private ArrayList<Date> time_value = new ArrayList<>();
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
    private LineGraphSeries<DataPoint> series;


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

        graphView_t = myview.findViewById(R.id.linechart);


        Date date = startOfDay();
        getBloodGlucoseLevels();
        return myview;
    }

    private void getBloodGlucoseLevels() {
        Call<BloodGlucoseResponse> bloodGlucoseResponseCall = laravelAPI.getBloodGlucoseLevel();
        bloodGlucoseResponseCall.enqueue(new Callback<BloodGlucoseResponse>() {
            @Override
            public void onResponse(Call<BloodGlucoseResponse> call, Response<BloodGlucoseResponse> response) {
                BloodGlucoseResponse bloodGlucoseResponse = response.body();
                for (BloodGlucose bloodGlucose : bloodGlucoseResponse.getBloodGlucoseRecords()) {
                    datalist.add(bloodGlucose);
                }
//                time_value.add(date);
                for (int i = 0; i < datalist.size(); i++) {
                    bg_value.add(datalist.get(i).getBloodGlucoseLevel().longValue());
                    String stringDate = datalist.get(i).getmCreatedAt();

                    //Convert time from GMT to EAT
                    System.err.println("Old date"+stringDate);
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
                    df.setTimeZone(TimeZone.getTimeZone("EAT"));
                    Date date = null;
                    try {
                        date = df.parse(stringDate);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    df.setTimeZone(TimeZone.getDefault());
                    String formattedDate = df.format(date);
                    System.err.println("New date"+formattedDate);

                    Date mydate = fromStringToDate(formattedDate, "yyyy-MM-dd HH:mm:ss");
                    time_value.add(mydate);
                }
                drawGraphs(graphView_t, time_value, bg_value);
            }

            @Override
            public void onFailure(Call<BloodGlucoseResponse> call, Throwable t) {
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_LONG);
                Log.e(TAG, "ERROR" + t.getMessage());
            }
        });
    }

    public static Date fromStringToDate(String stringDate, String pattern){
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        try {
            Date date = format.parse(stringDate);
            return date;
        } catch (ParseException e) {
            e.printStackTrace();
            Log.e(TAG, "ERROR" + e.getMessage());
        }
        return null;
    }

    public void drawGraphs(GraphView graphView, ArrayList<Date> X_axis, ArrayList<Long> Y_axis) {
        graphView.getViewport().setMinY(getMinValue(Y_axis) - 10);
        graphView.getViewport().setMaxY(getMaxValue(Y_axis) + 10);
        graphView.getViewport().setYAxisBoundsManual(true);


        series = new LineGraphSeries<>();
        for (int i = 0; i < Y_axis.size(); i++) {
            System.err.println("Graph Values, Date"+X_axis.get(i).toString()+"Blood glucose:"+ Y_axis.get(i).toString());
            series.appendData(new DataPoint(X_axis.get(i), Y_axis.get(i)), true, X_axis.size());
        }
        series.setTitle("Blood Glucose Trends");
        series.setColor(Color.BLUE);
        series.setDrawDataPoints(true);
        series.setDataPointsRadius(10);
        series.setThickness(3);
        graphView.addSeries(series);
        graphView.getViewport().setScrollable(true);
        graphView.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter() {
            @Override
            public String formatLabel(double value, boolean isValueX) {
                if (isValueX) {
                    return simpleDateFormat.format(new Date((long) value));
                } else {
                    return super.formatLabel(value, isValueX);
                }
            }
        });
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

    public Date startOfDay() {
        final long timestamp = new Date().getTime();
        final Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(timestamp);
        cal.set(Calendar.HOUR_OF_DAY, 0); //set hours to zero
        cal.set(Calendar.MINUTE, 0); // set minutes to zero
        cal.set(Calendar.SECOND, 0); //set seconds to zero
        Date date = cal.getTime();
        System.err.println("Start of day again:"+date.toString());
        return date;
    }



}