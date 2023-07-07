package com.example.e_padhai;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FacultyPanel extends AppCompatActivity {
    FloatingActionButton fab;
    private RecyclerView classRecycler;
    private NestedScrollView classLayout;
    private TextView noClass;
    private List<classesData> list;
    private DatabaseReference reference, reference2;
    private String userId;
    private ClassAdapter adapter;
    private FirebaseFirestore fstore;
    private String category;
    ProgressDialog pd;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faculty_panel);
        fab=findViewById(R.id.fab1);

        classRecycler=findViewById(R.id.classRecycler);
        pd = new ProgressDialog(this);
        classLayout=findViewById(R.id.classLayout);
        noClass=findViewById(R.id.noClass);
        fstore= FirebaseFirestore.getInstance();
        userId=FirebaseAuth.getInstance().getCurrentUser().getUid();
        DocumentReference df=fstore.collection("users").document(userId);
        pd.setMessage("Loading...");
        pd.show();
        df.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                pd.dismiss();
                if (document.exists()) {
                    category=document.getString("category");
                    reference= DatabaseUtil.getDatabase().getReference().child("Teacher").child(category).child(userId);

                    viewClass();

                    Intent intent=new Intent(FacultyPanel.this,AddClass.class);
                    intent.putExtra("id",userId);
                    intent.putExtra("category",category);

                    fab.setOnClickListener(v -> startActivity(intent));
                    Log.d("thisone", "DocumentSnapshot data: " + document.getData());
                } else {
                    Log.d("thisone", "No such document");
                }
            }else {
                pd.dismiss();
                Log.d("TAG", "get failed with ", task.getException());
            }

        });

    }

    private void viewClass() {
        reference.addValueEventListener(new ValueEventListener() {


            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                list=new ArrayList<classesData>();
                if(!snapshot.hasChild("classIdArr")){
                    noClass.setVisibility(View.VISIBLE);
                    classLayout.setVisibility(View.GONE);
                }
                else
                {
                    pd.setMessage("Loading...");
                    pd.show();
                    noClass.setVisibility(View.GONE);
                    classLayout.setVisibility(View.VISIBLE);
                    HashMap<String, Object> cId;

                    GenericTypeIndicator<HashMap<String, Object>> t = new GenericTypeIndicator<HashMap<String, Object>>() {};

                    cId=snapshot.getValue(t);

                   reference2=DatabaseUtil.getDatabase().getReference().child("Classes");


                    HashMap<String, Object> finalCId = cId;

                    reference2.addValueEventListener(new ValueEventListener() {
                       @Override

                       public void onDataChange(@NonNull @NotNull DataSnapshot dsnapshot) {
                           int k = 0;
                           ArrayList fddd;
                           for (DataSnapshot dsnapshot1 : dsnapshot.getChildren()) {
                               assert finalCId != null;
                               fddd = (ArrayList) finalCId.get("classIdArr");
                               assert fddd != null;
                               if(fddd.size()==list.size())
                                   break;

                               GenericTypeIndicator<classesData> t = new GenericTypeIndicator<classesData>() {};


                               classesData data = dsnapshot1.getValue(t);
                               assert data != null;

                               int i;
                                for( i=0;i<fddd.size();i++){
                               if (data.getClassId().equals(fddd.get(i))) {
                                   list.add(data);

                                   k = k + 1;
                               }}
                               if (k == fddd.size())
                                   break;
                           }

                           classRecycler.setHasFixedSize(true);
                           classRecycler.setLayoutManager(new LinearLayoutManager(FacultyPanel.this));
                           adapter = new ClassAdapter(list, FacultyPanel.this);
                           adapter.notifyDataSetChanged();
                           classRecycler.setAdapter(adapter);

                       }

                       @Override
                       public void onCancelled(@NonNull @NotNull DatabaseError error) {

                       }
                   });

pd.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
                Toast.makeText(FacultyPanel.this, "Something went Wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }
}