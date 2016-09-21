package main;

import java.util.ArrayList;
import java.util.Arrays;

import static java.lang.Math.abs;

/**
 * Created by Kristjan Laht on 21-Sep-16.
 */
public class GameLogic {
    private static final int B_SIZE = 3;
    private static final int EMPTY = -1;
    private static final int PLAYER1 = 0;
    private static final int PLAYER2 = 1;


    private int gameboard[][];
    private int curPlayer = 0;

    public int[][] getGameboard() { return gameboard; }

    public GameLogic(int curPlayer) {
        this.gameboard = new int[B_SIZE][B_SIZE];
        int[] tempRow = new int[B_SIZE];
        Arrays.fill(tempRow, -1);
        for (int i = 0; i < B_SIZE; i++) {
            gameboard[i] = Arrays.copyOf(tempRow, tempRow.length);
        }
        this.curPlayer = curPlayer;
    }

    public GameLogic(int[][] board, int curPlayer) {
        this.gameboard = board;
        this.curPlayer = curPlayer;
    }

    public int move(int[] p) {
        if (gameboard[p[0]][p[1]] != EMPTY) {
            throw new RuntimeException("Tried to play where somebody has played before");
        }
        gameboard[p[0]][p[1]] = curPlayer;
        curPlayer = abs(curPlayer - 1);

        return checkWinner();
    }

    // pure function
    public ArrayList<Integer[]> getPossibleMoves() {
        ArrayList<Integer[]> moves = new ArrayList<>();
        for (int y = 0; y < B_SIZE; y++) {
            for (int x = 0; x < B_SIZE; x++) {
                if (gameboard[y][x] == 0) {
                    moves.add(new Integer[] {y, x});
                }
            }
        }
        return moves;
    }

    // returns 0 if no winner, 1 if player I, 2 if player II
    // pure function
    public int checkWinner() {
        // check horisontal
        for (int y = 0; y < B_SIZE; y++) {
            int player1 = Arrays.stream(gameboard[y]).filter( cell -> cell == PLAYER1).sum();
            int player2 = Arrays.stream(gameboard[y]).filter( cell -> cell == PLAYER2).sum();

            Integer x = winner(player1, player2);
            if (x != null) return x;
        }
        // check vertical
        for (int x = 0; x < B_SIZE; x++) {
            int player1 = 0;
            int player2 = 0;
            for (int y = 0; y < B_SIZE; y++) {
                if (gameboard[y][x] == PLAYER1) {
                    player1++;
                }else if(gameboard[y][x] == PLAYER2) {
                    player2++;
                }
            }

            Integer w = winner(player1, player2);
            if (w != null) return w;
        }
        // check diag1
        {
            int player1 = 0;
            int player2 = 0;
            for (int i = 0; i < B_SIZE; i++) {
                if (gameboard[i][i] == PLAYER1) {
                    player1++;
                }else if(gameboard[i][i] == PLAYER2) {
                    player2++;
                }
            }
            Integer x = winner(player1, player2);
            if (x != null) return x;
        }
        // check diag2
        {
            int player1 = 0;
            int player2 = 0;
            for (int i = 0; i < B_SIZE; i++) {
                if (gameboard[i][B_SIZE - i - 1] == PLAYER1) {
                    player1++;
                } else if (gameboard[i][B_SIZE - i - 1] == PLAYER2) {
                    player2++;
                }
            }
            Integer x = winner(player1, player2);
            if (x != null) return x;
        }
        return 0;
    }

    private Integer winner(int player1, int player2) {
        if (player1 == B_SIZE) {
            return PLAYER1;
        }else if (player2 == B_SIZE) {
            return PLAYER2;
        }
        return null;
    }


}
