package raycaster;

public class Plane {
	Vector3 plane, normal;
	
	Plane(Vector3 a, Vector3 ab, Vector3 ac) {
		plane = a;
		normal = Vector3.cross(ab, ac);
		normal.normalize();
	}
	
	Plane (Side side) {
		plane = side.a;
		normal = Vector3.cross(side.b.sub(side.a), side.c.sub(side.a));
		normal.normalize();
	}
	
	@Override
	public String toString() {
		return plane + ", " + normal;
	}
	
}
