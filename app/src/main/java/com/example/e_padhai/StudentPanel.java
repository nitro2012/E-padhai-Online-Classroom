package com.example.e_padhai;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
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
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StudentPanel extends AppCompatActivity {
    FloatingActionButton fab;
    private RecyclerView classRecycler;
    private NestedScrollView classLayout;
    private TextView noClass;
    private List<classesData> list;
    private DatabaseReference reference, reference2;
    private String userId;
    private ClassAdapter adapter;
    private FirebaseFirestore fstore;
    private DataSnapshot ref;
    ProgressDialog pd;
    private String mClass=null;
    private DatabaseReference studentReference;

    private ValueEventListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_panel);
        fab=findViewById(R.id.fab2);
        classRecycler=findViewById(R.id.classViewRecycler);
        pd = new ProgressDialog(this);
        classLayout=findViewById(R.id.classStudentLayout);
        noClass=findViewById(R.id.noStudentClass);
        fstore= FirebaseFirestore.getInstance();
        userId= FirebaseAuth.getInstance().getCurrentUser().getUid();
        DocumentReference df=fstore.collection("users").document(userId);
        reference2= DatabaseUtil.getDatabase().getReference().child("Classes");
        reference= DatabaseUtil.getDatabase().getReference().child("Student").child(userId);

        studentReference = DatabaseUtil.getDatabase().getReference().child("Student");


        pd.setMessage("Loading...");
        pd.show();

        try {
            viewClass();
        } catch (Exception e) {
            e.printStackTrace();
        }

        fab.setOnClickListener(v -> AddClass());

    }


    private void updateStudent() {

        studentReference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                StudentData td = snapshot.getValue(StudentData.class);

                reference2.addListenerForSingleValueEvent(new ValueEventListener() {


                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot1) {
                        HashMap<String, Object> hp = new HashMap<String, Object>();
                        ArrayList<String> classIdArr;

                        int fl=1;
                        classIdArr = td.getClassIdArr();
                        if(classIdArr==null)
                            classIdArr=new ArrayList<>();
                        for(String ids:classIdArr){
                            if (ids.equals(mClass)) {
                                fl = 0;
                                Toast.makeText(StudentPanel.this, "choose different class!", Toast.LENGTH_SHORT).show();
                                break;
                            }
                        }
                        if(fl==1){
                        if(snapshot1.hasChild(mClass)){
                            if(mClass!=null)
                                classIdArr.add(mClass);


                            StudentData post = new StudentData(td.getName(),td.getEmail(),td.getRoll(),td.getKey(),td.getCategory(),classIdArr);
                            Map<String, Object> postValues = post.toMap();

                            Map<String, Object> childUpdates = new HashMap<>();
                            childUpdates.put("/"+userId, postValues);

                            studentReference.updateChildren(childUpdates).addOnSuccessListener(o -> {

                                try {
                                    updateClass();
                                    viewClass();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            }).addOnFailureListener(e -> Toast.makeText(StudentPanel.this, "Something went wrong", Toast.LENGTH_SHORT).show());

                        }
                        else{
                            Toast.makeText(StudentPanel.this, "Class does not exist!", Toast.LENGTH_SHORT).show();
                        }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) {

                    }
                });

//





            }
            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

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
                    pd.dismiss();

                }
                else
                {
                    noClass.setVisibility(View.GONE);
                    classLayout.setVisibility(View.VISIBLE);
                    pd.dismiss();
                    HashMap<String, Object> cId;

                    GenericTypeIndicator<HashMap<String, Object>> t = new GenericTypeIndicator<HashMap<String, Object>>() {};

                    cId=snapshot.getValue(t);




                    HashMap<String, Object> finalCId = cId;
                    reference2.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override

                        public void onDataChange(@NonNull @NotNull DataSnapshot dsnapshot) {
                            int k = 0;
                            ArrayList fddd = null;
                            for (DataSnapshot dsnapshot1 : dsnapshot.getChildren()) {
                                classesData data = dsnapshot1.getValue(classesData.class);
                                assert data != null;
                                fddd = (ArrayList) finalCId.get("classIdArr");
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
                            classRecycler.setLayoutManager(new LinearLayoutManager(StudentPanel.this));
                            adapter = new ClassAdapter(list, StudentPanel.this);
                            adapter.notifyDataSetChanged();
                            classRecycler.setAdapter(adapter);

                        }

                        @Override
                        public void onCancelled(@NonNull @NotNull DatabaseError error) {

                        }
                    });


                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
                Toast.makeText(StudentPanel.this, "Something went Wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }


    void AddClass(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter class Code");

// Set up the input
        final EditText input = new EditText(this);
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

// Set up the buttons
        builder.setPositiveButton("OK", (dialog, which) -> {
            mClass = input.getText().toString();
            updateStudent();
            //updateClass();
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        builder.show();

      //  updateStudent();

    }

    private void updateClass() {

        reference2.child(mClass).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                classesData cd = snapshot.getValue(classesData.class);
                ArrayList<String> studentArr;
                assert cd != null;
                studentArr =  cd.getStudentList();
                if(studentArr==null)
                    studentArr=new ArrayList<>();
                studentArr.add(userId);

                ArrayList fdat=cd.getfData();
                classesData post = new classesData(cd.getVideoCall(),cd.getClassName(),cd.getSection(),cd.getTeacherName(), cd.getClassId(), studentArr,fdat,cd.getTests());
                Map<String, Object> postValues = post.toMap();

                Map<String, Object> childUpdates = new HashMap<>();
                childUpdates.put("/"+mClass, postValues);

                reference2.updateChildren(childUpdates).addOnSuccessListener(o -> {


                }).addOnFailureListener(e -> Toast.makeText(StudentPanel.this, "Something went wrong", Toast.LENGTH_SHORT).show());
//





            }
            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

    }

}