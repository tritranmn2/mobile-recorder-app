package com.example.recorder;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class ChangeIcon extends Activity {

    ImageView btnTest;
    private boolean isResume;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnTest = findViewById(R.id.btn_rc);
        btnTest.setOnClickListener(new View.OnClickListener() {
                                       @SuppressLint("UseCompatLoadingForDrawables")
                                       @Override
                                       public void onClick(View view) {
                                           if(!isResume)
                                           {
                                               isResume = true;
                                               btnTest.setImageDrawable(getResources().getDrawable(R.drawable.ic_audio));

                                           }
                                           else
                                           {
                                               isResume =false;
                                               btnTest.setImageDrawable(getResources().getDrawable(R.drawable.ic_end_record));

                                           }
                                       }
                                   }

        );

    }
}
