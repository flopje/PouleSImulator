package com.bonenkamp.PouleSimulator.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Tournament object. Contains all the match objects and team positions.
 * Max number of teams in pool is 4, and played in a single robin round, so
 * 6 matches.
 */
public class Tournament implements Parcelable{

    private Array totals;
    private Match match1;
    private Match match2;
    private Match match3;
    private Match match4;
    private Match match5;
    private Match match6;

    private int currentMatchId;

    private ArrayList<Match> matchList;

    private Team tournamentPosition1;
    private Team tournamentPosition2;
    private Team tournamentPosition3;
    private Team tournamentPosition4;

    //TODO Clean up all objects -. get still needed? E.g is it allowed to change the values without
    // the control in a function? https://en.wikipedia.org/wiki/Encapsulation_(computer_programming)

    /**
     * Creates new Tournament object.
     * @param match1 {@link Match} first match in tournament
     * @param match2 {@link Match} second match in tournament
     * @param match3 {@link Match} third match in tournament
     * @param match4 {@link Match} fourth match in tournament
     * @param match5 {@link Match} fifth match in tournament
     * @param match6 {@link Match} sixth match in tournament
     */
    public Tournament(Match match1, Match match2, Match match3, Match match4,
                      Match match5, Match match6){
        this.match1             = match1;
        this.match2             = match2;
        this.match3             = match3;
        this.match4             = match4;
        this.match5             = match5;
        this.match6             = match6;

        // Set currentMatchId to 0, so we start with the match1
        currentMatchId     = 0;

        matchList = new ArrayList<>();

        matchList.add(this.match1);
        matchList.add(this.match2);
        matchList.add(this.match3);
        matchList.add(this.match4);
        matchList.add(this.match5);
        matchList.add(this.match6);

        // (Re)set the team points and tournament goals to 0
        ArrayList<Team> teams = getAllTeams();
        for (Team team : teams) {
            team.resetTournamentPoints();
            team.resetTournamentGoals();
        }

    }

    // Functions

    public Match match1() {
        return match1;
    }

    public Match match2() {
        return match2;
    }

    public Match match3() {
        return match3;
    }

    public Match match4() {
        return match4;
    }

    public Match match5() {
        return match5;
    }

    public Match match6() {
        return match1;
    }

    /**
     * Returns the next match in list, based on currentMatchId
     * @return Match next match to be played. Null if no next match is available.
     */
    public Match newMatch() {
        if(currentMatchId == 0 || currentMatchId == match1.id()) {
            return match1;
        }
        if (currentMatchId == match2.id()) {
            return match2;
        }
        if (currentMatchId == match3.id()) {
            return match3;
        }
        if (currentMatchId == match4.id()) {
            return match4;
        }
        if (currentMatchId == match5.id()) {
            return match5;
        }
        if (currentMatchId == match6.id()) {
            return match6;
        }
        return null;
    }

    /**
     * Adjust currentMatchId to nextNewMatch
     * @return boolean True if successful, false if failed (wrong currentMatchId) or no next match available
     */
    public boolean setNextMatch() {
        if(currentMatchId == 0) {
            currentMatchId = match2.id();
            return true;
        }
        if (currentMatchId == match1.id()) {
            currentMatchId = match2.id();
            return true;
        }
        if (currentMatchId == match2.id()) {
            currentMatchId = match3.id();
            return true;
        }
        if (currentMatchId == match3.id()) {
            currentMatchId = match4.id();
            return true;
        }
        if (currentMatchId == match4.id()) {
            currentMatchId = match5.id();
            return true;
        }
        if (currentMatchId == match5.id()) {
            currentMatchId = match6.id();
            return true;
        }
        if (currentMatchId == match6.id()) {
            // All matches played
            return false;
        }
        return false;
    }

    /**
     * Returns the tournament results (points, wins, losses, etc)
     * TODO write
     * @return Arraylist with Team objects, sorted winner - loser
     */
    @Deprecated
    public ArrayList<Team> getTournamentResults(){

        return null;
    }

    /**
     * Sort the team tournament positions. Check per team the total points and
     * arrange them from high to low
     * @return ArrayList Witch Team objects, from highest number of points to lowest.
     */
    public ArrayList<Team> getSortedTournamentPositions(){
        //TODO
        ArrayList<Team> teams = getAllTeams();

        Collections.sort(teams, new Comparator<Team>() {
            @Override
            public int compare(Team team1, Team team2) {
                int i = team2.getPoints() - team1.getPoints();
                if(i == 0) {
                    return team2.getTournamentGoals() - team1.getTournamentGoals();
                }
                return i;
            }
        });

        return teams;
    }

    /**
     * Returns a {@link ArrayList} with {@link Team} objects containing all the teams in this
     * tournament.
     * @return ArrayList with Team objects
     */
    private ArrayList<Team> getAllTeams() {
        ArrayList<Team> teamList = new ArrayList<>();

        // Check if teamlist contains team, if not add to list.
        for (Match match : matchList) {
            if(!teamList.contains(match.teamHome())) {
                teamList.add(match.teamHome());
            }
            if (!teamList.contains(match.teamAway())){
                teamList.add(match.teamAway());
            }
        }
        return teamList;
    }


    // Implement Parcelable

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(match1, 0);
        dest.writeParcelable(match2, 0);
        dest.writeParcelable(match3, 0);
        dest.writeParcelable(match4, 0);
        dest.writeParcelable(match5, 0);
        dest.writeParcelable(match6, 0);
        dest.writeInt(currentMatchId);
        dest.writeParcelable(tournamentPosition1, 0);
        dest.writeParcelable(tournamentPosition2, 0);
        dest.writeParcelable(tournamentPosition3, 0);
        dest.writeParcelable(tournamentPosition4, 0);
    }

    /**
     * Static field used to regenerate object, individually or as arrays
     */
    public static final Parcelable.Creator<Tournament> CREATOR = new Parcelable.Creator<Tournament>() {
        public Tournament createFromParcel(Parcel in) {
            return new Tournament(in);
        }
        public Tournament[] newArray(int size) {
            return new Tournament[size];
        }
    };

    /**Create from Parcel, it reads back int the order they were written! */
    public Tournament(Parcel in){
        match1                  = (Match) in.readParcelable(Match.class.getClassLoader());
        match2                  = (Match) in.readParcelable(Match.class.getClassLoader());
        match3                  = (Match) in.readParcelable(Match.class.getClassLoader());
        match4                  = (Match) in.readParcelable(Match.class.getClassLoader());
        match5                  = (Match) in.readParcelable(Match.class.getClassLoader());
        match6                  = (Match) in.readParcelable(Match.class.getClassLoader());
        currentMatchId          = in.readInt();
        tournamentPosition1     = (Team) in.readParcelable(Team.class.getClassLoader());
        tournamentPosition2     = (Team) in.readParcelable(Team.class.getClassLoader());
        tournamentPosition3     = (Team) in.readParcelable(Team.class.getClassLoader());
        tournamentPosition4     = (Team) in.readParcelable(Team.class.getClassLoader());
    }


}
