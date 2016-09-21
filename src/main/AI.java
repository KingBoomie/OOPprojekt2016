package main;

import java.util.ArrayList;

/**
 * Created by oskar on 21/09/2016.
 */
public class AI {
    int[][] board;
    int turn;
    String difficulty;
}

class Medium {
    static int[] turn(int[][] board, int turn) {
        int a;
        int b;
        while (true) {
            a = (int) Math.round(Math.random() * 3);
            b = (int) Math.round(Math.random() * 3);
            if (board[a][b] == 0) {
                return new int[]{a, b};
            }
        }
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
                        int fla = 1;
                        for (Integer[] a: Minmax.minmax(copy, Math.abs(turn-1))){
                            if(-a[0]<fla){
                                fla = -a[0];
                            }
                        }
                        lst.add(new Integer[]{fla, i, j});
                    }
                }
            }
        }
        return lst;
    }
}
