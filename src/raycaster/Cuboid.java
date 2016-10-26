package raycaster;

/**
 * Created by oskar on 12/10/2016.
 */
public class Cuboid extends FlatShape {
    /*public Cuboid(int centre, int length, int width, int height) {

        Vector3 corner1 = new Vector3(centre + length/2, centre + width/2, centre + height/2);
        Vector3 corner2 = new Vector3(centre + length/2, centre + width/2, centre - height/2);
        Vector3 corner3 = new Vector3(centre + length/2, centre - width/2, centre + height/2);
        Vector3 corner4 = new Vector3(centre + length/2, centre - width/2, centre - height/2);
        Vector3 corner5 = new Vector3(centre - length/2, centre + width/2, centre + height/2);
        Vector3 corner6 = new Vector3(centre - length/2, centre + width/2, centre - height/2);
        Vector3 corner7 = new Vector3(centre - length/2, centre - width/2, centre + height/2);
        Vector3 corner8 = new Vector3(centre - length/2, centre - width/2, centre - height/2);

        this(new Parallelogram[] {
                new Parallelogram(corner1, corner2, corner4, corner3)
        })
    }*/

    public Cuboid(Parallelogram[] parallelograms, Triangle[] triangles) {
        super(parallelograms, triangles);
    }

    public Cuboid(Triangle[] triangles) {
        super(triangles);
    }

    public Cuboid(Parallelogram[] parallelograms) {
        super(parallelograms);
    }

    /*public Parallelogram[] generateParallelograms(double centre, double length, double width, double height) {
        Parallelogram[] out = new Parallelogram[]
    }*/
}
