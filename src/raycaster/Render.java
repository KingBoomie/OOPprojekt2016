package raycaster;

import java.util.ArrayList;

public class Render {
	static Vector3[] directions;
	static Vector3 cameraPos = new Vector3(0, 0, 0);
	static int[] buffer;
	static int width, height;
	
	public static void initRender(double xfov, int width, int height) {
		Render.width = width;
		Render.height = height;
		buffer = new int[width * height];
		xfov *= Math.PI / 180;
		double yfov = 2 * Math.atan(Math.tan(xfov / 2) * height / width);
		
		directions = new Vector3[width * height]; //Pre-computed ray directions.
		//What if you turn around though?
		//TODO: Separate arrays for x and y angles for faster computing.
		for (int y = 0; y < height; y++) {
			double yAngle = y * yfov / height - yfov / 2;
			for (int x = 0; x < width; x++) {
				double xAngle = x * xfov / width - xfov / 2;
				int index = y * width + x;
				directions[index] = new Vector3(Math.tan(xAngle), Math.tan(yAngle), 1);
				directions[index].normalize();
			}
		}
	}
	
	public static int[] render(ArrayList<Shape> shapes) {
		double distance, tempDistance;
		Color color = Color.SKY_BLUE();
		int index = 0;
		
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++, index++) {
				Ray ray = new Ray(cameraPos, directions[index]);
				
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
				
			}
		}
		
		return buffer;
	}

}
