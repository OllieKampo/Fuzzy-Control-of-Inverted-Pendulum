package guis;

import java.util.Observable;
import java.util.Observer;

import javax.swing.JFrame;

import main.Client;
import main.Data;

public class ClientGui extends JFrame implements Observer {
	private static final long serialVersionUID = -5960416882838553652L;
	
	public static final int FRAME_WIDTH = 1800;
	public static final int FRAME_HEIGHT = 1000;
	
	private final Client client;
	private final Data data;
	private final LoadView loadView;
	private final ParameterView parameterView;
	private final OptionsView optionsView;
	private final SimulationView simulationView;
	private final ResultsView resultsView;
	
	private ViewState currentViewState;

	public ClientGui(Client client) {
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.setSize(FRAME_WIDTH, FRAME_HEIGHT);
		this.setTitle("Inverted pendulum control");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
		this.setLocationRelativeTo(null);
		
		this.client = client;
		this.data = client.getData();
		this.loadView = new LoadView(client);
		this.parameterView = new ParameterView(client);
		this.optionsView = new OptionsView(client);
		this.simulationView = new SimulationView(client);
		this.resultsView = new ResultsView(client);
		
		this.currentViewState = ViewState.LOAD_VIEW;
		this.getContentPane().add(loadView);
		this.data.addObserver(loadView);
		this.data.addObserver(client.getFuzzySystem());
		this.data.addObserver(client.getPIDSystem());
		this.data.addObserver(this);
		
		this.setVisible(true);
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		if (this.data.getDesiredViewState().equals(ViewState.LOAD_VIEW)) {
			if (!this.currentViewState.equals(ViewState.LOAD_VIEW)) {
				this.data.deleteObservers();
				this.data.addObserver(loadView);
				this.data.addObserver(client.getFuzzySystem());
				this.data.addObserver(client.getPIDSystem());
				this.data.addObserver(this);
				this.getContentPane().removeAll();
				this.getContentPane().add(loadView);
				this.repaint();
				this.printAll(getGraphics());
				this.setVisible(true);
				this.currentViewState = ViewState.LOAD_VIEW;
				this.data.updateAll();
			}
		} else if (this.data.getDesiredViewState().equals(ViewState.PARAMETER_VIEW)) {
			if (!this.currentViewState.equals(ViewState.PARAMETER_VIEW)) {
				this.data.deleteObservers();
				this.data.addObserver(parameterView);
				this.data.addObserver(client.getFuzzySystem());
				this.data.addObserver(client.getPIDSystem());
				this.data.addObserver(this);
				this.getContentPane().removeAll();
				this.getContentPane().add(parameterView);
				this.repaint();
				this.printAll(getGraphics());
				this.setVisible(true);
				this.currentViewState = ViewState.PARAMETER_VIEW;
				this.data.updateAll();
			}
		} else if (this.data.getDesiredViewState().equals(ViewState.OPTIONS_VIEW)) {
			if (!this.currentViewState.equals(ViewState.OPTIONS_VIEW)) {
				this.data.deleteObservers();
				this.data.addObserver(optionsView);
				this.data.addObserver(client.getFuzzySystem());
				this.data.addObserver(client.getPIDSystem());
				this.data.addObserver(this);
				this.getContentPane().removeAll();
				this.getContentPane().add(optionsView);
				this.repaint();
				this.printAll(getGraphics());
				this.setVisible(true);
				this.currentViewState = ViewState.OPTIONS_VIEW;
				this.data.updateAll();
			}
		} else if (this.data.getDesiredViewState().equals(ViewState.SIMULATION_VIEW)) {
			if (!this.currentViewState.equals(ViewState.SIMULATION_VIEW)) {
				this.data.deleteObservers();
				this.data.addObserver(simulationView);
				this.data.addObserver(client.getFuzzySystem());
				this.data.addObserver(client.getPIDSystem());
				this.data.addObserver(this);
				this.getContentPane().removeAll();
				this.getContentPane().add(simulationView);
				this.repaint();
				this.printAll(getGraphics());
				this.setVisible(true);
				this.currentViewState = ViewState.SIMULATION_VIEW;
				this.data.updateAll();
			}
		} else if (this.data.getDesiredViewState().equals(ViewState.RESULTS_VIEW)) {
			if (!this.currentViewState.equals(ViewState.RESULTS_VIEW)) {
				this.data.deleteObservers();
				this.data.addObserver(resultsView);
				this.data.addObserver(client.getFuzzySystem());
				this.data.addObserver(client.getPIDSystem());
				this.data.addObserver(this);
				this.getContentPane().removeAll();
				this.getContentPane().add(resultsView);
				this.repaint();
				this.printAll(getGraphics());
				this.setVisible(true);
				this.currentViewState = ViewState.RESULTS_VIEW;
				this.data.updateAll();
			}
		}
	}
}
