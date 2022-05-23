package com.example.healthy.serviece;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.healthy.SportActivity;

public class StepListener extends Service implements SensorEventListener, SteppListener {
    public static float steps=0;
    private StepDetector simpleStepDetector;
    private SensorManager sensorManager;
    private Sensor accel;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sensorManager=(SensorManager) getSystemService(Context.SENSOR_SERVICE);
        Sensor stepCounter=sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);
        accel = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        simpleStepDetector = new StepDetector();
        simpleStepDetector.registerListener(this);
        sensorManager.registerListener(this, accel, SensorManager.SENSOR_DELAY_FASTEST);

        if(stepCounter!=null) {
            final boolean batchMode = sensorManager.registerListener(
                    this, stepCounter, SensorManager.SENSOR_DELAY_FASTEST,2000000);
            if (!batchMode) {
                Toast.makeText(getApplicationContext(),"Could not register batch mode for sensor",Toast.LENGTH_SHORT).show();
            }else{
            }

        } else {
            Toast.makeText(this, "Device not Compatible!", Toast.LENGTH_LONG).show();
            this.stopSelf();
        }

    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            simpleStepDetector.updateAccel(
                    event.timestamp, event.values[0], event.values[1], event.values[2]);
        }
    }

    @Override
    public void step(long timeNs) {
        steps++;
        SportActivity.STEPS=steps;
        SportActivity.STEPPIK++;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_NOT_STICKY;
    }
}
