package raycaster;

public class Ray {
	Vector3 origin, direction;
	
	Ray(Vector3 origin, Vector3 direction) {
		this.origin = origin;
		this.direction = direction;
	}

	@Override
	public String toString() {
		return origin + ", " + direction;
	}
	
}
