package com.example.recorder;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

//chương trình chạy đầu tiên sẽ chạy file này (cày đặt mặc định trong manifest)
public class ListRecord extends Activity {
    private static final Integer REQUEST_AUDIO_PERMISSION_CODE = 101;
    Recording recording;
    DataAdapterRCList adapter;
    private ListView listView;
    ImageView btn_rc;
    public static boolean isRecording = false;
    public static BroadcastReceiver receiver;
    public static Intent intentServiceRecord;

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
//        addItem("song1","00:03:04","");
//        addItem("song2","00:03:52","");

        btn_rc = (ImageView) findViewById(R.id.btn_rc);
//        btn_rc = (ImageView) findViewById(R.id.btn_rc);


        intentServiceRecord = new Intent(this, ServiceRecord.class);
        IntentFilter mainFilter = new IntentFilter("SendRecord");
        receiver = new MyRecordReceiver();
        registerReceiver(receiver, mainFilter);

        btn_rc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String fileName = "";
                Integer size = items.size() + 1;
                fileName = "Record-" + String.valueOf(size);

                if (isRecording == false) {
                    if (!checkRecordingPermission()) {
                        requestRecordingPermission();
                    }
                    isRecording=true;
                    intentServiceRecord.putExtra("nameFile",fileName);
                    startService(intentServiceRecord);
                } else {
                    isRecording=false;
                    stopService(intentServiceRecord);
                }
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

    public void addItem( String fileName,String time_record, String source) {
//        Integer i = items.size() + 1;
//        fileName = "Record " + String.valueOf(i);

        Record record = new Record(fileName,time_record,source);

        database.insertRecord(record);
//        createRecord(record);

        Record recordDb = getRecord(record.name);
        items.add(recordDb);

        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
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

    public  class MyRecordReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String name = intent.getStringExtra("name");
            String date = intent.getStringExtra("date");
            String length = intent.getStringExtra("length");
            String source = intent.getStringExtra("source");
            addItem(name,length,source);
        }
    }

    private void requestRecordingPermission(){
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO},REQUEST_AUDIO_PERMISSION_CODE);
    }

    public Boolean checkRecordingPermission(){
        if(ContextCompat.checkSelfPermission(this,Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_DENIED){
            requestRecordingPermission();
            return false;
        }
        return true;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==REQUEST_AUDIO_PERMISSION_CODE){
            if(grantResults.length>0){
                Boolean permissionToRecord = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                if(permissionToRecord){
                    System.out.println("Permission Given");
                    Toast.makeText(this,"Permission Given", Toast.LENGTH_SHORT).show();
                }
                else {
                    System.out.println("Permission Denied");
                    Toast.makeText(this,"Permission Denied", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}


