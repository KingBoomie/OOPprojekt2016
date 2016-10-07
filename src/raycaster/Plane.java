package raycaster;

public class Plane {
	Vector3 plane, normal;
	
	Plane(Vector3 x, Vector3 y, Vector3 z) {
		plane = x;
		normal = Vector3.cross(
				Vector3.sub(y, x),
				Vector3.sub(z, x)
				);
	}
	
	Plane(Triangle triangle) {
		plane = triangle.a;
		normal = Vector3.cross(
				Vector3.sub(triangle.b, triangle.a),
				Vector3.sub(triangle.c, triangle.a)
				);
	}
	
	@Override
	public String toString() {
		return plane + ", " + normal;
	}
	
}
