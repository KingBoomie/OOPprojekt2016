package raycaster;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.PixelWriter;
import javafx.stage.Stage;

import java.nio.IntBuffer;
import java.util.ArrayList;

public class Main extends Application {
	
	public static PixelWriter screen;
	static long lastNow = 0; //Time test
	
	public static void main(String[] args) {
		launch(args);
		//Okay, so the program doesn't continue from here.
		System.out.println("Application closed.");
	}
	
	@Override
	public void start(Stage stage) {
		int width = 1280;
		int height = 720;
		stage.setTitle("Raycaster");
		Canvas canvas = new Canvas(width, height);
		Group root = new Group(canvas);
		Scene scene = new Scene(root, width, height);
		stage.setScene(scene);
		screen = canvas.getGraphicsContext2D().getPixelWriter();
		PixelFormat<IntBuffer> pixelFormat = PixelFormat.getIntArgbInstance();
		stage.show();
		
		Render.initRender(90, width, height);
		ArrayList<Shape> shapes = new ArrayList<Shape>();

		AnimationTimer loop = new AnimationTimer() {
			@Override
			public void handle(long now) {
                // Rendering
				int[] buffer = Render.render(shapes);
				Main.screen.setPixels(0, 0, width, height, pixelFormat, buffer, 0, width);
				
				//Time test
				System.out.println(1e9 / (now - lastNow));
				lastNow = now;
			}
		};
		
		loop.start();

	}
	
}
