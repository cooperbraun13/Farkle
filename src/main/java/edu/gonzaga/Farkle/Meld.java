package edu.gonzaga.Farkle;

public class Meld {
    private int[] meldDice;
    private int meldScore;

    public Meld() {
        meldDice = new int[6];
        meldScore = 0;
    }

    public void setMeldDie(int index, int value) {
        meldDice[index] = value;
    }

    // get the value of die in meld
    public int getMeldDie(int index) {
        return meldDice[index];
    }

    public int[] getMeldDie() {
        return meldDice;
    }

    // reset dice
    public void reset() {
        meldScore = 0;
        for (int i = 0; i < meldDice.length; i++) {
            meldDice[i] = 0;
        }
    }

    public int calculateMeldScore() {
        meldScore = 0;
        int[] meldDiceSizesCount = new int[7];
    
        for (int die : meldDice) {
            if (die != 0) {
                meldDiceSizesCount[die]++;
            }
        }
    
        // full house combo check, three of one value, two of another
        boolean hasFullHouse = false;
        int threeOfAKindValue = 0;
        int twoOfAKindValue = 0;
        int totalMeldedDice = 0;
    
        for (int i = 1; i < 7; i++) {
            if (meldDiceSizesCount[i] == 3) {
                threeOfAKindValue = i;
            } else if (meldDiceSizesCount[i] == 2) {
                twoOfAKindValue = i;
            }
            if (meldDiceSizesCount[i] > 0) {
                totalMeldedDice += meldDiceSizesCount[i];
            }
        }
    
        // confirm full house only if three and two of different values
        if (threeOfAKindValue != 0 && twoOfAKindValue != 0 && totalMeldedDice == 5) {
            hasFullHouse = true;
        }
    
        if (hasFullHouse) {
            meldScore = 1500; // full house
        } else {
            // check for a straight 
            boolean isStraight = true;
            for (int i = 1; i <= 6; i++) {
                if (meldDiceSizesCount[i] != 1) {
                    isStraight = false;
                    break;
                }
            }
    
            if (isStraight) {
                meldScore += 1000;
            } else {
                // check for 3 pairs
                int pairCount = 0;
                for (int i = 1; i < 7; i++) {
                    if (meldDiceSizesCount[i] == 2) {
                        pairCount++;
                    }
                }
    
                // 3 pairs points
                if (pairCount == 3) {
                    meldScore += 750;
                } else {
                    // check triples and higher
                    for (int i = 1; i < 7; i++) {
                        if (meldDiceSizesCount[i] >= 3) {
                            if (i == 1) { 
                                meldScore += 1000; // points for a triple of 1s
                            } else {
                                meldScore += i * 100; // points for other triples
                            }
                            // If more than three of a kind, add additional points
                            if (meldDiceSizesCount[i] > 3) {
                                meldScore += (meldDiceSizesCount[i] - 3) * ((i == 1) ? 100 : (i == 5 ? 50 : 0));
                            }
                        }
                    }
    
                    // points for 1s
                    if (meldDiceSizesCount[1] < 3) {
                        meldScore += meldDiceSizesCount[1] * 100;
                    }
    
                    // points for 5s
                    if (meldDiceSizesCount[5] < 3) {
                        meldScore += meldDiceSizesCount[5] * 50;
                    }
                }
            }
        }
        return meldScore;
    }      
}
