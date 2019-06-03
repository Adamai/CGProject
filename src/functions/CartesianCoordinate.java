package functions;

public class CartesianCoordinate {
	
	double[] CartesianCrdnt(double[] bc, double[] a, double[] b, double[] c) {	//Coordenadas cartesiana de um ponto p em baseado em 3 pontos e uma coordenada baricentrica
		if(bc.length != 3 || a.length != 2 || b.length != 2 || c.length != 2) {
			System.out.println("Algum(s) ponto ou coordenada esta mal definido");
			return null;
		} else {
			double[] result = new double[2];
			
			result[0] = a[0] * bc[0] + b[0] * bc[1] + c[0] * bc[2];
			result[1] = a[1] * bc[0] + b[1] * bc[1] + c[1] * bc[2];
			
			return result;
		}
	}

}
