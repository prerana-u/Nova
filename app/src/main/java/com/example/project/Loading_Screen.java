package com.example.project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

public class Loading_Screen extends AppCompatActivity {
    ProgressBar mProgressBar;
    ConstraintLayout l1;
    CountDownTimer mCountDownTimer;
    int i=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading__screen);

        mProgressBar=(ProgressBar)findViewById(R.id.pbar);
        l1=(ConstraintLayout)findViewById(R.id.l1);

        mProgressBar.setProgress(i);
        mProgressBar.getProgressDrawable().setColorFilter(
                Color.RED, android.graphics.PorterDuff.Mode.SRC_IN);
        mCountDownTimer=new CountDownTimer(5000,1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                Log.v("Log_tag", "Tick of Progress"+ i+ millisUntilFinished);
                i++;
                mProgressBar.setProgress((int)i*100/(5000/1000));
            }

            @Override
            public void onFinish() {
                i++;
                mProgressBar.setProgress(100);
                Intent intent = new Intent(Loading_Screen.this, Slidepage.class);//Mainactivity
                startActivity(intent);
            }
        };
        mCountDownTimer.start();

    }
}