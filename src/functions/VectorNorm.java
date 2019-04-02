package functions;

public class VectorNorm {

	float VectorNrm(float[] a) {	//Norma de um vetor
		if(a.length != 3) {
			System.out.println("Vetor mal definido");
			return 0;
		} else {
			float result = 0;
			
			result = (float) Math.sqrt(a[0] * a[0] + a[1]  * a[1] + a[2] * a[2]);
			
			return result;
		}
	}
	
}
