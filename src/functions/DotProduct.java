package functions;

public class DotProduct {
	
	double DotPrdct(double[] a, double[] b) {	//Produto escalar
		if(a.length != b.length || a.length != 3) {
			System.out.println("Pontos Nao 3d ou mal definidos");
			return 0;
		} else {
			double result = 0;
			
			result = a[0] * b[0];
			result = result + a[1] * b[1];
			result = result + a[2] * b[2];
			
			return result;
		}
	}

}
