package guis;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.util.Arrays;

import javax.swing.JPanel;

import main.FuzzyCartAndPendulumSystem;

public class FunctionActivationDisplay extends JPanel {
	private static final long serialVersionUID = 6253089091910921909L;
	
	private static final int WIDTH = 300;
	private static final int PANEL_HEIGHT = 120;
	private static final int GRAPH_HEIGHT = 110;
	private static final int PANEL_GRAPH_HEIGHT_DIFFERENCE = PANEL_HEIGHT - GRAPH_HEIGHT;
	
	private FuzzyCartAndPendulumSystem.MemberFunction[] mfs;
	private final FuzzyCartAndPendulumSystem.FuzzyVariable fv;
	private double var;

	public FunctionActivationDisplay(FuzzyCartAndPendulumSystem.FuzzyVariable fv, FuzzyCartAndPendulumSystem.MemberFunction[] mfs) {
		this.setPreferredSize(new Dimension(WIDTH, PANEL_HEIGHT));
		this.fv = fv;
		this.mfs = mfs;
		this.var = 0.0;
	}
	
	public void updateMemberFunctions(FuzzyCartAndPendulumSystem.MemberFunction[] mfs) {
		this.mfs = mfs;
		Arrays.sort(mfs);
	}
	
	public void updateVariableValue(double var) {
		this.var = var;
		this.repaint();
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.setColor(Color.BLACK);
		g.setFont(new Font("", Font.BOLD, 15));
		g.drawString(fv.getName(), WIDTH / 8, PANEL_HEIGHT / 10);
		g.drawLine(WIDTH / 8, PANEL_GRAPH_HEIGHT_DIFFERENCE + GRAPH_HEIGHT - (GRAPH_HEIGHT / 8), WIDTH / 8, PANEL_GRAPH_HEIGHT_DIFFERENCE + GRAPH_HEIGHT / 8);
		g.drawLine(WIDTH - (WIDTH / 8), PANEL_GRAPH_HEIGHT_DIFFERENCE + GRAPH_HEIGHT - (GRAPH_HEIGHT / 8), WIDTH - (WIDTH / 8), PANEL_GRAPH_HEIGHT_DIFFERENCE + GRAPH_HEIGHT / 8);
		g.drawLine(WIDTH / 8, PANEL_GRAPH_HEIGHT_DIFFERENCE + GRAPH_HEIGHT - (GRAPH_HEIGHT / 8), WIDTH - (WIDTH / 8), PANEL_GRAPH_HEIGHT_DIFFERENCE + GRAPH_HEIGHT - (GRAPH_HEIGHT / 8));
		for (int i = 0; i < mfs.length; i++) {
			switch (this.mfs[i].getName()) {
				case "HUGE": g.setColor(Color.BLUE); break;
				case "LARGE": g.setColor(Color.RED); break;
				case "BIG": g.setColor(Color.GREEN); break;
				case "SMALL": g.setColor(Color.CYAN); break;
				case "LITTLE": g.setColor(Color.ORANGE); break;
				case "TINY": g.setColor(Color.MAGENTA); break;
			}
			int a = (WIDTH / 8) + (int)(this.mfs[i].getStart() * (WIDTH - (WIDTH / 4)));
			int b = (WIDTH / 8) + (int)(this.mfs[i].getFirstPeek() * (WIDTH - (WIDTH / 4)));
			int c = (WIDTH / 8) + (int)(this.mfs[i].getSecondPeek() * (WIDTH - (WIDTH / 4)));
			int d = (WIDTH / 8) + (int)(this.mfs[i].getEnd() * (WIDTH - (WIDTH / 4)));
			double truth = mfs[i].fuzzify((this.var - this.fv.getMinVal()) / (this.fv.getMaxVal() - this.fv.getMinVal()));
			int[] xPoints = {a, a + (int)((b - a) * truth), d - (int)((d - c) * truth), d};
			int[] yPoints = {PANEL_GRAPH_HEIGHT_DIFFERENCE + GRAPH_HEIGHT - (GRAPH_HEIGHT / 8), PANEL_GRAPH_HEIGHT_DIFFERENCE + GRAPH_HEIGHT - (GRAPH_HEIGHT / 8) - (int)((GRAPH_HEIGHT - (GRAPH_HEIGHT / 4)) * truth),
					PANEL_GRAPH_HEIGHT_DIFFERENCE + GRAPH_HEIGHT - (GRAPH_HEIGHT / 8) - (int)((GRAPH_HEIGHT - (GRAPH_HEIGHT / 4)) * truth), PANEL_GRAPH_HEIGHT_DIFFERENCE + GRAPH_HEIGHT - (GRAPH_HEIGHT / 8)};
			g.fillPolygon(xPoints, yPoints, 4);
			g.drawLine(a, PANEL_GRAPH_HEIGHT_DIFFERENCE + GRAPH_HEIGHT - (GRAPH_HEIGHT / 8), b, PANEL_GRAPH_HEIGHT_DIFFERENCE + GRAPH_HEIGHT / 8);
			g.drawLine(b, PANEL_GRAPH_HEIGHT_DIFFERENCE + GRAPH_HEIGHT / 8, c, PANEL_GRAPH_HEIGHT_DIFFERENCE + GRAPH_HEIGHT / 8);
			g.drawLine(c, PANEL_GRAPH_HEIGHT_DIFFERENCE + GRAPH_HEIGHT / 8, d, PANEL_GRAPH_HEIGHT_DIFFERENCE + GRAPH_HEIGHT - (GRAPH_HEIGHT / 8));
		}
	}
}
