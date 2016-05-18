package edu.calpoly.nnegrey.baby8alpha;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by noahnegrey on 5/17/16.
 */
public class PatternTable {

    /** Joke table in the database. */
    public static final String DATABASE_TABLE_PATTERN = "pattern_table";

    /** Joke table column names and IDs for database access. */
    public static final String PATTERN_KEY_ID = "_id";
    public static final int PATTERN_COL_ID = 0;

    public static final String PATTERN_KEY_NAME = "pattern_name";
    public static final int PATTERN_COL_NAME = PATTERN_COL_ID + 1;

    public static final String PATTERN_KEY_ORDER = "pattern_order";
    public static final int PATTERN_COL_ORDER = PATTERN_COL_ID + 2;

    /** SQLite database creation statement. Auto-increments IDs of inserted pattern.
     * Pattern IDs are set after insertion into the database. */
    public static final String DATABASE_CREATE = "create table " + DATABASE_TABLE_PATTERN + " (" +
            PATTERN_KEY_ID + " integer primary key autoincrement, " +
            PATTERN_KEY_NAME	+ " text not null, " +
            PATTERN_KEY_ORDER   + " integer not null);";

    /** SQLite database table removal statement. Only used if upgrading database. */
    public static final String DATABASE_DROP = "drop table if exists " + DATABASE_TABLE_PATTERN;

    /**
     * Initializes the database.
     *
     * @param database
     * 				The database to initialize.
     */
    public static void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    /**
     * Upgrades the database to a new version.
     *
     * @param database
     * 					The database to upgrade.
     * @param oldVersion
     * 					The old version of the database.
     * @param newVersion
     * 					The new version of the database.
     */
    public static void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        Log.w(PatternTable.class.getName(), "onUpgrade: " + oldVersion + " to " + newVersion);
        database.execSQL(DATABASE_DROP);
        onCreate(database);
    }
}
