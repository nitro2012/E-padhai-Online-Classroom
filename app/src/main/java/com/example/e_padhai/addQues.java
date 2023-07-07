package com.example.e_padhai;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class addQues extends AppCompatActivity {
private EditText question,opt1,opt2,opt3,opt4;
private String  quest,option1,option2,option3,option4;
private Button done;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_ques);
        question=findViewById(R.id.question);
        opt1=findViewById(R.id.option1);
        opt2=findViewById(R.id.option2);
        opt3=findViewById(R.id.option3);
        opt4=findViewById(R.id.option4);

        done =findViewById(R.id.addQuestBtn);

        done.setOnClickListener(view -> checkValidation());

    }


    private void checkValidation() {
         quest=question.getText().toString();
           option1=opt1.getText().toString();
         option2=opt2.getText().toString();
         option3=opt3.getText().toString();
         option4=opt4.getText().toString();

        if(quest.isEmpty()){
            question.setError("Empty");
            question.requestFocus();
        }
        else if(option1.isEmpty()){
            opt1.setError("Empty");
            opt1.requestFocus();
        } else if(option2.isEmpty()){
            opt2.setError("Empty");
            opt2.requestFocus();
        } else if(option3.isEmpty()){
            opt3.setError("Empty");
            opt3.requestFocus();
        } else if(option4.isEmpty()){
            opt4.setError("Empty");
            opt4.requestFocus();
        }

        /*else if(bitmap==null){
            insertData();

        }*/
        else
        {
            insertData();
        }
    }

    private void insertData() {


        Intent intent=new Intent();

        intent.putExtra("question",quest);
        intent.putExtra("option1",option1);
        intent.putExtra("option2",option2);
        intent.putExtra("option3",option3);
        intent.putExtra("option4",option4);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }
}