package guis;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JTextField;

import main.PIDCartAndPendulumSystem;

public class PIDForceProportionsDisplay extends JPanel {
	private static final long serialVersionUID = 4166613738132405169L;
	
	public static final int WIDTH = 1200;
	public static final int HEIGHT = 120;
	
	private final JTextField pp;
	private final JTextField ip;
	private final JTextField dp;
	private final JTextField pc;
	private final JTextField ic;
	private final JTextField dc;

	public PIDForceProportionsDisplay() {
		this.pp = new JTextField();
		this.ip = new JTextField();
		this.dp = new JTextField();
		this.pc = new JTextField();
		this.ic = new JTextField();
		this.dc = new JTextField();
		
		this.pp.setPreferredSize(new Dimension(80, 50));
		this.ip.setPreferredSize(new Dimension(80, 50));
		this.dp.setPreferredSize(new Dimension(80, 50));
		this.pc.setPreferredSize(new Dimension(80, 50));
		this.ic.setPreferredSize(new Dimension(80, 50));
		this.dc.setPreferredSize(new Dimension(80, 50));
		
		this.pp.setEditable(false);
		this.ip.setEditable(false);
		this.dp.setEditable(false);
		this.pc.setEditable(false);
		this.ic.setEditable(false);
		this.dc.setEditable(false);
		
		JPanel pLeft = new JPanel() {
			private static final long serialVersionUID = 2849941763398251105L;
			@Override
			public void paintComponent(Graphics g) {
				g.drawString("Pendulum angle proportional force:", 5, 30);
				g.drawString("Cart position proportional force:", 5, 80);
			}
		};
		pLeft.setPreferredSize(new Dimension(250, HEIGHT));
		
		JPanel pRight = new JPanel();
		pRight.setLayout(new GridLayout(2, 1));
		pRight.add(pp);
		pRight.add(pc);
		pRight.setPreferredSize(new Dimension(150, HEIGHT));
		
		JPanel pPanel = new JPanel();
		pPanel.setLayout(new BoxLayout(pPanel, BoxLayout.X_AXIS));
		pPanel.add(pLeft);
		pPanel.add(pRight);
		pPanel.setPreferredSize(new Dimension(WIDTH / 3, HEIGHT));
		
		JPanel iLeft = new JPanel() {
			private static final long serialVersionUID = 2849941763398251105L;
			@Override
			public void paintComponent(Graphics g) {
				g.drawString("Pendulum angle integral force:", 5, 30);
				g.drawString("Cart position integral force:", 5, 80);
			}
		};
		iLeft.setPreferredSize(new Dimension(250, HEIGHT));
		
		JPanel iRight = new JPanel();
		iRight.setLayout(new GridLayout(2, 1));
		iRight.add(ip);
		iRight.add(ic);
		iRight.setPreferredSize(new Dimension(150, HEIGHT));
		
		JPanel iPanel = new JPanel();
		iPanel.setLayout(new BoxLayout(iPanel, BoxLayout.X_AXIS));
		iPanel.add(iLeft);
		iPanel.add(iRight);
		iPanel.setPreferredSize(new Dimension(WIDTH / 3, HEIGHT));
		
		JPanel dLeft = new JPanel() {
			private static final long serialVersionUID = 2849941763398251105L;
			@Override
			public void paintComponent(Graphics g) {
				g.drawString("Pendulum angle derivative force:", 5, 30);
				g.drawString("Cart position derivative force:", 5, 80);
			}
		};
		dLeft.setPreferredSize(new Dimension(250, HEIGHT));
		
		JPanel dRight = new JPanel();
		dRight.setLayout(new GridLayout(2, 1));
		dRight.add(dp);
		dRight.add(dc);
		dRight.setPreferredSize(new Dimension(150, HEIGHT));
		
		JPanel dPanel = new JPanel();
		dPanel.setLayout(new BoxLayout(dPanel, BoxLayout.X_AXIS));
		dPanel.add(dLeft);
		dPanel.add(dRight);
		dPanel.setPreferredSize(new Dimension(WIDTH / 3, HEIGHT));
		
		this.setLayout(new GridLayout(1, 3));
		
		this.add(pPanel);
		this.add(iPanel);
		this.add(dPanel);
		
		this.setPreferredSize(new Dimension(1200, 100));
	}
	
	public void updateValues(PIDCartAndPendulumSystem pid) {
		double ppfc = pid.getPendulumAngleProportionalForceContribution();
		this.pp.setText(String.format("%.02f", ppfc));
		if (Math.abs(ppfc) < 150.0) {
			this.pp.setForeground(Color.BLUE);
		} else if (Math.abs(ppfc) < 300.0) {
			this.pp.setForeground(Color.ORANGE);
		} else {
			this.pp.setForeground(Color.RED);
		}
		double ipfc = pid.getPendulumAngleIntegralForceContribution();
		this.ip.setText(String.format("%.02f", ipfc));
		if (Math.abs(ipfc) < 150.0) {
			this.ip.setForeground(Color.BLUE);
		} else if (Math.abs(ipfc) < 300.0) {
			this.ip.setForeground(Color.ORANGE);
		} else {
			this.ip.setForeground(Color.RED);
		}
		double dpfc = pid.getPendulumAngleDerivativeForceContribution();
		this.dp.setText(String.format("%.02f", dpfc));
		if (Math.abs(dpfc) < 150.0) {
			this.dp.setForeground(Color.BLUE);
		} else if (Math.abs(dpfc) < 300.0) {
			this.dp.setForeground(Color.ORANGE);
		} else {
			this.dp.setForeground(Color.RED);
		}
		double pcfc = pid.getCartPositionProportionalForceContribution();
		this.pc.setText(String.format("%.02f", pcfc));
		if (Math.abs(pcfc) < 150.0) {
			this.pc.setForeground(Color.BLUE);
		} else if (Math.abs(pcfc) < 300.0) {
			this.pc.setForeground(Color.ORANGE);
		} else {
			this.pc.setForeground(Color.RED);
		}
		double icfc = pid.getCartPositionIntegralForceContribution();
		this.ic.setText(String.format("%.02f", icfc));
		if (Math.abs(icfc) < 150.0) {
			this.ic.setForeground(Color.BLUE);
		} else if (Math.abs(icfc) < 300.0) {
			this.ic.setForeground(Color.ORANGE);
		} else {
			this.ic.setForeground(Color.RED);
		}
		double dcfc = pid.getCartPositionDerivativeForceContribution();
		this.dc.setText(String.format("%.02f", dcfc));
		if (Math.abs(dcfc) < 150.0) {
			this.dc.setForeground(Color.BLUE);
		} else if (Math.abs(dcfc) < 300.0) {
			this.dc.setForeground(Color.ORANGE);
		} else {
			this.dc.setForeground(Color.RED);
		}
	}
}
