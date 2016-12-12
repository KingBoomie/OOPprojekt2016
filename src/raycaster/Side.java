package raycaster;

public class Side {
	Vector3 a, b, c;
	Vector3 ab, ac;
	Plane plane;
	double xx, xy, yy, d;
	Color color;
	Boolean isQuad;
	
	Side(Vector3 a, Vector3 b, Vector3 c, Color color, Boolean isQuad) {
		this.a = a;
		this.b = b;
		this.c = c;
		this.color = color;
		this.isQuad = isQuad;
		update();
	}
	
	void update() { //Doesn't need to be called when simply translating the side.
		ab = Vector3.sub(b, a);
		ac = Vector3.sub(c, a);
		plane = new Plane(a, ab, ac); //When only translating, just do: side.plane.plane = side.plane.a
		xx = Vector3.dot(ab, ab);
		xy = Vector3.dot(ab, ac);
		yy = Vector3.dot(ac, ac);
		d = 1 / (yy * xx - xy * xy);
	}
	
	@Override
	public String toString() {
		return a + ", " + b + ", " + c;
	}
	
}
