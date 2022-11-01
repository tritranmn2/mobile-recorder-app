package com.example.recorder;

import android.app.Activity;
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

public class ListRecord extends Activity {

    private ListView listView;
    public ProgressBar progressBar;
    public ImageView img;
    Button btn;
    String[] items = {"Record 01", "Record 02", "Record 03"};
    String[] items1 = {"00:00:01", "00:00:02", "00:00:03"};
    String[] items2 = {"01Th01", "02Th02", "03Th03"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_record);



        listView = (ListView) findViewById(R.id.list_view);
        progressBar = (ProgressBar) findViewById(R.id.list_item_progress);
        btn = (Button) findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });

        DataAdapterRCList adapter = new DataAdapterRCList(this, items, items1, items2);
        listView.setAdapter(adapter);

        adapter.notifyDataSetChanged();
    }

    void addItem() {

    }
}

