package raycaster;

public class Color {
	
	public static int RGBtoInt(int r, int g, int b) {
		return r * 65536 + g * 256 + b - 16777216;
	}
	
}