package com.example.recorder;
import java.io.Serializable;
//Chưa đụng đến class Record
public class Record implements Serializable {
    public String items_title;
    public String items_length;
    public String items_date;
    Record(String items_title,String items_length,String items_date) {
        this.items_title = items_title;
        this.items_length = items_length;
        this.items_date = items_date;

    }
    public String getTitle(){
        return this.items_title;
    }
}
