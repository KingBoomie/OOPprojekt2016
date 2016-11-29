package raycaster;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.PixelWriter;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.nio.IntBuffer;
import java.util.ArrayList;

public class Main extends Application {
	
	public static PixelWriter screen;
	Camera camera;
	long lastNow = 0;
	boolean w, a, s, d, up, down;
	
	public static void main(String[] args) {
		launch(args);
		System.out.println("Application closed.");
	}
	
	@Override
	public void start(Stage stage) {
		//Initialize a bunch of stuff
		int width = 400;
		int height = 225;
		int upscale = 4;
		
		stage.setTitle("Raycaster");
		Canvas canvas = new Canvas(width * upscale, height * upscale);
		Group root = new Group(canvas);
		Scene scene = new Scene(root, width * upscale, height * upscale);
		stage.setScene(scene);
		screen = canvas.getGraphicsContext2D().getPixelWriter();
		PixelFormat<IntBuffer> pixelFormat = PixelFormat.getIntArgbInstance();
		stage.show();
		
		Render.initRender(75, width, height);
		ArrayList<Shape> shapes = new ArrayList<Shape>();
		ArrayList<Sphere> spheres = new ArrayList<Sphere>();
		camera = new Camera();
		
		//Key state tracker
		scene.setOnKeyPressed((KeyEvent event) -> {
			switch (event.getCode()) {
				case W:			w		= true; break;
				case A:			a		= true; break;
				case S:			s		= true; break;
				case D:			d		= true; break;
				case SHIFT:		up		= true; break;
				case CONTROL:	down	= true; break;
			}
		});
		scene.setOnKeyReleased((KeyEvent event) -> {
			switch (event.getCode()) {
				case W:			w		= false; break;
				case A:			a		= false; break;
				case S:			s		= false; break;
				case D:			d		= false; break;
				case SHIFT:		up		= false; break;
				case CONTROL:	down	= false; break;
			}
		});
		
		//Add all sorts of shapes you want.
		shapes.add(Shape.cube(new Vector3(-20, 20, 70), 10, Color.ORANGE()));
		shapes.add(Shape.cube(new Vector3(20, 20, 70), 10, Color.MAGENTA()));
		shapes.add(Shape.cube(new Vector3(-20, -20, 70), 10, Color.DARK_RED()));
		shapes.add(Shape.cube(new Vector3(20, -20, 70), 10, Color.NEON_GREEN()));
		shapes.add(Shape.polyPrism(new Vector3(0, -7.5, 70), 17, 5, 35, Color.DARK_YELLOW()));
		spheres.add(new Sphere(new Vector3(0, 17.5, 70), 9, Color.JADE()));
		
		//Start the rendering.
		AnimationTimer loop = new AnimationTimer() {
			@Override
			public void handle(long now) {
				//Delta time
				double dTime = (now - lastNow) / 1e9;
				lastNow = now;
				
				//Movement
				double ds = dTime * 25; //Camera speed
				if (w)		camera.translate(new Vector3(0, 0, ds));
				if (a)		camera.translate(new Vector3(-ds, 0, 0));
				if (s)		camera.translate(new Vector3(0, 0, -ds));
				if (d)		camera.translate(new Vector3(ds, 0, 0));
				if (up)		camera.translate(new Vector3(0, ds, 0));
				if (down)	camera.translate(new Vector3(0, -ds, 0));
				
				//Rendering
				int[] buffer = Render.render(shapes, spheres, camera);
				if (upscale > 1) {
					buffer = upscale(width, height, upscale, buffer);
				}
				Main.screen.setPixels(0, 0, width * upscale, height * upscale, pixelFormat, buffer, 0, width * upscale);
				
				//Time test
				System.out.println((int)(1000 * dTime) + "ms - " + Math.round(10 / dTime) / 10.0f + "fps");
			}
		};
		
		loop.start();

	}
	
	public static int[] upscale(int width, int height, int upscale, int[] oldBuffer) {
		int[] buffer = new int[width * height * upscale * upscale];
		
		int preW = width * upscale;
		for (int y = 0; y < height; y++) {
			int preY = y * upscale;
			for (int x = 0; x < width; x++) {
				int preX = x * upscale;
				int oldI = y * width + x;
				for (int b = 0; b < upscale; b++) {
					int preA = (preY + b) * preW + preX;
					for (int a = 0; a < upscale; a++) {
						buffer[preA + a] = oldBuffer[oldI];
					}
				}
			}
		}
		
		return buffer;
	}
	
}
