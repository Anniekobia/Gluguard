package com.example.gluconnect.UI;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.gluconnect.R;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private TextInputEditText username;
    private TextInputEditText email;
    TextInputEditText password;
    TextView errorMsg;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        username = findViewById(R.id.username);
        email = findViewById(R.id.signup_email);
        errorMsg = findViewById(R.id.error_msg);
        password = findViewById(R.id.signup_password);

        sharedPreferences = getApplicationContext().getSharedPreferences("MyPreferences", 0);

        boolean isLoggedIn = sharedPreferences.getBoolean("IsLoggedIn", false);
//        if (isLoggedIn) {
//            Intent intent = new Intent(MainActivity.this, DailyLogsActivity.class);
//            startActivity(intent);
//        } else {

            int position = 0;
            Bundle extras = getIntent().getExtras();
            if(extras != null) {
                position = extras.getInt("viewpager_position");
            }

            viewPager = findViewById(R.id.viewpager);
            ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
            adapter.addFragment(new FeaturesFragmentLog(), "");
            adapter.addFragment(new FeaturesFragmentTips(), "");
            adapter.addFragment(new FeaturesFragmentTrends(), "");
            adapter.addFragment(new FeaturesFragmentDiary(), "");
            adapter.addFragment(new LoginFragment(), "");
            viewPager.setAdapter(adapter);
            viewPager.setCurrentItem(position);
            TabLayout tabLayout = findViewById(R.id.tab_layout);
            tabLayout.setupWithViewPager(viewPager, true);
//        }
    }


    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mList = new ArrayList<>();
        private final List<String> mTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager supportFragmentManager) {
            super(supportFragmentManager);
        }

        @Override
        public Fragment getItem(int i) {
            return mList.get(i);
        }

        @Override
        public int getCount() {
            return mList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mList.add(fragment);
            mTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTitleList.get(position);
        }
    }

}
