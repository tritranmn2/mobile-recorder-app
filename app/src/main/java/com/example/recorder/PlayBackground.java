package com.example.recorder;


import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class PlayBackground extends Service {
    public static boolean boolIsServiceCreated = false;
    MediaPlayer player;
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        Toast.makeText(this, "playback Created", Toast.LENGTH_LONG).show();
        Log.e("playback", "onCreate");
        boolIsServiceCreated = true;
        player = MediaPlayer.create(getApplicationContext(), R.raw.music);
    }

    @Override
    public void onDestroy() {
        Toast.makeText(this, "playback Destroy", Toast.LENGTH_LONG).show();
        Log.e("playback", "onDestroy");
        player.stop();
        player.release();
        player = null;
    }

    @Override
    public void onStart(Intent intent, int start_id) {
        if (player.isPlaying()==true){
            Toast.makeText(this, "playback Already Started " + start_id, Toast.LENGTH_LONG).show();
        }
        else
            Toast.makeText(this, "playback Started " + start_id, Toast.LENGTH_LONG).show();
        Log.e("playback", "onStart");
        player.start();
    }
}
