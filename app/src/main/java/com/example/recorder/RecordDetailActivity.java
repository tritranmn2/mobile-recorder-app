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
import android.widget.Toast;


public class RecordDetailActivity extends Activity {
    ImageView btn_back;
    MediaPlayer RecordPlayer;
    TextView record_name;
    String ChosenRecord = "song1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_detail_record);
        btn_back = (ImageView) findViewById(R.id.return_button);
        record_name = (TextView) findViewById(R.id.record_name);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                onRestart();
                switchActivity(context,ListRecord.class);
            }
        });


        record_name.setText("song1");

    }
    public void switchActivity(Context context, Class nextActivity){
        Intent intent = new Intent(context, nextActivity);
        context.startActivity(intent);
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
            Toast.makeText(this,"STOPPED",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopPlayer();
    }
}


//đủ phân này r