package raycaster;

public class Plane {
	Vector3 plane, normal;
	
	Plane(Vector3 a, Vector3 ab, Vector3 ac) {
		plane = a;
		normal = Vector3.cross(ab, ac);
	}
	
	Plane (Triangle triangle) {
		plane = triangle.a;
		normal = Vector3.cross(triangle.b.sub(triangle.a), triangle.c.sub(triangle.a));
	}
	
	@Override
	public String toString() {
		return plane + ", " + normal;
	}
	
}
