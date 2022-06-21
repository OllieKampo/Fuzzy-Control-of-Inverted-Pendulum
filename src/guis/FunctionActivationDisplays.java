package guis;

import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JPanel;

import main.FuzzyCartAndPendulumSystem;

public class FunctionActivationDisplays extends JPanel {
	private static final long serialVersionUID = 4122987562336490726L;
	
	public static final int WIDTH = 1200;
	public static final int HEIGHT = 120;
	
	private FunctionActivationDisplay pp;
	private FunctionActivationDisplay pv;
	private FunctionActivationDisplay cp;
	private FunctionActivationDisplay cv;

	public FunctionActivationDisplays(FuzzyCartAndPendulumSystem.FuzzyVariable[] fvs, FuzzyCartAndPendulumSystem.MemberFunction[] mfs) {
		for (int i = 0; i < fvs.length; i++) {
			if (fvs[i].equals("cart position")) {
				this.cp = new FunctionActivationDisplay(fvs[i], mfs);
			} else if (fvs[i].equals("cart velocity")) {
				this.cv = new FunctionActivationDisplay(fvs[i], mfs);
			} else if (fvs[i].equals("pendulum angle")) {
				this.pp = new FunctionActivationDisplay(fvs[i], mfs);
			} else if (fvs[i].equals("pendulum angular velocity")) {
				this.pv = new FunctionActivationDisplay(fvs[i], mfs);
			}
		}
		this.setLayout(new GridLayout(1, 4));
		this.add(this.pp);
		this.add(this.pv);
		this.add(this.cp);
		this.add(this.cv);
		this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
	}
	
	public void updateMemberFunctions(FuzzyCartAndPendulumSystem.MemberFunction[] mfs) {
		this.pp.updateMemberFunctions(mfs);
		this.pv.updateMemberFunctions(mfs);
		this.cp.updateMemberFunctions(mfs);
		this.cv.updateMemberFunctions(mfs);
	}
	
	public void updateVariables(double pendulumAngle, double pendulumAngularVelocity, double cartPosition, double cartVelocity) {
		this.pp.updateVariableValue(pendulumAngle);
		this.pv.updateVariableValue(pendulumAngularVelocity);
		this.cp.updateVariableValue(cartPosition);
		this.cv.updateVariableValue(cartVelocity);
	}
}
