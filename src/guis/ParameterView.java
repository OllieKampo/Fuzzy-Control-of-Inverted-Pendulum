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
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import main.Client;
import main.Data;
import main.FuzzyCartAndPendulumSystem;
import main.PIDCartAndPendulumSystem;

public class ParameterView extends JPanel implements Observer {
	private static final long serialVersionUID = -7190291229611198271L;
	
	private final Client client;
	private final Data data;
	private final ConsolePanel console;
	private volatile boolean changing;

	public ParameterView(Client client) {
		this.client = client;
		this.data = client.getData();
		this.console = new ConsolePanel(client);
		this.changing = true;
		
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.populateAndAddParametersPanel();
		this.populateAndAddBottomPanel();
		this.setPreferredSize(new Dimension(ClientGui.FRAME_WIDTH, ClientGui.FRAME_HEIGHT));
	}
	
	@Override
	public void update(Observable o, Object arg) {
		this.console.update(o, arg);
	}
	
	private void populateAndAddParametersPanel() {
		JPanel fuzzyParametersHeader = new JPanel() {
			private static final long serialVersionUID = -5713198472968784780L;
			@Override
			public void paintComponent(Graphics g) {
				g.setFont(new Font("", Font.BOLD, 35));
				g.drawString("Fuzzy control parameters:", 10, 55);
			}
		};
		fuzzyParametersHeader.setPreferredSize(new Dimension(ClientGui.FRAME_WIDTH / 3, 80));
		
		JTextField fuzzyAddRuleField = new JTextField();
		fuzzyAddRuleField.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String log = client.getFuzzySystem().addFuzzyRule(fuzzyAddRuleField.getText());
				if (log.contains("Added") || log.contains("Replaced")) {
					fuzzyAddRuleField.setText("");
				}
				data.addLog(log);
			}
		});
		fuzzyAddRuleField.getDocument().addDocumentListener(new DocumentListener() {
		    @Override
		    public void insertUpdate(DocumentEvent e) {
		    	if (client.getFuzzySystem().isUsableRule(fuzzyAddRuleField.getText())) {
		    		fuzzyAddRuleField.setForeground(Color.BLACK);
		    	} else {
		    		fuzzyAddRuleField.setForeground(Color.RED);
		    	}
		    }
		    @Override
		    public void removeUpdate(DocumentEvent e) {
		    	if (client.getFuzzySystem().isUsableRule(fuzzyAddRuleField.getText())) {
		    		fuzzyAddRuleField.setForeground(Color.BLACK);
		    	} else {
		    		fuzzyAddRuleField.setForeground(Color.RED);
		    	}
		    }
		    @Override
		    public void changedUpdate(DocumentEvent e) { }
		});
		fuzzyAddRuleField.setPreferredSize(new Dimension(ClientGui.FRAME_WIDTH / 3, 60));
		
		JList<String> fuzzyRuleList = new JList<>();
		fuzzyRuleList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		fuzzyRuleList.setLayoutOrientation(JList.VERTICAL);
		fuzzyRuleList.setVisibleRowCount(-1);
		fuzzyRuleList.setListData(FuzzyCartAndPendulumSystem.DEFAULT_FUZZY_RULES);
		JScrollPane fuzzyRuleListScrollPane = new JScrollPane(fuzzyRuleList);
		fuzzyRuleListScrollPane.setPreferredSize(new Dimension(ClientGui.FRAME_WIDTH / 3, 480));
		
		JButton addReplaceButton = new JButton("Add / replace");
		addReplaceButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String log = client.getFuzzySystem().addFuzzyRule(fuzzyAddRuleField.getText());
				if (log.contains("Added") || log.contains("Replaced")) {
					fuzzyAddRuleField.setText("");
					fuzzyRuleList.setListData(client.getFuzzySystem().getFuzzyRuleList());
				}
				data.addLog(log);
			}
		});
		addReplaceButton.setPreferredSize(new Dimension(ClientGui.FRAME_WIDTH / 12, 60));
		
		JButton removeButton = new JButton("Remove");
		removeButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String log = client.getFuzzySystem().removeFuzzyRule(fuzzyAddRuleField.getText());
				if (log.contains("Removed")) {
					fuzzyAddRuleField.setText("");
					fuzzyRuleList.setListData(client.getFuzzySystem().getFuzzyRuleList());
				}
				data.addLog(log);
			}
		});
		removeButton.setPreferredSize(new Dimension(ClientGui.FRAME_WIDTH / 12, 60));
		
		JButton editButton = new JButton("Edit selected");
		editButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String s = fuzzyRuleList.getSelectedValue();
				if (s != null) {
					fuzzyAddRuleField.setText(s);
				} else {
					data.addLog("No rule selected to edit");
				}
			}
		});
		editButton.setPreferredSize(new Dimension(ClientGui.FRAME_WIDTH / 12, 60));
		
		JPanel buttonsPanel = new JPanel();
		buttonsPanel.setLayout(new GridLayout(1, 3));
		buttonsPanel.add(addReplaceButton);
		buttonsPanel.add(removeButton);
		buttonsPanel.add(editButton);
		buttonsPanel.setPreferredSize(new Dimension(ClientGui.FRAME_WIDTH / 3, 60));
		
		JPanel fuzzyAddRuleHeader = new JPanel() {
			private static final long serialVersionUID = -8666257409331381387L;
			@Override
			public void paintComponent(Graphics g) {
				g.setFont(new Font("", Font.BOLD, 25));
				g.drawString("Add, remove or replace fuzzy rule:", 20, 40);
			}
		};
		fuzzyAddRuleHeader.setPreferredSize(new Dimension(ClientGui.FRAME_WIDTH / 3, 60));
		
		JPanel fuzzyRuleListHeader = new JPanel() {
			private static final long serialVersionUID = -4951628621767244980L;
			@Override
			public void paintComponent(Graphics g) {
				g.setFont(new Font("", Font.BOLD, 25));
				g.drawString("Fuzzy rule list:", 20, 40);
			}
		};
		fuzzyRuleListHeader.setPreferredSize(new Dimension(ClientGui.FRAME_WIDTH / 3, 60));
		
		JPanel fuzzyPanelLeft = new JPanel();
		fuzzyPanelLeft.setLayout(new BoxLayout(fuzzyPanelLeft, BoxLayout.Y_AXIS));
		fuzzyPanelLeft.add(fuzzyParametersHeader);
		fuzzyPanelLeft.add(fuzzyAddRuleHeader);
		fuzzyPanelLeft.add(fuzzyAddRuleField);
		fuzzyPanelLeft.add(buttonsPanel);
		fuzzyPanelLeft.add(fuzzyRuleListHeader);
		fuzzyPanelLeft.add(fuzzyRuleListScrollPane);
		fuzzyPanelLeft.setPreferredSize(new Dimension(ClientGui.FRAME_WIDTH / 3, ClientGui.FRAME_HEIGHT - ConsolePanel.CONSOLE_HEIGHT));
		
		JPanel blankHeader = new JPanel();
		blankHeader.setPreferredSize(new Dimension(ClientGui.FRAME_WIDTH / 3, 80));
		
		JPanel memberFunctionsHeader = new JPanel() {
			private static final long serialVersionUID = 4367690185919675868L;
			@Override
			public void paintComponent(Graphics g) {
				g.setFont(new Font("", Font.BOLD, 25));
				g.drawString("Member functions:", 10, 40);
			}
		};
		memberFunctionsHeader.setPreferredSize(new Dimension(40, 60));
		
		FunctionDisplay functionDisplay = new FunctionDisplay(FuzzyCartAndPendulumSystem.DEFAULT_MEMBER_FUNCTIONS);
		
		JPanel mfStartDisplay = new JPanel(){
			private static final long serialVersionUID = -5660977020471543982L;
			@Override
			public void paintComponent(Graphics g) {
				g.drawString("Start:", 5, 35);
			}
		};
		mfStartDisplay.setPreferredSize(new Dimension(65, 60));
		
		JPanel mfFirstPeekDisplay = new JPanel(){
			private static final long serialVersionUID = 227396700389224654L;
			@Override
			public void paintComponent(Graphics g) {
				g.drawString("1st peek:", 5, 35);
			}
		};
		mfFirstPeekDisplay.setPreferredSize(new Dimension(65, 60));
		
		JPanel mfSecondPeekDisplay = new JPanel(){
			private static final long serialVersionUID = 6617516304888360688L;
			@Override
			public void paintComponent(Graphics g) {
				g.drawString("2nd peek:", 5, 35);
			}
		};
		mfSecondPeekDisplay.setPreferredSize(new Dimension(65, 60));
		
		JPanel mfEndDisplay = new JPanel(){
			private static final long serialVersionUID = -1119172798040748029L;
			@Override
			public void paintComponent(Graphics g) {
				g.drawString("End:", 5, 35);
			}
		};
		mfEndDisplay.setPreferredSize(new Dimension(65, 60));
		
		JTextField mfEndField = new JTextField();
		mfEndField.getDocument().addDocumentListener(new DocumentListener() {
		    @Override
		    public void insertUpdate(DocumentEvent e) {
	    		if (!changing) {
			    	try {
				    	if (Double.valueOf(mfEndField.getText()) > 1.0) {
				    		mfEndField.setForeground(Color.RED);
				    		data.addLog("End value for member functions must be less than or equal to one");
				    	} else {
				    		mfEndField.setForeground(Color.BLACK);
				    	}
			    	} catch (Exception ex) {
			    		mfEndField.setForeground(Color.RED);
			    		data.addLog("Specified second peek value for member functions is not a number");
			    	}
	    		}
		    }
		    @Override
		    public void removeUpdate(DocumentEvent e) {
	    		if (!changing) {
			    	try {
				    	if (Double.valueOf(mfEndField.getText()) > 1.0) {
				    		mfEndField.setForeground(Color.RED);
				    		data.addLog("End value for member functions must be less than or equal to one");
				    	} else {
				    		mfEndField.setForeground(Color.BLACK);
				    	}
			    	} catch (Exception ex) {
			    		mfEndField.setForeground(Color.RED);
			    		data.addLog("Specified second peek value for member functions is not a number");
			    	}
	    		}
		    }
		    @Override
		    public void changedUpdate(DocumentEvent e) { }
		});
		mfEndField.setPreferredSize(new Dimension(85, 60));
		
		JTextField mfSecondPeekField = new JTextField();
		mfSecondPeekField.getDocument().addDocumentListener(new DocumentListener() {
		    @Override
		    public void insertUpdate(DocumentEvent e) {
	    		if (!changing) {
			    	try {
			    		if (Double.valueOf(mfSecondPeekField.getText()) > Double.valueOf(mfEndField.getText())) {
				    		mfSecondPeekField.setForeground(Color.RED);
				    		mfEndField.setForeground(Color.RED);
				    		data.addLog("Second peek value for member functions must be less than or equal to end value");
				    	} else {
				    		mfSecondPeekField.setForeground(Color.BLACK);
				    	}
			    	} catch (Exception ex) {
			    		mfSecondPeekField.setForeground(Color.RED);
			    		data.addLog("Specified second peek value for member functions is not a number");
			    	}
	    		}
		    }
		    @Override
		    public void removeUpdate(DocumentEvent e) {
	    		if (!changing) {
			    	try {
			    		if (Double.valueOf(mfSecondPeekField.getText()) > Double.valueOf(mfEndField.getText())) {
				    		mfSecondPeekField.setForeground(Color.RED);
				    		mfEndField.setForeground(Color.RED);
				    		data.addLog("Second peek value for member functions must be less than or equal to end value");
				    	} else {
				    		mfSecondPeekField.setForeground(Color.BLACK);
				    	}
			    	} catch (Exception ex) {
			    		mfSecondPeekField.setForeground(Color.RED);
			    		data.addLog("Specified second peek value for member functions is not a number");
			    	}
	    		}
		    }
		    @Override
		    public void changedUpdate(DocumentEvent e) { }
		});
		mfSecondPeekField.setPreferredSize(new Dimension(85, 60));
		
		JTextField mfFirstPeekField = new JTextField();
		mfFirstPeekField.getDocument().addDocumentListener(new DocumentListener() {
		    @Override
		    public void insertUpdate(DocumentEvent e) {
	    		if (!changing) {
			    	try {
			    		if (Double.valueOf(mfFirstPeekField.getText()) > Double.valueOf(mfSecondPeekField.getText())) {
				    		mfFirstPeekField.setForeground(Color.RED);
				    		mfSecondPeekField.setForeground(Color.RED);
				    		data.addLog("First peek value for member functions must be less than or equal to second peek value");
				    	} else {
				    		mfFirstPeekField.setForeground(Color.BLACK);
				    	}
			    	} catch (Exception ex) {
			    		mfFirstPeekField.setForeground(Color.RED);
			    		data.addLog("Specified first peek value for member functions is not a number");
			    	}
	    		}
		    }
		    @Override
		    public void removeUpdate(DocumentEvent e) {
	    		if (!changing) {
			    	try {
			    		if (Double.valueOf(mfFirstPeekField.getText()) > Double.valueOf(mfSecondPeekField.getText())) {
				    		mfFirstPeekField.setForeground(Color.RED);
				    		mfSecondPeekField.setForeground(Color.RED);
				    		data.addLog("First peek value for member functions must be less than or equal to second peek value");
				    	} else {
				    		mfFirstPeekField.setForeground(Color.BLACK);
				    	}
			    	} catch (Exception ex) {
			    		mfFirstPeekField.setForeground(Color.RED);
			    		data.addLog("Specified first peek value for member functions is not a number");
			    	}
	    		}
		    }
		    @Override
		    public void changedUpdate(DocumentEvent e) { }
		});
		mfFirstPeekField.setPreferredSize(new Dimension(85, 60));
		
		JTextField mfStartField = new JTextField();
		mfStartField.getDocument().addDocumentListener(new DocumentListener() {
		    @Override
		    public void insertUpdate(DocumentEvent e) {
	    		if (!changing) {
			    	try {
				    	if (Double.valueOf(mfStartField.getText()) < 0.0) {
				    		mfStartField.setForeground(Color.RED);
				    		data.addLog("Start value for member functions must be greater than or equal to zero");
				    	} else if (Double.valueOf(mfStartField.getText()) > Double.valueOf(mfFirstPeekField.getText())) {
				    		mfStartField.setForeground(Color.RED);
				    		mfFirstPeekField.setForeground(Color.RED);
				    		data.addLog("Start value for member functions must be less than or equal to first peek value");
				    	} else {
				    		mfStartField.setForeground(Color.BLACK);
				    	}
			    	} catch (Exception ex) {
			    		mfStartField.setForeground(Color.RED);
			    		data.addLog("Specified start value for member functions is not a number");
			    	}
	    		}
		    }
		    @Override
		    public void removeUpdate(DocumentEvent e) {
	    		if (!changing) {
			    	try {
				    	if (Double.valueOf(mfStartField.getText()) < 0.0) {
				    		mfStartField.setForeground(Color.RED);
				    		data.addLog("Start value for member functions must be greater than or equal to zero");
				    	} else if (Double.valueOf(mfStartField.getText()) > Double.valueOf(mfFirstPeekField.getText())) {
				    		mfStartField.setForeground(Color.RED);
				    		mfFirstPeekField.setForeground(Color.RED);
				    		data.addLog("Start value for member functions must be less than or equal to first peek value");
				    	} else {
				    		mfStartField.setForeground(Color.BLACK);
				    	}
			    	} catch (Exception ex) {
			    		mfStartField.setForeground(Color.RED);
			    		data.addLog("Specified start value for member functions is not a number");
			    	}
	    		}
		    }
		    @Override
		    public void changedUpdate(DocumentEvent e) { }
		});
		mfStartField.setPreferredSize(new Dimension(85, 60));
		
		JPanel memberFunctionEditingFields = new JPanel();
		memberFunctionEditingFields.setLayout(new BoxLayout(memberFunctionEditingFields, BoxLayout.X_AXIS));
		memberFunctionEditingFields.add(mfStartDisplay);
		memberFunctionEditingFields.add(mfStartField);
		memberFunctionEditingFields.add(mfFirstPeekDisplay);
		memberFunctionEditingFields.add(mfFirstPeekField);
		memberFunctionEditingFields.add(mfSecondPeekDisplay);
		memberFunctionEditingFields.add(mfSecondPeekField);
		memberFunctionEditingFields.add(mfEndDisplay);
		memberFunctionEditingFields.add(mfEndField);
		memberFunctionEditingFields.setPreferredSize(new Dimension(ClientGui.FRAME_WIDTH / 3, 60));
		
		JComboBox<String> mfEditSelectionList = new JComboBox<>(FuzzyCartAndPendulumSystem.DEFAULT_MEMBER_FUNCTION_NAMES_WITH_COLOURS);
		mfEditSelectionList.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				FuzzyCartAndPendulumSystem.MemberFunction[] mfs = client.getFuzzySystem().getCopyOfCurrentMemberFunctions();
				for (int i = 0; i < mfs.length; i++) {
					if (mfs[i].getName().equals(((String)mfEditSelectionList.getSelectedItem()).split(" ")[0])) {
						changing = true;
						mfStartField.setText(Double.toString(mfs[i].getStart()));
						mfFirstPeekField.setText(Double.toString(mfs[i].getFirstPeek()));
						mfSecondPeekField.setText(Double.toString(mfs[i].getSecondPeek()));
						mfEndField.setText(Double.toString(mfs[i].getEnd()));
						changing = false;
					}
				}
			}
		});
		mfEditSelectionList.setSelectedIndex(0);
		mfEditSelectionList.setPreferredSize(new Dimension(ClientGui.FRAME_WIDTH / 6, 60));
		
		JButton mfEditSelectionButton = new JButton("Change values");
		mfEditSelectionButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					double start = Double.valueOf(mfStartField.getText());
					double firstPeek = Double.valueOf(mfFirstPeekField.getText());
					double secondPeek = Double.valueOf(mfSecondPeekField.getText());
					double end = Double.valueOf(mfEndField.getText());
					if (start >= 0.0 && firstPeek >= start && secondPeek >= firstPeek && end >= secondPeek && end <= 1.0) {
						client.getFuzzySystem().editMemberFunction(((String)mfEditSelectionList.getSelectedItem()).split(" ")[0], start, firstPeek, secondPeek, end);
						functionDisplay.updateMemberFunctions(client.getFuzzySystem().getCopyOfCurrentMemberFunctions());
					}
		    	} catch (Exception ex) {
		    		data.addLog("Member function cannot be redefined as a value is invalid");
		    	}
			}
		});
		mfEditSelectionButton.setPreferredSize(new Dimension(ClientGui.FRAME_WIDTH / 6, 60));
		
		JPanel memberFunctionEditingSelection = new JPanel();
		memberFunctionEditingSelection.setLayout(new GridLayout(1, 2));
		memberFunctionEditingSelection.add(mfEditSelectionList);
		memberFunctionEditingSelection.add(mfEditSelectionButton);
		memberFunctionEditingSelection.setPreferredSize(new Dimension(ClientGui.FRAME_WIDTH / 3, 60));
		
		JPanel hedgesHeader = new JPanel() {
			private static final long serialVersionUID = -1857484504205787856L;
			@Override
			public void paintComponent(Graphics g) {
				g.setFont(new Font("", Font.BOLD, 25));
				g.drawString("Hedges:", 20, 40);
			}
		};
		hedgesHeader.setPreferredSize(new Dimension(ClientGui.FRAME_WIDTH / 3, 60));
		
		HedgeDisplay hedgeDisplay = new HedgeDisplay(FuzzyCartAndPendulumSystem.DEFAULT_HEDGES);
		
		JPanel hStartDisplay = new JPanel(){
			private static final long serialVersionUID = 8665954616464374045L;
			@Override
			public void paintComponent(Graphics g) {
				g.drawString("Start:", 5, 35);
			}
		};
		hStartDisplay.setPreferredSize(new Dimension(65, 60));
		
		JPanel hFirstPeekDisplay = new JPanel(){
			private static final long serialVersionUID = 4367690185919675868L;
			@Override
			public void paintComponent(Graphics g) {
				g.drawString("1st peek:", 5, 35);
			}
		};
		hFirstPeekDisplay.setPreferredSize(new Dimension(65, 60));
		
		JPanel hSecondPeekDisplay = new JPanel(){
			private static final long serialVersionUID = -1857484504205787856L;
			@Override
			public void paintComponent(Graphics g) {
				g.drawString("2nd peek:", 5, 35);
			}
		};
		hSecondPeekDisplay.setPreferredSize(new Dimension(65, 60));
		
		JPanel hEndDisplay = new JPanel(){
			private static final long serialVersionUID = 5735570990111220842L;
			@Override
			public void paintComponent(Graphics g) {
				g.drawString("End:", 5, 35);
			}
		};
		hEndDisplay.setPreferredSize(new Dimension(65, 60));
		
		JTextField hEndField = new JTextField();
		hEndField.getDocument().addDocumentListener(new DocumentListener() {
		    @Override
		    public void insertUpdate(DocumentEvent e) {
	    		if (!changing) {
			    	try {
				    	if (Double.valueOf(hEndField.getText()) > 1.0) {
				    		hEndField.setForeground(Color.RED);
				    		data.addLog("End value for hedges must be less than or equal to one");
				    	} else {
				    		hEndField.setForeground(Color.BLACK);
				    	}
			    	} catch (Exception ex) {
			    		hEndField.setForeground(Color.RED);
			    		data.addLog("Specified second peek value for hedges is not a number");
			    	}
	    		}
		    }
		    @Override
		    public void removeUpdate(DocumentEvent e) {
	    		if (!changing) {
			    	try {
				    	if (Double.valueOf(hEndField.getText()) > 1.0) {
				    		hEndField.setForeground(Color.RED);
				    		data.addLog("End value for hedges must be less than or equal to one");
				    	} else {
				    		hEndField.setForeground(Color.BLACK);
				    	}
			    	} catch (Exception ex) {
			    		hEndField.setForeground(Color.RED);
			    		data.addLog("Specified second peek value for hedges is not a number");
			    	}
	    		}
		    }
		    @Override
		    public void changedUpdate(DocumentEvent e) { }
		});
		hEndField.setPreferredSize(new Dimension(85, 60));
		
		JTextField hSecondPeekField = new JTextField();
		hSecondPeekField.getDocument().addDocumentListener(new DocumentListener() {
		    @Override
		    public void insertUpdate(DocumentEvent e) {
	    		if (!changing) {
			    	try {
			    		if (Double.valueOf(hSecondPeekField.getText()) > Double.valueOf(hEndField.getText())) {
				    		hSecondPeekField.setForeground(Color.RED);
				    		hEndField.setForeground(Color.RED);
				    		data.addLog("Second peek value for hedges must be less than or equal to end value");
				    	} else {
				    		hSecondPeekField.setForeground(Color.BLACK);
				    	}
			    	} catch (Exception ex) {
			    		hSecondPeekField.setForeground(Color.RED);
			    		data.addLog("Specified second peek value for hedges is not a number");
			    	}
	    		}
		    }
		    @Override
		    public void removeUpdate(DocumentEvent e) {
	    		if (!changing) {
			    	try {
			    		if (Double.valueOf(hSecondPeekField.getText()) > Double.valueOf(hEndField.getText())) {
				    		hSecondPeekField.setForeground(Color.RED);
				    		hEndField.setForeground(Color.RED);
				    		data.addLog("Second peek value for hedges must be less than or equal to end value");
				    	} else {
				    		hSecondPeekField.setForeground(Color.BLACK);
				    	}
			    	} catch (Exception ex) {
			    		hSecondPeekField.setForeground(Color.RED);
			    		data.addLog("Specified second peek value for hedges is not a number");
			    	}
	    		}
		    }
		    @Override
		    public void changedUpdate(DocumentEvent e) { }
		});
		hSecondPeekField.setPreferredSize(new Dimension(85, 60));
		
		JTextField hFirstPeekField = new JTextField();
		hFirstPeekField.getDocument().addDocumentListener(new DocumentListener() {
		    @Override
		    public void insertUpdate(DocumentEvent e) {
	    		if (!changing) {
			    	try {
			    		if (Double.valueOf(hFirstPeekField.getText()) > Double.valueOf(hSecondPeekField.getText())) {
				    		hFirstPeekField.setForeground(Color.RED);
				    		hSecondPeekField.setForeground(Color.RED);
				    		data.addLog("First peek value for hedges must be less than or equal to second peek value");
				    	} else {
				    		hFirstPeekField.setForeground(Color.BLACK);
				    	}
			    	} catch (Exception ex) {
			    		hFirstPeekField.setForeground(Color.RED);
			    		data.addLog("Specified first peek value for hedges is not a number");
			    	}
	    		}
		    }
		    @Override
		    public void removeUpdate(DocumentEvent e) {
	    		if (!changing) {
			    	try {
			    		if (Double.valueOf(hFirstPeekField.getText()) > Double.valueOf(hSecondPeekField.getText())) {
				    		hFirstPeekField.setForeground(Color.RED);
				    		hSecondPeekField.setForeground(Color.RED);
				    		data.addLog("First peek value for hedges must be less than or equal to second peek value");
				    	} else {
				    		hFirstPeekField.setForeground(Color.BLACK);
				    	}
			    	} catch (Exception ex) {
			    		hFirstPeekField.setForeground(Color.RED);
			    		data.addLog("Specified first peek value for hedges is not a number");
			    	}
	    		}
		    }
		    @Override
		    public void changedUpdate(DocumentEvent e) { }
		});
		hFirstPeekField.setPreferredSize(new Dimension(85, 60));
		
		JTextField hStartField = new JTextField();
		hStartField.getDocument().addDocumentListener(new DocumentListener() {
		    @Override
		    public void insertUpdate(DocumentEvent e) {
	    		if (!changing) {
			    	try {
				    	if (Double.valueOf(hStartField.getText()) < 0.0) {
				    		hStartField.setForeground(Color.RED);
				    		data.addLog("Start value for hedges must be greater than or equal to zero");
				    	} else if (Double.valueOf(hStartField.getText()) > Double.valueOf(hFirstPeekField.getText())) {
				    		hStartField.setForeground(Color.RED);
				    		hFirstPeekField.setForeground(Color.RED);
				    		data.addLog("Start value for hedges must be less than or equal to first peek value");
				    	} else {
				    		hStartField.setForeground(Color.BLACK);
				    	}
			    	} catch (Exception ex) {
			    		hStartField.setForeground(Color.RED);
			    		data.addLog("Specified start value for hedges is not a number");
			    	}
	    		}
		    }
		    @Override
		    public void removeUpdate(DocumentEvent e) {
	    		if (!changing) {
			    	try {
				    	if (Double.valueOf(hStartField.getText()) < 0.0) {
				    		hStartField.setForeground(Color.RED);
				    		data.addLog("Start value for hedges must be greater than or equal to zero");
				    	} else if (Double.valueOf(hStartField.getText()) > Double.valueOf(hFirstPeekField.getText())) {
				    		hStartField.setForeground(Color.RED);
				    		hFirstPeekField.setForeground(Color.RED);
				    		data.addLog("Start value for hedges must be less than or equal to first peek value");
				    	} else {
				    		hStartField.setForeground(Color.BLACK);
				    	}
			    	} catch (Exception ex) {
			    		hStartField.setForeground(Color.RED);
			    		data.addLog("Specified start value for hedges is not a number");
			    	}
	    		}
		    }
		    @Override
		    public void changedUpdate(DocumentEvent e) { }
		});
		hStartField.setPreferredSize(new Dimension(85, 60));
		
		JPanel hedgeEditingFields = new JPanel();
		hedgeEditingFields.setLayout(new BoxLayout(hedgeEditingFields, BoxLayout.X_AXIS));
		hedgeEditingFields.add(hStartDisplay);
		hedgeEditingFields.add(hStartField);
		hedgeEditingFields.add(hFirstPeekDisplay);
		hedgeEditingFields.add(hFirstPeekField);
		hedgeEditingFields.add(hSecondPeekDisplay);
		hedgeEditingFields.add(hSecondPeekField);
		hedgeEditingFields.add(hEndDisplay);
		hedgeEditingFields.add(hEndField);
		hedgeEditingFields.setPreferredSize(new Dimension(ClientGui.FRAME_WIDTH / 3, 60));
		
		JComboBox<String> hEditSelectionList = new JComboBox<>(FuzzyCartAndPendulumSystem.DEFAULT_HEDGE_NAMES_WITH_COLOURS);
		hEditSelectionList.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				FuzzyCartAndPendulumSystem.Hedge[] hs = client.getFuzzySystem().getCopyOfCurrentHedges();
				for (int i = 0; i < hs.length; i++) {
					if (hs[i].getName().equals(((String)hEditSelectionList.getSelectedItem()).split(" ")[0])) {
						changing = true;
						hStartField.setText(Double.toString(hs[i].getStart()));
						hFirstPeekField.setText(Double.toString(hs[i].getFirstPeek()));
						hSecondPeekField.setText(Double.toString(hs[i].getSecondPeek()));
						hEndField.setText(Double.toString(hs[i].getEnd()));
						changing = false;
					}
				}
			}
		});
		hEditSelectionList.setSelectedIndex(0);
		hEditSelectionList.setPreferredSize(new Dimension(ClientGui.FRAME_WIDTH / 6, 60));
		
		JButton hEditSelectionButton = new JButton("Change values");
		hEditSelectionButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					double start = Double.valueOf(hStartField.getText());
					double firstPeek = Double.valueOf(hFirstPeekField.getText());
					double secondPeek = Double.valueOf(hSecondPeekField.getText());
					double end = Double.valueOf(hEndField.getText());
					if (start >= 0.0 && firstPeek >= start && secondPeek >= firstPeek && end >= secondPeek && end <= 1.0) {
						client.getFuzzySystem().editHedge(((String)hEditSelectionList.getSelectedItem()).split(" ")[0], start, firstPeek, secondPeek, end);
						hedgeDisplay.updateHedges(client.getFuzzySystem().getCopyOfCurrentHedges());
					}
		    	} catch (Exception ex) {
		    		data.addLog("Hedge cannot be redefined as a value is invalid");
		    	}
			}
		});
		hEditSelectionButton.setPreferredSize(new Dimension(ClientGui.FRAME_WIDTH / 6, 60));
		
		JPanel hedgeEditingSelection = new JPanel();
		hedgeEditingSelection.setLayout(new GridLayout(1, 2));
		hedgeEditingSelection.add(hEditSelectionList);
		hedgeEditingSelection.add(hEditSelectionButton);
		hedgeEditingSelection.setPreferredSize(new Dimension(ClientGui.FRAME_WIDTH / 3, 60));
		
		JPanel fuzzyPanelRight = new JPanel();
		fuzzyPanelRight.setLayout(new BoxLayout(fuzzyPanelRight, BoxLayout.Y_AXIS));
		fuzzyPanelRight.add(blankHeader);
		fuzzyPanelRight.add(memberFunctionsHeader);
		fuzzyPanelRight.add(memberFunctionEditingSelection);
		fuzzyPanelRight.add(memberFunctionEditingFields);
		fuzzyPanelRight.add(functionDisplay);
		fuzzyPanelRight.add(hedgesHeader);
		fuzzyPanelRight.add(hedgeEditingSelection);
		fuzzyPanelRight.add(hedgeEditingFields);
		fuzzyPanelRight.add(hedgeDisplay);
		fuzzyPanelRight.setPreferredSize(new Dimension(ClientGui.FRAME_WIDTH / 3, ClientGui.FRAME_HEIGHT - ConsolePanel.CONSOLE_HEIGHT));
		
		JPanel pidParametersHeader = new JPanel() {
			private static final long serialVersionUID = -5713198472968784780L;
			@Override
			public void paintComponent(Graphics g) {
				g.setFont(new Font("", Font.BOLD, 35));
				g.drawString("PID control parameters:", 10, 55);
			}
		};
		pidParametersHeader.setPreferredSize(new Dimension(ClientGui.FRAME_WIDTH / 3, 80));
		
		JTextField pGpField = new JTextField(Double.toString(PIDCartAndPendulumSystem.DEFAULT_PGP));
		pGpField.getDocument().addDocumentListener(new DocumentListener() {
		    @Override
		    public void insertUpdate(DocumentEvent e) {
		    	try {
		    		pGpField.setForeground(Color.BLACK);
		    		client.getPIDSystem().setPendulumAngleProportionalGain(Double.valueOf(pGpField.getText()));
		    		data.addLog("Pendulum angle proportional gain value set to: " + pGpField.getText());
		    	} catch (Exception ex) {
		    		pGpField.setForeground(Color.RED);
		    		client.getPIDSystem().setPendulumAngleProportionalGain(PIDCartAndPendulumSystem.DEFAULT_PGP);
		    		data.addLog("Pendulum angle proportional gain value= \"" + pGpField.getText() + "\" is not a number, the default gain value= \"" + Double.toString(PIDCartAndPendulumSystem.DEFAULT_PGP) + "\" has been applied");
		    	}
		    }
		    @Override
		    public void removeUpdate(DocumentEvent e) {
		    	try {
		    		pGpField.setForeground(Color.BLACK);
		    		client.getPIDSystem().setPendulumAngleProportionalGain(Double.valueOf(pGpField.getText()));
		    		data.addLog("Pendulum angle proportional gain value set to: " + pGpField.getText());
		    	} catch (Exception ex) {
		    		pGpField.setForeground(Color.RED);
		    		client.getPIDSystem().setPendulumAngleProportionalGain(PIDCartAndPendulumSystem.DEFAULT_PGP);
		    		data.addLog("Pendulum angle proportional gain value= \"" + pGpField.getText() + "\" is not a number, the default gain value= \"" + Double.toString(PIDCartAndPendulumSystem.DEFAULT_PGP) + "\" has been applied");
		    	}
		    }
		    @Override
		    public void changedUpdate(DocumentEvent e) { }
		});
		pGpField.setPreferredSize(new Dimension(ClientGui.FRAME_WIDTH / 3, 60));
		
		JTextField iGpField = new JTextField(Double.toString(PIDCartAndPendulumSystem.DEFAULT_IGP));
		iGpField.getDocument().addDocumentListener(new DocumentListener() {
		    @Override
		    public void insertUpdate(DocumentEvent e) {
		    	try {
		    		iGpField.setForeground(Color.BLACK);
		    		client.getPIDSystem().setPendulumAngleIntegralGain(Double.valueOf(iGpField.getText()));
		    		data.addLog("Pendulum angle integral gain value set to: " + iGpField.getText());
		    	} catch (Exception ex) {
		    		iGpField.setForeground(Color.RED);
		    		client.getPIDSystem().setPendulumAngleIntegralGain(PIDCartAndPendulumSystem.DEFAULT_IGP);
		    		data.addLog("Pendulum angle integral gain value= \"" + iGpField.getText() + "\" is not a number, the default gain value= \"" + Double.toString(PIDCartAndPendulumSystem.DEFAULT_IGP) + "\" has been applied");
		    	}
		    }
		    @Override
		    public void removeUpdate(DocumentEvent e) {
		    	try {
		    		iGpField.setForeground(Color.BLACK);
		    		client.getPIDSystem().setPendulumAngleIntegralGain(Double.valueOf(iGpField.getText()));
		    		data.addLog("Pendulum angle integral gain value set to: " + iGpField.getText());
		    	} catch (Exception ex) {
		    		iGpField.setForeground(Color.RED);
		    		client.getPIDSystem().setPendulumAngleIntegralGain(PIDCartAndPendulumSystem.DEFAULT_IGP);
		    		data.addLog("Pendulum angle integral gain value= \"" + iGpField.getText() + "\" is not a number, the default gain value= \"" + Double.toString(PIDCartAndPendulumSystem.DEFAULT_IGP) + "\" has been applied");
		    	}
		    }
		    @Override
		    public void changedUpdate(DocumentEvent e) { }
		});
		iGpField.setPreferredSize(new Dimension(ClientGui.FRAME_WIDTH / 3, 60));
		
		JTextField dGpField = new JTextField(Double.toString(PIDCartAndPendulumSystem.DEFAULT_DGP));
		dGpField.getDocument().addDocumentListener(new DocumentListener() {
		    @Override
		    public void insertUpdate(DocumentEvent e) {
		    	try {
		    		dGpField.setForeground(Color.BLACK);
		    		client.getPIDSystem().setPendulumAngleDerivativeGain(Double.valueOf(dGpField.getText()));
		    		data.addLog("Pendulum angle derivative gain value set to: " + dGpField.getText());
		    	} catch (Exception ex) {
		    		dGpField.setForeground(Color.RED);
		    		client.getPIDSystem().setPendulumAngleDerivativeGain(PIDCartAndPendulumSystem.DEFAULT_DGP);
		    		data.addLog("Pendulum angle derivative gain value= \"" + dGpField.getText() + "\" is not a number, the default gain value= \"" + Double.toString(PIDCartAndPendulumSystem.DEFAULT_DGP) + "\" has been applied");
		    	}
		    }
		    @Override
		    public void removeUpdate(DocumentEvent e) {
		    	try {
		    		dGpField.setForeground(Color.BLACK);
		    		client.getPIDSystem().setPendulumAngleDerivativeGain(Double.valueOf(dGpField.getText()));
		    		data.addLog("Pendulum angle derivative gain value set to: " + dGpField.getText());
		    	} catch (Exception ex) {
		    		dGpField.setForeground(Color.RED);
		    		client.getPIDSystem().setPendulumAngleDerivativeGain(PIDCartAndPendulumSystem.DEFAULT_DGP);
		    		data.addLog("Pendulum angle derivative gain value= \"" + dGpField.getText() + "\" is not a number, the default gain value= \"" + Double.toString(PIDCartAndPendulumSystem.DEFAULT_DGP) + "\" has been applied");
		    	}
		    }
		    @Override
		    public void changedUpdate(DocumentEvent e) { }
		});
		dGpField.setPreferredSize(new Dimension(ClientGui.FRAME_WIDTH / 3, 60));
		
		JTextField pGcField = new JTextField(Double.toString(PIDCartAndPendulumSystem.DEFAULT_PGC));
		pGcField.getDocument().addDocumentListener(new DocumentListener() {
		    @Override
		    public void insertUpdate(DocumentEvent e) {
		    	try {
		    		pGcField.setForeground(Color.BLACK);
		    		client.getPIDSystem().setCartPositionProportionalGain(Double.valueOf(pGcField.getText()));
		    		data.addLog("Cart position proportional gain value set to: " + pGcField.getText());
		    	} catch (Exception ex) {
		    		pGcField.setForeground(Color.RED);
		    		client.getPIDSystem().setCartPositionProportionalGain(PIDCartAndPendulumSystem.DEFAULT_PGC);
		    		data.addLog("Cart position proportional gain value= \"" + pGcField.getText() + "\" is not a number, the default gain value= \"" + Double.toString(PIDCartAndPendulumSystem.DEFAULT_PGC) + "\" has been applied");
		    	}
		    }
		    @Override
		    public void removeUpdate(DocumentEvent e) {
		    	try {
		    		pGcField.setForeground(Color.BLACK);
		    		client.getPIDSystem().setCartPositionProportionalGain(Double.valueOf(pGcField.getText()));
		    		data.addLog("Cart position proportional gain value set to: " + pGcField.getText());
		    	} catch (Exception ex) {
		    		pGcField.setForeground(Color.RED);
		    		client.getPIDSystem().setCartPositionProportionalGain(PIDCartAndPendulumSystem.DEFAULT_PGC);
		    		data.addLog("Cart position proportional gain value= \"" + pGcField.getText() + "\" is not a number, the default gain value= \"" + Double.toString(PIDCartAndPendulumSystem.DEFAULT_PGC) + "\" has been applied");
		    	}
		    }
		    @Override
		    public void changedUpdate(DocumentEvent e) { }
		});
		pGcField.setPreferredSize(new Dimension(ClientGui.FRAME_WIDTH / 3, 60));
		
		JTextField iGcField = new JTextField(Double.toString(PIDCartAndPendulumSystem.DEFAULT_IGC));
		iGcField.getDocument().addDocumentListener(new DocumentListener() {
		    @Override
		    public void insertUpdate(DocumentEvent e) {
		    	try {
		    		iGcField.setForeground(Color.BLACK);
		    		client.getPIDSystem().setCartPositionIntegralGain(Double.valueOf(iGcField.getText()));
		    		data.addLog("Cart position integral gain value set to: " + iGcField.getText());
		    	} catch (Exception ex) {
		    		iGcField.setForeground(Color.RED);
		    		client.getPIDSystem().setCartPositionIntegralGain(PIDCartAndPendulumSystem.DEFAULT_IGC);
		    		data.addLog("Cart position integral gain value= \"" + iGcField.getText() + "\" is not a number, the default gain value= \"" + Double.toString(PIDCartAndPendulumSystem.DEFAULT_IGC) + "\" has been applied");
		    	}
		    }
		    @Override
		    public void removeUpdate(DocumentEvent e) {
		    	try {
		    		iGcField.setForeground(Color.BLACK);
		    		client.getPIDSystem().setCartPositionIntegralGain(Double.valueOf(iGcField.getText()));
		    		data.addLog("Cart position integral gain value set to: " + iGcField.getText());
		    	} catch (Exception ex) {
		    		iGcField.setForeground(Color.RED);
		    		client.getPIDSystem().setCartPositionIntegralGain(PIDCartAndPendulumSystem.DEFAULT_IGC);
		    		data.addLog("Cart position integral gain value= \"" + iGcField.getText() + "\" is not a number, the default gain value= \"" + Double.toString(PIDCartAndPendulumSystem.DEFAULT_IGC) + "\" has been applied");
		    	}
		    }
		    @Override
		    public void changedUpdate(DocumentEvent e) { }
		});
		iGcField.setPreferredSize(new Dimension(ClientGui.FRAME_WIDTH / 3, 60));
		
		JTextField dGcField = new JTextField(Double.toString(PIDCartAndPendulumSystem.DEFAULT_DGC));
		dGcField.getDocument().addDocumentListener(new DocumentListener() {
		    @Override
		    public void insertUpdate(DocumentEvent e) {
		    	try {
		    		dGcField.setForeground(Color.BLACK);
		    		client.getPIDSystem().setCartPositionDerivativeGain(Double.valueOf(dGcField.getText()));
		    		data.addLog("Cart position derivative gain value set to: " + dGcField.getText());
		    	} catch (Exception ex) {
		    		dGcField.setForeground(Color.RED);
		    		client.getPIDSystem().setCartPositionDerivativeGain(PIDCartAndPendulumSystem.DEFAULT_DGC);
		    		data.addLog("Cart position derivative gain value= \"" + dGcField.getText() + "\" is not a number, the default gain value= \"" + Double.toString(PIDCartAndPendulumSystem.DEFAULT_DGC) + "\" has been applied");
		    	}
		    }
		    @Override
		    public void removeUpdate(DocumentEvent e) {
		    	try {
		    		dGcField.setForeground(Color.BLACK);
		    		client.getPIDSystem().setCartPositionDerivativeGain(Double.valueOf(dGcField.getText()));
		    		data.addLog("Cart position derivative gain value set to: " + dGcField.getText());
		    	} catch (Exception ex) {
		    		dGcField.setForeground(Color.RED);
		    		client.getPIDSystem().setCartPositionDerivativeGain(PIDCartAndPendulumSystem.DEFAULT_DGC);
		    		data.addLog("Cart position derivative gain value= \"" + dGcField.getText() + "\" is not a number, the default gain value= \"" + Double.toString(PIDCartAndPendulumSystem.DEFAULT_DGC) + "\" has been applied");
		    	}
		    }
		    @Override
		    public void changedUpdate(DocumentEvent e) { }
		});
		dGcField.setPreferredSize(new Dimension(ClientGui.FRAME_WIDTH / 3, 60));
		
		JPanel pGpHeader = new JPanel() {
			private static final long serialVersionUID = -6620218822520146340L;
			@Override
			public void paintComponent(Graphics g) {
				g.setFont(new Font("", Font.BOLD, 25));
				g.drawString("Pendulum angle proportional gain:", 20, 40);
			}
		};
		pGpHeader.setPreferredSize(new Dimension(ClientGui.FRAME_WIDTH / 3, 60));
		
		JPanel iGpHeader = new JPanel() {
			private static final long serialVersionUID = -8069698374756010681L;
			@Override
			public void paintComponent(Graphics g) {
				g.setFont(new Font("", Font.BOLD, 25));
				g.drawString("Pendulum angle integral gain:", 20, 40);
			}
		};
		iGpHeader.setPreferredSize(new Dimension(ClientGui.FRAME_WIDTH / 3, 60));
		
		JPanel dGpHeader = new JPanel() {
			private static final long serialVersionUID = -8013128003657738873L;
			@Override
			public void paintComponent(Graphics g) {
				g.setFont(new Font("", Font.BOLD, 25));
				g.drawString("Pendulum angle derivative gain:", 20, 40);
			}
		};
		dGpHeader.setPreferredSize(new Dimension(ClientGui.FRAME_WIDTH / 3, 60));
		
		JPanel pGcHeader = new JPanel() {
			private static final long serialVersionUID = -6630564942971423789L;
			@Override
			public void paintComponent(Graphics g) {
				g.setFont(new Font("", Font.BOLD, 25));
				g.drawString("Cart position proportional gain:", 20, 40);
			}
		};
		pGcHeader.setPreferredSize(new Dimension(ClientGui.FRAME_WIDTH / 3, 60));
		
		JPanel iGcHeader = new JPanel() {
			private static final long serialVersionUID = -6595023357431384803L;
			@Override
			public void paintComponent(Graphics g) {
				g.setFont(new Font("", Font.BOLD, 25));
				g.drawString("Cart position integral gain:", 20, 40);
			}
		};
		iGcHeader.setPreferredSize(new Dimension(ClientGui.FRAME_WIDTH / 3, 60));
		
		JPanel dGcHeader = new JPanel() {
			private static final long serialVersionUID = 4828855429145256988L;
			@Override
			public void paintComponent(Graphics g) {
				g.setFont(new Font("", Font.BOLD, 25));
				g.drawString("Cart position derivative gain:", 20, 40);
			}
		};
		dGcHeader.setPreferredSize(new Dimension(ClientGui.FRAME_WIDTH / 3, 60));
		
		JPanel pidPanel = new JPanel();
		pidPanel.setLayout(new BoxLayout(pidPanel, BoxLayout.Y_AXIS));
		pidPanel.add(pidParametersHeader);
		pidPanel.add(pGpHeader);
		pidPanel.add(pGpField);
		pidPanel.add(iGpHeader);
		pidPanel.add(iGpField);
		pidPanel.add(dGpHeader);
		pidPanel.add(dGpField);
		pidPanel.add(pGcHeader);
		pidPanel.add(pGcField);
		pidPanel.add(iGcHeader);
		pidPanel.add(iGcField);
		pidPanel.add(dGcHeader);
		pidPanel.add(dGcField);
		pidPanel.setPreferredSize(new Dimension(ClientGui.FRAME_WIDTH / 3, ClientGui.FRAME_HEIGHT - ConsolePanel.CONSOLE_HEIGHT));
		
		JPanel parametersPanel = new JPanel();
		parametersPanel.setLayout(new GridLayout(1, 3));
		parametersPanel.add(fuzzyPanelLeft);
		parametersPanel.add(fuzzyPanelRight);
		parametersPanel.add(pidPanel);
		parametersPanel.setPreferredSize(new Dimension(ClientGui.FRAME_WIDTH, ClientGui.FRAME_HEIGHT - ConsolePanel.CONSOLE_HEIGHT));
		
		this.add(parametersPanel);
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
