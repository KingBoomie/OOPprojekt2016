package raycaster;

public class Render {
	
	public static void render(int width, int height) {
		double xfov = Math.PI / 2; //90 degrees
		double yfov = 1.024779; //Assuming 16:9 aspect ratio
		
		//long startTime = System.nanoTime();
		for (int x = 0; x < width; x++) {
			double xAngle = x * xfov / width - xfov / 2;
			for (int y = 0; y < height; y++) {
				double yAngle = y * yfov / height - yfov / 2;
				
				Ray ray = new Ray(
						new Vector3(0, 0, 0), //Needs to be camera position
						//And offset from camera position
						new Vector3(Math.tan(xAngle), Math.tan(yAngle), 1)
						);
				
				//Tests
				Triangle triangle = new Triangle(
						new Vector3(-1, 0, 3),
						new Vector3(1, 1, 3),
						new Vector3(1, -1, 3)
						);
				
				if (true) {
					Main.screen.setArgb(x, y, -16776961);
				}
				
				if (CollisionCheck.rayTriangle(ray, triangle)) {
					Main.screen.setArgb(x, y, -16777216);
				}
				
			}
		}
		//long endTime = System.nanoTime();
		//System.out.println(endTime - startTime);
	}
	
	public static void testRender(int width, int height) {
		int c = -1;
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++, c -= 16777216 / width / height) {
				Main.screen.setArgb(x, y, c);
			}
		}
	}
}
