package com.bonenkamp.PouleSimulator.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Action object, contains a simplified form of the actions a team has made for the matchsimulaiton.
 */
public class Action implements Parcelable{

    private Team.BaseAction     action;
    private Team.Direction      direction;
    private Team.TeamPosition   teamPosition;
    private int                 strength;
    private boolean             shotOnGoal;
    private boolean             madeFoul;

    /**
     * New Action constructor
     * @param action Enum From {@link com.bonenkamp.PouleSimulator.models.Team.BaseAction}.
     * @param direction Enum From {@link com.bonenkamp.PouleSimulator.models.Team.Direction}.
     * @param strength int with total strength for this action.
     */
    public Action(Team.BaseAction action, Team.Direction direction, Team.TeamPosition teamPosition,
                  int strength, boolean shotOnGoal, boolean madeFoul) {
        this.action         = action;
        this.strength       = strength;
        this.direction      = direction;
        this.teamPosition   = teamPosition;
        this.shotOnGoal     = shotOnGoal;
        this.madeFoul       = madeFoul;
    }

    public Team.BaseAction action() {
        return action;
    }

    public Team.Direction direction() {
        return direction;
    }

    public int strength() {
        return strength;
    }

    public Team.TeamPosition teamPosition() {
        return teamPosition;
    }

    public boolean isShotOnGoal() {
        return shotOnGoal;
    }

    public boolean madeFoul() {
        return madeFoul;
    }

    //Implement Parcelable

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }

    /**
     * Static field used to regenerate object, individually or as arrays
     */
    public static final Parcelable.Creator<Action> CREATOR = new Parcelable.Creator<Action>() {
        public Action createFromParcel(Parcel in) {
            return new Action(in);
        }
        public Action[] newArray(int size) {
            return new Action[size];
        }
    };

    /**Create from Parcel, it reads back int the order they were written! */
    public Action(Parcel in){

    }
}
