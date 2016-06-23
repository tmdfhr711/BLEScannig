package com.example.ohrok.blescanning;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Region;

import java.util.List;
import java.util.UUID;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener{


    private TextView out;
    private Button turnon;;
    Button list;
    Button turnoff;

    /*
    private BeaconManager mBeaconManager;
    private Region region;
    */


    private static final String TAG = "MainActivity";
    private BeaconManager beaconManager;
    private static final Region ALL_ESTIMOTE_BEACONS = new Region("rid", null, null, null);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        beaconManager = new BeaconManager(this);
        beaconManager.setRangingListener(new BeaconManager.RangingListener()
        {
            @Override
            public void onBeaconsDiscovered(Region arg0, List<Beacon> arg1)
            {
                // TODO Auto-generated method stub
                Log.d(TAG, "Ranged beacons: " + arg1);

            }
        });
    }

    @Override
    protected void onStart()
    {
        beaconManager.connect(new BeaconManager.ServiceReadyCallback()
        {
            @Override
            public void onServiceReady()
            {
                try
                {
                    beaconManager.startRanging(ALL_ESTIMOTE_BEACONS);
                }
                catch (Exception e)
                {
                    Log.e(TAG, "Cannot start ranging", e);
                }
            }
        });

        super.onStart();
    }

    @Override
    protected void onStop()
    {
        try
        {
            beaconManager.stopRanging(ALL_ESTIMOTE_BEACONS);
        }
        catch (Exception e)
        {
            Log.e(TAG, "Cannot stop but it does not matter now", e);
        }
        super.onStop();
    }

    @Override
    protected void onDestroy()
    {
        beaconManager.disconnect();
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //ystemRequirementsChecker.checkWithDefaultDialogs(this);
    }

    @Override
    public void onClick(View v) {

    }

    public void showNotification(String title, String message) {
        Intent notifyIntent = new Intent(this, HomeActivity.class);
        notifyIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivities(this, 0,
                new Intent[] { notifyIntent }, PendingIntent.FLAG_UPDATE_CURRENT);
        Notification notification = new Notification.Builder(this)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .build();
        notification.defaults |= Notification.DEFAULT_SOUND;
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, notification);
    }
}
