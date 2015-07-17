package com.bonenkamp.PouleSimulator.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * Class for removal of an database record.
 */
public class RemoveRecordFromDatabase {

    public RemoveRecordFromDatabase() {}

    /**
     * Remove a record from a table
     * @param table String table name
     * @param id int Row id
     * @param context {@link Context} Application context
     */
    public void removeRecordFromDatabase(String table, int id, Context context) {
        DatabaseHelper helper = new DatabaseHelper(context);
        SQLiteDatabase database = helper.getWritableDatabase();

        String stringId = String.valueOf(id);
        database.execSQL("DELETE FROM " + table + " WHERE _ID = '" + stringId + "'");
    }
}
