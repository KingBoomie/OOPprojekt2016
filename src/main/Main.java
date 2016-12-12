package main;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
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

        Scene scene = new Scene(root, 400, 570);


        GameLogic game = new GameLogic(0);
        UiLogic UI = new UiLogic(game);


        // Register keyboard events
        scene.setOnKeyReleased(event -> {
            int num = (event.getCode().compareTo(KeyCode.DIGIT0) - 1 );
            if (num >= 0 && num <= 9){
                final int y = num / GameLogic.B_SIZE;
                final int x = num % GameLogic.B_SIZE;
                if (game.getPossibleMoves().stream().anyMatch( p -> p[0] == y && p[1] == x)){
                    UI.gameTurn(new Integer[]{y, x});
                }
            }
        });

        // Register all button views for mouse clicks
        Set<Node> buttonSet = root.lookupAll(".grid-button");
        buttonSet.forEach(node -> {
            Button button = (Button) node;
            final int finalY = GridPane.getRowIndex(button) - 1;
            final int finalX = GridPane.getColumnIndex(button);

            button.addEventHandler(ActionEvent.ACTION, (event) -> UI.gameTurn(new Integer[]{finalY, finalX}));

            UI.addEventHandler(UiLogic.PointEvents.MOVE, new Integer[]{finalY, finalX}, () -> {
                button.setText(UiLogic.playerDisplay    [ -(game.getCurPlayer() -1 )]);
                button.setDisable(true);
                button.getStyleClass().add(UiLogic.playerDisplay[game.getCurPlayer()]);
            });

            UI.addEventHandler(UiLogic.PointEvents.WINNER, new Integer[]{finalY, finalX}, () -> {
                button.getStyleClass().removeAll(UiLogic.playerDisplay[0], UiLogic.playerDisplay[1]);
                button.getStyleClass().add("winner");
            });

            UI.addEventHandler(UiLogic.PointEvents.START, new Integer[]{finalY, finalX}, () -> {
                button.setText(Integer.toString(finalX + finalY * 3 + 1));
                button.getStyleClass().clear();
                button.getStyleClass().addAll("grid-button", "button");
                button.setDisable(false);
            });

        });


//        settings.setManaged(true);

        // TODO custom sarcastic text on beating different AI difficulties
        // TODO button and list style

        // show / hide winner when needed
        VBox settings = (VBox) root.lookup("#game-settings");
        settings.managedProperty().bind(settings.visibleProperty());
        ChoiceBox playerSelection0 = (ChoiceBox) root.lookup("#player0");
        ChoiceBox playerSelection1 = (ChoiceBox) root.lookup("#player1");

        ObservableList<Player> players = FXCollections.observableArrayList (
                new Player(),
                new Player(new AI().SUPEREASY()),
                new Player(new AI().EASY()),
                new Player(new AI().MEDIUM()),
                new Player(new AI().HARD()),
                new Player(new AI().SUPERHARD())
        );

        playerSelection0.setItems(players);
        playerSelection1.setItems(players);

        Button newGame = (Button) root.lookup("#new-game-btn");
        newGame.setOnAction((event) -> {
            Player player1 = (Player) playerSelection0.getValue();
            Player player2 = (Player) playerSelection1.getValue();
            settings.setVisible(false);
            UI.startNewGame(player1, player2);
        });
        scene.setOnKeyPressed((event) -> {
            if (event.getCode() == KeyCode.U) {
                Player player1 = (Player) playerSelection0.getValue();
                Player player2 = (Player) playerSelection1.getValue();
                if (player1 == null || player2 == null) return; // Don't do anything when we haven't selected players
                settings.setVisible(false);
                UI.startNewGame(player1, player2);
            }
        });


        UI.addEventHandler(UiLogic.GeneralEvents.GAMEOVER, (winnerString) -> {
            Text gameOver = (Text) settings.lookup("#game-over");
            Text winner = (Text) settings.lookup("#winner");
            if (winnerString != null) {
                winner.setText(winnerString + " võitis");
            } else {
                winner.setText("Mitte keegi ei võitnud");
            }
            settings.setVisible(true);
        });


        primaryStage.setTitle("Tic-Tac-Toe");
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
