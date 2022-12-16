package com.example.recorder;

import android.Manifest;
import android.app.Activity;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.UserHandle;
import android.view.Display;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;


public class Recording extends AppCompatActivity{

    private Integer ID;
    private String name;
    private Date dateSave;
    private Date length;
    private String source;

    private static final Integer REQUEST_AUDIO_PERMISSION_CODE = 101;
    private MediaRecorder mediaRecorder;
    private MediaPlayer mediaPlayer;
    private Boolean isRecording;
    private static Boolean isPlaying;
    private Integer seconds;
    private String pathRecord;
    private String pathPlay;
    private Integer dummySeconds;
    private Integer playableSeconds;
    private Handler handler;
    private Activity activity;
    private Context context;
    ExecutorService executorService = Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println(context);

        context = getApplicationContext();
        System.out.println(context);
    }

    public Recording(){
        ID = 0;
        name = null;
        dateSave = null;
        length = null;
        source = null;
        mediaRecorder = null;
        mediaPlayer = null;
        isRecording = false;
        isPlaying = false;
        seconds = 0;
        pathRecord = null;
        pathPlay = null;
        dummySeconds = 0;
        playableSeconds = 0;
        handler = new Handler();
        activity = new Activity();
        context = activity;
    }

    public Recording(Activity ac){
        ID = 0;
        name = null;
        dateSave = null;
        length = null;
        source = null;
        mediaRecorder = null;
        mediaPlayer = null;
        isRecording = false;
        isPlaying = false;
        seconds = 0;
        pathRecord = null;
        pathPlay = null;
        dummySeconds = 0;
        playableSeconds = 0;
        handler = new Handler();
        activity = ac;
        context = activity;
    }

    public  Record Record_Stop(String nameFile){
        if(checkRecordingPermission()){
            if(!isRecording){
                isRecording = true;
                executorService.execute(new Runnable() {
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
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                playableSeconds = 0;
                                seconds = 0;
                                dummySeconds = 0;
                                runTimer();
                            }
                        });

                    }
                });
            }
            else {
                executorService.execute(new Runnable() {
                    @Override
                    public void run() {
                        mediaRecorder.stop();
                        mediaRecorder.release();
                        mediaRecorder = null;
                        playableSeconds = seconds;
                        dummySeconds = seconds;
                        seconds = 0;
                        isRecording = false;

                        source = pathRecord;
                        name = nameFile;
                        dateSave = new Date();
                        ID = 1; //ko biết tạo ID
                        length = new Date();
                        DateFormat dateFormat = null;
                        dateFormat = new SimpleDateFormat("HH:mm:ss");
                        Integer hours = dummySeconds/3600;
                        Integer minutes = (dummySeconds % 3600)/60;
                        Integer secs = dummySeconds % 60;
                        String format = hours.toString() + ":" + minutes.toString() + ":" + secs.toString();
                        try {
                            length =  dateFormat.parse(format);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                handler.removeCallbacksAndMessages(null);
                            }
                        });
                    }
                });
            }
        }
        else{
            requestRecordingPermission();
        }
        Record record = new Record(nameFile,"00:00:00",pathRecord);
        return record;
    }

    public void Play(String name){
        pathPlay = getRecordingFilePath(name);
        mediaPlayer = new MediaPlayer();
        if(!isPlaying){
            if(pathPlay!=null){
                try {
                    mediaPlayer.setDataSource(pathPlay);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else {
                Toast.makeText(context, "No recording present", Toast.LENGTH_SHORT).show();
                return;
            }
            try {
                mediaPlayer.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }
            mediaPlayer.start();
            isPlaying = true;
            runTimer();
        }
        else {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
            mediaPlayer = new MediaPlayer();
            isPlaying = false;
            seconds = 0;
            handler.removeCallbacksAndMessages(null);
        }
    }


    private void requestRecordingPermission(){
        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.RECORD_AUDIO},REQUEST_AUDIO_PERMISSION_CODE);
    }

    public Boolean checkRecordingPermission(){
        if(ContextCompat.checkSelfPermission(activity,Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_DENIED){
            requestRecordingPermission();
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==REQUEST_AUDIO_PERMISSION_CODE){
            if(grantResults.length>0){
                Boolean permissionToRecord = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                if(permissionToRecord){
                    System.out.println("Permission Given");
                    Toast.makeText(context,"Permission Given", Toast.LENGTH_SHORT).show();
                }
                else {
                    System.out.println("Permission Denied");
                    Toast.makeText(context,"Permission Denied", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private String getRecordingFilePath(String name){
        ContextWrapper contextWrapper = new ContextWrapper(context);
        File music = contextWrapper.getExternalFilesDir(Environment.DIRECTORY_MUSIC);
        File file = new File(music, name + ".mp3");
        return file.getPath();
    }

    private void runTimer(){
        handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                int hours = seconds / 3600;
                int minutes = (seconds % 3600)/60;
                int secs = seconds % 60;
                String time = String.format(Locale.getDefault(), "%02d:%02d:%02d",hours, minutes, secs);

                System.out.println(time);

//                cho time vào text view, time thay đổi theo từng giây

                if(isRecording || (isPlaying && playableSeconds != -1)){
                    seconds++;
                    playableSeconds--;
                    if(isPlaying && playableSeconds == -1 ){
                        mediaPlayer.stop();
                        mediaPlayer.release();
                        isPlaying = false;
                        mediaPlayer = null;
                        mediaPlayer = new MediaPlayer();
                        playableSeconds = dummySeconds;
                        seconds = 0;
                        handler.removeCallbacksAndMessages(null);
                        return;
                    }
                }
                handler.postDelayed(this, 1000);
            }
        });
    }

    public Integer getID() {
        return ID;
    }

    public Date getDateSave() {
        return dateSave;
    }

    public String getName() {
        return name;
    }

    public Date getLength() {
        return length;
    }

    public String getSource() {
        return source;
    }

}

