package guis;

public abstract class ViewState {
	private final String stateName;
	
	public static final ViewState LOAD_VIEW = new ViewState.LoadViewState();
	public static final ViewState PARAMETER_VIEW = new ViewState.ParameterViewState();
	public static final ViewState OPTIONS_VIEW = new ViewState.OptionsViewState();
	public static final ViewState SIMULATION_VIEW = new ViewState.SimulationViewState();
	public static final ViewState RESULTS_VIEW = new ViewState.ResultsViewState();
	
	private ViewState(String stateName) {
		this.stateName = stateName;
	}
	
	@Override
	public boolean equals(Object arg0) {
		return arg0.getClass() == this.getClass();
	}
	
	public String getViewStateName() {
		return this.stateName;
	}
	
	private static final class LoadViewState extends ViewState {

		public LoadViewState() {
			super("Load view");
		}
	}
	
	private static final class ParameterViewState extends ViewState {

		public ParameterViewState() {
			super("Parameter view");
		}
	}
	
	private static final class OptionsViewState extends ViewState {

		public OptionsViewState() {
			super("Options view");
		}
	}
	
	private static final class SimulationViewState extends ViewState {

		public SimulationViewState() {
			super("Simulation view");
		}
	}
	
	private static final class ResultsViewState extends ViewState {

		public ResultsViewState() {
			super("Results view");
		}
	}
}
