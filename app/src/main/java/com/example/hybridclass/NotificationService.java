package com.example.hybridclass;

import android.app.Service;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

public class NotificationService extends Service {

    Timer timer;
    TimerTask timerTask;
    String TAG = "Timers";
    int Your_X_SECS = 2;


    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e(TAG, "onStartCommand");
        super.onStartCommand(intent, flags, startId);
        startTimer();
        return START_STICKY;
    }


    @Override
    public void onCreate() {
        Log.e(TAG, "onCreate");
    }

    @Override
    public void onDestroy() {
        Log.e(TAG, "onDestroy");
        stoptimertask();
        super.onDestroy();
    }

        final Handler handler = new Handler();


    public void startTimer() {
                timer = new Timer();

                initializeTimerTask();

                timer.schedule(timerTask, 1000, Your_X_SECS * 1000);             }

    public void stoptimertask() {
                if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    public void initializeTimerTask() {

        timerTask = new TimerTask() {
            public void run() {

                                handler.post(new Runnable() {
                    public void run() {

                        Toast toast= Toast.makeText(getApplicationContext(),
                                "Finish or submit your test! Before times runs out.", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER|Gravity.CENTER_VERTICAL, 0, -10);
                        View view = toast.getView();

                                                view.getBackground().setColorFilter(Color.rgb(42,158,235), PorterDuff.Mode.SRC_IN);

                                                TextView text = view.findViewById(android.R.id.message);
                        text.setTextSize(20);
                        text.setTextColor(Color.BLACK);
                        toast.show();
                    }
                });
            }
        };
    }
}