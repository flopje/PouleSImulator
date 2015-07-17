package com.bonenkamp.PouleSimulator.models;

/**
 * Simple Team object. Only to be used for loading the teams from a xml file into the db.
 */
public class XmlTeamEntry {
    public String name;
    public int att;
    public int mid;
    public int def;

    public XmlTeamEntry(String name, int att, int mid, int def){
        this.name = name;
        this.att = att;
        this.mid = mid;
        this.def = def;
    }
}
