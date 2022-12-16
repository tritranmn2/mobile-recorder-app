package com.example.recorder;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

//chương trình chạy đầu tiên sẽ chạy file này (cày đặt mặc định trong manifest)
public class ListRecord extends Activity {
    Activity ac = this;
    DataAdapterRCList adapter;
    Recording recording;
    private ListView listView;
    public ProgressBar progressBar;
    public ImageView img;
    ImageView btn_rc;
    ImageView btn_search;
    List<String> items_title = new ArrayList<String>();
    List<String> items_length = new ArrayList<String>();
    List<String> items_date = new ArrayList<String>();
//    name

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toast.makeText(this, "OnCreated", Toast.LENGTH_SHORT).show();

        listView = (ListView) findViewById(R.id.list_rc);
//        progressBar = (ProgressBar) findViewById(R.id.list_item_progress);

        adapter = new DataAdapterRCList(this, items_title, items_length, items_date);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        btn_rc = (ImageView) findViewById(R.id.btn_rc);
        btn_search = (ImageView) findViewById(R.id.search);
        recording = new Recording(ac);
        btn_rc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = "";
                String time_record = "";
                String date = "";

//                do something, set name, time_record, date

                recording.Record_Stop("TestFile");

//                addItem(name, time_record, date);

            }
        });
        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recording.Play("TestFile");
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

    void addItem(String fileName, String time_record, String date) {
        if (fileName == ""){
            Integer length = items_title.size() + 1;
            fileName = "Record " + length;
        }
        if (time_record == ""){
            time_record = "00:00:01";
        }
        if (date == ""){
            date = "01Th01";
        }
        items_title.add(fileName);
        items_length.add(time_record);
        items_date.add(date);

        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }



}


