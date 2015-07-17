package com.bonenkamp.PouleSimulator.models;

/**
 * Simple referee object. Only to be used for loading the referees from a xml file into the db.
 */
public class XmlRefereeEntry {
    public String name;
    public int strictness;

    public XmlRefereeEntry(String name, int strictness){
        this.name = name;
        this.strictness = strictness;
    }
}
