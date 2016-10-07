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
	
	public static double dot(Vector3 a, Vector3 b) {
		return a.x * b.x + a.y * b.y + a.z * b.z;
	}
	
	public static Vector3 sub(Vector3 a, Vector3 b) {
		return new Vector3(
				a.x - b.x,
				a.y - b.y,
				a.z - b.z
				);
	}
	
	public static Vector3 add(Vector3 a, Vector3 b) {
		return new Vector3(
				a.x + b.x,
				a.y + b.y,
				a.z + b.z
				);
	}

	public static Vector3 mult(Vector3 a, double m) {
		return new Vector3(
				a.x * m,
				a.y * m,
				a.z * m
				);
	}
	
	@Override
	public String toString() {
		return x + ", " + y + ", " + z;
	}
	
}
