package com.example.recorder;


import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.util.List;

public class RecordDetailActivity extends Activity {
    ImageView btnBack;
    ToggleButton btnPlay;
    TextView tvCurRecordName,tvRecordRuntime;
    int curRecordId;
    String curRecordName = "song1";
    public String ACTION = "STOP";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_detail_record);
        btnBack = (ImageView) findViewById(R.id.return_button);
        tvCurRecordName = (TextView) findViewById(R.id.record_name);
        tvRecordRuntime = (TextView) findViewById(R.id.record_runtime);
        btnPlay = (ToggleButton) findViewById(R.id.play_button);
        Bundle extras = getIntent().getExtras(); //lay id truyen tu main qua
        if (extras != null) {
            curRecordId = extras.getInt("id");
        }
        Record record= ListRecord.getRecord(curRecordId); // khong anh huong toi records goc
        record.play();
        ACTION = record.status;
        curRecordName = record.name;
        tvCurRecordName.setText(record.name);
        tvRecordRuntime.setText(record.lenght);
        Context context = getApplicationContext();
        handleService(context,PlayBackground.class,ACTION,curRecordName);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                switchActivity(context,ListRecord.class);
            }
        });
        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NotificationCompat.Builder builder= new NotificationCompat.Builder(RecordDetailActivity.this,"record notification");
                Context context = v.getContext();
                builder.setContentTitle(record.name);

                if (record.status.equals("STOP")) {
                    ACTION = "PLAY";
                    record.play();
                } else if (record.status.equals("PAUSE")) {
                    ACTION = "RESUME";
                    record.play();
                }else if (record.status.equals("PLAY")) {
                    ACTION = "PAUSE";
                    record.pause();
                }
                handleService(context,PlayBackground.class,ACTION,curRecordName);
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        Context context = getApplicationContext();
        handleService(context,PlayBackground.class,"STOP",curRecordName);

    }

    public void switchActivity(Context context, Class nextActivity){
        Intent intent = new Intent(context, nextActivity);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);//dong nay quan trong
        startActivityIfNeeded(intent, 0);
    }
    public void handleService(Context context, Class nextActivity, String action, String nameRecord) {

        Intent playbackIntent = new Intent(context, nextActivity);

        playbackIntent.putExtra("ACTION", action);
        playbackIntent.putExtra("name", nameRecord);
        if (action.equals("PLAY") || action.equals("PAUSE") ) {
            context.startService(playbackIntent);
        } else if (action.equals("STOP")) {
            context.stopService(playbackIntent);
        } else if (action.equals("RESUME")) {
            context.startService(playbackIntent);
        }
    }
}


//đủ phân này r