package edu.calpoly.nnegrey.baby8alpha;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by noahnegrey on 5/17/16.
 */
public class CommandTable {
    public static final String DATABASE_TABLE_COMMAND = "command_table";

    public static final String COMMAND_KEY_ID = "_id";
    public static final int COMMAND_COL_ID = 0;

    public static final String COMMAND_KEY_PATTERN_ID = "pattern_id";
    public static final int COMMAND_COL_PATTERN_ID = COMMAND_COL_ID + 1;

    public static final String COMMAND_KEY_TYPE = "command_type";
    public static final int COMMAND_COL_TYPE = COMMAND_COL_ID + 2;

    public static final String COMMAND_KEY_DIRECTION = "command_direction";
    public static final int COMMAND_COL_DIRECTION = COMMAND_COL_ID + 3;

    public static final String COMMAND_KEY_VELOCITY = "command_velocity";
    public static final int COMMAND_COL_VELOCITY = COMMAND_COL_ID + 4;

    public static final String COMMAND_KEY_DURATION = "command_duration";
    public static final int COMMAND_COL_DURATION = COMMAND_COL_ID + 5;

    public static final String COMMAND_KEY_HEAD_DEGREE = "command_head_degree";
    public static final int COMMAND_COL_HEAD_DEGREE = COMMAND_COL_ID + 6;

    public static final String COMMAND_KEY_EFFECT = "command_effect";
    public static final int COMMAND_COL_EFFECT = COMMAND_COL_ID + 7;

    public static final String COMMAND_KEY_DATE = "command_order";
    public static final int COMMAND_COL_DATE = COMMAND_COL_ID + 8;

    /** SQLite database creation statement. Auto-increments IDs of inserted commands.
     * Command IDs are set after insertion into the database. */
    public static final String DATABASE_CREATE = "create table " + DATABASE_TABLE_COMMAND + " (" +
            COMMAND_KEY_ID + " integer primary key autoincrement, " +
            COMMAND_KEY_PATTERN_ID  + " integer not null, " +
            COMMAND_KEY_TYPE	    + " integer not null, " +
            COMMAND_KEY_DIRECTION	+ " integer not null, " +
            COMMAND_KEY_VELOCITY	+ " integer not null, " +
            COMMAND_KEY_DURATION	+ " integer not null, " +
            COMMAND_KEY_HEAD_DEGREE + " integer not null, " +
            COMMAND_KEY_EFFECT      + " text not null, " +
            COMMAND_KEY_DATE       + " integer not null);";

    /** SQLite database table removal statement. Only used if upgrading database. */
    public static final String DATABASE_DROP = "drop table if exists " + DATABASE_TABLE_COMMAND;

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
        Log.w(CommandTable.class.getName(), "onUpgrade: " + oldVersion + " to " + newVersion);
        database.execSQL(DATABASE_DROP);
        onCreate(database);
    }
}
