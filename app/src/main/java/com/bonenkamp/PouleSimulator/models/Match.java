package com.bonenkamp.PouleSimulator.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.bonenkamp.PouleSimulator.exceptions.MatchNotPlayedException;

import java.util.ArrayList;

/**
 * Match object. Contains the information about one match.
 */
public class Match implements Parcelable{
    private int id;
    private Team teamHome;
    private Team teamAway;
    private int goalsTeamHome;
    private int goalsTeamAway;
    private Referee referee;
    private boolean matchPlayed;

    private int ballPossessionHome;
    private int ballPossessionAway;

    /**
     * The match playtime. Based on matchduration * 1secon/ simulation_intervall.
     * Equals the total number of calculations made
     */
    int playTime;

    /**
     * Contstructor for a match.
     *
     * @param id        int unique match id
     * @param teamHome  Team teamHome
     * @param teamAway  Team teamAway
     * @param referee   Referee Object referee
     */
    public Match(int id, Team teamHome, Team teamAway, Referee referee){
        this.id         = id;
        this.teamHome   = teamHome;
        this.teamAway   = teamAway;
        this.referee    = referee;

        matchPlayed = false;

        // Reset goals to 0
        this.goalsTeamAway = 0;
        this.goalsTeamHome = 0;
    }

    //Methods

    /**
     * Match Id
     * @return int Match Id
     */
    public int id() {
        return id;
    }

    /**
     * The team that plays as the home team
     * @return {@link Team} team
     */
    public Team teamHome() {
        return teamHome;
    }

    /**
     * Return the team that plays as the away team
     * @return {@link Team} team
     */
    public Team teamAway() {
        return teamAway;
    }

    /**
     * Return the {@link Referee} referee for this match.
     * @return {@link Referee} referee
     */
    public Referee referee() {
        return referee;
    }

    // Getters setters

    /**
     * Return the number of goals the home team made
     * @return int number of goals
     */
    public int goalsTeamHome() {
        return goalsTeamHome;
    }

    /**
     * Return the number of goals the away team made.
     * @return int number of goals
     */
    public int goalsTeamAway() {
        return goalsTeamAway;
    }

    /**
     * Add new goal to total number of goals of teamHome.
     */
    public void addGoalTeamHome() {
        this.goalsTeamHome++;
    }

    /**
     * Add new goal to total number of goals of teamAway.
     */
    public void addGoalTeamAway() {
        this.goalsTeamAway++;
    }

    /**
     * Add one to ballpossionHome
     */
    public void addBallPossessionHome() {
        this.ballPossessionHome++;
    }

    /**
     * Add one to ballpossionAway
     */
    public void addBallPossessionAway() {
        this.ballPossessionAway++;
    }

    /**
     * Get ballpossessionHome, String representing the percentage of the total calculations
     * teamHome had the ball.
     * @return int
     */
    public String getBallPossessionHome() {
        double percentage = (((double) ballPossessionHome/(double) playTime))*100;
        return String.valueOf((int)percentage);
    }

    /**
     * Get ballpossessionAway, String representing the percentage of the total calculations
     * teamAway had the ball.
     * @return String
     */
    public String getBallPossessionAway() {
        double percentage = (((double) ballPossessionAway/(double) playTime))*100;
        return String.valueOf((int)percentage);
    }

    /**
     * Set the match result. This method set the results of a match. Also sets the value of sets
     * the object matchplayed status to true.
     * <p>
     * @param goalsTeamHome int Number of goals the team home made.
     * @param goalsTeamAway int Number of goals the team away made.
     * @param matchPlayed   boolean Set to true if the match is finished and final scores are set.
     * @param playTime      int representing the total number of simulation calculations made.
     */
    public void setMatchResult(int goalsTeamHome, int goalsTeamAway, boolean matchPlayed, int playTime) {
        this.matchPlayed = matchPlayed;

        this.goalsTeamHome = goalsTeamHome;
        this.goalsTeamAway = goalsTeamAway;

        if(playTime != 0) {
            this.playTime = playTime;
        }


        teamHome.addTournamentGoals(this.goalsTeamHome);
        teamAway.addTournamentGoals(this.goalsTeamAway);

        // If match is played, set corresponding points to the teams.
        if (this.matchPlayed) {
            if(this.goalsTeamHome == this.goalsTeamAway) {
                teamHome.addPoints(1);
                teamAway.addPoints(1);
            }else if(this.goalsTeamHome > this.goalsTeamAway) {
                teamHome.addPoints(3);
            }else {
                teamAway.addPoints(3);
            }
        }

    }

    /**
     * Returns true if this match has been played, false otherwise.
     * @return boolean
     */
    public boolean isMatchPlayed() {
        return matchPlayed;
    }

    /**
     * Returns the database id of the Winner, incase of equal score it returns null.
     * @return {@link Team} with the most goals, or null
     */
    public Team getWinner() throws MatchNotPlayedException{
        if(matchPlayed){
            if(goalsTeamHome == goalsTeamAway){
                return null;
            }else if(goalsTeamHome > goalsTeamAway){
                return teamHome;
            }else{
                return teamAway;
            }
        }else {
            throw new MatchNotPlayedException("Match has not yet been played");
        }
    }

    /**
     * Return the name of the match containing the 2 teamNames, as following:
     * [TEAM1] - [TEAM2]
     * @return string containing both teamnames.
     */
    public String getMatchName(){
        return teamHome.name() + " - " + teamAway.name();
    }

    /**
     * Returns a {@link String} with the current match result in following format:
     * [Number of goals team home made] - [Number of goals team away made]
     * @return
     */
    public String getCurrentResult() {
        return  this.goalsTeamHome + " - " + this.goalsTeamAway();
    }

    //Implement Parcelable

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeParcelable(teamHome, 0);
        dest.writeParcelable(teamAway, 0);
        dest.writeInt(goalsTeamHome);
        dest.writeInt(goalsTeamAway);
    }

    /**
     * Static field used to regenerate object, individually or as arrays
     */
    public static final Parcelable.Creator<Match> CREATOR = new Parcelable.Creator<Match>() {
        public Match createFromParcel(Parcel in) {
            return new Match(in);
        }
        public Match[] newArray(int size) {
            return new Match[size];
        }
    };

    /**Create from Parcel, it reads back int the order they were written! */
    public Match(Parcel in){
        id              = in.readInt();
        teamHome        = (Team) in.readParcelable(Team.class.getClassLoader());
        teamAway        = (Team) in.readParcelable(Team.class.getClassLoader());
        goalsTeamHome   = in.readInt();
        goalsTeamAway   = in.readInt();
    }


}
