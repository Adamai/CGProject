package functions;

public class DotProduct {
	
	float DotPrdct(float[] a, float[] b) {	//Produto escalar
		if(a.length != b.length || a.length != 3) {
			System.out.println("Pontos Nao 3d ou mal definidos");
			return 0;
		} else {
			float result = 0;
			
			result = a[0] * b[0];
			result = result + a[1] * b[1];
			result = result + a[2] * b[2];
			
			return result;
		}
	}

}
