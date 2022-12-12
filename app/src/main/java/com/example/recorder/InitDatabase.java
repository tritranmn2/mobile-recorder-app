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
        this.QueryData("INSERT INTO Records(id, name, dateSave, length) VALUES(null, 'datarecord01', datetime('now'), time(datetime('now')))");
        this.QueryData("INSERT INTO Records(id, name, dateSave, length) VALUES(null, 'datarecord02', datetime('now'), time(datetime('now')))");
        this.QueryData("INSERT INTO Records(id, name, dateSave, length) VALUES(null, 'datarecord03', datetime('now'), time(datetime('now')))");

    }

    public void QueryData(String sql) {
        SQLiteDatabase database = getWritableDatabase();
        database.execSQL(sql);
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
