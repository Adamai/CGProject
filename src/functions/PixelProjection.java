package functions;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class PixelProjection extends JPanel{
	
	BufferedImage canvas;
	
	public PixelProjection(int width, int height, String filePath, String cameraPath) {
		BufferedReader reader;
		BufferedReader cameraReader;
		
		MatricesMult mm = new MatricesMult();
		PointSub ps = new PointSub();
		DotProduct dp = new DotProduct();
		CrossProduct cp = new CrossProduct();
		VectorNorm vn = new VectorNorm();
		VectorNormalization vnz = new VectorNormalization();
		BarycentricCoordinate bc = new BarycentricCoordinate();
		CartesianCoordinate cc = new CartesianCoordinate();
		VectorSub vsb = new VectorSub();
		VectorSum vsm = new VectorSum();
		
		
		int lineCount = 0;
		int nVertex = 0;
		int nTriangles = 0;
		float xMax = 0, yMax = 0;
		float xMin = 99999, yMin = 99999;
		float[][] vertexCoordinates = null;
		float[][] vertexNormalizedCoordinates = null;
		int[][] triangleIndexes = null;
		
		float[] C = new float[3];
		float[] N = new float[3];
		float[] V = new float[3];
		float d = 0, hx = 0, hy = 0;
		
		float[] Vlinha = new float[3];
		float[] U = new float[3];
		float[] Nunder = new float[3];
		float[] Vlinhaunder = new float[3];
		float[] Uunder = new float[3];
		float[][] Imatrice = new float[3][3];
		float[][] vertexCoordinatesView = null;
		
		try {
			cameraReader = new BufferedReader(new FileReader(cameraPath));
			String line = cameraReader.readLine();
			int lineCounter = 0;
			
			while(line != null) {
				if(lineCounter == 0) {
					C[0] = Float.valueOf(line.split(" ")[0]);
					C[1] = Float.valueOf(line.split(" ")[1]);
					C[2] = Float.valueOf(line.split(" ")[2]);
				}
				else if(lineCounter == 1) {
					N[0] = Float.valueOf(line.split(" ")[0]);
					N[1] = Float.valueOf(line.split(" ")[1]);
					N[2] = Float.valueOf(line.split(" ")[2]);
				}
				else if(lineCounter == 2) {
					V[0] = Float.valueOf(line.split(" ")[0]);
					V[1] = Float.valueOf(line.split(" ")[1]);
					V[2] = Float.valueOf(line.split(" ")[2]);
				}
				else if(lineCounter == 3) {
					d = Float.valueOf(line);
				}
				else if(lineCounter == 4) {
					hx = Float.valueOf(line);
				}
				else if(lineCounter == 5) {
					hy = Float.valueOf(line);
				}
				lineCounter++;
				line = cameraReader.readLine();
			}
			cameraReader.close();
			//terminou de ler a entrada da camera
			float[] Ntmp = new float[3];
			Ntmp[0] = N[0] * (dp.DotPrdct(V, N) / dp.DotPrdct(N, N));
			Ntmp[1] = N[1] * (dp.DotPrdct(V, N) / dp.DotPrdct(N, N));
			Ntmp[2] = N[2] * (dp.DotPrdct(V, N) / dp.DotPrdct(N, N));
			//Ortogonalizar V
			Vlinha = vsb.VectorSb(V, Ntmp);
			U = cp.CrossPrdct(N, Vlinha);
			
			Nunder[0] = (1 / vn.VectorNrm(N)) * N[0];
			Nunder[1] = (1 / vn.VectorNrm(N)) * N[1];
			Nunder[2] = (1 / vn.VectorNrm(N)) * N[2];
			Vlinhaunder[0] = (1 / vn.VectorNrm(Vlinha)) * Vlinha[0];
			Vlinhaunder[1] = (1 / vn.VectorNrm(Vlinha)) * Vlinha[1];
			Vlinhaunder[2] = (1 / vn.VectorNrm(Vlinha)) * Vlinha[2];
			Uunder[0] = (1 / vn.VectorNrm(U)) * U[0];
			Uunder[1] = (1 / vn.VectorNrm(U)) * U[1];
			Uunder[2] = (1 / vn.VectorNrm(U)) * U[2];
			Imatrice[0][0] = Uunder[0];
			Imatrice[0][1] = Uunder[1];
			Imatrice[0][2] = Uunder[2];
			Imatrice[1][0] = Vlinhaunder[0];
			Imatrice[1][1] = Vlinhaunder[1];
			Imatrice[1][2] = Vlinhaunder[2];
			Imatrice[2][0] = Nunder[0];
			Imatrice[2][1] = Nunder[1];
			Imatrice[2][2] = Nunder[2];
			
			
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try {
			reader = new BufferedReader(new FileReader(filePath));
			String line = reader.readLine();
			
			while(line != null) {
				if(lineCount == 0) {		//so rodara uma vez
					nVertex = Integer.valueOf(line.split(" ")[0]);
					nTriangles = Integer.valueOf(line.split(" ")[1]);
					vertexCoordinates = new float[nVertex][3];
					vertexNormalizedCoordinates = new float[nVertex][2];		//somente x e y necessarios
					//parte 3
					//PAREI AQUI!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! Preencher vertexCoordinatesView
					vertexCoordinatesView = new float[nVertex][3];
					//parte 3
					triangleIndexes = new int[nTriangles][3];
				} else {
					if(lineCount <= nVertex) {
						vertexCoordinates[lineCount-1][0] = Float.valueOf(line.split(" ")[0]);
						vertexCoordinates[lineCount-1][1] = Float.valueOf(line.split(" ")[1]);
						vertexCoordinates[lineCount-1][2] = Float.valueOf(line.split(" ")[2]);
						if(vertexCoordinates[lineCount-1][0] > xMax) {
							xMax = vertexCoordinates[lineCount-1][0];
						}
						if(vertexCoordinates[lineCount-1][1] > yMax) {
							yMax = vertexCoordinates[lineCount-1][1];
						}
						if(vertexCoordinates[lineCount-1][0] < xMin) {
							xMin = vertexCoordinates[lineCount-1][0];
						}
						if(vertexCoordinates[lineCount-1][1] < yMin) {
							yMin = vertexCoordinates[lineCount-1][1];
						}
					}
					else if(lineCount <= nVertex + nTriangles) {
						triangleIndexes[lineCount-nVertex-1][0] = Integer.valueOf(line.split(" ")[0]);
						triangleIndexes[lineCount-nVertex-1][1] = Integer.valueOf(line.split(" ")[1]);
						triangleIndexes[lineCount-nVertex-1][2] = Integer.valueOf(line.split(" ")[2]);
					}
				}
				lineCount++;
				line = reader.readLine();
			}
			reader.close();
			//terminou de ler coordenadas dos triangulos
			
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		for(int i = 0; i < vertexCoordinates.length; i++) {
			vertexNormalizedCoordinates[i][0] = ((vertexCoordinates[i][0] - xMin) / (xMax - xMin)) * (width - 1);
			vertexNormalizedCoordinates[i][1] = ((vertexCoordinates[i][1] - yMin) / (yMax - yMin)) * (height - 1);
		}
		
		canvas = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		//System.out.println(width + " " + height);
		resetCanvas();
		JLabel label = new JLabel(new ImageIcon(canvas));
		add(label);
		
		for(int i = 0; i < vertexNormalizedCoordinates.length; i++) {
			canvas.setRGB(Math.round(vertexNormalizedCoordinates[i][0]), Math.round(vertexNormalizedCoordinates[i][1]), Color.WHITE.getRGB());
			//System.out.println(Math.round(vertexNormalizedCoordinates[i][0]) + " "+Math.round(vertexNormalizedCoordinates[i][1]) );
		}
		repaint();
	}
	
	public void resetCanvas() {
        for (int x = 0; x < canvas.getWidth(); x++) {
            for (int y = 0; y < canvas.getHeight(); y++) {
                canvas.setRGB(x, y, Color.BLACK.getRGB());
            }
        }
        repaint();
    }

}
