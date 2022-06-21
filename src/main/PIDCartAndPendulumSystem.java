package main;

public class PIDCartAndPendulumSystem extends CartAndPendulumSystem {
	private static final long serialVersionUID = 6520577911904321802L;
	
	public static final double DEFAULT_PGP = 2500.0;
	public static final double DEFAULT_IGP = 500.0;
	public static final double DEFAULT_DGP = 150.0;
	public static final double DEFAULT_PGC = 0.0;
	public static final double DEFAULT_IGC = 0.0;
	public static final double DEFAULT_DGC = 0.0;
	
	private double pGp;
	private double iGp;
	private double dGp;
	private double pGc;
	private double iGc;
	private double dGc;
	private double pp;
	private double ip;
	private double dp;
	private double pc;
	private double ic;
	private double dc;
	private double integralSumP;
	private double integralSumC;

	public PIDCartAndPendulumSystem(Client client) {
		super(client, "PID");
		this.pGp = DEFAULT_PGP / NANO_UNITS_PER_STD_UNIT;
		this.iGp = DEFAULT_IGP / (NANO_UNITS_PER_STD_UNIT * NANO_UNITS_PER_STD_UNIT);
		this.dGp = DEFAULT_DGP;
		this.pGc = DEFAULT_PGC / (NANO_UNITS_PER_STD_UNIT * NANO_UNITS_PER_STD_UNIT);
		this.iGc = DEFAULT_IGC / (NANO_UNITS_PER_STD_UNIT * NANO_UNITS_PER_STD_UNIT * NANO_UNITS_PER_STD_UNIT);
		this.dGc = DEFAULT_DGC / NANO_UNITS_PER_STD_UNIT;
		
		this.integralSumP = 0.0;
		this.integralSumC = 0.0;
	}

	@Override
	protected void control(long deltaNanoSeconds) {
		if (this.useNoise()) {
			this.pp = this.pGp * ((Math.PI / 2) - this.getPendulumAngleWithNoise());
			this.pc = this.pGc * ((TRACK_LENGTH / 2) - this.getCartPositionWithNoise());
			this.integralSumP += (deltaNanoSeconds * ((Math.PI / 2) - this.getPendulumAngleWithNoise()));
			this.integralSumC += (deltaNanoSeconds * ((TRACK_LENGTH / 2) - this.getCartPositionWithNoise()));
			this.ip = this.iGp * this.integralSumP;
			this.ic = this.iGc * this.integralSumC;
			this.dp = this.dGp * -this.getPendulumAngularVelocityWithNoise();
			this.dc = this.dGc * -this.getCartVelocityWithNoise();
			this.setForceWithNoise(pp + ip + dp + pc + ic + dc);
		} else {
			this.pp = this.pGp * ((Math.PI / 2) - this.getPendulumAngle());
			this.pc = this.pGc * ((TRACK_LENGTH / 2) - this.getCartPosition());
			this.integralSumP += (deltaNanoSeconds * ((Math.PI / 2) - this.getPendulumAngle()));
			this.integralSumC += (deltaNanoSeconds * ((TRACK_LENGTH / 2) - this.getCartPosition()));
			this.ip = this.iGp * this.integralSumP;
			this.ic = this.iGc * this.integralSumC;
			this.dp = this.dGp * -this.getPendulumAngularVelocity();
			this.dc = this.dGc * -this.getCartVelocity();
			this.setForce(pp + ip + dp + pc + ic + dc);
		}
	}
	
	@Override
	public void stop() {
		super.stop();
		this.integralSumP = 0.0;
		this.integralSumC = 0.0;
	}
	
	@Override
	public void reset() {
		super.reset();
		this.integralSumP = 0.0;
		this.integralSumC = 0.0;
	}
	
	public double getPendulumAngleProportionalForceContribution() {
		return pp * NANO_UNITS_PER_STD_UNIT;
	}
	
	public double getPendulumAngleIntegralForceContribution() {
		return ip * NANO_UNITS_PER_STD_UNIT;
	}
	
	public double getPendulumAngleDerivativeForceContribution() {
		return dp * NANO_UNITS_PER_STD_UNIT;
	}
	
	public double getCartPositionProportionalForceContribution() {
		return pc * NANO_UNITS_PER_STD_UNIT;
	}
	
	public double getCartPositionIntegralForceContribution() {
		return ic * NANO_UNITS_PER_STD_UNIT;
	}
	
	public double getCartPositionDerivativeForceContribution() {
		return dc * NANO_UNITS_PER_STD_UNIT;
	}
	
	public double getPendulumAngleProportionalGain() {
		return pGp;
	}

	public void setPendulumAngleProportionalGain(double proportionalGain) {
		this.pGp = proportionalGain / NANO_UNITS_PER_STD_UNIT;
	}

	public double getPendulumAngleIntegralGain() {
		return iGp;
	}

	public void setPendulumAngleIntegralGain(double integralGain) {
		this.iGp = integralGain / (NANO_UNITS_PER_STD_UNIT * NANO_UNITS_PER_STD_UNIT);
	}

	public double getPendulumAngleDerivativeGain() {
		return dGp;
	}

	public void setPendulumAngleDerivativeGain(double derivativeGain) {
		this.dGp = derivativeGain;
	}
	
	public double getCartPositionProportionalGain() {
		return pGc;
	}

	public void setCartPositionProportionalGain(double proportionalGain) {
		this.pGc = proportionalGain / (NANO_UNITS_PER_STD_UNIT * NANO_UNITS_PER_STD_UNIT);
	}

	public double getCartPositionIntegralGain() {
		return iGc;
	}

	public void setCartPositionIntegralGain(double integralGain) {
		this.iGc = integralGain / (NANO_UNITS_PER_STD_UNIT * NANO_UNITS_PER_STD_UNIT * NANO_UNITS_PER_STD_UNIT);
	}

	public double getCartPositionDerivativeGain() {
		return dGc;
	}

	public void setCartPositionDerivativeGain(double derivativeGain) {
		this.dGc = derivativeGain / NANO_UNITS_PER_STD_UNIT;
	}
}
