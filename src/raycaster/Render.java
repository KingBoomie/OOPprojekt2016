package raycaster;

import java.util.ArrayList;

public class Render {
	static Vector3[] directions;
	static Vector3 cameraPos = new Vector3(0, 0, 0);
	static byte[] buffer;
	static int width;
	static int height;
	
	public static void initRender(double xfov, int width, int height) {
		Render.width = width;
		Render.height = height;
		buffer = new byte[width * height * 3];
		
		xfov = xfov / 180 * Math.PI;
		double yfov = 2 * Math.atan(Math.tan(xfov / 2) * height / width);
		directions = new Vector3[width * height]; //Pre-computed ray directions.
		//What if you turn around though?
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
	
	public static byte[] render(ArrayList<Shape> shapes) {
		double distance;
		int index = 0;
		int byteIndex = 0;
		ArrayList<Double> distances = new ArrayList<>();
		ArrayList<Color> colors = new ArrayList<>();
		
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				Ray ray = new Ray(cameraPos, directions[index]);
				index += 1;
				
				for (Shape shape : shapes) {
					for (Triangle triangle : shape.triSides) {
						distance = CollisionCheck.rayTriangle(ray, triangle);
						if (distance > 0) {
							distances.add(distance);
							colors.add(shape.color);
						}
					}
					for (Parallelogram parallelogram : shape.quadSides) {
						distance = CollisionCheck.rayParallelogram(ray, parallelogram);
						if (distance > 0) {
							distances.add(distance);
							colors.add(shape.color);
						}
					}
				}
				distance = Double.MAX_VALUE;
				int iMax = -1;
				for (int i = 0; i < distances.size(); i++) {
					if (distance < distances.get(i)) {
						iMax = i;
						distance = distances.get(i);
					}
				}
				if (iMax > 0) {
					for (int i = 0; i < 3; i++) {
						byte[] shade = colors.get(iMax).Shade(distance);
						buffer[byteIndex] = shade[i];
						byteIndex += 1;
					}
				}
				
			}
		}
		
		return buffer;
	}

}
