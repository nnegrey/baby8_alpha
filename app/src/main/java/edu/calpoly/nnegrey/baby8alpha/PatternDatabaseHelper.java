package edu.calpoly.nnegrey.baby8alpha;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by noahnegrey on 5/17/16.
 */
public class PatternDatabaseHelper extends SQLiteOpenHelper {
    /** The name of the database. */
    public static final String DATABASE_NAME = "patterndatabase.db";

    /** The starting database version. */
    public static final int DATABASE_VERSION = 1;

    public PatternDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        PatternTable.onCreate(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        PatternTable.onUpgrade(db, oldVersion, newVersion);
    }
}
