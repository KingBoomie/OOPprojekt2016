package raycaster;

public class Shape {
	Side[] sides;
	Vector3[] vertices;
	
	Shape(Side[] sides, Vector3[] vertices) {
		this.sides = sides;
		this.vertices = vertices;
	}
	
	public static Shape cube(Vector3 center, double size, Color color) {
		Side[] sides = new Side[6];
		Vector3[] verts = new Vector3[8];
		double[] l2 = {-size / 2, size / 2};
		int i = 0;
		
		for (double x : l2) {
			for (double y : l2) {
				for (double z : l2) {
					verts[i] = new Vector3(x + center.x, y + center.y, z + center.z);
					i++;
				}
			}
		}
		
		sides[0] = new Side(verts[3], verts[2], verts[1], color, true);
		sides[1] = new Side(verts[3], verts[2], verts[7], color, true);
		sides[2] = new Side(verts[3], verts[1], verts[7], color, true);
		sides[3] = new Side(verts[4], verts[6], verts[5], color, true);
		sides[4] = new Side(verts[4], verts[0], verts[5], color, true);
		sides[5] = new Side(verts[4], verts[6], verts[0], color, true);
		
		return new Shape(sides, verts);
	}
	
	public static Shape cuboid(Vector3 center, double width, double height, double length, Color color) {
		Side[] sides = new Side[6];
		Vector3[] verts = new Vector3[8];
		double[] w2 = {-width / 2, width / 2};
		double[] h2 = {-height / 2, height / 2};
		double[] l2 = {-length / 2, length / 2};
		int i = 0;
		
		for (double x : w2) {
			for (double y : h2) {
				for (double z : l2) {
					verts[i] = new Vector3(x + center.x, y + center.y, z + center.z);
					i++;
				}
			}
		}
		
		sides[0] = new Side(verts[3], verts[2], verts[1], color, true);
		sides[1] = new Side(verts[3], verts[2], verts[7], color, true);
		sides[2] = new Side(verts[3], verts[1], verts[7], color, true);
		sides[3] = new Side(verts[4], verts[6], verts[5], color, true);
		sides[4] = new Side(verts[4], verts[0], verts[5], color, true);
		sides[5] = new Side(verts[4], verts[6], verts[0], color, true);
		
		return new Shape(sides, verts);
	}
	
	public static Shape square(Vector3 center, double size, Color color) {
		Side[] sides = new Side[1];
		Vector3[] verts = new Vector3[4];
		double[] l2 = {-size / 2, size / 2};
		int i = 0;
		
		for (double x : l2) {
			for (double z : l2) {
				verts[i] = new Vector3(x + center.x, center.y, z + center.z);
				i++;
			}
		}
		
		sides[0] = new Side(verts[0], verts[1], verts[2], color, true);
		
		return new Shape(sides, verts);
	}
	
	public static Shape rectangle(Vector3 center, double width, double length, Color color) {
		Side[] sides = new Side[1];
		Vector3[] verts = new Vector3[4];
		double[] w2 = {-width / 2, width / 2};
		double[] l2 = {-length / 2, length / 2};
		int i = 0;
		
		for (double x : w2) {
			for (double z : l2) {
				verts[i] = new Vector3(x + center.x, center.y, z + center.z);
				i++;
			}
		}
		
		sides[0] = new Side(verts[0], verts[1], verts[2], color, true);
		
		return new Shape(sides, verts);
	}
	
	public void translate(Vector3 direction) {
		for (Side side : sides) {
			side.a = side.a.add(direction);
			side.b = side.b.add(direction);
			side.c = side.c.add(direction);
			side.update();
		}
	}
}
