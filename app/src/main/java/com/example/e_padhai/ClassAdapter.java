package com.example.e_padhai;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ClassAdapter extends RecyclerView.Adapter<ClassAdapter.classViewAdapter> {
private List<classesData> list;
private Context context;

    public ClassAdapter(List<classesData> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @NotNull
    @Override
    public classViewAdapter onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.class_item_layout,parent,false);

        return new classViewAdapter(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull classViewAdapter holder, int position) {

        classesData item=list.get(position);
        String stud;
        if(item.getStudentList()!=null)
        {stud = item.getStudentList().size()+" Students";
        holder.nStud.setText(stud);}

        holder.className.setText(item.getClassName());
        holder.section.setText(item.getSection());
        holder.classView.setOnClickListener(v -> {
            String id=item.getClassId();
            Intent intent=new Intent(context, viewClass.class);
            intent.putExtra("id",id);
            context.startActivity(intent);

        });

    }

    @Override
    public int getItemCount() {

        return list.size();
    }

    public class classViewAdapter extends RecyclerView.ViewHolder {
        private TextView className,section,nStud;
        private LinearLayout classView;
        public classViewAdapter(@NonNull @NotNull View itemView) {

            super(itemView);
            className=itemView.findViewById(R.id.cname);
            section=itemView.findViewById(R.id.sec);
            nStud=itemView.findViewById(R.id.nstud);
            classView=itemView.findViewById(R.id.classView);
        }
    }
}
