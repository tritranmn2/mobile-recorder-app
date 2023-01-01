package com.example.recorder;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.File;
import java.io.IOException;

public class ServiceRecord extends Service {
    private String nameFile;
    private String pathRecord;
    private static MediaRecorder mediaRecorder = new MediaRecorder();
    private static int totalSeconds=0;
    private static boolean isRecording = false;
    @Override public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override public void onCreate() {
        Toast.makeText(this, "Record onCreate", Toast.LENGTH_SHORT).show();
        super.onCreate();

    }

    @SuppressLint("HandlerLeak")
//    Handler handler =new Handler();
    Handler handler =new Handler(){
        @Override
        public void handleMessage( Message msg) {
            super.handleMessage(msg);
            System.out.println("handler:"+(String)msg.obj);
//            send cho broast cast reciever để in setText cho textView

        }
    };
    public class CountTimeRunnable implements Runnable {
        @Override public void run() {
            if(!isRecording)return;
            totalSeconds++;
            MyTime myTime = new MyTime(totalSeconds);
//            System.out.println(myTime.toString());
            Message msg= handler.obtainMessage(1,myTime.toString());
            handler.sendMessage(msg);
            handler.postDelayed(this,1000);
        }
    }
    public Runnable countTimeRunnable = new CountTimeRunnable();
//    public Thread threadCountTime = new Thread(countTimeRunnable);
    public class RecorderRunnable implements Runnable {
        @Override public void run() {
            pathRecord = getRecordingFilePath(nameFile);
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            mediaRecorder.setOutputFile(pathRecord);
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            try {
                mediaRecorder.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }
            mediaRecorder.start();
            handler.post(countTimeRunnable);
        }
    }
    public Thread threadRecorder = new Thread(new RecorderRunnable());


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "Record onStartCommand", Toast.LENGTH_SHORT).show();

        super.onStartCommand(intent, flags, startId);
        Bundle extras = intent.getExtras();
        if (extras != null) {
            this.nameFile = extras.getString("nameFile");
        }
        isRecording =true;
        threadRecorder.start();
//        threadCountTime.start();
        return Service.START_STICKY;
    }

    @Override
    public void onDestroy() {
        Toast.makeText(this, "Record onDestroy", Toast.LENGTH_SHORT).show();

        super.onDestroy();
        mediaRecorder.stop();
        isRecording=false;
        String name = nameFile;
        String date = "mm-dd-yyyy";
        String length = (new MyTime(totalSeconds).toString());
        Intent intentSendInfor = new Intent("SendRecord");
        intentSendInfor.putExtra("name",name);
        intentSendInfor.putExtra("date",date);
        intentSendInfor.putExtra("length",length);
        intentSendInfor.putExtra("source",pathRecord);
        sendBroadcast(intentSendInfor);
        totalSeconds=0;
    }

    private String getRecordingFilePath(String name){
        Context context = getApplicationContext();
        ContextWrapper contextWrapper = new ContextWrapper(context);
        File music = contextWrapper.getExternalFilesDir(Environment.DIRECTORY_MUSIC);
        File file = new File(music, name + ".mp3");
        return file.getPath();

    }

}