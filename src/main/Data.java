package main;

import java.util.ArrayList;
import java.util.Observable;
import java.util.concurrent.atomic.AtomicBoolean;

import guis.ViewState;

public class Data extends Observable {
	// Default physical parameters
	public static final double DEFAULT_PENDULUM_LENGTH = 150_000_000.0;
	public static final double DEFAULT_PENDULUM_END_RADIUS = 15_000_000.0;
	public static final double DEFAULT_CART_HEIGHT_AND_WIDTH = 80_000_000.0;
	public static final double DEFAULT_PENDULUM_END_WEIGHT_MASS = 2.0;
	public static final double DEFAULT_CART_MASS = 15.0;
	public static final double DEFAULT_ACCELERATION_DUE_TO_GRAVITY = 0.00_000_000_980_665;
	public static final double DEFAULT_ROLLING_RESISTANCE = 0.05;
	public static final double DEFAULT_PENDULUM_DRAG_COEFFICIENT = 0.3;
	public static final double DEFAULT_CART_DRAG_COEFFICIENT = 0.4;
	public static final double DEFAULT_AIR_DENSITY = 1.225 / (CartAndPendulumSystem.NANO_UNITS_PER_STD_UNIT * CartAndPendulumSystem.NANO_UNITS_PER_STD_UNIT * CartAndPendulumSystem.NANO_UNITS_PER_STD_UNIT);
	
	// Default simulation parameters
	public static final double DEFAULT_SIMULATION_TICK_RATE = 100_000.0;
	public static final double DEFAULT_CONTROLLER_TICK_RATE = 10_000.0;
	public static final double DEFAULT_FRAME_RATE = 60.0;
	public static final double DEFAULT_SAMPLING_RATE = 60.0;
	public static final double DEFAULT_RUN_TIME_SCALE = 1.0;
	public static final boolean DEFAULT_IGNORE_CART = false;
	public static final boolean DEFAULT_USE_NOISE = true;
	public static final double DEFAULT_ANGLE_NOISE = 0.02;
	public static final double DEFAULT_POSITION_NOISE = 0.02;
	public static final double DEFAULT_FORCE_NOISE = 0.02;
	
	private final Client client;
	private ViewState desiredViewState;
	private final ArrayList<String> logs;
	private final ArrayList<String> simulationOutputLogs;
	private final ArrayList<String> commandHistory;
	private final ArrayList<ResultElement> fuzzyResults;
	private final ArrayList<ResultElement> pidResults;
	private final AtomicBoolean gatheringResults;
	private double actualSamplingRate;
	private final Thread resultGatherer;
	
	// Simulation parameters
	private double targetSimTickRate;
	private double targetContTickRate;
	private double targetFrameRate;
	private double targetSamplingRate;
	private double simulationRunTimeScale;
	private boolean ignoreCart;
	private boolean noise;
	private double angleNoise;
	private double positionNoise;
	private double forceNoise;
	
	// Physical parameters
	private double pendulumLength;
	private double pendulumEndRadius;
	private double cartHeightWidth;
	private double pendulumEndWeightMass;
	private double cartMass;
	private double accelerationDueToGravity;
	private double pendulumDragCoefficient;
	private double cartDragCoefficient;
	private double rollingResistance;
	private double airDensity;
	
	// System initializers
	private double force;
	private double velocityOfCart;
	private double accelerationOfCart;
	private double angularVelocityOfPendulum;
	private double angularAccelerationOfPendulum;
	private double cartPosition;
	private double pendulumAngle;

	public Data(Client c) {
		this.client = c;
		
		this.desiredViewState = ViewState.LOAD_VIEW;
		
		this.logs = new ArrayList<>();
		this.simulationOutputLogs = new ArrayList<>();
		this.commandHistory = new ArrayList<>();
		this.fuzzyResults = new ArrayList<>();
		this.pidResults = new ArrayList<>();
		
		this.gatheringResults = new AtomicBoolean(false);
		this.actualSamplingRate = 0.0;
		this.targetSamplingRate = DEFAULT_SAMPLING_RATE;
		this.resultGatherer = new Thread() {
			@Override
			public void run() {
				int ticks = 0;
				long nanoTimeLast = System.nanoTime();
				while (true) {
					if (gatheringResults.get()) {
						fuzzyResults.add(client.getFuzzySystem().getResults());
						pidResults.add(client.getPIDSystem().getResults());
						if (ticks < targetSamplingRate) {
							ticks++;
						} else {
							ticks = 0;
							actualSamplingRate = targetSamplingRate / ((System.nanoTime() - nanoTimeLast) / 1000000000.0);
							nanoTimeLast = System.nanoTime();
						}
						try {
							Thread.sleep((long)(1000 / targetSamplingRate));
						} catch (InterruptedException e) { }
					}
				}
			}
		};
		this.resultGatherer.start();
		
		this.targetSimTickRate = DEFAULT_SIMULATION_TICK_RATE;
		this.targetContTickRate = DEFAULT_CONTROLLER_TICK_RATE;
		this.targetFrameRate = DEFAULT_FRAME_RATE;
		this.simulationRunTimeScale = DEFAULT_RUN_TIME_SCALE;
		this.ignoreCart = DEFAULT_IGNORE_CART;
		this.noise = DEFAULT_USE_NOISE;
		this.angleNoise = DEFAULT_ANGLE_NOISE;
		this.positionNoise = DEFAULT_POSITION_NOISE;
		this.forceNoise = DEFAULT_FORCE_NOISE;
		
		this.pendulumLength = DEFAULT_PENDULUM_LENGTH;
		this.pendulumEndRadius = DEFAULT_PENDULUM_END_RADIUS;
		this.cartHeightWidth = DEFAULT_CART_HEIGHT_AND_WIDTH;
		this.pendulumEndWeightMass = DEFAULT_PENDULUM_END_WEIGHT_MASS;
		this.cartMass = DEFAULT_CART_MASS;
		this.accelerationDueToGravity = DEFAULT_ACCELERATION_DUE_TO_GRAVITY;
		this.rollingResistance = DEFAULT_ROLLING_RESISTANCE;
		this.pendulumDragCoefficient = DEFAULT_PENDULUM_DRAG_COEFFICIENT;
		this.cartDragCoefficient = DEFAULT_CART_DRAG_COEFFICIENT;
		this.airDensity = DEFAULT_AIR_DENSITY;
		
		this.randomizeCartAndPendulumStartData();
	}
	
	public void updateAll() {
		this.setChanged();
		this.notifyObservers();
	}
	
	public ViewState getDesiredViewState() {
		return this.desiredViewState;
	}
	
	public void setDesiredViewState(ViewState vs) {
		this.desiredViewState = vs;
		updateAll();
	}
	
	public ArrayList<String> getLogs() {
		return this.logs;
	}
	
	public ArrayList<String> getSimulationOutputLogs() {
		return this.simulationOutputLogs;
	}

	public ArrayList<String> getCommandHistory() {
		return this.commandHistory;
	}
	
	public String getCommandFromHistory(int index) {
		return this.commandHistory.get((this.commandHistory.size() - 1) - index);
	}
	
	public int getCommandHistorySize() {
		return this.commandHistory.size();
	}
	
	
	public void addLog(String string) {
		if (this.logs.size() == 40) {
			this.logs.remove(0);
		}
		this.logs.add(string);
		updateAll();
	}
	
	public void addSimulationOutputLog(String string) {
		if (this.simulationOutputLogs.size() == 40) {
			this.simulationOutputLogs.remove(0);
		}
		this.simulationOutputLogs.add(string);
		updateAll();
	}
	
	public void addCommandToHistory(String string) {
		if (this.commandHistory.size() == 40) {
			this.commandHistory.remove(0);
		}
		this.commandHistory.add(string);
		updateAll();
	}
	
	public ResultElement[] getFuzzyResults() {
		return fuzzyResults.toArray(new ResultElement[fuzzyResults.size()]);
	}

	public ResultElement[] getPidResults() {
		return pidResults.toArray(new ResultElement[pidResults.size()]);
	}
	
	public void purgeResults() {
		fuzzyResults.clear();
		pidResults.clear();
	}
	
	// Simulation parameters
	
	public boolean isGatheringResults() {
		return gatheringResults.get();
	}
	
	public void setGatheringResults(boolean gatheringResults) {
		this.gatheringResults.set(gatheringResults);
	}
	
	public double getActualSamplingRate() {
		return actualSamplingRate;
	}

	public void setActualSamplingRate(double actualSamplingRate) {
		this.actualSamplingRate = actualSamplingRate;
	}

	public double getTargetSamplingRate() {
		return targetSamplingRate;
	}

	public void setTargetSamplingRate(double targetSamplingRate) {
		this.targetSamplingRate = targetSamplingRate;
	}
	
	public double getTargetSimulationTickRate() {
		return targetSimTickRate;
	}

	public void setTargetSimulationTickRate(double targetSimTickRate) {
		this.targetSimTickRate = targetSimTickRate;
	}
	
	public double getTargetControllerTickRate() {
		return targetContTickRate;
	}

	public void setTargetControllerTickRate(double targetContTickRate) {
		this.targetContTickRate = targetContTickRate;
	}

	public double getSimulationRunTimeScale() {
		return simulationRunTimeScale;
	}

	public void setSimulationRunTimeScale(double simulationRunTimeScale) {
		this.simulationRunTimeScale = simulationRunTimeScale;
	}

	public double getTargetFrameRate() {
		return targetFrameRate;
	}

	public void setTargetFrameRate(double targetFrameRate) {
		this.targetFrameRate = targetFrameRate;
	}
	
	public boolean ignoreCart() {
		return this.ignoreCart;
	}
	
	public void setIgnoreCart(boolean ignoreCart) {
		this.ignoreCart = ignoreCart;
	}

	public boolean useNoise() {
		return noise;
	}

	public void setNoise(boolean noise) {
		this.noise = noise;
	}

	public double getAngleNoise() {
		return angleNoise;
	}

	public void setAngleNoise(double angleNoise) {
		this.angleNoise = angleNoise;
	}

	public double getPositionNoise() {
		return positionNoise;
	}

	public void setPositionNoise(double positionNoise) {
		this.positionNoise = positionNoise;
	}

	public double getForceNoise() {
		return forceNoise;
	}

	public void setForceNoise(double forceNoise) {
		this.forceNoise = forceNoise;
	}
	
	// Physical parameters
	
	public double getPendulumLength() {
		return pendulumLength;
	}

	public void setPendulumLength(double pendulumLength) {
		this.pendulumLength = pendulumLength;
	}

	public double getPendulumEndRadius() {
		return pendulumEndRadius;
	}

	public void setPendulumEndRadius(double pendulumEndRadius) {
		this.pendulumEndRadius = pendulumEndRadius;
	}
	
	public double getPendulumEndWeightMass() {
		return pendulumEndWeightMass;
	}

	public void setPendulumEndWeightMass(double pendulumEndWeightMass) {
		this.pendulumEndWeightMass = pendulumEndWeightMass;
	}

	public double getCartHeightWidth() {
		return cartHeightWidth;
	}

	public void setCartHeightWidth(double cartHeightWidth) {
		this.cartHeightWidth = cartHeightWidth;
	}

	public double getCartMass() {
		return cartMass;
	}

	public void setCartMass(double cartMass) {
		this.cartMass = cartMass;
	}

	public double getAccelerationDueToGravity() {
		return accelerationDueToGravity;
	}

	public void setAccelerationDueToGravity(double accelerationDueToGravity) {
		this.accelerationDueToGravity = accelerationDueToGravity;
	}
	
	public double getRollingResistance() {
		return rollingResistance;
	}

	public void setRollingResistance(double rollingResistance) {
		this.rollingResistance = rollingResistance;
	}


	public double getPendulumDragCoefficient() {
		return pendulumDragCoefficient;
	}

	public void setPendulumDragCoefficient(double pendulumDragCoefficient) {
		this.pendulumDragCoefficient = pendulumDragCoefficient;
	}

	public double getCartDragCoefficient() {
		return cartDragCoefficient;
	}

	public void setCartDragCoefficient(double cartDragCoefficient) {
		this.cartDragCoefficient = cartDragCoefficient;
	}
	
	public double getAirDensity() {
		return airDensity;
	}

	public void setAirDensity(double airDensity) {
		this.airDensity = airDensity;
	}
	
	// System initializers
	
	public void randomizeCartAndPendulumStartData() {
		this.force = 0.0;
		this.velocityOfCart = 0.0;
		this.accelerationOfCart = 0.0;
		this.angularVelocityOfPendulum = 0.0;
		this.angularAccelerationOfPendulum = 0.0;
		this.cartPosition = (CartAndPendulumSystem.TRACK_LENGTH / 4) + (Math.random() * (CartAndPendulumSystem.TRACK_LENGTH / 2));
		this.pendulumAngle = 0.5 + (Math.random() * (Math.PI - 1));
	}

	public void setCartAndPendulumStartDataToDefault() {
		this.force = 0.0;
		this.velocityOfCart = 0.0;
		this.accelerationOfCart = 0.0;
		this.angularVelocityOfPendulum = 0.0;
		this.angularAccelerationOfPendulum = 0.0;
		this.cartPosition = CartAndPendulumSystem.TRACK_LENGTH / 2;
		this.pendulumAngle = Math.PI / 2;
	}
	
	public double getStartingForce() {
		return force;
	}

	public void setStartingForce(double force) {
		this.force = force;
	}

	public double getStartingVelocityOfCart() {
		return velocityOfCart;
	}

	public void setStartingVelocityOfCart(double velocityOfCart) {
		this.velocityOfCart = velocityOfCart;
	}

	public double getStartingAccelerationOfCart() {
		return accelerationOfCart;
	}

	public void setStartingAccelerationOfCart(double accelerationOfCart) {
		this.accelerationOfCart = accelerationOfCart;
	}

	public double getStartingAngularVelocityOfPendulum() {
		return angularVelocityOfPendulum;
	}

	public void setStartingAngularVelocityOfPendulum(double angularVelocityOfPendulum) {
		this.angularVelocityOfPendulum = angularVelocityOfPendulum;
	}

	public double getStartingAngularAccelerationOfPendulum() {
		return angularAccelerationOfPendulum;
	}

	public void setStartingAngularAccelerationOfPendulum(double angularAccelerationOfPendulum) {
		this.angularAccelerationOfPendulum = angularAccelerationOfPendulum;
	}

	public double getStartingCartPosition() {
		return cartPosition;
	}

	public void setStartingCartPosition(double cartPosition) {
		this.cartPosition = cartPosition;
	}

	public double getStartingPendulumAngle() {
		return pendulumAngle;
	}

	public void setStartingPendulumAngle(double pendulumAngle) {
		this.pendulumAngle = pendulumAngle;
	}
}
