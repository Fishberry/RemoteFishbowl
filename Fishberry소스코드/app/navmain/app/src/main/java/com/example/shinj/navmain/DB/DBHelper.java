package com.example.shinj.navmain.DB;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    public DBHelper(Context context) {
        super(context, "Noti.db", null, 1 );
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE NotificationInformation (ip TEXT, isChecking INT, watchElement INT, temperLoopTime INT, pHLoopTime INT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public void insert(String ip, int isChecking) {
        SQLiteDatabase db = getWritableDatabase();

        db.execSQL("INSERT INTO NotificationInformation VALUES ('" + ip + "', " + isChecking + ", 0, 0, 0);");
        db.close();
    }

    public void logoutUpdate(int isChecking) {
        SQLiteDatabase db = getWritableDatabase();

        db.execSQL("UPDATE NotificationInformation SET isChecking=" + isChecking + ";");
        db.close();
    }

    public void update(String ip, int isChecking) {
        SQLiteDatabase db = getWritableDatabase();

        db.execSQL("UPDATE NotificationInformation SET ip='" + ip + "', isChecking=" + isChecking + ";");
        db.close();
    }

    public void updateNotification(int watchElement, int temperLoopTime, int pHLoopTime) {
        SQLiteDatabase db = getWritableDatabase();

        db.execSQL("UPDATE NotificationInformation SET watchElement=" + watchElement + ", temperLoopTime=" + temperLoopTime + ", pHLoopTime=" + pHLoopTime + ";");
        db.close();
    }

    public void delete() {
        SQLiteDatabase db = getWritableDatabase();

        db.execSQL("DELETE FROM NotificationInformation");
        db.close();
    }

    public DBElement getResult() {
        SQLiteDatabase db = getReadableDatabase();
        DBElement dbElement = new DBElement();
        final String Nothing = "NoValue";

        Cursor cursor = db.rawQuery("SELECT * FROM NotificationInformation", null);

        try {
            cursor.moveToNext();
            dbElement.setIp(cursor.getString(0));
            dbElement.setIsRememberIP(cursor.getInt(1));
            dbElement.setWatchElement(cursor.getInt(2));
            dbElement.setTemperLoopTime(cursor.getInt(3));
            dbElement.setpHLoopTime(cursor.getInt(4));
        } catch (Exception e) {}

        if (dbElement.getIp().equals("")) {
            dbElement.setIp("NoValue");
            return dbElement;
        }

        return dbElement;
    }
}
