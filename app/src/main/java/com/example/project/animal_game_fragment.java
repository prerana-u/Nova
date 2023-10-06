package com.example.project;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Random;

public class animal_game_fragment extends Fragment {

    Button c1, c2, c3, c4, cb;
    ImageButton img;
    MediaPlayer mp;

    int ch[] = {0, 1, 2, 3, 4, 5};
    String[] animals = {"COW", "PIG", "DOG", "HORSE", "HEN", "CAT"};
    int[] images = {R.drawable.cowgame, R.drawable.piggame, R.drawable.doggame, R.drawable.horsegame, R.drawable.hengame, R.drawable.catgame,};
    int[] audio = {R.raw.cowsound, R.raw.pig_sound, R.raw.dog_sound, R.raw.horse_sound, R.raw.hen_sound, R.raw.cat_sound,};

    int choice;
    Button[] bchoice;
    int[] selected = new int[4];

    public animal_game_fragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_animal_game_fragment, container, false);
    }

    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        img = (ImageButton) view.findViewById(R.id.blurim);
        c1 = (Button) view.findViewById(R.id.ch1);
        c2 = (Button) view.findViewById(R.id.ch2);
        c3 = (Button) view.findViewById(R.id.ch3);
        c4 = (Button) view.findViewById(R.id.ch4);
        bchoice = new Button[]{c1, c2, c3, c4};

        choice = ch[new Random().nextInt(ch.length)];
        img.setImageResource(images[choice]);
        mp= MediaPlayer.create(getActivity(),audio[choice]);
        mp.start();
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mp.start();
            }
        });

        cb = bchoice[new Random().nextInt(bchoice.length)];
        cb.setText(animals[choice]);

        selected[0] = choice;
        alloptions();
        makebuttons();

        c1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cb!=c1)
                {
                    Animation animShake = AnimationUtils.loadAnimation(getActivity(), R.anim.shake);
                    c1.startAnimation(animShake);
                }
                else{
                    correctans();
                }
            }
        });

        c2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cb!=c2)
                {
                    Animation animShake = AnimationUtils.loadAnimation(getActivity(), R.anim.shake);
                    c2.startAnimation(animShake);
                }
                else{
                    correctans();
                }
            }
        });

        c3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cb!=c3)
                {
                    Animation animShake = AnimationUtils.loadAnimation(getActivity(), R.anim.shake);
                    c3.startAnimation(animShake);
                }
                else{
                    correctans();
                }
            }
        });

        c4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cb!=c4)
                {
                    Animation animShake = AnimationUtils.loadAnimation(getActivity(), R.anim.shake);
                    c4.startAnimation(animShake);
                }
                else{
                    correctans();
                }
            }
        });
    }

    private void alloptions() {
        int r, no = 1, c = 0;
        while (no < 4) {
            r = ch[new Random().nextInt(ch.length)];
            c = 0;
            for (int k = 0; k < no; k++) {
                if (r == selected[k]) {
                    c++;
                }
            }
            if (c == 0) {
                selected[no] = r;
                no++;
            }
        }
    }

    private void makebuttons() {
        int i=1;

     for(int j=0;j<4;j++)
     {
         if(bchoice[j]!=cb)
         {
             bchoice[j].setText(animals[selected[i]]);
             i++;
         }
     }
    }

    public void correctans()
    {
        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        dialog.setMessage("Well Done !! Lets move on ...");
        dialog.setTitle("Correct ");
        dialog.setPositiveButton("Next",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent i=new Intent(getActivity(),Animal_sound_game.class);
                        startActivity(i);
                    }
                });
        AlertDialog alertDialog = dialog.create();
        alertDialog.show();
    }
}
