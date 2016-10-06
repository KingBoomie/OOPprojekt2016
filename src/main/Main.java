package main;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
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

        Scene scene = new Scene(root, 400, 400);


        GameLogic game = new GameLogic(0);
        GameLogic game2 = new GameLogic(new int[][] {{0, 1, 1}, {0, 0, 0}, {1, 0, 1}}, 1);
        System.out.println(game2.checkWinner());
        String[] playerDisplay = new String[] {"X", "Y"};

        // Register keyboard events
        scene.setOnKeyReleased(event -> {
            int num = (event.getCode().compareTo(KeyCode.DIGIT0) - 1 );
            final int y = num / GameLogic.B_SIZE;
            final int x = num % GameLogic.B_SIZE;
            game.moveHandle(y, x, event);
        });


        // Register all button views for mouse clicks
        Set<Node> buttonSet = root.lookupAll(".grid-button");
        buttonSet.forEach(node -> {
            Button button = (Button) node;
            final int finalY = GridPane.getRowIndex(button) -1;
            final int finalX = GridPane.getColumnIndex(button);

            button.addEventHandler(ActionEvent.ACTION, event -> game.moveHandle(finalY, finalX, event));

            game.addEventHandler(finalY, finalX, event -> button.setText(playerDisplay[game.getCurPlayer()]));

            button.setText(Integer.toString(finalX + finalY * 3 + 1));
        });


/*        for(;;){
            // TODO negamax isn't actually a good AI
            // TODO add GUI interactivity
            Integer[] nextMove = Minmax.negamax(game, 0);
            if (nextMove == null) { // game over
                break;
            }
            game.move(nextMove);
            Render.showBoard(game.getGameboard());
        }*/


        primaryStage.setTitle("Tic-Tac-Toe");
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
