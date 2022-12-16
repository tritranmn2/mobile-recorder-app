package com.example.recorder;


import android.app.Service;
import android.content.Intent;
import android.content.res.Resources;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.sql.Time;

public class PlayBackground extends Service {
    public static String ACTION ;
    static public MediaPlayer player;
    static public String curSourceRecord = "";
    static public int pStart =0;
    static public int curIdRecord;
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        Toast.makeText(this, "playback Created", Toast.LENGTH_SHORT).show();
//        curIdRecord = getResources().getIdentifier(curSourceRecord, "raw", getApplication().getPackageName());
//        player = MediaPlayer.create(getApplicationContext(), curIdRecord);
        setSourceRecord();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);

        Bundle extras = intent.getExtras();
        if (extras != null) {
            ACTION = extras.getString("ACTION");
            curSourceRecord = extras.getString("source");
        }
        if (player.isPlaying() || !ACTION.equals("RESUME")) {
            player.stop();
        }
        switch (ACTION) {
            case "PLAY":
//                curIdRecord = getResources().getIdentifier(curSourceRecord, "raw", getPackageName());
//                player = MediaPlayer.create(getApplicationContext(), curIdRecord);
                setSourceRecord();
                player.start();
                break;
            case "RESUME":
//                curIdRecord = getResources().getIdentifier(curSourceRecord, "raw", getPackageName());
//                player = MediaPlayer.create(getApplicationContext(), curIdRecord);
                setSourceRecord();
                player.seekTo(pStart);
                player.start();
                break;
            case "STOP":
                player.stop();
                break;
            case "PAUSE":
                pStart = player.getCurrentPosition();
                player.pause();
                break;
            default:
                break;
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

    void setSourceRecord() {
        try {
            player.setDataSource(curSourceRecord);
        } catch (IOException e) {
            e.printStackTrace();
        }
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
