package com.fatihkonuk.sensorapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.widget.TextView;

public class MotionSensorActivity extends AppCompatActivity implements SensorEventListener {
    private SensorManager sensorManager;
    private Sensor sensor;
    private Vibrator vibrator;
    TextView txt,xAxisTxt,yAxisTxt,zAxisTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_motion_sensor);
        init();

        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this,sensor,SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void init() {
        // Text Views
        txt = findViewById(R.id.mainTextView);
        xAxisTxt = findViewById(R.id.xAxisTextView);
        yAxisTxt = findViewById(R.id.yAxisTextView);
        zAxisTxt = findViewById(R.id.zAxisTextView);
        //System Services
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

    }

    float last_x,last_y,last_z;
    @Override
    public void onSensorChanged(SensorEvent event) {
        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Ringtone sound = RingtoneManager.getRingtone(getApplicationContext(), uri);

        long[] vibPath = {0, 500, 500, 500};
        float[] values = event.values;
        float x = values[0];
        float y = values[1];
        float z = values[2];

        xAxisTxt.setText("X: "+x+"\nLastX: "+last_x);
        yAxisTxt.setText("Y: "+y+"\nLastY: "+last_y);
        zAxisTxt.setText("Z: "+z+"\nLastZ: "+last_z);

        if (x*last_x < 0) {
            txt.setText("X ekseninde döndü");
            getWindow().getDecorView().setBackgroundColor(Color.RED);
            sound.play();
            vibrator.vibrate(vibPath,-1);
        }
        if (y*last_y < 0) {
            txt.setText("Y ekseninde döndü");
            getWindow().getDecorView().setBackgroundColor(Color.GREEN);
            sound.play();
            vibrator.vibrate(vibPath,-1);
        }
        if (z*last_z < 0) {
            txt.setText("Z ekseninde döndü");
            getWindow().getDecorView().setBackgroundColor(Color.BLUE);
            sound.play();
            vibrator.vibrate(vibPath,-1);
        }
        last_x = x;
        last_y = y;
        last_z = z;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        sensorManager.unregisterListener(this);
    }
}