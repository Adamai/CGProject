package functions;

public class VectorNormalization {

	double[] VectorNrmlztn(double[] a) {	//Normalizacao de um vetor
		if(a.length != 3) {
			System.out.println("Vetor mal definido");
			return null;
		} else {
			double[] result = new double[3];
			
			VectorNorm VN = new VectorNorm();
			
			double norm = VN.VectorNrm(a); 
			
			result[0] = a[0]/norm;
			result[1] = a[1]/norm;
			result[2] = a[2]/norm;
			
			return result;
		}
	}
	
}
