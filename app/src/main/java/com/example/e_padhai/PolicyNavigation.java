package com.example.e_padhai;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class PolicyNavigation extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_policy);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
