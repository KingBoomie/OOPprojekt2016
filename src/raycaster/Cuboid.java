package raycaster;

/**
 * Created by oskar on 12/10/2016.
 */
public class Cuboid extends FlatShape {

    public Cuboid(Vector3 centre, int length, int width, int height) {
        super(generateParallelograms(centre, length, width, height));
    }


    public Cuboid(Parallelogram[] parallelograms, Triangle[] triangles) {
        super(parallelograms, triangles);
    }

    public Cuboid(Triangle[] triangles) {
        super(triangles);
    }

    public Cuboid(Parallelogram[] parallelograms) {
        super(parallelograms);
    }

    public static Parallelogram[] generateParallelograms(Vector3 centre, double length, double width, double height) {
        Vector3 corner1 = centre.add( new Vector3( length/2, + width/2, + height/2));
        Vector3 corner2 = centre.add( new Vector3( length/2, + width/2, - height/2));
        Vector3 corner3 = centre.add( new Vector3( length/2, - width/2, + height/2));
        Vector3 corner4 = centre.add( new Vector3( length/2, - width/2, - height/2));
        Vector3 corner5 = centre.add( new Vector3(-length/2, + width/2, + height/2));
        Vector3 corner6 = centre.add( new Vector3(-length/2, + width/2, - height/2));
        Vector3 corner7 = centre.add( new Vector3(-length/2, - width/2, + height/2));
        Vector3 corner8 = centre.add( new Vector3(-length/2, - width/2, - height/2));

        return new Parallelogram[] {
                new Parallelogram(corner1, corner2, corner3, corner4),
                new Parallelogram(corner1, corner2, corner5, corner6),
                new Parallelogram(corner5, corner6, corner7, corner8),
                new Parallelogram(corner3, corner4, corner7, corner8),
                new Parallelogram(corner1, corner3, corner5, corner7),
                new Parallelogram(corner2, corner4, corner6, corner8),

        };
    }
}
