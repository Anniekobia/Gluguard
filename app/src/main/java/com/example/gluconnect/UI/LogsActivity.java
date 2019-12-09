package com.example.gluconnect.UI;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.RelativeLayout;

import com.example.gluconnect.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class LogsActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private  Bundle extras;
    RelativeLayout relativeLayout;
    private CardView meal, exercise, bloodGlucose, medicine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logs);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Log");
        setSupportActionBar(toolbar);

        relativeLayout = findViewById(R.id.logs_layout);

        meal = findViewById(R.id.mealCardView);
        exercise = findViewById(R.id.exerciseCardView);
        bloodGlucose = findViewById(R.id.bloodGlucoseCardView);
        medicine = findViewById(R.id.medicationCardView);

        meal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new DailyLogsFragment();
                loadFragment(fragment);
            }
        });

        final BottomNavigationView navigation = findViewById(R.id.bottom_navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        extras = getIntent().getExtras();
        final View activityRootView = findViewById(R.id.root);
        activityRootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int heightDiff = activityRootView.getRootView().getHeight() - activityRootView.getHeight();
                if (heightDiff > dpToPx(getApplicationContext(), 200)) { // if more than 200 dp, it's probably a keyboard...
                    navigation.setVisibility(View.GONE);
                }else {
                    navigation.setVisibility(View.VISIBLE);
                }
            }
        });

    }

    public static float dpToPx(Context context, float valueInDp) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, valueInDp, metrics);
    }

    // Menu icons are inflated just as they were with actionbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment;
            switch (item.getItemId()) {
                case R.id.diary_icon:
                    toolbar.setTitle("Diary");
                    fragment = new DiaryFragment();
                    fragment.setArguments(extras);
                    loadFragment(fragment);
                    return true;
                case R.id.analytics_icon:
                    toolbar.setTitle("Trends");
                    fragment = new AnalyticsFragment();
                    loadFragment(fragment);
                    return true;
                case R.id.record_icon:
                    toolbar.setTitle("Log");
                    fragment = new DailyLogsFragment();
                    loadFragment(fragment);
                    return true;
//                case R.id.steps_icon:
//                    toolbar.setTitle("Steps");
//                    fragment = new StepCounterFragment();
//                    loadFragment(fragment);
//                    return true;
                case R.id.tips_icon:
                    relativeLayout.setVisibility(View.GONE);
                    toolbar.setTitle("Tips");
                    fragment = new TipsFragment();
                    loadFragment(fragment);
                    return true;
            }
            return false;
        }
    };

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }


    public void openProfile(MenuItem item) {
        Fragment fragment;
        toolbar.setTitle("Profile");
        fragment = new ProfileFragment();
        loadFragment(fragment);
    }


}
