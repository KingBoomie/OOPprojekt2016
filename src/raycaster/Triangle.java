package raycaster;

public class Triangle {
	Vector3 a, b, c;
	
	Triangle(Vector3 a, Vector3 b, Vector3 c) {
		this.a = a;
		this.b = b;
		this.c = c;
	}
	
	@Override
	public String toString() {
		return a + ", " + b + ", " + c;
	}
	
}
