package main;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.Set;

public class Main extends Application {

    public interface GameEventListener {
        void onMove(Integer[] position);
    }


    @Override
    public void start(Stage primaryStage) throws Exception{
        final int B_SIZE = 3;
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));

        Scene scene = new Scene(root, 400, 600);


        GameLogic game = new GameLogic(0);

        // these games are for testing win conditions
        /*GameLogic game3 = new GameLogic(new int[][] {{0, 1, 1}, {0, 0, 0}, {1, 0, 1}}, 1);
        GameLogic game2 = new GameLogic(new int[][] {{0, 1, 0}, {1, 0, 1}, {1, 1 ,0}}, 1);
        GameLogic game4 = new GameLogic(new int[][] {{0, 1, 0}, {0, 0, 1}, {0, 1 ,1}}, 1);
        System.out.println(game2.checkWinner());
        */
        String[] playerDisplay = new String[] {"X", "Y"};

        // Register keyboard events
        scene.setOnKeyReleased(event -> {
            int num = (event.getCode().compareTo(KeyCode.DIGIT0) - 1 );
            final int y = num / GameLogic.B_SIZE;
            final int x = num % GameLogic.B_SIZE;
            game.gameTurn(new Integer[]{y, x});
        });

        // Register all button views for mouse clicks
        Set<Node> buttonSet = root.lookupAll(".grid-button");
        buttonSet.forEach(node -> {
            Button button = (Button) node;
            final int finalY = GridPane.getRowIndex(button) -1;
            final int finalX = GridPane.getColumnIndex(button);

            button.addEventHandler(ActionEvent.ACTION, (event) -> game.gameTurn(new Integer[]{finalY, finalX}));

            game.addEventHandler(GameLogic.Events.MOVE, new Integer[]{finalY, finalX}, () -> {
                button.setText(playerDisplay[game.getCurPlayer()]);
                button.setDisable(true);
                button.getStyleClass().add(playerDisplay[game.getCurPlayer()]);
            });

            game.addEventHandler(GameLogic.Events.WINNER, new Integer[]{finalY, finalX}, () -> {
                button.getStyleClass().removeAll(playerDisplay[0], playerDisplay[1]);
                button.getStyleClass().add("winner");
            });

            game.addEventHandler(GameLogic.Events.START, new Integer[]{finalY, finalX}, () -> {
                button.setText(Integer.toString(finalX + finalY * 3 + 1));
                button.getStyleClass().clear();
                button.getStyleClass().add("grid-button");
                button.setDisable(false);
            });

        });

        // show / hide winner when needed
        VBox settings = (VBox) root.lookup("#game-settings");
        settings.setManaged(true);
        // TODO show text on game over
        // TODO show other text on SB victory
            // TODO custom text on beating different AI difficulties
        // TODO populate player choosal lists with AI's
            // TODO figure out how to store AI's
            // TODO newgame button handler
        // TODO logic for hiding/showing the options/(game over) menu
        // TODO set better pref-s in fxml
        // TODO check window size
        // TODO button and list style
        // TODO keyboard button for new game
        // TODO block hover style
        // FIXME player 0 starts with 'Y' not 'X'

        ListView player0 = (ListView) settings.lookup("#player0");
        ListView player1 = (ListView) settings.lookup("#player1");
        //player0.setItems();

        Button newGame = (Button) settings.lookup("#new-game-btn");
        game.addEventHandler(GameLogic.Events.START, new Integer[]{-1, -1}, () -> {

            //game.startNewGame();
        });





        primaryStage.setTitle("Tic-Tac-Toe");
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
