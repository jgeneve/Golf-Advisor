package com.golf.dss.golf_project.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;

import com.golf.dss.golf_project.Classes.Club;
import com.golf.dss.golf_project.Classes.User;

import java.util.ArrayList;

public class GolfDatabase extends SQLiteOpenHelper{

    //General informations
    private static final String DATABASE_NAME = "GolfDB.db";
    private static final int DATABASE_VERSION = 1;


    //Tables name
    private static final String USER_TABLE_NAME = "USER";
    private static final String CLUB_TABLE_NAME = "CLUB";

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
    //=> CLUB
    private static final String CLUB_ID = "ID";
    private static final String CLUB_NAME = "NAME";
    private static final String CLUB_MEN_MIN_DISTANCE = "MEN_MIN_DISTANCE";
    private static final String CLUB_MEN_MAX_DISTANCE = "MEN_MAX_DISTANCE";
    private static final String CLUB_MEN_AVG_DISTANCE = "MEN_AVG_DISTANCE";
    private static final String CLUB_WOMEN_MIN_DISTANCE = "WOMEN_MIN_DISTANCE";
    private static final String CLUB_WOMEN_MAX_DISTANCE = "WOMEN_MAX_DISTANCE";
    private static final String CLUB_WOMEN_AVG_DISTANCE = "WOMEN_AVG_DISTANCE";
    private static final String CLUB_USER_AVG_DISTANCE = "WOMEN_AVG_DISTANCE";
    private static final String CLUB_USER_NB_USE = "WOMEN_AVG_DISTANCE";

    //Creation string
    //=> USER
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
    //=> CLUB
    private static final String DATABASE_CREATE_CLUB = "CREATE TABLE " + CLUB_TABLE_NAME + "("
            + CLUB_ID + " integer primary key autoincrement,"
            + CLUB_NAME + " text not null,"
            + CLUB_MEN_MIN_DISTANCE + " integer not null,"
            + CLUB_MEN_MAX_DISTANCE + " integer not null,"
            + CLUB_MEN_AVG_DISTANCE + " integer not null,"
            + CLUB_WOMEN_MIN_DISTANCE + " integer not null,"
            + CLUB_WOMEN_MAX_DISTANCE + " integer not null,"
            + CLUB_WOMEN_AVG_DISTANCE + " integer not null,"
            + CLUB_USER_AVG_DISTANCE + " integer not null,"
            + CLUB_USER_NB_USE + " integer not null"
            + ");";

    private static GolfDatabase mInstance;
    private String[] allColumnsUser;
    private String[] allColumnsClub;

    static {
        mInstance = null;
    }


    private GolfDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.allColumnsUser = new String[]{
                USER_FIRSTNAME,
                USER_AGE,
                USER_GENDER,
                USER_HEIGHT,
                USER_WEIGHT,
                USER_LEVEL,
                USER_STYLE,
                USER_TRAINING_FREQUENCY,
                USER_EXPERIENCE_TIME
        };
        this.allColumnsClub = new String[] {
                CLUB_ID,
                CLUB_NAME,
                CLUB_MEN_MIN_DISTANCE,
                CLUB_MEN_MAX_DISTANCE,
                CLUB_MEN_AVG_DISTANCE,
                CLUB_WOMEN_MIN_DISTANCE,
                CLUB_WOMEN_MAX_DISTANCE,
                CLUB_WOMEN_AVG_DISTANCE
        };
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE_USER);
        database.execSQL(DATABASE_CREATE_CLUB);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        Log.w(GolfDatabase.class.getName(), "Upgrading database from version " + oldVersion + " to " + newVersion + ", which will destroy all old data");
        database.execSQL("DROP TABLE IF EXISTS " + USER_TABLE_NAME);
        database.execSQL("DROP TABLE IF EXISTS " + CLUB_TABLE_NAME);
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

    public void deleteConnectedUser() {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(USER_TABLE_NAME, null, null);
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

    /*------------------------------------------------------ CLUB METHODS ------------------------------------------------------*/
    public void insertDefaultClubs(){
        Club driver = new Club("Driver", 200, 230, 260, 150, 175, 200);
        Club three_wood = new Club("3-wood", 180, 215, 235, 125, 150, 180);
        Club five_wood = new Club("5-wood", 170, 195, 210, 105, 135, 170);
        Club three_iron = new Club("3-iron", 160, 180, 200, 100, 125, 160);
        Club four_iron = new Club("4-iron", 150, 170, 185, 90, 120, 150);
        Club five_iron = new Club("5-iron", 140, 160, 170, 80, 110, 140);
        Club six_iron = new Club("6-iron", 130, 150, 160, 70, 100, 130);
        Club seven_iron = new Club("7-iron", 120, 140, 150, 65, 90, 120);
        Club height_iron = new Club("8-iron", 110, 130, 140, 60, 80, 110);
        Club nine_iron = new Club("9-iron", 95, 115, 130, 55, 70, 95);
        Club PW = new Club("PW", 80, 105, 120, 50, 60, 80);
        Club SW = new Club("SW", 60, 80, 100, 40, 50, 60);
        ArrayList<Club> myDefaultClubs = new ArrayList<>();
        myDefaultClubs.add(driver);
        myDefaultClubs.add(three_wood);
        myDefaultClubs.add(five_wood);
        myDefaultClubs.add(three_iron);
        myDefaultClubs.add(four_iron);
        myDefaultClubs.add(five_iron);
        myDefaultClubs.add(six_iron);
        myDefaultClubs.add(seven_iron);
        myDefaultClubs.add(height_iron);
        myDefaultClubs.add(nine_iron);
        myDefaultClubs.add(PW);
        myDefaultClubs.add(SW);

        SQLiteDatabase db = getWritableDatabase();
        for (Club myClub: myDefaultClubs) {
            ContentValues values = new ContentValues();
            values.put(CLUB_NAME, myClub.getName());
            values.put(CLUB_MEN_MIN_DISTANCE, myClub.getMenMinDistance());
            values.put(CLUB_MEN_AVG_DISTANCE, myClub.getMenAvgDistance());
            values.put(CLUB_MEN_MAX_DISTANCE, myClub.getMenMaxDistance());
            values.put(CLUB_WOMEN_MIN_DISTANCE, myClub.getWomenMinDistance());
            values.put(CLUB_WOMEN_AVG_DISTANCE, myClub.getWomenAvgDistance());
            values.put(CLUB_WOMEN_MAX_DISTANCE, myClub.getWomenMaxDistance());
            db.insert(CLUB_TABLE_NAME, null, values);
        }
    }

    /*------------------------------------------------------ TOOL METHODS ------------------------------------------------------*/
    public void deleteAllData() {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(USER_TABLE_NAME, null, null);
        db.delete(CLUB_TABLE_NAME, null, null);
    }

    public static GolfDatabase getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new GolfDatabase(context.getApplicationContext());
        }
        return mInstance;
    }
}
