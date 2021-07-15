package com.example.robiul.familylocator;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Show_id extends AppCompatActivity {

    String code,name,s;
    TextView T;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_id);
        T=(TextView) findViewById(R.id.code_id);
        button=(Button) findViewById(R.id.rb4);
        Intent myintent=getIntent();
        if(myintent!=null)
        {
            code=myintent.getStringExtra("Code");
            name=myintent.getStringExtra("Name");
        }
        s="User id:";
        s=s+code;
        T.setText(s);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NevagitionActivity();

            }
        });


    }

    void  NevagitionActivity()
    {

        Intent myintent=new Intent(Show_id.this,Mynavigation.class);
        myintent.putExtra("Code",code);
        myintent.putExtra("Name",name);
        startActivity(myintent);
        finish();

    }
}
