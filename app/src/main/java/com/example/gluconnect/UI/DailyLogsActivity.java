package com.example.gluconnect.UI;

import android.content.Intent;
import android.os.Bundle;

import com.example.gluconnect.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.MenuItem;
import android.view.View;

public class DailyLogsActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private  Bundle extras;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_logs);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Dairy");
        setSupportActionBar(toolbar);
        loadFragment(new DiaryFragment());

        BottomNavigationView navigation = findViewById(R.id.bottom_navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        extras = getIntent().getExtras();
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
//                    Intent intent = new Intent(DailyLogsActivity.this, DailyLogsActivityTest.class);
//                    startActivity(intent);
                    fragment = new DailyLogsFragment();
                    loadFragment(fragment);
                    return true;
                case R.id.tips_icon:
                    toolbar.setTitle("Tips");
                    fragment = new TipsFragment();
                    loadFragment(fragment);
                    return true;
                case R.id.profile_details_icon:
                    toolbar.setTitle("Profile");
                    fragment = new ProfileFragment();
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

    public void openLogTest(View view) {
        Intent intent = new Intent(this, DailyLogsActivityTest.class);
        startActivity(intent);
    }
}
