package main;

//import org.jetbrains.annotations.Contract;
//import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by oskar on 21/09/2016.
 *
 * Anna seis, käigu tegija ja raskusaste.
 * Annab vastu koordinaadid käiguga.
 * Kui anda viik, siis ei tee suurt midagi.
 *
 * negamax added by Kristjan
 */
public class AI {
    String difficulty;
    GameLogic game;

    public AI(String difficulty, GameLogic game) {
        this.difficulty = difficulty;
        this.game = game;
    }

    Integer[] go() {
        if (difficulty.equals("Medium") ) {
            return Medium.go(game);
        } else if (difficulty.equals("Hard")) {
            return Hard.go(game);
        } else {
            throw new RuntimeException("Difficulty selected incorrectly");
        }
    }

}

class Medium {
    static Integer[] go(GameLogic game) {
        int[][] board = game.getGameboard();
        Integer a;
        Integer b;
        while (true) {
            a = ThreadLocalRandom.current().nextInt(0, board[0].length);
            b = ThreadLocalRandom.current().nextInt(0, board[1].length);
            try {
                game.move(new Integer[] {a, b});
                return new Integer[] {a, b};
            } catch (RuntimeException exception) {}
        }
    }
}

class Hard {
    static Integer[] go(GameLogic game) {
        Moves possibleMoves = Minmax.minmax(game);
        Integer bestValue = Collections.max(possibleMoves.values);
        Moves goodMoves = new Moves();
        for (int i = 0; i < possibleMoves.values.size(); i++) {
            if (possibleMoves.values.get(i) == bestValue) {
                goodMoves.coordinates.add(possibleMoves.coordinates.get(i));
                goodMoves.values.add(possibleMoves.values.get(i));
            }
        }
        int outIndex = ThreadLocalRandom.current().nextInt(0, goodMoves.coordinates.size());
        return goodMoves.coordinates.get(outIndex);
    }
}

class Moves {
    ArrayList<Integer[]> coordinates;
    ArrayList<Integer> values;

    public Moves() {
        this.coordinates = new ArrayList<Integer[]>();
        this.values = new ArrayList<Integer>();
    }
}

class Minmax {
    /*static ArrayList<Integer[]> minmax(int[][] board, int turn) {
        ArrayList<Integer[]> lst = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == -1) {
                    int[][] copy = deepcopy(board);
                    copy[i][j] = turn;
                    if (new GameLogic(copy, turn).checkWinner() == turn) {
                        lst.add(new Integer[]{1, i, j});
                    } else if (new GameLogic(copy, turn).checkWinner() == Math.abs(turn - 1)) {
                        lst.add(new Integer[]{-1, i, j});
                    } else {
                        int fla = 0;
                        for (Integer[] a : Minmax.minmax(copy, Math.abs(turn - 1))) {
                            fla += a[0];
                        }
                        lst.add(new Integer[]{fla, i, j});
                    }
                }
            }
        }
        return lst;
    }*/

    /**
     * Useful for finding both best and worst moves
     * Unoptimised, could be made faster for specifically best or worst moves with alpha-beta pruning.
     */
    static Moves minmax(GameLogic game) {
        Moves out = new Moves();
        int player = game.getCurPlayer();
        ArrayList<Integer[]> posMoves = game.getPossibleMoves();
        for (Integer[] i : posMoves) {
            GameLogic tempGame = new GameLogic(game);
            tempGame.move(i);
            if (((Integer) tempGame.checkWinner()).equals(player)) { //TODO checkWinner is bugged
                out.coordinates.add(i);
                out.values.add(1);
            } else if (((Integer) tempGame.checkWinner()).equals(negate(player))) {
                out.coordinates.add(i);
                out.values.add(-1);
            } else if (tempGame.getPossibleMoves().isEmpty()) { //TODO tie implementation in checkWinner?
                out.coordinates.add(i);
                out.values.add(0);
            } else {
                Moves turnVals = minmax(tempGame);
                int bestValue = -Collections.max(turnVals.values);
                out.coordinates.add(i);
                out.values.add(bestValue);
            }
        }
        return out;
    }

    //@Nullable
    //@Contract(pure = true)
    static Integer[] negamax (GameLogic game, int maximisingPlayer) {
        GameLogic thisRound = new GameLogic(game);
        int curState = thisRound.checkWinner();
        ArrayList<Integer[]> possibleMoves = thisRound.getPossibleMoves();

        // Check if game end
        if (curState != -1) {
            return null;
        } else if (possibleMoves.isEmpty()) { // tie
            return null;
        }

        int bestValue = Integer.MIN_VALUE;
        Integer[] bestPos = {-1, -1};

        for (Integer[] pos : possibleMoves) {
            thisRound.move(pos);
            int value = -negamaxImpl(thisRound, negate(maximisingPlayer));
            System.out.printf("Value %d\n", value);
            if (value > bestValue) {
                bestValue = value;
                bestPos = pos;
            }
        }
        return bestPos;

    }

    //@Contract(pure = true)
    private static int negamaxImpl (GameLogic game, int maximisingPlayer) {
        GameLogic thisRound = new GameLogic(game);
        int curWinner = thisRound.checkWinner();
        ArrayList<Integer[]> possibleMoves = thisRound.getPossibleMoves();

        // Check if game end
        if (curWinner != -1) {
            if (curWinner == maximisingPlayer) {
                return 1;
            } else {
                return -1;
            }
        }else if(possibleMoves.isEmpty()){ // tie
            return 0;
        }


        int retValue = 0;
        for (Integer[] pos : possibleMoves ) {
            thisRound.move(pos);
            int value = -negamaxImpl(thisRound, negate(maximisingPlayer));
            retValue += value;
        }
        return retValue;

    }

    /**
     * Only useful for 2-dimensional boards.
     */
    public static int[][] deepcopy(int[][] board) {
        int[][] tempBoard = new int[board.length][board[0].length];
        for (int y = 0; y < board.length; y++) {
            tempBoard[y] = Arrays.copyOf(board[y], board[y].length);
        }
        return tempBoard;
    }

    /**
     * @param v useful only if v == 0 or 1
     * @return boolean neg v
     */
    //@Contract(pure = true)
    private static int negate(int v) { return -(v -1); }

}
