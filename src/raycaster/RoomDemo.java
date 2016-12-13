package raycaster;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.PixelWriter;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.nio.IntBuffer;
import java.util.ArrayList;

public class RoomDemo extends Application {
	
	public static PixelWriter screen;
	Camera camera;
	long lastNow = 0;
	double[][] rotation = {{1, 0, 0}, {0, 1, 0}, {0, 0, 1}};
	boolean w, a, s, d, up, down, yawL, yawR, pitchU, pitchD, rollL, rollR, space, t, g; //Keys
	
	public static void main(String[] args) {
		launch(args);
		System.out.println("Application closed.");
	}
	
	@Override
	public void start(Stage stage) {
		//Initialize a bunch of stuff
		int width = 1920; //480
		int height = 1040; //262
		int upscale = 4; //4
		
		stage.initStyle(StageStyle.TRANSPARENT);
		stage.setTitle("Raycaster");
		Canvas canvas = new Canvas(width, height);
		Group root = new Group(canvas);
		Scene scene = new Scene(root, width, height);
		stage.setScene(scene);
		screen = canvas.getGraphicsContext2D().getPixelWriter();
		PixelFormat<IntBuffer> pixelFormat = PixelFormat.getIntArgbInstance();
		stage.show();
		
		Render.initRender(75, width, height, upscale); //1080
		ArrayList<Shape> shapes = new ArrayList<Shape>();
		ArrayList<Sphere> spheres = new ArrayList<Sphere>();
		
		//Key state tracker
		scene.setOnKeyPressed((KeyEvent event) -> {
			switch (event.getCode()) {
				case W:			w		= true; break;
				case A:			a		= true; break;
				case S:			s		= true; break;
				case D:			d		= true; break;
				case SHIFT:		up		= true; break;
				case CONTROL:	down	= true; break;
				case Q:			yawL	= true; break;
				case E:			yawR	= true; break;
				case R:			pitchU	= true; break;
				case F:			pitchD	= true; break;
				case Z:			rollL	= true; break;
				case X:			rollR	= true; break;
				case SPACE:		space	= true; break;
				case T:			t		= true; break;
				case G:			g		= true; break;
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
				case Q:			yawL	= false; break;
				case E:			yawR	= false; break;
				case R:			pitchU	= false; break;
				case F:			pitchD	= false; break;
				case Z:			rollL	= false; break;
				case X:			rollR	= false; break;
				case SPACE:		space	= false; break;
				case T:			t		= false; break;
				case G:			g		= false; break;
			}
		});
		
		//Add all sorts of shapes you want.
		camera = new Camera(new Vector3(0, 180, -340));
		Vector3 light = new Vector3(0, 190, 0); //Point-light location, can be moved.
		Shape wire = Shape.polyPrism(new Vector3(0, 375, 0), 3, 1, 300, Color.BLACK());
		shapes.add(wire);
		Shape lamp = Shape.tetrahedron(new Vector3(0, 202.5, 0), 25, Color.LIGHT_YELLOW());
		shapes.add(lamp);
		shapes.add(Shape.cuboid(new Vector3(0, 150, 0), 500, 300, 700, new Color(92, 94, 70))); //Room
		shapes.add(Shape.cuboid(new Vector3(0, 100, 275), 300, 10, 150, new Color(88, 42, 19))); //Desk
		shapes.add(Shape.polyPrism(new Vector3(-125, 50, 225), 3, 10, 100, new Color(88, 42, 19)));
		shapes.add(Shape.polyPrism(new Vector3(125, 50, 225), 3, 10, 100, new Color(88, 42, 19)));
		spheres.add(new Sphere(new Vector3(0, 30, 150), 35, new Color(88, 42, 19))); //Chair
		shapes.add(Shape.cuboid(new Vector3(200, 150, -50), 100, 10, 300, new Color(88, 42, 19))); //Shelf
		spheres.add(new Sphere(new Vector3(200, 180, 50), 30, Color.ORANGE()));
		spheres.add(new Sphere(new Vector3(200, 180, -150), 30, Color.SALMON()));
		shapes.add(Shape.tetrahedron(new Vector3(200, 155, -50), 66.67, Color.LIGHT_PURPLE()));
		Vector3[] verts = new Vector3[4];
		double[] tw2 = {-250.0 / 2, 250.0 / 2};
		double[] tl2 = {-150.0 / 2, 150.0 / 2};
		int ti = 0;
		for (double x : tw2) {
			for (double z : tl2) {
				verts[ti] = new Vector3(-249.99999, z + 175, x - 125);
				ti++;
			}
		}
		shapes.add(new Shape(new Side[] {new Side(verts[0], verts[1], verts[2], Color.SKY_BLUE(), true)}, verts));
		
		//Start the rendering.
		AnimationTimer loop = new AnimationTimer() {
			@Override
			public void handle(long now) {
				//Delta time
				double dTime = (now - lastNow) / 1e9;
				lastNow = now;
				
				//Movement
				double ds = dTime * 50; //Camera speed
				double dr = dTime / 2;
				Vector3 translate = new Vector3(0, 0, 0); //Camera total translate this frame.
				boolean changed = false;

				// TODO: having "changed = true" everywhere is really ugly, think of a better way when it's not 2:00

				if (w)		{ translate.z += ds;		changed = true; }
				if (a)		{ translate.x -= ds;		changed = true; }
				if (s)		{ translate.z -= ds;		changed = true; }
				if (d)		{ translate.x += ds;		changed = true; }
				if (up)		{ translate.y += ds;		changed = true; }
				if (down)	{ translate.y -= ds;		changed = true; }
				if (yawL)	{ camera.rotate(0, -dr, 0);	changed = true; }
				if (yawR)	{ camera.rotate(0, dr, 0);	changed = true; }
				if (pitchU)	{ camera.rotate(-dr, 0, 0);	changed = true; }
				if (pitchD)	{ camera.rotate(dr, 0, 0);	changed = true; }
				if (rollL)	{ camera.rotate(0, 0, dr);	changed = true; }
				if (rollR)	{ camera.rotate(0, 0, -dr);	changed = true; }
				if (space) {
					camera.xAngle = 0;
					camera.yAngle = 0;
					camera.zAngle = 0;
					changed = true;
				}
				if (t) {
					Vector3 lampTrans = new Vector3(0, ds, 0);
					wire.translate(lampTrans);
					lamp.translate(lampTrans);
					light.y += ds;
					changed = true;
				} else if (g) {
					Vector3 lampTrans = new Vector3(0, -ds, 0);
					wire.translate(lampTrans);
					lamp.translate(lampTrans);
					light.y -= ds;
					changed = true;
				}
				
				camera.translate(Matrix.vectorRotate(translate, rotation));
				rotation = Matrix.rotationMatrix(camera.xAngle, camera.yAngle, camera.zAngle);
				
				//Rendering
				int[] buffer = Render.progressiveRender(shapes, spheres, camera, light, rotation, changed);
				RoomDemo.screen.setPixels(0, 0, width, height, pixelFormat, buffer, 0, width);
				
				//Time test
				//System.out.println((int)(1000 * dTime) + "ms - " + Math.round(10 / dTime) / 10.0f + "fps");
			}
		};
		
		loop.start();

	}
	
}
