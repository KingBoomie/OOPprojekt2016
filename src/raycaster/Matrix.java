package raycaster;

public class Matrix {
	
	public static double[][] rotationMatrix(double xAngle, double yAngle, double zAngle) {
		double[][] test = multiply(multiply(new double[][]
				{{Math.cos(zAngle), -Math.sin(zAngle), 0},
				 {Math.sin(zAngle),  Math.cos(zAngle), 0},
				 {		0,					0,		   1}},
				 new double[][]
				{{ Math.cos(yAngle), 0, Math.sin(yAngle)},
				 {		0,			 1,			0		},
				 {-Math.sin(yAngle), 0, Math.cos(yAngle)}}),
				 new double[][]
				{{1,		0,					0		},
				 {0, Math.cos(xAngle), -Math.sin(xAngle)},
				 {0, Math.sin(xAngle),  Math.cos(xAngle)}});
		//System.out.println(Double.toString(test[0][0]) + test[0][1] + test[0][2] + "\n"
		//		 + test[1][0] + test[1][1] + test[1][2] + "\n"
		//		+ test[2][0] + test[2][1] + test[2][2] + "\n");
		return test;
	}
	
	public static double[][] multiply(double[][] a, double[][] b) {
		int h = a.length;
		int w = b[0].length;
		int d = b.length;
		double[][] c = new double[h][w];
		
		for (int i = 0; i < h; i++) {
			for (int j = 0; j < w; j++) {
				c[i][j] = 0;
				for (int k = 0; k < d; k++) {
					c[i][j] += a[i][k] * b[k][j];
				}
			}
		}
		
		return c;
	}
	
	public static Vector3 vectorRotate(Vector3 a, double[][] r) {
		double[][] b = multiply(r, new double[][] {{a.x}, {a.y}, {a.z}});
		return new Vector3(b[0][0], b[1][0], b[2][0]);
	}

}
