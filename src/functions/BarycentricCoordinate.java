package functions;

public class BarycentricCoordinate {
	
	float[] BarycentricCrdnt(float[] p, float[] a, float[] b, float[] c) {	//Coordenadas baricentricas de um ponto p em rel. a a, b e c
		if(p.length != 2 || a.length != 2 || b.length != 2 || c.length != 2) {
			System.out.println("Algum(s) ponto esta mal definido");
			return null;
		} else {
			float[] result = new float[3];
			
			float AREAabc = Math.abs(a[0] * b[1] * 1 + a[1] * 1 * c[0] + 1 * b[0] * c[1] + (-1)*(1 * b[1] * c[0] + a[0] * 1 * c[1] + a[1] * b[0] * 1)) / 2;
			float AREApbc = Math.abs(p[0] * b[1] * 1 + p[1] * 1 * c[0] + 1 * b[0] * c[1] + (-1)*(1 * b[1] * c[0] + p[0] * 1 * c[1] + p[1] * b[0] * 1)) / 2;
			float AREAapc = Math.abs(a[0] * p[1] * 1 + a[1] * 1 * c[0] + 1 * p[0] * c[1] + (-1)*(1 * p[1] * c[0] + a[0] * 1 * c[1] + a[1] * p[0] * 1)) / 2;
			float AREAabp = Math.abs(a[0] * b[1] * 1 + a[1] * 1 * p[0] + 1 * b[0] * p[1] + (-1)*(1 * b[1] * p[0] + a[0] * 1 * p[1] + a[1] * b[0] * 1)) / 2;
			
			result[0] = AREApbc / AREAabc;
			result[1] = AREAapc / AREAabc;
			result[2] = AREAabp / AREAabc;
			
			return result;
		}
	}

}
