package com.example.e_padhai;



import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;

import android.icu.util.Calendar;

import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RadioGroup;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.card.MaterialCardView;

import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class TestActivityTeacher extends AppCompatActivity implements OnOptionClickListener , OnOptionCheckedListener {
MaterialCardView newQues;
    TimePicker startPicker,endPicker;
    DatePicker testDatePicker;
    List<Long> timeStamps=new ArrayList<>();
    List<List<Integer> >ansList=new ArrayList<>();
    private ActivityResultLauncher<Intent> activityResultLauncher;
String classCode;
    private DatabaseReference databaseReference;
    private List<List<String>>  questAndAns = new ArrayList<List<String>>();
    private Button post;

    private testAdapter adapter;
private RecyclerView testRec;
    private String quest,o1,o2,o3,o4;
    private ProgressDialog progressDialog;
    private int nQues;
    private boolean checkFlag,radioFlag=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {


                        assert result.getData() != null;
                        quest=result.getData().getStringExtra("question");
                        o1=result.getData().getStringExtra("option1");
                        o2=result.getData().getStringExtra("option2");
                        o3=result.getData().getStringExtra("option3");
                        o4=result.getData().getStringExtra("option4");
                        List<String> x = new ArrayList<String>();
                        x.add(quest);
                        x.add(o1);
                        x.add(o2);
                        x.add(o3);
                        x.add(o4);
                        x.add("0");
                        questAndAns.add(x);

                        testRec.setVisibility(View.VISIBLE);


                        testRec.setHasFixedSize(false);
                        testRec.setLayoutManager(new LinearLayoutManager(TestActivityTeacher.this));
                        adapter = new testAdapter(questAndAns, TestActivityTeacher.this,TestActivityTeacher.this,TestActivityTeacher.this,true);
                        adapter.notifyDataSetChanged();
                        testRec.setAdapter(adapter);
                    }
                });


        setContentView(R.layout.activity_test_teacher);
classCode=getIntent().getStringExtra("id");//from intent
        startPicker= findViewById(R.id.startTime);
        endPicker= findViewById(R.id.endTime);
        testDatePicker=findViewById(R.id.testDate);
        progressDialog = new ProgressDialog(this);
        newQues=findViewById(R.id.newQues);
        testRec=findViewById(R.id.TestRec);
post=findViewById(R.id.postBtn);
        databaseReference = DatabaseUtil.getDatabase().getReference().child("Classes");
        newQues.setOnClickListener(view -> addQuestion());
        post.setOnClickListener(view -> postTest());
    }

    private void postTest() {

        setDateTime();
        if(questAndAns.size()==0)
            Toast.makeText(this, "Enter question", Toast.LENGTH_SHORT).show();

        else{
            progressDialog.setMessage("Uploading...");
            progressDialog.show();
            final String uniquekey=databaseReference.child(classCode).child("Tests").push().getKey();
            assert uniquekey != null;


            databaseReference.child(classCode).child("Tests").child(uniquekey).child("TimeStamp").setValue(timeStamps).addOnSuccessListener(aVoid -> {
                progressDialog.dismiss();




                databaseReference.child(classCode).child("Tests").child(uniquekey).child("Questions").setValue(questAndAns).addOnSuccessListener(abVoid -> {
                    progressDialog.dismiss();


                    Toast.makeText(TestActivityTeacher.this, "Test Successfully Posted", Toast.LENGTH_SHORT).show();






                }).addOnFailureListener(e -> {
                    progressDialog.dismiss();
                    Toast.makeText(TestActivityTeacher.this, "Something went Wrong", Toast.LENGTH_SHORT).show();
                });




            }).addOnFailureListener(e -> {
                progressDialog.dismiss();
                Toast.makeText(TestActivityTeacher.this, "Something went Wrong", Toast.LENGTH_SHORT).show();
            });


            databaseReference.child(classCode).child("Tests").child(uniquekey).child("Answers").setValue(ansList).addOnSuccessListener(aaVoid -> {
                progressDialog.dismiss();


                Toast.makeText(TestActivityTeacher.this, "Answers Successfully Posted", Toast.LENGTH_SHORT).show();

                finish();




            }).addOnFailureListener(e -> {
                progressDialog.dismiss();
                Toast.makeText(TestActivityTeacher.this, "Something went Wrong", Toast.LENGTH_SHORT).show();
            });


        }

    }


    private void setDateTime() {
        int year = testDatePicker.getYear();
        int month = testDatePicker.getMonth();
        int day = testDatePicker.getDayOfMonth();
        int shour= startPicker.getCurrentHour();
        int sminute = startPicker.getCurrentMinute();
        int ehour= endPicker.getCurrentHour();
        int eminute = endPicker.getCurrentMinute();


        Calendar calendar = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            calendar = Calendar.getInstance();

            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, day);
            calendar.set(Calendar.HOUR_OF_DAY, shour);
            calendar.set(Calendar.MINUTE, sminute);
            calendar.set(Calendar.SECOND, 0);
            timeStamps.add(calendar.getTimeInMillis());
            calendar.set(Calendar.HOUR_OF_DAY, ehour);
            calendar.set(Calendar.MINUTE, eminute);
            timeStamps.add(calendar.getTimeInMillis());
        }
    }

    public void addQuestion() {
        Intent intent=new Intent(TestActivityTeacher.this,addQues.class);
activityResultLauncher.launch(intent);
        nQues++;

List<Integer> tmp=new ArrayList<>();
tmp.add(1);
ansList.add(tmp);
    }


    @Override
    public void onOptionClick(int num,int pos) {

        if(radioFlag==false){

            if((questAndAns.get(pos)).size()==6)
            (questAndAns.get(pos)).set(5,"0");

            else
                (questAndAns.get(pos)).add("0");}
            checkFlag=false;
            radioFlag=true;

        List<Integer> tmp=new ArrayList<>();

        tmp.add(num);

        ansList.set(pos,tmp);
    }
    @Override
    public void onOptionCheck(int num,int pos) {

        if(!checkFlag)
        {
            List<Integer> tmpo=new ArrayList<>();
            tmpo.add(1);

            if(!(ansList.get(pos)).contains((Integer)(num)))
            ansList.set(pos,tmpo);

            adapter.getHolder().check1.setChecked(true);


            int k=questAndAns.get(pos).size();
            if(questAndAns.get(pos).size()==6)

            (questAndAns.get(pos)).set(5,"1");
            else
            {(questAndAns.get(pos)).add("1");}

            checkFlag=true;
            radioFlag=false;

            List<Integer> tmp=new ArrayList<>();
            tmp.add(1);
            if(num>0)
            {
                tmp.add(num);

                if(!(ansList.get(pos)).contains((Integer)(num)))
                ansList.set(pos,tmp);}
            else
            {
                if((ansList.get(pos)).size()==1) {

                    adapter.getHolder().check1.setChecked(true);
                }

                else
                    (ansList.get(pos)).remove((Integer)(-1*num));

            }



        }
        else{

            if(num>0)
                if(!(ansList.get(pos)).contains((Integer)(num)))
                (ansList.get(pos)).add(num);
            else
            {
                if((ansList.get(pos)).size()==1) {

                    adapter.getHolder().check1.setChecked(true);
                }

                else
                (ansList.get(pos)).remove((Integer)(-1*num));


            }

        }


    }
}