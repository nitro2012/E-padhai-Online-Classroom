package com.example.e_padhai;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

//import com.example.collegeapp.LoginPage;
//import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
//import com.smarteist.autoimageslider.SliderAnimations;
//import com.smarteist.autoimageslider.SliderView;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginHomeFragment extends Fragment {

    private CardView facultycard;
    private CardView studentcard;

    public LoginHomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login_home, container, false);

        facultycard = view.findViewById(R.id.faculty_login);
        studentcard = view.findViewById(R.id.student_login);

        facultycard.setOnClickListener(view12 -> {

            Intent fi=new Intent(getContext(),LoginPageActivity.class);
            MainActivity.userType="Faculty";
            fi.putExtra("name","Faculty");
            startActivity(fi);
        });

        studentcard.setOnClickListener(view13 -> {

            Intent si=new Intent(getContext(),LoginPageActivity.class);
            MainActivity.userType="Student";
            si.putExtra("name","Student");
            startActivity(si);
        });

        return view;
    }
}