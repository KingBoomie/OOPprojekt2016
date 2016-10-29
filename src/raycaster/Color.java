package raycaster;

public class Color {
	byte r, g, b;
	
	Color(int r, int g, int b) {
		this.r = (byte)r;
		this.g = (byte)g;
		this.b = (byte)b;
	}
	
	Color(byte r, byte g, byte b) {
		this.r = r;
		this.g = g;
		this.b = b;
	}
	
	public static int toInt(int r, int g, int b) {
		return r * 65536 + g * 256 + b - 16777216;
	}
	
	public static byte toPixelFormat(byte b) {
		return (byte) ((b & 0x80) == 0 ? b : 0xff - (b & ~0x80));
	}
	
	public byte[] Shade(double dist) {
		double mult = 42 / (42 + dist);
		
		return new byte[] {
				toPixelFormat((byte)((r & 0xff) * mult)),
				toPixelFormat((byte)((g & 0xff) * mult)),
				toPixelFormat((byte)((b & 0xff) * mult))
		};
	}
	
	public static Color BLUE() {
		return new Color(0, 0, 255);
	}
	
	public static Color GREEN() {
		return new Color(0, 255, 0);
	}
	
	public static Color RED() {
		return new Color(255, 0, 0);
	}
	
	public static Color YELLOW() {
		return new Color(255, 255, 0);
	}
	
	public static Color MAGENTA() {
		return new Color(255, 0, 255);
	}
	
	public static Color CYAN() {
		return new Color(0, 255, 255);
	}
	
	public static Color BLACK() {
		return new Color(0, 0, 0);
	}
	
	public static Color GRAY() {
		return new Color(127, 127, 127);
	}
	
	public static Color WHITE() {
		return new Color(255, 255, 255);
	}
	
	public static Color NEON_GREEN() {
		return new Color(127, 255, 0);
	}
	
	public static Color ORANGE() {
		return new Color(255, 127, 0);
	}
	
	public static Color PINK() {
		return new Color(255, 0, 127);
	}
	
	public static Color PURPLE() {
		return new Color(127, 0, 255);
	}
	
	public static Color SKY_BLUE() {
		return new Color(0, 127, 255);
	}
	
	public static Color JADE() {
		return new Color(0, 255, 127);
	}
	
	public static Color DARK_RED() {
		return new Color(127, 0, 0);
	}
	
	public static Color DARK_GREEN() {
		return new Color(0, 127, 0);
	}
	
	public static Color DARK_BLUE() {
		return new Color(0, 0, 127);
	}
	
	public static Color SALMON() {
		return new Color(255, 127, 127);
	}
	
	public static Color LIGHT_GREEN() {
		return new Color(127, 255, 127);
	}
	
	public static Color LIGHT_PURPLE() {
		return new Color(127, 127, 255);
	}
	
	public static Color TEAL() {
		return new Color(0, 127, 127);
	}
	
	public static Color DARK_PURPLE() {
		return new Color(127, 0, 127);
	}
	
	public static Color DARK_YELLOW() {
		return new Color(127, 127, 0);
	}
	
	public static Color LIGHT_CYAN() {
		return new Color(127, 255, 255);
	}
	
	public static Color LIGHT_PINK() {
		return new Color(255, 127, 255);
	}
	
	public static Color LIGHT_YELLOW() {
		return new Color(255, 255, 127);
	}
	
}