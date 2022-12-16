//package com.example.recorder;
//
//
//import android.Manifest;
//import android.app.Activity;
//import android.content.ContextWrapper;
//import android.content.pm.PackageManager;
//import android.media.MediaPlayer;
//import android.media.MediaRecorder;
//import android.os.Bundle;
//import android.os.Environment;
//import android.os.Handler;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.core.app.ActivityCompat;
//import androidx.core.content.ContextCompat;
//
//import java.io.File;
//import java.io.IOException;
//import java.util.Date;
//import java.util.Locale;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//import java.text.DateFormat;
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//
//public class Recording  extends Activity {
//    private Integer ID;
//    private String name;
//    private Date dateSave;
//    private Date length;
//    private String source;
//
//    private static final Integer REQUEST_AUDIO_PERMISSION_CODE = 101;
//    private MediaRecorder mediaRecorder;
//    private MediaPlayer mediaPlayer;
//    private Boolean isRecording;
//    private Boolean isPlaying;
//    private Integer seconds;
//    private String pathRecord;
//    private String pathPlay;
//    private Integer dummySeconds;
//    private Integer playableSeconds;
//    private Handler handler;
//    Activity activity;
//
//    ExecutorService executorService = Executors.newSingleThreadExecutor();
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//    }
//
//    public Recording(){
//        ID = 0;
//        name = null;
//        dateSave = null;
//        length = null;
//        source = null;
//        mediaRecorder = null;
//        mediaPlayer = null;
//        isRecording = false;
//        isPlaying = false;
//        seconds = 0;
//        pathRecord = null;
//        pathPlay = null;
//        dummySeconds = 0;
//        playableSeconds = 0;
//        handler = new Handler();
//        activity = new Activity();
//    }
//
//    public Recording(Activity ac){
//        ID = 0;
//        name = null;
//        dateSave = null;
//        length = null;
//        source = null;
//        mediaRecorder = null;
//        mediaPlayer = null;
//        isRecording = false;
//        isPlaying = false;
//        seconds = 0;
//        pathRecord = null;
//        pathPlay = null;
//        dummySeconds = 0;
//        playableSeconds = 0;
//        handler = new Handler();
//        activity = ac;
//    }
//
//    public void Record_Stop(String nameFile){
//        if(checkRecordingPermission()){
//            if(!isRecording){
//                isRecording = true;
//                executorService.execute(new Runnable() {
//                    @Override
//                    public void run() {
//                        pathRecord = getRecordingFilePath(nameFile);
//                        mediaRecorder = new MediaRecorder();
//                        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
//                        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
//                        mediaRecorder.setOutputFile(pathRecord);
//                        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
//                        try {
//                            mediaRecorder.prepare();
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//
//                        mediaRecorder.start();
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                playableSeconds = 0;
//                                seconds = 0;
//                                dummySeconds = 0;
//                                runTimer();
//                            }
//                        });
//
//                    }
//                });
//            }
//            else {
//                executorService.execute(new Runnable() {
//                    @Override
//                    public void run() {
//                        mediaRecorder.stop();
//                        mediaRecorder.release();
//                        mediaRecorder = null;
//                        playableSeconds = seconds;
//                        dummySeconds = seconds;
//                        seconds = 0;
//                        isRecording = false;
//
//                        source = pathRecord;
//                        name = nameFile;
//                        dateSave = new Date();
//                        ID = 1; //ko biết tạo ID
//                        length = new Date();
//                        DateFormat dateFormat = null;
//                        dateFormat = new SimpleDateFormat("HH:mm:ss");
//                        Integer hours = dummySeconds/3600;
//                        Integer minutes = (dummySeconds % 3600)/60;
//                        Integer secs = dummySeconds % 60;
//                        String format = hours.toString() + ":" + minutes.toString() + ":" + secs.toString();
//                        try {
//                            length =  dateFormat.parse(format);
//                        } catch (ParseException e) {
//                            e.printStackTrace();
//                        }
//
//
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                handler.removeCallbacksAndMessages(null);
//                            }
//                        });
//                    }
//                });
//            }
//        }
//        else{
//            requestRecordingPermission();
//        }
//    }
//
//    public void Play(String name){
//        pathPlay = getRecordingFilePath(name);
//        mediaPlayer = new MediaPlayer();
//        if(!isPlaying){
//            if(pathPlay!=null){
//                try {
//                    mediaPlayer.setDataSource(pathPlay);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//            else {
//                Toast.makeText(getApplicationContext(), "No recording present", Toast.LENGTH_SHORT).show();
//                return;
//            }
//            try {
//                mediaPlayer.prepare();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            mediaPlayer.start();
//            isPlaying = true;
//            runTimer();
//        }
//        else {
//            mediaPlayer.stop();
//            mediaPlayer.release();
//            mediaPlayer = null;
//            mediaPlayer = new MediaPlayer();
//            isPlaying = false;
//            seconds = 0;
//            handler.removeCallbacksAndMessages(null);
//        }
//    }
//
//    private void requestRecordingPermission(){
//        ActivityCompat.requestPermissions(Recording.this, new String[]{Manifest.permission.RECORD_AUDIO},REQUEST_AUDIO_PERMISSION_CODE);
//    }
//
//    public Boolean checkRecordingPermission(){
//        if(ContextCompat.checkSelfPermission(this,Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_DENIED){
//            requestRecordingPermission();
//            return false;
//        }
//        return true;
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if(requestCode==REQUEST_AUDIO_PERMISSION_CODE){
//            if(grantResults.length>0){
//                Boolean permissionToRecord = grantResults[0] == PackageManager.PERMISSION_GRANTED;
//                if(permissionToRecord){
//                    Toast.makeText(getApplicationContext(),"Permission Given", Toast.LENGTH_SHORT).show();
//                }
//                else {
//                    Toast.makeText(getApplicationContext(),"Permission Denied", Toast.LENGTH_SHORT).show();
//                }
//            }
//        }
//    }
//
//    private String getRecordingFilePath(String name){
//        ContextWrapper contextWrapper = new ContextWrapper(getApplicationContext());
//        File music = contextWrapper.getExternalFilesDir(Environment.DIRECTORY_MUSIC);
//        File file = new File(music, name + ".mp3");
//        return file.getPath();
//    }
//
//    private void runTimer(){
//        handler = new Handler();
//        handler.post(new Runnable() {
//            @Override
//            public void run() {
//                int hours = seconds / 3600;
//                int minutes = (seconds % 3600)/60;
//                int secs = seconds % 60;
//                String time = String.format(Locale.getDefault(), "%02d:%02d:%02d",hours, minutes, secs);
////                cho time vào text view, time thay đổi theo từng giây
//                if(isRecording || (isPlaying && playableSeconds != -1)){
//                    seconds++;
//                    playableSeconds--;
//                    if(isPlaying && playableSeconds == -1 ){
//                        mediaPlayer.stop();
//                        mediaPlayer.release();
//                        isPlaying = false;
//                        mediaPlayer = null;
//                        mediaPlayer = new MediaPlayer();
//                        playableSeconds = dummySeconds;
//                        seconds = 0;
//                        handler.removeCallbacksAndMessages(null);
//                        return;
//                    }
//                }
//                handler.postDelayed(this, 1000);
//            }
//        });
//    }
//
//    public Integer getID() {
//        return ID;
//    }
//
//    public Date getDateSave() {
//        return dateSave;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public Date getLength() {
//        return length;
//    }
//
//    public String getSource() {
//        return source;
//    }
//}

package com.example.recorder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.ContextWrapper;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Executable;
import java.util.Locale;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Recording extends AppCompatActivity {

    private static final Integer REQUEST_AUDIO_PERMISSION_CODE = 101;
    private ImageView recordbtn;
    private TextView tvPath, tvTime;
    private MediaRecorder mediaRecorder;
    private MediaPlayer mediaPlayer;
    private Boolean isRecording = false;
    private Boolean isPlaying = false;
    private Integer seconds = 0;
    private String pathRecord = null;
    private String pathPlay = null;
    private Integer dummySeconds = 0;
    private Integer playableSeconds = 0;
    private Handler handler;

    ExecutorService executorService = Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        recordbtn = (ImageView) findViewById(R.id.btn_rc);
//        tvPath = (TextView) findViewById(R.id.path);
//        tvTime = (TextView) findViewById(R.id.time);


        mediaPlayer = new MediaPlayer();
        recordbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Record_Stop();
            }
        });




    }

    public void Record_Stop(){
        if(checkRecordingPermission()){
            if(!isRecording){
                isRecording = true;
                executorService.execute(new Runnable() {
                    @Override
                    public void run() {
                        pathRecord = getRecordingFilePath("TestFile");
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
//                                        tvPath.setText(pathRecord);
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

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                handler.removeCallbacksAndMessages(null);
                            }
                        });
                        System.out.println(seconds);
                        System.out.println(dummySeconds);
                        System.out.println(playableSeconds);

                    }
                });
            }
        }
        else{
            requestRecordingPermission();
        }
    }

    private void requestRecordingPermission(){
        ActivityCompat.requestPermissions(Recording.this, new String[]{Manifest.permission.RECORD_AUDIO},REQUEST_AUDIO_PERMISSION_CODE);
    }

    public Boolean checkRecordingPermission(){
        if(ContextCompat.checkSelfPermission(this,Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_DENIED){
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
                    Toast.makeText(getApplicationContext(),"Permission Given", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(getApplicationContext(),"Permission Denied", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private String getRecordingFilePath(String name){
        ContextWrapper contextWrapper = new ContextWrapper(getApplicationContext());
        File music = contextWrapper.getExternalFilesDir(Environment.DIRECTORY_MUSIC);
        File file = new File(music, name + ".mp3");
        return file.getPath();
    }

    private void runTimer(){
        handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                int minutes = (seconds % 3600)/60;
                int secs = seconds % 60;
                String time = String.format(Locale.getDefault(), "%02d:%02d", minutes, secs);
                System.out.println(time);

//                tvTime.setText(time);

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

    @Override
    protected void onPause() {
        Toast.makeText(this, "OnPause", Toast.LENGTH_SHORT).show();
        super.onPause();

    }

    @Override
    protected void onResume() {
        Toast.makeText(this, "OnResume", Toast.LENGTH_SHORT).show();

        super.onResume();

    }

    @Override
    protected void onRestart() {
        Toast.makeText(this, "OnRestart", Toast.LENGTH_SHORT).show();

        super.onRestart();
    }

    @Override
    protected void onDestroy() {
        Toast.makeText(this, "OnDestroy", Toast.LENGTH_SHORT).show();

        super.onDestroy();
    }

    @Override
    protected void onStop() {
        Toast.makeText(this, "OnStop", Toast.LENGTH_SHORT).show();

        super.onStop();
    }
}