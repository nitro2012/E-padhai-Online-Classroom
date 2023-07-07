package com.example.e_padhai;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class DoubtSolvingPanelActivity extends AppCompatActivity {

    BottomNavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doubt_solving_panel);

        navigationView = findViewById(R.id.bottom_navigation);
        getSupportFragmentManager().beginTransaction().replace(R.id.body_container, new FeedsFragment()).commit();
        navigationView.setSelectedItemId(R.id.nav_feed);

        navigationView.setOnItemSelectedListener(item -> {
            Fragment fragment = null;
            switch(item.getItemId()){
                case R.id.nav_feed:
                    fragment = new FeedsFragment();
                    break;


                case R.id.nav_trend:
                    fragment = new TrendingFragment();
                    break;
            }
            getSupportFragmentManager().beginTransaction().replace(R.id.body_container, fragment).commit();


            return true;
        });
    }
}