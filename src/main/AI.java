package main;

//import org.jetbrains.annotations.Contract;
//import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

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
    int[][] board;
    int turn;
    String difficulty;

    Integer[] go() {
        if (difficulty.equals("Medium") ) {
            return Medium.go(board, turn);
        } else if (difficulty.equals("Hard")) {
            return Hard.go(board, turn);
        } else {
            return new Integer[]{1};
        }
    }

    public AI(int[][] board, int turn, String difficulty) {
        this.board = board;
        this.turn = turn;
        this.difficulty = difficulty;
    }
}

class Medium {
    static Integer[] go(int[][] board, int turn) {
        Integer a;
        Integer b;
        while (true) {
            a = (int) Math.round(Math.random() * 3);
            b = (int) Math.round(Math.random() * 3);
            if (board[a][b] == -1) {
                return new Integer[]{a, b};
            }
        }
    }
}

class Hard {
    static Integer[] go(int[][] board, int turn) {
        ArrayList<Integer[]> lst = Minmax.minmax(board, turn);
        int fla = Integer.MIN_VALUE;
        for (Integer[] a : lst) {
            if (a[0] > fla) {
                fla = a[0];
            }
        }
        ArrayList<Integer[]> val = new ArrayList<>();
        for (Integer[] a : lst) {
            if (a[0] == fla) {
                val.add(a);
            }
        }
        int a = (int) Math.round(Math.random() * val.size());
        return new Integer[]{val.get(a)[1], val.get(a)[2]};
    }
}

class Minmax {
    static ArrayList<Integer[]> minmax(int[][] board, int turn) {
        ArrayList<Integer[]> lst = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == -1) {
                    int[][] copy = board.clone();
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
    }


    static Integer[] negamax (GameLogic game, int maximisingPlayer) {
        GameLogic thisRound = new GameLogic(game);
        int curState = thisRound.checkWinner();
        ArrayList<Integer[]> possibleMoves = thisRound.getPossibleMoves();

        // Check if game end
        if (curState != -1) {
            return null;
        }else if(possibleMoves.isEmpty()){ // tie
            return null;
        }

        int bestValue = Integer.MIN_VALUE;
        Integer[] bestPos = {-1, -1};

        for (Integer[] pos : possibleMoves ) {
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
     * @param v useful only if v == 0 or 1
     * @return boolean neg v
     */
    //@Contract(pure = true)
    private static int negate(int v) { return -(v -1); }

}
