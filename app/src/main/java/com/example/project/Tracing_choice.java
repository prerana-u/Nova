package com.example.project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class Tracing_choice extends AppCompatActivity {

    Button clicked;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracing_choice);

    }

    public void getvalue(View v)
    {
        clicked=(Button)findViewById(v.getId());
        String st=clicked.getText().toString();
        Intent intent = new Intent(Tracing_choice.this, Tracing_alphabets.class);
        intent.putExtra("alphastring",st);
        startActivity(intent);
    }

}