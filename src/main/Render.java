package main;

public class Render {
	static public void showBoard(int[][] board) {
		String[] XO = {"X", "O", " "};
		
		for (int a = 0; a < 3; a++) {
			System.out.println(XO[board[a][0]] + "│" + XO[board[a][1]] + "│" + XO[board[a][2]]);
			if (a < 2) {
				System.out.println("─┼─┼─");
			}
		}
	}
}
