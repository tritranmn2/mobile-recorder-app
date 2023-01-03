package com.example.recorder;


import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.List;

public class RecordDetailActivity extends Activity {
    ImageView btnBack;
    ToggleButton btnPlay;
    TextView tvCurRecordName,tvRecordRuntime,tvRecordLength;
    ImageButton btnMute;
    SeekBar sbCurTime;
    int curRecordId;
    String curRecordSource;
    public String ACTION = "STOP";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_detail_record);
//        declare
        btnBack = (ImageView) findViewById(R.id.return_button);
        tvCurRecordName = (TextView) findViewById(R.id.record_name);
        tvRecordRuntime = (TextView) findViewById(R.id.record_runtime);
        tvRecordLength = (TextView) findViewById(R.id.record_length);
        btnPlay = (ToggleButton) findViewById(R.id.play_button);
        sbCurTime = (SeekBar)findViewById(R.id.seekBar) ;
        btnMute = (ImageButton) findViewById(R.id.mute_button);
        Bundle extras = getIntent().getExtras(); //lay id truyen tu main qua
        if (extras != null) {
            curRecordId = extras.getInt("id");
        }
        Record record= ListRecord.getRecord(curRecordId); // khong anh huong toi records goc
        record.play();
        ACTION = record.status;
        curRecordSource = record.source;
        tvCurRecordName.setText(record.name);
        tvRecordLength.setText(record.lenght);
        int length = MyTime.toSeconds(record.lenght);
        sbCurTime.setMax(length);
        //        register
        sbCurTime.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    Context context = sbCurTime.getContext();
                    Intent playbackIntent = new Intent(context, PlayBackground.class);
                    playbackIntent.putExtra("ACTION", "RESUME");
                    playbackIntent.putExtra("source", curRecordSource);
                    playbackIntent.putExtra("id", curRecordId);
                    playbackIntent.putExtra("pStart", progress);
                    record.play();
                    btnPlay.setButtonDrawable(getResources().getDrawable(R.drawable.ic_pause_detail));

                    context.startService(playbackIntent);
                }
            }

            @Override public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        class MyCurTimeReceiver extends BroadcastReceiver {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();

                if( action.equals("SendCurTimePlay-"+String.valueOf(curRecordId))  ){
                    int curTime = intent.getIntExtra("curTime",0) +1;
                    System.out.println("receiver"+String.valueOf(curRecordId)+":" + String.valueOf(curTime));
                    sbCurTime.setProgress(curTime);
                    String curTimeString = (new MyTime(curTime)).toString();
                    tvRecordRuntime.setText(curTimeString);
//                    record.play();
                    if (curTime + 1 > length) {
                        record.stop();
                        btnPlay.setButtonDrawable(getResources().getDrawable(R.drawable.ic_play_detail));
//                        unregisterReceiver(this);
                    }
                }
            }
        }
        IntentFilter receiveCurTimePlayFilter= new IntentFilter("SendCurTimePlay-"+String.valueOf(curRecordId));
        BroadcastReceiver receiver = new MyCurTimeReceiver();
        registerReceiver(receiver, receiveCurTimePlayFilter);


        Context context = getApplicationContext();
        handleService(context,PlayBackground.class,ACTION,curRecordSource);
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
                Context context = v.getContext();
                if (record.status.equals("STOP")) {
                    ACTION = "PLAY";
                    record.play();
                    btnPlay.setButtonDrawable(getResources().getDrawable(R.drawable.ic_pause_detail));
                } else if (record.status.equals("PAUSE")) {
                    ACTION = "RESUME";
                    record.play();
                    btnPlay.setButtonDrawable(getResources().getDrawable(R.drawable.ic_pause_detail));
                }else if (record.status.equals("PLAY")) {
                    ACTION = "PAUSE";
                    record.pause();
                    btnPlay.setButtonDrawable(getResources().getDrawable(R.drawable.ic_play_detail));
                }
                handleService(context,PlayBackground.class,ACTION,curRecordSource);

            }
        });
        btnMute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                if(!record.status.equals("PLAY") && !record.status.equals("RESUME")) return;
                if (!ACTION.equals("MUTE")) {
                    ACTION="MUTE";
                    btnPlay.setButtonDrawable(getResources().getDrawable(R.drawable.ic_pause_detail));
                    handleService(context,PlayBackground.class,ACTION,curRecordSource);

                } else {
                    ACTION= "RESUME";
                    btnPlay.setButtonDrawable(getResources().getDrawable(R.drawable.ic_pause_detail));
                    handleService(context,PlayBackground.class,"UNMUTE",curRecordSource);
                    handleService(context,PlayBackground.class,ACTION,curRecordSource);

                }
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        Context context = getApplicationContext();
        handleService(context,PlayBackground.class,"STOP",curRecordSource);

    }

    public void switchActivity(Context context, Class nextActivity){
        Intent intent = new Intent(context, nextActivity);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);//dong nay quan trong
        startActivityIfNeeded(intent, 0);
    }
    public void handleService(Context context, Class nextActivity, String action, String nameRecord) {

        Intent playbackIntent = new Intent(context, nextActivity);

        playbackIntent.putExtra("ACTION", action);
        playbackIntent.putExtra("source", nameRecord);
        playbackIntent.putExtra("id", curRecordId);
        if (action.equals("PLAY") || action.equals("PAUSE")|| action.equals("MUTE")|| action.equals("UNMUTE") ) {
            context.startService(playbackIntent);
        } else if (action.equals("STOP")) {
            context.stopService(playbackIntent);
        } else if (action.equals("RESUME")) {
            context.startService(playbackIntent);
        }
    }
}


//đủ phân này r