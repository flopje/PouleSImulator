package com.bonenkamp.PouleSimulator.data;

import android.provider.BaseColumns;

/**
 *
 * Contains the Database and table contract.
 * <p>
 * For the table see:
 * {@link TournamentDatabaseContract.TeamTable}
 */
public final class TournamentDatabaseContract {

    public static final String AUTHORITY        = "com.bonenkamp.";
    public static final String SCHEME           = "content://";
    public static final String DB_NAME          = "Tournament";
    public static final int DB_VERSION          = 1;
    public static final String TEXT_TYPE        = "TEXT";
    public static final String COMMA_SEPARATOR  = ",";
    public static final String SLASH            = "/";

    // Do not allow this class to be initiated
    private TournamentDatabaseContract() {}

    // Arraylist of the create statements, can be expanded
    public static final String[] SQL_CREATE_STATEMENTS = {
            TeamTable.CREATE_TABLE,
            RefereeTable.CREATE_TABLE
    };

    /**
     * The table class, containing the column names,
     * and create and delete query.
     */
    public final class TeamTable implements BaseColumns {

        // Do not allow to be initiated by accident
        private TeamTable() {}

        //Columns
        public static final String TEAMS_TABLE_NAME             = "Teams";

        public static final String KEY_TEAM_NAME                = "name";

        public static final String KEY_TEAM_GOALS               = "goals";

        public static final String KEY_TEAM_ATT_STRENGTH        = "attack_strength";

        public static final String KEY_TEAM_MID_STRENGTH        = "midfield_strength";

        public static final String KEY_TEAM_DEF_STRENGTH        = "defense_strength";

        public static final String KEY_TEAM_GOALS_AGAINST       = "goals_against";

        public static final String KEY_TEAM_TOTAL_MATCHES_WON   = "total_matches_won";

        public static final String KEY_TEAM_TOTAL_MATCHES_LOST  = "total_matches_lost";

        public static final String KEY_TEAM_TOTAL_MATCHES_EVEN  = "total_matches_even";

        // SQL create statement
        public static final String CREATE_TABLE = "CREATE TABLE " + TEAMS_TABLE_NAME + " ("
                                                    + _ID + " INTEGER PRIMARY KEY,"
                                                    + KEY_TEAM_NAME + " TEXT,"
                                                    + KEY_TEAM_GOALS + " INTEGER,"
                                                    + KEY_TEAM_GOALS_AGAINST + " INTEGER,"
                                                    + KEY_TEAM_TOTAL_MATCHES_WON + " INTEGER,"
                                                    + KEY_TEAM_TOTAL_MATCHES_LOST + " INTEGER,"
                                                    + KEY_TEAM_TOTAL_MATCHES_EVEN + " INTEGER,"
                                                    + KEY_TEAM_ATT_STRENGTH + " INTEGER,"
                                                    + KEY_TEAM_MID_STRENGTH + " INTEGER,"
                                                    + KEY_TEAM_DEF_STRENGTH + " INTEGER"
                                                    + " );";

        // SQL Ddelete statement
        public static final String DELETE_TABLE = "DROP TABLE IF EXISTS " + TEAMS_TABLE_NAME;
    }

    /**
     * The referee table.
     */
    public final class RefereeTable implements BaseColumns {

        public static final String REFEREE_TABLE_NAME       = "Referees";

        public static final String KEY_REFEREE_NAME         = "name";

        public static final String KEY_REFEREE_STRICTNESS   = "strictness";

        // Create statement
        public static final String CREATE_TABLE = "CREATE TABLE " + REFEREE_TABLE_NAME + " ("
                + _ID + " INTEGER PRIMARY KEY,"
                + KEY_REFEREE_NAME + " TEXT,"
                + KEY_REFEREE_STRICTNESS + " INTEGER"
                + " );";

        //Delete table statement
        public static final String DELETE_TABLE = "DROP TABLE IF EXISTS " + REFEREE_TABLE_NAME;
    }

}
