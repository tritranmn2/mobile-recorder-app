package com.example.recorder;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import android.content.IntentFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class DataAdapterRCList extends BaseAdapter {

    private Activity activity;
    private List<Record> records;
    static public int idOld = -1;
    static public int idOld2 = -1;
    public  DataAdapterRCList adapter =this;
    public static  BroadcastReceiver receiver;
    public  SeekBar sbCurTime;
    public DataAdapterRCList(Activity activity, List<Record> records ) {
        this.activity = activity;
        this.records = records;
    }

    @Override public int getCount() {
        return records.size();
    }

    @Override public Record getItem(int i) {
        return records.get(i);
    }

    @Override public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        IntentFilter receiveCurTimePlayFilter = new IntentFilter("SendCurTimePlay");
        receiver = new MyCurTimeReceiver();
        activity.registerReceiver(receiver, receiveCurTimePlayFilter);

        // Gọi layoutInflater ra để bắt đầu ánh xạ view và data.
        LayoutInflater inflater = activity.getLayoutInflater();
        view = inflater.inflate(R.layout.list_rc_item, null);

        TextView tvName = (TextView) view.findViewById(R.id.list_item_title);
        tvName.setText(records.get(i).name);
        TextView tvLength = (TextView) view.findViewById(R.id.list_item_length);
        tvLength.setText(records.get(i).lenght);
        TextView tvDate = (TextView) view.findViewById(R.id.list_item_date);
        tvDate.setText(records.get(i).dateSave);
        sbCurTime = (SeekBar) view.findViewById(R.id.list_item_progress);
        sbCurTime.setMax(MyTime.toSeconds(records.get(i).lenght));
        sbCurTime.setVisibility(View.INVISIBLE);

        ImageView btn_item=(ImageView) view.findViewById(R.id.item_play_btn);
        if (records.get(i).status.equals("STOP")) {
            btn_item.setImageResource(R.drawable.playbutton);
        } else if (records.get(i).status.equals("PAUSE")) {
            btn_item.setImageResource(R.drawable.playbutton);
            sbCurTime.setVisibility(View.VISIBLE);
        } else {
            btn_item.setImageResource(R.drawable.pause);
            sbCurTime.setVisibility(View.VISIBLE);
        }
        btn_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                String ACTION = "";
                String sourceRecord = records.get(i).source;
                if (records.get(i).status.equals("STOP")) {
                    btn_item.setImageResource(R.drawable.pause); //set sang nut pause
                    sbCurTime.setVisibility(View.VISIBLE);
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
                handleService(context,PlayBackground.class,ACTION,sourceRecord,i);
                idOld = i;
            }
        });

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Chỗ này là set cho khi chuyển sang detail thì các item chuyển
                //về trạng thái stop
                records.get(i).stop();
                if (idOld != -1) {
                    if (records.get(idOld).status.equals("PLAY")) {
                        records.get(idOld).stop();
                    }
                }
                adapter.notifyDataSetChanged();
                Context context = v.getContext();
                switchDetailActivity(context,RecordDetailActivity.class,i);
            }
        });
        return view;
    }

    public void switchDetailActivity(Context context, Class nextActivity,int id){
        Intent intent = new Intent(context, nextActivity);
        intent.putExtra("id", id);
        context.startActivity(intent);
    }


    public void handleService(Context context, Class nextActivity, String action, String sourceRecord, int i) {
        Intent playbackIntent = new Intent(context, nextActivity);
        playbackIntent.putExtra("ACTION", action);
        playbackIntent.putExtra("source", sourceRecord);
        if (action.equals("PLAY") || action.equals("PAUSE") ) {
            context.startService(playbackIntent);
        } else if (action.equals("STOP")) {
            context.stopService(playbackIntent);
        } else if (action.equals("RESUME")) {
            context.startService(playbackIntent);
            records.get(i).play();
        }
    }

//    nhận lenght(int) ở recorder + curTime(int) ở player
    public  class MyCurTimeReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            switch (action) {
                case "SendCurTimePlay":
                    int curTime = intent.getIntExtra("curTime",0);
                    sbCurTime.setProgress(curTime);
                    System.out.println("reciever:"+ String.valueOf(curTime));
                    break;

                default:
                    break;
            }

        }
    }

}