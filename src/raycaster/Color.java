package raycaster;

public class Color {
	int r, g, b;
	
	Color(int r, int g, int b) {
		this.r = r;
		this.g = g;
		this.b = b;
	}
	
	public static int toInt(int r, int g, int b) {
		return r * 65536 + g * 256 + b - 16777216;
	}
	
	public int toInt() {
		return r * 65536 + g * 256 + b - 16777216;
	}
	
	public int Shade(double dist) {
		return (toInt(
				(int)(r * 100 / (100 + dist)),
				(int)(g * 100 / (100 + dist)),
				(int)(b * 100 / (100 + dist))
				));
	}
	
}