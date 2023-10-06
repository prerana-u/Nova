package com.example.project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class signin extends AppCompatActivity {

    private static final Pattern PASS_PAT =
            Pattern.compile("^"+
                    "(?=.*[0-9])"+
                    "(?=.*[a-z])"+
                    "(?=.*[A-Z])"+
                    "(?=.*[@#$%^&+=])"+
                    ".{4,15}" +
                    "$");

    private static final Pattern phone_PAT = Pattern.compile("^[0-9\\s]*$");

    private static final Pattern name_PAT = Pattern.compile("^[a-zA-Z\\s]*$");


    DatePickerDialog picker;
    EditText eText,name,prof,email;
    RadioButton rb3;
    RadioGroup rg;
    TextView login;
    TextInputLayout pass,cpass;
    private static final String TAG = "MyActivity";
    FirebaseAuth fAuth;
    private FirebaseFirestore fstore;
    Calendar cldr;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        name=(EditText)findViewById(R.id.etname);
        prof=(EditText)findViewById(R.id.etprof);
        email=(EditText)findViewById(R.id.etemail);
        pass=findViewById(R.id.etpass);
        cpass=findViewById(R.id.etcpass);
        rg=(RadioGroup)findViewById(R.id.rg);
        rb3=(RadioButton)findViewById(R.id.rb3);
        login=(TextView)findViewById(R.id.textView1);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(signin.this, MainActivity.class);
                startActivity(intent);
            }
        });

        fAuth = FirebaseAuth.getInstance();
        fstore=FirebaseFirestore.getInstance();
        if(fAuth.getCurrentUser() !=null)
        {
            Intent intent = new Intent(signin.this, Profile.class);
            startActivity(intent);
            finish();
        }

//datepicker
        eText=(EditText) findViewById(R.id.et_dob);
        eText.setInputType(InputType.TYPE_NULL);
        eText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(signin.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                eText.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                                eText.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);

                            }
                        }, year, month, day);
                picker.show();
            }
        });

        eText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus)
                {

                }
            }
        });

        //validation

        name.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    String sname=name.getText().toString();
                    if(sname.trim().equals("")){
                        name.setError("Empty !!");
                    }
                    else if(!name_PAT.matcher(sname).matches())
                    {
                        name.setError("Incorrect Name !");
                    }
                }
            }
        });

        prof.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    String sprof=prof.getText().toString();
                    if(sprof.isEmpty())
                    {
                        prof.setError("Empty !!");
                    }
                    else if(!phone_PAT.matcher(sprof).matches())
                    {
                        prof.setError("Incorrect Phone Number !");
                    }
                    else if(prof.length()!=10){
                        prof.setError("Incorrect Length for Phone Number !");
                    }
                }
            }
        });

        email.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    String semail=email.getText().toString();
                    if(semail.isEmpty())
                    {
                        email.setError("Empty !!");
                    }
                    else if(!Patterns.EMAIL_ADDRESS.matcher(semail).matches())
                    {
                        email.setError("Incorrect Email !");
                    }
                }
            }
        });


    }

    public void validate(View v)
    {
        int err=0;
        final String spass=pass.getEditText().getText().toString();
        String scpass=cpass.getEditText().getText().toString();
        final String semail=email.getText().toString();
        final String sname=name.getText().toString();
        final String sphone=prof.getText().toString();

        if(sname.trim().equals("")){
            name.setError("Empty !!");
            err++;
        }
        else if(!name_PAT.matcher(sname).matches())
        {
            name.setError("Incorrect Name !");
            err++;
        }
        if(rg.getCheckedRadioButtonId()==-1)
        {
            rb3.setError("Empty !!");
        }
        if(sphone.isEmpty())
        {
            prof.setError("Empty !!");
            err++;
        }
        else if(!phone_PAT.matcher(sphone).matches())
        {
            prof.setError("Incorrect Phone Number !");
            err++;
        }
        else if(prof.length()!=10){
            prof.setError("Incorrect Length for Phone Number !");
            err++;
        }

        if(semail.isEmpty())
        {
            email.setError("Empty !!");
            err++;
        }
        else if(!Patterns.EMAIL_ADDRESS.matcher(semail).matches())
        {
            email.setError("Incorrect Email !");
            err++;
        }

        if(spass.isEmpty())
        {
            pass.setError("Empty !!");
            err++;
        }
        else if(!PASS_PAT.matcher(spass).matches())
        {
            pass.setError("Password must contain 1 uppercase and a lower case letter, a digit and a special char. !");
            err++;
        }
        if(scpass.isEmpty())
        {
            cpass.setError("Empty !!");
            err++;
        }
        else if(!PASS_PAT.matcher(spass).matches())
        {
            cpass.setError("Password must contain 1 uppercase and a lower case letter, a digit and a special char. !");
            err++;
        }
        else if(!spass.equals(scpass))
        {
            cpass.setError("Password don't match !");
            err++;
        }
        //final check
        if(err>0)
        {
            Toast.makeText(signin.this,"Please fill in all details correctly",Toast.LENGTH_SHORT).show();
        }
        else
        {
            fAuth.createUserWithEmailAndPassword(semail,spass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        //send verification mail
                        FirebaseUser fuser=fAuth.getCurrentUser();
                        fuser.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(signin.this,"Verification Mail Sent",Toast.LENGTH_LONG).show();
                            }
                        });

                        int selectedId = rg.getCheckedRadioButtonId();
                        RadioButton rb = (RadioButton) findViewById(selectedId);
                        String gen=rb.getText().toString();

                        //add data in the cloud
                        String UserID =fAuth.getCurrentUser().getUid();
                        DocumentReference docref=fstore.collection("User").document(UserID);
                        Map<String,Object> data =new HashMap<>();
                        data.put("Email",semail);
                        data.put("Password",spass);
                        data.put("Name",sname);
                        data.put("Phone",sphone);
                        data.put("Age",5);
                        data.put("Level",1);
                        data.put("Gender",gen);
                        docref.set(data).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d(TAG,"Doc saved");
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG,"Not saved");
                            }
                        });
                    Toast.makeText(signin.this,"Sign In Successful",Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(signin.this, After_Signup.class);
                        startActivity(intent);
                    }
                    else
                    {
                        Toast.makeText(signin.this,"Sign In Not Successful"+task.getException().getMessage(),Toast.LENGTH_LONG).show();
                    }
                }
            });
        }

    }

}