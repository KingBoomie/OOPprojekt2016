package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 300, 275));

        GameLogic game = new GameLogic(0);
        game.move(new int[] {0, 0});
        System.out.println("000000000000000000");
        System.out.println("llllllllllllllllll");
        Render.showBoard(game.getGameboard());
        
        //primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);

    }
}
