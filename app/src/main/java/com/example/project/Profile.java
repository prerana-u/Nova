package com.example.project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class Profile extends AppCompatActivity {
Button btLogout,bnotver,referesh;
TextView notver;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userID;
    private  FirebaseAuth.AuthStateListener mAuthSListner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        btLogout=findViewById(R.id.logout);
        bnotver=findViewById(R.id.bnotver);
        notver=findViewById(R.id.tbnotver);
        referesh=findViewById(R.id.refresh);


        fAuth=FirebaseAuth.getInstance();
        fStore=FirebaseFirestore.getInstance();

        userID=fAuth.getCurrentUser().getUid();
        final FirebaseUser user=fAuth.getCurrentUser();

        if(!user.isEmailVerified()){
            bnotver.setVisibility(View.VISIBLE);
            notver.setVisibility(View.VISIBLE);
            bnotver.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    user.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(Profile.this,"Verification Mail Sent",Toast.LENGTH_LONG).show();
                        }
                    });
                }
            });
        }
        else {
            bnotver.setVisibility(View.INVISIBLE);
            notver.setVisibility(View.INVISIBLE);
        }
        if(user.isEmailVerified()) {
            Intent intent = new Intent(Profile.this, Home.class);
            startActivity(intent);
        }
        btLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(Profile.this, MainActivity.class);
                startActivity(intent);
            }
        });

        referesh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Profile.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}