package guis;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

import main.Client;
import main.Data;

public class LoadView extends JPanel implements Observer {
	private static final long serialVersionUID = -3080373178695889101L;
	
	private final Data data;

	public LoadView(Client client) {
		this.data = client.getData();
		
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.setPreferredSize(new Dimension(ClientGui.FRAME_WIDTH, ClientGui.FRAME_HEIGHT));
		
		this.populateAndAddHeaderPanel();
		this.populateAndAddMiddlePanel();
		this.populateAndAddButtonsPanel();
		
		// Allow this application to make changes to your hard drive (Buttons)
		// Use the same location for all auto saves (Button) Use seperate location (Button)
		// Temp file auto save location. Save temp files? (Button) (Start where you left off, blank to let the program choose)
		// Options auto save location. Save options? (Button) (Save your options and settings, blank to let the program choose)
		// Simulation results auto save location. Auto save simulation results? (Default save location for simulation results, blank to let the program choose, custom saves can be made later)
		// Number of results to save (Maximum number of simulation saves allowed before overwriting the oldest save, any number <= 0 or blank specifies unlimited saves)
	}

	@Override
	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub
	}
	
	private void populateAndAddHeaderPanel() {
		JPanel titleDisplay = new JPanel() {
			private static final long serialVersionUID = 4086737210099466527L;

			@Override
			public void paintComponent(Graphics g) {
				g.setFont(new Font("", Font.BOLD, 60));
				g.drawString("Stabilization control of an inverted pendulum", 250, 60);
			}
		};
		titleDisplay.setPreferredSize(new Dimension(ClientGui.FRAME_WIDTH, 100));
		
		JPanel authorDisplay = new JPanel() {
			private static final long serialVersionUID = -8679134374895555425L;

			@Override
			public void paintComponent(Graphics g) {
				g.setFont(new Font("", Font.BOLD, 50));
				g.drawString("Oliver Kamperis", 700, 50);
			}
		};
		authorDisplay.setPreferredSize(new Dimension(ClientGui.FRAME_WIDTH, 100));
		
		JPanel uniDisplay = new JPanel() {
			private static final long serialVersionUID = 6052801312601696626L;

			@Override
			public void paintComponent(Graphics g) {
				g.setFont(new Font("", Font.BOLD, 50));
				g.drawString("University of Birmingham", 580, 50);
			}
		};
		uniDisplay.setPreferredSize(new Dimension(ClientGui.FRAME_WIDTH, 100));
		
		JPanel schoolDisplay = new JPanel() {
			private static final long serialVersionUID = 8502871992227052668L;

			@Override
			public void paintComponent(Graphics g) {
				g.setFont(new Font("", Font.BOLD, 50));
				g.drawString("Mechanical Engineering", 600, 50);
			}
		};
		schoolDisplay.setPreferredSize(new Dimension(ClientGui.FRAME_WIDTH, 100));
		
		JPanel headerPanel = new JPanel();
		headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
		headerPanel.add(titleDisplay);
		headerPanel.add(authorDisplay);
		headerPanel.add(uniDisplay);
		headerPanel.add(schoolDisplay);
		headerPanel.setPreferredSize(new Dimension(ClientGui.FRAME_WIDTH, 400));
		
		this.add(headerPanel);
	}
	
	private void populateAndAddMiddlePanel() {
		JPanel middlePanel = new JPanel();
		middlePanel.setLayout(new GridLayout(1, 2));
		// TODO
		middlePanel.setPreferredSize(new Dimension(ClientGui.FRAME_WIDTH, 400));
		
		this.add(middlePanel);
	}
	
	private void populateAndAddButtonsPanel() {
		JButton exitButton = new JButton("Exit");
		exitButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					System.exit(0);
				}
			});
		exitButton.setPreferredSize(new Dimension(ClientGui.FRAME_WIDTH / 2, 200));
		
		JButton continueButton = new JButton("Continue");
		continueButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					data.setDesiredViewState(ViewState.SIMULATION_VIEW);
				}
			});
		continueButton.setPreferredSize(new Dimension(ClientGui.FRAME_WIDTH / 2, 200));
		
		JPanel buttonsPanel = new JPanel();
		buttonsPanel.setLayout(new GridLayout(1, 2));
		buttonsPanel.add(exitButton);
		buttonsPanel.add(continueButton);
		buttonsPanel.setPreferredSize(new Dimension(ClientGui.FRAME_WIDTH, 200));
		
		this.add(buttonsPanel);
	}
}
