package guis;

import java.awt.Color;
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
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import main.CartAndPendulumSystem;
import main.Client;
import main.Data;
import main.FuzzyCartAndPendulumSystem;
import main.PIDCartAndPendulumSystem;

public class SimulationView extends JPanel implements Observer {
	private static final long serialVersionUID = 2425381194575510133L;
	
	private final Data data;
	private final ConsolePanel console;
	private final JTabbedPane simulationTabs;
	private final RatesPanel fuzzyRates;
	private final RatesPanel pidRates;
	private final FuzzyCartAndPendulumSystem fuzzySystem;
	private final FunctionActivationDisplays fuzzyFunctionActivationDisplays;
	private final PIDCartAndPendulumSystem pidSystem;
	private final PIDForceProportionsDisplay pidForcesDisplay;
	private final PositionGraph fuzzyPositionGraph;
	private final PositionGraph pidPositionGraph;
	private final ResponseGraph fuzzyResponseGraph;
	private final ResponseGraph pidResponseGraph;
	
	private volatile double actualFrameRate;
	private volatile double targetFrameRate;
	private final Thread displayUpdater;
	
	public SimulationView(Client client) {
		this.data = client.getData();
		this.console = new ConsolePanel(client);
		this.simulationTabs = new JTabbedPane();
		this.fuzzyRates = new RatesPanel();
		this.pidRates = new RatesPanel();
		this.fuzzySystem = client.getFuzzySystem();
		this.fuzzyFunctionActivationDisplays = new FunctionActivationDisplays(FuzzyCartAndPendulumSystem.DEFAULT_FUZZY_VARIABLES, FuzzyCartAndPendulumSystem.DEFAULT_MEMBER_FUNCTIONS);
		this.pidSystem = client.getPIDSystem();
		this.pidForcesDisplay = new PIDForceProportionsDisplay();
		this.fuzzyPositionGraph = new PositionGraph();
		this.pidPositionGraph = new PositionGraph();
		this.fuzzyResponseGraph = new ResponseGraph("Pendulum angle (rads)", 0.0, Math.PI);
		this.pidResponseGraph = new ResponseGraph("Pendulum angle (rads)", 0.0, Math.PI);
		
		this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		this.populateAndAddLeftPanel();
		this.populateAndAddRightPanel();
		this.setPreferredSize(new Dimension(ClientGui.FRAME_WIDTH, ClientGui.FRAME_HEIGHT));
		
		this.actualFrameRate = 0.0;
		this.targetFrameRate = data.getTargetFrameRate();
		this.displayUpdater = new Thread() {
			@Override
			public void run() {
				int frames = 0;
				long nanoTimeLast = System.nanoTime();
				while (true) {
					fuzzySystem.repaint();
					fuzzyPositionGraph.updateDisplay(fuzzySystem.getCartPosition(), CartAndPendulumSystem.CART_LENGTH / 2, CartAndPendulumSystem.TRACK_LENGTH - (CartAndPendulumSystem.CART_LENGTH / 2), fuzzySystem.getPendulumAngle(), 0.0, Math.PI);
					fuzzyResponseGraph.addPointToEnd(fuzzySystem.getPendulumAngle());
					fuzzyFunctionActivationDisplays.updateVariables(fuzzySystem.getPendulumAngle(), fuzzySystem.getPendulumAngularVelocity(), fuzzySystem.getCartPosition(), fuzzySystem.getCartVelocity());
					pidSystem.repaint();
					pidPositionGraph.updateDisplay(pidSystem.getCartPosition(), CartAndPendulumSystem.CART_LENGTH / 2, CartAndPendulumSystem.TRACK_LENGTH - (CartAndPendulumSystem.CART_LENGTH / 2), pidSystem.getPendulumAngle(), 0.0, Math.PI);
					pidResponseGraph.addPointToEnd(pidSystem.getPendulumAngle());
					pidForcesDisplay.updateValues(pidSystem);
					if (frames < targetFrameRate) {
						frames++;
					} else {
						frames = 0;
						actualFrameRate = targetFrameRate / ((System.nanoTime() - nanoTimeLast) / 1000000000.0);
						nanoTimeLast = System.nanoTime();
						fuzzyRates.updateActuals(fuzzySystem.getActualSimulationTickRate(), fuzzySystem.getActualControllerTickRate(), actualFrameRate, data.getActualSamplingRate());
						pidRates.updateActuals(pidSystem.getActualSimulationTickRate(), pidSystem.getActualControllerTickRate(), actualFrameRate, data.getActualSamplingRate());
						fuzzyFunctionActivationDisplays.updateMemberFunctions(fuzzySystem.getCopyOfCurrentMemberFunctions());
					}
					long end = System.nanoTime() + (long)(CartAndPendulumSystem.NANO_UNITS_PER_STD_UNIT / targetFrameRate);
					while (System.nanoTime() < end) {
					     Thread.yield();
					}
				}
			}
		};
		this.displayUpdater.start();
	}
	
	@Override
	public void update(Observable o, Object arg) {
		this.fuzzyRates.updateTargets(this.data.getTargetSimulationTickRate(), this.data.getTargetControllerTickRate(), this.data.getTargetFrameRate(), this.data.getTargetSamplingRate());
		this.fuzzyRates.updateTimeScaleFactor(data.getSimulationRunTimeScale());
		this.pidRates.updateTargets(this.data.getTargetSimulationTickRate(), this.data.getTargetControllerTickRate(), this.data.getTargetFrameRate(), this.data.getTargetSamplingRate());
		this.pidRates.updateTimeScaleFactor(data.getSimulationRunTimeScale());
		this.targetFrameRate = data.getTargetFrameRate();
		this.console.update(o, arg);
	}
	
	private void populateAndAddLeftPanel() {
		JButton startButton = new JButton("Start");
		startButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if (!fuzzySystem.isPaused()) {
						data.purgeResults();
					}
					fuzzySystem.start();
					pidSystem.start();
					data.setGatheringResults(true);
				}
			});
		startButton.setPreferredSize(new Dimension(200, 84));
		
		JButton pauseButton = new JButton("Pause");
		pauseButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					data.setGatheringResults(false);
					fuzzySystem.pause();
					pidSystem.pause();
				}
			});
		pauseButton.setPreferredSize(new Dimension(200, 84));
		
		JButton stopButton = new JButton("Stop");
		stopButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					data.setGatheringResults(false);
					fuzzySystem.stop();
					pidSystem.stop();
				}
			});
		stopButton.setPreferredSize(new Dimension(200, 84));
		
		JButton resetStartButton = new JButton("Reset to start");
		resetStartButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					data.purgeResults();
					fuzzySystem.reset();
					pidSystem.reset();
				}
			});
		resetStartButton.setPreferredSize(new Dimension(200, 84));
		
		JButton resetDefaultButton = new JButton("Reset to center");
		resetDefaultButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					data.purgeResults();
					data.setCartAndPendulumStartDataToDefault();
					fuzzySystem.reset();
					pidSystem.reset();
				}
			});
		resetDefaultButton.setPreferredSize(new Dimension(200, 84));
		
		JButton resetRandomButton = new JButton("Reset to random");
		resetRandomButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					data.purgeResults();
					data.randomizeCartAndPendulumStartData();
					fuzzySystem.reset();
					pidSystem.reset();
				}
			});
		resetRandomButton.setPreferredSize(new Dimension(200, 84));
		
		JTextField cartPositionField = new JTextField(Double.toString((CartAndPendulumSystem.TRACK_LENGTH / 2) / CartAndPendulumSystem.NANO_UNITS_PER_MILLI_UNIT));
		cartPositionField.getDocument().addDocumentListener(new DocumentListener() {
		    @Override
		    public void insertUpdate(DocumentEvent e) {
		    	try {
		    		cartPositionField.setForeground(Color.BLACK);
		    		data.addLog("Starting cart position set to: " + cartPositionField.getText() + ", reset the simulation to begin as specified");
		    	} catch (Exception ex) {
		    		cartPositionField.setForeground(Color.RED);
		    		data.addLog("Starting cart position value= \"" + cartPositionField.getText() + "\" is not a number, the default value= \"" + Double.toString((CartAndPendulumSystem.TRACK_LENGTH / 2) / CartAndPendulumSystem.NANO_UNITS_PER_MILLI_UNIT) + "\" has been applied");
		    	}
		    }
		    @Override
		    public void removeUpdate(DocumentEvent e) {
		    	try {
		    		cartPositionField.setForeground(Color.BLACK);
		    		data.addLog("Starting cart position set to: " + cartPositionField.getText() + ", reset the simulation to begin as specified");
		    	} catch (Exception ex) {
		    		cartPositionField.setForeground(Color.RED);
		    		data.addLog("Starting cart position value= \"" + cartPositionField.getText() + "\" is not a number, the default value= \"" + Double.toString((CartAndPendulumSystem.TRACK_LENGTH / 2) / CartAndPendulumSystem.NANO_UNITS_PER_MILLI_UNIT) + "\" has been applied");
		    	}
		    }
		    @Override
		    public void changedUpdate(DocumentEvent e) { }
		});
		cartPositionField.setPreferredSize(new Dimension(75, 42));
		
		JTextField pendulumAngleField = new JTextField(Double.toString(Math.toDegrees(Math.PI / 2)));
		cartPositionField.getDocument().addDocumentListener(new DocumentListener() {
		    @Override
		    public void insertUpdate(DocumentEvent e) {
		    	try {
		    		pendulumAngleField.setForeground(Color.BLACK);
		    	} catch (Exception ex) {
		    		pendulumAngleField.setForeground(Color.RED);
		    		data.addLog("Starting pendulum angle value= \"" + pendulumAngleField.getText() + "\" is not a number, the default value= \"" + Double.toString(Math.toDegrees(Math.PI / 2)) + "\" has been applied");
		    	}
		    }
		    @Override
		    public void removeUpdate(DocumentEvent e) {
		    	try {
		    		pendulumAngleField.setForeground(Color.BLACK);
		    	} catch (Exception ex) {
		    		pendulumAngleField.setForeground(Color.RED);
		    		data.addLog("Starting pendulum angle value= \"" + pendulumAngleField.getText() + "\" is not a number, the default value= \"" + Double.toString(Math.toDegrees(Math.PI / 2)) + "\" has been applied");
		    	}
		    }
		    @Override
		    public void changedUpdate(DocumentEvent e) { }
		});
		pendulumAngleField.setPreferredSize(new Dimension(75, 42));
		
		JPanel cartPositionHeader = new JPanel() {
			private static final long serialVersionUID = 5416988153100679367L;
			@Override
			public void paintComponent(Graphics g) {
				g.setFont(new Font("", Font.BOLD, 15));
				g.drawString("Cart position:", 5, 30);
			}
		};
		cartPositionHeader.setPreferredSize(new Dimension(125, 42));
		
		JPanel pendulumAngleHeader = new JPanel() {
			private static final long serialVersionUID = 2025052640620560150L;
			@Override
			public void paintComponent(Graphics g) {
				g.setFont(new Font("", Font.BOLD, 15));
				g.drawString("Pendulum angle:", 5, 30);
			}
		};
		pendulumAngleHeader.setPreferredSize(new Dimension(125, 42));
		
		JPanel cartPositionSubPanel = new JPanel();
		cartPositionSubPanel.setLayout(new BoxLayout(cartPositionSubPanel, BoxLayout.X_AXIS));
		cartPositionSubPanel.add(cartPositionHeader);
		cartPositionSubPanel.add(cartPositionField);
		cartPositionSubPanel.setPreferredSize(new Dimension(200, 42));
		
		JPanel pendulumAngleSubPanel = new JPanel();
		pendulumAngleSubPanel.setLayout(new BoxLayout(pendulumAngleSubPanel, BoxLayout.X_AXIS));
		pendulumAngleSubPanel.add(pendulumAngleHeader);
		pendulumAngleSubPanel.add(pendulumAngleField);
		pendulumAngleSubPanel.setPreferredSize(new Dimension(200, 42));
		
		JPanel resetToPanel = new JPanel();
		resetToPanel.setLayout(new GridLayout(2, 1));
		resetToPanel.add(cartPositionSubPanel);
		resetToPanel.add(pendulumAngleSubPanel);
		resetToPanel.setPreferredSize(new Dimension(200, 84));
		
		JButton resetToButton = new JButton("Reset to:");
		resetToButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					data.purgeResults();
			    	try {
						data.setStartingCartPosition(Double.valueOf(cartPositionField.getText()) * CartAndPendulumSystem.NANO_UNITS_PER_MILLI_UNIT);
			    	} catch (Exception ex) {
			    		data.setStartingCartPosition(CartAndPendulumSystem.TRACK_LENGTH / 2);
			    	}
			    	try {
						data.setStartingPendulumAngle(Math.toRadians(Double.valueOf(pendulumAngleField.getText())));
			    	} catch (Exception ex) {
						data.setStartingPendulumAngle(Math.PI / 2);
			    	}
					data.setStartingForce(0.0);
					data.setStartingVelocityOfCart(0.0);
					data.setStartingAccelerationOfCart(0.0);
					data.setStartingAngularVelocityOfPendulum(0.0);
					data.setStartingAngularAccelerationOfPendulum(0.0);
					fuzzySystem.reset();
					pidSystem.reset();
				}
			});
		resetToButton.setPreferredSize(new Dimension(200, 84));
		
		JButton parameterViewButton = new JButton("Control parameters");
		parameterViewButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					data.setDesiredViewState(ViewState.PARAMETER_VIEW);
				}
			});
		parameterViewButton.setPreferredSize(new Dimension(200, 84));
		
		JButton optionsViewButton = new JButton("Options");
		optionsViewButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					data.setDesiredViewState(ViewState.OPTIONS_VIEW);
				}
			});
		optionsViewButton.setPreferredSize(new Dimension(200, 84));
		
		JButton resultsViewButton = new JButton("Results");
		resultsViewButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					data.setDesiredViewState(ViewState.RESULTS_VIEW);
				}
			});
		resultsViewButton.setPreferredSize(new Dimension(200, 84));
		
		JButton homeViewButton = new JButton("Home");
		homeViewButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					data.setDesiredViewState(ViewState.LOAD_VIEW);
				}
			});
		homeViewButton.setPreferredSize(new Dimension(200, 84));
		
		JPanel leftPanel = new JPanel();
		leftPanel.setLayout(new GridLayout(12, 1));
		leftPanel.add(startButton);
		leftPanel.add(pauseButton);
		leftPanel.add(stopButton);
		leftPanel.add(resetStartButton);
		leftPanel.add(resetDefaultButton);
		leftPanel.add(resetRandomButton);
		leftPanel.add(resetToButton);
		leftPanel.add(resetToPanel);
		leftPanel.add(parameterViewButton);
		leftPanel.add(optionsViewButton);
		leftPanel.add(resultsViewButton);
		leftPanel.add(homeViewButton);
		leftPanel.setPreferredSize(new Dimension(200, 1000));
		
		this.add(leftPanel);
	}
	
	private void populateAndAddRightPanel() {
		JPanel fuzzyGraphs = new JPanel();
		fuzzyGraphs.setLayout(new BoxLayout(fuzzyGraphs, BoxLayout.Y_AXIS));
		fuzzyGraphs.add(fuzzyPositionGraph);
		fuzzyGraphs.add(fuzzyResponseGraph);
		fuzzyGraphs.setPreferredSize(new Dimension(400, 800));
		
		JPanel pidGraphs = new JPanel();
		pidGraphs.setLayout(new BoxLayout(pidGraphs, BoxLayout.Y_AXIS));
		pidGraphs.add(pidPositionGraph);
		pidGraphs.add(pidResponseGraph);
		pidGraphs.setPreferredSize(new Dimension(400, 800));
		
		JPanel fuzzySubPanel = new JPanel();
		fuzzySubPanel.setLayout(new BoxLayout(fuzzySubPanel, BoxLayout.Y_AXIS));
		fuzzySubPanel.add(fuzzyRates);
		fuzzySubPanel.add(fuzzySystem);
		fuzzySubPanel.add(fuzzyFunctionActivationDisplays);
		fuzzySubPanel.setPreferredSize(new Dimension(1200, 800));
		
		JPanel fuzzyPanel =  new JPanel();
		fuzzyPanel.setLayout(new BoxLayout(fuzzyPanel, BoxLayout.X_AXIS));
		fuzzyPanel.add(fuzzySubPanel);
		fuzzyPanel.add(fuzzyGraphs);
		fuzzyPanel.setPreferredSize(new Dimension(1600, 800));
		
		JPanel pidSubPanel = new JPanel();
		pidSubPanel.setLayout(new BoxLayout(pidSubPanel, BoxLayout.Y_AXIS));
		pidSubPanel.add(pidRates);
		pidSubPanel.add(pidSystem);
		pidSubPanel.add(pidForcesDisplay);
		pidSubPanel.setPreferredSize(new Dimension(1200, 800));
		
		JPanel pidPanel = new JPanel();
		pidPanel.setLayout(new BoxLayout(pidPanel, BoxLayout.X_AXIS));
		pidPanel.add(pidSubPanel);
		pidPanel.add(pidGraphs);
		pidPanel.setPreferredSize(new Dimension(1600, 800));
		
		simulationTabs.addTab("Fuzzy", fuzzyPanel);
		simulationTabs.addTab("PID", pidPanel);
		simulationTabs.setPreferredSize(new Dimension(1600, 800));
		
		JPanel rightPanel = new JPanel();
		rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
		rightPanel.add(simulationTabs);
		rightPanel.add(console);
		rightPanel.setPreferredSize(new Dimension(ConsolePanel.CONSOLE_WIDTH, ClientGui.FRAME_HEIGHT - ConsolePanel.CONSOLE_HEIGHT));
		
		this.add(rightPanel);
	}
}
