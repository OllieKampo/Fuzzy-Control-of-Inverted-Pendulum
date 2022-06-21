package main;

import java.io.Serializable;

public class Vector2D implements Comparable<Vector2D>, Cloneable, Serializable {
	private static final long serialVersionUID = -3269131803136087948L;
	
	private double x;
	private double y;
	
	public Vector2D(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	public Vector2D() {
		this.x = 0.0;
		this.y = 0.0;
	}
	
	public static Vector2D newUnitYEqualsXVector() {
		return new Vector2D(1.0, 1.0);
	}
	
	public static Vector2D newUnitXAxisVector() {
		return new Vector2D(1.0, 0.0);
	}
	
	public static Vector2D newUnitYAxisVector() {
		return new Vector2D(0.0, 1.0);
	}
	
	@Override
	public synchronized boolean equals(Object obj) {
		if (obj instanceof Vector2D) {
			Vector2D vector = (Vector2D) obj;
			return this.x == vector.x && this.y == vector.y;
		} else {
			return super.equals(obj);
		}
	}
	
	@Override
	public synchronized int compareTo(Vector2D vector) {
		return (int) (this.getMagnitude() - vector.getMagnitude());
	}
	
	@Override
	public synchronized Vector2D clone() {
		return new Vector2D(this.x, this.y);
	}
	
	@Override
	public synchronized String toString() {
		return "[X= " + x + ", Y= " + y + "]";
	}
	/*
	public abstract class VectorPaintStyle {
		public final String name;
		public VectorPaintStyle(String name) {
			this.name = name;
		}
		public abstract void paintVector(Graphics g, int x, int y);
		public class Circle {
			public final int radius;
			public Circle(int radius) {
				super("Circle Vector paint style");
				this.radius = radius
			}
			private void paintVector(Graphics g, int x, int y) {
				g.drawCircle(x, y, this.radius);
			}
		}
	}
	public void paint(Graphics g, VectorPaintStyle vps) {
		vps.paint(g, this.x, this.y);
	}
	*/
	public synchronized double getX() {
		return x;
	}
	
	public synchronized int getXintValue() {
		return (int)x;
	}
	
	public synchronized void setX(double x) {
		this.x = x;
	}
	
	public synchronized double getY() {
		return y;
	}
	
	public synchronized int getYintValue() {
		return (int)y;
	}
	
	public synchronized void setY(double y) {
		this.y = y;
	}
	
	public synchronized double[] getPosition() {
		return new double[] {this.x, this.y};
	}
	
	public synchronized void setPosition(Vector2D point) {
		this.x = point.x;
		this.y = point.y;
	}
	
	public synchronized void setPosition(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	public synchronized void returnToOrigin() {
		this.x = 0.0;
		this.y = 0.0;
	}
	
	public synchronized void add(Vector2D vector) {
		this.x += vector.x;
		this.y += vector.y;
	}
	
	public synchronized void add(double x, double y) {
		this.x += x;
		this.y += y;
	}
	
	public synchronized void subtract(Vector2D vector) {
		this.x -= vector.x;
		this.y -= vector.y;
	}
	
	public synchronized void subtract(double x, double y) {
		this.x -= x;
		this.y -= y;
	}
	
	public synchronized void multiply(Vector2D vector) {
		this.x *= vector.x;
		this.y *= vector.y;
	}
	
	public synchronized void multiply(double x, double y) {
		this.x *= x;
		this.y *= y;
	}
	
	public synchronized void multiply(double factor) {
		this.x *= factor;
		this.y *= factor;
	}
	
	public synchronized void divide(Vector2D vector) {
		this.x /= vector.x;
		this.y /= vector.y;
	}
	
	public synchronized void divide(double x, double y) {
		this.x /= x;
		this.y /= y;
	}
	
	public synchronized void divide(double factor) {
		this.x /= factor;
		this.y /= factor;
	}
	
	public synchronized double getMagnitude() {
		return Math.sqrt((this.x * this.x) + (this.y * this.y));
	}
	
	public synchronized double getGradient() {
		return this.y / this.x;
	}
	
	public synchronized double getDegreeAngleBetween(Vector2D vector) {
		return this.getDegreeAngleAroundOrigin() - vector.getDegreeAngleAroundOrigin();
	}
	
	public synchronized double getRadianAngleBetween(Vector2D vector) {
		return this.getRadianAngleAroundOrigin() - vector.getRadianAngleAroundOrigin();
	}
	
	public synchronized double getDegreeAngleAroundOrigin() {
		if (this.x >= 0.0 && this.y == 0.0) {
			return Math.toDegrees(0.0);
		} else if (this.x > 0.0 && this.y > 0.0) {
			return Math.toDegrees(Math.atan(this.y / this.x));
		} else if (this.x == 0.0 && this.y > 0.0) {
			return Math.toDegrees(Math.PI / 2.0);
		} else if (this.x < 0.0 && this.y > 0.0) {
			return Math.toDegrees(Math.PI + Math.atan(this.y / this.x));
		} else if (this.x < 0.0 && this.y == 0.0) {
			return Math.toDegrees(Math.PI);
		} else if (this.x < 0.0 && this.y < 0.0) {
			return Math.toDegrees(Math.PI + Math.atan(this.y / this.x));
		} else if (this.x == 0.0 && this.y < 0.0) {
			return Math.toDegrees(Math.PI * 1.5);
		} else {
			return Math.toDegrees((2.0 * Math.PI) + Math.atan(this.y / this.x));
		}
	}
	
	public synchronized double getRadianAngleAroundOrigin() {
		if (this.x >= 0.0 && this.y == 0.0) {
			return 0.0;
		} else if (this.x > 0.0 && this.y > 0.0) {
			return Math.atan(this.y / this.x);
		} else if (this.x == 0 && this.y > 0.0) {
			return Math.PI / 2.0;
		} else if (this.x < 0.0 && this.y > 0.0) {
			return Math.PI + Math.atan(this.y / this.x);
		} else if (this.x < 0.0 && this.y == 0.0) {
			return Math.PI;
		} else if (this.x < 0.0 && this.y < 0.0) {
			return Math.PI + Math.atan(this.y / this.x);
		} else if (this.x == 0.0 && this.y < 0.0) {
			return Math.PI * 1.5;
		} else {
			return (2.0 * Math.PI) + Math.atan(this.y / this.x);
		}
	}
	
	public synchronized double getDegreeAngleAround(Vector2D vector) {
		Vector2D temp = this.getPositionRelativeTo(vector);
		if (temp.x >= 0.0 && temp.y == 0.0) {
			return Math.toDegrees(0.0);
		} else if (temp.x > 0.0 && temp.y > 0.0) {
			return Math.toDegrees(Math.atan(temp.y / temp.x));
		} else if (temp.x == 0 && temp.y > 0) {
			return Math.toDegrees(Math.PI / 2.0);
		} else if (temp.x < 0.0 && temp.y > 0.0) {
			return Math.toDegrees(Math.PI + Math.atan(temp.y / temp.x));
		} else if (temp.x < 0.0 && temp.y == 0.0) {
			return Math.toDegrees(Math.PI);
		} else if (temp.x < 0.0 && temp.y < 0.0) {
			return Math.toDegrees(Math.PI + Math.atan(temp.y / temp.x));
		} else if (temp.x == 0.0 && temp.y < 0.0) {
			return Math.toDegrees(Math.PI * 1.5);
		} else {
			return Math.toDegrees((2 * Math.PI) + Math.atan(temp.y / temp.x));
		}
	}
	
	public synchronized double getRadianAngleAround(Vector2D vector) {
		Vector2D temp = this.getPositionRelativeTo(vector);
		if (temp.x >= 0.0 && temp.y == 0.0) {
			return 0.0;
		} else if (temp.x > 0.0 && temp.y > 0.0) {
			return Math.atan(temp.y / temp.x);
		} else if (temp.x == 0.0 && temp.y > 0.0) {
			return Math.PI / 2.0;
		} else if (temp.x < 0.0 && temp.y > 0.0) {
			return Math.PI + Math.atan(temp.y / temp.x);
		} else if (temp.x < 0.0 && temp.y == 0.0) {
			return Math.PI;
		} else if (temp.x < 0.0 && temp.y < 0.0) {
			return Math.PI + Math.atan(temp.y / temp.x);
		} else if (temp.x == 0.0 && temp.y < 0.0) {
			return Math.PI * 1.5;
		} else {
			return (2.0 * Math.PI) + Math.atan(temp.y / temp.x);
		}
	}
	
	public synchronized void normalize() { 
		this.divide(this.getMagnitude());
	}
	
	public synchronized Vector2D getNormalized() {
		Vector2D thisTemp = this.clone();
		thisTemp.divide(this.getMagnitude());
		return thisTemp;
	}
	
	public synchronized double getDotProduct(Vector2D vector) {
		return (this.x * vector.x) + (this.y * vector.y);
	}
	
	public synchronized double getAnalogCrossProduct(Vector2D vector) {
		return (this.x * vector.y) - (this.y * vector.x);
	}
	
	/**
	 * Rotates this Vector2D counter-clockwise around the origin by the specified number of degrees.
	 * @param degrees - The number of degrees to rotate around as a double
	 */
	public synchronized void rotateDegrees(double degrees) {
		double cos = Math.cos((degrees * Math.PI) / 180.0);
		double sin = Math.sin((degrees * Math.PI) / 180.0);
		double xt = (x * cos) - (y * sin);
		this.y = (x * sin) + (y * cos);
		this.x = xt;
	}
	
	/**
	 * Rotates this Vector2D counter-clockwise around the origin by the specified number of radians.
	 * @param radians - The number of degrees to rotate around as a double
	 */
	public synchronized void rotateRadians(double radians) {
		double cos = Math.cos(radians);
		double sin = Math.sin(radians);
		double xt = (x * cos) - (y * sin);
		this.y = (x * sin) + (y * cos);
		this.x = xt;
	}
	
	/**
	 * Rotates this Vector2D counter-clockwise around the specified argument Vector2D by the specified number of degrees.
	 * @param vector - The Vector2D to rotate around
	 * @param degrees - The number of degrees to rotate around as a double
	 */
	public synchronized void rotateDegreesAround(Vector2D vector, double degrees) {
		this.subtract(vector);
		this.rotateDegrees(degrees);
		this.add(vector);
	}
	
	/**
	 * Rotates this Vector2D counter-clockwise around the specified argument Vector2D by the specified number of radians.
	 * @param vector - The Vector2D to rotate around
	 * @param radians - The number of radians to rotate around as a double
	 */
	public synchronized void rotateRadiansAround(Vector2D vector, double radians) {
		this.subtract(vector);
		this.rotateRadians(radians);
		this.add(vector);
	}
	
	/**
	 * @return new Vector2D perpendicular to this Vector2D
	 */
	public synchronized Vector2D getPerpendicular() {
		Vector2D temp = this.clone();
		temp.rotateDegrees(90.0);
		return temp;
	}
	
	public synchronized Vector2D getPositionRelativeTo(Vector2D point) {
		return new Vector2D(this.x - point.x, this.y - point.y);
	}
	
	public synchronized Vector2D getPositionRelativeTo(double x, double y) {
		return new Vector2D(this.x - x, this.y - y);
	}
}
