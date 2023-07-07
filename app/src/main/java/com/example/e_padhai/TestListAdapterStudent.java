package com.example.e_padhai;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.NotNull;


import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class TestListAdapterStudent extends RecyclerView.Adapter<TestListAdapterStudent.TestListViewAdapter> {
    private List<HashMap> list;
    private Context context;
    private String category,classId;
    public TestListAdapterStudent(List<HashMap> list, Context context, String category,String classId) {
        this.list = list;
        this.context = context;
        this.classId=classId;
        this.category=category;
    }

    @NonNull
    @NotNull
    @Override
    public TestListViewAdapter onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.test_list_view_layout,parent,false);

        return new TestListViewAdapter(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull TestListViewAdapter holder, int position) {
        HashMap item=list.get(position);
        String uid= Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
DatabaseUtil.getDatabase().getReference().child("Classes").child(classId).child("Tests").child((String) Objects.requireNonNull(item.get("TestId"))).child("Result").child(uid).addValueEventListener(new ValueEventListener() {
    @Override
    public void onDataChange(@NonNull DataSnapshot snapshot) {
        Long mark= (Long) snapshot.getValue();
        if(mark!=null){
        holder.marks.setText(mark.toString());}
    }

    @Override
    public void onCancelled(@NonNull DatabaseError error) {

    }
});

        ArrayList<Long> l=(ArrayList<Long>) item.get("TimeStamp");
        Timestamp ts
                = new Timestamp(l.get(0));


        Date date = ts;

        holder.TestCode.setText(Integer.toString(position+1));
        holder.date.setText(date.toString());


if(category.equals("Current")) {
    holder.itemView.setOnClickListener(view -> {
        Intent intent = new Intent(context, TestActivityStudent.class);

        intent.putExtra("classId", classId);
        intent.putExtra("testId", (String) item.get("TestId"));
        context.startActivity(intent);
    });
}





        


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class TestListViewAdapter extends RecyclerView.ViewHolder {
        private TextView TestCode,date,marks;


        public TestListViewAdapter(@NonNull @NotNull View itemView) {
            super(itemView);
            TestCode=itemView.findViewById(R.id.testCode);
            marks=itemView.findViewById(R.id.mark);
            date=itemView.findViewById(R.id.date);


            
        }
    }
}
