package guis;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.util.ArrayList;

import javax.swing.JPanel;

public class ResponseGraph extends JPanel {
	private static final long serialVersionUID = 2989540266172049166L;
	
	public static final int WIDTH = 400;
	public static final int HEIGHT = 400;
	
	private final String name;
	private final ArrayList<Integer> history;
	private double min;
	private double max;

	public ResponseGraph(String name, double min, double max) {
		this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
		this.name = name;
		this.history = new ArrayList<>();
		this.min = min;
		this.max = max;
	}
	
	public void addPointToEnd(double yValue) {
		this.history.add(new Integer((HEIGHT / 8) + (int)(((yValue - min) / (max - min)) * ((HEIGHT * 3) / 4))));
		if (this.history.size() > 300) {
			this.history.remove(0);
		}
		this.repaint();
	}
	
	public void removePointFromEnd() {
		if (this.history.size() > 0) {
			this.history.remove(this.history.size() - 1);
		}
		this.repaint();
	}
	
	public void clear() {
		this.history.clear();
		this.repaint();
	}
	
	public void addPointToStart(double yValue) {
		this.history.add(0, new Integer((HEIGHT / 8) + (int)(((yValue - min) / (max - min)) * ((HEIGHT * 3) / 4))));
		if (this.history.size() > 300) {
			this.history.remove(this.history.size() - 1);
		}
		this.repaint();
	}
	
	public void removePointFromStart() {
		if (this.history.size() > 0) {
			this.history.remove(0);
		}
		this.repaint();
	}
	
	public void setMinMax(double min, double max) {
		this.min = min;
		this.max = max;
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.setColor(Color.BLACK);
		g.setFont(new Font("", Font.BOLD, 15));
		g.drawString("Time (s)", WIDTH - (WIDTH / 5), (HEIGHT / 2) + (HEIGHT / 20));
		g.drawString(this.name, WIDTH / 7, HEIGHT - (HEIGHT / 10));
		g.drawString(String.format("%.02f", min), 40 - (String.format("%.02f", min).replace(".", "").length() * 7), HEIGHT - (HEIGHT / 8));
		g.drawString(String.format("%.02f", min + ((max - min) / 2)), 40 - (String.format("%.02f", max / 2).replace(".", "").length() * 7), HEIGHT / 2);
		g.drawString(String.format("%.02f", max), 40 - (String.format("%.02f", max).replace(".", "").length() * 7), HEIGHT / 8);
		g.drawLine(WIDTH / 8, HEIGHT / 8, WIDTH / 8, HEIGHT - (HEIGHT / 8));
		g.drawLine(WIDTH / 8, HEIGHT / 2, WIDTH - (WIDTH / 8), HEIGHT / 2);
		g.setColor(Color.CYAN);
		int[] a = new int[this.history.size()];
		for (int i = 0; i < a.length; i++) {
			a[i] = (WIDTH / 8) + i;
		}
		int[] b = new int[this.history.size()];
		for (int i = 0; i < b.length; i++) {
			b[i] = HEIGHT - this.history.get(i).intValue();
		}
		g.drawPolyline(a, b, a.length);
		g.setColor(Color.RED);
		if (this.history.size() != 0) {
			g.fillOval((WIDTH / 8) + this.history.size() - 5, HEIGHT - this.history.get(this.history.size() - 1).intValue() - 5, 10, 10);
		}
	}
}
