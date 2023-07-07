package com.example.e_padhai;


import android.annotation.SuppressLint;
import android.content.Context;

import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;


import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.google.firebase.database.annotations.NotNull;




import java.util.List;


public class testAdapter extends RecyclerView.Adapter<testAdapter.testViewAdapter> {
    private List<List<String>> list;
    private Context context;
    private OnOptionClickListener onOptionClickListener;
    private OnOptionCheckedListener onOptionCheckedListener;
public RecyclerView.ViewHolder holder1;
    private boolean isTeacher;
    public testAdapter(List<List<String>> list, Context context,OnOptionClickListener onOptionClickListener,OnOptionCheckedListener onOptionCheckedListener,boolean isTeacher) {
        this.list = list;
        this.context = context;
        this.onOptionClickListener=onOptionClickListener;
        this.onOptionCheckedListener=onOptionCheckedListener;
        this.isTeacher=isTeacher;
    }


    @NonNull
    @NotNull
    @Override
    public testViewAdapter onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.test_item_layout,parent,false);

        return new testAdapter.testViewAdapter(view);
    }
    public testViewAdapter getHolder(){
        return (testViewAdapter) holder1;
    }
    @Override
    public void onBindViewHolder(@NonNull @NotNull testViewAdapter holder,  int position) {

        //final String[] type = {"Radio Button"};

        holder1 = holder;



        List<String> item=list.get(position);
        int qType=0;
holder.qno.setText(String.valueOf(position+1));
        holder.question.setText(item.get(0));
        holder.option1.setText(item.get(1));
        holder.option2.setText(item.get(2));
        holder.option3.setText(item.get(3));
        holder.option4.setText(item.get(4));

        if(isTeacher) {
            holder.check1.setText(item.get(1));
            holder.check2.setText(item.get(2));
            holder.check3.setText(item.get(3));
            holder.check4.setText(item.get(4));
            String[] items = new String[]{"Radio Button", "Check Box"};
            holder.typeSpinner.setAdapter(new ArrayAdapter<String>(context, R.layout.support_simple_spinner_dropdown_item, items));
            holder.typeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                    String type = holder.typeSpinner.getSelectedItem().toString();
                    if (type == "Radio Button") {

                        holder.radioGroup.setVisibility(View.VISIBLE);
                        holder.checkBoxes.setVisibility(View.GONE);

                        RadioGroup.OnCheckedChangeListener radioListener = (group, checkedId) -> {
                            switch (checkedId) {
                /*default:

                    Toast.makeText(context, "No Support for questions with more than 4 possible answers", Toast.LENGTH_SHORT).show();
                    break;*/
                                case R.id.op4:

                                    onOptionClickListener.onOptionClick(4, position);

                                    break;
                                case R.id.op3:

                                    onOptionClickListener.onOptionClick(3, position);
                                    break;
                                case R.id.op2:

                                    onOptionClickListener.onOptionClick(2, position);
                                    break;
                                case R.id.op1:

                                    onOptionClickListener.onOptionClick(1, position);
                                    break;
                            }
                        };
                        holder.radioGroup.setOnCheckedChangeListener(radioListener);
                    } else {

                        holder.radioGroup.setVisibility(View.GONE);
                        holder.checkBoxes.setVisibility(View.VISIBLE);

                        holder.check1.setOnCheckedChangeListener((compoundButton, b) -> {
                            if(compoundButton.isChecked()){

                                onOptionCheckedListener.onOptionCheck(1,position);

                            }
                            else{
                                onOptionCheckedListener.onOptionCheck(-1,position);
                            }
                        });
                        holder.check2.setOnCheckedChangeListener((compoundButton, b) -> {
                            if(compoundButton.isChecked()){

                                onOptionCheckedListener.onOptionCheck(2,position);

                            }
                            else{
                                onOptionCheckedListener.onOptionCheck(-2,position);
                            }
                        });
                        holder.check3.setOnCheckedChangeListener((compoundButton, b) -> {
                            if(compoundButton.isChecked()){

                                onOptionCheckedListener.onOptionCheck(3,position);

                            }
                            else{
                                onOptionCheckedListener.onOptionCheck(-3,position);
                            }
                        });
                        holder.check4.setOnCheckedChangeListener((compoundButton, b) -> {
                            if(compoundButton.isChecked()){

                                onOptionCheckedListener.onOptionCheck(4,position);

                            }
                            else{
                                onOptionCheckedListener.onOptionCheck(-4,position);
                            }
                        });

                    }


                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
        else{
            qType=Integer.parseInt(item.get(5));
            holder.typeSpinner.setVisibility(View.GONE);
            if (qType==0) {

                holder.radioGroup.setVisibility(View.VISIBLE);
                holder.checkBoxes.setVisibility(View.GONE);

                RadioGroup.OnCheckedChangeListener radioListener = (group, checkedId) -> {
                    switch (checkedId) {
                /*def

                    Toast.makeText(context, "No Support for questions with more than 4 possible answers", Toast.LENGTH_SHORT).show();
                    break;*/
                        case R.id.op4:

                            onOptionClickListener.onOptionClick(4, position);

                            break;
                        case R.id.op3:

                            onOptionClickListener.onOptionClick(3, position);
                            break;
                        case R.id.op2:

                            onOptionClickListener.onOptionClick(2, position);
                            break;
                        case R.id.op1:

                            onOptionClickListener.onOptionClick(1, position);
                            break;
                    }
                };
                holder.radioGroup.setOnCheckedChangeListener(radioListener);
            }

            else {

                holder.radioGroup.setVisibility(View.GONE);
                holder.checkBoxes.setVisibility(View.VISIBLE);
                holder.check1.setText(item.get(1));
                holder.check2.setText(item.get(2));
                holder.check3.setText(item.get(3));
                holder.check4.setText(item.get(4));
                holder.check1.setOnCheckedChangeListener((compoundButton, b) -> {
                    if(compoundButton.isChecked()){

                        onOptionCheckedListener.onOptionCheck(1,position);

                    }
                    else{
                        onOptionCheckedListener.onOptionCheck(-1,position);
                    }
                });
                holder.check2.setOnCheckedChangeListener((compoundButton, b) -> {
                    if(compoundButton.isChecked()){

                        onOptionCheckedListener.onOptionCheck(2,position);

                    }
                    else{
                        onOptionCheckedListener.onOptionCheck(-2,position);
                    }
                });
                holder.check3.setOnCheckedChangeListener((compoundButton, b) -> {
                    if(compoundButton.isChecked()){

                        onOptionCheckedListener.onOptionCheck(3,position);

                    }
                    else{
                        onOptionCheckedListener.onOptionCheck(-3,position);
                    }
                });
                holder.check4.setOnCheckedChangeListener((compoundButton, b) -> {
                    if(compoundButton.isChecked()){

                        onOptionCheckedListener.onOptionCheck(4,position);

                    }
                    else{
                        onOptionCheckedListener.onOptionCheck(-4,position);
                    }
                });
            }
        }



    }
    @Override
    public int getItemCount() {

        return list.size();
    }

    public class testViewAdapter extends RecyclerView.ViewHolder {
        public RadioButton option1,option2,option3,option4;
        public CheckBox check1,check2,check3,check4;
private TextView question,qno;
private RadioGroup radioGroup;
private LinearLayout checkBoxes;
private Spinner typeSpinner;

        public testViewAdapter(@NonNull @NotNull View itemView) {

            super(itemView);

            qno=itemView.findViewById(R.id.qno);

            checkBoxes=itemView.findViewById(R.id.checkBoxes);
            radioGroup =  itemView.findViewById(R.id.radioGroup1);
            typeSpinner=itemView.findViewById(R.id.testTypeSpinner);
            question=itemView.findViewById(R.id.quest);
            option1=itemView.findViewById(R.id.op1);
            option2=itemView.findViewById(R.id.op2);
            option3=itemView.findViewById(R.id.op3);
            option4=itemView.findViewById(R.id.op4);
            check1=itemView.findViewById(R.id.ch1);
            check2=itemView.findViewById(R.id.ch2);
            check3=itemView.findViewById(R.id.ch3);
            check4=itemView.findViewById(R.id.ch4);
            if(isTeacher)
                check1.setChecked(true);
            if(!isTeacher)
                option1.setChecked(false);


        }
    }
}
