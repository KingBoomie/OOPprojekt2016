package main;

import java.util.ArrayList;
import java.util.Arrays;

import static java.lang.Math.abs;

/**
 * Created by Kristjan Laht on 21-Sep-16.
 */
public class GameLogic {
    private static final int B_SIZE = 3;


    private int gameboard[][];
    private int curPlayer = 0;

    private class Point2D {
        public int y;
        public int x;

        public Point2D(int y, int x) {
            this.x = x;
            this.y = y;
        }
    }

    public GameLogic(int curPlayer) {
        this.gameboard = new int[B_SIZE][B_SIZE]; // initialized to 0
        this.curPlayer = curPlayer;
    }

    public GameLogic(int[][] board, int curPlayer) {
        this.gameboard = board;
        this.curPlayer = curPlayer;
    }

    public int move(int[] p) {
        if (gameboard[p[0]][p[1]] != 0) {
            throw new RuntimeException("Tried to play where somebody has played before");
        }
        gameboard[p[0]][p[1]] = curPlayer + 1;
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
            int player1 = Arrays.stream(gameboard[y]).filter( cell -> cell == 1).sum();
            int player2 = Arrays.stream(gameboard[y]).filter( cell -> cell == 2).sum();
            
            if (player1 == B_SIZE) {
                return 1;
            }else if (player2 == B_SIZE) {
                return 2;
            }
        }
        // check vertical
        for (int x = 0; x < B_SIZE; x++) {
            int player1 = 0;
            int player2 = 0;
            for (int y = 0; y < B_SIZE; y++) {
                if (gameboard[y][x] == 1) {
                    player1++;
                }else if(gameboard[y][x] == 2) {
                    player2++;
                }
            }
        }
        // check diag1
        {
            int player1 = 0;
            int player2 = 0;
            for (int i = 0; i < B_SIZE; i++) {
                if (gameboard[i][i] == 1) {
                    player1++;
                }else if(gameboard[i][i] == 2) {
                    player2++;
                }
            }
            if (player1 == B_SIZE) {
                return 1;
            }else if (player2 == B_SIZE) {
                return 2;
            }
        }
        // check diag2
        {
            int player1 = 0;
            int player2 = 0;
            for (int i = 0; i < B_SIZE; i++) {
                if (gameboard[i][B_SIZE - i - 1] == 1) {
                    player1++;
                }else if(gameboard[i][B_SIZE - i - 1] == 2) {
                    player2++;
                }
            }
            if (player1 == B_SIZE) {
                return 1;
            }else if (player2 == B_SIZE) {
                return 2;
            }
        }
        return 0;
    }


}
