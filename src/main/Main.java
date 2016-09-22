package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.util.Set;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        final int B_SIZE = 3;
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));

        primaryStage.setTitle("Tic-Tac-Toe");
        primaryStage.setScene(new Scene(root, 400, 400));

        GameLogic game = new GameLogic(0);
        String[] playerDisplay = new String[] {"X", "Y"};


        // Register all button views for mouse clicks
        Set<Node> buttonSet = root.lookupAll(".grid-button");
        buttonSet.forEach(node -> {
            Button button = (Button) node;
            int finalY = GridPane.getRowIndex(button);
            int finalX = GridPane.getColumnIndex(button);

            button.setOnAction(event -> {
                game.move(new Integer[] {finalY-1, finalX});
                button.setText(playerDisplay[game.getCurPlayer()]);
            });
            button.setText(Integer.toString(finalX + finalY * 3 - 2));
        });

        // TODO Register keyboard input

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



        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
