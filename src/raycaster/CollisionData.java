package raycaster;

public class CollisionData {
	double distance;
	Vector3 coord, normal;
	Color color;
	boolean isSphere;
	
	CollisionData(double distance, Vector3 coord, Vector3 normal, Color color, boolean isSphere) {
		this.distance = distance;
		this.coord = coord;
		this.normal = normal;
		this.color = color;
		this.isSphere = isSphere;
	}
}
