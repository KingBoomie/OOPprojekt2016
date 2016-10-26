package raycaster;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.PixelWriter;
import javafx.stage.Stage;

import java.nio.ByteBuffer;
import java.util.Arrays;

public class Main extends Application {
	
	public static PixelWriter screen;
	
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
		PixelFormat<ByteBuffer> pixelFormat = PixelFormat.getByteRgbInstance();
		stage.show();
		
		Render.initRender(90, width, height);

        final int AVERAGE_FRAMES_COUNT = 5;
        long[] renderTimes = new long[AVERAGE_FRAMES_COUNT];



		AnimationTimer loop = new AnimationTimer() {
            int i = 0;
			@Override
			public void handle(long now) {
                long startTime = System.nanoTime(); //Time test

                // rendering
                // Ignore the argument temorarily
				byte[] buffer = Render.render(null);
				Main.screen.setPixels(0, 0, width, height, pixelFormat, buffer, 0, width * 3);

                long endTime = System.nanoTime(); //Time test

                renderTimes[i] = endTime - startTime;
                i++;
                if (i == AVERAGE_FRAMES_COUNT) {
                    long avgTime = Arrays.stream(renderTimes).sum() / AVERAGE_FRAMES_COUNT;
                    System.out.printf("Rendering took %d ms, averaged over %d frames\n", avgTime / 1000000, AVERAGE_FRAMES_COUNT); //Time test
                    i = 0;
                }

			}
		};
		loop.start();


	}
	
}
