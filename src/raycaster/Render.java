package raycaster;

import java.util.ArrayList;
import java.util.stream.IntStream;

public class Render {
	static Vector3[] directions;
	static int[] buffer;
	static int width, height;
	
	public static void initRender(double xfov, int width, int height) {
		Render.width = width;
		Render.height = height;
		buffer = new int[width * height];
		xfov *= Math.PI / 180;
		double yfov = 2 * Math.atan(Math.tan(xfov / 2) * height / width);
		
		directions = new Vector3[width * height];
		int index = 0;
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++, index++) {
				double yAngle = y * yfov / height - yfov / 2;
				double xAngle = x * xfov / width - xfov / 2;
				directions[index] = new Vector3(Math.tan(xAngle), Math.tan(yAngle), 1);
				directions[index].normalize();
			}
		}
		//TODO: Directions could be stored in a format that's ready for matrix multiplication because they won't be used before being multiplied anyways.
	}
	
	public static int[] render(ArrayList<Shape> shapes, Camera camera) {
		//TODO: Calculate rotation matrix here? Perhaps a separate class for it?
		//matrix = MatriceClassName.rotationMatrix(Camera.xAngle, Camera.yAngle, Camera.zAngle);
		//Or something...
		
		IntStream.range(0, buffer.length).parallel().forEach(index -> {
			//Vector3 direction = MatriceClassName.multiply(directions[index], matrix);
			Vector3 direction = directions[index]; //Temporary until the above gets implemented.
			Ray ray = new Ray(camera.position, direction);
			double distance, tempDistance;
			Color color = Color.SKY_BLUE();
			
			distance = Double.MAX_VALUE;
			for (Shape shape : shapes) {
				for (Side side : shape.sides) {
					tempDistance = CollisionCheck.raySide(ray, side);
					if (tempDistance > 0 && tempDistance < distance) {
						distance = tempDistance;
						color = side.color;
					}
				}
			}
			
			if (distance == Double.MAX_VALUE) {
				buffer[index] = Color.SKY_BLUE().color; //Default "skybox" color.
			} else {
				buffer[index] = color.shade(distance);
			}
			
		});
		
		return buffer;
	}

}
