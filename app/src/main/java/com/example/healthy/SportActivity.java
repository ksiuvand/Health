package com.example.healthy;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.healthy.serviece.StepListener;
import com.example.healthy.data.MyDatabase;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class SportActivity extends AppCompatActivity {

    private static final int SENSOR_CODE=567;
    public static float STEPS=0;
    public static float STEPPIK;
    private int[] upperBounds={50,100,200,250,500,1000,2000,2500,3000,5000,10000};

    ListView listView;
    TextView stepsView;
    TextView distanceInMetres;
    TextView caloriesBurnt;
    ProgressBar pBar;

    MyDatabase db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sport);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.sport);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.eat:
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.sport:
                        return true;
                    case R.id.water:
                        startActivity(new Intent(getApplicationContext(), WaterActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.profile:
                        startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });

        if(ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACTIVITY_RECOGNITION) == PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACTIVITY_RECOGNITION}, SENSOR_CODE);
        }

        stepsView=findViewById(R.id.stepsView);
        distanceInMetres=findViewById(R.id.distance);
        caloriesBurnt=findViewById(R.id.calories);
        pBar=findViewById(R.id.progress);

        db=new MyDatabase(this);


        Intent StepsIntent = new Intent(getApplicationContext(), StepListener.class);
        startService(StepsIntent);

        update();

        write();
    }

    public void update(){
        final Handler handler=new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                updateValues();

                //set steps to 0 at start of the day
                SimpleDateFormat sdf=new SimpleDateFormat("HH:mm:ss");
                sdf.setTimeZone(TimeZone.getTimeZone("IST"));
                String timeNow=sdf.format(new Date());
                if(timeNow.equals("00:00:00")||timeNow.equals("00:00:01")||timeNow.equals("00:00:02")){
                    STEPS=0;
                    STEPPIK=0;
                    pBar.setProgress(0);
                }

                handler.postDelayed(this,1000);
            }
        });

    }
    public void write(){
        final Handler writeHandler=new Handler();
        writeHandler.post(new Runnable() {
            @Override
            public void run() {
                SimpleDateFormat sdf=new SimpleDateFormat("EEE, MMM d, ''yy");
                sdf.setTimeZone(TimeZone.getTimeZone("IST"));
                String dateNow=sdf.format(new Date());
                //String datenow=new SimpleDateFormat("E,d MM YYYY").format(Calendar.getInstance().getTime());
                db.writeTo(dateNow,(int)(STEPS*0.762),(float)((int)(STEPS*0.762)*0.76));

                writeHandler.postDelayed(this,3600000);
            }
        });
    }
    public void updateValues(){
        int sr = (int) STEPPIK;
        stepsView.setText(sr+"\n STEPS");

        int m= (int) (STEPPIK*0.762);
        distanceInMetres.setText(m+" m");

        float caloriesc= (float) (m*0.76);
        caloriesBurnt.setText(caloriesc+" cal");

        //Set it from array
        for(int i:upperBounds){
            if(STEPS<i){
                pBar.setMax(8000);
                break;
            }
        }
        pBar.setProgress((int) STEPPIK);

    }

    @Override
    protected void onPause() {
        super.onPause();
        write();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
