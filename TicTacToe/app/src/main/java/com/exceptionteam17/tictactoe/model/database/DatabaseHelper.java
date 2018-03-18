package com.exceptionteam17.tictactoe.model.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper{

    public static final Integer WINS = 1;
    public static final Integer DRAWS = 2;
    public static final Integer LOSES = 3;
    public static final Integer WINS_MULTI = 4;
    public static final Integer DRAWS_MULTI = 5;
    public static final Integer LOSES_MULTI = 6;

    private static DatabaseHelper instance;

    private static final String DATABASE_NAME = "TicTacToe.db";
    private static int DATABASE_VERSION = 1;

    private static final String TABLE_USERS_SCORES = "users_scores";

    private static final String T_USERS_SCORES_COL_1 = "username";
    private static final String T_USERS_SCORES_COL_2 = "wins";
    private static final String T_USERS_SCORES_COL_3 = "draws";
    private static final String T_USERS_SCORES_COL_4 = "loses";
    private static final String T_USERS_SCORES_COL_5 = "wins_multi";
    private static final String T_USERS_SCORES_COL_6 = "draws_multi";
    private static final String T_USERS_SCORES_COL_7 = "loses_multi";

    private static final String USERS_SCORES_TABLE_CREATE = "CREATE TABLE IF NOT EXISTS " + TABLE_USERS_SCORES +
            " (" + T_USERS_SCORES_COL_1 + " TEXT, " +
            T_USERS_SCORES_COL_2 + " INTEGER DEFAULT 0, " +
            T_USERS_SCORES_COL_3 + " INTEGER DEFAULT 0, " +
            T_USERS_SCORES_COL_4 + " INTEGER DEFAULT 0, " +
            T_USERS_SCORES_COL_5 + " INTEGER DEFAULT 0, " +
            T_USERS_SCORES_COL_6 + " INTEGER DEFAULT 0, " +
            T_USERS_SCORES_COL_7 + " INTEGER DEFAULT 0," +
            " PRIMARY KEY (" + T_USERS_SCORES_COL_1 + "));";



    private DatabaseHelper(Context context) {
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

        String q = "SELECT " + T_USERS_SCORES_COL_1 + " FROM " + TABLE_USERS_SCORES + " WHERE " + T_USERS_SCORES_COL_1 + " = \"" + username + "\"";
        Cursor c = db.rawQuery(q, null);
        if (c.getCount() > 0) {
            c.close();
            return false;
        }
        c.close();
        ContentValues contentValues = new ContentValues();
        contentValues.put(T_USERS_SCORES_COL_1, username);
        contentValues.put(T_USERS_SCORES_COL_2, 0);
        contentValues.put(T_USERS_SCORES_COL_3, 0);
        contentValues.put(T_USERS_SCORES_COL_4, 0);
        contentValues.put(T_USERS_SCORES_COL_5, 0);
        contentValues.put(T_USERS_SCORES_COL_6, 0);
        contentValues.put(T_USERS_SCORES_COL_7, 0);

        long b = db.insert(TABLE_USERS_SCORES, null, contentValues);
        return (b != -1);
//        String query = "INSERT INTO " + TABLE_USERS_SCORES + "(" + T_USERS_SCORES_COL_1 + ") " + "VALUES(" + username + ")";
//        db.execSQL(query);
//        return true;

    }

    private Integer getUserStat(String username, Integer coll){
        SQLiteDatabase db = this.getWritableDatabase();
        String myRawQuery = "SELECT * FROM " + TABLE_USERS_SCORES + " WHERE " + T_USERS_SCORES_COL_1 + " = \"" + username + "\"";
        Cursor c = db.rawQuery(myRawQuery, null);
        if(c.getCount() == 0){
            return 0;
        }

        c.moveToFirst();
        Integer out = c.getInt(coll);
        c.close();
        return out;
    }

    public Integer getUserWins (String username){
        return getUserStat(username, WINS);
    }

    public Integer getUserDraws (String username){
        return getUserStat(username, DRAWS);
    }

    public Integer getUserLoses (String username){
        return getUserStat(username, LOSES);
    }

    public Integer getUserWinsMulti (String username){
        return getUserStat(username, WINS_MULTI);
    }

    public Integer getUserDrawsMulti (String username){
        return getUserStat(username, DRAWS_MULTI);
    }

    public Integer getUserLosesMulti (String username){
        return getUserStat(username, LOSES_MULTI);
    }

    /// this is not working properly, must fix
    private void addUserStat(String username, int coll){
        SQLiteDatabase db = this.getWritableDatabase();
        String coll_name = "";
        switch (coll){
            case 1:
                coll_name = T_USERS_SCORES_COL_2;
                break;
            case 2:
                coll_name = T_USERS_SCORES_COL_3;
                break;
            case 3:
                coll_name = T_USERS_SCORES_COL_4;
                break;
            case 4:
                coll_name = T_USERS_SCORES_COL_5;
                break;
            case 5:
                coll_name = T_USERS_SCORES_COL_6;
                break;
            case 6:
                coll_name = T_USERS_SCORES_COL_7;
                break;
        }
        int i = getUserStat(username, coll) + 1;
        String update = "UPDATE " + TABLE_USERS_SCORES + " SET " + coll_name + " = " + i + " WHERE " + T_USERS_SCORES_COL_1 + " = \"" + username + "\"";
        db.execSQL(update);
    }

    public void addUserWin (String username){
        addUserStat(username, WINS);
    }

    public void addUserDraw (String username){
        addUserStat(username, DRAWS);
    }

    public void addUserLose (String username){
        addUserStat(username, LOSES);
    }

    public void addUserWinMulti (String username){
        addUserStat(username, WINS_MULTI);
    }

    public void addUserDrawMulti (String username){
        addUserStat(username, DRAWS_MULTI);
    }

    public void addUserLoseMulti (String username){
        addUserStat(username, LOSES_MULTI);
    }
}
