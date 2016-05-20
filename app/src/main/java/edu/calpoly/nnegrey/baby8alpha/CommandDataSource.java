package edu.calpoly.nnegrey.baby8alpha;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by noahnegrey on 5/17/16.
 */
public class CommandDataSource {
    private SQLiteDatabase database;
    private CommandDatabaseHelper dbHelper;
    private String[] allColumns = { CommandTable.COMMAND_KEY_ID,
            CommandTable.COMMAND_KEY_PATTERN_ID,
            CommandTable.COMMAND_KEY_TYPE,
            CommandTable.COMMAND_KEY_DIRECTION,
            CommandTable.COMMAND_KEY_VELOCITY,
            CommandTable.COMMAND_KEY_DURATION,
            CommandTable.COMMAND_KEY_HEAD_DEGREE,
            CommandTable.COMMAND_KEY_EFFECT,
            CommandTable.COMMAND_KEY_DATE };

    public CommandDataSource(Context context) {
        dbHelper = new CommandDatabaseHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public Command createCommand(long pattern_id, int type, int direction, int velocity, int duration, int head_degree, String effect) {
        ContentValues values = new ContentValues();
        values.put(CommandTable.COMMAND_KEY_PATTERN_ID, pattern_id);
        values.put(CommandTable.COMMAND_KEY_TYPE, type);
        values.put(CommandTable.COMMAND_KEY_DIRECTION, direction);
        values.put(CommandTable.COMMAND_KEY_VELOCITY, velocity);
        values.put(CommandTable.COMMAND_KEY_DURATION, duration);
        values.put(CommandTable.COMMAND_KEY_HEAD_DEGREE, head_degree);
        values.put(CommandTable.COMMAND_KEY_EFFECT, effect);
        values.put(CommandTable.COMMAND_KEY_DATE, new Date().getTime());
        long insertId = database.insert(CommandTable.DATABASE_TABLE_COMMAND, null,
                values);
        Cursor cursor = database.query(CommandTable.DATABASE_TABLE_COMMAND,
                allColumns, CommandTable.COMMAND_KEY_ID + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();
        Command newCommand = cursorToCommand(cursor);
        cursor.close();
        return newCommand;
    }

    public void deleteCommand(Command command) {
        long id = command.getId();
        database.delete(CommandTable.DATABASE_TABLE_COMMAND, CommandTable.COMMAND_KEY_ID
                + " = " + id, null);
    }

    public void updateCommand(Command command) {
        ContentValues values = new ContentValues();
        values.put(CommandTable.COMMAND_KEY_ID, command.getId());
        values.put(CommandTable.COMMAND_KEY_TYPE, command.getType());
        values.put(CommandTable.COMMAND_KEY_DIRECTION, command.getDirection());
        values.put(CommandTable.COMMAND_KEY_VELOCITY, command.getVelocity());
        values.put(CommandTable.COMMAND_KEY_DURATION, command.getDuration());
        values.put(CommandTable.COMMAND_KEY_HEAD_DEGREE, command.getHeadDegree());
        values.put(CommandTable.COMMAND_KEY_EFFECT, command.getEffect());
        values.put(CommandTable.COMMAND_KEY_DATE, new Date().getTime());
        database.update(CommandTable.DATABASE_TABLE_COMMAND, values, CommandTable.COMMAND_KEY_ID + " = " + command.getId(), null);
    }

    public List<Command> getAllCommands(long pattern_id) {
        List<Command> commands = new ArrayList<Command>();

        Cursor cursor = database.query(CommandTable.DATABASE_TABLE_COMMAND,
                allColumns, CommandTable.COMMAND_KEY_PATTERN_ID + " = " + pattern_id, null, null, null, CommandTable.COMMAND_KEY_DATE + " ASC");

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Command command = cursorToCommand(cursor);
            commands.add(command);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return commands;
    }

    private Command cursorToCommand(Cursor cursor) {
        Command command = new Command();
        command.setId(cursor.getLong(CommandTable.COMMAND_COL_ID));
        command.setType(cursor.getInt(CommandTable.COMMAND_COL_TYPE));
        command.setDirection(cursor.getInt(CommandTable.COMMAND_COL_DIRECTION));
        command.setVelocity(cursor.getInt(CommandTable.COMMAND_COL_VELOCITY));
        command.setDuration(cursor.getInt(CommandTable.COMMAND_COL_DURATION));
        command.setHeadDegree(cursor.getInt(CommandTable.COMMAND_COL_HEAD_DEGREE));
        command.setEffect(cursor.getString(CommandTable.COMMAND_COL_EFFECT));
        command.setPatternId(cursor.getLong(CommandTable.COMMAND_COL_PATTERN_ID));
        return command;
    }
}
