package com.example.recorder;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class DataAdapterRCList extends BaseAdapter {

    private Activity activity;
    private List<Record> records;
//    private ProgressBar[] progressBar;
    private boolean isServiceRunning =false;
    static public int idOld = -1;
    static public int idOld2 = -1;
    static public View viewOld ;
    public  DataAdapterRCList adapter =this;

    public DataAdapterRCList(Activity activity, List<Record> records ) {
        this.activity = activity;
        this.records = records;
    }
    @Override
    public int getCount() {
        return records.size();
    }

    @Override
    public Object getItem(int i) {
        return records.get(i).name;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }


//    public void updateData(int i) {
//        if (records.get(i).status.equals("STOP")) {
//            int a =0;
//        }
//       adapter.notifyDataSetChanged();
//
//    }




    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        // Gọi layoutInflater ra để bắt đầu ánh xạ view và data.
        LayoutInflater inflater = activity.getLayoutInflater();

        // Dùng inflater để gắn giao diện list_rc_item vào biến view
        view = inflater.inflate(R.layout.list_rc_item, null);

        // set tên từng record
        TextView tvName = (TextView) view.findViewById(R.id.list_item_title);
        tvName.setText(records.get(i).name);

        //Set độ dài từng record
        TextView tvName1 = (TextView) view.findViewById(R.id.list_item_length);
        tvName1.setText(records.get(i).lenght);

        //set ngày từng record
        TextView tvName2 = (TextView) view.findViewById(R.id.list_item_date);
        tvName2.setText(records.get(i).dateSave);

        //set ẩn cái thanh thời gian của từng record
        ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.list_item_progress);
        progressBar.setVisibility(View.INVISIBLE);
        ImageView btn_item=(ImageView) view.findViewById(R.id.item_play_btn);
        if (records.get(i).status.equals("STOP")) {
            btn_item.setImageResource(R.drawable.playbutton);
        } else {
            btn_item.setImageResource(R.drawable.pause);
            progressBar.setVisibility(View.VISIBLE);
        }

        btn_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
//                Intent playbackIntent = new Intent(context,PlayBackground.class);
                String ACTION = "";
                String nameRecord = records.get(i).name;
                if (records.get(i).status.equals("STOP")) {
                    btn_item.setImageResource(R.drawable.pause); //set sang nut pause
                    progressBar.setVisibility(View.VISIBLE);
                    records.get(i).play();//play lan dau tien
                    if (idOld != -1) {
                        if (i != idOld) {
                            records.get(idOld).stop();
                            adapter.notifyDataSetChanged();
                        }
                    }
                } else if (records.get(i).status.equals("PLAY")) {
                    if (idOld != -1) {
                        if (i == idOld) {
                            btn_item.setImageResource(R.drawable.playbutton);
                            records.get(i).pause();
                            idOld2 = idOld;
                        } else {
                            records.get(i).stop();
                        }
                    }
                } else if (records.get(i).status.equals("PAUSE")) {
                    if (idOld2 != -1 && idOld2 == i) {
                        btn_item.setImageResource(R.drawable.pause);
                        records.get(i).resume();
                    }
                }
                ACTION = records.get(i).status;
//                ImageView v1= (ImageView) v;
//                final Bitmap bmapCurrent = ((BitmapDrawable)v1.getDrawable()).getBitmap();
//                Drawable myDrawable = context.getDrawable(R.drawable.playbutton);
//                final Bitmap bmapPlayButton = ((BitmapDrawable) myDrawable).getBitmap();
//                if(bmapCurrent.sameAs(bmapPlayButton)){//chưa phát nhạc
//                    btn_item.setImageResource(R.drawable.pause); //set sang nut pause
//                    progressBar.setVisibility(View.VISIBLE);
//                    ACTION = "PLAY";
//
//                }else{
//                    btn_item.setImageResource(R.drawable.playbutton);
//                    ACTION = "STOP";
//                }
                handleService(context,PlayBackground.class,ACTION,nameRecord,i);
                idOld = i;

            }
        });

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                switchDetailActivity(context,RecordDetailActivity.class,i);
            }

        });

        // Trả về view kết quả.
        return view;
    }

    public void switchDetailActivity(Context context, Class nextActivity,int id){
        Intent intent = new Intent(context, nextActivity);
        intent.putExtra("id", id);
        context.startActivity(intent);
    }

    public void handleService(Context context, Class nextActivity, String action, String nameRecord, int i) {

        Intent playbackIntent = new Intent(context, nextActivity);

        playbackIntent.putExtra("ACTION", action);
        playbackIntent.putExtra("name", nameRecord);
        if (action.equals("PLAY") || action.equals("PAUSE") ) {
            context.startService(playbackIntent);
        } else if (action.equals("STOP")) {
            context.stopService(playbackIntent);
//            context.
        } else if (action.equals("RESUME")) {
            context.startService(playbackIntent);
            records.get(i).play();
        }
    }



}