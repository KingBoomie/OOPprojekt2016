package raycaster;

public class Parallelogram extends Side {
	Vector3 a, b, c, e;
	Vector3[] vertices = {a, b, c, e};
	Vector3 ab, ac;
	Plane plane;
	double xx, xy, yy, d;
	
	Parallelogram(Vector3 a, Vector3 b, Vector3 c, Vector3 d) {
		this.a = a;
		this.b = b;
		this.c = c;
		this.e = d;
		Update();
	}
	
	void Update() {
		ab = Vector3.sub(b, a);
		ac = Vector3.sub(c, a);
		plane = new Plane(a, ab, ac);
		xx = Vector3.dot(ab, ab);
		xy = Vector3.dot(ab, ac);
		yy = Vector3.dot(ac, ac);
		d = 1 / (yy * xx - xy * xy);
	}
	
	@Override
	public String toString() {
		return a + ", " + b + ", " + c + ", " + e;
	}

}
