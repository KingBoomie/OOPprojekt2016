package main;

//import org.jetbrains.annotations.Contract;
//import org.jetbrains.annotations.Nullable;


import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.lang.Math.abs;

public class GameLogic {
    public static final int B_SIZE = 3;
    private static final int EMPTY = -1;
    private static final int PLAYER1 = 0;
    private static final int PLAYER2 = 1;

    private int gameboard[][];
    private int curPlayer = 0;
    private Player[] players;

    public enum Events { MOVE, WINNER, START}
    private Map<Events,PointHandlers> handlers = new EnumMap<>(Events.class);


    public void addEventHandler(Events eventType, Integer[] pos, Runnable handler) {
        handlers.putIfAbsent(eventType, new PointHandlers());
        handlers.get(eventType).addEventHandler(pos, handler);
    }

    private void moveHandle(Integer[] pos) {
        this.move(pos);
        handlers.get(Events.MOVE).handle(pos);
    }

    public void gameTurn(Integer[] pos) {
        // if game over
        if (getPossibleMoves().isEmpty() || checkWinner() != -1){
            System.out.println("Game Over");

            ArrayList<Integer[]> winnerPositions = getWinnerPositions();

            if (!winnerPositions.isEmpty()) {

                System.out.println("Winner is " + checkWinner());

                for (Integer[] winnerPos : winnerPositions) {
                    handlers.get(Events.WINNER).handle(winnerPos);
                }
            }
        } else {
            if (players[curPlayer].isAI()) {
                pos = players[curPlayer].GO(this);
                moveHandle(pos);
                gameTurn(null);

            } else if (pos != null) {
                moveHandle(pos);
                gameTurn(null);
            }
        }
    }
    public void startNewGame(Player player0, Player player1) {
        // init buttons
        for (int y = 0; y < B_SIZE; y++) {
            for (int x = 0; x < B_SIZE; x++) {
                Integer[] pos = new Integer[]{y, x};
                handlers.get(Events.START).handle(pos);
            }
        }
        // init players
        players = new Player[]{player0, player1};

        // clear board
        this.gameboard = getNewGameboard();
    }



    public int[][] getGameboard() {
        int[][] tempBoard = new int[B_SIZE][B_SIZE];
        for (int y = 0; y < B_SIZE; y++) {
            tempBoard[y] = Arrays.copyOf(gameboard[y], B_SIZE);
        }
        return tempBoard;
    }

    private int[][] getNewGameboard() {
        int[][] gameboard = new int[B_SIZE][B_SIZE];
        for (int y = 0; y < B_SIZE; y++) {
            int[] tempRow = new int[B_SIZE];
            for (int x = 0; x < B_SIZE; x++) {
                gameboard[y][x] = -1;
            }
        }
        return gameboard;
    }

    public final int getCurPlayer() {
        return curPlayer;
    }



    public GameLogic(int curPlayer) {
        this.gameboard = getNewGameboard();


        this.curPlayer = curPlayer;
    }

    public GameLogic(int[][] board, int curPlayer) {
        this.gameboard = new int[B_SIZE][B_SIZE];
        for (int y = 0; y < B_SIZE; y++) {
            this.gameboard[y] = Arrays.copyOf(board[y], B_SIZE);
        }
        this.curPlayer = curPlayer;
    }

    public GameLogic(GameLogic game) {
        this.gameboard = game.getGameboard();
        this.curPlayer = game.curPlayer;
    }


    /**
     * @param 2D int array (y,x) where to move
     * @return winner, if it game is over
     */
    public int move (Integer[] p) {
        if (p == null)
            throw new RuntimeException("Tried to move to pos null");
        if (gameboard[p[0]][p[1]] != EMPTY) {
            throw new RuntimeException("Tried to play where somebody has played before");
        }
        gameboard[p[0]][p[1]] = curPlayer;
        curPlayer = abs(curPlayer - 1);

        return checkWinner();
    }

    public ArrayList<Integer[]> getPossibleMoves() {
        ArrayList<Integer[]> moves = new ArrayList<>();
        for (int y = 0; y < B_SIZE; y++) {
            for (int x = 0; x < B_SIZE; x++) {
                if (gameboard[y][x] == EMPTY) {
                    moves.add(new Integer[]{y, x});
                }
            }
        }
        return moves;
    }

    /**
     * @return -1 if no winner, 0 if player I, 1 if player II
     */
    public int checkWinner() {
        ArrayList<Integer[]> winnerPositions = getWinnerPositions();
        if (winnerPositions.isEmpty()) {
            return EMPTY;
        }
        Integer[] winnerPos = winnerPositions.get(0);
        return gameboard[winnerPos[0]][winnerPos[1]];
    }

    private ArrayList<Integer[]> getWinnerPositions () {
        // check horisontal
        for (int y = 0; y < B_SIZE; y++) {
            int player1 = Arrays.stream(gameboard[y]).filter(cell -> cell == PLAYER1).map(i -> 1).sum();
            int player2 = Arrays.stream(gameboard[y]).filter(cell -> cell == PLAYER2).map(i -> 1).sum();

            Integer x = winner(player1, player2);
            if (x != null) {
                final int finalY = y;
                return IntStream
                        .range(0, B_SIZE)
                        .mapToObj(i -> new Integer[]{finalY, i})
                        .collect(Collectors.toCollection(ArrayList::new));
            }
        }
        // check vertical
        for (int x = 0; x < B_SIZE; x++) {
            int player1 = 0;
            int player2 = 0;
            for (int y = 0; y < B_SIZE; y++) {
                if (gameboard[y][x] == PLAYER1) {
                    player1++;
                } else if (gameboard[y][x] == PLAYER2) {
                    player2++;
                }
            }

            Integer w = winner(player1, player2);
            if (w != null) {
                final int finalX = x;
                return IntStream
                        .range(0, B_SIZE)
                        .mapToObj(i -> new Integer[]{i, finalX})
                        .collect(Collectors.toCollection(ArrayList::new));
            }
        }
        // check diag1
        {
            int player1 = 0;
            int player2 = 0;
            for (int i = 0; i < B_SIZE; i++) {
                if (gameboard[i][i] == PLAYER1) {
                    player1++;
                } else if (gameboard[i][i] == PLAYER2) {
                    player2++;
                }
            }
            Integer x = winner(player1, player2);
            if (x != null) return IntStream
                    .range(0, B_SIZE)
                    .mapToObj(i -> new Integer[]{i, i})
                    .collect(Collectors.toCollection(ArrayList::new));
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
            if (x != null) return IntStream
                    .range(0, B_SIZE)
                    .mapToObj( i -> new Integer[] {i, B_SIZE - i - 1})
                    .collect(Collectors.toCollection(ArrayList::new));
        }
        return new ArrayList<Integer[]>();
    }
    private Integer winner(int player1, int player2) {
        if (player1 == B_SIZE) {
            return PLAYER1;
        } else if (player2 == B_SIZE) {
            return PLAYER2;
        }
        return null;
    }

}

class Point2d {
    int x, y;

    public Point2d(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Point2d point2d = (Point2d) o;

        if (x != point2d.x) return false;
        return y == point2d.y;

    }

    @Override
    public int hashCode() {
        int result = x;
        result = 31 * result + y;
        return result;
    }
}

class PointHandlers {

    Map<Point2d, Runnable> handles = new HashMap<>();

    public void addEventHandler (Integer[] pos_a, Runnable handler) {
        Point2d pos = new Point2d(pos_a[1], pos_a[0]);
        handles.put(pos, handler);
    }

    public void handle (Integer[] pos_a) {
        Point2d pos = new Point2d(pos_a[1], pos_a[0]);
        if (!handles.containsKey(pos))
            throw new RuntimeException("This position dosen't exist");
        handles.get(pos).run();
    }
}
