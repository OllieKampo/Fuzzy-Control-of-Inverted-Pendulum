package guis;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.Arrays;

import javax.swing.JPanel;

import main.FuzzyCartAndPendulumSystem;

public class FunctionDisplay extends JPanel {
	private static final long serialVersionUID = -2659652282825813983L;
	
	private static final int WIDTH = 600;
	private static final int HEIGHT = 180;
	
	private FuzzyCartAndPendulumSystem.MemberFunction[] mfs;

	public FunctionDisplay(FuzzyCartAndPendulumSystem.MemberFunction[] mfs) {
		this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
		this.mfs = mfs;
	}
	
	public void updateMemberFunctions(FuzzyCartAndPendulumSystem.MemberFunction[] mfs) {
		this.mfs = mfs;
		this.repaint();
		Arrays.sort(mfs);
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.setColor(Color.BLACK);
		g.drawLine(WIDTH / 8, HEIGHT - (HEIGHT / 8), WIDTH / 8, HEIGHT / 8);
		g.drawLine(WIDTH - (WIDTH / 8), HEIGHT - (HEIGHT / 8), WIDTH - (WIDTH / 8), HEIGHT / 8);
		g.drawLine(WIDTH / 8, HEIGHT - (HEIGHT / 8), WIDTH - (WIDTH / 8), HEIGHT - (HEIGHT / 8));
		for (int i = 0; i < mfs.length; i++) {
			switch (this.mfs[i].getName()) {
				case "HUGE": g.setColor(Color.BLUE); break;
				case "LARGE": g.setColor(Color.RED); break;
				case "BIG": g.setColor(Color.GREEN); break;
				case "SMALL": g.setColor(Color.CYAN); break;
				case "LITTLE": g.setColor(Color.ORANGE); break;
				case "TINY": g.setColor(Color.MAGENTA); break;
			}
			g.drawLine((WIDTH / 8) + (int)(this.mfs[i].getStart() * (WIDTH - (WIDTH / 4))), HEIGHT - (HEIGHT / 8), (WIDTH / 8) + (int)(this.mfs[i].getFirstPeek() * (WIDTH - (WIDTH / 4))), HEIGHT / 8);
			g.drawLine((WIDTH / 8) + (int)(this.mfs[i].getFirstPeek() * (WIDTH - (WIDTH / 4))), HEIGHT / 8, (WIDTH / 8) + (int)(this.mfs[i].getSecondPeek() * (WIDTH - (WIDTH / 4))), HEIGHT / 8);
			g.drawLine((WIDTH / 8) + (int)(this.mfs[i].getSecondPeek() * (WIDTH - (WIDTH / 4))), HEIGHT / 8, (WIDTH / 8) + (int)(this.mfs[i].getEnd() * (WIDTH - (WIDTH / 4))), HEIGHT - (HEIGHT / 8));
		}
	}
}
