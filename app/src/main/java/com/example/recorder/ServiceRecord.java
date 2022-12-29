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

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.File;
import java.io.IOException;

public class ServiceRecord extends Service {
    private String nameFile;
    private String pathRecord;
    private static MediaRecorder mediaRecorder;

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();

    }
    @SuppressLint("HandlerLeak")
    Handler handler =new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            Log.e("Handler record Service",  (String)msg.obj);
        }
    };


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        Bundle extras = intent.getExtras();
        if (extras != null) {

            this.nameFile = extras.getString("nameFile");
        }
        Thread threadRecorder = new Thread(new Runnable() {
            @Override
            public void run() {

                pathRecord = getRecordingFilePath(nameFile);
                System.out.println(pathRecord);
                mediaRecorder = new MediaRecorder();
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
            }
        });
        threadRecorder.start();
        return Service.START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mediaRecorder.stop();
        String name = nameFile;
        String date = "mm-dd-yyyy";
        String length = "00:05:11";


        Intent intentSendInfor = new Intent("SendRecord");
        intentSendInfor.putExtra("name",name);
        intentSendInfor.putExtra("date",date);
        intentSendInfor.putExtra("length",length);
        intentSendInfor.putExtra("source",pathRecord);
        sendBroadcast(intentSendInfor);


        mediaRecorder.release();
        mediaRecorder = null;


    }

    private String getRecordingFilePath(String name){
        Context context = getApplicationContext();
        ContextWrapper contextWrapper = new ContextWrapper(context);
        File music = contextWrapper.getExternalFilesDir(Environment.DIRECTORY_MUSIC);
        File file = new File(music, name + ".mp3");
        return file.getPath();
    }

}