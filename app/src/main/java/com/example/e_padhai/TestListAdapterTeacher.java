package com.example.e_padhai;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.annotations.NotNull;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class TestListAdapterTeacher extends RecyclerView.Adapter<TestListAdapterTeacher.TestListViewAdapter> {
    private List<HashMap> list;
    private Context context;
    private String testId,classId;
    public TestListAdapterTeacher(List<HashMap> list, Context context, String category, String classId) {
        this.list = list;
        this.context = context;
        this.testId=category;
        this.classId=classId;
    }

    @NonNull
    @NotNull
    @Override
    public TestListAdapterTeacher.TestListViewAdapter onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.teacher_test_list_view_layout,parent,false);

        return new TestListAdapterTeacher.TestListViewAdapter(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull TestListAdapterTeacher.TestListViewAdapter holder, int position) {
        HashMap item=list.get(position);



        ArrayList<Long> l=(ArrayList<Long>) item.get("TimeStamp");
        Timestamp ts
                = new Timestamp(l.get(0));


        Date date = ts;

        holder.TestCode.setText(Integer.toString(position+1));
        holder.date.setText(date.toString());


        holder.viewTest.setOnClickListener(view -> {
            Intent intent =new Intent(context,viewResult.class);


            intent.putExtra("classId",classId);
            intent.putExtra("testId",(String)item.get("TestId"));
            context.startActivity(intent);
        });









    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class TestListViewAdapter extends RecyclerView.ViewHolder {
        private TextView TestCode,date;
        private Button viewTest;

        public TestListViewAdapter(@NonNull @NotNull View itemView) {
            super(itemView);
            TestCode=itemView.findViewById(R.id.testCode);

            date=itemView.findViewById(R.id.date);

            viewTest=itemView.findViewById(R.id.viewTest);


        }
    }
}

