package edu.calpoly.nnegrey.baby8alpha;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by noahnegrey on 5/17/16.
 */
public class PatternDataSource {
    private SQLiteDatabase database;
    private PatternDatabaseHelper dbHelper;
    private String[] allColumns = { PatternTable.PATTERN_KEY_ID,
            PatternTable.PATTERN_KEY_NAME,
            PatternTable.PATTERN_KEY_ORDER };

    public PatternDataSource(Context context) {
        dbHelper = new PatternDatabaseHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public Pattern createPattern(String name, long order) {
        ContentValues values = new ContentValues();
        values.put(PatternTable.PATTERN_KEY_NAME, name);
        values.put(PatternTable.PATTERN_KEY_ORDER, order);
        long insertId = database.insert(PatternTable.DATABASE_TABLE_PATTERN, null,
                values);
        Cursor cursor = database.query(PatternTable.DATABASE_TABLE_PATTERN,
                allColumns, PatternTable.PATTERN_KEY_ID + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();
        Pattern newPattern = cursorToPattern(cursor);
        cursor.close();
        return newPattern;
    }

    public void deletePattern(Pattern pattern) {
        long id = pattern.getId();
        database.delete(PatternTable.DATABASE_TABLE_PATTERN, PatternTable.PATTERN_KEY_ID
                + " = " + id, null);
    }

    public void updateOrder(Pattern pattern) {
        ContentValues values = new ContentValues();
        values.put(PatternTable.PATTERN_KEY_ID, pattern.getId());
        values.put(PatternTable.PATTERN_KEY_NAME, pattern.getPatternName());
        values.put(PatternTable.PATTERN_KEY_ORDER, pattern.getDisplayOrder());
        database.update(PatternTable.DATABASE_TABLE_PATTERN, values, PatternTable.PATTERN_KEY_ID + " = " + pattern.getId(), null);
    }

    public List<Pattern> getAllPatterns() {
        List<Pattern> patterns = new ArrayList<Pattern>();

        Cursor cursor = database.query(PatternTable.DATABASE_TABLE_PATTERN,
                allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Pattern pattern = cursorToPattern(cursor);
            patterns.add(pattern);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return patterns;
    }

    private Pattern cursorToPattern(Cursor cursor) {
        Pattern pattern = new Pattern();
        pattern.setId(cursor.getLong(PatternTable.PATTERN_COL_ID));

        pattern.setPatternName(cursor.getString(PatternTable.PATTERN_COL_NAME));
        pattern.setDisplayOrder(cursor.getLong(PatternTable.PATTERN_COL_ORDER));
        return pattern;
    }
}
