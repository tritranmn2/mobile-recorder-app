package com.example.recorder;

import java.sql.Time;

public class MyTime {
    private int hour;
    private int minute;
    private int second;
    public MyTime(int seconds) {
        this.hour = seconds / 3600;
        this.minute = (seconds % 3600)/60;
        this.second = seconds % 60;
    }
    public String toString() {
        return MyTime.valueOf(this.hour) + ":" + MyTime.valueOf(this.minute) + ":" +
                MyTime.valueOf(this.second);
    }


    public int toSeconds() {
        return this.hour * 3600 + this.minute * 60 + this.second;
    }
    public MyTime increase(){
        int seconds = this.toSeconds();
        seconds += 1;
        return new MyTime(seconds);
    }
//    this function doesn't test yet
    public static MyTime valueOf(String strTime) {
        Time t = Time.valueOf(strTime);
        int secs = t.getHours() * 3600 + t.getMinutes() * 60 + t.getSeconds();
        return  new MyTime(secs) ;
    }

    public static String valueOf(int number) {
        if(number<10) return "0"+String.valueOf(number);
        return String.valueOf(number);
    }
}
