package raycaster;

import java.util.ArrayList;
import java.util.stream.IntStream;

public class Render {
	static Vector3[] directions;
	static int[] buffer;
	static int width, height;
	
	public static void initRender(double xfov, int width, int height) {
		Render.width = width;
		Render.height = height;
		buffer = new int[width * height];
		xfov *= Math.PI / 180;
		double yfov = 2 * Math.atan(Math.tan(xfov / 2) * height / width);
		
		directions = new Vector3[width * height];
		int index = 0;
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++, index++) {
				double yAngle =  yfov / 2 - y * yfov / height;
				double xAngle = x * xfov / width - xfov / 2;
				directions[index] = new Vector3(Math.tan(xAngle), Math.tan(yAngle), 1);
				directions[index].normalize();
			}
		}
		//TODO: Directions could be stored in a format that's ready for matrix multiplication because they won't be used before being multiplied anyways.
	}
	
	public static int[] render(ArrayList<Shape> shapes, ArrayList<Sphere> spheres, Camera camera, Vector3 light) {
		//TODO: Calculate rotation matrix here? Perhaps a separate class for it?
		//matrix = MatriceClassName.rotationMatrix(Camera.xAngle, Camera.yAngle, Camera.zAngle);
		//Or something...
		 
		IntStream.range(0, buffer.length).parallel().forEach(index -> {
			//Vector3 direction = MatriceClassName.multiply(directions[index], matrix);
			Vector3 direction = directions[index]; //Temporary until the above gets implemented.
			Ray ray = new Ray(camera.position, direction);
			CollisionData collision;
			ArrayList<CollisionData> collisions = new ArrayList<CollisionData>();
			
			//Get collision data for all shapes.
			for (Shape shape : shapes) {
				for (Side side : shape.sides) {
					collision = CollisionCheck.raySide(ray, side);
					if (collision == null) continue;
					collisions.add(collision);
				}
			}
			for (Sphere sphere : spheres) {
				collision = CollisionCheck.raySphere(ray, sphere);
				if (collision == null) continue; //If this can be negative, it shouldn't ever be.
				if (collision.distance < 0) {
					System.out.println("NEGATIVE!");
					System.exit(0);
				}
				collisions.add(collision);
			}
			
			//Find the closest collision.
			collision = new CollisionData(Double.MAX_VALUE, null, null, null, false);
			for (CollisionData tempCol : collisions) {
				if (tempCol.distance < collision.distance) {
					collision = tempCol;
				}
			}
			if (collision.normal != null) {
				if (collision.isSphere) { //Calculate lighting data in case of sphere.
					collision.coord = ray.origin.add(ray.direction.mult(collision.distance));
					collision.normal = collision.coord.sub(collision.normal);
					collision.normal.normalize();
				} else if (Vector3.dot(direction, collision.normal) > 0) { //Flip the normal if the camera is on the "other side" of the plane.
					collision.normal = collision.normal.mult(-1);
				}
				
				//Calculate the lighting.
				Vector3 lightDir = light.sub(collision.coord);
				double d = Vector3.dot(lightDir, collision.normal);
				if (d < 0) { //Light source is on the other side.
					buffer[index] = collision.color.shade(0);
				} else {
					double lightLen = lightDir.len();
					//diffuse - cos(a) between light source and normal
					//specular - cos(a) between light reflection and camera (this can be negative, so I improvised)
					//intensity - diffuse + (specular + 1) / 2
					//This isn't too resource-intensive, but it's barely an improvement in comparison to plain diffuse.
					buffer[index] = collision.color.shade((2 * d + Vector3.dot(collision.normal.mult(2 * d).sub(lightDir), direction.mult(-1)) + lightLen) / (4 * lightLen));
					//buffer[index] = collision.color.shade(d / lightLen); //Plain diffuse
				}
				
			} else {
				buffer[index] = Color.SKY_BLUE().color;
			}
			
		});
		
		return buffer;
	}

}
