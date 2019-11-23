package com.example.gluconnect.UI;

import android.os.Bundle;

import com.example.gluconnect.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.Menu;
import android.view.MenuItem;

public class DailyLogsActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private  Bundle extras;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_logs);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Diary");
        setSupportActionBar(toolbar);
        loadFragment(new DiaryFragment());

        BottomNavigationView navigation = findViewById(R.id.bottom_navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        extras = getIntent().getExtras();
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
                case R.id.tips_icon:
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
