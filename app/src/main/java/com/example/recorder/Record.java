package com.example.recorder;

import java.sql.Date;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Record {
    String name;
    //    Date dateSave;
//    Time lenght;
    String dateSave;
    String lenght;
    String status = "STOP";
    String source;
    final String modifierSource = "R.raw.";
    final String modifierDate = "2022-12-12";
    final String modifierTime = "00:32:00";

    public Record() {
        this.name = "default";
        this.dateSave = modifierDate;
        this.lenght = modifierTime;
        this.source = modifierSource;
    }

    public Record(String name, String dateSave, String lenght, String source) {
        this.name = name;
        this.dateSave = dateSave;
        this.lenght = lenght;
        this.source = source;
    }

    public Record(String name, String lenght, String source) {

        this.name = name;
        this.dateSave = modifierDate; //sẽ thay đổi khi xuống dataBase
        this.lenght = lenght;
        this.source = source;
    }
    public void play() {
        status = "PLAY";
    }
    public void pause() {
        status = "PAUSE";
    }
    public void stop() {
        status = "STOP";
    }
    public void resume() {
        status = "RESUME";
    }

}
