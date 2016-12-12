package raycaster;


import com.momchil_atanasov.data.front.parser.*;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.PixelWriter;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.IntBuffer;
import java.text.MessageFormat;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

public class Main extends Application {
	
	public static PixelWriter screen;
	Camera camera;
	long lastNow = 0;
	boolean w, a, s, d, up, down;
	PrintWriter logWriter;

	public static void main(String[] args) {
		launch(args);

		System.out.println("Application closed.");
	}
	
	@Override
	public void start(Stage stage) throws IOException {
		//Initialize a bunch of stuff


		int width = 1024;
		int height = 512;

		// Logger init

		try {
			logWriter = new PrintWriter("./log/" + System.currentTimeMillis());
		} catch (FileNotFoundException e) {
			logWriter = null;
			System.out.println("Not saving frame drawing times to log");
		}


		// JavaFX init
		stage.setTitle("Raycaster");
		Canvas canvas = new Canvas(width, height);
		Group root = new Group(canvas);
		Scene scene = new Scene(root, width, height);
		stage.setScene(scene);
		screen = canvas.getGraphicsContext2D().getPixelWriter();
		PixelFormat<IntBuffer> pixelFormat = PixelFormat.getIntArgbInstance();
		stage.show();

        // Renderer init
        int upscale = 8;
		Render.initRender(90, width, height);
        camera = new Camera();
        Vector3 light = new Vector3(30, 50, 0); //Point-light location, can be moved.

        // -------------------------------------------
        // get the obj
        // -------------------------------------------
        InputStream in = null;
        try {
            in = new FileInputStream("./3D/Inner_divide.obj");
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
            e.printStackTrace();
        }
        final IOBJParser parser = new OBJParser();
        final OBJModel model = parser.parse(in);

        // for debugging
        System.out.println(MessageFormat.format(
                "OBJ model has {0} vertices, {1} normals, {2} texture coordinates, and {3} objects.",
                model.getVertices().size(),
                model.getNormals().size(),
                model.getTexCoords().size(),
                model.getObjects().size()));

        // -------------------------------------------
        // make it into a shape
        // -------------------------------------------

        ArrayList<Shape> shapes = model.getObjects()
                .stream()
                .flatMap(object -> object.getMeshes()
                        .stream()
                        .map(mesh -> {
                            // new flatshape
                            Triangle[] triangles = mesh.getFaces()
                                    .stream()
                                    .map(objFace -> {
                                        // new triangle
                                        Vector3[] vertexes = objFace.getReferences()
                                                .stream()
                                                .map(ref -> {
                                                    OBJVertex v = model.getVertex(ref);
                                                    return new Vector3(v.x, v.y, v.z+200);
                                                })
                                                .toArray(size -> new Vector3[size]);

                                        return new Triangle(vertexes[0], vertexes[1], vertexes[2]);
                                    })
                                    .toArray(size -> new Triangle[size]);

                            return new FlatShape(triangles);
                        }))
                .collect(Collectors.toCollection(ArrayList::new));

        //Add all sorts of shapes you want.
        shapes.add(Shape.cube(new Vector3(-20, 20, 70), 10, Color.ORANGE()));
        shapes.add(Shape.cube(new Vector3(20, 20, 70), 10, Color.MAGENTA()));
        shapes.add(Shape.cube(new Vector3(-20, -20, 70), 10, Color.DARK_RED()));
        shapes.add(Shape.cube(new Vector3(20, -20, 70), 10, Color.NEON_GREEN()));
        shapes.add(Shape.polyPrism(new Vector3(0, -7.5, 70), 5, 5, 35, Color.DARK_YELLOW()));
        spheres.add(new Sphere(new Vector3(0, 17.5, 70), 9, Color.JADE()));
        shapes.add(Shape.square(new Vector3(0, -25, 0), 1e3, Color.WHITE()));


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


        AnimationTimer loop = new AnimationTimer() {
            long frameCount = 0;
            @Override
            public void handle(long now) {
                ++frameCount;

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
                int[] buffer = Render.progressiveRender(shapes, spheres, camera, light);
                Main.screen.setPixels(0, 0, width, height, pixelFormat, buffer, 0, width);

                //Time test
                System.out.println((int)(1000 * dTime) + "ms - " + Math.round(10 / dTime) / 10.0f + "fps");
                if (logWriter != null && frameCount % 2 == 0) { logWriter.println((int) (1000 * dTime)); logWriter.flush();}
            }
        };

        loop.start();
    }
	
}
