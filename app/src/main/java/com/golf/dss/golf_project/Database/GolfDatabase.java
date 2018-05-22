package com.golf.dss.golf_project.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;

import com.golf.dss.golf_project.Classes.User;

public class GolfDatabase extends SQLiteOpenHelper{

    //General informations
    private static final String DATABASE_NAME = "GolfDB.db";
    private static final int DATABASE_VERSION = 1;


    //Tables name
    private static final String USER_TABLE_NAME = "USER";

    //Tables columns names
    //=> USER
    private static final String USER_ID = "ID";
    private static final String USER_FIRSTNAME = "FIRSTNAME";
    private static final String USER_AGE = "AGE";
    private static final String USER_GENDER = "GENDER";
    private static final String USER_HEIGHT = "HEIGHT";
    private static final String USER_WEIGHT = "WEIGHT";
    private static final String USER_LEVEL = "LEVEL";
    private static final String USER_STYLE = "STYLE";
    private static final String USER_TRAINING_FREQUENCY = "TRAINING_FREQUENCY";
    private static final String USER_EXPERIENCE_TIME = "EXPERIENCE_TIME";

    //Creation string
    private static final String DATABASE_CREATE_USER = "CREATE TABLE " + USER_TABLE_NAME + "("
            + USER_ID + " integer primary key autoincrement,"
            + USER_FIRSTNAME + " text not null,"
            + USER_AGE + " text not null,"
            + USER_GENDER + " text not null,"
            + USER_HEIGHT + " text not null,"
            + USER_WEIGHT + " text not null,"
            + USER_LEVEL + " text not null,"
            + USER_STYLE + " text not null,"
            + USER_TRAINING_FREQUENCY + " text not null,"
            + USER_EXPERIENCE_TIME + " text not null"
            + ");";

    private static GolfDatabase mInstance;
    private String[] allColumnsUser;

    static {
        mInstance = null;
    }


    private GolfDatabase(Context context) {
        super(context, Environment.getExternalStorageDirectory()+"/Android/data/com.golf.dss.golf_project/files/"+DATABASE_NAME, null, DATABASE_VERSION);
        this.allColumnsUser = new String[]{USER_ID, USER_FIRSTNAME, USER_AGE, USER_GENDER, USER_HEIGHT, USER_WEIGHT, USER_LEVEL, USER_STYLE, USER_TRAINING_FREQUENCY, USER_EXPERIENCE_TIME};
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE_USER);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        Log.w(GolfDatabase.class.getName(), "Upgrading database from version " + oldVersion + " to " + newVersion + ", which will destroy all old data");
        database.execSQL("DROP TABLE IF EXISTS " + USER_TABLE_NAME);
        onCreate(database);
    }

    /*------------------------------------------------------ USER METHODS ------------------------------------------------------*/

    public void insertConnectedUser(User user) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(USER_FIRSTNAME, user.getFirstname());
        values.put(USER_AGE, user.getAge());
        values.put(USER_GENDER, user.getGender());
        values.put(USER_HEIGHT, user.getHeight());
        values.put(USER_WEIGHT, user.getWeight());
        values.put(USER_LEVEL, user.getLevel());
        values.put(USER_STYLE, user.getStyle());
        values.put(USER_TRAINING_FREQUENCY, user.getFrequency());
        values.put(USER_EXPERIENCE_TIME, user.getExpTime());
        db.insert(USER_TABLE_NAME, null, values);
    }

    public User getConnectedUser() {
        User user = null;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(USER_TABLE_NAME, this.allColumnsUser, null, null, null, null, null);
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            user = new User(
                    cursor.getString(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getString(4),
                    cursor.getString(5),
                    cursor.getString(6),
                    cursor.getString(7),
                    cursor.getString(8)
            );
        }
        cursor.close();
        return user;
    }

    /*------------------------------------------------------ TOOL METHODS ------------------------------------------------------*/
    public void deleteAllData() {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(USER_TABLE_NAME, null, null);
    }

    public static GolfDatabase getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new GolfDatabase(context.getApplicationContext());
        }
        return mInstance;
    }
}
