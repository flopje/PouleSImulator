package com.bonenkamp.PouleSimulator.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 *
 * Database helper class for the creation of the db and upgrade of the Database.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    public DatabaseHelper(Context context){
        super(context, TournamentDatabaseContract.DB_NAME, null, TournamentDatabaseContract.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TournamentDatabaseContract.TeamTable.CREATE_TABLE);
        db.execSQL(TournamentDatabaseContract.RefereeTable.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Delete the tables
        db.execSQL(TournamentDatabaseContract.TeamTable.DELETE_TABLE);
        db.execSQL(TournamentDatabaseContract.RefereeTable.DELETE_TABLE);

        // Create the tables
        onCreate(db);
    }
}
