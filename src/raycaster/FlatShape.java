package raycaster;

import java.util.HashSet;

/**
 * Created by oskar on 12/10/2016.
 */
public class FlatShape extends Shape {
    protected Parallelogram[] parallelograms;
    protected Triangle[] triangles;
    protected Vector3[] vertices;

    public Parallelogram[] getQuads() {
        return parallelograms;
    }

    public Triangle[] getTriangles() {
        return triangles;
    }

    public Vector3[] getVertices() {
        return vertices;
    }

    public FlatShape(Parallelogram[] parallelograms, Triangle[] triangles) {
        this.parallelograms = parallelograms;
        this.triangles = triangles;
        generateVertices(addSideArrays(parallelograms, triangles));
    }

    public FlatShape(Triangle[] triangles) {

        this.triangles = triangles;
        generateVertices(triangles);
    }

    public FlatShape(Parallelogram[] parallelograms) {

        this.parallelograms = parallelograms;
        generateVertices(parallelograms);
    }

    public void generateVertices(Side[] sides) {
        HashSet<Vector3> verticesSet = new HashSet<>();
        for (Side side : sides) {
            for (Vector3 vertix : side.vertices) {
                verticesSet.add(vertix);
            }
        }
        this.vertices = verticesSet.toArray(new Vector3[verticesSet.size()]);
    }

    /**
     *
     * Combines two arrays into one.
     */
    public Side[] addSideArrays(Side[] sides1, Side[] sides2) {
        Side[] out = new Side[sides1.length+sides2.length];
        for (int i = 0; i < out.length; i++) {
            if (i < sides1.length)
                out[i] = sides1[i];
            else
                out[i] = sides2[i-sides1.length];
        }
        return out;
    }
}
