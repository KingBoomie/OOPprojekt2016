package raycaster;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.Group;
import javafx.scene.image.PixelWriter;
import javafx.scene.canvas.*;

public class Main extends Application {
	
	public static PixelWriter screen;
	
	public static void main(String[] args) {
		launch(args);
		//Okay, so the program doesn't continue from here.
		System.out.println("Application closed.");
	}
	
	@Override
	public void start(Stage stage) {
		int width = 480;
		int height = 270;
		stage.setTitle("Placeholder Title");
		Canvas canvas = new Canvas(width, height);
		Group root = new Group(canvas);
		Scene scene = new Scene(root, width, height);
		stage.setScene(scene);
		screen = canvas.getGraphicsContext2D().getPixelWriter();;
		stage.show();
		
		Render.initRender(90, width, height);
	}
	
}
