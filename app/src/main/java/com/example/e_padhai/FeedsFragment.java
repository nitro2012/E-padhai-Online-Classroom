package com.example.e_padhai;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class FeedsFragment extends Fragment {

    public AppCompatActivity activity;

    private Toolbar toolbar;
    private FloatingActionButton fab;

    public FeedsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_feeds, container, false);

//        toolbar = (Toolbar) view.findViewById(R.id.main_feed_toolbar);
//        fab = (FloatingActionButton) view.findViewById(R.id.add_ques_action_button);
//        activity = (AppCompatActivity) getActivity();
//        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
//       // ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("E-Padhai App");
//
//        fab.setOnClickListener(view1 -> {
//            Intent intent = new Intent(getActivity(), AskADoubt.class);
//            startActivity(intent);
//        });



        return view;
    }
}