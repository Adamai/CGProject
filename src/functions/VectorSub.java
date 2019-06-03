package functions;

public class VectorSub {
	
	double[] VectorSb(double[] a, double[] b) {
		if(a.length != b.length || a.length != 3) {
			System.out.println("Vetores mal definidos");
			return null;
		} else {
			double[] result = new double[3];
			
			result[0] = a[0] - b[0];
			result[1] = a[1] - b[1];
			result[2] = a[2] - b[2];
			
			return result;
		}
	}
	
}
