package raycaster;

public class Render {
	
	public static void initRender(double xfov, int width, int height) {
		xfov = xfov / 180 * Math.PI;
		double yfov = 2 * Math.atan(Math.tan(xfov / 2) * height / width);
		Vector3[][] directions = new Vector3[width][height];
		
		for (int x = 0; x < width; x++) {
			double xAngle = x * xfov / width - xfov / 2;
			for (int y = 0; y < height; y++) {
				double yAngle = y * yfov / height - yfov / 2;
				directions[x][y] = new Vector3(Math.tan(xAngle), Math.tan(yAngle), 1);
			}
		}
		
		for (int i = 0; i < 11; i++) {
			long startTime = System.nanoTime();
			render(directions, width, height);
			long endTime = System.nanoTime();
			System.out.println((endTime - startTime) / 1000000);
		}
	}
	
	public static void render(Vector3[][] dirs, int width, int height) {
		Vector3 cameraPos = new Vector3(0, 0, 0); //Needs to be an actual movable position.
		//Test
		Triangle triangle = new Triangle(
				new Vector3(-1, 0, 3),
				new Vector3(1, 1, 3),
				new Vector3(1, -1, 3)
				);
		
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				Ray ray = new Ray(cameraPos, cameraPos.add(dirs[x][y]));
				
				//Test				
				if (CollisionCheck.rayTriangle(ray, triangle)) {
					Main.screen.setArgb(x, y, -16777216);
				}
				
			}
		}
		
	}

}
