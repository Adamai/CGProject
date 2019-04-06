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
	
	public PixelProjection(int width, int height, String filePath) {
		BufferedReader reader;
		
		int lineCount = 0;
		int nVertex = 0;
		int nTriangles = 0;
		float xMax = 0, yMax = 0;
		float xMin = 99999, yMin = 99999;
		float[][] vertexCoordinates = null;
		float[][] vertexNormalizedCoordinates = null;
		int[][] triangleIndexes = null;
		try {
			reader = new BufferedReader(new FileReader(filePath));
			String line = reader.readLine();
			
			while(line != null) {
				if(lineCount == 0) {		//so rodara uma vez
					nVertex = Integer.valueOf(line.split(" ")[0]);
					nTriangles = Integer.valueOf(line.split(" ")[1]);
					vertexCoordinates = new float[nVertex][3];
					vertexNormalizedCoordinates = new float[nVertex][2];		//somente x e y necessarios
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
