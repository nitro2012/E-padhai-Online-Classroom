package com.example.e_padhai;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.e_padhai.DatabaseUtil;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class viewClass extends AppCompatActivity {
private TextView className,section,clink;
String classId;
private RecyclerView classRec;
DatabaseReference ref;
String name;
Boolean call=false;
String type="student";
    private feedAdapter adapter;
    private ArrayList<feedData> list;
    private FirebaseFirestore fstore;
    private MaterialCardView tapShare;
    Button testButton,join ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_class);


        testButton=findViewById(R.id.testButton);
        join=findViewById(R.id.join_call_button);
        className=findViewById(R.id.cname1);
        section=findViewById(R.id.sec1);
        clink=findViewById(R.id.clink);
        tapShare=findViewById(R.id.tapShare);
        classRec=findViewById(R.id.classRec);
        classId=getIntent().getStringExtra("id");
        ref= DatabaseUtil.getDatabase().getReference().child("Classes").child(classId);
        fstore= FirebaseFirestore.getInstance();
        DocumentReference df=fstore.collection("users").document(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid());
        df.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();

                if (document.exists()) {
                    name=document.getString("Name");

                    try {
                        if(document.getBoolean("isFaculty"))
                            type="teacher";

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    tapShare.setOnClickListener(v -> Openshare());
                    setVals();
                } else {
                    Log.d("thisone", "No such document");
                    tapShare.setOnClickListener(v -> Openshare());

                }
            }else {
                tapShare.setOnClickListener(v -> Openshare());

                Log.d("TAG", "get failed with ", task.getException());
            }

        });


        testButton.setOnClickListener(v -> {

            if(type.equals("teacher"))
            {Intent intent=new Intent(viewClass.this,SelectTestTeacher.class);
                intent.putExtra("id",classId);

            startActivity(intent);}

            else
            {Intent intent=new Intent(viewClass.this,SelectTestStudent.class);
                intent.putExtra("id",classId);
                startActivity(intent);}
        });

        join.setOnClickListener(v -> {


            if(type.equals("teacher")){

                if(!call)
                {

                    call=true;
                    startVideoCall();

                }
                else{
                    call=false;
                    ref.child("VideoCall").setValue(false);
                    join.setText("Join");
                }

            }

            else{

                ref.addValueEventListener(new ValueEventListener() {
                                              @Override
                                              public void onDataChange(@NonNull DataSnapshot snapshot) {


                                                  HashMap<String, Object> classData = (HashMap<String, Object>) snapshot.getValue();

                                                  assert classData != null;
                                                  if ((Boolean) classData.get("VideoCall"))
                                                  {
                                                      call=true;
                                                      startVideoCall();

                                                  }


                                                  else {

                                                          Toast.makeText(viewClass.this, "No class !!", Toast.LENGTH_SHORT).show();
                                                  }
                                              }

                                              @Override
                                              public void onCancelled(@NonNull DatabaseError error) {

                                              }


                                          }


                );

            }

        });




    }

    private void startVideoCall(){

        ref.child("VideoCall").setValue(true);
        Intent intent=new Intent(viewClass.this,virtualClass.class);

        intent.putExtra("id",classId);

        if(type.equals("teacher"))
        join.setText("End Call");

        startActivity(intent);
    }


    @Override
    public void onResume(){
        super.onResume();
        setVals();

    }
    private void setVals() {
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                classesData data=snapshot.getValue(classesData.class);
                assert data != null;
                className.setText(data.getClassName());
                section.setText(data.getSection());
                clink.setText(data.getClassId());
                list=new ArrayList<>();
                if(snapshot.hasChild("fData")){
                    ArrayList fList= (ArrayList) snapshot.child("fData").getValue();
                    for(Object hm:fList){
                        HashMap hass=(HashMap) hm;
                        String text= (String) hass.get("text");
                        String time= (String) hass.get("time");
                        String date= (String) hass.get("date");
                        String author= (String) hass.get("author");
                        List<String> urlList= (List<String>) hass.get("urlList");
                        feedData fD=new feedData(text,time,date,author,urlList);

                                list.add(fD);
                    }

                    classRec.setVisibility(View.VISIBLE);
                   // List<feedData> fData=data.getData();

                    classRec.setHasFixedSize(false);
                    classRec.setLayoutManager(new LinearLayoutManager(viewClass.this));
                    adapter = new feedAdapter(list, viewClass.this);
                    adapter.notifyDataSetChanged();
                    classRec.setAdapter(adapter);
                }

            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }

    public void share(View view) {
    }

    public void Openshare() {

        Intent intent=new Intent(viewClass.this,ShareClass.class);
        intent.putExtra("id",classId);
        intent.putExtra("name",name);
        startActivity(intent);
    }
}