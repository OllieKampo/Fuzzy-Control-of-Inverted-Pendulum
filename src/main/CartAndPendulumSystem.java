package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.swing.JPanel;
/**
 * @author Oliver Kamperis
 * TODO
 */
public abstract class CartAndPendulumSystem extends JPanel implements Observer {
	private static final long serialVersionUID = -6058726174799778701L;
	
	// Constants
	public static final double NANO_UNITS_PER_STD_UNIT = 1_000_000_000.0;
	public static final double NANO_UNITS_PER_MILLI_UNIT = 1_000_000.0;
	public static final double MILLI_UNITS_PER_NANO_UNIT = 0.00_000_1;
	public static final double MILLI_UNITS_PER_STD_UNIT = 1_000.0;
	
	// Lengths
	public static final double CART_LENGTH = 175_000_000.0;
	public static final double CART_TOP_TO_PENDULUM_HINDGE_JOINT_DISTANCE = 25_000_000.0;
	public static final double HINDGE_WIDTH = 20_000_000;
	public static final double TRACK_LENGTH = 1_000_000_000.0;
	public static final double FRAME_WIDTH = 1_200_000_000.0;
	public static final double FRAME_HEIGHT = 580_000_000.0;
	public static final double TRACK_TO_CART_BOTTOM_CLEARANCE = 30_000_000.0;
	public static final double FRAME_BOTTOM_TO_TRACK_CLEARANCE = 100_000_000.0;
	public static final double FRAME_SIDE_TO_TRACK_START_CLEARANCE = (FRAME_WIDTH - TRACK_LENGTH) / 2;
	public static final double WHEEL_POSITION_OFFSET_FROM_CART_ENDS = 35_000_000.0;
	
	// Actual simulation parameters
	private volatile double targetSimTickRate;
	private volatile double targetContTickRate;
	private volatile double timeScale;
	private boolean ignoreCart;
	private boolean noise;
	private double angleNoise;
	private double positionNoise;
	private double forceNoise;
	
	// Actual physical parameters
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
	
	// Position vectors
	protected final Vector2D position_of_pendulum_top_end;
	protected final Vector2D position_of_pendulum_hindge;
	protected final Vector2D position_of_front_of_cart;
	protected final Vector2D position_of_rear_of_cart;
	
	// Movement variables
	private double force; // Giga-Newtons
	private double velocityOfCart; // Nano-meters / Nano-second
	private double accelerationOfCart; // Nano-meters / Nano-second ^ 2
	private double angularVelocityOfPendulum; // Rads / Nano-second
	private double angularAccelerationOfPendulum; // Rads / Nano-second ^ 2
	
	// References and miscellaneous private variables
	protected final Client client;
	protected final Data data;
	private final AtomicBoolean running;
	private volatile double actualSimTickRate;
	private volatile double actualContTickRate;
	private final Thread simTicker;
	private final Thread contTicker;
	private final String name;
	private long startTime;
	private long pauseTime;
	private long unpauseTime;
	private boolean paused;
	
	public CartAndPendulumSystem(Client client, String name) {
		this.setPreferredSize(new Dimension((int)(FRAME_WIDTH / NANO_UNITS_PER_MILLI_UNIT), (int)(FRAME_HEIGHT / NANO_UNITS_PER_MILLI_UNIT)));
		
		this.client = client;
		this.data = client.getData();
		
		// Actual simulation parameter init
		this.targetSimTickRate = data.getTargetSimulationTickRate();
		this.targetContTickRate = data.getTargetControllerTickRate();
		this.timeScale = data.getSimulationRunTimeScale();
		this.ignoreCart = data.ignoreCart();
		this.noise = data.useNoise();
		this.positionNoise = data.getPositionNoise();
		this.angleNoise = data.getAngleNoise();
		this.forceNoise = data.getForceNoise();
		
		// Actual physical parameter init
		this.pendulumLength = data.getPendulumLength();
		this.pendulumEndRadius = data.getPendulumEndRadius();
		this.cartHeightWidth = data.getCartHeightWidth();
		this.pendulumEndWeightMass = data.getPendulumEndWeightMass();
		this.cartMass = data.getCartMass();
		this.accelerationDueToGravity = data.getAccelerationDueToGravity();
		this.rollingResistance = data.getRollingResistance();
		this.pendulumDragCoefficient = data.getPendulumDragCoefficient();
		this.cartDragCoefficient = data.getCartDragCoefficient();
		this.airDensity = data.getAirDensity();
		
		// Position vector init
		this.position_of_pendulum_hindge = new Vector2D(data.getStartingCartPosition(), cartHeightWidth + FRAME_BOTTOM_TO_TRACK_CLEARANCE + TRACK_TO_CART_BOTTOM_CLEARANCE + CART_TOP_TO_PENDULUM_HINDGE_JOINT_DISTANCE);
		this.position_of_pendulum_top_end = new Vector2D(data.getStartingCartPosition() + pendulumLength, cartHeightWidth + FRAME_BOTTOM_TO_TRACK_CLEARANCE + TRACK_TO_CART_BOTTOM_CLEARANCE + CART_TOP_TO_PENDULUM_HINDGE_JOINT_DISTANCE);
		position_of_pendulum_top_end.rotateRadiansAround(position_of_pendulum_hindge, data.getStartingPendulumAngle());
		this.position_of_front_of_cart = new Vector2D(data.getStartingCartPosition() + (CART_LENGTH / 2), FRAME_BOTTOM_TO_TRACK_CLEARANCE + TRACK_TO_CART_BOTTOM_CLEARANCE);
		this.position_of_rear_of_cart = new Vector2D(data.getStartingCartPosition() - (CART_LENGTH / 2), FRAME_BOTTOM_TO_TRACK_CLEARANCE + TRACK_TO_CART_BOTTOM_CLEARANCE);
		
		// Movement variable init
		this.force = data.getStartingForce();
		this.velocityOfCart = data.getStartingVelocityOfCart();
		this.accelerationOfCart = data.getStartingAccelerationOfCart();
		this.angularVelocityOfPendulum = data.getStartingAngularVelocityOfPendulum();
		this.angularAccelerationOfPendulum = data.getStartingAngularAccelerationOfPendulum();
		
		this.running = new AtomicBoolean(false);
		this.actualSimTickRate = 0.0;
		this.actualContTickRate = 0.0;
		
		this.simTicker = new Thread() {
			@Override
			public void run() {
				int ticks = 0;
				long tickNanoTimeLast = System.nanoTime();
				long nanoTimeCurrent = tickNanoTimeLast;
				long nanoTimeLast = nanoTimeCurrent;
				long deltaNanoSeconds;
				while (true) {
					if (running.get()) {
						nanoTimeCurrent = System.nanoTime();
						deltaNanoSeconds = nanoTimeCurrent - nanoTimeLast;
						nanoTimeLast = nanoTimeCurrent;
						tick((long)(deltaNanoSeconds * timeScale));
						if (ticks < targetSimTickRate) {
							ticks++;
						} else {
							ticks = 0;
							actualSimTickRate = targetSimTickRate / ((System.nanoTime() - tickNanoTimeLast) / NANO_UNITS_PER_STD_UNIT);
							tickNanoTimeLast = System.nanoTime();
						}
						long end = System.nanoTime() + (long)(NANO_UNITS_PER_STD_UNIT / targetSimTickRate);
						while (System.nanoTime() < end) {
						     Thread.yield();
						}
					} else {
						try {
							Thread.sleep(1000);
							nanoTimeLast = System.nanoTime();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
			}
		};
		this.simTicker.start();
		
		this.contTicker = new Thread() {
			@Override
			public void run() {
				int ticks = 0;
				long tickNanoTimeLast = System.nanoTime();
				long nanoTimeCurrent = tickNanoTimeLast;
				long nanoTimeLast = nanoTimeCurrent;
				long deltaNanoSeconds;
				while (true) {
					if (running.get()) {
						nanoTimeCurrent = System.nanoTime();
						deltaNanoSeconds = nanoTimeCurrent - nanoTimeLast;
						nanoTimeLast = nanoTimeCurrent;
						control((long)(deltaNanoSeconds * timeScale));
						if (ticks < targetContTickRate) {
							ticks++;
						} else {
							ticks = 0;
							actualContTickRate = targetContTickRate / ((System.nanoTime() - tickNanoTimeLast) / NANO_UNITS_PER_STD_UNIT);
							tickNanoTimeLast = System.nanoTime();
						}
						long end = System.nanoTime() + (long)(NANO_UNITS_PER_STD_UNIT / targetContTickRate);
						while (System.nanoTime() < end) {
						     Thread.yield();
						}
					} else {
						try {
							Thread.sleep(1000);
							nanoTimeLast = System.nanoTime();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
			}
		};
		this.contTicker.start();
		
		this.name = name;
		this.startTime = 0;
		this.pauseTime = 0;
		this.unpauseTime = 0;
		this.paused = false;
	}
	
	@Override
	public void update(Observable arg0, Object arg1) {
		this.targetSimTickRate = data.getTargetSimulationTickRate();
		this.targetContTickRate = data.getTargetControllerTickRate();
		this.timeScale = data.getSimulationRunTimeScale();
		this.ignoreCart = data.ignoreCart();
		this.noise = data.useNoise();
		this.positionNoise = data.getPositionNoise();
		this.angleNoise = data.getAngleNoise();
		this.forceNoise = data.getForceNoise();
	}
	
	protected abstract void control(long deltaNanoSeconds);
	
	private void tick(long deltaNanoSeconds) {
		double linearVelocityOfPendulum = this.velocityOfCart - (this.angularVelocityOfPendulum * this.pendulumLength * Math.sin(position_of_pendulum_top_end.getRadianAngleAround(position_of_pendulum_hindge)));
		double rollingResistanceForce = this.rollingResistance * (this.cartMass + (Math.sin(position_of_pendulum_top_end.getRadianAngleAround(position_of_pendulum_hindge)))) * this.accelerationDueToGravity;
		
		if (!(this.force < 0 && this.position_of_rear_of_cart.getX() == 0.0) && !(this.force > 0 && this.position_of_front_of_cart.getX() == TRACK_LENGTH)) {
			double force = this.force + (this.velocityOfCart == 0.0 ? 0.0 : (this.velocityOfCart < 0.0 ? rollingResistanceForce : -rollingResistanceForce))
					- (0.5 * this.cartDragCoefficient * this.airDensity * this.cartHeightWidth * this.cartHeightWidth * this.velocityOfCart * this.velocityOfCart)
					- (0.5 * this.pendulumDragCoefficient * this.airDensity * Math.PI * this.pendulumEndRadius * this.pendulumEndRadius * linearVelocityOfPendulum * linearVelocityOfPendulum);
			
			this.accelerationOfCart = (force / (cartMass + (pendulumEndWeightMass * Math.abs(Math.cos(position_of_pendulum_top_end.getRadianAngleAround(position_of_pendulum_hindge))))));
			double v0 = this.velocityOfCart;
			this.velocityOfCart = (accelerationOfCart * deltaNanoSeconds) + velocityOfCart;
			double x = 0.5 * (velocityOfCart + v0) * deltaNanoSeconds;
			
			if (this.position_of_rear_of_cart.getX() + x < 0.0) {
				x = -this.position_of_rear_of_cart.getX();
				this.angularVelocityOfPendulum -= (this.velocityOfCart / this.pendulumLength) * Math.sin(position_of_pendulum_top_end.getRadianAngleAround(position_of_pendulum_hindge));
				this.accelerationOfCart = 0.0;
				this.velocityOfCart = 0.0;
			} else if (this.position_of_front_of_cart.getX() + x > TRACK_LENGTH) {
				x = TRACK_LENGTH - this.position_of_front_of_cart.getX();
				this.angularVelocityOfPendulum -= (this.velocityOfCart / this.pendulumLength) * Math.sin(position_of_pendulum_top_end.getRadianAngleAround(position_of_pendulum_hindge));
				this.accelerationOfCart = 0.0;
				this.velocityOfCart = 0.0;
			}
			if (!this.ignoreCart) {
				this.position_of_pendulum_hindge.add(x, 0.0);
				this.position_of_pendulum_top_end.add(x, 0.0);
				this.position_of_front_of_cart.add(x, 0.0);
				this.position_of_rear_of_cart.add(x, 0.0);
			}
		}
		
		this.angularAccelerationOfPendulum = 3.0 * (((accelerationOfCart / pendulumLength) * Math.sin(position_of_pendulum_top_end.getRadianAngleAround(position_of_pendulum_hindge)))
				- ((this.accelerationDueToGravity / pendulumLength) * Math.cos(position_of_pendulum_top_end.getRadianAngleAround(position_of_pendulum_hindge)))
				- (((0.5 * this.pendulumDragCoefficient * this.airDensity * Math.PI * this.pendulumEndRadius * this.pendulumEndRadius * this.angularVelocityOfPendulum * this.pendulumLength * this.angularVelocityOfPendulum * this.pendulumLength) / this.pendulumEndWeightMass) / this.pendulumLength));
		double v1 = this.angularVelocityOfPendulum;
		this.angularVelocityOfPendulum += (angularAccelerationOfPendulum * deltaNanoSeconds);
		double y = 0.5 * (angularVelocityOfPendulum + v1) * deltaNanoSeconds;
		
		if (position_of_pendulum_top_end.getRadianAngleAround(position_of_pendulum_hindge) + y > Math.PI * 1.5 || position_of_pendulum_top_end.getRadianAngleAround(position_of_pendulum_hindge) + y < 0.1) {
			y = ((2 * Math.PI) + 0.1) - position_of_pendulum_top_end.getRadianAngleAround(position_of_pendulum_hindge);
			this.angularVelocityOfPendulum = 0.0;
			this.angularAccelerationOfPendulum = 0.0;
		} else if (position_of_pendulum_top_end.getRadianAngleAround(position_of_pendulum_hindge) + y > Math.PI - 0.1) {
			y = (Math.PI - 0.1) - position_of_pendulum_top_end.getRadianAngleAround(position_of_pendulum_hindge);
			this.angularVelocityOfPendulum = 0.0;
			this.angularAccelerationOfPendulum = 0.0;
		}
		
		this.position_of_pendulum_top_end.rotateRadiansAround(position_of_pendulum_hindge, y);
	}
	
	public void start() {
		if (this.running.compareAndSet(false, true)) {
			if (paused) {
				this.data.addSimulationOutputLog("Unpausing " + name + " controller");
				this.unpauseTime = System.nanoTime();
				this.paused = false;
			} else {
				this.data.addSimulationOutputLog("Starting " + name + " controller");
				this.startTime = System.nanoTime();
				this.pauseTime = startTime;
				this.unpauseTime = startTime;
			}
		}
	}
	
	public void stop() {
		if (this.running.compareAndSet(true, false)) {
			this.data.addSimulationOutputLog("Stopping " + name + " controller");
			this.pendulumLength = data.getPendulumLength();
			this.pendulumEndRadius = data.getPendulumEndRadius();
			this.cartHeightWidth = data.getCartHeightWidth();
			this.pendulumEndWeightMass = data.getPendulumEndWeightMass();
			this.cartMass = data.getCartMass();
			this.accelerationDueToGravity = data.getAccelerationDueToGravity();
			this.rollingResistance = data.getRollingResistance();
			this.pendulumDragCoefficient = data.getPendulumDragCoefficient();
			this.cartDragCoefficient = data.getCartDragCoefficient();
			this.airDensity = data.getAirDensity();
			
			this.position_of_pendulum_hindge.setPosition(data.getStartingCartPosition(), cartHeightWidth + FRAME_BOTTOM_TO_TRACK_CLEARANCE + TRACK_TO_CART_BOTTOM_CLEARANCE + CART_TOP_TO_PENDULUM_HINDGE_JOINT_DISTANCE);
			this.position_of_pendulum_top_end.setPosition(data.getStartingCartPosition() + pendulumLength, cartHeightWidth + FRAME_BOTTOM_TO_TRACK_CLEARANCE + TRACK_TO_CART_BOTTOM_CLEARANCE + CART_TOP_TO_PENDULUM_HINDGE_JOINT_DISTANCE);
			position_of_pendulum_top_end.rotateRadiansAround(position_of_pendulum_hindge, data.getStartingPendulumAngle());
			this.position_of_front_of_cart.setPosition(data.getStartingCartPosition() + (CART_LENGTH / 2), FRAME_BOTTOM_TO_TRACK_CLEARANCE + TRACK_TO_CART_BOTTOM_CLEARANCE);
			this.position_of_rear_of_cart.setPosition(data.getStartingCartPosition() - (CART_LENGTH / 2), FRAME_BOTTOM_TO_TRACK_CLEARANCE + TRACK_TO_CART_BOTTOM_CLEARANCE);
			
			this.force = data.getStartingForce();
			this.velocityOfCart = data.getStartingVelocityOfCart();
			this.accelerationOfCart = data.getStartingAccelerationOfCart();
			this.angularVelocityOfPendulum = data.getStartingAngularVelocityOfPendulum();
			this.angularAccelerationOfPendulum = data.getStartingAngularAccelerationOfPendulum();
			
			this.paused = false;
		}
	}
	
	public void pause() {
		if (this.running.compareAndSet(true, false)) {
			this.data.addSimulationOutputLog("Pausing " + name + " controller");
			this.pauseTime = System.nanoTime();
			this.paused = true;
		}
	}
	
	public void reset() {
		this.data.addSimulationOutputLog("Reseting " + name + " controller");
		this.pendulumLength = data.getPendulumLength();
		this.pendulumEndRadius = data.getPendulumEndRadius();
		this.cartHeightWidth = data.getCartHeightWidth();
		this.pendulumEndWeightMass = data.getPendulumEndWeightMass();
		this.cartMass = data.getCartMass();
		this.accelerationDueToGravity = data.getAccelerationDueToGravity();
		this.rollingResistance = data.getRollingResistance();
		this.pendulumDragCoefficient = data.getPendulumDragCoefficient();
		this.cartDragCoefficient = data.getCartDragCoefficient();
		this.airDensity = data.getAirDensity();
		
		this.position_of_pendulum_hindge.setPosition(data.getStartingCartPosition(), cartHeightWidth + FRAME_BOTTOM_TO_TRACK_CLEARANCE + TRACK_TO_CART_BOTTOM_CLEARANCE + CART_TOP_TO_PENDULUM_HINDGE_JOINT_DISTANCE);
		this.position_of_pendulum_top_end.setPosition(data.getStartingCartPosition() + pendulumLength, cartHeightWidth + FRAME_BOTTOM_TO_TRACK_CLEARANCE + TRACK_TO_CART_BOTTOM_CLEARANCE + CART_TOP_TO_PENDULUM_HINDGE_JOINT_DISTANCE);
		position_of_pendulum_top_end.rotateRadiansAround(position_of_pendulum_hindge, data.getStartingPendulumAngle());
		this.position_of_front_of_cart.setPosition(data.getStartingCartPosition() + (CART_LENGTH / 2), FRAME_BOTTOM_TO_TRACK_CLEARANCE + TRACK_TO_CART_BOTTOM_CLEARANCE);
		this.position_of_rear_of_cart.setPosition(data.getStartingCartPosition() - (CART_LENGTH / 2), FRAME_BOTTOM_TO_TRACK_CLEARANCE + TRACK_TO_CART_BOTTOM_CLEARANCE);
		
		this.force = data.getStartingForce();
		this.velocityOfCart = data.getStartingVelocityOfCart();
		this.accelerationOfCart = data.getStartingAccelerationOfCart();
		this.angularVelocityOfPendulum = data.getStartingAngularVelocityOfPendulum();
		this.angularAccelerationOfPendulum = data.getStartingAngularAccelerationOfPendulum();
		
		this.startTime = System.nanoTime();
		this.pauseTime = startTime;
		this.unpauseTime = startTime;
	}
	
	public double getActualSimulationTickRate() {
		return actualSimTickRate;
	}
	
	public double getActualControllerTickRate() {
		return actualContTickRate;
	}

	public ResultElement getResults() {
		return new ResultElement((System.nanoTime() - this.startTime) - (this.unpauseTime - this.pauseTime),
				this.force * NANO_UNITS_PER_STD_UNIT,
				this.accelerationOfCart * NANO_UNITS_PER_STD_UNIT,
				this.velocityOfCart * MILLI_UNITS_PER_STD_UNIT,
				this.position_of_pendulum_hindge.getX() / NANO_UNITS_PER_MILLI_UNIT,
				this.angularAccelerationOfPendulum * NANO_UNITS_PER_STD_UNIT * NANO_UNITS_PER_STD_UNIT,
				this.angularVelocityOfPendulum * NANO_UNITS_PER_STD_UNIT,
				this.position_of_pendulum_top_end.getRadianAngleAround(position_of_pendulum_hindge));
	}
	
	public boolean isPaused() {
		return paused;
	}
	
	protected boolean useNoise() {
		return noise;
	}
	
	public double getCartForce() {
		return force;
	}
	
	protected void setForce(double force) {
		this.force = force;
	}
	
	protected void setForceWithNoise(double force) {
		this.force = force * (1.0 - ((1.0 - (2.0 * Math.random())) * this.forceNoise)); 
	}
	
	public double getCartAcceleration() {
		return accelerationOfCart;
	}
	
	public double getCartVelocity() {
		return velocityOfCart;
	}
	
	protected double getCartVelocityWithNoise() {
		return velocityOfCart * (1.0 - ((1.0 - (2.0 * Math.random())) * this.positionNoise));
	}
	
	public double getCartPosition() {
		return position_of_pendulum_hindge.getX();
	}
	
	protected double getCartPositionWithNoise() {
		return position_of_pendulum_hindge.getX() * (1.0 - ((1.0 - (2.0 * Math.random())) * this.positionNoise));
	}
	
	public double getPendulumAngularAcceleration() {
		return angularAccelerationOfPendulum;
	}
	
	public double getPendulumAngularVelocity() {
		return angularVelocityOfPendulum;
	}
	
	protected double getPendulumAngularVelocityWithNoise() {
		return angularVelocityOfPendulum * (1.0 - ((1.0 - (2.0 * Math.random())) * this.angleNoise));
	}
	
	public double getPendulumAngle() {
		return position_of_pendulum_top_end.getRadianAngleAround(position_of_pendulum_hindge);
	}
	
	protected double getPendulumAngleWithNoise() {
		return position_of_pendulum_top_end.getRadianAngleAround(position_of_pendulum_hindge) * (1.0 - ((1.0 - (2.0 * Math.random())) * this.angleNoise));
	}
	
	public double getCartRearPosition() {
		return position_of_rear_of_cart.getX();
	}
	
	public double getCartFrontPosition() {
		return position_of_front_of_cart.getX();
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.setColor(Color.BLACK);
		g.drawLine((int)((this.position_of_pendulum_top_end.getX() + FRAME_SIDE_TO_TRACK_START_CLEARANCE) / NANO_UNITS_PER_MILLI_UNIT), (int)((FRAME_HEIGHT - this.position_of_pendulum_top_end.getY()) / NANO_UNITS_PER_MILLI_UNIT),
				(int)((this.position_of_pendulum_hindge.getX() + FRAME_SIDE_TO_TRACK_START_CLEARANCE) / NANO_UNITS_PER_MILLI_UNIT), (int)((FRAME_HEIGHT - this.position_of_pendulum_hindge.getY()) / NANO_UNITS_PER_MILLI_UNIT));
		g.drawArc((int)(((this.position_of_pendulum_top_end.getX() + FRAME_SIDE_TO_TRACK_START_CLEARANCE) - (this.pendulumEndRadius / 2)) / NANO_UNITS_PER_MILLI_UNIT),
				(int)((FRAME_HEIGHT - (this.position_of_pendulum_top_end.getY() + (this.pendulumEndRadius / 2))) / NANO_UNITS_PER_MILLI_UNIT),
				(int)(this.pendulumEndRadius / NANO_UNITS_PER_MILLI_UNIT), (int)(this.pendulumEndRadius / NANO_UNITS_PER_MILLI_UNIT), 0, 360);
		g.drawArc((int)(((this.position_of_pendulum_hindge.getX() + FRAME_SIDE_TO_TRACK_START_CLEARANCE) - (HINDGE_WIDTH / 2)) / NANO_UNITS_PER_MILLI_UNIT),
				(int)((FRAME_HEIGHT - (this.position_of_pendulum_hindge.getY() + (HINDGE_WIDTH / 2))) / NANO_UNITS_PER_MILLI_UNIT),
				(int)(HINDGE_WIDTH / NANO_UNITS_PER_MILLI_UNIT), (int)(HINDGE_WIDTH / NANO_UNITS_PER_MILLI_UNIT), 0, 180);
		g.drawLine((int)(((this.position_of_pendulum_hindge.getX() + FRAME_SIDE_TO_TRACK_START_CLEARANCE) - (HINDGE_WIDTH / 2)) / NANO_UNITS_PER_MILLI_UNIT),
				(int)((FRAME_HEIGHT - this.position_of_pendulum_hindge.getY()) / NANO_UNITS_PER_MILLI_UNIT),
				(int)(((this.position_of_pendulum_hindge.getX() + FRAME_SIDE_TO_TRACK_START_CLEARANCE) - (HINDGE_WIDTH / 2)) / NANO_UNITS_PER_MILLI_UNIT),
				(int)((FRAME_HEIGHT - (this.position_of_pendulum_hindge.getY() - CART_TOP_TO_PENDULUM_HINDGE_JOINT_DISTANCE)) / NANO_UNITS_PER_MILLI_UNIT));
		g.drawLine((int)(((this.position_of_pendulum_hindge.getX() + FRAME_SIDE_TO_TRACK_START_CLEARANCE) + (HINDGE_WIDTH / 2)) / NANO_UNITS_PER_MILLI_UNIT),
				(int)((FRAME_HEIGHT - this.position_of_pendulum_hindge.getY()) / NANO_UNITS_PER_MILLI_UNIT),
				(int)(((this.position_of_pendulum_hindge.getX() + FRAME_SIDE_TO_TRACK_START_CLEARANCE) + (HINDGE_WIDTH / 2)) / NANO_UNITS_PER_MILLI_UNIT),
				(int)((FRAME_HEIGHT - (this.position_of_pendulum_hindge.getY() - CART_TOP_TO_PENDULUM_HINDGE_JOINT_DISTANCE)) / NANO_UNITS_PER_MILLI_UNIT));
		int[] cartX = new int[4];
		cartX[0] = (int)((this.position_of_rear_of_cart.getX() + FRAME_SIDE_TO_TRACK_START_CLEARANCE) / NANO_UNITS_PER_MILLI_UNIT);
		cartX[1] = cartX[0];
		cartX[2] = (int)((this.position_of_front_of_cart.getX() + FRAME_SIDE_TO_TRACK_START_CLEARANCE) / NANO_UNITS_PER_MILLI_UNIT);
		cartX[3] = cartX[2];
		int[] cartY = new int[4];
		cartY[0] = (int)((FRAME_HEIGHT - (position_of_rear_of_cart.getY() + this.cartHeightWidth)) / NANO_UNITS_PER_MILLI_UNIT);
		cartY[1] = (int)((FRAME_HEIGHT - position_of_front_of_cart.getY()) / NANO_UNITS_PER_MILLI_UNIT);
		cartY[2] = cartY[1];
		cartY[3] = cartY[0];
		g.drawPolygon(cartX, cartY, 4);
		g.drawArc((int)((((this.position_of_rear_of_cart.getX() + FRAME_SIDE_TO_TRACK_START_CLEARANCE) + WHEEL_POSITION_OFFSET_FROM_CART_ENDS) - TRACK_TO_CART_BOTTOM_CLEARANCE) / NANO_UNITS_PER_MILLI_UNIT),
				(int)((FRAME_HEIGHT - (this.position_of_rear_of_cart.getY() + TRACK_TO_CART_BOTTOM_CLEARANCE)) / NANO_UNITS_PER_MILLI_UNIT),
				(int)((TRACK_TO_CART_BOTTOM_CLEARANCE * 2) / NANO_UNITS_PER_MILLI_UNIT), (int)((TRACK_TO_CART_BOTTOM_CLEARANCE * 2) / NANO_UNITS_PER_MILLI_UNIT), 180, 180);
		g.drawArc((int)((((this.position_of_front_of_cart.getX() + FRAME_SIDE_TO_TRACK_START_CLEARANCE) - WHEEL_POSITION_OFFSET_FROM_CART_ENDS) - TRACK_TO_CART_BOTTOM_CLEARANCE) / NANO_UNITS_PER_MILLI_UNIT),
				(int)((FRAME_HEIGHT - (this.position_of_front_of_cart.getY() + TRACK_TO_CART_BOTTOM_CLEARANCE)) / NANO_UNITS_PER_MILLI_UNIT),
				(int)((TRACK_TO_CART_BOTTOM_CLEARANCE * 2) / NANO_UNITS_PER_MILLI_UNIT), (int)((TRACK_TO_CART_BOTTOM_CLEARANCE * 2) / NANO_UNITS_PER_MILLI_UNIT), 180, 180);
		g.drawLine((int)(((FRAME_WIDTH - TRACK_LENGTH) / 2) / NANO_UNITS_PER_MILLI_UNIT), (int)((FRAME_HEIGHT - FRAME_BOTTOM_TO_TRACK_CLEARANCE) / NANO_UNITS_PER_MILLI_UNIT),
				(int)(((FRAME_WIDTH + TRACK_LENGTH) / 2) / NANO_UNITS_PER_MILLI_UNIT), (int)((FRAME_HEIGHT - FRAME_BOTTOM_TO_TRACK_CLEARANCE) / NANO_UNITS_PER_MILLI_UNIT));
		if (Math.abs(this.force * NANO_UNITS_PER_STD_UNIT) < 150.0) {
			g.setColor(Color.BLUE);
		} else if (Math.abs(this.force * NANO_UNITS_PER_STD_UNIT) < 300.0) {
			g.setColor(Color.ORANGE);
		} else {
			g.setColor(Color.RED);
		}
		int forceLineX1 = (int)((this.position_of_pendulum_hindge.getX() + FRAME_SIDE_TO_TRACK_START_CLEARANCE) / NANO_UNITS_PER_MILLI_UNIT);
		int forceLineY1 = (int)((FRAME_HEIGHT - (this.position_of_pendulum_hindge.getY() - (CART_TOP_TO_PENDULUM_HINDGE_JOINT_DISTANCE + (this.cartHeightWidth / 2.0)))) / NANO_UNITS_PER_MILLI_UNIT);
		int forceLineX2 = (int)(((this.position_of_pendulum_hindge.getX() + FRAME_SIDE_TO_TRACK_START_CLEARANCE) + (this.force * NANO_UNITS_PER_STD_UNIT * NANO_UNITS_PER_MILLI_UNIT)) / NANO_UNITS_PER_MILLI_UNIT);
		int forceLineY2 = (int)((FRAME_HEIGHT - (this.position_of_pendulum_hindge.getY() - (CART_TOP_TO_PENDULUM_HINDGE_JOINT_DISTANCE + (this.cartHeightWidth / 2.0)))) / NANO_UNITS_PER_MILLI_UNIT);
		g.drawLine(forceLineX1, forceLineY1, forceLineX2, forceLineY2);
		if (this.force > 0.0) {
			g.drawLine(forceLineX2, forceLineY2,
					forceLineX2 - 10, forceLineY2 - 10);
			g.drawLine(forceLineX2, forceLineY2,
					forceLineX2 - 10, forceLineY2 + 10);
		} else {
			g.drawLine(forceLineX2, forceLineY2,
					forceLineX2 + 10, forceLineY2 - 10);
			g.drawLine(forceLineX2, forceLineY2,
					forceLineX2 + 10, forceLineY2 + 10);
		}
		g.drawString("Tractive force: " + String.format("%.02f", this.force * NANO_UNITS_PER_STD_UNIT), forceLineX1 - 70, forceLineY1 - 20);
	}
}
