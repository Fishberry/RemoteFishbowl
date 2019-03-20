package com.example.shinj.navmain;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    public DBHelper(Context context) {
        super(context, "IP.db", null, 1 );
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE IpInformation (ip TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public void insert(String ip) {
        SQLiteDatabase db = getWritableDatabase();

        db.execSQL("INSERT INTO IpInformation values('" + ip + "');");
        db.close();
    }

    public void update(String ip) {
        SQLiteDatabase db = getWritableDatabase();

        db.execSQL("DELETE FROM IpInformation");
        db.execSQL("INSERT INTO IpInformation values('" + ip + "');");
        db.close();
    }

    public void delete() {
        SQLiteDatabase db = getWritableDatabase();

        db.execSQL("DELETE FROM IpInformation");
        db.close();
    }

    public String getResult() {
        SQLiteDatabase db = getReadableDatabase();
        String result = "";
        final String Nothing = "NoValue";

        Cursor cursor = db.rawQuery("SELECT * FROM IpInformation", null);

        while (cursor.moveToNext()) {
            result += cursor.getString(0);
        }

        if (result.equals(""))
            return Nothing;

        return result;
    }
}
