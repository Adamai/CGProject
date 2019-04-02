package functions;

public class VectorNormalization {

	float[] VectorNrmlztn(float[] a) {	//Normalizacao de um vetor
		if(a.length != 3) {
			System.out.println("Vetor mal definido");
			return null;
		} else {
			float[] result = new float[3];
			
			VectorNorm VN = new VectorNorm();
			
			float norm = VN.VectorNrm(a); 
			
			result[0] = a[0]/norm;
			result[1] = a[1]/norm;
			result[2] = a[2]/norm;
			
			return result;
		}
	}
	
}
