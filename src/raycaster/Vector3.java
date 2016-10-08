package raycaster;

public class Vector3 {
	double x, y, z;
	
	Vector3(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public static Vector3 cross(Vector3 a, Vector3 b) {
		return new Vector3(
				a.y * b.z - a.z * b.y,
				a.z * b.x - a.x * b.z,
				a.x * b.y - a.y * b.x
				);
	}
	
	public Vector3 cross(Vector3 b) {
		return new Vector3(
				y * b.z - z * b.y,
				z * b.x - x * b.z,
				x * b.y - y * b.x
				);
	}
	
	public static double dot(Vector3 a, Vector3 b) {
		return a.x * b.x + a.y * b.y + a.z * b.z;
	}
	
	public double dot(Vector3 b) {
		return x * b.x + y * b.y + z * b.z;
	}
	
	public static Vector3 sub(Vector3 a, Vector3 b) {
		return new Vector3(
				a.x - b.x,
				a.y - b.y,
				a.z - b.z
				);
	}
	
	public Vector3 sub(Vector3 b) {
		return new Vector3(
				x - b.x,
				y - b.y,
				z - b.z
				);
	}
	
	public static Vector3 add(Vector3 a, Vector3 b) {
		return new Vector3(
				a.x + b.x,
				a.y + b.y,
				a.z + b.z
				);
	}
	
	public Vector3 add(Vector3 b) {
		return new Vector3(
				x + b.x,
				y + b.y,
				z + b.z
				);
	}

	public static Vector3 mult(Vector3 a, double m) {
		return new Vector3(
				a.x * m,
				a.y * m,
				a.z * m
				);
	}
	
	public Vector3 mult(double m) {
		return new Vector3(
				x * m,
				y * m,
				z * m
				);
	}
	
	public static double len(Vector3 a) {
		return Math.sqrt(a.dot(a));
	}
	
	public double len() {
		return Math.sqrt(this.dot(this));
	}
	
	@Override
	public String toString() {
		return x + ", " + y + ", " + z;
	}
	
}
