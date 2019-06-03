package functions;

public class VectorNorm {

	double VectorNrm(double[] a) {	//Norma de um vetor
		if(a.length != 3) {
			System.out.println("Vetor mal definido");
			return 0;
		} else {
			double result = 0;
			
			result = (double) Math.sqrt(a[0] * a[0] + a[1]  * a[1] + a[2] * a[2]);
			
			return result;
		}
	}
	
}
