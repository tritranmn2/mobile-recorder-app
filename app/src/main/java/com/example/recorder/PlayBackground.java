package com.example.recorder;


import android.app.Service;
import android.content.Intent;
import android.content.res.Resources;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import java.sql.Time;

public class PlayBackground extends Service {
    public static String ACTION ;
    static public MediaPlayer player;
    static public String nameResource = "song1";
    static public int pStart =0;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        Toast.makeText(this, "playback Created", Toast.LENGTH_SHORT).show();
        int currentRecord = getResources().getIdentifier(nameResource, "raw", getApplication().getPackageName());
        player = MediaPlayer.create(getApplicationContext(), currentRecord);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);

        Bundle extras = intent.getExtras();
        if (extras != null) {
            ACTION = extras.getString("ACTION");
            nameResource = extras.getString("name");
        }
        if (player.isPlaying() || !ACTION.equals("RESUME")) {
            player.stop();
        }

        if (ACTION.equals("PLAY")) {
            int currentRecord = getResources().getIdentifier(nameResource, "raw", getPackageName());
            player = MediaPlayer.create(getApplicationContext(), currentRecord);
            player.start();

        } else if (ACTION.equals("PAUSE")) {
            pStart=player.getCurrentPosition();
            player.pause();
        } else if (ACTION.equals("STOP") ) {
            player.stop();
        }else if (ACTION.equals("RESUME") ) {
            int currentRecord = getResources().getIdentifier(nameResource, "raw", getPackageName());
            player = MediaPlayer.create(getApplicationContext(), currentRecord);
            player.seekTo(pStart);
            player.start();

        }
        return Service.START_STICKY;
    }

    @Override
    public void onDestroy() {
        Toast.makeText(this, "playback Destroy", Toast.LENGTH_SHORT).show();
//        Log.e("playback", "onDestroy");
        player.stop();
        player.release();
        player = null;
    }



//    @Override
//    public void onStart(Intent intent, int start_id) {
//        if (player.isPlaying() == true) {
//            Toast.makeText(this, "playback Already Started " + start_id, Toast.LENGTH_SHORT).show();
//        } else
//            Toast.makeText(this, "playback Started " + start_id, Toast.LENGTH_SHORT).show();
//        Log.e("playback", "onStart");
//        player.start();
//    }
}
