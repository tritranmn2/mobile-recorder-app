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
//    private List<String> items_title;
//    private List<String> items_length;
//    private List<String> items_date;
    private List<Record> records;
    private ProgressBar[] progressBar;
    public Intent playbackIntent;
    private boolean isServiceRunning =false;

//    public DataAdapterRCList(Activity activity, List<String> title, List<String> length, List<String> date) {
//        this.activity = activity;
//        this.items_title = title;
//        this.items_length = length;
//        this.items_date = date;
//    }
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

        //Bật tắt nút play và pasuse
        ImageView btn_item = (ImageView) view.findViewById(R.id.list_item_img);

        btn_item.setOnClickListener(new View.OnClickListener() {
            int flag = 1;
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                ImageView v1= (ImageView) v;
                final Bitmap bmap = ((BitmapDrawable)v1.getDrawable()).getBitmap();
                Drawable myDrawable = context.getDrawable(R.drawable.playbutton);
                final Bitmap myLogo = ((BitmapDrawable) myDrawable).getBitmap();
                if (bmap.sameAs(myLogo)) {
                    if (isServiceRunning) {
                        context.stopService(playbackIntent);
                    }
                    btn_item.setImageResource(R.drawable.pause);
                    progressBar.setVisibility(View.VISIBLE);
                    playbackIntent = new Intent(context,PlayBackground.class);

                    context.startService(playbackIntent);
                    isServiceRunning=true;
                } else {
                    btn_item.setImageResource(R.drawable.playbutton);
                    progressBar.setVisibility(View.VISIBLE);
                    context.stopService(playbackIntent);
                }

            }
        });

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                switchActivity(context,RecordDetailActivity.class,i);
            }

        });

        // Trả về view kết quả.
        return view;
    }
    public void switchActivity(Context context, Class nextActivity,int id){
        Intent intent = new Intent(context, nextActivity);
        intent.putExtra("id", id);
        context.startActivity(intent);
    }



}