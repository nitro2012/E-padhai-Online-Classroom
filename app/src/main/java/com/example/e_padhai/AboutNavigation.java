package com.example.e_padhai;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class AboutNavigation extends AppCompatActivity {

    private CardView findus_map;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        findus_map = findViewById(R.id.findus_map);
        findus_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openmaps();
            }
        });

    }

    private void openmaps() {

        Uri uri = Uri.parse("geo:0, 0?q=Delhi");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        intent.setPackage("com.google.android.apps.maps");
        startActivity(intent);
    }
}
