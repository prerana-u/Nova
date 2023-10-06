package com.example.project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.common.primitives.Ints;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class Number_Tracing extends AppCompatActivity {

    DrawingView dv ;
    private Paint mPaint;
    ConstraintLayout mtile;
    ImageView pic,pic1,img;
    InputImage image,backimage;
    TextRecognizer recognizer;
    Button next,submit,clear,back;
    Bitmap bitmap;
    String Alphabet="",drawnalphabet="",alphastring;
    float areaofdraw,areaofpic;
    int c=0,n;
    String[] array={"1","2","3","4","5","6","7","8","9","0"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_number__tracing);

        img=(ImageView)findViewById(R.id.img12);
        next=(Button)findViewById(R.id.button2);
        submit=(Button)findViewById(R.id.button1);
        clear=(Button)findViewById(R.id.clearb);
        back=(Button)findViewById(R.id.backb);

        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            alphastring = extras.getString("alphastring");
        }
        n= Arrays.asList(array).indexOf(alphastring)+1;

        //start of fetch image
        String link="numbers/"+"n"+alphastring+".jpg";
        //Toast.makeText(Number_Tracing.this,String.valueOf(c),Toast.LENGTH_SHORT).show();

        FirebaseStorage storage=FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl("gs://project-c644b.appspot.com").child(link);

        try{
            final File file= File.createTempFile("image","jpeg");
            storageRef.getFile(file).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    bitmap=BitmapFactory.decodeFile(file.getAbsolutePath());
                    img.setImageBitmap(bitmap);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(Number_Tracing.this,"Fail",Toast.LENGTH_SHORT).show();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        // end of fetching image


        mtile=(ConstraintLayout)findViewById(R.id.mtile);
        pic=(ImageView)findViewById(R.id.img12);
        pic1=(ImageView)findViewById(R.id.im);

        dv = new DrawingView(this);
        mtile.addView(dv);
        RelativeLayout rvtrans=new RelativeLayout(this);
        mtile.addView(rvtrans);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setColor(Color.YELLOW);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(40);


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(Number_Tracing.this,Number_choice.class);
                startActivity(i);
            }
        });

        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dv.clearpath();
                Intent i=new Intent(Number_Tracing.this,Number_Tracing.class);
                i.putExtra("alphastring",array[n-1]);
                startActivity(i);
            }
        });

        final ImageView image = new ImageView(this);
        image.setImageResource(R.drawable.trophy);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Alphabet.compareTo(drawnalphabet)==0)
                {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(Number_Tracing.this);
                    dialog.setMessage("Well Done !! Lets move on ...");
                    dialog.setTitle("Correct ");
                    dialog.setPositiveButton("Next",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    //Next Alphabet
                                    Intent intent = new Intent(Number_Tracing.this, Number_Tracing.class);
                                    intent.putExtra("alphastring",array[n]);
                                    startActivity(intent);
                                }
                            }).setView(image);
                    AlertDialog alertDialog = dialog.create();
                    alertDialog.show();
                    Button buttonbackground = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
                    buttonbackground.setBackgroundColor(Color.argb(100,9,74,84));
                    buttonbackground.setTextColor(Color.WHITE);
                }
                else
                {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(Number_Tracing.this);
                    dialog.setMessage("Incorrect Number !! Try Again ...");
                    dialog.setTitle("Incorrect ");
                    dialog.setPositiveButton("Ok",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    //Nothing here
                                }
                            });
                    AlertDialog alertDialog = dialog.create();
                    alertDialog.show();
                    Button buttonbackground = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
                    buttonbackground.setBackgroundColor(Color.argb(100,4,114,129));
                    buttonbackground.setTextColor(Color.WHITE);
                }
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Number_Tracing.this, Number_Tracing.class);
                intent.putExtra("alphastring",array[n]);
                startActivity(intent);
            }
        });
    }
    public void takeScreenShot1(View view) {
        if(c==1) {
            mtile.setBackgroundResource(0);
            next.setVisibility(View.INVISIBLE);
            submit.setVisibility(View.INVISIBLE);
            view.setDrawingCacheEnabled(true);
            view.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_LOW);
            view.buildDrawingCache();
            Bitmap snapshot = Bitmap.createBitmap(view.getDrawingCache());
            view.setDrawingCacheEnabled(false);
            view.destroyDrawingCache();
            pic.setImageBitmap(snapshot);
            mtile.setBackgroundColor(Color.WHITE);
            next.setVisibility(View.VISIBLE);
            submit.setVisibility(View.VISIBLE);
            img.setImageBitmap(bitmap);
            image = InputImage.fromBitmap(snapshot, 0);
            recognizeText1(image);
        }
    }

    public void takeScreenShot(View view) {
        mtile.setBackgroundResource(0);
        next.setVisibility(View.INVISIBLE);
        submit.setVisibility(View.INVISIBLE);
        img.setImageResource(0);
        pic.setImageResource(0);
        view.setDrawingCacheEnabled(true);
        view.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_LOW);
        view.buildDrawingCache();
        Bitmap snapshot = Bitmap.createBitmap(view.getDrawingCache());
        view.setDrawingCacheEnabled(false);
        view.destroyDrawingCache();
        pic.setImageBitmap(snapshot);
        mtile.setBackgroundColor(Color.WHITE);
        next.setVisibility(View.VISIBLE);
        submit.setVisibility(View.VISIBLE);
        img.setImageBitmap(bitmap);
        image = InputImage.fromBitmap(snapshot, 0);
        recognizeText(image);

    }

    public class DrawingView extends View {

        public int width;
        public  int height;
        private Bitmap  mBitmap;
        private Canvas mCanvas;
        private Path mPath;
        private Paint   mBitmapPaint;
        Context context;
        private Paint circlePaint;
        private Path circlePath;

        public DrawingView(Context c) {
            super(c);
            context=c;
            mPath = new Path();
            mBitmapPaint = new Paint(Paint.DITHER_FLAG);
            circlePaint = new Paint();
            circlePath = new Path();
            circlePaint.setAntiAlias(true);
            circlePaint.setColor(Color.BLUE);
            circlePaint.setStyle(Paint.Style.STROKE);
            circlePaint.setStrokeJoin(Paint.Join.MITER);
            circlePaint.setStrokeWidth(1f);
        }

        public void clearpath()
        {
            mPath = new Path();
            Paint clearPaint = new Paint();
            clearPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
            mCanvas.drawRect(0, 0, 0, 0, clearPaint); }

        @Override
        protected void onSizeChanged(int w, int h, int oldw, int oldh) {
            super.onSizeChanged(w, h, oldw, oldh);
            mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
            mCanvas = new Canvas(mBitmap);
        }
        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            canvas.drawBitmap( mBitmap, 0, 0, mBitmapPaint);
            canvas.drawPath( mPath,  mPaint);
            canvas.drawPath( circlePath,  circlePaint);
        }

        private float mX, mY;
        private static final float TOUCH_TOLERANCE = 2;

        private void touch_start(float x, float y) {
            c++;
            mPath.reset();
            mPath.moveTo(x, y);
            mX = x;
            mY = y;
            Log.d("start xy==>", x+","+y);
            takeScreenShot1(mtile);
        }
        private void touch_move(float x, float y) {
            Log.d("move xy==>", x+","+y);
            float dx = Math.abs(x - mX);
            float dy = Math.abs(y - mY);
            if ((dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE)) {
                mPath.quadTo(mX, mY, (x + mX)/2, (y + mY)/2);
                mX = x;
                mY = y;
            }
        }
        private void touch_up() {
            mPath.lineTo(mX, mY);
            Log.d("end xy", mX+","+mY);
            circlePath.reset();
            mCanvas.drawPath(mPath,  mPaint);
            mPath.reset();
            //put it in submit and add clear button to remove lines
            takeScreenShot(mtile);
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            float x = event.getX();
            float y = event.getY();
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    touch_start(x, y);
                    invalidate();
                    break;
                case MotionEvent.ACTION_MOVE:
                    touch_move(x, y);
                    invalidate();
                    break;
                case MotionEvent.ACTION_UP:
                    touch_up();
                    invalidate();
                    break;
            }
            return true;
        }
    }

    private void recognizeText1(InputImage image) {
        Alphabet=array[n-1];
        Toast.makeText(Number_Tracing.this,"back "+Alphabet,Toast.LENGTH_SHORT).show();

        TextRecognizer recognizer = TextRecognition.getClient();
        Task<Text> result = recognizer.process(image)
                .addOnSuccessListener(new OnSuccessListener<Text>() {
                    @Override
                    public void onSuccess(Text visionText) {
                        for (Text.TextBlock block : visionText.getTextBlocks()) {
                            Rect boundingBox = block.getBoundingBox();
                            Point[] cornerPoints = block.getCornerPoints();
                            String text = block.getText();
                            Rect blockFrame = block.getBoundingBox();
                            String s=blockFrame.flattenToString();
                            String[] sides=s.split(" ");
                            float a1=Float.parseFloat(sides[3])-Float.parseFloat(sides[0]);
                            float a2=Float.parseFloat(sides[1])-Float.parseFloat(sides[0]);
                            areaofdraw=a1*a2;

                            //Toast.makeText(Tracing_alphabets.this,String.valueOf(areaofdraw),Toast.LENGTH_SHORT).show();

                            //Toast.makeText(Tracing_alphabets.this,text,Toast.LENGTH_SHORT).show();


                            for (Text.Line line: block.getLines()) {
                                // ...
                                for (Text.Element element: line.getElements()) {
                                    // ...
                                }
                            }
                        }
                    }
                })
                .addOnFailureListener(
                        new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // Task failed with an exception
                                Toast.makeText(Number_Tracing.this,"lol",Toast.LENGTH_SHORT).show();

                                // ...
                            }
                        });
    }

    private void recognizeText(InputImage image) {
        TextRecognizer recognizer = TextRecognition.getClient();
        Task<Text> result = recognizer.process(image)
                .addOnSuccessListener(new OnSuccessListener<Text>() {
                    @Override
                    public void onSuccess(Text visionText) {
                        for (Text.TextBlock block : visionText.getTextBlocks()) {
                            Rect boundingBox = block.getBoundingBox();
                            Point[] cornerPoints = block.getCornerPoints();
                            String text = block.getText();
                            Rect blockFrame = block.getBoundingBox();
                            String s=blockFrame.flattenToString();
                            String[] sides=s.split(" ");

                            // for back pic
                            float a11=Float.parseFloat(sides[3])-Float.parseFloat(sides[0]);
                            float a22=Float.parseFloat(sides[1])-Float.parseFloat(sides[0]);
                            areaofpic=a11*a22;
                            drawnalphabet=text;
                            Toast.makeText(Number_Tracing.this,"drawn "+drawnalphabet,Toast.LENGTH_SHORT).show();

                            //compare areas and find diff of percentage

                            //Toast.makeText(Tracing_alphabets.this,String.valueOf(areaofpic),Toast.LENGTH_SHORT).show();


                        }
                    }
                });
    }

    private void processTextBlock(Text result) {
        // [START mlkit_process_text_block]
        String resultText = result.getText();
        for (Text.TextBlock block : result.getTextBlocks()) {
            String blockText = block.getText();
            Point[] blockCornerPoints = block.getCornerPoints();
            Rect blockFrame = block.getBoundingBox();
            for (Text.Line line : block.getLines()) {
                String lineText = line.getText();
                Point[] lineCornerPoints = line.getCornerPoints();
                Rect lineFrame = line.getBoundingBox();
                for (Text.Element element : line.getElements()) {
                    String elementText = element.getText();
                    Point[] elementCornerPoints = element.getCornerPoints();
                    Rect elementFrame = element.getBoundingBox();
                }
            }
        }
    }

    private TextRecognizer getTextRecognizer() {
        TextRecognizer detector = TextRecognition.getClient();
        return detector;
    }

}
