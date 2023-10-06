package com.example.project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentTransaction;
import androidx.fragment.app.Fragment;

import android.app.FragmentManager;
import android.os.Bundle;
import android.view.View;

public class Flashcard extends AppCompatActivity {

    CardView cv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flashcard);
        cv = (CardView) findViewById(R.id.flashcard);
        loadFlashcard(new Flashcard_fragment());
    }

    private void loadFlashcard(Fragment fragment) {
        cv.setVisibility(View.VISIBLE);
        getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.anim.card_flip_right_in,
                        R.anim.card_flip_right_out,
                        R.anim.card_flip_left_in,
                        R.anim.card_flip_left_out)
                .replace(R.id.fc, fragment)
                .commit(); // save the changes
    }
}