package main;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.Map;
import java.util.function.Consumer;

public class UiLogic {
    public static final String[] playerDisplay = new String[] {"X", "Y"};

    private final GameLogic gameLogic;
    private Player[] players;

    enum PointEvents {MOVE, WINNER, START}
    enum GeneralEvents {GAMEOVER}
    private Map<PointEvents,PointHandlers> pointHandlers = new EnumMap<>(PointEvents.class);
    private Map<GeneralEvents, Consumer> generalHandlers = new EnumMap<>(GeneralEvents.class);


    public UiLogic(GameLogic gameLogic) {
        this.gameLogic = gameLogic;
    }

    public void addEventHandler(PointEvents eventType, Integer[] pos, Runnable handler) {
        pointHandlers.putIfAbsent(eventType, new PointHandlers());
        pointHandlers.get(eventType).addEventHandler(pos, handler);
    }
    public void addEventHandler(GeneralEvents eventType, Consumer handler) {
        generalHandlers.putIfAbsent(eventType, handler);
    }


    private void moveHandle(Integer[] pos) {
        gameLogic.move(pos);
        pointHandlers.get(PointEvents.MOVE).handle(pos);
    }

    public void gameTurn(Integer[] pos) {
        // if game over
        if (gameLogic.getPossibleMoves().isEmpty() || gameLogic.checkWinner() != -1) {
            System.out.println("Game Over");
            String winner = null;

            ArrayList<Integer[]> winnerPositions = gameLogic.getWinnerPositions();
            if (!winnerPositions.isEmpty()) {
                winner = playerDisplay[gameLogic.checkWinner()];
                System.out.println("Winner is " + winner);
                for (Integer[] winnerPos : winnerPositions) {
                    pointHandlers.get(PointEvents.WINNER).handle(winnerPos);
                }
            }
            generalHandlers.get(GeneralEvents.GAMEOVER).accept(winner);
        } else {
            if (players[gameLogic.getCurPlayer()].isAI()) {
                pos = players[gameLogic.getCurPlayer()].GO(gameLogic);
                moveHandle(pos);
                gameTurn(null);

            } else if (pos != null) {
                moveHandle(pos);
                gameTurn(null);
            }
        }
    }

    public void startNewGame(Player player0, Player player1) {
        // clear board
        gameLogic.clearGame();

        // init buttons
        for (int y = 0; y < GameLogic.B_SIZE; y++) {
            for (int x = 0; x < GameLogic.B_SIZE; x++) {
                Integer[] pos = new Integer[]{y, x};
                pointHandlers.get(PointEvents.START).handle(pos);
            }
        }
        // init players
        this.players = new Player[]{player0, player1};

        // first turn
        gameTurn(null);
    }


}