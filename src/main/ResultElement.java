package main;

public final class ResultElement {
	public final long nanoTime;
	public final double force;
	public final double cartAcceleration;
	public final double cartVelocity;
	public final double cartPosition;
	public final double pendulumAcceleration;
	public final double pendulumVelocity;
	public final double pendulumAngle;
	
	public ResultElement(long nanoTime, double force, double cartAcceleration, double cartVelocity, double cartPosition,
		double pendulumAcceleration, double pendulumVelocity, double pendulumAngle) {
		super();
		this.nanoTime = nanoTime;
		this.force = force;
		this.cartAcceleration = cartAcceleration;
		this.cartVelocity = cartVelocity;
		this.cartPosition = cartPosition;
		this.pendulumAcceleration = pendulumAcceleration;
		this.pendulumVelocity = pendulumVelocity;
		this.pendulumAngle = pendulumAngle;
	}
	
	@Override
	public String toString() {
		return "Time (ns): " + this.nanoTime
				+ ", Force on cart (N): " + this.force
				+ ", Cart acceleration (mm/s^2): " + this.cartAcceleration
				+ ", Cart Velocity (mm/s): " + this.cartVelocity
				+ ", Cart position (mm): " + this.cartPosition
				+ ", Pendulum acceleration (rads/s^2): " + this.pendulumAcceleration
				+ ", Pendulum velocity (rads/s): " + this.pendulumVelocity
				+ ", Pendulum angle (rads): " + this.pendulumAngle;
	}
	
	public String toSavableString() {
		return "" + this.nanoTime + ":" +
		this.force + ":" +
		this.cartAcceleration + ":" +
		this.cartVelocity + ":" +
		this.cartPosition + ":" +
		this.pendulumAcceleration + ":" +
		this.pendulumVelocity + ":" +
		this.pendulumAngle;
	}
	
	public static ResultElement loadFromString(String s) {
		String[] a = s.split(":");
		return new ResultElement(Integer.valueOf(a[0]).longValue(), Integer.valueOf(a[1]).doubleValue(), Integer.valueOf(a[2]).doubleValue(), Integer.valueOf(a[3]).doubleValue(),
									Integer.valueOf(a[4]).doubleValue(), Integer.valueOf(a[5]).doubleValue(), Integer.valueOf(a[6]).doubleValue(), Integer.valueOf(a[7]).doubleValue());
	}
}
