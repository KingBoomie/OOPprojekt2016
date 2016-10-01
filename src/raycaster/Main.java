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
		// Something's broken.
		Render.testRender();
	}
	
	@Override
	public void start(Stage stage) {
		stage.setTitle("Placeholder Title");
		Canvas canvas = new Canvas(1280, 720);
		Group root = new Group(canvas);
		Scene scene = new Scene(root, 1280, 720);
		stage.setScene(scene);
		screen = canvas.getGraphicsContext2D().getPixelWriter();;
		stage.show();
	}
	
}
