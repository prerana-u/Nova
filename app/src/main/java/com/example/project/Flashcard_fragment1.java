package com.example.project;

import android.annotation.SuppressLint;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.fragment.app.Fragment;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentPagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class Flashcard_fragment1 extends Fragment {

    Button next,play;
    MediaPlayer mp;

    public Flashcard_fragment1() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_flashcard_fragment1, container, false);
    }

    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        next=(Button)view.findViewById(R.id.nextb);
        play=(Button)view.findViewById(R.id.playb);
        mp=MediaPlayer.create(getActivity(),R.raw.pig_sound);


        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextCard(new Flashcard_fragment2());
            }
        });

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mp.start();
            }
        });
    }


    public void nextCard(Fragment newcard)
    {
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.anim.card_flip_right_in,
                        R.anim.card_flip_right_out,
                        R.anim.card_flip_left_in,
                        R.anim.card_flip_left_out)
                .replace(R.id.fc, newcard)
                .commit();
    }
}