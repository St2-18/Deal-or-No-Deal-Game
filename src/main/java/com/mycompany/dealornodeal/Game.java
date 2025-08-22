package com.mycompany.dealornodeal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Game {
    // Rounds: number of boxes to open in each round
    // Rounds: 1: 5, 2: 5, 3: 5, 4: 3, 5: 2 (Total = 20 boxes will be opened)
    private final int[] rounds = {5, 5, 5, 3, 2};
    private final List<Integer> prizes;
    private final boolean[] opened;
    private int currentRound;         // 0-indexed: 0 means round 1, ... 4 means round 5
    private int boxesOpenedThisRound;
    
    public Game() {
        currentRound = 0;
        boxesOpenedThisRound = 0;
        prizes = new ArrayList<>();
        // Define 22 sample prizes (for display purposes, they can be shown in two columns)
        int[] samplePrizes = {
            100, 200, 300, 400, 500, 600, 700, 800, 900, 1000,
            2000, 3000, 4000, 5000, 7500, 10000, 15000, 20000, 25000, 30000, 50000, 100000
        };
        for (int prize : samplePrizes) {
            prizes.add(prize);
        }
        // Randomize the prizes assigned to boxes
        Collections.shuffle(prizes);
        opened = new boolean[prizes.size()];
    }
    
    // Returns the prize for a given box index.
    public int getPrize(int index) {
        return prizes.get(index);
    }
    
    // Marks a box as opened and increments the count for the current round.
    public void openBox(int index) {
        if (!opened[index]) {
            opened[index] = true;
            boxesOpenedThisRound++;
        }
    }
    
    // Checks if a box is already opened.
    public boolean isBoxOpened(int index) {
        return opened[index];
    }
    
    // Returns the current round (1-indexed for display).
    public int getCurrentRound() {
        return currentRound + 1;
    }
    
    // Returns the number of boxes to open in the current round.
    public int getBoxesToOpenThisRound() {
        return rounds[currentRound];
    }
    
    // Returns the number of boxes already opened in the current round.
    public int getBoxesOpenedThisRound() {
        return boxesOpenedThisRound;
    }
    
    // Moves to the next round and resets the counter.
    public void nextRound() {
        if (currentRound < rounds.length - 1) {
            currentRound++;
            boxesOpenedThisRound = 0;
        }
    }
    
    // Calculates a dynamic bank offer based on the average of remaining prizes.
    public double getBankOffer() {
        int sumRemaining = 0;
        int countRemaining = 0;
        for (int i = 0; i < prizes.size(); i++) {
            if (!opened[i]) {
                sumRemaining += prizes.get(i);
                countRemaining++;
            }
        }
        if (countRemaining == 0) return 0;
        double average = (double) sumRemaining / countRemaining;
        return average * 0.8;  // adjustment factor
    }
    
    // Returns a sorted copy of the prize list for the prize board.
    public List<Integer> getSortedPrizes() {
        List<Integer> sorted = new ArrayList<>(prizes);
        Collections.sort(sorted);
        return sorted;
    }
}
