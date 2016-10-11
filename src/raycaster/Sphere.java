package raycaster;

public class Sphere {
	Vector3 centre;
	double radius, radius2;
	
	Sphere(Vector3 centre, double radius) {
		this.centre = centre;
		this.radius = radius;
		radius2 = radius * radius;
	}
}
