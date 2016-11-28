package raycaster;

public class Sphere {
	Vector3 centre;
	double radius, radius2;
	Color color;
	
	Sphere(Vector3 centre, double radius, Color color) {
		this.centre = centre;
		this.radius = radius;
		radius2 = radius * radius;
		this.color = color;
	}
	
	public void translate(Vector3 translate) {
		centre = centre.add(translate);
	}
}
