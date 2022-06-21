package guis;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import main.CartAndPendulumSystem;
import main.Client;
import main.Data;

public class OptionsView extends JPanel implements Observer {
	private static final long serialVersionUID = 4920833858559509237L;
	
	private final Data data;
	private final ConsolePanel console;

	public OptionsView(Client client) {
		this.data = client.getData();
		this.console = new ConsolePanel(client);
		
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.populateAndAddVariablesPanel();
		this.populateAndAddBottomPanel();
		this.setPreferredSize(new Dimension(ClientGui.FRAME_WIDTH, ClientGui.FRAME_HEIGHT));
	}
	
	@Override
	public void update(Observable o, Object arg) {
		this.console.update(o, arg);
	}
	
	private void populateAndAddVariablesPanel() {
		JPanel physicalParametersHeader = new JPanel() {
			private static final long serialVersionUID = -5713198472968784780L;
			@Override
			public void paintComponent(Graphics g) {
				g.setFont(new Font("", Font.BOLD, 35));
				g.drawString("Physical parameters:", 10, 55);
			}
		};
		physicalParametersHeader.setPreferredSize(new Dimension(ClientGui.FRAME_WIDTH / 4, 80));
		
		JTextField pendulumLengthField = new JTextField(Double.toString(Data.DEFAULT_PENDULUM_LENGTH / CartAndPendulumSystem.NANO_UNITS_PER_MILLI_UNIT));
		pendulumLengthField.getDocument().addDocumentListener(new DocumentListener() {
		    @Override
		    public void insertUpdate(DocumentEvent e) {
		    	try {
		    		pendulumLengthField.setForeground(Color.BLACK);
		    		data.setPendulumLength(Double.valueOf(pendulumLengthField.getText()) * CartAndPendulumSystem.NANO_UNITS_PER_MILLI_UNIT);
		    		data.addLog("Pendulum length set to: " + pendulumLengthField.getText() + ", reset the simulation to apply the change to the model");
		    	} catch (Exception ex) {
		    		pendulumLengthField.setForeground(Color.RED);
		    		data.setPendulumLength(Data.DEFAULT_PENDULUM_LENGTH);
		    		data.addLog("Pendulum length value= \"" + pendulumLengthField.getText() + "\" is not a number, the default value= \"" + Double.toString(Data.DEFAULT_PENDULUM_LENGTH / CartAndPendulumSystem.NANO_UNITS_PER_MILLI_UNIT) + "\" has been applied");
		    	}
		    }
		    @Override
		    public void removeUpdate(DocumentEvent e) {
		    	try {
		    		pendulumLengthField.setForeground(Color.BLACK);
		    		data.setPendulumLength(Double.valueOf(pendulumLengthField.getText()) * CartAndPendulumSystem.NANO_UNITS_PER_MILLI_UNIT);
		    		data.addLog("Pendulum length set to: " + pendulumLengthField.getText() + ", reset the simulation to apply the change to the model");
		    	} catch (Exception ex) {
		    		pendulumLengthField.setForeground(Color.RED);
		    		data.setPendulumLength(Data.DEFAULT_PENDULUM_LENGTH);
		    		data.addLog("Pendulum length value= \"" + pendulumLengthField.getText() + "\" is not a number, the default value= \"" + Double.toString(Data.DEFAULT_PENDULUM_LENGTH / CartAndPendulumSystem.NANO_UNITS_PER_MILLI_UNIT) + "\" has been applied");
		    	}
		    }
		    @Override
		    public void changedUpdate(DocumentEvent e) { }
		});
		pendulumLengthField.setPreferredSize(new Dimension(ClientGui.FRAME_WIDTH / 4, 36));
		
		JTextField pendulumEndRadiusField = new JTextField(Double.toString(Data.DEFAULT_PENDULUM_END_RADIUS / CartAndPendulumSystem.NANO_UNITS_PER_MILLI_UNIT));
		pendulumEndRadiusField.getDocument().addDocumentListener(new DocumentListener() {
		    @Override
		    public void insertUpdate(DocumentEvent e) {
		    	try {
		    		pendulumEndRadiusField.setForeground(Color.BLACK);
		    		data.setPendulumEndRadius(Double.valueOf(pendulumEndRadiusField.getText()) * CartAndPendulumSystem.NANO_UNITS_PER_MILLI_UNIT);
		    		data.addLog("Pendulum end radius set to: " + pendulumEndRadiusField.getText() + ", reset the simulation to apply the change to the model");
		    	} catch (Exception ex) {
		    		pendulumEndRadiusField.setForeground(Color.RED);
		    		data.setPendulumEndRadius(Data.DEFAULT_PENDULUM_END_RADIUS);
		    		data.addLog("Pendulum end radius value= \"" + pendulumEndRadiusField.getText() + "\" is not a number, the default value= \"" + Double.toString(Data.DEFAULT_PENDULUM_END_RADIUS / CartAndPendulumSystem.NANO_UNITS_PER_MILLI_UNIT) + "\" has been applied");
		    	}
		    }
		    @Override
		    public void removeUpdate(DocumentEvent e) {
		    	try {
		    		pendulumEndRadiusField.setForeground(Color.BLACK);
		    		data.setPendulumEndRadius(Double.valueOf(pendulumEndRadiusField.getText()) * CartAndPendulumSystem.NANO_UNITS_PER_MILLI_UNIT);
		    		data.addLog("Pendulum end radius set to: " + pendulumEndRadiusField.getText() + ", reset the simulation to apply the change to the model");
		    	} catch (Exception ex) {
		    		pendulumEndRadiusField.setForeground(Color.RED);
		    		data.setPendulumEndRadius(Data.DEFAULT_PENDULUM_END_RADIUS);
		    		data.addLog("Pendulum end radius value= \"" + pendulumEndRadiusField.getText() + "\" is not a number, the default value= \"" + Double.toString(Data.DEFAULT_PENDULUM_END_RADIUS / CartAndPendulumSystem.NANO_UNITS_PER_MILLI_UNIT) + "\" has been applied");
		    	}
		    }
		    @Override
		    public void changedUpdate(DocumentEvent e) { }
		});
		pendulumEndRadiusField.setPreferredSize(new Dimension(ClientGui.FRAME_WIDTH / 4, 36));
		
		JTextField cartHeightWidthField = new JTextField(Double.toString(Data.DEFAULT_CART_HEIGHT_AND_WIDTH / CartAndPendulumSystem.NANO_UNITS_PER_MILLI_UNIT));
		cartHeightWidthField.getDocument().addDocumentListener(new DocumentListener() {
		    @Override
		    public void insertUpdate(DocumentEvent e) {
		    	try {
		    		cartHeightWidthField.setForeground(Color.BLACK);
		    		data.setCartHeightWidth(Double.valueOf(cartHeightWidthField.getText()) * CartAndPendulumSystem.NANO_UNITS_PER_MILLI_UNIT);
		    		data.addLog("Cart height and width set to: " + cartHeightWidthField.getText() + ", reset the simulation to apply the change to the model");
		    	} catch (Exception ex) {
		    		cartHeightWidthField.setForeground(Color.RED);
		    		data.setCartHeightWidth(Data.DEFAULT_CART_HEIGHT_AND_WIDTH);
		    		data.addLog("Cart height and width value= \"" + cartHeightWidthField.getText() + "\" is not a number, the default value= \"" + Double.toString(Data.DEFAULT_CART_HEIGHT_AND_WIDTH / CartAndPendulumSystem.NANO_UNITS_PER_MILLI_UNIT) + "\" has been applied");
		    	}
		    }
		    @Override
		    public void removeUpdate(DocumentEvent e) {
		    	try {
		    		cartHeightWidthField.setForeground(Color.BLACK);
		    		data.setCartHeightWidth(Double.valueOf(cartHeightWidthField.getText()) * CartAndPendulumSystem.NANO_UNITS_PER_MILLI_UNIT);
		    		data.addLog("Cart height and width set to: " + cartHeightWidthField.getText() + ", reset the simulation to apply the change to the model");
		    	} catch (Exception ex) {
		    		cartHeightWidthField.setForeground(Color.RED);
		    		data.setCartHeightWidth(Data.DEFAULT_CART_HEIGHT_AND_WIDTH);
		    		data.addLog("Cart height and width value= \"" + cartHeightWidthField.getText() + "\" is not a number, the default value= \"" + Double.toString(Data.DEFAULT_CART_HEIGHT_AND_WIDTH / CartAndPendulumSystem.NANO_UNITS_PER_MILLI_UNIT) + "\" has been applied");
		    	}
		    }
		    @Override
		    public void changedUpdate(DocumentEvent e) { }
		});
		cartHeightWidthField.setPreferredSize(new Dimension(ClientGui.FRAME_WIDTH / 4, 36));
		
		JTextField pendulumEndWeightMassField = new JTextField(Double.toString(Data.DEFAULT_PENDULUM_END_WEIGHT_MASS));
		pendulumEndWeightMassField.getDocument().addDocumentListener(new DocumentListener() {
		    @Override
		    public void insertUpdate(DocumentEvent e) {
		    	try {
		    		pendulumEndWeightMassField.setForeground(Color.BLACK);
		    		data.setPendulumEndWeightMass(Double.valueOf(pendulumEndWeightMassField.getText()));
		    		data.addLog("Pendulum end weight mass set to: " + pendulumEndWeightMassField.getText() + ", reset the simulation to apply the change to the model");
		    	} catch (Exception ex) {
		    		pendulumEndWeightMassField.setForeground(Color.RED);
		    		data.setPendulumEndWeightMass(Data.DEFAULT_PENDULUM_END_WEIGHT_MASS);
		    		data.addLog("Pendulum end weight mass value= \"" + pendulumEndWeightMassField.getText() + "\" is not a number, the default value= \"" + Double.toString(Data.DEFAULT_PENDULUM_END_WEIGHT_MASS) + "\" has been applied");
		    	}
		    }
		    @Override
		    public void removeUpdate(DocumentEvent e) {
		    	try {
		    		pendulumEndWeightMassField.setForeground(Color.BLACK);
		    		data.setPendulumEndWeightMass(Double.valueOf(pendulumEndWeightMassField.getText()));
		    		data.addLog("Pendulum end weight mass set to: " + pendulumEndWeightMassField.getText() + ", reset the simulation to apply the change to the model");
		    	} catch (Exception ex) {
		    		pendulumEndWeightMassField.setForeground(Color.RED);
		    		data.setPendulumEndWeightMass(Data.DEFAULT_PENDULUM_END_WEIGHT_MASS);
		    		data.addLog("Pendulum end weight mass value= \"" + pendulumEndWeightMassField.getText() + "\" is not a number, the default value= \"" + Double.toString(Data.DEFAULT_PENDULUM_END_WEIGHT_MASS) + "\" has been applied");
		    	}
		    }
		    @Override
		    public void changedUpdate(DocumentEvent e) { }
		});
		pendulumEndWeightMassField.setPreferredSize(new Dimension(ClientGui.FRAME_WIDTH / 4, 36));
		
		JTextField cartMassField = new JTextField(Double.toString(Data.DEFAULT_CART_MASS));
		cartMassField.getDocument().addDocumentListener(new DocumentListener() {
		    @Override
		    public void insertUpdate(DocumentEvent e) {
		    	try {
		    		cartMassField.setForeground(Color.BLACK);
		    		data.setCartMass(Double.valueOf(cartMassField.getText()));
		    		data.addLog("Cart mass set to: " + cartMassField.getText() + ", reset the simulation to apply the change to the model");
		    	} catch (Exception ex) {
		    		cartMassField.setForeground(Color.RED);
		    		data.setCartMass(Data.DEFAULT_CART_MASS);
		    		data.addLog("Cart mass value= \"" + cartMassField.getText() + "\" is not a number, the default value= \"" + Double.toString(Data.DEFAULT_CART_MASS) + "\" has been applied");
		    	}
		    }
		    @Override
		    public void removeUpdate(DocumentEvent e) {
		    	try {
		    		cartMassField.setForeground(Color.BLACK);
		    		data.setCartMass(Double.valueOf(cartMassField.getText()));
		    		data.addLog("Cart mass set to: " + cartMassField.getText() + ", reset the simulation to apply the change to the model");
		    	} catch (Exception ex) {
		    		cartMassField.setForeground(Color.RED);
		    		data.setCartMass(Data.DEFAULT_CART_MASS);
		    		data.addLog("Cart mass value= \"" + cartMassField.getText() + "\" is not a number, the default value= \"" + Double.toString(Data.DEFAULT_CART_MASS) + "\" has been applied");
		    	}
		    }
		    @Override
		    public void changedUpdate(DocumentEvent e) { }
		});
		cartMassField.setPreferredSize(new Dimension(ClientGui.FRAME_WIDTH / 4, 36));
		
		JTextField accelerationDueToGravityField = new JTextField(Double.toString(Data.DEFAULT_ACCELERATION_DUE_TO_GRAVITY * CartAndPendulumSystem.NANO_UNITS_PER_STD_UNIT));
		accelerationDueToGravityField.getDocument().addDocumentListener(new DocumentListener() {
		    @Override
		    public void insertUpdate(DocumentEvent e) {
		    	try {
		    		accelerationDueToGravityField.setForeground(Color.BLACK);
		    		data.setCartMass(Double.valueOf(accelerationDueToGravityField.getText()) / CartAndPendulumSystem.NANO_UNITS_PER_STD_UNIT);
		    		data.addLog("Acceleration due to gravity set to: " + accelerationDueToGravityField.getText() + ", reset the simulation to apply the change to the model");
		    	} catch (Exception ex) {
		    		accelerationDueToGravityField.setForeground(Color.RED);
		    		data.setCartMass(Data.DEFAULT_ACCELERATION_DUE_TO_GRAVITY);
		    		data.addLog("Acceleration due to gravity value= \"" + accelerationDueToGravityField.getText() + "\" is not a number, the default value= \"" + Double.toString(Data.DEFAULT_ACCELERATION_DUE_TO_GRAVITY * CartAndPendulumSystem.NANO_UNITS_PER_STD_UNIT) + "\" has been applied");
		    	}
		    }
		    @Override
		    public void removeUpdate(DocumentEvent e) {
		    	try {
		    		accelerationDueToGravityField.setForeground(Color.BLACK);
		    		data.setCartMass(Double.valueOf(accelerationDueToGravityField.getText()) / CartAndPendulumSystem.NANO_UNITS_PER_STD_UNIT);
		    		data.addLog("Acceleration due to gravity set to: " + accelerationDueToGravityField.getText() + ", reset the simulation to apply the change to the model");
		    	} catch (Exception ex) {
		    		accelerationDueToGravityField.setForeground(Color.RED);
		    		data.setCartMass(Data.DEFAULT_ACCELERATION_DUE_TO_GRAVITY);
		    		data.addLog("Acceleration due to gravity value= \"" + accelerationDueToGravityField.getText() + "\" is not a number, the default value= \"" + Double.toString(Data.DEFAULT_ACCELERATION_DUE_TO_GRAVITY * CartAndPendulumSystem.NANO_UNITS_PER_STD_UNIT) + "\" has been applied");
		    	}
		    }
		    @Override
		    public void changedUpdate(DocumentEvent e) { }
		});
		accelerationDueToGravityField.setPreferredSize(new Dimension(ClientGui.FRAME_WIDTH / 4, 36));
		
		JTextField rollingResistanceField = new JTextField(Double.toString(Data.DEFAULT_ROLLING_RESISTANCE));
		rollingResistanceField.getDocument().addDocumentListener(new DocumentListener() {
		    @Override
		    public void insertUpdate(DocumentEvent e) {
		    	try {
		    		rollingResistanceField.setForeground(Color.BLACK);
		    		data.setRollingResistance(Double.valueOf(rollingResistanceField.getText()));
		    		data.addLog("Rolling resistance coefficient set to: " + rollingResistanceField.getText() + ", reset the simulation to apply the change to the model");
		    	} catch (Exception ex) {
		    		rollingResistanceField.setForeground(Color.RED);
		    		data.setRollingResistance(Data.DEFAULT_ROLLING_RESISTANCE);
		    		data.addLog("Rolling resistance coefficient value= \"" + rollingResistanceField.getText() + "\" is not a number, the default value= \"" + Double.toString(Data.DEFAULT_ROLLING_RESISTANCE) + "\" has been applied");
		    	}
		    }
		    @Override
		    public void removeUpdate(DocumentEvent e) {
		    	try {
		    		rollingResistanceField.setForeground(Color.BLACK);
		    		data.setRollingResistance(Double.valueOf(rollingResistanceField.getText()));
		    		data.addLog("Rolling resistance coefficient set to: " + rollingResistanceField.getText() + ", reset the simulation to apply the change to the model");
		    	} catch (Exception ex) {
		    		rollingResistanceField.setForeground(Color.RED);
		    		data.setRollingResistance(Data.DEFAULT_ROLLING_RESISTANCE);
		    		data.addLog("Rolling resistance coefficient value= \"" + rollingResistanceField.getText() + "\" is not a number, the default value= \"" + Double.toString(Data.DEFAULT_ROLLING_RESISTANCE) + "\" has been applied");
		    	}
		    }
		    @Override
		    public void changedUpdate(DocumentEvent e) { }
		});
		rollingResistanceField.setPreferredSize(new Dimension(ClientGui.FRAME_WIDTH / 4, 36));
		
		JTextField pendulumDragCoefficientField = new JTextField(Double.toString(Data.DEFAULT_PENDULUM_DRAG_COEFFICIENT));
		pendulumDragCoefficientField.getDocument().addDocumentListener(new DocumentListener() {
		    @Override
		    public void insertUpdate(DocumentEvent e) {
		    	try {
		    		pendulumDragCoefficientField.setForeground(Color.BLACK);
		    		data.setPendulumDragCoefficient(Double.valueOf(pendulumDragCoefficientField.getText()));
		    		data.addLog("Pendulum drag coefficient set to: " + pendulumDragCoefficientField.getText() + ", reset the simulation to apply the change to the model");
		    	} catch (Exception ex) {
		    		pendulumDragCoefficientField.setForeground(Color.RED);
		    		data.setPendulumDragCoefficient(Data.DEFAULT_PENDULUM_DRAG_COEFFICIENT);
		    		data.addLog("Pendulum drag coefficient value= \"" + pendulumDragCoefficientField.getText() + "\" is not a number, the default value= \"" + Double.toString(Data.DEFAULT_PENDULUM_DRAG_COEFFICIENT) + "\" has been applied");
		    	}
		    }
		    @Override
		    public void removeUpdate(DocumentEvent e) {
		    	try {
		    		pendulumDragCoefficientField.setForeground(Color.BLACK);
		    		data.setPendulumDragCoefficient(Double.valueOf(pendulumDragCoefficientField.getText()));
		    		data.addLog("Pendulum drag coefficient set to: " + pendulumDragCoefficientField.getText() + ", reset the simulation to apply the change to the model");
		    	} catch (Exception ex) {
		    		pendulumDragCoefficientField.setForeground(Color.RED);
		    		data.setPendulumDragCoefficient(Data.DEFAULT_PENDULUM_DRAG_COEFFICIENT);
		    		data.addLog("Pendulum drag coefficient value= \"" + pendulumDragCoefficientField.getText() + "\" is not a number, the default value= \"" + Double.toString(Data.DEFAULT_PENDULUM_DRAG_COEFFICIENT) + "\" has been applied");
		    	}
		    }
		    @Override
		    public void changedUpdate(DocumentEvent e) { }
		});
		pendulumDragCoefficientField.setPreferredSize(new Dimension(ClientGui.FRAME_WIDTH / 4, 36));
		
		JTextField cartDragCoefficientField = new JTextField(Double.toString(Data.DEFAULT_CART_DRAG_COEFFICIENT));
		cartDragCoefficientField.getDocument().addDocumentListener(new DocumentListener() {
		    @Override
		    public void insertUpdate(DocumentEvent e) {
		    	try {
		    		cartDragCoefficientField.setForeground(Color.BLACK);
		    		data.setCartDragCoefficient(Double.valueOf(cartDragCoefficientField.getText()));
		    		data.addLog("Cart drag coefficient set to: " + cartDragCoefficientField.getText() + ", reset the simulation to apply the change to the model");
		    	} catch (Exception ex) {
		    		cartDragCoefficientField.setForeground(Color.RED);
		    		data.setCartDragCoefficient(Data.DEFAULT_CART_DRAG_COEFFICIENT);
		    		data.addLog("Cart drag coefficient value= \"" + cartDragCoefficientField.getText() + "\" is not a number, the default value= \"" + Double.toString(Data.DEFAULT_CART_DRAG_COEFFICIENT) + "\" has been applied");
		    	}
		    }
		    @Override
		    public void removeUpdate(DocumentEvent e) {
		    	try {
		    		cartDragCoefficientField.setForeground(Color.BLACK);
		    		data.setCartDragCoefficient(Double.valueOf(cartDragCoefficientField.getText()));
		    		data.addLog("Cart drag coefficient set to: " + cartDragCoefficientField.getText() + ", reset the simulation to apply the change to the model");
		    	} catch (Exception ex) {
		    		cartDragCoefficientField.setForeground(Color.RED);
		    		data.setCartDragCoefficient(Data.DEFAULT_CART_DRAG_COEFFICIENT);
		    		data.addLog("Cart drag coefficient value= \"" + cartDragCoefficientField.getText() + "\" is not a number, the default value= \"" + Double.toString(Data.DEFAULT_CART_DRAG_COEFFICIENT) + "\" has been applied");
		    	}
		    }
		    @Override
		    public void changedUpdate(DocumentEvent e) { }
		});
		cartDragCoefficientField.setPreferredSize(new Dimension(ClientGui.FRAME_WIDTH / 4, 36));
		
		JTextField airDensityField = new JTextField(Double.toString(Data.DEFAULT_AIR_DENSITY * (CartAndPendulumSystem.NANO_UNITS_PER_STD_UNIT * CartAndPendulumSystem.NANO_UNITS_PER_STD_UNIT * CartAndPendulumSystem.NANO_UNITS_PER_STD_UNIT)));
		airDensityField.getDocument().addDocumentListener(new DocumentListener() {
		    @Override
		    public void insertUpdate(DocumentEvent e) {
		    	try {
		    		airDensityField.setForeground(Color.BLACK);
		    		data.setAirDensity(Double.valueOf(airDensityField.getText()) / (CartAndPendulumSystem.NANO_UNITS_PER_STD_UNIT * CartAndPendulumSystem.NANO_UNITS_PER_STD_UNIT * CartAndPendulumSystem.NANO_UNITS_PER_STD_UNIT));
		    		data.addLog("Air density set to: " + airDensityField.getText() + ", reset the simulation to apply the change to the model");
		    	} catch (Exception ex) {
		    		airDensityField.setForeground(Color.RED);
		    		data.setAirDensity(Data.DEFAULT_AIR_DENSITY);
		    		data.addLog("Air density value= \"" + airDensityField.getText() + "\" is not a number, the default value= \"" + Double.toString(Data.DEFAULT_AIR_DENSITY * (CartAndPendulumSystem.NANO_UNITS_PER_STD_UNIT * CartAndPendulumSystem.NANO_UNITS_PER_STD_UNIT * CartAndPendulumSystem.NANO_UNITS_PER_STD_UNIT)) + "\" has been applied");
		    	}
		    }
		    @Override
		    public void removeUpdate(DocumentEvent e) {
		    	try {
		    		airDensityField.setForeground(Color.BLACK);
		    		data.setAirDensity(Double.valueOf(airDensityField.getText()) / (CartAndPendulumSystem.NANO_UNITS_PER_STD_UNIT * CartAndPendulumSystem.NANO_UNITS_PER_STD_UNIT * CartAndPendulumSystem.NANO_UNITS_PER_STD_UNIT));
		    		data.addLog("Air density set to: " + airDensityField.getText() + ", reset the simulation to apply the change to the model");
		    	} catch (Exception ex) {
		    		airDensityField.setForeground(Color.RED);
		    		data.setAirDensity(Data.DEFAULT_AIR_DENSITY);
		    		data.addLog("Air density value= \"" + airDensityField.getText() + "\" is not a number, the default value= \"" + Double.toString(Data.DEFAULT_AIR_DENSITY * (CartAndPendulumSystem.NANO_UNITS_PER_STD_UNIT * CartAndPendulumSystem.NANO_UNITS_PER_STD_UNIT * CartAndPendulumSystem.NANO_UNITS_PER_STD_UNIT)) + "\" has been applied");
		    	}
		    }
		    @Override
		    public void changedUpdate(DocumentEvent e) { }
		});
		airDensityField.setPreferredSize(new Dimension(ClientGui.FRAME_WIDTH / 4, 36));
		
		JPanel pendulumLengthHeader = new JPanel() {
			private static final long serialVersionUID = 2139601658327848660L;
			@Override
			public void paintComponent(Graphics g) {
				g.setFont(new Font("", Font.BOLD, 25));
				g.drawString("Pendulum length (mm):", 20, 28);
			}
		};
		pendulumLengthHeader.setPreferredSize(new Dimension(ClientGui.FRAME_WIDTH / 4, 36));
		
		JPanel pendulumEndRadiusHeader = new JPanel() {
			private static final long serialVersionUID = -6101241624230050513L;
			@Override
			public void paintComponent(Graphics g) {
				g.setFont(new Font("", Font.BOLD, 25));
				g.drawString("Pendulum end radius (mm):", 20, 28);
			}
		};
		pendulumEndRadiusHeader.setPreferredSize(new Dimension(ClientGui.FRAME_WIDTH / 4, 36));
		
		JPanel cartHeightWidthHeader = new JPanel() {
			private static final long serialVersionUID = 8833099442554902546L;
			@Override
			public void paintComponent(Graphics g) {
				g.setFont(new Font("", Font.BOLD, 25));
				g.drawString("Cart height and width (mm):", 20, 28);
			}
		};
		cartHeightWidthHeader.setPreferredSize(new Dimension(ClientGui.FRAME_WIDTH / 4, 36));
		
		JPanel pendulumEndWeightMassHeader = new JPanel() {
			private static final long serialVersionUID = 7860491667803043217L;
			@Override
			public void paintComponent(Graphics g) {
				g.setFont(new Font("", Font.BOLD, 25));
				g.drawString("Pendulum end weight mass (kg):", 20, 28);
			}
		};
		pendulumEndWeightMassHeader.setPreferredSize(new Dimension(ClientGui.FRAME_WIDTH / 4, 36));
		
		JPanel cartMassHeader = new JPanel() {
			private static final long serialVersionUID = 6431936617390588945L;
			@Override
			public void paintComponent(Graphics g) {
				g.setFont(new Font("", Font.BOLD, 25));
				g.drawString("Cart mass (kg):", 20, 28);
			}
		};
		cartMassHeader.setPreferredSize(new Dimension(ClientGui.FRAME_WIDTH / 4, 36));
		
		JPanel accelerationDueToGravityHeader = new JPanel() {
			private static final long serialVersionUID = -5835073142951392039L;
			@Override
			public void paintComponent(Graphics g) {
				g.setFont(new Font("", Font.BOLD, 25));
				g.drawString("Acceleration due to gravity (m/s^2):", 20, 28);
			}
		};
		accelerationDueToGravityHeader.setPreferredSize(new Dimension(ClientGui.FRAME_WIDTH / 4, 36));
		
		JPanel rollingResistanceHeader = new JPanel() {
			private static final long serialVersionUID = -7434150790479613155L;
			@Override
			public void paintComponent(Graphics g) {
				g.setFont(new Font("", Font.BOLD, 25));
				g.drawString("Rolling resistance coefficient:", 20, 28);
			}
		};
		rollingResistanceHeader.setPreferredSize(new Dimension(ClientGui.FRAME_WIDTH / 4, 36));
		
		JPanel pendulumDragCoefficientHeader = new JPanel() {
			private static final long serialVersionUID = -3439577390183844868L;
			@Override
			public void paintComponent(Graphics g) {
				g.setFont(new Font("", Font.BOLD, 25));
				g.drawString("Pendulum end drag coefficient:", 20, 28);
			}
		};
		pendulumDragCoefficientHeader.setPreferredSize(new Dimension(ClientGui.FRAME_WIDTH / 4, 36));
		
		JPanel cartDragCoefficientHeader = new JPanel() {
			private static final long serialVersionUID = -2320403582432005727L;
			@Override
			public void paintComponent(Graphics g) {
				g.setFont(new Font("", Font.BOLD, 25));
				g.drawString("Cart drag coefficient:", 20, 28);
			}
		};
		cartDragCoefficientHeader.setPreferredSize(new Dimension(ClientGui.FRAME_WIDTH / 4, 36));
		
		JPanel airDensityHeader = new JPanel() {
			private static final long serialVersionUID = -8876851152982744430L;
			@Override
			public void paintComponent(Graphics g) {
				g.setFont(new Font("", Font.BOLD, 25));
				g.drawString("Air density (kg/m^3):", 20, 28);
			}
		};
		airDensityHeader.setPreferredSize(new Dimension(ClientGui.FRAME_WIDTH / 4, 36));
		
		JPanel physicalParametersPanel = new JPanel();
		physicalParametersPanel.setLayout(new BoxLayout(physicalParametersPanel, BoxLayout.Y_AXIS));
		physicalParametersPanel.add(physicalParametersHeader);
		physicalParametersPanel.add(pendulumLengthHeader);
		physicalParametersPanel.add(pendulumLengthField);
		physicalParametersPanel.add(pendulumEndRadiusHeader);
		physicalParametersPanel.add(pendulumEndRadiusField);
		physicalParametersPanel.add(cartHeightWidthHeader);
		physicalParametersPanel.add(cartHeightWidthField);
		physicalParametersPanel.add(pendulumEndWeightMassHeader);
		physicalParametersPanel.add(pendulumEndWeightMassField);
		physicalParametersPanel.add(cartMassHeader);
		physicalParametersPanel.add(cartMassField);
		physicalParametersPanel.add(accelerationDueToGravityHeader);
		physicalParametersPanel.add(accelerationDueToGravityField);
		physicalParametersPanel.add(rollingResistanceHeader);
		physicalParametersPanel.add(rollingResistanceField);
		physicalParametersPanel.add(pendulumDragCoefficientHeader);
		physicalParametersPanel.add(pendulumDragCoefficientField);
		physicalParametersPanel.add(cartDragCoefficientHeader);
		physicalParametersPanel.add(cartDragCoefficientField);
		physicalParametersPanel.add(airDensityHeader);
		physicalParametersPanel.add(airDensityField);
		physicalParametersPanel.setPreferredSize(new Dimension(ClientGui.FRAME_WIDTH / 4, ClientGui.FRAME_HEIGHT - ConsolePanel.CONSOLE_HEIGHT));
		
		JPanel simulationParametersHeader = new JPanel() {
			private static final long serialVersionUID = -5713198472968784780L;
			@Override
			public void paintComponent(Graphics g) {
				g.setFont(new Font("", Font.BOLD, 35));
				g.drawString("Simulation parameters:", 10, 55);
			}
		};
		simulationParametersHeader.setPreferredSize(new Dimension(ClientGui.FRAME_WIDTH / 4, 80));
		
		JTextField simTickRateField = new JTextField(Double.toString(Data.DEFAULT_SIMULATION_TICK_RATE));
		simTickRateField.getDocument().addDocumentListener(new DocumentListener() {
		    @Override
		    public void insertUpdate(DocumentEvent e) {
		    	try {
		    		simTickRateField.setForeground(Color.BLACK);
		    		data.setTargetSimulationTickRate(Double.valueOf(simTickRateField.getText()));
		    		data.addLog("Physics tick rate set to: " + simTickRateField.getText());
		    	} catch (Exception ex) {
		    		simTickRateField.setForeground(Color.RED);
		    		data.setTargetSimulationTickRate(Data.DEFAULT_SIMULATION_TICK_RATE);
		    		data.addLog("Physics tick rate value= \"" + simTickRateField.getText() + "\" is not a number, the default value= \"" + Double.toString(Data.DEFAULT_SIMULATION_TICK_RATE) + "\" has been applied");
		    	}
		    }
		    @Override
		    public void removeUpdate(DocumentEvent e) {
		    	try {
		    		simTickRateField.setForeground(Color.BLACK);
		    		data.setTargetSimulationTickRate(Double.valueOf(simTickRateField.getText()));
		    		data.addLog("Physics tick rate set to: " + simTickRateField.getText());
		    	} catch (Exception ex) {
		    		simTickRateField.setForeground(Color.RED);
		    		data.setTargetSimulationTickRate(Data.DEFAULT_SIMULATION_TICK_RATE);
		    		data.addLog("Physics tick rate value= \"" + simTickRateField.getText() + "\" is not a number, the default value= \"" + Double.toString(Data.DEFAULT_SIMULATION_TICK_RATE) + "\" has been applied");
		    	}
		    }
		    @Override
		    public void changedUpdate(DocumentEvent e) { }
		});
		simTickRateField.setPreferredSize(new Dimension(ClientGui.FRAME_WIDTH / 4, 36));
		
		JTextField contTickRateField = new JTextField(Double.toString(Data.DEFAULT_CONTROLLER_TICK_RATE));
		contTickRateField.getDocument().addDocumentListener(new DocumentListener() {
		    @Override
		    public void insertUpdate(DocumentEvent e) {
		    	try {
		    		contTickRateField.setForeground(Color.BLACK);
		    		data.setTargetControllerTickRate(Double.valueOf(contTickRateField.getText()));
		    		data.addLog("Controller tick rate set to: " + contTickRateField.getText());
		    	} catch (Exception ex) {
		    		contTickRateField.setForeground(Color.RED);
		    		data.setTargetControllerTickRate(Data.DEFAULT_CONTROLLER_TICK_RATE);
		    		data.addLog("Controller tick rate value= \"" + contTickRateField.getText() + "\" is not a number, the default value= \"" + Double.toString(Data.DEFAULT_CONTROLLER_TICK_RATE) + "\" has been applied");
		    	}
		    }
		    @Override
		    public void removeUpdate(DocumentEvent e) {
		    	try {
		    		contTickRateField.setForeground(Color.BLACK);
		    		data.setTargetControllerTickRate(Double.valueOf(contTickRateField.getText()));
		    		data.addLog("Controller tick rate set to: " + contTickRateField.getText());
		    	} catch (Exception ex) {
		    		contTickRateField.setForeground(Color.RED);
		    		data.setTargetControllerTickRate(Data.DEFAULT_CONTROLLER_TICK_RATE);
		    		data.addLog("Controller tick rate value= \"" + contTickRateField.getText() + "\" is not a number, the default value= \"" + Double.toString(Data.DEFAULT_CONTROLLER_TICK_RATE) + "\" has been applied");
		    	}
		    }
		    @Override
		    public void changedUpdate(DocumentEvent e) { }
		});
		contTickRateField.setPreferredSize(new Dimension(ClientGui.FRAME_WIDTH / 4, 36));
		
		JTextField frameRateField = new JTextField(Double.toString(Data.DEFAULT_FRAME_RATE));
		frameRateField.getDocument().addDocumentListener(new DocumentListener() {
		    @Override
		    public void insertUpdate(DocumentEvent e) {
		    	try {
		    		frameRateField.setForeground(Color.BLACK);
		    		data.setTargetFrameRate(Double.valueOf(frameRateField.getText()));
		    		data.addLog("Frame rate set to: " + frameRateField.getText());
		    	} catch (Exception ex) {
		    		frameRateField.setForeground(Color.RED);
		    		data.setTargetFrameRate(Data.DEFAULT_FRAME_RATE);
		    		data.addLog("Frame rate value= \"" + frameRateField.getText() + "\" is not a number, the default value= \"" + Double.toString(Data.DEFAULT_FRAME_RATE) + "\" has been applied");
		    	}
		    }
		    @Override
		    public void removeUpdate(DocumentEvent e) {
		    	try {
		    		frameRateField.setForeground(Color.BLACK);
		    		data.setTargetFrameRate(Double.valueOf(frameRateField.getText()));
		    		data.addLog("Frame rate set to: " + frameRateField.getText());
		    	} catch (Exception ex) {
		    		frameRateField.setForeground(Color.RED);
		    		data.setTargetFrameRate(Data.DEFAULT_FRAME_RATE);
		    		data.addLog("Frame rate value= \"" + frameRateField.getText() + "\" is not a number, the default value= \"" + Double.toString(Data.DEFAULT_FRAME_RATE) + "\" has been applied");
		    	}
		    }
		    @Override
		    public void changedUpdate(DocumentEvent e) { }
		});
		frameRateField.setPreferredSize(new Dimension(ClientGui.FRAME_WIDTH / 4, 36));
		
		JTextField samplingRateField = new JTextField(Double.toString(Data.DEFAULT_SAMPLING_RATE));
		samplingRateField.getDocument().addDocumentListener(new DocumentListener() {
		    @Override
		    public void insertUpdate(DocumentEvent e) {
		    	try {
		    		samplingRateField.setForeground(Color.BLACK);
		    		data.setTargetSamplingRate(Double.valueOf(samplingRateField.getText()));
		    		data.addLog("Results sampling rate set to: " + samplingRateField.getText());
		    	} catch (Exception ex) {
		    		samplingRateField.setForeground(Color.RED);
		    		data.setTargetSamplingRate(Data.DEFAULT_SAMPLING_RATE);
		    		data.addLog("Results sampling rate value= \"" + samplingRateField.getText() + "\" is not a number, the default value= \"" + Double.toString(Data.DEFAULT_SAMPLING_RATE) + "\" has been applied");
		    	}
		    }
		    @Override
		    public void removeUpdate(DocumentEvent e) {
		    	try {
		    		samplingRateField.setForeground(Color.BLACK);
		    		data.setTargetSamplingRate(Double.valueOf(samplingRateField.getText()));
		    		data.addLog("Results sampling rate set to: " + samplingRateField.getText());
		    	} catch (Exception ex) {
		    		samplingRateField.setForeground(Color.RED);
		    		data.setTargetSamplingRate(Data.DEFAULT_SAMPLING_RATE);
		    		data.addLog("Results sampling rate value= \"" + samplingRateField.getText() + "\" is not a number, the default value= \"" + Double.toString(Data.DEFAULT_SAMPLING_RATE) + "\" has been applied");
		    	}
		    }
		    @Override
		    public void changedUpdate(DocumentEvent e) { }
		});
		samplingRateField.setPreferredSize(new Dimension(ClientGui.FRAME_WIDTH / 4, 36));
		
		JTextField timeScaleField = new JTextField(Double.toString(Data.DEFAULT_RUN_TIME_SCALE));
		timeScaleField.getDocument().addDocumentListener(new DocumentListener() {
		    @Override
		    public void insertUpdate(DocumentEvent e) {
		    	try {
		    		timeScaleField.setForeground(Color.BLACK);
		    		data.setSimulationRunTimeScale(Double.valueOf(timeScaleField.getText()));
		    		data.addLog("Run time scaling set to: " + timeScaleField.getText());
		    	} catch (Exception ex) {
		    		timeScaleField.setForeground(Color.RED);
		    		data.setSimulationRunTimeScale(Data.DEFAULT_RUN_TIME_SCALE);
		    		data.addLog("Run time scaling value= \"" + timeScaleField.getText() + "\" is not a number, the default value= \"" + Double.toString(Data.DEFAULT_RUN_TIME_SCALE) + "\" has been applied");
		    	}
		    }
		    @Override
		    public void removeUpdate(DocumentEvent e) {
		    	try {
		    		timeScaleField.setForeground(Color.BLACK);
		    		data.setSimulationRunTimeScale(Double.valueOf(timeScaleField.getText()));
		    		data.addLog("Run time scaling set to: " + timeScaleField.getText());
		    	} catch (Exception ex) {
		    		timeScaleField.setForeground(Color.RED);
		    		data.setSimulationRunTimeScale(Data.DEFAULT_RUN_TIME_SCALE);
		    		data.addLog("Run time scaling value= \"" + timeScaleField.getText() + "\" is not a number, the default value= \"" + Double.toString(Data.DEFAULT_RUN_TIME_SCALE) + "\" has been applied");
		    	}
		    }
		    @Override
		    public void changedUpdate(DocumentEvent e) { }
		});
		timeScaleField.setPreferredSize(new Dimension(ClientGui.FRAME_WIDTH / 4, 36));
		
		JRadioButton dontIgnoreCartButton = new JRadioButton("Allow cart movement");
		dontIgnoreCartButton.setSelected(true);
		dontIgnoreCartButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (dontIgnoreCartButton.isSelected()) {
					data.setIgnoreCart(false);
					data.addLog("Cart movement is no longer being ignored");
				}
			}
		});
		dontIgnoreCartButton.setPreferredSize(new Dimension(ClientGui.FRAME_WIDTH / 8, 36));
		
		JRadioButton ignoreCartButton = new JRadioButton("Ignore cart movement");
		ignoreCartButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (ignoreCartButton.isSelected()) {
					data.setIgnoreCart(true);
					data.addLog("Cart movement is now being ignored !! NOTE !! Fuzzy rules containing antecendant statements involving the cart WILL NOT FIRE");
				}
			}
		});
		ignoreCartButton.setPreferredSize(new Dimension(ClientGui.FRAME_WIDTH / 8, 36));
		
		ButtonGroup ignoreCartButtonGroup = new ButtonGroup();
		ignoreCartButtonGroup.add(dontIgnoreCartButton);
		ignoreCartButtonGroup.add(ignoreCartButton);
		
		JPanel ignoreCartButtons = new JPanel();
		ignoreCartButtons.setLayout(new BoxLayout(ignoreCartButtons, BoxLayout.X_AXIS));
		ignoreCartButtons.add(dontIgnoreCartButton);
		ignoreCartButtons.add(ignoreCartButton);
		ignoreCartButtons.setPreferredSize(new Dimension(ClientGui.FRAME_WIDTH / 4, 36));
		
		ignoreCartButton.setSelected(Data.DEFAULT_IGNORE_CART);
		dontIgnoreCartButton.setSelected(!Data.DEFAULT_IGNORE_CART);
		
		JRadioButton useNoiseButton = new JRadioButton("Enable noise");
		useNoiseButton.setSelected(true);
		useNoiseButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (useNoiseButton.isSelected()) {
					data.setNoise(true);
					data.addLog("Noise enabled");
				}
			}
		});
		useNoiseButton.setPreferredSize(new Dimension(ClientGui.FRAME_WIDTH / 8, 36));
		
		JRadioButton dontUseNoiseButton = new JRadioButton("Disable noise");
		dontUseNoiseButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (dontUseNoiseButton.isSelected()) {
					data.setNoise(false);
					data.addLog("Noise disabled");
				}
			}
		});
		dontUseNoiseButton.setPreferredSize(new Dimension(ClientGui.FRAME_WIDTH / 8, 36));
		
		useNoiseButton.setSelected(Data.DEFAULT_USE_NOISE);
		dontUseNoiseButton.setSelected(!Data.DEFAULT_USE_NOISE);
		
		
		ButtonGroup useNoiseButtonGroup = new ButtonGroup();
		useNoiseButtonGroup.add(useNoiseButton);
		useNoiseButtonGroup.add(dontUseNoiseButton);
		
		JPanel useNoiseButtons = new JPanel();
		useNoiseButtons.setLayout(new BoxLayout(useNoiseButtons, BoxLayout.X_AXIS));
		useNoiseButtons.add(useNoiseButton);
		useNoiseButtons.add(dontUseNoiseButton);
		useNoiseButtons.setPreferredSize(new Dimension(ClientGui.FRAME_WIDTH / 4, 36));
		
		JTextField positionNoiseField = new JTextField(Double.toString(Data.DEFAULT_POSITION_NOISE));
		positionNoiseField.getDocument().addDocumentListener(new DocumentListener() {
		    @Override
		    public void insertUpdate(DocumentEvent e) {
		    	try {
		    		positionNoiseField.setForeground(Color.BLACK);
		    		data.setPositionNoise(Double.valueOf(positionNoiseField.getText()));
		    		data.addLog("Cart position sensor noise set to: " + positionNoiseField.getText());
		    	} catch (Exception ex) {
		    		positionNoiseField.setForeground(Color.RED);
		    		data.setPositionNoise(Data.DEFAULT_POSITION_NOISE);
		    		data.addLog("Cart position sensor noise value= \"" + positionNoiseField.getText() + "\" is not a number, the default value= \"" + Double.toString(Data.DEFAULT_POSITION_NOISE) + "\" has been applied");
		    	}
		    }
		    @Override
		    public void removeUpdate(DocumentEvent e) {
		    	try {
		    		positionNoiseField.setForeground(Color.BLACK);
		    		data.setPositionNoise(Double.valueOf(positionNoiseField.getText()));
		    		data.addLog("Cart position sensor noise set to: " + positionNoiseField.getText());
		    	} catch (Exception ex) {
		    		positionNoiseField.setForeground(Color.RED);
		    		data.setPositionNoise(Data.DEFAULT_POSITION_NOISE);
		    		data.addLog("Cart position sensor noise value= \"" + positionNoiseField.getText() + "\" is not a number, the default value= \"" + Double.toString(Data.DEFAULT_POSITION_NOISE) + "\" has been applied");
		    	}
		    }
		    @Override
		    public void changedUpdate(DocumentEvent e) { }
		});
		positionNoiseField.setPreferredSize(new Dimension(ClientGui.FRAME_WIDTH / 4, 36));
		
		JTextField angleNoiseField = new JTextField(Double.toString(Data.DEFAULT_ANGLE_NOISE));
		angleNoiseField.getDocument().addDocumentListener(new DocumentListener() {
		    @Override
		    public void insertUpdate(DocumentEvent e) {
		    	try {
		    		angleNoiseField.setForeground(Color.BLACK);
		    		data.setAngleNoise(Double.valueOf(angleNoiseField.getText()));
		    		data.addLog("Pendulum angle sensor noise set to: " + angleNoiseField.getText());
		    	} catch (Exception ex) {
		    		angleNoiseField.setForeground(Color.RED);
		    		data.setAngleNoise(Data.DEFAULT_ANGLE_NOISE);
		    		data.addLog("Pendulum angle sensor noise value= \"" + angleNoiseField.getText() + "\" is not a number, the default value= \"" + Double.toString(Data.DEFAULT_ANGLE_NOISE) + "\" has been applied");
		    	}
		    }
		    @Override
		    public void removeUpdate(DocumentEvent e) {
		    	try {
		    		angleNoiseField.setForeground(Color.BLACK);
		    		data.setAngleNoise(Double.valueOf(angleNoiseField.getText()));
		    		data.addLog("Pendulum angle sensor noise set to: " + angleNoiseField.getText());
		    	} catch (Exception ex) {
		    		angleNoiseField.setForeground(Color.RED);
		    		data.setAngleNoise(Data.DEFAULT_ANGLE_NOISE);
		    		data.addLog("Pendulum angle sensor noise value= \"" + angleNoiseField.getText() + "\" is not a number, the default value= \"" + Double.toString(Data.DEFAULT_ANGLE_NOISE) + "\" has been applied");
		    	}
		    }
		    @Override
		    public void changedUpdate(DocumentEvent e) { }
		});
		angleNoiseField.setPreferredSize(new Dimension(ClientGui.FRAME_WIDTH / 4, 36));
		
		JTextField forceNoiseField = new JTextField(Double.toString(Data.DEFAULT_FORCE_NOISE));
		forceNoiseField.getDocument().addDocumentListener(new DocumentListener() {
		    @Override
		    public void insertUpdate(DocumentEvent e) {
		    	try {
		    		forceNoiseField.setForeground(Color.BLACK);
		    		data.setForceNoise(Double.valueOf(forceNoiseField.getText()));
		    		data.addLog("Cart force motor noise set to: " + forceNoiseField.getText());
		    	} catch (Exception ex) {
		    		forceNoiseField.setForeground(Color.RED);
		    		data.setForceNoise(Data.DEFAULT_FORCE_NOISE);
		    		data.addLog("Cart force motor noise value= \"" + forceNoiseField.getText() + "\" is not a number, the default value= \"" + Double.toString(Data.DEFAULT_FORCE_NOISE) + "\" has been applied");
		    	}
		    }
		    @Override
		    public void removeUpdate(DocumentEvent e) {
		    	try {
		    		forceNoiseField.setForeground(Color.BLACK);
		    		data.setForceNoise(Double.valueOf(forceNoiseField.getText()));
		    		data.addLog("Cart force motor noise set to: " + forceNoiseField.getText());
		    	} catch (Exception ex) {
		    		forceNoiseField.setForeground(Color.RED);
		    		data.setForceNoise(Data.DEFAULT_FORCE_NOISE);
		    		data.addLog("Cart force motor noise value= \"" + forceNoiseField.getText() + "\" is not a number, the default value= \"" + Double.toString(Data.DEFAULT_FORCE_NOISE) + "\" has been applied");
		    	}
		    }
		    @Override
		    public void changedUpdate(DocumentEvent e) { }
		});
		forceNoiseField.setPreferredSize(new Dimension(ClientGui.FRAME_WIDTH / 4, 36));
		
		JPanel simTickRateHeader = new JPanel() {
			private static final long serialVersionUID = -8439884086333457472L;
			@Override
			public void paintComponent(Graphics g) {
				g.setFont(new Font("", Font.BOLD, 25));
				g.drawString("Physics tick rate:", 20, 28);
			}
		};
		simTickRateHeader.setPreferredSize(new Dimension(ClientGui.FRAME_WIDTH / 4, 36));
		
		JPanel contTickRateHeader = new JPanel() {
			private static final long serialVersionUID = -8439884086333457472L;
			@Override
			public void paintComponent(Graphics g) {
				g.setFont(new Font("", Font.BOLD, 25));
				g.drawString("Controller tick rate:", 20, 28);
			}
		};
		contTickRateHeader.setPreferredSize(new Dimension(ClientGui.FRAME_WIDTH / 4, 36));
		
		JPanel frameRateHeader = new JPanel() {
			private static final long serialVersionUID = -5382070015505987744L;
			@Override
			public void paintComponent(Graphics g) {
				g.setFont(new Font("", Font.BOLD, 25));
				g.drawString("Frame rate:", 20, 28);
			}
		};
		frameRateHeader.setPreferredSize(new Dimension(ClientGui.FRAME_WIDTH / 4, 36));
		
		JPanel samplingRateHeader = new JPanel() {
			private static final long serialVersionUID = -2108713227479420385L;
			@Override
			public void paintComponent(Graphics g) {
				g.setFont(new Font("", Font.BOLD, 25));
				g.drawString("Result sampling rate:", 20, 28);
			}
		};
		samplingRateHeader.setPreferredSize(new Dimension(ClientGui.FRAME_WIDTH / 4, 36));
		
		JPanel timeScaleHeader = new JPanel() {
			private static final long serialVersionUID = -4626839757142773264L;
			@Override
			public void paintComponent(Graphics g) {
				g.setFont(new Font("", Font.BOLD, 25));
				g.drawString("Run time scaling:", 20, 28);
			}
		};
		timeScaleHeader.setPreferredSize(new Dimension(ClientGui.FRAME_WIDTH / 4, 36));
		
		JPanel ignoreCartHeader = new JPanel() {
			private static final long serialVersionUID = 4437388772275460140L;
			@Override
			public void paintComponent(Graphics g) {
				g.setFont(new Font("", Font.BOLD, 25));
				g.drawString("Allow or ignore cart movement:", 20, 28);
			}
		};
		ignoreCartHeader.setPreferredSize(new Dimension(ClientGui.FRAME_WIDTH / 4, 36));
		
		JPanel useNoiseHeader = new JPanel() {
			private static final long serialVersionUID = 4437388772275460140L;
			@Override
			public void paintComponent(Graphics g) {
				g.setFont(new Font("", Font.BOLD, 25));
				g.drawString("Noise:", 20, 28);
			}
		};
		useNoiseHeader.setPreferredSize(new Dimension(ClientGui.FRAME_WIDTH / 4, 36));
		
		JPanel angleNoiseHeader = new JPanel() {
			private static final long serialVersionUID = 3526042551005285683L;
			@Override
			public void paintComponent(Graphics g) {
				g.setFont(new Font("", Font.BOLD, 25));
				g.drawString("Pendulum angle sensor noise:", 20, 28);
			}
		};
		angleNoiseHeader.setPreferredSize(new Dimension(ClientGui.FRAME_WIDTH / 4, 36));
		
		JPanel positionNoiseHeader = new JPanel() {
			private static final long serialVersionUID = 7907807239345831931L;
			@Override
			public void paintComponent(Graphics g) {
				g.setFont(new Font("", Font.BOLD, 25));
				g.drawString("Cart position sensor noise:", 20, 28);
			}
		};
		positionNoiseHeader.setPreferredSize(new Dimension(ClientGui.FRAME_WIDTH / 4, 36));
		
		JPanel forceNoiseHeader = new JPanel() {
			private static final long serialVersionUID = 8508407231420442393L;
			@Override
			public void paintComponent(Graphics g) {
				g.setFont(new Font("", Font.BOLD, 25));
				g.drawString("Cart force motor noise:", 20, 28);
			}
		};
		forceNoiseHeader.setPreferredSize(new Dimension(ClientGui.FRAME_WIDTH / 4, 36));
		
		JPanel simulationParametersPanel = new JPanel();
		simulationParametersPanel.setLayout(new BoxLayout(simulationParametersPanel, BoxLayout.Y_AXIS));
		simulationParametersPanel.add(simulationParametersHeader);
		simulationParametersPanel.add(simTickRateHeader);
		simulationParametersPanel.add(simTickRateField);
		simulationParametersPanel.add(contTickRateHeader);
		simulationParametersPanel.add(contTickRateField);
		simulationParametersPanel.add(frameRateHeader);
		simulationParametersPanel.add(frameRateField);
		simulationParametersPanel.add(samplingRateHeader);
		simulationParametersPanel.add(samplingRateField);
		simulationParametersPanel.add(timeScaleHeader);
		simulationParametersPanel.add(timeScaleField);
		simulationParametersPanel.add(ignoreCartHeader);
		simulationParametersPanel.add(ignoreCartButtons);
		simulationParametersPanel.add(useNoiseHeader);
		simulationParametersPanel.add(useNoiseButtons);
		simulationParametersPanel.add(angleNoiseHeader);
		simulationParametersPanel.add(angleNoiseField);
		simulationParametersPanel.add(positionNoiseHeader);
		simulationParametersPanel.add(positionNoiseField);
		simulationParametersPanel.add(forceNoiseHeader);
		simulationParametersPanel.add(forceNoiseField);
		simulationParametersPanel.setPreferredSize(new Dimension(ClientGui.FRAME_WIDTH / 4, ClientGui.FRAME_HEIGHT - ConsolePanel.CONSOLE_HEIGHT));
		
		JPanel variablesPanel = new JPanel();
		variablesPanel.setLayout(new GridLayout(1, 2));
		variablesPanel.add(physicalParametersPanel);
		variablesPanel.add(simulationParametersPanel);
		variablesPanel.setPreferredSize(new Dimension(ClientGui.FRAME_WIDTH, ClientGui.FRAME_HEIGHT - ConsolePanel.CONSOLE_HEIGHT));
		
		this.add(variablesPanel);
	}
	
	private void populateAndAddBottomPanel() {
		JButton simulationViewButton = new JButton("To simulation");
		simulationViewButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					data.setDesiredViewState(ViewState.SIMULATION_VIEW);
				}
			});
		simulationViewButton.setPreferredSize(new Dimension(200, 100));
		
		JButton homeViewButton = new JButton("To home");
		homeViewButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					data.setDesiredViewState(ViewState.LOAD_VIEW);
				}
			});
		homeViewButton.setPreferredSize(new Dimension(200, 100));
		
		JPanel buttonsPanel = new JPanel();
		buttonsPanel.setLayout(new GridLayout(2, 1));
		buttonsPanel.add(simulationViewButton);
		buttonsPanel.add(homeViewButton);
		buttonsPanel.setPreferredSize(new Dimension(200, 200));
		
		JPanel bottomPanel = new JPanel();
		bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.X_AXIS));
		bottomPanel.add(buttonsPanel);
		bottomPanel.add(console);
		bottomPanel.setPreferredSize(new Dimension(ClientGui.FRAME_WIDTH, ConsolePanel.CONSOLE_HEIGHT));
		
		this.add(bottomPanel);
	}
}
