package main;

/**
 * Created by oskar on 21/09/2016.
 */
public class AI {
    int[][] board;
    int turn;
    String difficulty;
}


class Medium{
    static int[] turn(int[][] board, int turn){
        int a;
        int b;
        while (true){
            a = (int) Math.round(Math.random()*3);
            b = (int) Math.round(Math.random()*3);
            if (board[a][b] == 0){
                return new int[] {a, b};
            }
        }
    }
}

class Minmax{
    static int sw(int a){
        if (a == 1)
            return 2;
        else
            return 1;
    }
    static int[] minmax(int[][] board, int turn){
        return new int[] {1};
    }
}
