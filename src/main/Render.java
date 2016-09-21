package main;

public class Render {
	static public void showBoard(int[][] board) {
		for (int a = 0; a < 3; a++) {
			System.out.println(board[a][0] + "│" + board[a][1] + "│" + board[a][2]);
			if (a < 2) {
				System.out.println("─┼─┼─");
			}
		}
	}
}
