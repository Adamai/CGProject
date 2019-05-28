package functions;

import java.awt.event.KeyListener;
import java.util.Scanner;

import javax.swing.JFrame;

public class Main {

	public static void main(String[] args) {
		// LETRA A -----------------------------------------
		MatricesMult mm = new MatricesMult();
		float[][] a1 = new float[2][3];
		a1[0][0] = (float) 1.5;
		a1[0][1] = (float) 2.5;
		a1[0][2] = (float) 3.5;
		a1[1][0] = (float) 4.5;
		a1[1][1] = (float) 5.5;
		a1[1][2] = (float) 6.5;

		float[][] a2 = new float[3][2];
		a2[0][0] = (float) 7.5;
		a2[0][1] = (float) 8.5;
		a2[1][0] = (float) 9.5;
		a2[1][1] = (float) 10.5;
		a2[2][0] = (float) 11.5;
		a2[2][1] = (float) 12.5;

		float[][] a = mm.MatricesMltply(a1, a2);

		System.out.println("a)");
		for (int i = 0; i < a.length; i++) {
			for (int j = 0; j < a[0].length; j++) {
				System.out.print(a[i][j] + " ");
			}
			System.out.println("");
		}

		// LETRA B -------------------------------------------
		PointSub ps = new PointSub();
		float b1[] = new float[3];
		b1[0] = (float) 3.5;
		b1[1] = (float) 1.5;
		b1[2] = (float) 2.0;

		float b2[] = new float[3];
		b2[0] = (float) 1.0;
		b2[1] = (float) 2.0;
		b2[2] = (float) 1.5;

		float[] resultb = ps.PointSb(b1, b2);
		System.out.println("b)\n" + resultb[0] + " " + resultb[1] + " " + resultb[2]);

		// LETRA C -------------------------------------------
		DotProduct dp = new DotProduct();
		float c1[] = new float[3];
		c1[0] = (float) 3.5;
		c1[1] = (float) 1.5;
		c1[2] = (float) 2.0;

		float c2[] = new float[3];
		c2[0] = (float) 1.0;
		c2[1] = (float) 2.0;
		c2[2] = (float) 1.5;

		float resultc = dp.DotPrdct(c1, c2);
		System.out.println("c)\n" + resultc);

		// LETRA D -------------------------------------------
		CrossProduct cp = new CrossProduct();
		float d1[] = new float[3];
		d1[0] = (float) 3.5;
		d1[1] = (float) 1.5;
		d1[2] = (float) 2.0;

		float d2[] = new float[3];
		d2[0] = (float) 1.0;
		d2[1] = (float) 2.0;
		d2[2] = (float) 1.5;

		float[] resultd = cp.CrossPrdct(d1, d2);
		System.out.println("d)\n" + resultd[0] + " " + resultd[1] + " " + resultd[2]);

		// LETRA E -------------------------------------------
		VectorNorm vn = new VectorNorm();
		float e1[] = new float[3];
		e1[0] = (float) 3.5;
		e1[1] = (float) 1.5;
		e1[2] = (float) 2.0;

		float resulte = vn.VectorNrm(e1);
		System.out.println("e)\n" + resulte);

		// LETRA F -------------------------------------------
		VectorNormalization vnz = new VectorNormalization();
		float f1[] = new float[3];
		f1[0] = (float) 3.5;
		f1[1] = (float) 1.5;
		f1[2] = (float) 2.0;

		float[] resultf = vnz.VectorNrmlztn(f1);
		System.out.println("f)\n" + resultf[0] + " " + resultf[1] + " " + resultf[2]);

		// LETRA G -------------------------------------------
		BarycentricCoordinate bc = new BarycentricCoordinate();
		float gp[] = new float[2];
		gp[0] = (float) -0.25;
		gp[1] = (float) 0.75;

		float g1[] = new float[2];
		g1[0] = (float) -1.0;
		g1[1] = (float) 1.0;

		float g2[] = new float[2];
		g2[0] = (float) 0.0;
		g2[1] = (float) -1.0;

		float g3[] = new float[2];
		g3[0] = (float) 1.0;
		g3[1] = (float) 1.0;

		float resultg[] = bc.BarycentricCrdnt(gp, g1, g2, g3);
		System.out.println("g)\n" + resultg[0] + " " + resultg[1] + " " + resultg[2]);

		// LETRA H -------------------------------------------
		CartesianCoordinate cc = new CartesianCoordinate();
		float hp[] = new float[3];
		hp[0] = (float) 0.5;
		hp[1] = (float) 0.25;
		hp[2] = (float) 0.25;

		float h1[] = new float[2];
		h1[0] = (float) -1.0;
		h1[1] = (float) 1.0;

		float h2[] = new float[2];
		h2[0] = (float) 0.0;
		h2[1] = (float) -1.0;

		float h3[] = new float[2];
		h3[0] = (float) 1.0;
		h3[1] = (float) 1.0;

		float resulth[] = cc.CartesianCrdnt(hp, h1, h2, h3);
		System.out.println("h)\n" + resulth[0] + " " + resulth[1]);

		// SEGUNDA PARTE -------------------------------------------

		int width = 400;
		int height = 400;

		JFrame frame = new JFrame("Projection");
		PixelProjection pp = new PixelProjection(width, height, "calice2.byu", "camera.txt", "iluminacao.txt");

		frame.add(pp);
		frame.pack();
		frame.setVisible(true);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		while (true) {			//estado continuo da aplicação
			System.out.println("Comando?");
			Scanner scan = new Scanner(System.in);
			String cmd = scan.nextLine();
			if (cmd.equals("r")) {
				frame.dispose();
				frame = new JFrame("Projection");
				pp = new PixelProjection(width, height, "calice2.byu", "camera.txt", "iluminacao.txt");

				frame.add(pp);
				frame.pack();
				frame.setVisible(true);
				frame.setResizable(false);
				frame.setLocationRelativeTo(null);
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			}
		}

	}

}
