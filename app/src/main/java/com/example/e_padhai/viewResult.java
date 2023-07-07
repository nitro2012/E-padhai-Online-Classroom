package com.example.e_padhai;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;


public class viewResult extends AppCompatActivity {

    Button generateRes;
    private DatabaseReference databaseReference;
    private  String classId=null;
    private  String testId=null;
    TableLayout table;
    private HashMap<String, Double> result;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_result);

        classId=getIntent().getStringExtra("classId");
        testId=getIntent().getStringExtra("testId");
        databaseReference=DatabaseUtil.getDatabase().getReference().child("Classes").child(classId).child("Tests").child(testId);

        generateRes=findViewById(R.id.generateRes);

        result=new HashMap<String, Double>();


        table=findViewById(R.id.resTable);


            databaseReference.child("Result").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {


                    HashMap<String, String> res = (HashMap<String, String>) snapshot.getValue();

                    int cnt = 1;

                    if(res!=null) {
                        for (Map.Entry entry : res.entrySet()) {


                            TableRow row = new TableRow(viewResult.this);
                            TextView tv2 = new TextView(viewResult.this);
                            TextView tv1 = new TextView(viewResult.this);
                            TextView tv3 = new TextView(viewResult.this);
                            tv1.setText(String.valueOf(cnt++));
                            tv2.setText(String.valueOf((String) entry.getKey()).substring(0, 5));
                            tv3.setText(String.valueOf(entry.getValue())+"%");
                            tv2.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1f));
                            tv1.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1f));
                            tv3.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1f));

                            row.addView(tv1);
                            row.addView(tv2);

                            row.addView(tv3);
                            table.addView(row);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });


        generateRes.setOnClickListener(v -> calculateResult());

    }



    void calculateResult(){
        databaseReference.child("Answers").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<List<Integer> >ansList= (List<List<Integer>>) snapshot.getValue();

                int total=ansList.size();
                for(List<Integer> l:ansList)
                {
                    Collections.sort(l);
                }

                databaseReference.child("Responses").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot1) {

                        HashMap<String,List<List<Integer>>> responses= (HashMap<String, List<List<Integer>>>) snapshot1.getValue();

                        if(responses==null)
                            Toast.makeText(viewResult.this, "No responses!!", Toast.LENGTH_SHORT).show();
                        else {

                            for (Map.Entry entry : responses.entrySet()) {

                                List<List<Integer>> studentRes = (List<List<Integer>>) entry.getValue();
                                int marks = 0;

                                for (int i = 0; i < ansList.size(); i++) {

                                    Collections.sort(studentRes.get(i));
                                    int ques = 1;
                                    for (int j = 0; j < ansList.get(i).size(); j++) {

                                        String sAns= String.valueOf(studentRes.get(i).get(j));
                                        if(studentRes.get(i).size()!=1&& sAns.equals("0"))
                                            continue;
                                        if (!Objects.equals(studentRes.get(i).get(j), ansList.get(i).get(j))) {
                                            ques = 0;
                                            break;
                                        }


                                    }

                                    marks += ques;

                                }

                                float ans=((float)marks/(float)total)*100;//;
                                result.put((String) entry.getKey(),Math.round(ans * 100.0) / 100.0 );
                            }
                        }
                        databaseReference.child("Result").setValue(result);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }



}