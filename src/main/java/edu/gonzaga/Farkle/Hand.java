package edu.gonzaga.Farkle;

public class Hand {
    private Die[] dice;

    public Hand(int numDice) {
        dice = new Die[numDice];
        for (int i = 0; i < numDice; i++) {
            dice[i] = new Die();
        }
    }

    public void roll() {
        for (Die die : dice) {
            die.roll();
        }
    }

    public int getDiceCount() {
        return dice.length;
    }

    public Die getDie(int index) {
        return dice[index];
    }
}
