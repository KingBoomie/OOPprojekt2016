package raycaster;

public class CollisionCheck {
	
	public static double rayTriangle(Ray ray, Triangle triangle) {
		double d = Vector3.dot(ray.direction, triangle.plane.normal);
		
		if (d > 1e-6 || d < -1e-6) {
			double m = Vector3.dot(Vector3.sub(triangle.plane.plane, ray.origin), triangle.plane.normal) / d;
			
			if (m >= 0) {
				Vector3 length = Vector3.mult(ray.direction, m);
				Vector3 coord = Vector3.add(ray.origin, length);
				
				Vector3 z = Vector3.sub(coord, triangle.a);
				
				double xz = Vector3.dot(triangle.ab, z);
				double yz = Vector3.dot(triangle.ac, z);

				double u = (triangle.xx * yz - triangle.xy * xz) * triangle.d;
				double v = (triangle.yy * xz - triangle.xy * yz) * triangle.d;
				
				if ((u >= 0) && (v >= 0) && (u + v <= 1)) {
					return length.len();
				}
				
				return -1;
			}
			
			return -1;
		}
		
		return -1;
	}
	
	public static double rayParallelogram(Ray ray, Parallelogram parallelogram) {
		double d = Vector3.dot(ray.direction, parallelogram.plane.normal);
		
		if (d > 1e-6 || d < -1e-6) {
			double m = Vector3.dot(Vector3.sub(parallelogram.plane.plane, ray.origin), parallelogram.plane.normal) / d;
			
			if (m >= 0) {
				Vector3 length = Vector3.mult(ray.direction, m);
				Vector3 coord = Vector3.add(ray.origin, length);
				
				Vector3 z = Vector3.sub(coord, parallelogram.a);
				
				double xz = Vector3.dot(parallelogram.ab, z);
				double yz = Vector3.dot(parallelogram.ac, z);

				double u = (parallelogram.xx * yz - parallelogram.xy * xz) * parallelogram.d;
				double v = (parallelogram.yy * xz - parallelogram.xy * yz) * parallelogram.d;
				
				if ((u >= 0) && (v >= 0) && (u <= 1) && (v <= 1)) {
					return length.len();
				}
				
				return -1;
			}
			
			return -1;
		}
		
		return -1;
	}
	
}
