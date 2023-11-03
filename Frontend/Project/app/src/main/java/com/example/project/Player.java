package com.example.project;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private int id;
    private String playerName;
    private int number;
    private String position;
    private List<Shots> shotsList;
    private int threePointMakes;
    private int threePointAttempts;
    private int twoPointMakes;
    private int twoPointAttempts;

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
    }

    public void recordThreePointShot(boolean made) {
        this.threePointAttempts++;
        if (made) this.threePointMakes++;
    }

    public void recordTwoPointShot(boolean made) {
        this.twoPointAttempts++;
        if (made) this.twoPointMakes++;
    }

    public void addShot(Shots shot) {
        shotsList.add(shot);
    }

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
}
