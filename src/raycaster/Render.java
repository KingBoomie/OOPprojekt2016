package raycaster;

public class Render {
	
	public static void render() {
		Main.screen.setArgb(100, 100, -1);
	}
	
	public static void testRender() {
		int c = -1;
		for (int x = 0; x < 1280; x++) {
			for (int y = 0; y < 720; y++, c -= 18) {
				Main.screen.setArgb(x, y, c);
			}
		}
	}
}
