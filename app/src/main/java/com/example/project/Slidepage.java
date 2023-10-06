package com.example.project;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Slidepage extends AppCompatActivity {

    ViewPager viewPager;
    TextView[] mDots;
    LinearLayout dotlayout;


    private SliderAdapter sliderAdapter;
    FirebaseAuth fAuth;
    private FirebaseAuth.AuthStateListener nAuthSListner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slidepage);
        viewPager = findViewById(R.id.viewPager);
        dotlayout=(LinearLayout)findViewById(R.id.dots);

            TextView t1=(TextView)findViewById(R.id.skip);
            t1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i=new Intent(Slidepage.this,MainActivity.class);
                    startActivity(i);
                }
            });

        fAuth=FirebaseAuth.getInstance();
        nAuthSListner= new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser mFirebaseUser = fAuth.getCurrentUser();
                if( mFirebaseUser != null){
                    //Toast.makeText(MainActivity.this,"Logged In", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Slidepage.this, Home.class);
                    startActivity(intent);
                }
            }
        };
        SliderAdapter adapter = new SliderAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        dotsIndicator(0);
        viewPager.addOnPageChangeListener(viewListner);


    }

    public void dotsIndicator(int position){
        mDots=new TextView[4];
        dotlayout.removeAllViews();
        for(int i=0;i<mDots.length;i++){
            mDots[i]=new TextView(this);
            mDots[i].setText(Html.fromHtml("&#8226;"));
            mDots[i].setTextSize(35);
            mDots[i].setTextColor(getResources().getColor(R.color.colorAccent));
            dotlayout.addView(mDots[i]);
        }

        if(mDots.length>0){
            mDots[position].setTextColor(getResources().getColor(R.color.colorPrimaryDark));
        }
    }

    ViewPager.OnPageChangeListener viewListner=new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {

            dotsIndicator(position);

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };
}