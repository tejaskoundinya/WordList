package com.tejaskoundinya.wordlist.com.tejaskoundinya.wordlist.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by tejas
 */
public class WordDbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    static final String DATABASE_NAME = "word.db";

    public WordDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        final String SQL_CREATE_WORD_TABLE = "CREATE TABLE " + WordContract.WordEntry.TABLE_NAME + " (" +
                WordContract.WordEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +

                WordContract.WordEntry.COLUMN_WORD_NAME + " TEXT UNIQUE NOT NULL, " +
                WordContract.WordEntry.COLUMN_MEANING + " TEXT NOT NULL, " +
                WordContract.WordEntry.COLUMN_POS + " TEXT" +
                " );";

        sqLiteDatabase.execSQL(SQL_CREATE_WORD_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + WordContract.WordEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

}
