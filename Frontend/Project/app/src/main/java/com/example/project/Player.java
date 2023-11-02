package com.example.project;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private String name;
    private List<Shots> shotsList;
    private int threePointMakes;
    private int threePointAttempts;
    private int twoPointMakes;
    private int twoPointAttempts;

    public Player(String name) {
        this.name = name;
        this.threePointMakes = 0;
        this.threePointAttempts = 0;
        this.twoPointMakes = 0;
        this.twoPointAttempts = 0;
        this.shotsList = new ArrayList<>();
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
        return name;
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


}
