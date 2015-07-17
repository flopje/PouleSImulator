package com.bonenkamp.PouleSimulator.data;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.bonenkamp.PouleSimulator.models.Referee;
import com.bonenkamp.PouleSimulator.models.Team;

import java.util.ArrayList;

/**
 * Load the referees from the database.
 */
public class LoadReferees {

    private Context context;

    public void LoadReferees(){

    }

    /**
     * Load all the teams and team information from the database.
     * Then create per team a {@link Referee} object.
     * Lastly add all the {@link Referee} objects to a {@link ArrayList}.
     *
     * @param context the activity context.
     * @return a {@link ArrayList} collection with {@link Referee} objects
     */
    public static ArrayList<Referee> getAllRefereesFromDatabase(Context context) {
        // Retrieve the referees from database
        DatabaseHelper helper = new DatabaseHelper(context);
        SQLiteDatabase database = helper.getReadableDatabase();

        //Which columns we want to be returned
        String[] projection = {TournamentDatabaseContract.RefereeTable._ID,
                TournamentDatabaseContract.RefereeTable.KEY_REFEREE_NAME,
                TournamentDatabaseContract.RefereeTable.KEY_REFEREE_STRICTNESS,
        };

        //Collection for all the available referees
        ArrayList<Referee> refereeCollection = new ArrayList<>();

        String sortOrder = TournamentDatabaseContract.RefereeTable.KEY_REFEREE_NAME + " DESC";

        try {
            //Cursor stores the retrieve database rows, no where clause, we want every result
            Cursor cursor = database.query(
                    TournamentDatabaseContract.RefereeTable.REFEREE_TABLE_NAME,
                    projection,
                    null, null, null, null, sortOrder);

            if (cursor.moveToFirst()) {
                while (!cursor.isAfterLast()) {
                    int id = cursor.getInt(cursor.getColumnIndex(
                            TournamentDatabaseContract.RefereeTable._ID));

                    String name = cursor.getString(cursor.getColumnIndex(
                            TournamentDatabaseContract.RefereeTable.KEY_REFEREE_NAME));

                    int strictness = cursor.getInt(cursor.getColumnIndex(
                            TournamentDatabaseContract.RefereeTable.KEY_REFEREE_STRICTNESS));

                    Referee referee = new Referee(id, name, strictness);

                    refereeCollection.add(referee);

                    cursor.moveToNext();
                }
            }
            //Always close cursor.
            cursor.close();

            // Close database object
            database.close();
        } catch (SQLException e) {
            Log.e("SQL read error", e.toString());
        }

        return refereeCollection;
    }
}
