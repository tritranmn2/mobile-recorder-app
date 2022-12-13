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

import java.util.List;

public class RecordDetailActivity extends Activity {
    ImageView btn_back;
    TextView tvName;
    TextView tvRcRuntime;
    int id;

    MediaPlayer RecordPlayer;
    String ChosenRecord = "song1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_detail_record);
        btn_back = (ImageView) findViewById(R.id.return_button);
        tvName = (TextView) findViewById(R.id.record_name);
        tvRcRuntime = (TextView) findViewById(R.id.record_runtime);
        Bundle extras = getIntent().getExtras(); //lay id truyen tu main qua

        if (extras != null) {
             id = extras.getInt("id");
        }
        Record record= ListRecord.getRecord(id);
        tvName.setText(record.name);
        tvRcRuntime.setText(record.lenght);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                switchActivity(context,ListRecord.class);
            }
        });
    }

    public void switchActivity(Context context, Class nextActivity){
        Intent intent = new Intent(context, nextActivity);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivityIfNeeded(intent, 0);
    }

    public void play(View v){
        if(RecordPlayer == null){
            Resources res = getResources();
            int currentRecord = res.getIdentifier(ChosenRecord, "raw", getPackageName());
            RecordPlayer = MediaPlayer.create(this, currentRecord);
            RecordPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    stopPlayer();
                }
            });
        }
        if(RecordPlayer.isPlaying()){
            pause(v);
        }
        else RecordPlayer.start();

    }
    public void pause(View v){
        if (RecordPlayer != null){
            RecordPlayer.pause();
        }
    }
    public void stop(View v){
        stopPlayer();
    }
    private void stopPlayer(){
        if (RecordPlayer !=null){
            RecordPlayer.release();
            RecordPlayer = null;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopPlayer();
    }
}


//đủ phân này r