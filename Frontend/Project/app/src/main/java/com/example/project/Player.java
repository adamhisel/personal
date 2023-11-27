package com.example.project;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a basketball player with properties such as player ID, name, number,
 * position, shooting records, and a list of shot attempts.
 *
 * @author Jagger Gourley
 */
public class Player {
    private final int id;
    private final String playerName;
    private final List<Shots> shotsList;
    private int number;
    private String position;
    private int threePointMakes;
    private int threePointAttempts;
    private int twoPointMakes;
    private int twoPointAttempts;
    private int assists;
    private int rebounds;
    private int blocks;
    private int steals;

    /**
     * Constructs a Player instance with initial details.
     *
     * @param id         the unique identifier for the player
     * @param playerName the name of the player
     * @param number     the jersey number of the player
     * @param position   the playing position of the player
     */
    public Player(int id, String playerName, int number, String position) {
        this.id = id;
        this.playerName = playerName;
        this.number = number;
        this.position = position;
        this.shotsList = new ArrayList<>();
        this.threePointMakes = 0;
        this.threePointAttempts = 0;
        this.twoPointMakes = 0;
        this.twoPointAttempts = 0;
        this.assists = 0;
        this.rebounds = 0;
        this.blocks = 0;
        this.steals = 0;
    }

    /**
     * Records the outcome of a three-point shot attempt.
     *
     * @param made true if the shot was made, false otherwise
     */
    public void recordThreePointShot(boolean made) {
        this.threePointAttempts++;
        if (made) this.threePointMakes++;
    }

    /**
     * Records the outcome of a two-point shot attempt.
     *
     * @param made true if the shot was made, false otherwise
     */
    public void recordTwoPointShot(boolean made) {
        this.twoPointAttempts++;
        if (made) this.twoPointMakes++;
    }

    /**
     * Adds a shot to the player's shot list.
     *
     * @param shot the shot to be added
     */
    public void addShot(Shots shot) {
        shotsList.add(shot);
    }

    public void incrementAssists() {
        assists++;
    }

    public void decrementAssists() {
        assists = Math.max(0, assists - 1);
    }

    public void incrementRebounds() {
        rebounds++;
    }

    public void decrementRebounds() {
        rebounds = Math.max(0, rebounds - 1);
    }

    public void incrementBlocks() {
        blocks++;
    }

    public void decrementBlocks() {
        blocks = Math.max(0, blocks - 1);
    }

    public void incrementSteals() {
        steals++;
    }

    public void decrementSteals() {
        steals = Math.max(0, steals - 1);
    }

    // Simple getters and setters
    public String getName() {
        return playerName;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public List<Shots> getShots() {
        return shotsList;
    }

    public int getThreePointMakes() {
        return threePointMakes;
    }

    public int getTwoPointMakes() {
        return twoPointMakes;
    }

    public int getId() {
        return id;
    }

    public int getAssists() { return assists; }

    public int getRebounds() { return rebounds; }

    public int getBlocks() { return blocks; }

    public int getSteals() { return steals; }
}

