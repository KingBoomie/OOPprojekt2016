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
import java.nio.IntBuffer;
import java.util.ArrayList;
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
		PixelFormat<IntBuffer> pixelFormat = PixelFormat.getIntArgbInstance();
		stage.show();
		
		Render.initRender(90, width, height);

        final int AVERAGE_FRAMES_COUNT = 10; //Time test
        long[] renderTimes = new long[AVERAGE_FRAMES_COUNT]; //Time test

		ArrayList<Shape> shapes = new ArrayList<>(Arrays.asList(
				new FlatShape(new Parallelogram []{
						new Parallelogram(
								new Vector3(-42, 24, -1337),
								new Vector3(-42, 24, 1337),
								new Vector3(-42, -24, -1337),
								new Vector3(-42, -24, 1337)
						),
						new Parallelogram(
								new Vector3(-42, 24, -1337),
								new Vector3(-42, 24, 1337),
								new Vector3(42, 44, -1337),
								new Vector3(42, 44, 1337)
						)
				}),
				new Sphere(new Vector3(10, -10, 100), 10)


		));

		AnimationTimer loop = new AnimationTimer() {
            int i = 0; //Time test
			@Override
			public void handle(long now) {
                long startTime = System.nanoTime(); //Time test

                // Rendering
				int[] buffer = Render.render(shapes);
				Main.screen.setPixels(0, 0, width, height, pixelFormat, buffer, 0, width);

				//Time test
                long endTime = System.nanoTime();
                renderTimes[i] = endTime - startTime;
                i++;
                if (i == AVERAGE_FRAMES_COUNT) {
                    long avgTime = Arrays.stream(renderTimes).sum() / AVERAGE_FRAMES_COUNT;
                    System.out.printf("Average %d FPS over %d frames\n", 1000 / (avgTime / 1000000), AVERAGE_FRAMES_COUNT);
                    i = 0;
                }
                //Time test end

			}
		};
		
		loop.start();

	}
	
}
