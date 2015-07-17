package com.bonenkamp.PouleSimulator.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.util.ArrayMap;
import android.util.Log;

import java.util.Random;

/**
 * Class containing the information about a team.
 * And the needed methods for the matchsimulation.
 */
public class Team implements Parcelable{

    private int id;
    private String name;
    private int points;
    private int goals;
    private int goalsAgainst;
    private int totalMatchesWon;
    private int totalMatchesLost;
    private int totalMatchesEven;
    private int aggression;
    private int midfieldStrength;
    private int attackStrength;
    private int defenseStrength;

    private int tournamentGoals;
    private int tournamentGoalsAgainst;

    private Random generator;

    private TeamPosition teamPosition;

    public boolean hasBall = false;

    /**
     * The team position on the field.
     */
    public enum TeamPosition {
        DEFENSE, DEFENSE_MIDDLE, MIDDLE, ATTACK_MIDDLE, ATTACK
    }

    /**
     * The play direction
     */
    public enum Direction {
        FORWARDS, BACKWARDS
    }

    /**
     * The basic actions a team can undertake
     */
    public enum BaseAction {
        ATTACK, DEFEND
    }

    /**
     * Contstructor for a new team
     * @param id                int with unique Database id
     * @param name              String with teamname
     * @param aggression        int with aggression level. (max 5)
     * @param midfieldStrength  int with midfield strength (max 100)
     * @param attackStrength    int with attack strength (max 100)
     * @param defenseStrength   int with defense strength (max 100)
     */
    public Team(int id, String name, int aggression,int midfieldStrength, int attackStrength,
                int defenseStrength){
        this.id                 = id;
        this.name               = name;
        this.aggression         = aggression;
        this.midfieldStrength   = midfieldStrength;
        this.attackStrength     = attackStrength;
        this.defenseStrength    = defenseStrength;

        // Double check values
        if(aggression       > 5)    aggression          = 5;
        if(midfieldStrength > 100)  midfieldStrength    = 100;
        if(attackStrength   > 100)  attackStrength      = 100;
        if(defenseStrength  > 100)  defenseStrength     = 100;
    }

    // Methods

    public void resetTournamentGoals() {
        tournamentGoals         = 0;
        tournamentGoalsAgainst  = 0;
    }

    public void addTournamentGoals(int goals) {
        this.tournamentGoals = this.tournamentGoals + goals;
    }

    public void addTournamentGoalsAgainst(int goals) {
        this.tournamentGoalsAgainst = this.tournamentGoalsAgainst + goals;
    }

    public int getTournamentGoals() {
        return tournamentGoals;
    }

    public int getTournamentGoalsAgainst() {
        return tournamentGoalsAgainst;
    }

    /**
     * Returns this team unique id, based on database index.
     * @return in id
     */
    public int id() {
        return id;
    }

    public String name() {return name;}

    public void resetTournamentPoints() {
        points = 0;
    }

    /**
     * Returns the team aggression level.
     * @return int aggression level
     */
    public int aggression() {
        return aggression;
    }

    /**
     * Returns the team midFieldStrength stat.
     * @return int midFieldStrength
     */
    public int midFieldStrength() {
        return midfieldStrength;
    }

    /**
     * Returns the team attackstrength stat.
     * @return int attackstrength
     */
    public int attackStrength() {
        return attackStrength;
    }

    /**
     * Returns the team defenseStrength stat.
     * @return int defensestrength
     */
    public int defenseStrength() {
        return defenseStrength;
    }

    //Getters and setters

    /**
     * Adds the new points to the existing points
     * @param points int The new points. (0, 1, or 3)
     */
    public void addPoints(int points){
        this.points = this.points + points;
    }

    /**
     * Return the total pmatchpoints this team has
     * @return int total matchpoints
     */
    public int getPoints(){
        return points;
    }

    /**
     * Add the new goals to the current total goal count
     * @param goals int Total new goals
     */
    public void addGoals(int goals){
        this.goals = this.goals + goals;
    }

    /**
     * Return the total goals made
     * @return int Total number of goals
     */
    public int getGoals(){
        return goals;
    }

    /**
     * Add the new goals against to the total goals against count.
     * @param goals int Total new goals against.
     */
    public void addGoalsAgainst(int goals) {
        this.goalsAgainst = goalsAgainst + goals;
    }

    /**
     * Return the number of goals against
     * @return int Number of goals against
     */
    public int getGoalsAgainst() {
        return goalsAgainst;
    }

    /**
     * Return the average goals per match
     * @return double Average goals per match
     */
    public double averageGoals() {
        if (totalMatchesPlayed() != 0 ) {
            return (double)goals/(double)totalMatchesPlayed();
        }
        return 0;

    }

    /**
     * Add the new match won total to the exiting count
     * @param totalMatchesWon int new matches won
     */
    public void addTotalMatchesWon(int totalMatchesWon) {
        this.totalMatchesWon = this.totalMatchesWon + totalMatchesWon;
    }

    /**
     * Return the total matches this team has won
     * @return int
     */
    public int getTotalMatchesWon() {
        return totalMatchesWon;
    }

    /**
     * Add the new number of matches lost to the exiting count
     * @param totalMatchesLost int matches lost
     */
    public void addTotalMatchesLost(int totalMatchesLost) {
        this.totalMatchesLost = this.totalMatchesLost + totalMatchesLost;
    }

    /**
     * Return the total matches this team has lost
     * @return
     */
    public int getTotalMatchesLost() {
        return totalMatchesLost;
    }

    /**
     * Add the new nu,ber of matches played even to the existing count.
     * @param totalMatchesEven int matches played even.
     */
    public void addTotalMatchesEven(int totalMatchesEven) {
        this.totalMatchesEven = this.totalMatchesEven + totalMatchesEven;
    }

    /**
     * Return the total number of matches played even.
     * @return
     */
    public int getTotalMatchesEven() {
        return totalMatchesEven;
    }

    /**
     * Set the team field position
     * @param position {@link com.bonenkamp.PouleSimulator.models.Team.TeamPosition} the team position
     */
    public void setTeamPosition(TeamPosition position) {
        this.teamPosition = position;
    }

    // Methods

    /**
     * Return the total macthes this team has played
     * @return int
     */
    public int totalMatchesPlayed() {
        return totalMatchesWon + totalMatchesLost + totalMatchesEven;
    }

    /**
     * Position boosters or penalties (negative value).
     * @return int array, where first value is attack boost, second value is defense boost.
     */
    public int[] getPositionMutators() {
        switch(teamPosition) {
            case MIDDLE:
                return new int[]{0,0};

            case DEFENSE:
                return new int[]{-10,15};

            case DEFENSE_MIDDLE:
                return new int[]{-5,7};

            case ATTACK_MIDDLE:
                return new int[]{7,-10};

            case ATTACK:
                return new int[]{13,-13};

            default:
                return new int[]{0,0};
        }
    }

    // actions

    /**
     * Return the team action during a match
     * @return {@link Action}
     */
    public Action getAction() {
        generator = new Random();

        int attackBoost     = getPositionMutators()[0];
        int defenseBoost    = getPositionMutators()[1];

        Log.e("position", name + " - " + String.valueOf(teamPosition));

        if (hasBall) {
            BaseAction baseAction       = BaseAction.ATTACK;
            Direction attackDirection   = attack();

            int totalStrength   = attackStrength + (int)(0.5*midfieldStrength);
            int boost           = (totalStrength/100)*attackBoost;
            boolean shotOnGoal  = shot();
            boolean madeFoul    = madeFoul();

            // Set the strength value for this action, based upon a number between 0 and
            // attackStrengh + 1/2 * midfieldStrength
            int strength = generator.nextInt(totalStrength) + boost;

            Action action = new Action(baseAction, attackDirection, teamPosition, strength,
                    shotOnGoal, madeFoul);

            return action;

        } else {
            BaseAction baseAction       = BaseAction.DEFEND;
            Direction defendDirection   = defend();

            int totalStrength   = attackStrength + (int) (0.5*midfieldStrength);
            int boost           = (totalStrength/100)*defenseBoost;
            boolean madeFoul    = madeFoul();

            // Set the strength value for this action, based upon a number between 0 and
            // attackStrengh + midfieldStrength
            int strength = generator.nextInt(totalStrength) + boost;

            Action action = new Action(baseAction, defendDirection, teamPosition, strength,
                    false, madeFoul);

            return action;
        }
    }

    /**
     * Calculate and return the {@link com.bonenkamp.PouleSimulator.models.Team.Direction}
     * play direction of the ball during a defense scenario.
     * @return {@link com.bonenkamp.PouleSimulator.models.Team.Direction} direction
     */
    private Direction defend(){
        //Same as attack but for the defense
        Random random = new Random();
        int change = random.nextInt(100) + 1; //add to include 100

        if(change < 30 ) {
            return Direction.FORWARDS;
        }

        return Direction.BACKWARDS;
    }

    /**
     * Calculates if a shot results in a goal. Returns True if a shot results in a goal, false if
     * the shot is missed.
     * @return boolean
     */
    private boolean shot() {
        if(teamPosition == TeamPosition.ATTACK) {

            generator = new Random();
            int number = generator.nextInt(100);
            if (number < 10 ) {
                return true;
            }
            return false;
        }else if(teamPosition == TeamPosition.ATTACK_MIDDLE) {
            generator = new Random();
            int number = generator.nextInt(100);
            if (number < 5 ) {
                return true;
            }
            return false;
        }

        return false;
    }

    private boolean madeFoul() {
        Random random = new Random();
        int chance = random.nextInt(100);

        if (chance > 20) {
            return false;
        }

        return true;
    }

    /**
     * Adjust the team position based on {@link Action} object parameters
     * @param action {@link Action}
     */
    public void adjustTeamPosition(BaseAction action) {

        if(action ==  BaseAction.ATTACK){
            // If an attack is successfull, the team goes one field sector forward.
            switch(teamPosition) {
                case MIDDLE:
                    teamPosition = TeamPosition.ATTACK_MIDDLE;
                    break;

                case DEFENSE:
                    teamPosition = TeamPosition.DEFENSE_MIDDLE;
                    break;

                case DEFENSE_MIDDLE:
                    teamPosition = TeamPosition.ATTACK_MIDDLE;
                    break;

                case ATTACK_MIDDLE:
                    teamPosition = TeamPosition.ATTACK;
                    break;

                case ATTACK:
                    teamPosition = TeamPosition.ATTACK;
                    break;

                default:
                    teamPosition = TeamPosition.MIDDLE;
                    break;
            }
        }else{
            switch(teamPosition) {
                case MIDDLE:
                    teamPosition = TeamPosition.DEFENSE_MIDDLE;
                    break;

                case DEFENSE:
                    teamPosition = TeamPosition.DEFENSE;
                    break;

                case DEFENSE_MIDDLE:
                    teamPosition = TeamPosition.DEFENSE;
                    break;

                case ATTACK_MIDDLE:
                    teamPosition = TeamPosition.DEFENSE_MIDDLE;
                    break;

                case ATTACK:
                    teamPosition = TeamPosition.ATTACK_MIDDLE;
                    break;

                default:
                    teamPosition = TeamPosition.MIDDLE;
                    break;
            }

        }

    }

    /**
     * Calculate and return the {@link com.bonenkamp.PouleSimulator.models.Team.Direction}
     * play direction of the ball during a attack scenario.
     * @return {@link com.bonenkamp.PouleSimulator.models.Team.Direction} direction
     */
    private Direction attack(){
        //calculate the success of a attack, based on statistics.
        Random random = new Random();
        int change = random.nextInt(100)+1;

        if(change < 30 ) {
            return Direction.BACKWARDS;
        }

        //return change of succesful attack
        return Direction.FORWARDS;
    }

    /**
     * Override the toString() method, so this object returns the teamName for use in
     * {@link android.widget.ArrayAdapter}
     * @return String the teamName
     */
    @Override
    public String toString(){
        return name;
    }

    //Implement Parcelable
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeInt(id);
        dest.writeInt(attackStrength);
        dest.writeInt(midfieldStrength);
        dest.writeInt(defenseStrength);
        dest.writeInt(points);
        dest.writeInt(goals);
        dest.writeInt(goalsAgainst);
        dest.writeInt(totalMatchesWon);
        dest.writeInt(totalMatchesLost);
        dest.writeInt(totalMatchesEven);
        dest.writeInt(aggression);
    }

    /**
     * Static field used to regenerate object, individually or as arrays
     */
    public static final Parcelable.Creator<Team> CREATOR = new Parcelable.Creator<Team>() {
        public Team createFromParcel(Parcel in) {
            return new Team(in);
        }
        public Team[] newArray(int size) {
            return new Team[size];
        }
    };

    /**Create from Parcel, it reads back int the order they were written! */
    public Team(Parcel in){
        name                = in.readString();
        id                  = in.readInt();
        attackStrength      = in.readInt();
        midfieldStrength    = in.readInt();
        defenseStrength     = in.readInt();
        points              = in.readInt();
        goals               = in.readInt();
        goalsAgainst        = in.readInt();
        totalMatchesWon     = in.readInt();
        totalMatchesLost    = in.readInt();
        totalMatchesEven    = in.readInt();
        aggression          = in.readInt();
    }
}
