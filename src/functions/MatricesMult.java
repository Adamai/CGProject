package functions;

public class MatricesMult {
	
	public double[][] MatricesMltply(double[][] a, double[][] b){
		if(a[0].length != b.length) {
			System.out.println("Matrizes n�o podem ser multiplicadas");
			return null;
		}
		else {
			double[][] result = new double[a.length][b[0].length];
			double sum = 0;
			
			for(int i=0; i < a.length; i++) {			//i linha do primeiro
				for(int l=0; l < b[0].length; l++) {	//l coluna do segundo
					for(int k=0; k < b.length; k++) {	//k linha do segundo = k coluna do primeiro
						sum = sum + a[i][k] * b[k][l];
					}
					result[i][l] = sum;
					sum = 0;
				}
			}
			
			return result;
			
		}
	}

}
