package com.example.project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Number_choice extends AppCompatActivity {

    Button clicked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_number_choice);
    }


    public void getvalue(View v)
    {
        clicked=(Button)findViewById(v.getId());
        String st=clicked.getText().toString();
        Intent intent = new Intent(Number_choice.this, Number_Tracing.class);
        intent.putExtra("alphastring",st);
        startActivity(intent);
    }
}