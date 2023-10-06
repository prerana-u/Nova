package com.example.project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.ui.AppBarConfiguration;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import javax.annotation.Nullable;

import de.hdodenhof.circleimageview.CircleImageView;

public class Profile_page extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    Button  save;
    CircleImageView ppic;
    ImageButton edit;
    TextView email;
    EditText name, phone;
    FirebaseAuth fAuth;
    Spinner spinner;
    private FirebaseFirestore fstore;
    DocumentReference docref;
    int levelno;


    private Toolbar toolbar;
    DrawerLayout drawer;
    NavigationView navigationView;
    private AppBarConfiguration mAppBarConfiguration;

    private static final Pattern phone_PAT = Pattern.compile("^[0-9\\s]*$");
    private static final Pattern name_PAT = Pattern.compile("^[a-zA-Z\\s]*$");


    //add fetch from database to the page

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);

        ppic=(CircleImageView)findViewById(R.id.profile_image);
        edit=(ImageButton)findViewById(R.id.bedit);
        save=(Button)findViewById(R.id.bsave);
        email=(TextView)findViewById(R.id.etemail);
        name=(EditText)findViewById(R.id.etname);
        phone=(EditText)findViewById(R.id.etphone);

        fAuth = FirebaseAuth.getInstance();
        fstore=FirebaseFirestore.getInstance();
        String UserID =fAuth.getCurrentUser().getUid();
        docref=fstore.collection("User").document(UserID);


        spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(this);
        List<String> categories = new ArrayList<String>();
        categories.add("Level : 1");
        categories.add("Level : 2");
        categories.add("Level : 3");
        ArrayAdapter dataAdapter = new ArrayAdapter(this, R.layout.level_list,  categories);
        dataAdapter.setDropDownViewResource(R.layout.level_list);
        spinner.setAdapter(dataAdapter);


        docref.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
               email.setText(documentSnapshot.getString("Email"));
               name.setText(documentSnapshot.getString("Name"));
               phone.setText(documentSnapshot.getString("Phone"));
               Long l=documentSnapshot.getLong("Level");
               //Toast.makeText(Profile_page.this,String.valueOf(l),Toast.LENGTH_SHORT).show();
                if(String.valueOf(l)=="1"){
                    spinner.setSelected(true);
                }
                else if(String.valueOf(l)=="2"){
                    spinner.setSelection(2);
                    spinner.setSelected(true);
                }
                else if(String.valueOf(l)=="3"){
                    spinner.setSelection(2);
                }
            }
        });
        spinner.setEnabled(false);

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name.setEnabled(true);
                phone.setEnabled(true);
                spinner.setEnabled(true);
                save.setVisibility(View.VISIBLE);
                phone.setInputType(InputType.TYPE_CLASS_NUMBER);
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String n = name.getText().toString();
                String sp = phone.getText().toString();

                if (sp.isEmpty()) {
                    phone.setError("Empty !!");
                } else if (!phone_PAT.matcher(sp).matches()) {
                    phone.setError("Incorrect Phone Number !");
                } else if (phone.length() != 10) {
                    phone.setError("Incorrect Length for Phone Number !");
                } else if (n.isEmpty()) {
                    name.setError("Empty !!");
                } else if (!name_PAT.matcher(n).matches()) {
                name.setError("Incorrect Name !!");
                } else {
                    //level
                    docref.update("Level", levelno)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                }
                            });

                    //name
                    docref.update("Name", n)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                }
                            });
                    //phone
                    docref.update("Phone", sp)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                        }
                    });

                    Toast.makeText(Profile_page.this,"Profile Updated",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Profile_page.this, Profile_page.class);
                    startActivity(intent);//fetched from database
                }
            }
        });

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setBackgroundColor(Color.parseColor("#80000000"));
        setSupportActionBar(toolbar);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                int id=item.getItemId();
                switch (id){

                    case R.id.nav_home:
                        //Toast.makeText(Profile_page.this,"lol",Toast.LENGTH_SHORT).show();

                        Intent h= new Intent(Profile_page.this,Home.class);
                        startActivity(h);
                        break;

                    case R.id.nav_achv:
                        //Toast.makeText(Profile_page.this,"lol",Toast.LENGTH_SHORT).show();
                        Intent h1= new Intent(Profile_page.this,Achivement.class);
                        startActivity(h1);
                        break;

                    case R.id.nav_level3:
                        Intent h3= new Intent(Profile_page.this,Level3.class);
                        startActivity(h3);
                        break;

                    case R.id.nav_profile:
                        Intent h2= new Intent(Profile_page.this,Profile_page.class);
                        startActivity(h2);
                        break;

                    case R.id.nav_logout:
                        FirebaseAuth.getInstance().signOut();
                        Intent intent = new Intent(Profile_page.this, MainActivity.class);
                        startActivity(intent);
                        break;
                }
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String level = parent.getItemAtPosition(position).toString();
        if(level=="Level : 1")
            levelno=1;
        else if(level=="Level : 2")
            levelno=2;
        else if(level=="Level : 3")
            levelno=3;
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent){
        //be empty
    }
}