package functions;

public class CrossProduct {
	
	float[] CrossPrdct(float[] a, float[] b) {		//Produto vetorial
		if(a.length != b.length || a.length != 3) {
			System.out.println("Vetores mal definidos");
			return null;
		} else {
			float[] result = new float[3];
			
			result[0] = a[1] * b[2] + (-1)*(a[2] * b[1]);
			result[1] = a[2] * b[0] + (-1)*(a[0] * b[2]);
			result[2] = a[0] * b[1] + (-1)*(a[1] * b[0]);
			
			return result;
		}
	}

}
