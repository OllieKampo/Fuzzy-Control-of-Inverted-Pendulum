package guis;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import main.CartAndPendulumSystem;
import main.Client;
import main.Data;
import main.ResultElement;

public class ResultsView extends JPanel implements Observer {
	private static final long serialVersionUID = 4373883906079611829L;
	
	private final Data data;
	
	private ResultElement[] fuzzyResults;
	private ResultElement[] pidResults;
	private final JTabbedPane viewTabs;
	private final JSlider frameSlider;
	private volatile boolean userSliding;
	
	private volatile double fuzzyVelMax;
	private volatile double fuzzyVelMin;
	private volatile double fuzzyAngVelMax;
	private volatile double fuzzyAngVelMin;
	private volatile double pidVelMax;
	private volatile double pidVelMin;
	private volatile double pidAngVelMax;
	private volatile double pidAngVelMin;
	
	private final ResponseGraph fuzzyPositionResponseGraph;
	private final ResponseGraph fuzzyVelocityResponseGraph;
	private final ResponseGraph fuzzyAngleResponseGraph;
	private final ResponseGraph fuzzyAngVelocityResponseGraph;
	private final ResponseGraph pidPositionResponseGraph;
	private final ResponseGraph pidVelocityResponseGraph;
	private final ResponseGraph pidAngleResponseGraph;
	private final ResponseGraph pidAngVelocityResponseGraph;
	
	private volatile int resultIndex;
	private final AtomicBoolean run;
	private final AtomicBoolean rewind;
	private final Thread displayUpdater;

	public ResultsView(Client client) {
		this.data = client.getData();
		
		this.fuzzyResults = new ResultElement[0];
		this.pidResults = new ResultElement[0];
		this.viewTabs = new JTabbedPane();
		this.frameSlider = new JSlider(JSlider.HORIZONTAL, 0, 1_000_000, 0);
		this.userSliding = false;
		
		this.fuzzyVelMax = 0.0;
		this.fuzzyVelMin = 0.0;
		this.fuzzyAngVelMax = 0.0;
		this.fuzzyAngVelMin = 0.0;
		this.pidVelMax = 0.0;
		this.pidVelMin = 0.0;
		this.pidAngVelMax = 0.0;
		this.pidAngVelMin = 0.0;
		
		this.fuzzyPositionResponseGraph = new ResponseGraph("Cart position (mm)", (CartAndPendulumSystem.CART_LENGTH / 2) / CartAndPendulumSystem.NANO_UNITS_PER_MILLI_UNIT, (CartAndPendulumSystem.TRACK_LENGTH - (CartAndPendulumSystem.CART_LENGTH / 2)) / CartAndPendulumSystem.NANO_UNITS_PER_MILLI_UNIT);
		this.fuzzyVelocityResponseGraph = new ResponseGraph("Cart velocity (mm/s)", 0.0, 0.0);
		this.fuzzyAngleResponseGraph = new ResponseGraph("Pendulum angle (rads)", 0.0, Math.PI);
		this.fuzzyAngVelocityResponseGraph = new ResponseGraph("Pendulum angular velocity (rads/s)", 0.0, 0.0);
		this.pidPositionResponseGraph = new ResponseGraph("Cart position (mm)", (CartAndPendulumSystem.CART_LENGTH / 2) / CartAndPendulumSystem.NANO_UNITS_PER_MILLI_UNIT, (CartAndPendulumSystem.TRACK_LENGTH - (CartAndPendulumSystem.CART_LENGTH / 2)) / CartAndPendulumSystem.NANO_UNITS_PER_MILLI_UNIT);
		this.pidVelocityResponseGraph = new ResponseGraph("Cart velocity (mm/s)", 0.0, 0.0);
		this.pidAngleResponseGraph = new ResponseGraph("Pendulum angle (rads)", 0.0, Math.PI);
		this.pidAngVelocityResponseGraph = new ResponseGraph("Pendulum angular velocity (rads/s)", 0.0, 0.0);
		
		this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		this.populateAndAddLeftPanel();
		this.populateAndAddRightPanel();
		this.setPreferredSize(new Dimension(ClientGui.FRAME_WIDTH, ClientGui.FRAME_HEIGHT));
		
		this.resultIndex = 0;
		this.run = new AtomicBoolean(false);
		this.rewind = new AtomicBoolean(false);
		this.displayUpdater = new Thread() {
			@Override
			public void run() {
				while (true) {
					if (run.get() && !userSliding) {
						if (rewind.get()) {
							if (resultIndex - 300 > 0) {
								resultIndex--;
								fuzzyPositionResponseGraph.addPointToStart(fuzzyResults[resultIndex - 300].cartPosition);
								fuzzyVelocityResponseGraph.addPointToStart(fuzzyResults[resultIndex - 300].cartVelocity);
								fuzzyAngleResponseGraph.addPointToStart(fuzzyResults[resultIndex - 300].pendulumAngle);
								fuzzyAngVelocityResponseGraph.addPointToStart(fuzzyResults[resultIndex - 300].pendulumVelocity);
								pidPositionResponseGraph.addPointToStart(pidResults[resultIndex - 300].cartPosition);
								pidVelocityResponseGraph.addPointToStart(pidResults[resultIndex - 300].cartVelocity);
								pidAngleResponseGraph.addPointToStart(pidResults[resultIndex - 300].pendulumAngle);
								pidAngVelocityResponseGraph.addPointToStart(pidResults[resultIndex - 300].pendulumVelocity);
							} else if (resultIndex > 0) {
								resultIndex--;
								fuzzyPositionResponseGraph.removePointFromEnd();
								fuzzyVelocityResponseGraph.removePointFromEnd();
								fuzzyAngleResponseGraph.removePointFromEnd();
								fuzzyAngVelocityResponseGraph.removePointFromEnd();
								pidPositionResponseGraph.removePointFromEnd();
								pidVelocityResponseGraph.removePointFromEnd();
								pidAngleResponseGraph.removePointFromEnd();
								pidAngVelocityResponseGraph.removePointFromEnd();
							} else {
								try {
									Thread.sleep(1000);
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
							}
						} else {
							if (resultIndex < fuzzyResults.length && resultIndex < pidResults.length) {
								fuzzyPositionResponseGraph.addPointToEnd(fuzzyResults[resultIndex].cartPosition);
								fuzzyVelocityResponseGraph.addPointToEnd(fuzzyResults[resultIndex].cartVelocity);
								fuzzyAngleResponseGraph.addPointToEnd(fuzzyResults[resultIndex].pendulumAngle);
								fuzzyAngVelocityResponseGraph.addPointToEnd(fuzzyResults[resultIndex].pendulumVelocity);
								pidPositionResponseGraph.addPointToEnd(pidResults[resultIndex].cartPosition);
								pidVelocityResponseGraph.addPointToEnd(pidResults[resultIndex].cartVelocity);
								pidAngleResponseGraph.addPointToEnd(pidResults[resultIndex].pendulumAngle);
								pidAngVelocityResponseGraph.addPointToEnd(pidResults[resultIndex].pendulumVelocity);
								resultIndex++;
							} else {
								try {
									Thread.sleep(1000);
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
							}
						}
						if (resultIndex < fuzzyResults.length - 1 && resultIndex < pidResults.length - 1) {
							long end = System.nanoTime() + (long)(((fuzzyResults[resultIndex + 1].nanoTime + pidResults[resultIndex + 1].nanoTime) / 2) - ((fuzzyResults[resultIndex].nanoTime + pidResults[resultIndex].nanoTime) / 2));
							while (System.nanoTime() < end) {
							     Thread.yield();
							}
						}
						frameSlider.setValue((int)(((double)resultIndex / pidResults.length) * 1_000_000));
					} else {
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
			}
		};
		this.displayUpdater.start();
	}

	@Override
	public void update(Observable o, Object arg) {
		this.fuzzyResults = data.getFuzzyResults();
		this.pidResults = data.getPidResults();
		for (int i = 0; i < fuzzyResults.length; i++) {
			if (fuzzyResults[i].cartVelocity > fuzzyVelMax) {
				fuzzyVelMax = fuzzyResults[i].cartVelocity;
			} else if (fuzzyResults[i].cartVelocity < fuzzyVelMin) {
				fuzzyVelMin = fuzzyResults[i].cartVelocity;
			}
			if (fuzzyResults[i].pendulumVelocity > fuzzyAngVelMax) {
				fuzzyAngVelMax = fuzzyResults[i].pendulumVelocity;
			} else if (fuzzyResults[i].pendulumVelocity < fuzzyAngVelMin) {
				fuzzyAngVelMin = fuzzyResults[i].pendulumVelocity;
			}
		}
		if (Math.abs(fuzzyVelMax) > Math.abs(fuzzyVelMin)) {
			fuzzyVelMin = -fuzzyVelMax;
		} else {
			fuzzyVelMax = -fuzzyVelMin;
		}
		if (Math.abs(fuzzyAngVelMax) > Math.abs(fuzzyAngVelMin)) {
			fuzzyAngVelMin = -fuzzyAngVelMax;
		} else {
			fuzzyAngVelMax = -fuzzyAngVelMin;
		}
		for (int i = 0; i < pidResults.length; i++) {
			if (pidResults[i].cartVelocity > pidVelMax) {
				pidVelMax = pidResults[i].cartVelocity;
			} else if (pidResults[i].cartVelocity < pidVelMin) {
				pidVelMin = pidResults[i].cartVelocity;
			}
			if (pidResults[i].pendulumVelocity > pidAngVelMax) {
				pidAngVelMax = pidResults[i].pendulumVelocity;
			} else if (pidResults[i].pendulumVelocity < pidAngVelMin) {
				pidAngVelMin = pidResults[i].pendulumVelocity;
			}
		}
		if (Math.abs(pidVelMax) > Math.abs(pidVelMin)) {
			pidVelMin = -pidVelMax;
		} else {
			pidVelMax = -pidVelMin;
		}
		if (Math.abs(pidAngVelMax) > Math.abs(pidAngVelMin)) {
			pidAngVelMin = -pidAngVelMax;
		} else {
			pidAngVelMax = -pidAngVelMin;
		}
		fuzzyVelocityResponseGraph.setMinMax(fuzzyVelMin, fuzzyVelMax);
		fuzzyAngVelocityResponseGraph.setMinMax(fuzzyAngVelMin, fuzzyAngVelMax);
		pidVelocityResponseGraph.setMinMax(pidVelMin, pidVelMax);
		pidAngVelocityResponseGraph.setMinMax(pidAngVelMin, pidAngVelMax);
	}
	
	private void populateAndAddRightPanel() {
		JPanel fuzzyHeader = new JPanel() {
			private static final long serialVersionUID = -5713198472968784780L;
			@Override
			public void paintComponent(Graphics g) {
				g.setFont(new Font("", Font.BOLD, 35));
				g.drawString("Fuzzy results:", 10, 40);
			}
		};
		fuzzyHeader.setPreferredSize(new Dimension(1600, 50));
		
		JPanel fuzzyGraphs = new JPanel();
		fuzzyGraphs.setLayout(new GridLayout(1, 4));
		fuzzyGraphs.add(this.fuzzyPositionResponseGraph);
		fuzzyGraphs.add(this.fuzzyVelocityResponseGraph);
		fuzzyGraphs.add(this.fuzzyAngleResponseGraph);
		fuzzyGraphs.add(this.fuzzyAngVelocityResponseGraph);
		fuzzyGraphs.setPreferredSize(new Dimension(1600, 400));
		
		JPanel pidHeader = new JPanel() {
			private static final long serialVersionUID = -5713198472968784780L;
			@Override
			public void paintComponent(Graphics g) {
				g.setFont(new Font("", Font.BOLD, 35));
				g.drawString("PID results:", 10, 40);
			}
		};
		pidHeader.setPreferredSize(new Dimension(1600, 50));
		
		JPanel pidGraphs = new JPanel();
		pidGraphs.setLayout(new GridLayout(1, 4));
		pidGraphs.add(this.pidPositionResponseGraph);
		pidGraphs.add(this.pidVelocityResponseGraph);
		pidGraphs.add(this.pidAngleResponseGraph);
		pidGraphs.add(this.pidAngVelocityResponseGraph);
		pidGraphs.setPreferredSize(new Dimension(1600, 400));
		
		JPanel graphsPanel =  new JPanel();
		graphsPanel.setLayout(new BoxLayout(graphsPanel, BoxLayout.Y_AXIS));
		graphsPanel.add(fuzzyHeader);
		graphsPanel.add(fuzzyGraphs);
		graphsPanel.add(pidHeader);
		graphsPanel.add(pidGraphs);
		graphsPanel.setPreferredSize(new Dimension(1600, 900));
		
		viewTabs.add("Graphs", graphsPanel);
		viewTabs.setPreferredSize(new Dimension(1600, 900));
		
		frameSlider.setMajorTickSpacing(100_000);
		frameSlider.setMinorTickSpacing(10_000);
		frameSlider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent arg0) {
				if (userSliding) {
					int resultIndexOld = resultIndex;
					resultIndex = (int)(((double)frameSlider.getValue() / 1_000_000) * pidResults.length);
					if (resultIndexOld < resultIndex) {
						for (int i = 1; resultIndexOld + i <= resultIndex; i++) {
							fuzzyPositionResponseGraph.addPointToEnd(fuzzyResults[resultIndexOld + i].cartPosition);
							fuzzyVelocityResponseGraph.addPointToEnd(fuzzyResults[resultIndexOld + i].cartVelocity);
							fuzzyAngleResponseGraph.addPointToEnd(fuzzyResults[resultIndexOld + i].pendulumAngle);
							fuzzyAngVelocityResponseGraph.addPointToEnd(fuzzyResults[resultIndexOld + i].pendulumVelocity);
							pidPositionResponseGraph.addPointToEnd(pidResults[resultIndexOld + i].cartPosition);
							pidVelocityResponseGraph.addPointToEnd(pidResults[resultIndexOld + i].cartVelocity);
							pidAngleResponseGraph.addPointToEnd(pidResults[resultIndexOld + i].pendulumAngle);
							pidAngVelocityResponseGraph.addPointToEnd(pidResults[resultIndexOld + i].pendulumVelocity);
						}
					} else {
						for (int i = 1; resultIndexOld - i >= resultIndex; i++) {
							if (resultIndexOld - i - 300 > 0) {
								fuzzyPositionResponseGraph.addPointToStart(fuzzyResults[resultIndexOld - i - 300].cartPosition);
								fuzzyVelocityResponseGraph.addPointToStart(fuzzyResults[resultIndexOld - i - 300].cartVelocity);
								fuzzyAngleResponseGraph.addPointToStart(fuzzyResults[resultIndexOld - i - 300].pendulumAngle);
								fuzzyAngVelocityResponseGraph.addPointToStart(fuzzyResults[resultIndexOld - i - 300].pendulumVelocity);
								pidPositionResponseGraph.addPointToStart(pidResults[resultIndexOld - i - 300].cartPosition);
								pidVelocityResponseGraph.addPointToStart(pidResults[resultIndexOld - i - 300].cartVelocity);
								pidAngleResponseGraph.addPointToStart(pidResults[resultIndexOld - i - 300].pendulumAngle);
								pidAngVelocityResponseGraph.addPointToStart(pidResults[resultIndexOld - i - 300].pendulumVelocity);
							} else if (resultIndexOld - i > 0) {
								fuzzyPositionResponseGraph.removePointFromEnd();
								fuzzyVelocityResponseGraph.removePointFromEnd();
								fuzzyAngleResponseGraph.removePointFromEnd();
								fuzzyAngVelocityResponseGraph.removePointFromEnd();
								pidPositionResponseGraph.removePointFromEnd();
								pidVelocityResponseGraph.removePointFromEnd();
								pidAngleResponseGraph.removePointFromEnd();
								pidAngVelocityResponseGraph.removePointFromEnd();
							}
						}
					}
				}
			}
		});
		frameSlider.addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent arg0) { }
			@Override
			public void mouseEntered(MouseEvent arg0) { }
			@Override
			public void mouseExited(MouseEvent arg0) { }
			
			@Override
			public void mousePressed(MouseEvent arg0) {
				userSliding = true;
			}
			
			@Override
			public void mouseReleased(MouseEvent arg0) {
				userSliding = false;
			}
		});
		frameSlider.setPaintTicks(true);
		frameSlider.setPaintLabels(true);
		frameSlider.setPreferredSize(new Dimension(1600, 100));
		
		JPanel rightPanel = new JPanel();
		rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
		rightPanel.add(viewTabs);
		rightPanel.add(frameSlider);
		rightPanel.setPreferredSize(new Dimension(1600, ClientGui.FRAME_HEIGHT));
		
		this.add(rightPanel);
	}
	
	private void populateAndAddLeftPanel() {
		JButton playButton = new JButton("Play");
		playButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					run.set(true);
					rewind.set(false);
				}
			});
		playButton.setPreferredSize(new Dimension(200, 84));
		
		JButton rewindButton = new JButton("Rewind");
		rewindButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					run.set(true);
					rewind.set(true);
				}
			});
		rewindButton.setPreferredSize(new Dimension(200, 84));
		
		JButton pauseButton = new JButton("Pause");
		pauseButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					run.set(false);
				}
			});
		pauseButton.setPreferredSize(new Dimension(200, 84));
		
		JButton resetStartButton = new JButton("Reset");
		resetStartButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					resultIndex = 0;
					frameSlider.setValue(0);
					fuzzyPositionResponseGraph.clear();
					fuzzyVelocityResponseGraph.clear();
					fuzzyAngleResponseGraph.clear();
					fuzzyAngVelocityResponseGraph.clear();
					pidPositionResponseGraph.clear();
					pidVelocityResponseGraph.clear();
					pidAngleResponseGraph.clear();
					pidAngVelocityResponseGraph.clear();
				}
			});
		resetStartButton.setPreferredSize(new Dimension(200, 84));
		
		JButton simulationViewButton = new JButton("To simulation");
		simulationViewButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					data.setDesiredViewState(ViewState.SIMULATION_VIEW);
				}
			});
		simulationViewButton.setPreferredSize(new Dimension(200, 100));
		
		JButton homeViewButton = new JButton("To home");
		homeViewButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					data.setDesiredViewState(ViewState.LOAD_VIEW);
				}
			});
		homeViewButton.setPreferredSize(new Dimension(200, 100));
		
		JPanel leftPanel = new JPanel();
		leftPanel.setLayout(new GridLayout(6, 1));
		leftPanel.add(playButton);
		leftPanel.add(pauseButton);
		leftPanel.add(rewindButton);
		leftPanel.add(resetStartButton);
		leftPanel.add(simulationViewButton);
		leftPanel.add(homeViewButton);
		leftPanel.setPreferredSize(new Dimension(200, ClientGui.FRAME_HEIGHT));
		
		this.add(leftPanel);
	}
}
