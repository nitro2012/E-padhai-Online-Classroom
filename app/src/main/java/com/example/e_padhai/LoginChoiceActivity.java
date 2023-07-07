package com.example.e_padhai;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class LoginChoiceActivity extends AppCompatActivity {

    public static String userType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_choice);

        Button facultyButton = (Button) findViewById(R.id.faculty_login_button);
        Button studentButton = (Button) findViewById(R.id.student_login_button);


        facultyButton.setOnClickListener(view -> openFacultyLogin());


        studentButton.setOnClickListener(view -> openStudentLogin());

    }



    public void openFacultyLogin() {
        Intent intent = new Intent(this, LoginPageActivity.class);
        userType = "Faculty";
        intent.putExtra("name","Faculty");
        startActivity(intent);
    }

    public void openStudentLogin() {
        Intent intent = new Intent(this, LoginPageActivity.class);
        userType = "Student";
        intent.putExtra("name", "Student");
        startActivity(intent);
    }

}