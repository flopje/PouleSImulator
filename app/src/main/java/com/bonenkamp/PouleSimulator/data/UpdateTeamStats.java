package com.bonenkamp.PouleSimulator.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.bonenkamp.PouleSimulator.models.Team;

/**
 * Updaye the goals and goals against fields of a team in the database
 */
public class UpdateTeamStats {

    public UpdateTeamStats() {}

    /**
     * Update the goals and goals against fields of a team in the database
     * @param id int Row id {@link Team} id
     * @param goals int number of goals
     * @param goalsAgainst int Number of goals against.
     * @param context {@link Context} application context
     * @return boolean true if successful, otherwise false.
     */
    public boolean updateTeamStats(int id, int goals, int goalsAgainst, Context context) {

        DatabaseHelper helper = new DatabaseHelper(context);
        SQLiteDatabase database = helper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(TournamentDatabaseContract.TeamTable.KEY_TEAM_GOALS, goals);
        values.put(TournamentDatabaseContract.TeamTable.KEY_TEAM_GOALS_AGAINST, goalsAgainst);

        String selection = TournamentDatabaseContract.TeamTable._ID + " = ? ";

        try {
            database.update(
                    TournamentDatabaseContract.TeamTable.TEAMS_TABLE_NAME,
                    values,
                    selection,
                    new String[] {String.valueOf(id)});
        }catch (SQLException e) {
            Log.e("Insert new team exc.", e.toString());
            return false;
        }
        database.close();
        helper.close();

        return true;
    }
}
