package com.example.recorder;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

//chương trình chạy đầu tiên sẽ chạy file này (cày đặt mặc định trong manifest)
public class ListRecord extends Activity {

    Activity ac = this;
    DataAdapterRCList adapter;
    private ListView listView;
    ImageView btn_rc;
    List<Record> items = new ArrayList<Record>();

    //database
    static InitDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toast.makeText(this, "OnCreate 1", Toast.LENGTH_SHORT).show();

        database = new InitDatabase(this, "data.sqlite", null, 1);
        displayAllRecords();

        listView = (ListView) findViewById(R.id.list_rc);
        adapter = new DataAdapterRCList(this, items);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        //Load danh sach nhac
        addItem("song1","00:03:04","");
        addItem("song2","00:03:52","");

        btn_rc = (ImageView) findViewById(R.id.btn_rc);

        btn_rc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = "";
                String time_record = "";
                String source = "";
                addItem(name, time_record,source);
            }
        });
    }


    @Override protected void onPause() {
        Toast.makeText(this, "OnPause 1", Toast.LENGTH_SHORT).show();
        super.onPause();
    }

    @Override protected void onResume() {
        Toast.makeText(this, "OnResume 1", Toast.LENGTH_SHORT).show();
        super.onResume();

    }

    @Override protected void onRestart() {
        Toast.makeText(this, "OnRestart 1", Toast.LENGTH_SHORT).show();
        super.onRestart();
    }

    @Override protected void onDestroy() {
        Toast.makeText(this, "OnDestroy 1", Toast.LENGTH_SHORT).show();
        super.onDestroy();
    }

    @Override protected void onStop() {
        Toast.makeText(this, "OnStop 1", Toast.LENGTH_SHORT).show();
        super.onStop();
    }

    void addItem(String fileName, String time_record, String source) {
        if (fileName == ""){
            Integer length = items.size() + 1;
            fileName = "Record-" + String.valueOf(length);
        }

        Record record = new Record(fileName,time_record,source);

        createRecord(record);

        Record recordDb = getRecord(record.name);
        items.add(recordDb);

        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    void createRecord(Record record){
        database.insertRecord(record);

        NotificationCompat.Builder builder= new NotificationCompat.Builder(ListRecord.this,"record notification");
        builder.setContentTitle("Add new record");
        builder.setContentText(record.name +" is added");
        builder.setSmallIcon(R.drawable.playbutton);
        builder.setAutoCancel(true);
        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(ListRecord.this);
        managerCompat.notify(1,builder.build());

    }

    void DeleteRecord(int id){
        String query ="DELETE FROM Records where id= '"+ String.valueOf(id) +"'";
        database.QueryData(query);
    }

    Cursor getAllRecords() {
        Cursor data = database.GetData("SELECT * FROM Records" );
        return data;
    }

    Record getRecord(String name) {
        Cursor data = database.GetData("SELECT * FROM Records WHERE name = '" + name.trim() + "'");
        data.moveToNext();
        int id = data.getInt(0);
        String date = data.getString(2);
        String time = data.getString(3);
        String source = data.getString(4);
        Record record = new Record(name, date, time, source);
        return  record;
    }

    static Record getRecord(int id) {
        Cursor data = database.GetData("SELECT * FROM Records WHERE id = '" + String.valueOf(id) + "'");
        data.moveToNext();
        String name = data.getString(1);
        String date = data.getString(2);
        String time = data.getString(3);
        String source = data.getString(4);
        Record record = new Record(name, date, time, source);
        return  record;
    }

    void displayAllRecords() {
        Cursor data = database.GetData("SELECT * FROM Records");
        while(data.moveToNext()) {
            Integer id = data.getInt(0);
            String name = data.getString(1);
            String date = data.getString(2);
            String time_record = data.getString(3);
            String source = data.getString(4);
            Record record = new Record(name, date, time_record, source);
            items.add(record);
        }
    }
}


