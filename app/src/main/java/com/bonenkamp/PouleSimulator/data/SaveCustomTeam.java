package com.bonenkamp.PouleSimulator.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Save a user created team
 */
public class SaveCustomTeam {

    public SaveCustomTeam() {}

    /**
     * Save a new team in the database.
     * @param name String the team name
     * @param attackStrength int the attackStrength value
     * @param midfieldStrength int the midfieldStrength value
     * @param defenseStrength int the defenseStrength value
     * @param context Context the application context
     * @return boolean  true if successful, otherwise false.
     */
    public boolean saveTeamInDatabase(String name, int attackStrength, int midfieldStrength,
                                       int defenseStrength, Context context) {

        DatabaseHelper helper = new DatabaseHelper(context);
        SQLiteDatabase database = helper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(TournamentDatabaseContract.TeamTable.KEY_TEAM_NAME, name);
        values.put(TournamentDatabaseContract.TeamTable.KEY_TEAM_ATT_STRENGTH, attackStrength);
        values.put(TournamentDatabaseContract.TeamTable.KEY_TEAM_DEF_STRENGTH, defenseStrength);
        values.put(TournamentDatabaseContract.TeamTable.KEY_TEAM_MID_STRENGTH, midfieldStrength);

        try {
            database.insertOrThrow(TournamentDatabaseContract.TeamTable.TEAMS_TABLE_NAME,
                    null,
                    values);
        }catch (SQLException e) {
            Log.e("Insert new team exc.", e.toString());
        }
        database.close();
        helper.close();

        return true;
    }
}
