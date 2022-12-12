package com.example.recorder;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class RecordDetailActivity extends Activity {
    ImageView btn_back;
    TextView tvName;
    TextView tvRcRuntime;
    int id;
//     static InitDatabase database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_detail_record);
        btn_back = (ImageView) findViewById(R.id.return_button);
        tvName = (TextView) findViewById(R.id.record_name);
        tvRcRuntime = (TextView) findViewById(R.id.record_runtime);
        Bundle extras = getIntent().getExtras(); //lay id truyen tu main qua

        if (extras != null) {
             id = extras.getInt("id");
        }
        Record record= ListRecord.getRecord(id);
        tvName.setText(record.name);
        tvRcRuntime.setText(record.lenght);




        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
//                onRestart();

                switchActivity(context,ListRecord.class);
            }
        });

    }
    public void switchActivity(Context context, Class nextActivity){
        Intent intent = new Intent(context, nextActivity);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivityIfNeeded(intent, 0);
//        context.startActivity(intent);
    }

}

//đủ phân này r