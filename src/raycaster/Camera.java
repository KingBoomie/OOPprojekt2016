package raycaster;

public class Camera {
	Vector3 position;
	double xAngle, yAngle, zAngle;
	static final double pi2 = Math.PI * 2; 
	
	Camera() {
		position = new Vector3(0, 0, 0);
		xAngle = 0;
		yAngle = 0;
		zAngle = 0;
	}
	
	Camera(Vector3 position) {
		this.position = position;
		xAngle = 0;
		yAngle = 0;
		zAngle = 0;
	}
	
	Camera(Vector3 position, double xAngle, double yAngle, double zAngle) {
		this.position = position;
		this.xAngle = xAngle;
		this.yAngle = yAngle;
		this.zAngle = zAngle;
	}
	
	public void rotate(double xAngle, double yAngle, double zAngle) {
		this.xAngle += xAngle;
		this.yAngle += yAngle;
		this.zAngle += zAngle;
		xAngle %= pi2;
		yAngle %= pi2;
		zAngle %= pi2;
	}
	
	public void translate(Vector3 translate) {
		position = position.add(translate);
	}
}
