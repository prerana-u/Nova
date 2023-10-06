package com.example.project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SelectWords extends AppCompatActivity {

    Button greeting,manners,fruits,drinks,objects;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_words);

        greeting=(Button)findViewById(R.id.greetings);
        manners=(Button)findViewById(R.id.manners);
        drinks=(Button)findViewById(R.id.drinks);
        fruits=(Button)findViewById(R.id.fruits);
        objects=(Button)findViewById(R.id.objects);

        greeting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(SelectWords.this,Speechtotext.class);
                i.putExtra("Category","0");
                startActivity(i);
            }
        });
        manners.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(SelectWords.this,Speechtotext.class);
                i.putExtra("Category","1");
                startActivity(i);
            }
        });
        objects.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(SelectWords.this,Speechtotext.class);
                i.putExtra("Category","2");
                startActivity(i);
            }
        });
        fruits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(SelectWords.this,Speechtotext.class);
                i.putExtra("Category","3");
                startActivity(i);
            }
        });
        drinks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(SelectWords.this,Speechtotext.class);
                i.putExtra("Category","4");
                startActivity(i);
            }
        });

    }
}