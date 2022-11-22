package com.example.recorder;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class RecordDetailActivity extends Activity {
    ImageView btn_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_detail_record);
        btn_back = (ImageView) findViewById(R.id.return_button);

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                onRestart();
                switchActivity(context,ListRecord.class);
            }
        });

    }
    public void switchActivity(Context context, Class nextActivity){
        Intent intent = new Intent(context, nextActivity);
        context.startActivity(intent);
    }

}

//đủ phân này r