package edu.gonzaga.Farkle;

import java.util.Arrays;

public class Player {
    private String name;
    private int totalScore;
    private boolean[] isDieInMeld;
    
    public Player(String name) {
        this.name = name;
        this.totalScore = 0;
        this.isDieInMeld = new boolean[6];
        Arrays.fill(isDieInMeld, false);
    }

    // get player name
    public String getName() {
        return name;
    }

    // get players total score
    public int getTotalScore() {
        return totalScore;
    }

    // add to players score
    public void addScore(int points) {
        totalScore += points;
    }

    // reset meld for next roll
    public void resetMeld() {
        Arrays.fill(isDieInMeld, false);    
    }

    // get the boolean array tracking if dice are in the meld
    public boolean[] getIsDieInMeld() {
        return isDieInMeld;
    }

    public void reroll(Die[] dice) {
        for (int i = 0; i < 6; i++) {
            if (!isDieInMeld[i]) {
                dice[i].roll();
            }
        }
    }
}