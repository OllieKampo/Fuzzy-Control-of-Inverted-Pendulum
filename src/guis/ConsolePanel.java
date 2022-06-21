package guis;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;

import main.Client;
import main.Data;

public class ConsolePanel extends JPanel implements Observer {
	private static final long serialVersionUID = 3924144433241172527L;
	
	public static final int CONSOLE_WIDTH = 1600;
	public static final int CONSOLE_HEIGHT = 200;
	
	private final Client client;
	private final Data data;
	
	private final JTabbedPane consoleTabs;
	private final JList<String> outputPane;
	private final JList<String> simulationDataPane;
	private final JList<String> commandsPane;
	private final JTextField commandField;
	private int currentCommandHistoryIndex;
	private String tempCommand;

	public ConsolePanel(Client client) {
		this.client = client;
		this.data = client.getData();
		
		this.consoleTabs = new JTabbedPane();
		this.outputPane = new JList<>();
		this.simulationDataPane = new JList<>();
		this.commandsPane = new JList<>();
		this.commandField = new JTextField();
		this.currentCommandHistoryIndex = -1;
		
		outputPane.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		outputPane.setLayoutOrientation(JList.VERTICAL);
		outputPane.setVisibleRowCount(-1);
		JScrollPane outputScrollPane = new JScrollPane(outputPane);
		outputScrollPane.setName("Logs");
		
		simulationDataPane.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		simulationDataPane.setLayoutOrientation(JList.VERTICAL);
		simulationDataPane.setVisibleRowCount(-1);
		JScrollPane simulationDataScrollPane = new JScrollPane(simulationDataPane);
		simulationDataScrollPane.setName("Simulation output");
		
		commandsPane.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		commandsPane.setLayoutOrientation(JList.VERTICAL);
		commandsPane.setVisibleRowCount(-1);
		JScrollPane commandsScrollPane = new JScrollPane(commandsPane);
		commandsScrollPane.setName("Command history");
		
		consoleTabs.addTab("Logs", outputScrollPane);
		consoleTabs.addTab("Simulation output", simulationDataScrollPane);
		consoleTabs.addTab("Command history", commandsScrollPane);
		consoleTabs.setPreferredSize(new Dimension(1200, 170));
		
		commandField.setEditable(true);
		commandField.addActionListener(new CommandHandler());
		commandField.addKeyListener(new UpKeyListener());
		commandField.addKeyListener(new DownKeyListener());
		commandField.setText("Type commands here to execute, type /help for a command list, press arrow key up to select last command");
		commandField.setPreferredSize(new Dimension(1100, 30));
		
		JButton enterButton = new JButton();
		enterButton.addActionListener(new CommandHandler());
		enterButton.setText("Enter");
		enterButton.setPreferredSize(new Dimension(100, 30));
		
		JPanel commandPanel = new JPanel();
		commandPanel.setLayout(new BoxLayout(commandPanel, BoxLayout.X_AXIS));
		commandPanel.add(enterButton);
		commandPanel.add(commandField);
		commandPanel.setPreferredSize(new Dimension(1200, 30));
		
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.add(consoleTabs);
		this.add(commandPanel);
		this.setPreferredSize(new Dimension(CONSOLE_WIDTH, CONSOLE_HEIGHT));
	}

	@Override
	public void update(Observable o, Object arg) {
		outputPane.setListData(data.getLogs().toArray(new String[]{}));
		simulationDataPane.setListData(data.getSimulationOutputLogs().toArray(new String[]{}));
		commandsPane.setListData(data.getCommandHistory().toArray(new String[]{}));
	}
	
	private class CommandHandler implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (commandField.getText().equals("Type commands here to execute, type /help for a command list, press arrow key up to select last command")) {
				commandField.setText("");
			}
			if (commandField.getText() != null && !commandField.getText().equals("")) {
				processCommand(commandField.getText());
				data.addCommandToHistory(commandField.getText());
				currentCommandHistoryIndex = -1;
				tempCommand = "";
				commandField.setText("");
			}
		}
	}
	
	private class UpKeyListener implements KeyListener {
		@Override
		public void keyPressed(KeyEvent arg0) {
			if (arg0.getKeyCode() == KeyEvent.VK_UP) {
				if (currentCommandHistoryIndex < data.getCommandHistorySize() - 1) {
					currentCommandHistoryIndex++;
					if (currentCommandHistoryIndex == 0) {
						tempCommand = commandField.getText();
					}
					commandField.setText(data.getCommandFromHistory(currentCommandHistoryIndex));
				}
			}
		}
		@Override
		public void keyReleased(KeyEvent arg0) { }
		@Override
		public void keyTyped(KeyEvent arg0) { }
	}
	
	private class DownKeyListener implements KeyListener {
		@Override
		public void keyPressed(KeyEvent arg0) {
			if (arg0.getKeyCode() == KeyEvent.VK_DOWN) {
				if (currentCommandHistoryIndex >= 0) {
					currentCommandHistoryIndex--;
					if (currentCommandHistoryIndex == -1) {
						commandField.setText(tempCommand);
					} else {
						commandField.setText(data.getCommandFromHistory(currentCommandHistoryIndex));
					}
				}
			}
		}
		@Override
		public void keyReleased(KeyEvent arg0) { }
		@Override
		public void keyTyped(KeyEvent arg0) { }
	}
	
	private void processCommand(String command) {
		try {
			if (command.contains("reset to:")) {
				String[] a = command.split(":");
				String[] b = a[1].split("=");
				double cp = Double.valueOf(b[1].split(",")[0]);
				double cv = Double.valueOf(b[2].split(",")[0]);
				double pa = Math.toRadians(Double.valueOf(b[3].split(",")[0]));
				double pv = Double.valueOf(b[4]);
				this.data.setStartingCartPosition(cp);
				this.data.setStartingVelocityOfCart(cv);
				this.data.setStartingPendulumAngle(pa);
				this.data.setStartingAngularVelocityOfPendulum(pv);
				this.client.getFuzzySystem().reset();
				this.client.getPIDSystem().reset();
				return;
			} else if (command.contains("stt:")) {
				this.data.setTargetSimulationTickRate(Double.valueOf(command.split(": ")[1]));
				this.data.updateAll();
				return;
			} else if (command.contains("ctt:")) {
				this.data.setTargetControllerTickRate(Double.valueOf(command.split(": ")[1]));
				this.data.updateAll();
				return;
			} else if (command.contains("tf:")) {
				this.data.setTargetFrameRate(Double.valueOf(command.split(": ")[1]));
				this.data.updateAll();
				return;
			} else if (command.contains("rts:")) {
				this.data.setSimulationRunTimeScale(Double.valueOf(command.split(": ")[1]));
				this.data.updateAll();
				return;
			} else if (command.contains("ts:")) {
				this.data.setTargetSamplingRate(Double.valueOf(command.split(": ")[1]));
				this.data.updateAll();
				return;
			}
		} catch (Exception e) {
			this.data.addLog("Command: " + command + " Not recognised");
			return;
		}
		switch (command) {
		case "/help":
			this.data.addLog("Available commands:");
			this.data.addLog(" - start");
			this.data.addLog(" - stop");
			this.data.addLog(" - pause");
			this.data.addLog(" - reset default");
			this.data.addLog(" - reset random");
			this.data.addLog(" - reset start");
			this.data.addLog(" - reset to: cP= \"value\", cV= \"value\", pA= \"value\", pV= \"value\"");
			this.data.addLog(" - stt: \"value\"");
			this.data.addLog(" - ctt: \"value\"");
			this.data.addLog(" - tf: \"value\"");
			this.data.addLog(" - ts: \"value\"");
			this.data.addLog(" - rts: \"value\"");
			this.data.addLog(" - clear logs");
			this.data.addLog(" - clear output");
			this.data.addLog(" - clear history");
			this.data.addLog(" - clear all");
			return;
		case "get values":
			this.data.addLog("PID: " + this.client.getPIDSystem().getResults());
			this.data.addLog("Fuzzy: " + this.client.getFuzzySystem().getResults());
			return;
		case "start":
			this.client.getFuzzySystem().start();
			this.client.getPIDSystem().start();
			return;
		case "stop":
			this.client.getFuzzySystem().stop();
			this.client.getPIDSystem().stop();
			return;
		case "pause":
			this.client.getFuzzySystem().pause();
			this.client.getPIDSystem().pause();
			return;
		case "reset default":
			this.data.setCartAndPendulumStartDataToDefault();
			this.client.getFuzzySystem().reset();
			this.client.getPIDSystem().reset();
			return;
		case "reset random":
			this.data.randomizeCartAndPendulumStartData();
			this.client.getFuzzySystem().reset();
			this.client.getPIDSystem().reset();
			return;
		case "reset start":
			this.client.getFuzzySystem().reset();
			this.client.getPIDSystem().reset();
			return;
		case "clear logs":
			this.data.getLogs().clear();
			this.data.updateAll();
			return;
		case "clear output":
			this.data.getSimulationOutputLogs().clear();
			this.data.updateAll();
			return;
		case "clear history":
			this.data.getCommandHistory().clear();
			this.data.updateAll();
			return;
		case "clear all":
			this.data.getLogs().clear();
			this.data.getSimulationOutputLogs().clear();
			this.data.getCommandHistory().clear();
			this.data.updateAll();
			return;
		default:
			this.data.addLog("Command: " + command + " Not recognised");
		}
	}
}
