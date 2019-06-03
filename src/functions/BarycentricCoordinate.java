package functions;

public class BarycentricCoordinate {
	
	double[] BarycentricCrdnt(double[] p, double[] a, double[] b, double[] c) {	//Coordenadas baricentricas de um ponto p em rel. a a, b e c
		if(p.length != 2 || a.length != 2 || b.length != 2 || c.length != 2) {
			System.out.println("Algum(s) ponto esta mal definido");
			return null;
		} else {
			double[] result = new double[3];
			
			double AREAabc = Math.abs(a[0] * b[1] * 1 + a[1] * 1 * c[0] + 1 * b[0] * c[1] + (-1)*(1 * b[1] * c[0] + a[0] * 1 * c[1] + a[1] * b[0] * 1)) / 2;
			double AREApbc = Math.abs(p[0] * b[1] * 1 + p[1] * 1 * c[0] + 1 * b[0] * c[1] + (-1)*(1 * b[1] * c[0] + p[0] * 1 * c[1] + p[1] * b[0] * 1)) / 2;
			double AREAapc = Math.abs(a[0] * p[1] * 1 + a[1] * 1 * c[0] + 1 * p[0] * c[1] + (-1)*(1 * p[1] * c[0] + a[0] * 1 * c[1] + a[1] * p[0] * 1)) / 2;
			double AREAabp = Math.abs(a[0] * b[1] * 1 + a[1] * 1 * p[0] + 1 * b[0] * p[1] + (-1)*(1 * b[1] * p[0] + a[0] * 1 * p[1] + a[1] * b[0] * 1)) / 2;
			
			result[0] = AREApbc / AREAabc;
			result[1] = AREAapc / AREAabc;
			result[2] = AREAabp / AREAabc;
			
			return result;
		}
	}

}
