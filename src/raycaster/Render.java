package raycaster;

public class Render {
	
	public static void initRender(double xfov, int width, int height) {
		xfov = xfov / 180 * Math.PI;
		double yfov = 2 * Math.atan(Math.tan(xfov / 2) * height / width);
		Vector3[][] directions = new Vector3[width][height]; //Pre-computed ray directions.
		//What if you turn around though?
		for (int x = 0; x < width; x++) {
			double xAngle = x * xfov / width - xfov / 2;
			for (int y = 0; y < height; y++) {
				double yAngle = y * yfov / height - yfov / 2;
				directions[x][y] = new Vector3(Math.tan(xAngle), Math.tan(yAngle), 1);
				directions[x][y].normalize();
			}
		}
		
		for (int i = 0; i < 11; i++) {
			long startTime = System.nanoTime(); //Time test
			render(directions, width, height);
			long endTime = System.nanoTime(); //Time test
			System.out.println((endTime - startTime) / 1000000); //Time test
		}
	}
	
	private static double distance;
	
	public static void render(Vector3[][] dirs, int width, int height) {
		Vector3 cameraPos = new Vector3(0, 0, 0); //Needs to be an actual movable position.
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
		Color color = new Color(128, 0, 256);
		
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				Ray ray = new Ray(cameraPos, dirs[x][y]);
				
				//for (shape : shapes) {
				//	for (triangle : shape.triSides) {
				//		distance = CollisionCheck.rayTriangle(ray, triangle);
				//		if (distance > 0) {
				//			distances.add(distance);
				//			colors.add(shape.color);
				//		}
				//	}
				//	for (parallelogram : shape.quadSides) {
				//		distance = CollisionCheck.rayParallelogram(ray, parallelogram);
				//		if (distance > 0) {
				//			distances.add(distance);
				//			colors.add(shape.color);
				//		}
				//	}
				//}
				//distance = max(distances)
				//Main.screen.setArgb(x, y, colors.get(distances.index(distance)).shade(distance))
				distance = CollisionCheck.rayParallelogram(ray, parallelogram); //Test
				if (distance > 0) {
					Main.screen.setArgb(x, y, color.Shade(distance)); //Temp
				}
				else distance = Double.MAX_VALUE; //Temp
				distance = Math.min(CollisionCheck.raySphere(ray, sphere), distance); //Test
				if (distance > 0) {
					Main.screen.setArgb(x, y, color.Shade(distance)); //Temp
				}
				
			}
		}
		
	}

}
