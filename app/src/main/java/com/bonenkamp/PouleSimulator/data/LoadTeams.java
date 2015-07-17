package com.bonenkamp.PouleSimulator.data;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.bonenkamp.PouleSimulator.models.Team;

import java.util.ArrayList;

/**
 *
 * Load all the teams and team information from the database.
 *
 * {@link LoadTeams#getAllTeamsFromDatabase(Context)}
 */
public class LoadTeams {

    private Context context;

    public void LoadTeams(){

    }

    /**
     * Load all the teams and team information from the database.
     * Then create per team a {@link Team} object.
     * Lastly add all the {@link Team} objects to a {@link ArrayList}.
     *
     * @param context the activity context.
     * @return a {@link ArrayList} collection with {@link Team} objects
     */
    public static ArrayList<Team> getAllTeamsFromDatabase(Context context) {
        // Retrieve all the teams from database
        DatabaseHelper helper = new DatabaseHelper(context);
        SQLiteDatabase database = helper.getReadableDatabase();

        //Which columns we want to be returned
        String[] projection = {TournamentDatabaseContract.TeamTable._ID,
                TournamentDatabaseContract.TeamTable.KEY_TEAM_NAME,
                TournamentDatabaseContract.TeamTable.KEY_TEAM_ATT_STRENGTH,
                TournamentDatabaseContract.TeamTable.KEY_TEAM_MID_STRENGTH,
                TournamentDatabaseContract.TeamTable.KEY_TEAM_DEF_STRENGTH,
                TournamentDatabaseContract.TeamTable.KEY_TEAM_GOALS,
                TournamentDatabaseContract.TeamTable.KEY_TEAM_GOALS_AGAINST,
                TournamentDatabaseContract.TeamTable.KEY_TEAM_TOTAL_MATCHES_WON,
                TournamentDatabaseContract.TeamTable.KEY_TEAM_TOTAL_MATCHES_LOST,
                TournamentDatabaseContract.TeamTable.KEY_TEAM_TOTAL_MATCHES_EVEN
        };

        //Collection with the available teams in it
        ArrayList<Team> teamsCollection = new ArrayList<>();

        String sortOrder = TournamentDatabaseContract.TeamTable.KEY_TEAM_NAME + " DESC";

        try{
            //Cursor stores the retrieve database rows, no where clause, we want every result
            Cursor cursor = database.query(TournamentDatabaseContract.TeamTable.TEAMS_TABLE_NAME,
                    projection,
                    null, null, null, null, sortOrder);

            if(cursor.moveToFirst()) {
                while (!cursor.isAfterLast()) {
                    int id                  = cursor.getInt(cursor.getColumnIndex(
                            TournamentDatabaseContract.TeamTable._ID));

                    String name             = cursor.getString(cursor.getColumnIndex(
                            TournamentDatabaseContract.TeamTable.KEY_TEAM_NAME));

                    int attackStrength      = cursor.getInt(cursor.getColumnIndex(
                            TournamentDatabaseContract.TeamTable.KEY_TEAM_ATT_STRENGTH));

                    int midfieldStrength    = cursor.getInt(cursor.getColumnIndex(
                            TournamentDatabaseContract.TeamTable.KEY_TEAM_MID_STRENGTH));

                    int defenseStrength     = cursor.getInt(cursor.getColumnIndex(
                            TournamentDatabaseContract.TeamTable.KEY_TEAM_DEF_STRENGTH));

                    Team team = new Team(id, name, 0, attackStrength, midfieldStrength,
                            defenseStrength);

                    team.addGoals(cursor.getInt(
                            cursor.getColumnIndex(TournamentDatabaseContract.TeamTable.KEY_TEAM_GOALS)
                    ));
                    team.addGoalsAgainst(cursor.getInt(
                            cursor.getColumnIndex(TournamentDatabaseContract.TeamTable.KEY_TEAM_GOALS_AGAINST)
                    ));
                    team.addTotalMatchesWon(cursor.getInt(
                            cursor.getColumnIndex(
                                    TournamentDatabaseContract.TeamTable.KEY_TEAM_TOTAL_MATCHES_WON)
                    ));
                    team.addTotalMatchesLost(cursor.getInt(
                            cursor.getColumnIndex(
                                    TournamentDatabaseContract.TeamTable.KEY_TEAM_TOTAL_MATCHES_LOST)
                    ));
                    team.addTotalMatchesEven(cursor.getInt(
                            cursor.getColumnIndex(
                                    TournamentDatabaseContract.TeamTable.KEY_TEAM_TOTAL_MATCHES_EVEN)
                    ));

                    teamsCollection.add(team);

                    cursor.moveToNext();
                }
            }
            //Always close cursor.
            cursor.close();

            //Close database object
            database.close();
        }catch(SQLException e){
            Log.e("SQL read error", e.toString());
        }


        return teamsCollection;
    }
}
