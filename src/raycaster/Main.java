package raycaster;


import com.momchil_atanasov.data.front.parser.*;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.PixelWriter;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.IntBuffer;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

public class Main extends Application {
	
	public static PixelWriter screen;

	public static void main(String[] args) {
		launch(args);
		//Okay, so the program doesn't continue from here.
		System.out.println("Application closed.");
	}
	
	@Override
	public void start(Stage stage) throws IOException {
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


       /* ArrayList<Shape> shapes = new ArrayList<>(Arrays.asList(
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
				new Cuboid(new Vector3(10, -10, 50), 20, 20, 20),
				new Cuboid(new Vector3(10, 0, 20), 5, 5, 5)
		));*/



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
                    System.out.printf("Average %d ms over %d frames\n", (avgTime / 1000000), AVERAGE_FRAMES_COUNT);
                    i = 0;
                }
                //Time test end

			}
		};
		
		loop.start();

	}
	
}
