package functions;

import java.awt.event.KeyListener;
import java.util.Scanner;

import javax.swing.JFrame;

public class Main {

	public static void main(String[] args) {
		// LETRA A -----------------------------------------
		MatricesMult mm = new MatricesMult();
		double[][] a1 = new double[2][3];
		a1[0][0] = (double) 1.5;
		a1[0][1] = (double) 2.5;
		a1[0][2] = (double) 3.5;
		a1[1][0] = (double) 4.5;
		a1[1][1] = (double) 5.5;
		a1[1][2] = (double) 6.5;

		double[][] a2 = new double[3][2];
		a2[0][0] = (double) 7.5;
		a2[0][1] = (double) 8.5;
		a2[1][0] = (double) 9.5;
		a2[1][1] = (double) 10.5;
		a2[2][0] = (double) 11.5;
		a2[2][1] = (double) 12.5;

		double[][] a = mm.MatricesMltply(a1, a2);

		System.out.println("a)");
		for (int i = 0; i < a.length; i++) {
			for (int j = 0; j < a[0].length; j++) {
				System.out.print(a[i][j] + " ");
			}
			System.out.println("");
		}

		// LETRA B -------------------------------------------
		PointSub ps = new PointSub();
		double b1[] = new double[3];
		b1[0] = (double) 3.5;
		b1[1] = (double) 1.5;
		b1[2] = (double) 2.0;

		double b2[] = new double[3];
		b2[0] = (double) 1.0;
		b2[1] = (double) 2.0;
		b2[2] = (double) 1.5;

		double[] resultb = ps.PointSb(b1, b2);
		System.out.println("b)\n" + resultb[0] + " " + resultb[1] + " " + resultb[2]);

		// LETRA C -------------------------------------------
		DotProduct dp = new DotProduct();
		double c1[] = new double[3];
		c1[0] = (double) 3.5;
		c1[1] = (double) 1.5;
		c1[2] = (double) 2.0;

		double c2[] = new double[3];
		c2[0] = (double) 1.0;
		c2[1] = (double) 2.0;
		c2[2] = (double) 1.5;

		double resultc = dp.DotPrdct(c1, c2);
		System.out.println("c)\n" + resultc);

		// LETRA D -------------------------------------------
		CrossProduct cp = new CrossProduct();
		double d1[] = new double[3];
		d1[0] = (double) 3.5;
		d1[1] = (double) 1.5;
		d1[2] = (double) 2.0;

		double d2[] = new double[3];
		d2[0] = (double) 1.0;
		d2[1] = (double) 2.0;
		d2[2] = (double) 1.5;

		double[] resultd = cp.CrossPrdct(d1, d2);
		System.out.println("d)\n" + resultd[0] + " " + resultd[1] + " " + resultd[2]);

		// LETRA E -------------------------------------------
		VectorNorm vn = new VectorNorm();
		double e1[] = new double[3];
		e1[0] = (double) 3.5;
		e1[1] = (double) 1.5;
		e1[2] = (double) 2.0;

		double resulte = vn.VectorNrm(e1);
		System.out.println("e)\n" + resulte);

		// LETRA F -------------------------------------------
		VectorNormalization vnz = new VectorNormalization();
		double f1[] = new double[3];
		f1[0] = (double) 3.5;
		f1[1] = (double) 1.5;
		f1[2] = (double) 2.0;

		double[] resultf = vnz.VectorNrmlztn(f1);
		System.out.println("f)\n" + resultf[0] + " " + resultf[1] + " " + resultf[2]);

		// LETRA G -------------------------------------------
		BarycentricCoordinate bc = new BarycentricCoordinate();
		double gp[] = new double[2];
		gp[0] = (double) -0.25;
		gp[1] = (double) 0.75;

		double g1[] = new double[2];
		g1[0] = (double) -1.0;
		g1[1] = (double) 1.0;

		double g2[] = new double[2];
		g2[0] = (double) 0.0;
		g2[1] = (double) -1.0;

		double g3[] = new double[2];
		g3[0] = (double) 1.0;
		g3[1] = (double) 1.0;

		double resultg[] = bc.BarycentricCrdnt(gp, g1, g2, g3);
		System.out.println("g)\n" + resultg[0] + " " + resultg[1] + " " + resultg[2]);

		// LETRA H -------------------------------------------
		CartesianCoordinate cc = new CartesianCoordinate();
		double hp[] = new double[3];
		hp[0] = (double) 0.5;
		hp[1] = (double) 0.25;
		hp[2] = (double) 0.25;

		double h1[] = new double[2];
		h1[0] = (double) -1.0;
		h1[1] = (double) 1.0;

		double h2[] = new double[2];
		h2[0] = (double) 0.0;
		h2[1] = (double) -1.0;

		double h3[] = new double[2];
		h3[0] = (double) 1.0;
		h3[1] = (double) 1.0;

		double resulth[] = cc.CartesianCrdnt(hp, h1, h2, h3);
		System.out.println("h)\n" + resulth[0] + " " + resulth[1]);

		// SEGUNDA PARTE -------------------------------------------

		int width = 400;
		int height = 400;
		String entrada = "calice2.byu";
		
		JFrame frame = new JFrame("Projection");
		PixelProjection pp = new PixelProjection(width, height, entrada, "camera.txt", "iluminacao.txt");

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
				pp = new PixelProjection(width, height, entrada, "camera.txt", "iluminacao.txt");

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
