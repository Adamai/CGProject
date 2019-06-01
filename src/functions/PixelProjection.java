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

public class PixelProjection extends JPanel {

	BufferedImage canvas;
	float[][] zBuffer;
	float[][] vertexCoordinatesView;
	float[][] vertexCoordinatesScreen;
	int[][] triangleIndexes;
	BarycentricCoordinate bc;
	float[] Iamb;
	float[] Il;
	float Ka, Ks, Eta;
	float[] Pl;
	float[] Kd;
	float[] Od;
	VectorSum vsm = new VectorSum();
	VectorNormalization vnz = new VectorNormalization();
	float[][] vertexNormals;
	CrossProduct cp = new CrossProduct();
	DotProduct dp = new DotProduct();
	

	public PixelProjection(int width, int height, String filePath, String cameraPath, String illuminationPath) {
		BufferedReader reader;
		BufferedReader cameraReader;
		BufferedReader illuminationReader;

		MatricesMult mm = new MatricesMult();
		PointSub ps = new PointSub();
		VectorNorm vn = new VectorNorm();
		bc = new BarycentricCoordinate();
		CartesianCoordinate cc = new CartesianCoordinate();
		VectorSub vsb = new VectorSub();

		int lineCount = 0;
		int nVertex = 0;
		int nTriangles = 0;
		float xMax = 0, yMax = 0;
		float xMin = 99999, yMin = 99999;
		float[][] vertexCoordinates = null;
		float[][] vertexNormalizedCoordinates = null;
		triangleIndexes = null;

		float[] C = new float[3];
		float[] N = new float[3];
		float[] V = new float[3];
		float d = 0, hx = 0, hy = 0;

		Iamb = new float[3];
		Il = new float[3];
		Ka = 0;
		Ks = 0;
		Eta = 0;
		Pl = new float[3];
		Kd = new float[3];
		Od = new float[3];

		float[] Vlinha = new float[3];
		float[] U = new float[3];
		float[] Nunder = new float[3];
		float[] Vlinhaunder = new float[3];
		float[] Uunder = new float[3];
		float[][] Imatrice = new float[3][3];
		vertexCoordinatesView = null;
		float[][] vertexCoordinatesPerspec = null;
		vertexCoordinatesScreen = null;
		float[][] barycenterCoordinates = null;
		float[][] triangleNormals = null;
		vertexNormals = null;
		zBuffer = null;

		try {
			cameraReader = new BufferedReader(new FileReader(cameraPath));
			String line = cameraReader.readLine();
			int lineCounter = 0;

			while (line != null) {
				if (lineCounter == 0) {
					C[0] = Float.valueOf(line.split(" ")[0]);
					C[1] = Float.valueOf(line.split(" ")[1]);
					C[2] = Float.valueOf(line.split(" ")[2]);
				} else if (lineCounter == 1) {
					N[0] = Float.valueOf(line.split(" ")[0]);
					N[1] = Float.valueOf(line.split(" ")[1]);
					N[2] = Float.valueOf(line.split(" ")[2]);
				} else if (lineCounter == 2) {
					V[0] = Float.valueOf(line.split(" ")[0]);
					V[1] = Float.valueOf(line.split(" ")[1]);
					V[2] = Float.valueOf(line.split(" ")[2]);
				} else if (lineCounter == 3) {
					d = Float.valueOf(line);
				} else if (lineCounter == 4) {
					hx = Float.valueOf(line);
				} else if (lineCounter == 5) {
					hy = Float.valueOf(line);
				}
				lineCounter++;
				line = cameraReader.readLine();
			}
			cameraReader.close();
			// terminou de ler a entrada da camera

			// iniciar leitura da entrada de iluminação
			illuminationReader = new BufferedReader(new FileReader(illuminationPath));
			line = illuminationReader.readLine();
			lineCounter = 0;

			while (line != null) {
				if (lineCounter == 0) {
					Iamb[0] = Float.valueOf(line.split(" ")[0]);
					Iamb[1] = Float.valueOf(line.split(" ")[1]);
					Iamb[2] = Float.valueOf(line.split(" ")[2]);
				} else if (lineCounter == 1) {
					Ka = Float.valueOf(line);
				} else if (lineCounter == 2) {
					Il[0] = Float.valueOf(line.split(" ")[0]);
					Il[1] = Float.valueOf(line.split(" ")[1]);
					Il[2] = Float.valueOf(line.split(" ")[2]);
				} else if (lineCounter == 3) {
					Pl[0] = Float.valueOf(line.split(" ")[0]);
					Pl[1] = Float.valueOf(line.split(" ")[1]);
					Pl[2] = Float.valueOf(line.split(" ")[2]);
				} else if (lineCounter == 4) {
					Kd[0] = Float.valueOf(line.split(" ")[0]);
					Kd[1] = Float.valueOf(line.split(" ")[1]);
					Kd[2] = Float.valueOf(line.split(" ")[2]);
				} else if (lineCounter == 5) {
					Od[0] = Float.valueOf(line.split(" ")[0]);
					Od[1] = Float.valueOf(line.split(" ")[1]);
					Od[2] = Float.valueOf(line.split(" ")[2]);
				} else if (lineCounter == 6) {
					Ks = Float.valueOf(line);
				} else if (lineCounter == 7) {
					Eta = Float.valueOf(line);
				}
				lineCounter++;
				line = illuminationReader.readLine();
			}
			illuminationReader.close();
			// terminou de ler a entrada de iluminação

			float[] Ntmp = new float[3];
			Ntmp[0] = N[0] * (dp.DotPrdct(V, N) / dp.DotPrdct(N, N));
			Ntmp[1] = N[1] * (dp.DotPrdct(V, N) / dp.DotPrdct(N, N));
			Ntmp[2] = N[2] * (dp.DotPrdct(V, N) / dp.DotPrdct(N, N));
			// Ortogonalizar V
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

			while (line != null) {
				if (lineCount == 0) { // so rodara uma vez
					nVertex = Integer.valueOf(line.split(" ")[0]);
					nTriangles = Integer.valueOf(line.split(" ")[1]);
					vertexCoordinates = new float[nVertex][3];
					vertexNormalizedCoordinates = new float[nVertex][2]; // somente x e y necessarios
					// parte 2
					vertexCoordinatesView = new float[nVertex][3];
					vertexCoordinatesPerspec = new float[nVertex][2];
					vertexCoordinatesScreen = new float[nVertex][2];
					// parte 2
					//parte 3
					barycenterCoordinates = new float[nTriangles][3];
					triangleNormals = new float[nTriangles][3];
					vertexNormals = new float[nVertex][3];
					zBuffer = new float[width][height];
					//parte 3
					triangleIndexes = new int[nTriangles][3];
				} else {
					if (lineCount <= nVertex) {
						vertexCoordinates[lineCount - 1][0] = Float.valueOf(line.split(" ")[0]);
						vertexCoordinates[lineCount - 1][1] = Float.valueOf(line.split(" ")[1]);
						vertexCoordinates[lineCount - 1][2] = Float.valueOf(line.split(" ")[2]);
						if (vertexCoordinates[lineCount - 1][0] > xMax) {
							xMax = vertexCoordinates[lineCount - 1][0];
						}
						if (vertexCoordinates[lineCount - 1][1] > yMax) {
							yMax = vertexCoordinates[lineCount - 1][1];
						}
						if (vertexCoordinates[lineCount - 1][0] < xMin) {
							xMin = vertexCoordinates[lineCount - 1][0];
						}
						if (vertexCoordinates[lineCount - 1][1] < yMin) {
							yMin = vertexCoordinates[lineCount - 1][1];
						}
						

						// PARTE 2
						float[][] tmpMatrice = new float[3][1]; // P - C
						tmpMatrice[0][0] = ps.PointSb(vertexCoordinates[lineCount - 1], C)[0];
						tmpMatrice[1][0] = ps.PointSb(vertexCoordinates[lineCount - 1], C)[1];
						tmpMatrice[2][0] = ps.PointSb(vertexCoordinates[lineCount - 1], C)[2];

						vertexCoordinatesView[lineCount - 1][0] = mm.MatricesMltply(Imatrice, tmpMatrice)[0][0];
						vertexCoordinatesView[lineCount - 1][1] = mm.MatricesMltply(Imatrice, tmpMatrice)[1][0];
						vertexCoordinatesView[lineCount - 1][2] = mm.MatricesMltply(Imatrice, tmpMatrice)[2][0];

						// coordenadas perspectiva
						vertexCoordinatesPerspec[lineCount - 1][0] = d
								* (vertexCoordinatesView[lineCount - 1][0] / vertexCoordinatesView[lineCount - 1][2]);
						vertexCoordinatesPerspec[lineCount - 1][1] = d
								* (vertexCoordinatesView[lineCount - 1][1] / vertexCoordinatesView[lineCount - 1][2]);

						// coordenadas normalizadas (usando array de perspectiva)
						vertexCoordinatesPerspec[lineCount - 1][0] = vertexCoordinatesPerspec[lineCount - 1][0] / hx;
						vertexCoordinatesPerspec[lineCount - 1][1] = vertexCoordinatesPerspec[lineCount - 1][1] / hy;

						// coordenadas de tela
						vertexCoordinatesScreen[lineCount
								- 1][0] = (float) ((((vertexCoordinatesPerspec[lineCount - 1][0]) + 1) / 2) * width
										+ 0.5);
						vertexCoordinatesScreen[lineCount - 1][1] = (float) (height
								- (((vertexCoordinatesPerspec[lineCount - 1][1]) + 1) / 2) * height + 0.5);

//						if(lineCount < 6) {
//							System.out.println(vertexCoordinatesScreen[lineCount - 1][0] + " " + vertexCoordinatesScreen[lineCount - 1][1]);
//						}

						// PARTE 2
					} else if (lineCount <= nVertex + nTriangles) {
						triangleIndexes[lineCount - nVertex - 1][0] = Integer.valueOf(line.split(" ")[0]);
						triangleIndexes[lineCount - nVertex - 1][1] = Integer.valueOf(line.split(" ")[1]);
						triangleIndexes[lineCount - nVertex - 1][2] = Integer.valueOf(line.split(" ")[2]);
					}
				}
				lineCount++;
				line = reader.readLine();
			}
			reader.close();
			// terminou de ler coordenadas dos triangulos

		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// PARTE 3 - Loop para percorrer todos os triangulos
		//nessa situação o V do ponto a ser utilizado posteriormente é = a -P de vista (-x, -y, -z)
		for (int i = 0; i < triangleIndexes.length; i++) {
			//Normal dos triangulos (coordenadas de vista)
			triangleNormals[i][0] = vnz.VectorNrmlztn(cp.CrossPrdct(
					ps.PointSb(vertexCoordinatesView[triangleIndexes[i][1]-1], vertexCoordinatesView[triangleIndexes[i][0]-1]),
					ps.PointSb(vertexCoordinatesView[triangleIndexes[i][2]-1], vertexCoordinatesView[triangleIndexes[i][0]-1])
					))[0];
			triangleNormals[i][1] = vnz.VectorNrmlztn(cp.CrossPrdct(
					ps.PointSb(vertexCoordinatesView[triangleIndexes[i][1]-1], vertexCoordinatesView[triangleIndexes[i][0]-1]),
					ps.PointSb(vertexCoordinatesView[triangleIndexes[i][2]-1], vertexCoordinatesView[triangleIndexes[i][0]-1])
					))[1];
			triangleNormals[i][2] = vnz.VectorNrmlztn(cp.CrossPrdct(
					ps.PointSb(vertexCoordinatesView[triangleIndexes[i][1]-1], vertexCoordinatesView[triangleIndexes[i][0]-1]),
					ps.PointSb(vertexCoordinatesView[triangleIndexes[i][2]-1], vertexCoordinatesView[triangleIndexes[i][0]-1])
					))[2];
			//coordenadas do baricentro do triangulo
			barycenterCoordinates[i][0] = 					
					(vertexCoordinatesView[triangleIndexes[i][0]-1][0] + 
							vertexCoordinatesView[triangleIndexes[i][1]-1][0] + vertexCoordinatesView[triangleIndexes[i][2]-1][0])/3;
			barycenterCoordinates[i][1] = 					
					(vertexCoordinatesView[triangleIndexes[i][0]-1][1] + 
							vertexCoordinatesView[triangleIndexes[i][1]-1][1] + vertexCoordinatesView[triangleIndexes[i][2]-1][1])/3;
			barycenterCoordinates[i][2] = 					
					(vertexCoordinatesView[triangleIndexes[i][0]-1][2] + 
							vertexCoordinatesView[triangleIndexes[i][1]-1][2] + vertexCoordinatesView[triangleIndexes[i][2]-1][1])/3;
		}
		for(int i = 0; i < vertexNormals.length; i++) {	//zerando vertexNormals para fazer somatorio e encontra-los
			vertexNormals[i][0] = 0;
			vertexNormals[i][1] = 0;
			vertexNormals[i][2] = 0;
		}
		for(int i = 1; i <= nVertex; i++) { //somar normais de triangulos que possuem cada vertice i e normalizar
			for(int j = 0; j < triangleIndexes.length; j++) { //somando normais de triangulos que possuem o vertice
				if(triangleIndexes[j][0] == i || triangleIndexes[j][1] == i || triangleIndexes[j][2] == i) {
					vertexNormals[i - 1][0] = vertexNormals[i - 1][0] + triangleNormals[j][0];
					vertexNormals[i - 1][1] = vertexNormals[i - 1][1] + triangleNormals[j][1];
					vertexNormals[i - 1][2] = vertexNormals[i - 1][2] + triangleNormals[j][2];
				}
			}
			// normalizando a soma das normais
			vertexNormals[i - 1] = vnz.VectorNrmlztn(vertexNormals[i - 1]);
		}
		//preparando z-buffer
		for(int i = 0; i < width; i++) {
			for(int j = 0; j < height; j++) {
				zBuffer[i][j] = Float.MAX_VALUE;
			}
		}
		
		
		for (int i = 0; i < vertexCoordinates.length; i++) {
			vertexNormalizedCoordinates[i][0] = ((vertexCoordinates[i][0] - xMin) / (xMax - xMin)) * (width - 1);
			vertexNormalizedCoordinates[i][1] = ((vertexCoordinates[i][1] - yMin) / (yMax - yMin)) * (height - 1);
		}

		canvas = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

		resetCanvas();
		JLabel label = new JLabel(new ImageIcon(canvas));
		add(label);

//		for(int i = 0; i < vertexNormalizedCoordinates.length; i++) {
//			canvas.setRGB(Math.round(vertexNormalizedCoordinates[i][0]), Math.round(vertexNormalizedCoordinates[i][1]), Color.WHITE.getRGB());
//		}

		// PARTE 2 e 3
		for (int i = 0; i < triangleIndexes.length; i++) {
			// vertices do triangulo abc
			int ax = (int) vertexCoordinatesScreen[triangleIndexes[i][0] - 1][0];
			int ay = (int) vertexCoordinatesScreen[triangleIndexes[i][0] - 1][1];
			int bx = (int) vertexCoordinatesScreen[triangleIndexes[i][1] - 1][0];
			int by = (int) vertexCoordinatesScreen[triangleIndexes[i][1] - 1][1];
			int cx = (int) vertexCoordinatesScreen[triangleIndexes[i][2] - 1][0];
			int cy = (int) vertexCoordinatesScreen[triangleIndexes[i][2] - 1][1];
			

			int v1x = 0;
			int v1y = 0;
			int v2x = 0;
			int v2y = 0;
			int v3x = 0;
			int v3y = 0;
			// decidindo qual o mais alto - inicio
			if (ay <= by && ay <= cy) { // ay é o mais alto?
				v1x = ax;
				v1y = ay;
				if (by <= cy) {
					v2x = bx;
					v2y = by;
					v3x = cx;
					v3y = cy;
				} else {
					v2x = cx;
					v2y = cy;
					v3x = bx;
					v3y = by;
				}
			} else if (by <= cy && by <= ay) { // by é o mais alto?
				v1x = bx;
				v1y = by;
				if (ay <= cy) { // ay é o segundo mais alto?
					v2x = ax;
					v2y = ay;
					v3x = cx;
					v3y = cy;
				} else { // cy é o segundo mais alto
					v2x = cx;
					v2y = cy;
					v3x = ax;
					v3y = ay;
				}
			} else if (cy <= ay && cy <= by) { // cy é o mais alto?
				v1x = cx;
				v1y = cy;
				if (by <= ay) { // by é p segundo maior?
					v2x = bx;
					v2y = by;
					v3x = ax;
					v3y = ay;
				} else { // ay é o segundo maior
					v2x = ax;
					v2y = ay;
					v3x = bx;
					v3y = by;
				}
			}
			// decidindo qual o mais alto : (mais alto) v1 <= v2 <= v3 (mais baixo)- FIM
			if (v1y == v2y && v2y == v3y) {
				drawLine2(v1x, v2x, v1y, v2y, i);
				drawLine2(v1x, v3x, v1y, v3y, i);
			} else if (v2y == v3y && v2y > v1y) { // checando se o triangulo é um bottom
				drawLine2(v1x, v2x, v1y, v2y, i);
				drawBottomTriangle(v1x, v1y, v2x, v2y, v3x, v3y, i);

			} else if (v1y == v2y && v1y < v3y) { // checando se o triangulo é top
				drawLine2(v1x, v3x, v1y, v3y, i);

				drawTopTriangle(v1x, v1y, v2x, v2y, v3x, v3y, i);

			} else { // dividir triangulo em 2
				int v4x = (int) (v1x + ((float) (v2y - v1y) / (float) (v3y - v1y)) * (v3x - v1x));
				int v4y = v2y;

				drawBottomTriangle(v1x, v1y, v2x, v2y, v4x, v4y, i);
				drawTopTriangle(v2x, v2y, v4x, v4y, v3x, v3y, i);

			}

		} // fim do loop de todos os triangulos

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

	public void drawLine(int ax, int bx, int ay, int by) {
		// Nao funciona para retas verticais. deltax = 0, Nao divide por 0
		int deltax = bx - ax;
		int deltay = by - ay;
		double error = 0;
		// deltay = 0 pq é scanline
		if (deltax == 0) {
			// System.out.println(ax + " " + bx);
			canvas.setRGB(ax, ay, Color.WHITE.getRGB());
			return;
		}
		double deltaerr = Math.abs(deltay / deltax);
		int y = ay;
		if (ax < bx) {
			for (int x = ax; x < bx; x++) {
				canvas.setRGB(x, y, Color.WHITE.getRGB());
				error = error + deltaerr;
				if (error >= 0.5) {
					y = y + 1;
					error = error - 1;
				}
			}
		} else if (ax > bx) {
			for (int x = ax; x > bx; x--) {
				canvas.setRGB(x, y, Color.WHITE.getRGB());
				error = error + deltaerr;
				if (error >= 0.5) {
					y = y + 1;
					error = error - 1;
				}
			}
		}
	}
	
	private void drawLine2(int x1, int x2, int y1, int y2, int triangleIndex) {
		//Utilizar coords. do triangulo para achar a coord. baricentrica do ponto a ser pintado
			//para então encontrar o Z dele em coord. de vista
		int d = 0;
		int dx = Math.abs(x2 - x1);
		int dy = Math.abs(y2 - y1);
		int dx2 = 2 * dx;
		int dy2 = 2 * dy;
		int ix = x1 < x2 ? 1 : -1;
		int iy = y1 < y2 ? 1 : -1;
		int x = x1;
		int y = y1;
		int[] Ia, Id, Is, I;
		Ia = new int[3];
		Is = new int[3];
		Id = new int[3];
		I = new int[3];
		
		float[] P = new float[2];
		float[] barycentricCoord = new float[3];
		float[] Pv = new float[3];
		float[] V = new float[3];
		float[] N = new float[3];
		float[] L = new float[3];
		float[] R = new float[3];

		if (dx >= dy) {
			loop1:
			while (true) {
				//obter ponto em coordenada de vista
				P[0] = (float)x;
				P[1] = (float)y;
				barycentricCoord = bc.BarycentricCrdnt(P, vertexCoordinatesScreen[triangleIndexes[triangleIndex][0]-1], 
						vertexCoordinatesScreen[triangleIndexes[triangleIndex][1]-1], vertexCoordinatesScreen[triangleIndexes[triangleIndex][2]-1]);
				Pv[0] = vertexCoordinatesView[triangleIndexes[triangleIndex][0] - 1] [0] * barycentricCoord[0] +
							vertexCoordinatesView[triangleIndexes[triangleIndex][1] - 1] [0] * barycentricCoord[0] +
							vertexCoordinatesView[triangleIndexes[triangleIndex][2] - 1] [0] * barycentricCoord[0];
				Pv[1] = vertexCoordinatesView[triangleIndexes[triangleIndex][0] - 1] [1] * barycentricCoord[1] +
						vertexCoordinatesView[triangleIndexes[triangleIndex][1] - 1] [1] * barycentricCoord[1] +
						vertexCoordinatesView[triangleIndexes[triangleIndex][2] - 1] [1] * barycentricCoord[1];
				Pv[2] = vertexCoordinatesView[triangleIndexes[triangleIndex][0] - 1] [2] * barycentricCoord[2] +
						vertexCoordinatesView[triangleIndexes[triangleIndex][1] - 1] [2] * barycentricCoord[2] +
						vertexCoordinatesView[triangleIndexes[triangleIndex][2] - 1] [2] * barycentricCoord[2];
				//ponto em coordenada de vista Pv obtido. Comparar seu Z com o do Z-Buffer para a posição
				if(Pv[2] < zBuffer[x][y]) {
					//gravando no z-buffer
					zBuffer[x][y] = Pv[2];
					//calcular a cor e pintar:
					
					Ia[0] = (int) (Ka * Iamb[0]);
					Ia[1] = (int) (Ka * Iamb[1]);
					Ia[2] = (int) (Ka * Iamb[2]);
					
					V[0] = -Pv[0];
					V[1] = -Pv[1];
					V[2] = -Pv[2];
					V = vnz.VectorNrmlztn(V);
					N[0] = vertexNormals[triangleIndexes[triangleIndex][0] - 1] [0] * barycentricCoord[0] +
							vertexNormals[triangleIndexes[triangleIndex][1] - 1] [0] * barycentricCoord[0] +
							vertexNormals[triangleIndexes[triangleIndex][2] - 1] [0] * barycentricCoord[0];
					N[1] = vertexNormals[triangleIndexes[triangleIndex][0] - 1] [1] * barycentricCoord[1] +
							vertexNormals[triangleIndexes[triangleIndex][1] - 1] [1] * barycentricCoord[1] +
							vertexNormals[triangleIndexes[triangleIndex][2] - 1] [1] * barycentricCoord[1];
					N[2] = vertexNormals[triangleIndexes[triangleIndex][0] - 1] [2] * barycentricCoord[2] +
							vertexNormals[triangleIndexes[triangleIndex][1] - 1] [2] * barycentricCoord[2] +
							vertexNormals[triangleIndexes[triangleIndex][2] - 1] [2] * barycentricCoord[2];
					N = vnz.VectorNrmlztn(N);
					L[0] = Pl[0] - Pv[0];
					L[1] = Pl[1] - Pv[1];
					L[2] = Pl[2] - Pv[2];
					L = vnz.VectorNrmlztn(L);
					R[0] = (2 * dp.DotPrdct(N, L) * N[0]) - L[0];
					R[1] = (2 * dp.DotPrdct(N, L) * N[1]) - L[1];
					R[2] = (2 * dp.DotPrdct(N, L) * N[2]) - L[2];
					
					//casos especiais
					if(dp.DotPrdct(N, L) < 0 && dp.DotPrdct(N, V) < 0) {
						N[0] = -N[0];
						N[1] = -N[1];
						N[2] = -N[2];
					}
					if(dp.DotPrdct(N, L) < 0 && dp.DotPrdct(N, V) >= 0) {
						Id[0] = 0; Id[1] = 0; Id[2] = 0;
						Is[0] = 0; Is[1] = 0; Is[2] = 0;
					} else {
						Id[0] = (int) (dp.DotPrdct(N, L) * Kd[0] * Od[0] * Il[0]);
						Id[1] = (int) (dp.DotPrdct(N, L) * Kd[1] * Od[1] * Il[1]);
						Id[2] = (int) (dp.DotPrdct(N, L) * Kd[2] * Od[2] * Il[2]);
					}
					if(dp.DotPrdct(R, V) < 0) {
						Is[0] = 0; Is[1] = 0; Is[2] = 0;
					} else if( !(dp.DotPrdct(N, L) < 0 && dp.DotPrdct(N, V) >= 0) ) {
						Is[0] = (int) (Math.pow(dp.DotPrdct(R, V), Eta) * Ks * Il[0]);
						Is[1] = (int) (Math.pow(dp.DotPrdct(R, V), Eta) * Ks * Il[1]);
						Is[2] = (int) (Math.pow(dp.DotPrdct(R, V), Eta) * Ks * Il[2]);
					}
					//fim dos casos especiais
					
					I[0] = Ia[0] + Id[0] + Is[0];
					I[1] = Ia[1] + Id[1] + Is[1];
					I[2] = Ia[2] + Id[2] + Is[2];
					if(I[0] > 255)
						I[0] = 255;
					if(I[1] > 255)
						I[1] = 255;
					if(I[2] > 255)
						I[2] = 255;
					
					canvas.setRGB(x, y, new Color(I[0], I[1], I[2]).getRGB());
					if (x == x2)
						break loop1;
					x += ix;
					d += dy2;
					if (d > dx) {
						y += iy;
						d -= dx2;
					}
				} else {
					if (x == x2)
						break loop1;
					x += ix;
					d += dy2;
					if (d > dx) {
						y += iy;
						d -= dx2;
					}
				}
				
			}
		} else {
			loop2:
			while (true) {
				//obter ponto em coordenada de vista
				P[0] = (float)x;
				P[1] = (float)y;
				barycentricCoord = bc.BarycentricCrdnt(P, vertexCoordinatesScreen[triangleIndexes[triangleIndex][0]-1], 
						vertexCoordinatesScreen[triangleIndexes[triangleIndex][1]-1], vertexCoordinatesScreen[triangleIndexes[triangleIndex][2]-1]);
				Pv[0] = vertexCoordinatesView[triangleIndexes[triangleIndex][0] - 1] [0] * barycentricCoord[0] +
							vertexCoordinatesView[triangleIndexes[triangleIndex][1] - 1] [0] * barycentricCoord[0] +
							vertexCoordinatesView[triangleIndexes[triangleIndex][2] - 1] [0] * barycentricCoord[0];
				Pv[1] = vertexCoordinatesView[triangleIndexes[triangleIndex][0] - 1] [1] * barycentricCoord[1] +
						vertexCoordinatesView[triangleIndexes[triangleIndex][1] - 1] [1] * barycentricCoord[1] +
						vertexCoordinatesView[triangleIndexes[triangleIndex][2] - 1] [1] * barycentricCoord[1];
				Pv[2] = vertexCoordinatesView[triangleIndexes[triangleIndex][0] - 1] [2] * barycentricCoord[2] +
						vertexCoordinatesView[triangleIndexes[triangleIndex][1] - 1] [2] * barycentricCoord[2] +
						vertexCoordinatesView[triangleIndexes[triangleIndex][2] - 1] [2] * barycentricCoord[2];
				//ponto em coordenada de vista Pv obtido. Comparar seu Z com o do Z-Buffer para a posição
				if(Pv[2] < zBuffer[x][y]) {
					//gravando no z-buffer
					zBuffer[x][y] = Pv[2];
					//calcular a cor e pintar:

					Ia[0] = (int) (Ka * Iamb[0]);
					Ia[1] = (int) (Ka * Iamb[1]);
					Ia[2] = (int) (Ka * Iamb[2]);
					
					V[0] = -Pv[0];
					V[1] = -Pv[1];
					V[2] = -Pv[2];
					V = vnz.VectorNrmlztn(V);
					N[0] = vertexNormals[triangleIndexes[triangleIndex][0] - 1] [0] * barycentricCoord[0] +
							vertexNormals[triangleIndexes[triangleIndex][1] - 1] [0] * barycentricCoord[0] +
							vertexNormals[triangleIndexes[triangleIndex][2] - 1] [0] * barycentricCoord[0];
					N[1] = vertexNormals[triangleIndexes[triangleIndex][0] - 1] [1] * barycentricCoord[1] +
							vertexNormals[triangleIndexes[triangleIndex][1] - 1] [1] * barycentricCoord[1] +
							vertexNormals[triangleIndexes[triangleIndex][2] - 1] [1] * barycentricCoord[1];
					N[2] = vertexNormals[triangleIndexes[triangleIndex][0] - 1] [2] * barycentricCoord[2] +
							vertexNormals[triangleIndexes[triangleIndex][1] - 1] [2] * barycentricCoord[2] +
							vertexNormals[triangleIndexes[triangleIndex][2] - 1] [2] * barycentricCoord[2];
					N = vnz.VectorNrmlztn(N);
					L[0] = Pl[0] - Pv[0];
					L[1] = Pl[1] - Pv[1];
					L[2] = Pl[2] - Pv[2];
					L = vnz.VectorNrmlztn(L);
					R[0] = (2 * dp.DotPrdct(N, L) * N[0]) - L[0];
					R[1] = (2 * dp.DotPrdct(N, L) * N[1]) - L[1];
					R[2] = (2 * dp.DotPrdct(N, L) * N[2]) - L[2];
					
					//casos especiais
					if(dp.DotPrdct(N, L) < 0 && dp.DotPrdct(N, V) < 0) {
						N[0] = -N[0];
						N[1] = -N[1];
						N[2] = -N[2];
					}
					if(dp.DotPrdct(N, L) < 0 && dp.DotPrdct(N, V) >= 0) {
						Id[0] = 0; Id[1] = 0; Id[2] = 0;
						Is[0] = 0; Is[1] = 0; Is[2] = 0;
					} else {
						Id[0] = (int) (dp.DotPrdct(N, L) * Kd[0] * Od[0] * Il[0]);
						Id[1] = (int) (dp.DotPrdct(N, L) * Kd[1] * Od[1] * Il[1]);
						Id[2] = (int) (dp.DotPrdct(N, L) * Kd[2] * Od[2] * Il[2]);
					}
					if(dp.DotPrdct(R, V) < 0) {
						Is[0] = 0; Is[1] = 0; Is[2] = 0;
					} else if( !(dp.DotPrdct(N, L) < 0 && dp.DotPrdct(N, V) >= 0) ) {
						Is[0] = (int) (Math.pow(dp.DotPrdct(R, V), Eta) * Ks * Il[0]);
						Is[1] = (int) (Math.pow(dp.DotPrdct(R, V), Eta) * Ks * Il[1]);
						Is[2] = (int) (Math.pow(dp.DotPrdct(R, V), Eta) * Ks * Il[2]);
					}
					//fim dos casos especiais
					
					I[0] = Ia[0] + Id[0] + Is[0];
					I[1] = Ia[1] + Id[1] + Is[1];
					I[2] = Ia[2] + Id[2] + Is[2];
					if(I[0] > 255)
						I[0] = 255;
					if(I[1] > 255)
						I[1] = 255;
					if(I[2] > 255)
						I[2] = 255;
					
					canvas.setRGB(x, y, new Color(I[0], I[1], I[2]).getRGB());
					if (y == y2)
						break loop2;
					y += iy;
					d += dx2;
					if (d > dy) {
						x += ix;
						d -= dy2;
					}
				} else {
					if (y == y2)
						break loop2;
					y += iy;
					d += dx2;
					if (d > dy) {
						x += ix;
						d -= dy2;
					}
				}
				
			}
		}
	}

	private void drawBottomTriangle(int ax, int ay, int bx, int by, int cx, int cy, int triangleIndex) {
		// a é o vertice do topo, b o da esquerda e c o da direita
		float slope1 = (float) (bx - ax) / (float) (by - ay);
		float slope2 = (float) (cx - ax) / (float) (cy - ay);

		float x1 = ax;
		float x2 = ax;

		for (int scanY = ay; scanY <= by; scanY++) {
			drawLine2((int) x1, (int) x2, scanY, scanY, triangleIndex);
			x1 = x1 + slope1;
			x2 = x2 + slope2;
		}
	}

	private void drawTopTriangle(int ax, int ay, int bx, int by, int cx, int cy, int triangleIndex) {
		// c é o vertice de baixo, a é o vertice da esquerda e b o da direita
		float slope1 = (float) (cx - ax) / (float) (cy - ay);
		float slope2 = (float) (cx - bx) / (float) (cy - by);

		float x1 = cx;
		float x2 = cx;

		for (int scanY = cy; scanY >= ay; scanY--) {
			drawLine2((int) x1, (int) x2, scanY, scanY, triangleIndex);
			x1 = x1 - slope1;
			x2 = x2 - slope2;
		}
	}

}
