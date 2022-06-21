package guis;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.Arrays;

import javax.swing.JPanel;

import main.FuzzyCartAndPendulumSystem;

public class HedgeDisplay extends JPanel {
	private static final long serialVersionUID = -2659652282825813983L;
	
	private static final int WIDTH = 600;
	private static final int HEIGHT = 180;
	
	private FuzzyCartAndPendulumSystem.Hedge[] hs;

	public HedgeDisplay(FuzzyCartAndPendulumSystem.Hedge[] hs) {
		this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
		this.hs = hs;
		Arrays.sort(hs);
	}
	
	public void updateHedges(FuzzyCartAndPendulumSystem.Hedge[] hs) {
		this.hs = hs;
		this.repaint();
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.setColor(Color.BLACK);
		g.drawLine(WIDTH / 8, HEIGHT - (HEIGHT / 8), WIDTH / 8, HEIGHT / 8);
		g.drawLine(WIDTH - (WIDTH / 8), HEIGHT - (HEIGHT / 8), WIDTH - (WIDTH / 8), HEIGHT / 8);
		g.drawLine(WIDTH / 8, HEIGHT - (HEIGHT / 8), WIDTH - (WIDTH / 8), HEIGHT - (HEIGHT / 8));
		for (int i = 0; i < hs.length; i++) {
			switch (this.hs[i].getName()) {
				case "EXTREMELY": g.setColor(Color.BLUE); break;
				case "VERY": g.setColor(Color.RED); break;
				case "MEDIUM": g.setColor(Color.GREEN); break;
				case "SOMEWHAT": g.setColor(Color.CYAN); break;
				case "SLIGHTLY": g.setColor(Color.ORANGE); break;
				case "ABS_MAX": g.setColor(Color.YELLOW); break;
				case "ABS_MEDIAN": g.setColor(Color.PINK); break;
				case "ABS_MIN": g.setColor(Color.GRAY); break;
				case "ANY": g.setColor(Color.MAGENTA); break;
			}
			g.drawLine((WIDTH / 8) + (int)(this.hs[i].getStart() * (WIDTH - (WIDTH / 4))), HEIGHT - (HEIGHT / 8), (WIDTH / 8) + (int)(this.hs[i].getFirstPeek() * (WIDTH - (WIDTH / 4))), HEIGHT / 8);
			g.drawLine((WIDTH / 8) + (int)(this.hs[i].getFirstPeek() * (WIDTH - (WIDTH / 4))), HEIGHT / 8, (WIDTH / 8) + (int)(this.hs[i].getSecondPeek() * (WIDTH - (WIDTH / 4))), HEIGHT / 8);
			g.drawLine((WIDTH / 8) + (int)(this.hs[i].getSecondPeek() * (WIDTH - (WIDTH / 4))), HEIGHT / 8, (WIDTH / 8) + (int)(this.hs[i].getEnd() * (WIDTH - (WIDTH / 4))), HEIGHT - (HEIGHT / 8));
		}
	}
}
