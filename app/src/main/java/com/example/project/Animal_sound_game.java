package com.example.project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Animal_sound_game extends AppCompatActivity {

    CardView cv;
    Button back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animal_sound_game);
        cv = (CardView) findViewById(R.id.gamecard);
        back=(Button)findViewById(R.id.backb);
        loadFlashcard(new animal_game_fragment());

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(Animal_sound_game.this,Achivement.class);
                startActivity(i);
            }
        });

    }

    private void loadFlashcard(Fragment fragment) {
        cv.setVisibility(View.VISIBLE);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.gamecard, fragment)
                .commit(); // save the changes
    }
}