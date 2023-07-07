package com.example.e_padhai;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;

public class AskADoubt extends AppCompatActivity {

    private Toolbar toolbar;
    private Spinner spinner;
    private EditText question_box;
    private ImageView imageView;
    private Button cancelbtn, postbtn;

    private String askedByName ="";
    private DatabaseReference askedByReference;
    private ProgressDialog loader;
    private String myUrl ="";
    StorageTask uploadTask;
    StorageReference storageReference;
    private Uri imageuri;

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private String onLineUserid="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_ask_adoubt);

        toolbar = findViewById(R.id.ask_ques_toolbar);
        setSupportActionBar(toolbar);
       // getSupportActionBar().setTitle("E-Padhai App");


        spinner = findViewById(R.id.category_spinner);
        question_box = findViewById(R.id.doubt_entry);
        imageView = findViewById(R.id.ques_image);
        cancelbtn = findViewById(R.id.cancel_button);
        postbtn = findViewById(R.id.post_button);

        loader =  new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        onLineUserid = mUser.getUid();

        askedByReference = FirebaseDatabase.getInstance().getReference("users").child(onLineUserid);

    }
}