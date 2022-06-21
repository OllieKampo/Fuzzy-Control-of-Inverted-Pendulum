package guis;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.util.ArrayList;

import javax.swing.JPanel;

public class PositionGraph extends JPanel {
	private static final long serialVersionUID = 8963089200088066803L;
	
	private static final int WIDTH = 400;
	private static final int HEIGHT = 400;
	
	private final ArrayList<Integer> historyX;
	private final ArrayList<Integer> historyY;

	public PositionGraph() {
		this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
		this.historyX = new ArrayList<>();
		this.historyY = new ArrayList<>();
	}
	
	public void updateDisplay(double xValue, double xMin, double xMax, double yValue, double yMin, double yMax) {
		this.historyX.add(new Integer((WIDTH / 8) + (int)(((xValue - xMin) / (xMax - xMin)) * ((WIDTH * 3) / 4))));
		this.historyY.add(new Integer((HEIGHT / 8) + (int)(((yValue - yMin) / (yMax - yMin)) * ((HEIGHT * 3) / 4))));
		if (this.historyX.size() > 100) {
			this.historyX.remove(0);
			this.historyY.remove(0);
		}
		this.repaint();
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.setColor(Color.BLACK);
		g.setFont(new Font("", Font.BOLD, 15));
		g.drawString("Cart position (mm)", WIDTH - (WIDTH / 3), (HEIGHT / 2) + (HEIGHT / 20));
		g.drawString("Pendulum angle (rads)", (WIDTH / 2) + (WIDTH / 40), HEIGHT - (HEIGHT / 10));
		g.drawLine(WIDTH / 2, HEIGHT / 8, WIDTH / 2, HEIGHT - (HEIGHT / 8));
		g.drawLine(WIDTH / 8, HEIGHT / 2, WIDTH - (WIDTH / 8), HEIGHT / 2);
		g.setColor(Color.CYAN);
		int[] a = new int[this.historyX.size()];
		for (int i = 0; i < a.length; i++) {
			a[i] = this.historyX.get(i).intValue();
		}
		int[] b = new int[this.historyY.size()];
		for (int i = 0; i < b.length; i++) {
			b[i] = HEIGHT - this.historyY.get(i).intValue();
		}
		g.drawPolyline(a, b, a.length);
		g.setColor(Color.RED);
		if (this.historyX.size() != 0) {
			g.fillOval(this.historyX.get(this.historyX.size() - 1).intValue() - 5, HEIGHT - this.historyY.get(this.historyY.size() - 1).intValue() - 5, 10, 10);
		}
	}
}
