package com.bonenkamp.PouleSimulator.models;

/**
 * Class containing information about the referee
 */
public class Referee {
    private int id;
    private String name;
    private int strictness;

    /**
     * Constructor for new referee
     * @param id            int with unique Database id
     * @param name          String with referee name
     * @param strictness    int with strictness level (max 5)
     */
    public Referee(int id, String name, int strictness) {
        this.id             = id;
        this.name           = name;
        this.strictness     = strictness;

        // Value check
        if(strictness > 5) strictness  = 5;
    }

    public int id() {
        return id;
    }

    public String name() {
        return name;
    }

    public int stricktness() {
        return strictness;
    }

}
