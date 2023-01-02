package com.example.recorder;


import android.annotation.SuppressLint;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.sql.Time;

public class PlayBackground extends Service {
    public static String ACTION ;
    static public MediaPlayer player =new MediaPlayer() ;
    static public String curSourceRecord = "";
    static public int pStart =0;
    static public int curIdRecord;
    static public int curTime;
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @SuppressLint("HandlerLeak")
    Handler handler =new Handler(){
        @Override
        public void handleMessage( Message msg) {
            super.handleMessage(msg);
            int curTime =(int)msg.obj;
            System.out.println("handle Play" + String.valueOf(curIdRecord)+":" + String.valueOf(curTime));
            Intent intentSendCurTimePlay = new Intent("SendCurTimePlay-"+ String.valueOf(curIdRecord));
            intentSendCurTimePlay.putExtra("curTime",curTime);
            sendBroadcast(intentSendCurTimePlay);
        }
    };
    public Runnable curTimeRunnable = new CurTimeRunnable() ;
    public Thread threadCurTime = new Thread(curTimeRunnable);

    @Override
    public void onCreate() {
        super.onCreate();
        curTime=0;
//        threadCurTime.start();
        Toast.makeText(this, "playback Created", Toast.LENGTH_SHORT).show();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "playback onStartCm", Toast.LENGTH_SHORT).show();
        super.onStartCommand(intent, flags, startId);

        Bundle extras = intent.getExtras();
        if (extras != null) {
            ACTION = extras.getString("ACTION");
            curSourceRecord = extras.getString("source");
            curIdRecord = extras.getInt("id");
            if (ACTION.equals("RESUME")) {
                int resumeTime=extras.getInt("pStart");
                System.out.println("Progress:"+ String.valueOf(resumeTime));
                if (resumeTime != 0) {
                    pStart=resumeTime*1000;
                    player.stop();
                }
            }
        }
        if ( player.isPlaying() && !ACTION.equals("RESUME")) {
            player.stop();
        }
        switch (ACTION) {
            case "PLAY":
//                curIdRecord = getResources().getIdentifier(curSourceRecord, "raw", getPackageName());
//                player = MediaPlayer.create(getApplicationContext(), curIdRecord);
                setSourceRecord();
                handler.post(curTimeRunnable);
                player.start();
                break;
            case "RESUME":
//                curIdRecord = getResources().getIdentifier(curSourceRecord, "raw", getPackageName());
//                player = MediaPlayer.create(getApplicationContext(), curIdRecord);
                setSourceRecord();
                player.seekTo(pStart);
                player.start();
                handler.post(curTimeRunnable);

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
        player.stop();
        //Có 2 dòng này là lỗi
//        player.release();
//        player = null;
    }

    void setSourceRecord() {
        try {
            System.out.println(curSourceRecord);
            player =new MediaPlayer() ;
            player.setDataSource(curSourceRecord);
            try {
                player.prepare();//thiếu dòng này là lỗi
            } catch (IOException e) {
                e.printStackTrace();
            }
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
    public int last=0;
    public class CurTimeRunnable implements Runnable {
        @Override public void run() {
            if (!player.isPlaying()) {
                if(last==1)return;
                last=1;
                int lastTime =curTime+1;
                Message msg= handler.obtainMessage(1,lastTime);
                handler.sendMessage(msg);
                handler.postDelayed(this,1000);
//                handler.removeCallbacksAndMessages(null);
                return;
            }
            last=0;
            curTime = player.getCurrentPosition()/1000;
            Message msg= handler.obtainMessage(1,curTime);
            handler.sendMessage(msg);
            handler.postDelayed(this,1000);
        }
    }

//    class MyCurProgressReceiver extends BroadcastReceiver {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            String action = intent.getAction();
//            if (action.equals("SendProgress")) {
//                int progress = intent.getIntExtra("progress", 0);
//                setSourceRecord();
//                player.seekTo(progress);
//                player.start();
//                handler.post(curTimeRunnable);
//            }
//        }
//    }
}
