package com.bonenkamp.PouleSimulator.logic;

import android.util.Log;

import com.bonenkamp.PouleSimulator.data.UpdateTeamStats;
import com.bonenkamp.PouleSimulator.fragments.TournamentMatchFragment;
import com.bonenkamp.PouleSimulator.models.Action;
import com.bonenkamp.PouleSimulator.models.Match;
import com.bonenkamp.PouleSimulator.models.Referee;
import com.bonenkamp.PouleSimulator.models.Team;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/**
 *
 * Contains the basic code for the simulation of a match.
 */
public class MatchSimulation  {

    /**
     * Time between every simulation, in ms.
     */
    private static final int SIMULATION_INTERVAL_TIME   = 500;

    /**
     * Match duration in (minutes)
     */
    private static final int MATCH_TIME = 10;

    /**
     * 1 second in ms. Needed for game-time calculation.
     */
    private static final int SECOND = 1000;

    private Team teamHome;
    private Team teamAway;
    private Match match;
    private Random generator;
    private boolean continueSimulation = true;
    private boolean simulationDone = false;
    private Timer timer;

    private TournamentMatchFragment.StartSimulationAsync task;

    public MatchSimulation() {}

    /**
     * Start the match simulation. Generate via a {@link Random} number the start team, and start a
     * {@link TimerTask} wich executes the needed functions at set interval time.
     * @param match The {@link Match} to be simulated
     * @param task The AsyncTask from where this function is called, so the ui can be updated during
     *             the simulation.
     * @return The simulated match as {@link Match} with the goals set.
     */
    public Match startSimulation(Match match, int startTime, TournamentMatchFragment.StartSimulationAsync task){

        this.teamHome   = match.teamHome();
        this.teamAway   = match.teamAway();
        this.match      = match;
        this.task       = task;

        // Choose with 'random' coin-toss idea the beginning team.
        generator = new Random();

        if(generator.nextInt(2) == 0){
            startMatch(teamHome, startTime);
        }else{
            startMatch(teamAway, startTime);
        };
        // Lock return match object until simulation is done.
        synchronized (match){
            while(!simulationDone){
                try {
                    match.wait();
                }catch (InterruptedException e){
                    Log.e("Task interrupted", e.toString());
                    return null;
                }
            }
        }

        return match;
    }

    private void startMatch(Team startTeam, int startTime){
        // Start from the start position of the game.
        teamHome.setTeamPosition(Team.TeamPosition.MIDDLE);
        teamAway.setTeamPosition(Team.TeamPosition.MIDDLE);
        startTeam.hasBall = true;

        final int startTimeSeconds = startTime;

        // New timer task which calls the simulation every set interval.
        timer = new Timer();
        timer.schedule(new TimerTask() {

            int seconds = startTimeSeconds * (SECOND / SIMULATION_INTERVAL_TIME);
            int gameTime = MATCH_TIME * (SECOND / SIMULATION_INTERVAL_TIME);

            @Override
            public void run() {

                // Match done, or interrupted.
                if ( seconds == gameTime) {
                    synchronized (match) {
                        endMatch();
                        match.notifyAll();
                    }
                    cancel();

                } else if (!continueSimulation) {
                    synchronized (match) {
                        match.notifyAll();
                    }
                    cancel();

                } else {
                    /**
                     * Notify Asynctask to display standings and game time.
                     * Update ui every game minute, approx. every second real time.
                     */
                    if ( seconds % (SECOND / SIMULATION_INTERVAL_TIME) == 0) {
                        String[] progress = {
                                String.valueOf(
                                        (seconds / (SECOND / SIMULATION_INTERVAL_TIME)) + 1),
                                match.getCurrentResult()
                        };
                        task.progress(progress);
                    }

                    if ( teamHome.hasBall) {
                        //TeamHome has the ball.
                        match.addBallPossessionHome();
                        matchCalculation(teamHome, teamAway);
                    } else if (teamAway.hasBall) {
                        //TeamAway has te ball.
                        match.addBallPossessionAway();
                        matchCalculation(teamAway, teamHome);
                    }
                    seconds++;
                }
            }
        }, 0, SIMULATION_INTERVAL_TIME);
    }

    /**
     * The actual match simulation calculations. Needs the team that is attacking and the team that
     * is defending.
     * @param attackTeam    {@link Team} object with attacking team
     * @param defenseTeam   {@link Team} object with the defense team
     */
    private void matchCalculation(Team attackTeam, Team defenseTeam) {

        Action actionAttack = attackTeam.getAction();
        Action actionDefense = defenseTeam.getAction();

        if(actionAttack.action() == Team.BaseAction.ATTACK){
            if(actionAttack.strength() > actionDefense.strength()) {
                if (!actionAttack.madeFoul()) {
                    if (actionAttack.isShotOnGoal()) {
                        goal(attackTeam);
                    } else {
                        attackTeam.adjustTeamPosition(actionAttack.action());
                        defenseTeam.adjustTeamPosition(actionDefense.action());
                    }
                }else {
                    // Made Foul
                    if (refereeWhistles())
                    attackTeam.hasBall = false;
                    defenseTeam.hasBall = true;
                }
            }else{
                // Failed attack, team stays same sector for one calculation
                attackTeam.hasBall = false;
                defenseTeam.hasBall = true;
            }
        }else{
            if ( !actionAttack.madeFoul()) {
                if( actionAttack.strength() > actionDefense.strength()) {
                    attackTeam.adjustTeamPosition(actionAttack.action());
                }else{
                    attackTeam.hasBall = false;
                    defenseTeam.hasBall = true;
                }
            } else {
                attackTeam.hasBall = false;
                defenseTeam.hasBall = true;
            }
        }
    }

    /**
     * Called when a goal has been scored. Needs the {@link Team} object that has scored. Resets the
     * team positions to {@link com.bonenkamp.PouleSimulator.models.Team.TeamPosition} MIDDLE.
     * (Aftrap positie) adds the goal to the corresponding team in the match goal total.
     * Finally this method set the {@link Team#hasBall} value to false on the team that scored,
     * and true on the opposing team.
     * @param team
     */
    private void goal(Team team) {
        // goal
        if(match.teamHome().id() == team.id()) {
            // Add a goal to the teams' total
            match.addGoalTeamHome();
            match.setMatchResult(match.goalsTeamHome(), match.goalsTeamAway(), false, 0);

            // Resume match, other team may begin
            teamHome.hasBall = false;
            teamAway.hasBall = true;

            teamHome.setTeamPosition(Team.TeamPosition.MIDDLE);
            teamAway.setTeamPosition(Team.TeamPosition.MIDDLE);
        } else if (match.teamAway().id() == team.id()){
            match.addGoalTeamAway();
            match.setMatchResult(match.goalsTeamHome(), match.goalsTeamAway(), false, 0);

            teamHome.hasBall = true;
            teamAway.hasBall = false;

            teamHome.setTeamPosition(Team.TeamPosition.MIDDLE);
            teamAway.setTeamPosition(Team.TeamPosition.MIDDLE);
        }
    }

    /**
     * A foul is made, now return if the referee will discontinue the attack
     * @return boolean True if the referee whistles, and a freekick is given.
     */
    private boolean refereeWhistles() {
        int strictness = match.referee().stricktness();

        // Normally the change is 1 out 3 fouls is 'seen'
        Random random = new Random();
        int number = random.nextInt(100);

        // Percentage of fouls the referee will not flag down.
        int percentageOfFoulsRejected = ( strictness * 10 ) + 10;

        if ( number > percentageOfFoulsRejected ) {
            return false;
        }

        return true;
    }

    /**
     * Call to end the match.
     * This function calls {@link MatchSimulation#stopSimulation()} and sets the
     * {@link Match#setMatchResult(int, int, boolean, int)} in the match object.
     */
    private void endMatch() {
        simulationDone = true;
        stopSimulation();

        // Set results
        int totalPlayTime = MATCH_TIME * (SECOND / SIMULATION_INTERVAL_TIME);
        match.setMatchResult(match.goalsTeamHome(), match.goalsTeamAway(), true, totalPlayTime);
    }

    /**
     * Stop the current simulation. Sets both {@link MatchSimulation#continueSimulation} and
     * the {@link MatchSimulation#simulationDone} variable to false.
     */
    public void stopSimulation() {
        continueSimulation = false;
        timer.cancel();
        timer.purge();
    }
}
