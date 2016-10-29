package raycaster;

public class Sphere extends Shape {
	Vector3 centre;
	double radius, radius2;
	Color color;
	
	Sphere(Vector3 centre, double radius, Color color) {
		this.centre = centre;
		this.radius = radius;
		radius2 = radius * radius;
		this.color = color;
	}
}
