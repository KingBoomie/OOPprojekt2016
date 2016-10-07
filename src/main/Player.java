package main;

/**
 * Created by Kristjan Laht on 21-Sep-16.
 */

public class Player {
    private AI ai;
    private boolean playerTypeAI;

    public Integer[] GO(GameLogic game) {
        if (playerTypeAI) {
            return this.ai.GO(game);
        }
        return null;
    }
    public Player(AI ai) {
        this.ai = ai;
        this.playerTypeAI = true;
    }
    public Player() {
        this.playerTypeAI = false;
    }
    public boolean isAI() {
        return playerTypeAI;
    }
}
