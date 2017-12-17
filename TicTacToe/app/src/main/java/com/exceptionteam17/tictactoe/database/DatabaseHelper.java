package com.exceptionteam17.tictactoe.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper{

    private static DatabaseHelper instance;

    private static final String DATABASE_NAME = "TicTacToe.db";
    private static int DATABASE_VERSION = 1;

    private static final String TABLE_USERS_SCORES = "users_scores";

    private static final String T_USERS_SCORES_COL_1 = "username";
    private static final String T_USERS_SCORES_COL_2 = "wins";
    private static final String T_USERS_SCORES_COL_3 = "draws";
    private static final String T_USERS_SCORES_COL_4 = "loses";

    private static final String USERS_SCORES_TABLE_CREATE = "CREATE TABLE IF NOT EXISTS " + TABLE_USERS_SCORES +
            " (" + T_USERS_SCORES_COL_1 + " TEXT, " +
            T_USERS_SCORES_COL_2 + " INTEGER, " +
            T_USERS_SCORES_COL_3 + " INTEGER, " +
            T_USERS_SCORES_COL_4 + " INTEGER," +
            " PRIMARY KEY (" + T_USERS_SCORES_COL_1 + "));";



    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(USERS_SCORES_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String drop = "DROP TABLE " + USERS_SCORES_TABLE_CREATE + ";";
        db.execSQL(drop);
        onCreate(db);
    }

    public static synchronized DatabaseHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseHelper(context.getApplicationContext());
        }
        return instance;
    }


    public boolean addUser(String username) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(T_USERS_SCORES_COL_1, username);
        contentValues.put(T_USERS_SCORES_COL_2, 0);
        contentValues.put(T_USERS_SCORES_COL_3, 0);
        contentValues.put(T_USERS_SCORES_COL_4, 0);

        long b = db.insert(TABLE_USERS_SCORES, null, contentValues);
        return (b != -1);
    }
}
