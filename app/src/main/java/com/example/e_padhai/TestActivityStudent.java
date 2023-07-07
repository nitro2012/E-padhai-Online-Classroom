package com.example.e_padhai;

import static java.lang.Long.parseLong;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompatSideChannelService;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TestActivityStudent extends AppCompatActivity implements OnOptionClickListener,OnOptionCheckedListener {

    private DatabaseReference databaseReference;
    private DatabaseReference questionReference;
    private FirebaseAuth auth;
    private String studentId;
    private List<List<String>>  questAndAns ;
    private  List<List<Integer> >ansStr= null;
    private testAdapter adapter;
    private RecyclerView testRec;
    List<Long> timeStamps;

    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_student);

        progressDialog = new ProgressDialog(this);
        ansStr= new ArrayList<>();
        auth=FirebaseAuth.getInstance();
        studentId= Objects.requireNonNull(auth.getCurrentUser()).getUid();
        testRec=findViewById(R.id.TestRecStudent);
        Button submit = findViewById(R.id.submitBtn);
        String classCode=getIntent().getStringExtra("classId");
           String     testCode=getIntent().getStringExtra("testId");


        databaseReference=DatabaseUtil.getDatabase().getReference().child("Classes").child(classCode).child("Tests").child(testCode);
        questionReference = databaseReference.child("Questions");
        DatabaseReference timeStampReference = databaseReference.child("TimeStamp");

        timeStampReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                timeStamps= (List<Long>) snapshot.getValue();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        generateTest();
        submit.setOnClickListener(view -> submitTest());
    }

    private void generateTest() {

        questionReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                questAndAns= (List<List<String>>) snapshot.getValue();
                List<Integer> tmp=new ArrayList<>();
                tmp.add(0);
                for(int i=0;i<questAndAns.size();i++)
                {

                    ansStr.add( tmp);
                }
                testRec.setVisibility(View.VISIBLE);


                testRec.setHasFixedSize(false);
                testRec.setLayoutManager(new LinearLayoutManager(TestActivityStudent.this));
                adapter = new testAdapter(questAndAns, TestActivityStudent.this,TestActivityStudent.this,TestActivityStudent.this,false);
                adapter.notifyDataSetChanged();
                testRec.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void submitTest() {
        Long datetime = System.currentTimeMillis();
        if((timeStamps.get(0)) <=datetime &&  (timeStamps.get(1))>=datetime ) {
            databaseReference.child("Responses").child(studentId).setValue(ansStr).addOnSuccessListener(aVoid -> {
                progressDialog.dismiss();
                Toast.makeText(TestActivityStudent.this, "Response Submitted Successfully !!", Toast.LENGTH_SHORT).show();
                finish();
            }).addOnFailureListener(e -> {
                progressDialog.dismiss();
                Toast.makeText(TestActivityStudent.this, "Something went Wrong", Toast.LENGTH_SHORT).show();
            });
        }
        else
            Toast.makeText(this, "Time Up !!", Toast.LENGTH_SHORT).show();

    }




    @Override
    public void onOptionClick(int num,int pos) {



        List<Integer> tmp=new ArrayList<>();

        tmp.add(num);

        ansStr.set(pos,tmp);
    }
    @Override
    public void onOptionCheck(int num,int pos) {

        (ansStr.get(pos)).remove((Integer)(0));

            if(num>0)
                if(!(ansStr.get(pos)).contains((Integer)(num)))
                (ansStr.get(pos)).add(num);
            else
            {
                   (ansStr.get(pos)).remove((Integer)(-1*num));


            }




    }
}