package guis;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class RatesPanel extends JPanel {
	private static final long serialVersionUID = 2314584514717645793L;
	
	public static final int WIDTH = 1200;
	public static final int HEIGHT = 100;
	
	private final JTextField actualSimTickRateField;
	private final JTextField targetSimTickRateField;
	private final JTextField actualContTickRateField;
	private final JTextField targetContTickRateField;
	private final JTextField actualFrameRateField;
	private final JTextField targetFrameRateField;
	private final JTextField actualSamplingRateField;
	private final JTextField targetSamplingRateField;
	private final JTextField runTimeScaleField;

	public RatesPanel() {
		this.actualSimTickRateField = new JTextField();
		this.targetSimTickRateField = new JTextField();
		this.actualContTickRateField = new JTextField();
		this.targetContTickRateField = new JTextField();
		this.actualFrameRateField = new JTextField();
		this.targetFrameRateField = new JTextField();
		this.actualSamplingRateField = new JTextField();
		this.targetSamplingRateField = new JTextField();
		this.runTimeScaleField = new JTextField();
		
		this.actualSimTickRateField.setPreferredSize(new Dimension(80, 50));
		this.targetSimTickRateField.setPreferredSize(new Dimension(80, 50));
		this.actualContTickRateField.setPreferredSize(new Dimension(80, 50));
		this.targetContTickRateField.setPreferredSize(new Dimension(80, 50));
		this.actualFrameRateField.setPreferredSize(new Dimension(80, 50));
		this.targetFrameRateField.setPreferredSize(new Dimension(80, 50));
		this.actualSamplingRateField.setPreferredSize(new Dimension(80, 50));
		this.targetSamplingRateField.setPreferredSize(new Dimension(80, 50));
		this.runTimeScaleField.setPreferredSize(new Dimension(80, 100));
		
		this.actualSimTickRateField.setEditable(false);
		this.targetSimTickRateField.setEditable(false);
		this.actualContTickRateField.setEditable(false);
		this.targetContTickRateField.setEditable(false);
		this.actualFrameRateField.setEditable(false);
		this.targetFrameRateField.setEditable(false);
		this.actualSamplingRateField.setEditable(false);
		this.targetSamplingRateField.setEditable(false);
		this.runTimeScaleField.setEditable(false);
		
		JPanel simTickRateLeft = new JPanel() {
			private static final long serialVersionUID = 2849941763398251105L;
			@Override
			public void paintComponent(Graphics g) {
				g.drawString("Target physics tick rate:", 5, 30);
				g.drawString("Actual physics tick rate:", 5, 80);
			}
		};
		simTickRateLeft.setPreferredSize(new Dimension(160, 100));
		
		JPanel simTickRateRight = new JPanel();
		simTickRateRight.setLayout(new GridLayout(2, 1));
		simTickRateRight.add(targetSimTickRateField);
		simTickRateRight.add(actualSimTickRateField);
		simTickRateRight.setPreferredSize(new Dimension(80, 100));
		
		JPanel simTickRatePanel = new JPanel();
		simTickRatePanel.setLayout(new BoxLayout(simTickRatePanel, BoxLayout.X_AXIS));
		simTickRatePanel.add(simTickRateLeft);
		simTickRatePanel.add(simTickRateRight);
		simTickRatePanel.setPreferredSize(new Dimension(240, 100));
		
		JPanel contTickRateLeft = new JPanel() {
			private static final long serialVersionUID = 3591551293348940198L;
			@Override
			public void paintComponent(Graphics g) {
				g.drawString("Target controller tick rate:", 5, 30);
				g.drawString("Actual controller tick rate:", 5, 80);
			}
		};
		contTickRateLeft.setPreferredSize(new Dimension(160, 100));
		
		JPanel contTickRateRight = new JPanel();
		contTickRateRight.setLayout(new GridLayout(2, 1));
		contTickRateRight.add(targetContTickRateField);
		contTickRateRight.add(actualContTickRateField);
		contTickRateRight.setPreferredSize(new Dimension(80, 100));
		
		JPanel contTickRatePanel = new JPanel();
		contTickRatePanel.setLayout(new BoxLayout(contTickRatePanel, BoxLayout.X_AXIS));
		contTickRatePanel.add(contTickRateLeft);
		contTickRatePanel.add(contTickRateRight);
		contTickRatePanel.setPreferredSize(new Dimension(240, 100));
		
		JPanel frameRateLeft = new JPanel() {
			private static final long serialVersionUID = -3013737575230485795L;
			@Override
			public void paintComponent(Graphics g) {
				g.drawString("Target frame rate:", 5, 30);
				g.drawString("Actual frame rate:", 5, 80);
			}
		};
		frameRateLeft.setPreferredSize(new Dimension(160, 100));
		
		JPanel frameRateRight = new JPanel();
		frameRateRight.setLayout(new GridLayout(2, 1));
		frameRateRight.add(targetFrameRateField);
		frameRateRight.add(actualFrameRateField);
		frameRateRight.setPreferredSize(new Dimension(80, 100));
		
		JPanel frameRatePanel = new JPanel();
		frameRatePanel.setLayout(new BoxLayout(frameRatePanel, BoxLayout.X_AXIS));
		frameRatePanel.add(frameRateLeft);
		frameRatePanel.add(frameRateRight);
		frameRatePanel.setPreferredSize(new Dimension(240, 100));
		
		JPanel samplingRateLeft = new JPanel() {
			private static final long serialVersionUID = -3617742514132518214L;
			@Override
			public void paintComponent(Graphics g) {
				g.drawString("Target sampling rate:", 5, 30);
				g.drawString("Actual sampling rate:", 5, 80);
			}
		};
		samplingRateLeft.setPreferredSize(new Dimension(160, 100));
		
		JPanel samplingRateRight = new JPanel();
		samplingRateRight.setLayout(new GridLayout(2, 1));
		samplingRateRight.add(targetSamplingRateField);
		samplingRateRight.add(actualSamplingRateField);
		samplingRateRight.setPreferredSize(new Dimension(80, 100));
		
		JPanel samplingRatePanel = new JPanel();
		samplingRatePanel.setLayout(new BoxLayout(samplingRatePanel, BoxLayout.X_AXIS));
		samplingRatePanel.add(samplingRateLeft);
		samplingRatePanel.add(samplingRateRight);
		samplingRatePanel.setPreferredSize(new Dimension(240, 100));
		
		JPanel scalingRateLeft = new JPanel() {
			private static final long serialVersionUID = 1938076321077091718L;
			@Override
			public void paintComponent(Graphics g) {
				g.drawString("Run time scaling factor:", 10, 50);
			}
		};
		scalingRateLeft.setPreferredSize(new Dimension(160, 100));
		
		JPanel scalingRatePanel = new JPanel();
		scalingRatePanel.setLayout(new BoxLayout(scalingRatePanel, BoxLayout.X_AXIS));
		scalingRatePanel.add(scalingRateLeft);
		scalingRatePanel.add(runTimeScaleField);
		scalingRatePanel.setPreferredSize(new Dimension(240, 100));
		
		this.setLayout(new GridLayout(1, 5));
		
		this.add(simTickRatePanel);
		this.add(contTickRatePanel);
		this.add(frameRatePanel);
		this.add(samplingRatePanel);
		this.add(scalingRatePanel);
		
		this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
	}
	
	public void updateTargets(double targetSimTick, double targetContTick, double targetFrames, double targetSampling) {
		this.targetSimTickRateField.setText(String.format("%.02f", targetSimTick));
		this.targetContTickRateField.setText(String.format("%.02f", targetContTick));
		this.targetFrameRateField.setText(String.format("%.02f", targetFrames));
		this.targetSamplingRateField.setText(String.format("%.02f", targetSampling));
	}
	
	public void updateActuals(double actualSimTick, double actualContTick, double actualFrames, double actualSampling) {
		this.actualSimTickRateField.setText(String.format("%.02f", actualSimTick));
		this.actualContTickRateField.setText(String.format("%.02f", actualContTick));
		this.actualFrameRateField.setText(String.format("%.02f", actualFrames));
		this.actualSamplingRateField.setText(String.format("%.02f", actualSampling));
	}
	
	public void updateTimeScaleFactor(double factor) {
		this.runTimeScaleField.setText(String.format("%.02f", factor));
	}
}
