package com.example.recorder;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

//chương trình chạy đầu tiên sẽ chạy file này (cày đặt mặc định trong manifest)
public class ListRecord extends Activity {
    Activity ac = this;
    DataAdapterRCList adapter;
    private ListView listView;
    public ProgressBar progressBar;
    public ImageView img;
    ImageView btn;
    List<String> items_title = new ArrayList<String>();
    List<String> items_length = new ArrayList<String>();
    List<String> items_date = new ArrayList<String>();
//    name

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView) findViewById(R.id.list_rc);
        progressBar = (ProgressBar) findViewById(R.id.list_item_progress);

        adapter = new DataAdapterRCList(this, items_title, items_length, items_date);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        btn = (ImageView) findViewById(R.id.btn_rc);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = "";
                String time_record = "";
                String date = "";

                //do something, set name, time_record, date

                addItem(name, time_record, date);
            }
        });
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

