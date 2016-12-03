package raycaster;

import java.util.ArrayList;
import java.util.stream.IntStream;

public class Render {
	static Vector3[] directions;
	static int[] buffer;
	static int width, height;
	static int upscale;
	static int scaleMultiplier;
	static Camera oldCamera;
	static int renderIndex;
	
	public static void initRender(double xfov, int width, int height, int upscale) {
		Render.upscale = upscale;
		Render.scaleMultiplier = upscale;
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

	public static void upscale(int index, int upscale) {

		for (int y = 0; y < upscale; y++) {
			for (int x = 0; x < upscale; x++) {
				buffer[index + y * width + x] = buffer[index];
			}
		}
	}
	public static int[] progressiveRender(ArrayList<Shape> shapes, ArrayList<Sphere> spheres, Camera camera, Vector3 light) {


		int curZoom = upscale/scaleMultiplier;
		if(!camera.equals(oldCamera)) {
			System.out.println("Camera moved");

			oldCamera = new Camera(camera);

			// reset rendering
			scaleMultiplier = upscale;
			renderIndex = 0;

			// render
			render(shapes, spheres, camera, light, renderIndex, scaleMultiplier);
			// prepare for next
			scaleMultiplier /= 2;
			renderIndex++;

		} else if (renderIndex < curZoom * curZoom ) {
			// if have rendered a new square, upscale it
			if ((renderIndex+1) % (curZoom*curZoom) == 0) {
				render(shapes, spheres, camera, light, renderIndex, scaleMultiplier);
				if (scaleMultiplier != 1) // upscaling can't be less than 1
					scaleMultiplier /= 2;

			} else { // else just render it
				render(shapes, spheres, camera, light, renderIndex, -1);
			}
			renderIndex++;

		}
		System.out.print("+");
		return buffer;
	}
	
	public static int[] render(ArrayList<Shape> shapes, ArrayList<Sphere> spheres, Camera camera, Vector3 light, int it, int curUpScale) {
		//TODO: Calculate rotation matrix here? Perhaps a separate class for it?
		//matrix = MatriceClassName.rotationMatrix(Camera.xAngle, Camera.yAngle, Camera.zAngle);
		//Or something...
		//assert iteration < upscale;

		IntStream.range(0, buffer.length/upscale/upscale).parallel().forEach(index -> {
			final int smallWidth = width/upscale;
			final int smallX = index%smallWidth;
			final int smallY = index/smallWidth;
			final int largex = smallX*upscale;
			final int largey = smallY*upscale;
			final int iterationX = it%upscale;
			final int iterationY = it/upscale;
			final int all = (largey + iterationY)*width + largex + iterationX;

			index = all;
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
				if (collision == null) continue;
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
					//Shadows. Very resource heavy.
					Vector3 lightRay = new Vector3(lightDir.x, lightDir.y, lightDir.z);
					lightRay.normalize();
					boolean inShadow = false;
					CollisionData shadow = null;
					for (Sphere sphere : spheres) {
						shadow = CollisionCheck.raySphere(new Ray(collision.coord, lightRay), sphere);
						if (shadow == null) continue;
						if (shadow.distance < lightLen) {
							inShadow = true;
							break;
						}
					}
					for (Shape shape : shapes) {
						if (inShadow) break;
						for (Side side : shape.sides) {
							shadow = CollisionCheck.raySide(new Ray(collision.coord, lightRay), side);
							if (shadow == null) continue;
							if (shadow.distance < lightLen) {
								inShadow = true;
								break;
							}
						}
					}
					if (inShadow) {
						buffer[index] = collision.color.shade(0);
					} else {
						//diffuse - cos(a) between light source and normal
						//specular - cos(a) between light reflection and camera (this can be negative, so I improvised)
						//intensity - diffuse + (specular + 1) / 2
						//This isn't too resource-intensive, but it's barely an improvement in comparison to plain diffuse.
						buffer[index] = collision.color.shade((2 * d + Vector3.dot(collision.normal.mult(2 * d).sub(lightDir), direction.mult(-1)) + lightLen) / (4 * lightLen));
						//buffer[index] = collision.color.shade(d / lightLen); //Plain diffuse
					}
				}
				
			} else {
				buffer[index] = Color.SKY_BLUE().color;
			}
			if (curUpScale != -1 && curUpScale != 1) {
				upscale(index, curUpScale);
			}
		});
		
		return buffer;
	}

}
