package raycaster;

public class CollisionCheck {
	
	public static boolean rayPlane(Ray ray, Plane plane) {
		double d = Vector3.dot(ray.direction, plane.normal);
		
		if (d > 1e-6 || d < -1e-6) {
			double mult = Vector3.dot(Vector3.sub(plane.plane, ray.origin), plane.normal) / d;
			// Vector3 coord = Vector3.add(ray.origin, Vector3.mult(ray.direction, m));
			return (mult >= 0);
		}
		
		return false;
		
	}
	
	public static boolean rayTriangle(Ray ray, Triangle triangle) {
		Plane plane = new Plane(triangle.a, triangle.b, triangle.c);
		double d = Vector3.dot(ray.direction, plane.normal);
		
		if (d > 1e-6 || d < -1e-6) {
			double m = Vector3.dot(Vector3.sub(plane.plane, ray.origin), plane.normal) / d;
			
			if (m >= 0) {
				Vector3 coord = Vector3.add(ray.origin, Vector3.mult(ray.direction, m));
				
				//Experimental:
				Vector3 x = Vector3.sub(coord, triangle.a);
				Vector3 y = Vector3.sub(triangle.c, triangle.a);
				Vector3 z = Vector3.sub(triangle.b, triangle.a);
				
				double dotYY = Vector3.dot(y, y);
				double dotYZ = Vector3.dot(y, z);
				double dotYX = Vector3.dot(y, x);
				double dotZZ = Vector3.dot(z, z);
				double dotZX = Vector3.dot(z, x);
				
				double d2 = 1 / (dotYY * dotZZ - dotYZ * dotYZ);
				double u = (dotZZ * dotYX - dotYZ * dotZX) * d2;
				double v = (dotYY * dotZX - dotYZ * dotYX) * d2;
				
				return (u >= 0) && (v >= 0) && (u + v <= 1);
			}
			
			return false;
		}
		
		return false;
	}
	
}
