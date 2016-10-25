package raycaster;

import java.nio.ByteBuffer;

import javafx.scene.image.PixelFormat;

public class Render {
	static Vector3[] directions;
	static Vector3 cameraPos = new Vector3(0, 0, 0);
	static PixelFormat<ByteBuffer> pixelFormat = PixelFormat.getByteRgbInstance();
	static byte[] buffer;
	
	public static void initRender(double xfov, int width, int height) {
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
	
	public static void render(int width, int height/*, Shape[] shapes*/) {
		//Test
		Triangle triangle = new Triangle(
				new Vector3(-1, 0, 3),
				new Vector3(1, 1, 3),
				new Vector3(1, -1, 3)
				);
		Parallelogram parallelogram = new Parallelogram(
				new Vector3(-42, 24, -1337),
				new Vector3(-42, 24, 1337),
				new Vector3(-42, -24, -1337),
				new Vector3(-42, -24, 1337)
				);
		Sphere sphere = new Sphere(new Vector3(10, -10, 100), 20);
		Color color = new Color(127, 0, 255);
		//Test end
		
		int index = 0;
		int byteIndex = 0;
		
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				Ray ray = new Ray(cameraPos, directions[index]);
				index += 1;
				
//				for (shape : shapes) {
//					for (triangle : shape.triSides) {
//						distance = CollisionCheck.rayTriangle(ray, triangle);
//						if (distance > 0) {
//							distances.add(distance);
//							colors.add(shape.color);
//						}
//					}
//					for (parallelogram : shape.quadSides) {
//						distance = CollisionCheck.rayParallelogram(ray, parallelogram);
//						if (distance > 0) {
//							distances.add(distance);
//							colors.add(shape.color);
//						}
//					}
//				}
//				distance = min(distances)
//				Main.screen.setArgb(x, y, colors.get(distances.index(distance)).shade(distance))
				
				//Test
				double distance;
				distance = CollisionCheck.rayParallelogram(ray, parallelogram);
				if (distance > 0) {
					for (int i = 0; i < 3; i++) {
						byte[] shade = color.Shade(distance);
						buffer[byteIndex] = shade[i];
						byteIndex += 1;
					}
				}
				else {
					distance = Double.MAX_VALUE;
					for (int i = 0; i < 3; i++) {
						buffer[byteIndex] = 0;
						byteIndex += 1;
					}
				}
				distance = Math.min(CollisionCheck.raySphere(ray, sphere), distance);
				if (distance > 0) {
					byteIndex -= 3; //Very temporary bandaid!
					for (int i = 0; i < 3; i++) {
						byte[] shade = color.Shade(distance);
						buffer[byteIndex] = shade[i];
						byteIndex += 1;
					}
				}
				//Test end
				
			}
		}
		
		Main.screen.setPixels(0, 0, width, height, pixelFormat, buffer, 0, width * 3);
		
	}

}
