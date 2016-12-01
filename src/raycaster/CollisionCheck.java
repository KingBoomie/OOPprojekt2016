package raycaster;

public class CollisionCheck {
	
	public static CollisionData raySide(Ray ray, Side side) {
		double d = Vector3.dot(ray.direction, side.plane.normal);
		
		if (d > 1e-6 || d < -1e-6) {
			double m = Vector3.dot(Vector3.sub(side.plane.plane, ray.origin), side.plane.normal) / d;
			
			if (m > 1e-3) {
				Vector3 length = Vector3.mult(ray.direction, m);
				Vector3 coord = Vector3.add(ray.origin, length);
				
				Vector3 z = Vector3.sub(coord, side.a);
				
				double xz = Vector3.dot(side.ab, z);
				double yz = Vector3.dot(side.ac, z);

				double u = (side.xx * yz - side.xy * xz) * side.d;
				double v = (side.yy * xz - side.xy * yz) * side.d;
				
				if (((!side.isQuad) && (u >= 0) && (v >= 0) && (u + v <= 1)) ||
						((side.isQuad) && (u >= 0) && (v >= 0) && (u <= 1) && (v <= 1))) {
					return new CollisionData(m, coord, side.plane.normal, side.color, false);
				}
				
				return null;
			}
			
			return null;
		}
		
		return null;
	}
	
	public static CollisionData raySphere(Ray ray, Sphere sphere) {
		Vector3 d = Vector3.sub(sphere.centre, ray.origin);
		double k1 = Vector3.dot(d, ray.direction);
		if (k1 <= 0) {
			return null;
		}
		double k2 = Vector3.dot(d, d) - k1 * k1;
		if (k2 > sphere.radius2) {
			return null;
		}
		double k = Math.sqrt(sphere.radius2 - k2);
		double m = Math.min(k1 - k, k1 + k);
		
		if (m > 1e-3) {
			return new CollisionData(m, null, sphere.centre, sphere.color, true); //Data to calculate lighting later, should this be rendered.
		} else {
			return null;
		}
	}
	
}
