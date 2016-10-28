package raycaster;

import java.util.DoubleSummaryStatistics;

public class CollisionCheck {

	public static final double noCollision = Double.MAX_VALUE;

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
				
				return noCollision;
			}
			
			return noCollision;
		}
		
		return noCollision;
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
				
				return noCollision;
			}
			
			return noCollision;
		}
		
		return noCollision;
	}

	public static double raySphere(Ray ray, Sphere sphere) {
		Vector3 d = Vector3.sub(sphere.centre, ray.origin);
		double k1 = Vector3.dot(d, ray.direction);
		if (k1 <= 0) {
			return noCollision;
		}
		double k2 = Vector3.dot(d, d) - k1 * k1;
		if (k2 > sphere.radius2) {
			return noCollision;
		}
		double k = Math.sqrt(sphere.radius2 - k2);
		return Math.min(k1 - k, k1 + k);
	}
	
}
