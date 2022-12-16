package com.example.recorder;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class InitDatabase extends SQLiteOpenHelper {
    public InitDatabase(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        this.QueryData("DROP TABLE IF EXISTS Records");
        this.QueryData("CREATE TABLE IF NOT EXISTS Records(id INT, name VARCHAR(255), dateSave TEXT, length TEXT,source TEXT)");

    }

    public void QueryData(String sql) {
        SQLiteDatabase database = getWritableDatabase();
        database.execSQL(sql);
    }
    void insertRecord(Record record){
        Cursor data = this.GetData("SELECT MAX(id) FROM Records");
        data.moveToNext();
        int maxId = data.getInt(0);
        int id = maxId +1;

        if (maxId == 0) {
            Cursor check = this.GetData("SELECT id FROM Records");
            check.moveToNext();
            int countIds = check.getCount();
            if (countIds == 0) {
                id=0; //ptu dau tien
            }
        }

        this.QueryData("INSERT INTO Records(id, name, dateSave, length) VALUES(" +
                String.valueOf(id) + ", '" + record.name +"', datetime('now'),'" + record.lenght +"')");
    }


    public Cursor GetData(String sql) {
        SQLiteDatabase database = getReadableDatabase();
        return database.rawQuery(sql, null);
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
