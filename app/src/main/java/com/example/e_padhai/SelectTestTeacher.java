package com.example.e_padhai;

import static android.view.View.VISIBLE;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SelectTestTeacher extends AppCompatActivity {

    private RecyclerView currentTests,upcomingTests,pastTests;
    private LinearLayout currentNoData,upcomingNoData,pastNoData;
    private List<HashMap> list1;
    private List<HashMap> list2;
    private List<HashMap> list3;
    private FloatingActionButton fab;
    private DatabaseReference reference,dbRef;
    private TestListAdapterTeacher adapter;
    public String classId="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_test_teacher);

        classId=getIntent().getStringExtra("id");
        currentTests=findViewById(R.id.TcurrentTests);
        upcomingTests=findViewById(R.id.TupcomingTests);
        pastTests =findViewById(R.id.TpastTests);

        currentNoData=findViewById(R.id.TcurrentNoData);
        upcomingNoData=findViewById(R.id.TupcomingNoData);
        pastNoData=findViewById(R.id.TpastNoData);

        reference= DatabaseUtil.getDatabase().getReference().child("Classes").child(classId).child("Tests");

        fab=findViewById(R.id.newTestFab);
        fab.setOnClickListener(view -> {
            Intent intent =new Intent(SelectTestTeacher.this,TestActivityTeacher.class);

            intent.putExtra("id",classId);
            startActivity(intent);
        });
        fetchTestData();


    }

    private void fetchTestData() {


        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                list1=new ArrayList<HashMap>();
                list2=new ArrayList<HashMap>();
                list3=new ArrayList<HashMap>();



                for(DataSnapshot dsnapshot:snapshot.getChildren()){

                    HashMap TestIdArr=(HashMap) dsnapshot.getValue();
                    ArrayList al= (ArrayList) TestIdArr.get("TimeStamp");
                    Long start= (Long) al.get(0);
                    Long end= (Long) al.get(1);
                    Long datetime = System.currentTimeMillis();
                    TestIdArr.put("TestId",dsnapshot.getKey());
                    if(datetime>=start&&datetime<end)
                        list1.add(TestIdArr);

                    else if(datetime<start)
                        list2.add(TestIdArr);

                    else
                        list3.add(TestIdArr);



                }

                if(list1.isEmpty()){
                    currentNoData.setVisibility(View.VISIBLE);
                    currentTests.setVisibility(View.GONE);
                }
                else {
                    currentNoData.setVisibility(View.GONE);
                    currentTests.setVisibility(VISIBLE);
                    currentTests.setHasFixedSize(true);
                    currentTests.setLayoutManager(new LinearLayoutManager(SelectTestTeacher.this));
                    adapter = new TestListAdapterTeacher(list1, SelectTestTeacher.this, "Current",classId);
                    currentTests.setAdapter(adapter);
                }if(list2.isEmpty()){
                    upcomingNoData.setVisibility(View.VISIBLE);
                    upcomingTests.setVisibility(View.GONE);
                }
                else {
                    upcomingNoData.setVisibility(View.GONE);
                    upcomingTests.setVisibility(VISIBLE);
                    upcomingTests.setHasFixedSize(true);
                    upcomingTests.setLayoutManager(new LinearLayoutManager(SelectTestTeacher.this));
                    adapter = new TestListAdapterTeacher(list2, SelectTestTeacher.this,  "upcoming", classId);
                    upcomingTests.setAdapter(adapter);
                }if(list3.isEmpty()){
                    pastNoData.setVisibility(View.VISIBLE);
                    pastTests.setVisibility(View.GONE);
                }
                else {
                    pastNoData.setVisibility(View.GONE);
                    pastTests.setVisibility(VISIBLE);
                    pastTests.setHasFixedSize(true);
                    pastTests.setLayoutManager(new LinearLayoutManager(SelectTestTeacher.this));
                    adapter = new TestListAdapterTeacher(list3, SelectTestTeacher.this, "Past", classId);
                    pastTests.setAdapter(adapter);
                }

            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
                Toast.makeText(SelectTestTeacher.this, "Something went wrong ", Toast.LENGTH_SHORT).show();
            }
        });
    }



}