package com.example.recorder;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.List;

public class RecordDetailActivity extends Activity {
    ImageView btn_back;
    TextView tvName;
    TextView tvRcRuntime;
    ToggleButton btn_play;
    int id;
    String ChosenRecord = "song1";
    public String ACTION = "STOP";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_detail_record);
        btn_back = (ImageView) findViewById(R.id.return_button);
        tvName = (TextView) findViewById(R.id.record_name);
        tvRcRuntime = (TextView) findViewById(R.id.record_runtime);
        btn_play = (ToggleButton) findViewById(R.id.play_button);
        Bundle extras = getIntent().getExtras(); //lay id truyen tu main qua

        if (extras != null) {
             id = extras.getInt("id");
        }
        Record record= ListRecord.getRecord(id);
        ChosenRecord = record.name;
        tvName.setText(record.name);
        tvRcRuntime.setText(record.lenght);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                switchActivity(context,ListRecord.class);
            }
        });
        btn_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
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
                handleService(context,PlayBackground.class,ACTION,ChosenRecord);

            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        Context context = getApplicationContext();
        handleService(context,PlayBackground.class,"STOP",ChosenRecord);

    }

    public void switchActivity(Context context, Class nextActivity){
        Intent intent = new Intent(context, nextActivity);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
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