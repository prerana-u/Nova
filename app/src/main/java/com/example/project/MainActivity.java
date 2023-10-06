package com.example.project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.preference.EditTextPreference;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.internal.$Gson$Preconditions;

import java.util.regex.Pattern;

public class MainActivity<mRunnable> extends AppCompatActivity {

    EditText email;
    TextInputLayout pass;
    TextView sn,fp;
    Button b1;

    ConstraintLayout l2;
    FirebaseAuth fAuth;
    private FirebaseAuth.AuthStateListener nAuthSListner;

    private static final Pattern PASS_PAT =
            Pattern.compile("^"+
                    "(?=.*[0-9])"+
                    "(?=.*[a-z])"+
                    "(?=.*[A-Z])"+
                    "(?=.*[@#$%^&+=])"+
                    ".{4,15}" +
                    "$");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        email=(EditText)findViewById(R.id.etemail);
        pass=findViewById(R.id.etpass);
        b1=(Button)findViewById(R.id.button1);
        l2=(ConstraintLayout)findViewById(R.id.l2);
        sn=(TextView)findViewById(R.id.textView1);
        fp=(TextView)findViewById(R.id.textView2);


        fAuth=FirebaseAuth.getInstance();

        nAuthSListner= new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser mFirebaseUser = fAuth.getCurrentUser();
                if( mFirebaseUser != null){
                    //Toast.makeText(MainActivity.this,"Logged In", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MainActivity.this, Home.class);
                    startActivity(intent);
                }
            }
        };



        email.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    String mail=email.getText().toString();
                    if(mail.isEmpty())
                    {
                        email.setError("Empty !!");
                    }
                    else if(!Patterns.EMAIL_ADDRESS.matcher(mail).matches())
                    {
                        email.setError("Incorrect Email ID !");
                        email.setText("");
                    }
                    else {
                        email.setError(null);
                    }
                }
            }
        });

        pass.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    String p=pass.getEditText().getText().toString();

                    if(p.isEmpty())
                    {
                        pass.setError("Empty !!");
                    }
                    else if(!PASS_PAT.matcher(p).matches())
                    {
                        pass.setError("Incorrect Password !");
                        pass.getEditText().setText("");
                    }
                    else
                    {
                        pass.setError(null);
                    }
                }
            }
        });


        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mail=email.getText().toString();
                String p=pass.getEditText().getText().toString();

                if(mail.isEmpty())
                {
                    email.setError("Empty !!");
                }
                else if(!Patterns.EMAIL_ADDRESS.matcher(mail).matches())
                {
                    email.setError("Incorrect Email ID!");
                }
                else if(p.isEmpty())
                {
                    email.setError("Empty !!");
                }
                else if(!PASS_PAT.matcher(p).matches())
                {
                    pass.setError("Incorrect Password !");
                }
                else
                {
                    fAuth.signInWithEmailAndPassword(mail,p).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(MainActivity.this," Login Successful",Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(MainActivity.this, Profile.class);
                                startActivity(intent);
                            }
                            else
                            {
                                Toast.makeText(MainActivity.this,"Sign In Not Successful"+task.getException().getMessage(),Toast.LENGTH_LONG).show();

                            }
                        }
                    });

                }
            }
        });

        sn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, signin.class);
                startActivity(intent);
            }
        });

        fp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText resetmail=new EditText(v.getContext());
                AlertDialog.Builder passresetdialog=new AlertDialog.Builder(v.getContext());
                passresetdialog.setTitle("Reset Password ? ");
                passresetdialog.setMessage("Enter Your Email ");
                passresetdialog.setView(resetmail);


                passresetdialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //send reset mail
                        String mail=resetmail.getText().toString();
                        fAuth.sendPasswordResetEmail(mail).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(MainActivity.this,"Reset Password Link Sent",Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(MainActivity.this,"Error"+e.getMessage(),Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                });

                passresetdialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //close
                    }
                });

                passresetdialog.create().show();
            }
        });

    }

    protected  void onStart() {
        super.onStart();
        fAuth.addAuthStateListener(nAuthSListner);
    }


}