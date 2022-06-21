package main;

import static org.junit.Assert.*;

import org.junit.Test;

public class Vector2DTest {
	
	@Test
	public void setPositionTest1() {
		Vector2D actual = Vector2D.newUnitYEqualsXVector();
		Vector2D expected = new Vector2D(5.0, 5.0);
		actual.setPosition(expected);
		assertEquals(expected, actual);
	}
	
	@Test
	public void setPositionTest2() {
		Vector2D actual = new Vector2D();
		Vector2D expected = new Vector2D(10.0, 10.0);
		actual.setPosition(expected.getX(), expected.getY());
		assertEquals(expected, actual);
	}
	
	@Test
	public void returnToOriginTest1() {
		Vector2D actual = Vector2D.newUnitYEqualsXVector();
		actual.returnToOrigin();
		assertEquals(new Vector2D(), actual);
	}
	
	@Test
	public void returnToOriginTest2() {
		Vector2D actual = new Vector2D(-75.0, 54.968);
		actual.setPosition(new Vector2D(7.89, -3.74));
		actual.returnToOrigin();
		assertEquals(new Vector2D(), actual);
	}
	
	@Test
	public void addTest1() {
		Vector2D actual = new Vector2D();
		Vector2D expected = new Vector2D(5.0, 7.8);
		actual.add(expected);
		assertEquals(expected, actual);
	}
	
	@Test
	public void addTest2() {
		Vector2D actual = new Vector2D(10.0, 10.0);
		Vector2D expected = new Vector2D(37.5, 25.0);
		actual.add(new Vector2D(27.5, 15.0));
		assertEquals(expected, actual);
	}
	
	@Test
	public void addTest3() {
		Vector2D actual = new Vector2D(10.0, 10.0);
		Vector2D expected = new Vector2D(75.0, 7.5);
		actual.add(new Vector2D(65.0, -2.5));
		assertEquals(expected, actual);
	}
	
	@Test
	public void addTest4() {
		Vector2D actual = new Vector2D(10.0, 10.0);
		Vector2D expected = new Vector2D(25.0, 25.0);
		Vector2D temp = new Vector2D(15.0, 15.0);
		actual.add(temp.getX(), temp.getY());
		assertEquals(expected, actual);
	}
	
	@Test
	public void subtractTest1() {
		Vector2D actual = new Vector2D(600.0, 875.0);
		Vector2D expected = new Vector2D(125.0, 509.2);
		actual.subtract(new Vector2D(475.0, 365.8));
		assertEquals(expected, actual);
	}
	
	@Test
	public void subtractTest2() {
		Vector2D actual = new Vector2D();
		Vector2D expected = new Vector2D(-5.0, 5.0);
		actual.subtract(new Vector2D(5.0, -5.0));
		assertEquals(expected, actual);
	}
	
	@Test
	public void subtractTest3() {
		Vector2D actual = new Vector2D(25.0, 25.0);
		Vector2D expected = new Vector2D(10.0, 15.0);
		actual.subtract(new Vector2D(15.0, 10.0));
		assertEquals(expected, actual);
	}
	
	@Test
	public void subtractTest4() {
		Vector2D actual = new Vector2D(15.0, 15.0);
		Vector2D expected = new Vector2D(5.0, 7.5);
		Vector2D temp = new Vector2D(10.0, 7.5);
		actual.subtract(temp.getX(), temp.getY());
		assertEquals(expected, actual);
	}
	
	@Test
	public void multiplyTest1() {
		Vector2D actual = new Vector2D();
		Vector2D expected = new Vector2D();
		actual.multiply(new Vector2D(5.0, 5.0));
		assertEquals(expected, actual);
	}
	
	@Test
	public void multiplyTest2() {
		Vector2D actual = new Vector2D(25.0, 25.0);
		Vector2D expected = new Vector2D(2.5, 2.5);
		actual.multiply(new Vector2D(0.1, 0.1));
		assertEquals(expected, actual);
	}
	
	@Test
	public void multiplyTest3() {
		Vector2D actual = new Vector2D(2000.0, 2000.0);
		Vector2D expected = new Vector2D(50000.0, 750.0);
		actual.multiply(new Vector2D(25.0, 3.0/8.0));
		assertEquals(expected, actual);
	}
	
	@Test
	public void multiplyTest4() {
		Vector2D actual = new Vector2D(100.0, 200.0);
		Vector2D expected = new Vector2D(457.5, 1950.0);
		actual.multiply(new Vector2D(4.575, 9.75));
		assertEquals(expected, actual);
	}
	
	@Test
	public void multiplyTest5() {
		Vector2D actual = new Vector2D(15.0, 15.0);
		Vector2D expected = new Vector2D(10.0, 5.0);
		Vector2D temp = new Vector2D(2.0/3.0, 1.0/3.0);
		actual.multiply(temp.getX(), temp.getY());
		assertEquals(expected, actual);
	}
	
	@Test
	public void multiplyTest6() {
		Vector2D actual = new Vector2D(15.0, 15.0);
		Vector2D expected = new Vector2D(60.0, 60.0);
		actual.multiply(4.0);
		assertEquals(expected, actual);
	}
	
	@Test
	public void multiplyTest7() {
		Vector2D actual = new Vector2D(10.0, 15.0);
		Vector2D expected = new Vector2D(67.0, 100.5);
		actual.multiply(6.7);
		assertEquals(expected, actual);
	}
	
	@Test
	public void divideTest1() {
		Vector2D actual = new Vector2D();
		Vector2D expected = new Vector2D();
		actual.divide(new Vector2D(5.0, 5.0));
		assertEquals(expected, actual);
	}
	
	@Test
	public void divideTest2() {
		Vector2D actual = new Vector2D(25.0, 25.0);
		Vector2D expected = new Vector2D(250.0, 250.0);
		actual.divide(new Vector2D(0.1, 0.1));
		assertEquals(expected, actual);
	}
	
	@Test
	public void divideTest3() {
		Vector2D actual = new Vector2D(50000.0, 750.0);
		Vector2D expected = new Vector2D(25.0, 3.0/8.0);
		actual.divide(new Vector2D(2000.0, 2000.0));
		assertEquals(expected, actual);
	}
	
	@Test
	public void divideTest4() {
		Vector2D actual = new Vector2D(457.5, 1950.0);
		Vector2D expected = new Vector2D(100.0, 200.0);
		actual.divide(new Vector2D(4.575, 9.75));
		assertEquals(expected, actual);
	}
	
	@Test
	public void divideTest5() {
		Vector2D actual = new Vector2D(15.0, 15.0);
		Vector2D expected = new Vector2D(7.5, 45.0);
		Vector2D temp = new Vector2D(2.0, 1.0/3.0);
		actual.divide(temp.getX(), temp.getY());
		assertEquals(expected, actual);
	}
	
	@Test
	public void divideTest6() {
		Vector2D actual = new Vector2D(60.0, 60.0);
		Vector2D expected = new Vector2D(15.0, 15.0);
		actual.divide(4.0);
		assertEquals(expected, actual);
	}
	
	@Test
	public void divideTest7() {
		Vector2D actual = new Vector2D(67.0, 100.5);
		Vector2D expected = new Vector2D(10.0, 15.0);
		actual.divide(6.7);
		assertEquals(expected, actual);
	}
	
	@Test
	public void magnitudeTest1() {
		Vector2D actual = new Vector2D(10.0, 10.0);
		assertEquals(14.14, actual.getMagnitude(), 0.01);
	}
	
	@Test
	public void magnitudeTest2() {
		Vector2D actual = new Vector2D(125.7, 56.3);
		assertEquals(137.73, actual.getMagnitude(), 0.01);
	}
	
	@Test
	public void degreeAngleBetweenTest1() {
		Vector2D vec1 = new Vector2D(10.0, 10.0);
		vec1.rotateRadians(2.0);
		Vector2D vec2 = new Vector2D(10.0, 10.0);
		assertEquals(2.0, vec1.getRadianAngleBetween(vec2), 0.01);
	}
}
