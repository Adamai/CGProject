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

	public PixelProjection(int width, int height, String filePath, String cameraPath, String illuminationPath) {
		BufferedReader reader;
		BufferedReader cameraReader;
		BufferedReader illuminationReader;

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

		float[] Iamb = new float[3];
		float[] Il = new float[3];
		float Ka = 0, Ks = 0, Eta = 0;
		float[] Pl = new float[3];
		float[] Kd = new float[3];
		float[] Od = new float[3];

		float[] Vlinha = new float[3];
		float[] U = new float[3];
		float[] Nunder = new float[3];
		float[] Vlinhaunder = new float[3];
		float[] Uunder = new float[3];
		float[][] Imatrice = new float[3][3];
		float[][] vertexCoordinatesView = null;
		float[][] vertexCoordinatesPerspec = null;
		float[][] vertexCoordinatesScreen = null;
		float[][] barycenterCoordinates = null;
		float[][] triangleNormals = null;

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

						// PARTE 3
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
		
		//PARTE 3
		for(int i = 0; i < nTriangles; i++) { //passar pelos vertices de todos os triangulos. Calcular normal dos triangulos
			
		}
		//PARTE 3

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

		// PARTE 3
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
				drawLine2(v1x, v2x, v1y, v2y);
				drawLine2(v1x, v3x, v1y, v3y);
			} else if (v2y == v3y && v2y > v1y) { // checando se o triangulo é um bottom
				drawLine2(v1x, v2x, v1y, v2y);
				drawBottomTriangle(v1x, v1y, v2x, v2y, v3x, v3y);

			} else if (v1y == v2y && v1y < v3y) { // checando se o triangulo é top
				drawLine2(v1x, v3x, v1y, v3y);

				drawTopTriangle(v1x, v1y, v2x, v2y, v3x, v3y);

			} else { // dividir triangulo em 2
				int v4x = (int) (v1x + ((float) (v2y - v1y) / (float) (v3y - v1y)) * (v3x - v1x));
				int v4y = v2y;

				drawBottomTriangle(v1x, v1y, v2x, v2y, v4x, v4y);
				drawTopTriangle(v2x, v2y, v4x, v4y, v3x, v3y);

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

	private void drawLine2(int x1, int x2, int y1, int y2) {
		int d = 0;
		int dx = Math.abs(x2 - x1);
		int dy = Math.abs(y2 - y1);
		int dx2 = 2 * dx;
		int dy2 = 2 * dy;
		int ix = x1 < x2 ? 1 : -1;
		int iy = y1 < y2 ? 1 : -1;
		int x = x1;
		int y = y1;

		if (dx >= dy) {
			while (true) {
				canvas.setRGB(x, y, Color.WHITE.getRGB());
				if (x == x2)
					break;
				x += ix;
				d += dy2;
				if (d > dx) {
					y += iy;
					d -= dx2;
				}
			}
		} else {
			while (true) {
				canvas.setRGB(x, y, Color.WHITE.getRGB());
				if (y == y2)
					break;
				y += iy;
				d += dx2;
				if (d > dy) {
					x += ix;
					d -= dy2;
				}
			}
		}
	}

	private void drawBottomTriangle(int ax, int ay, int bx, int by, int cx, int cy) {
		// a é o vertice do topo, b o da esquerda e c o da direita
		float slope1 = (float) (bx - ax) / (float) (by - ay);
		float slope2 = (float) (cx - ax) / (float) (cy - ay);

		float x1 = ax;
		float x2 = ax;

		for (int scanY = ay; scanY <= by; scanY++) {
			drawLine2((int) x1, (int) x2, scanY, scanY);
			x1 = x1 + slope1;
			x2 = x2 + slope2;
		}
	}

	private void drawTopTriangle(int ax, int ay, int bx, int by, int cx, int cy) {
		// c é o vertice de baixo, a é o vertice da esquerda e b o da direita
		float slope1 = (float) (cx - ax) / (float) (cy - ay);
		float slope2 = (float) (cx - bx) / (float) (cy - by);

		float x1 = cx;
		float x2 = cx;

		for (int scanY = cy; scanY >= ay; scanY--) {
			drawLine2((int) x1, (int) x2, scanY, scanY);
			x1 = x1 - slope1;
			x2 = x2 - slope2;
		}
	}

}
