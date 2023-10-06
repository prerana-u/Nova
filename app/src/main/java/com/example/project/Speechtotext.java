package com.example.project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

public class Speechtotext extends AppCompatActivity {

    public static final Integer RecordAudioRequestCode = 1;
    private SpeechRecognizer speechRecognizer;
    private EditText editText;
    private Button micButton,next;
    TextView head;
    MediaPlayer mp;
    private ImageButton image;

    String text[]={"GREETINGS","MANNERS","OBJECTS","FRUITS","DRINKS"};

    int audioid[][]={
            {R.raw.hello,R.raw.goodmorning,R.raw.goodafternoon,R.raw.goodnight,R.raw.bye},
            {R.raw.please,R.raw.thankyou,R.raw.sorry,R.raw.welcome,R.raw.excuseme},
            {R.raw.table,R.raw.chair,R.raw.bed,R.raw.lamp,R.raw.book},
            {R.raw.banana,R.raw.orange,R.raw.grapes,R.raw.apple,R.raw.mango},
            {R.raw.coffee,R.raw.tea,R.raw.milk,R.raw.juice,R.raw.water},
    };

    String[][] arrayimage={
            {"hello","goodmorning","goodafternoon","goodnight","bye"},
            {"please","thankyou","sorry","welcome","excuseme"},
            {"table","chair","bed","lamp","book"},
            {"banana","orange","grape","apple","mango"},
            {"coffee","tea","milk","juice","water"},
    };

    String[][] message={
            {"Hello","Good Morning","Good Afternoon","Good Night","Bye"},
            {"Please","Thank You","Sorry","Welcome","Excuse Me"},
            {"Table","Chair","Bed","Lamp","Book"},
            {"Banana","Orange","Grape","Apple","Pear"},
            {"Coffee","Tea","Milk","Juice","Water"},
    };

    int index=0;
    int category=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speechtotext);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            checkPermission();
        }

        editText = findViewById(R.id.text);
        micButton = findViewById(R.id.speak);
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        image = (ImageButton) findViewById(R.id.image);
        next = (Button) findViewById(R.id.next);
        head=(TextView)findViewById(R.id.heading);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            category = Integer.parseInt(extras.getString("Category"));
        }

        head.setText(text[category]);
        String link = "words/" + arrayimage[category][index] + ".jpg";
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl("gs://project-c644b.appspot.com/").child(link);
        try {
            final File file = File.createTempFile("image", "jpg");
           // Toast.makeText(Speechtotext.this, "ok", Toast.LENGTH_SHORT).show();
            storageRef.getFile(file).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                    image.setImageBitmap(bitmap);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    e.printStackTrace();
                    Toast.makeText(Speechtotext.this, "Fail" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mp=MediaPlayer.create(Speechtotext.this,audioid[category][index]);
                mp.start();
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mp.stop();
            }
        });

        final Intent speechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());

        speechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle bundle) {

            }

            @Override
            public void onBeginningOfSpeech() {
                editText.setText("");
                editText.setHint("Listening...");
            }

            @Override
            public void onRmsChanged(float v) {

            }

            @Override
            public void onBufferReceived(byte[] bytes) {

            }

            @Override
            public void onEndOfSpeech() {

            }

            @Override
            public void onError(int i) {

            }

            @Override
            public void onResults(Bundle bundle) {
               // micButton.setImageResource(R.drawable.ic_baseline_mic_24);
                ArrayList<String> data = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                editText.setText(data.get(0));
                if(editText.getText().toString().equalsIgnoreCase(message[category][index]))
                {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(Speechtotext.this);
                    dialog.setMessage("Well Done !! Lets move on ...");
                    dialog.setTitle("Correct ");
                    dialog.setPositiveButton("Next",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    index=index+1;
                                    if(index<5) {
                                        String link="words/"+arrayimage[category][index]+".jpg";
                                        FirebaseStorage storage=FirebaseStorage.getInstance();
                                        StorageReference storageRef = storage.getReferenceFromUrl("gs://project-c644b.appspot.com/").child(link);
                                        try{
                                            final File file= File.createTempFile("image","jpg");
                                            storageRef.getFile(file).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                                @Override
                                                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                                    Bitmap bitmap= BitmapFactory.decodeFile(file.getAbsolutePath());
                                                    image.setImageBitmap(bitmap);
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    e.printStackTrace();
                                                    Toast.makeText(Speechtotext.this,"Fail"+e.getMessage(),Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                        editText.setText("");
                                    }
                                    else {
                                        Toast.makeText(Speechtotext.this,"Completed",Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(Speechtotext.this, SelectWords.class);
                                        startActivity(intent);
                                    }
                                }
                            });
                    AlertDialog alertDialog = dialog.create();
                    alertDialog.show();
                }
                else
                {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(Speechtotext.this);
                    dialog.setMessage("Incorrect Word !! Try Again ...");
                    dialog.setTitle("Incorrect ");
                    dialog.setPositiveButton("Ok",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    //Nothing here
                                }
                            });
                    AlertDialog alertDialog = dialog.create();
                    alertDialog.show();
                }
            }

            @Override
            public void onPartialResults(Bundle bundle) {

            }

            @Override
            public void onEvent(int i, Bundle bundle) {

            }
        });

        micButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_UP){
                    speechRecognizer.stopListening();
                }
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN){
                    //micButton.setImageResource(R.drawable.ic_baseline_mic_off_24);
                    speechRecognizer.startListening(speechRecognizerIntent);
                }
                return false;
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        speechRecognizer.destroy();
    }

    private void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.RECORD_AUDIO},RecordAudioRequestCode);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == RecordAudioRequestCode && grantResults.length > 0 ){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED)
                Toast.makeText(this,"Permission Granted",Toast.LENGTH_SHORT).show();
        }
    }
}