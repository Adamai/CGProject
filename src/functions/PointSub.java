package functions;

public class PointSub {
	
	float[] PointSb(float[] a, float[] b) {
		if(a.length != b.length || a.length != 3) {
			System.out.println("Vetores mal definidos");
			return null;
		} else {
			float[] result = new float[3];
			
			result[0] = a[0] - b[0];
			result[1] = a[1] - b[1];
			result[2] = a[2] - b[2];
			
			return result;
		}
	}

}
