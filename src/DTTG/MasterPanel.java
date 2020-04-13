package DTTG;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

import DataSource.*;

public class MasterPanel extends DTTGModule {
	ArrayList<JTextField> jtfDay = new ArrayList<JTextField>();
	ArrayList<JTextField> jtfHour = new ArrayList<JTextField>();

	JComboBox jcbStud;
	JTextField name = new JTextField();
	JTextField gname = new JTextField();
	JTextField type = new JTextField();
	JTextField pppw = new JTextField();
	JTextField tppw = new JTextField();
	JTextField strength = new JTextField();
	JTextField capacity = new JTextField();
	JSpinner daySpin = new JSpinner();
	JSpinner hourSpin = new JSpinner();

	JPanel gPanel, masterPanel, deptPanel, buttonPanel;
	JList commonList;
	JTextArea commonListDisplay;

	JButton showBtn, cancelBtn, createBtn, addBtn, editBtn, removeBtn;
	JTabbedPane tabbedpane;
	JTable tableView;
	JScrollPane scrollpane, tableAggregate;

	final int INITIAL_ROWHEIGHT = 20;
	String[] tchnames = { "Teacher Name" };
	String[] studnames = { "Student Name", "No.of Students" };
	String[] roomnames = { "Room Name", "Room Type", "Room Capacity" };
	String[] subjnames = { "Subject Name", "No.of Theory/Week","No.of Practical/Week" };

	Vector<String> tmpList = new Vector<String>();
	Vector<String> dataList = new Vector<String>();
	Vector<Object[]> dataTable = new Vector<Object[]>();

	/**
	 * MasterData Constructor
	 */
	public MasterPanel(DTTG dttg) {
		super(dttg, "", "");
		JButton tchB, subjB, studB, roomB, timeB;

		InitJTF();
		InitTimeJTF();
		tmpList = new Vector<String>();
		dataList = new Vector<String>();
		dataTable = new Vector<Object[]>();

		commonList = new JList();
		commonListDisplay = new JTextArea();
		commonListDisplay.setColumns(20);
		commonListDisplay.setEditable(false);
		commonListDisplay.setSize(100, 200);
		
		commonList.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent arg0) {
				if(commonList.getSelectedIndex()>-1)
				commonListDisplay.setText(dataList.get(commonList.getSelectedIndex()));
			}
		});
		

		// %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
		// Data Entry
		// %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
		masterPanel = new JPanel();
		deptPanel = new JPanel();
		buttonPanel = new JPanel();

		masterPanel.setLayout(new BorderLayout());
		deptPanel.setLayout(new BorderLayout());
		buttonPanel.setLayout(new GridLayout(1, 4));
		masterPanel.add(buttonPanel, BorderLayout.NORTH);
		masterPanel.add(deptPanel, BorderLayout.CENTER);

		tchB = new JButton(getString("MasterData.tch"));
		subjB = new JButton(getString("MasterData.subj"));
		studB = new JButton(getString("MasterData.stud"));
		roomB = new JButton(getString("MasterData.room"));
		timeB = new JButton(getString("MasterData.time"));

		buttonPanel.add(tchB);
		buttonPanel.add(subjB);
		buttonPanel.add(studB);
		buttonPanel.add(roomB);
		buttonPanel.add(timeB);

		tchB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				InitJTF();
				deptPanel.removeAll();
				deptPanel.add(getTeacharContent(getString("MasterData.tch")),BorderLayout.CENTER);
				deptPanel.updateUI();
				createTable(DataSet.TEACHER, tchnames);
			}
		});

		subjB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				InitJTF();
				deptPanel.removeAll();
				deptPanel.add(getSubjectContent(getString("MasterData.subj")),BorderLayout.CENTER);
				deptPanel.updateUI();
				createTable(DataSet.SUBJECT, subjnames);
			}
		});

		studB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				InitJTF();
				deptPanel.removeAll();
				deptPanel.add(getStudentContent(getString("MasterData.stud")),BorderLayout.CENTER);
				deptPanel.updateUI();
				createTable(DataSet.STUDENT, studnames);
			}
		});

		roomB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				InitJTF();
				deptPanel.removeAll();
				deptPanel.add(getRoomContent(getString("MasterData.room")),BorderLayout.CENTER);
				deptPanel.updateUI();
				createTable(DataSet.ROOM, roomnames);
			}
		});

		timeB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				InitTimeJTF();
				deptPanel.removeAll();
				deptPanel.add(getTimeContent(getString("MasterData.time")),BorderLayout.CENTER);
				deptPanel.updateUI();
				ValidateTime();
			}
		});
	}

	public JPanel getMasterPanel() {
		return masterPanel;
	}

	public JPanel getTimeContent(String title) {
		JPanel TimeContentPanel = new JPanel();

		JPanel btnPanel;
		JButton addBtn = new JButton("Add");
		JPanel dayPanel = new JPanel();
		JPanel hourPanel = new JPanel();
		JPanel spinPanel = new JPanel();

		GridBagConstraints c = new GridBagConstraints();

		dayPanel.setBorder(new TitledBorder(" Days : "));
		hourPanel.setBorder(new TitledBorder(" Hours : "));
		spinPanel.setBorder(new LineBorder(Color.WHITE, 2));
		TimeContentPanel.setLayout(new GridBagLayout());
		dayPanel.setLayout(new GridLayout(3, 2, 10, 10));
		hourPanel.setLayout(new GridLayout(4, 3, 10, 10));
		spinPanel.setLayout(new GridBagLayout());

		InitTimeJTF();
		c.anchor = GridBagConstraints.CENTER;
		c.insets = new Insets(20, 10, 20, 10);
		for (int i = 0; i < DataSet.MAXDAY; i++) {
			btnPanel = new JPanel();
			btnPanel.setLayout(new GridBagLayout());
			btnPanel.add(new JLabel("Day " + (i + 1) + ": "), c);
			btnPanel.add(jtfDay.get(i), c);
			dayPanel.add(btnPanel);
		}
		c.insets = new Insets(2, 1, 2, 1);
		for (int i = 0; i < DataSet.MAXHOUR; i++) {
			btnPanel = new JPanel();
			btnPanel.setLayout(new GridBagLayout());
			btnPanel.add(new JLabel("Hour " + (i + 1) + ": "), c);
			btnPanel.add(jtfHour.get(i), c);
			hourPanel.add(btnPanel);
		}

		// new SpinnerNumberModel(value, min, max, step);
		daySpin.setModel(new SpinnerNumberModel(new Integer(1), new Integer(1),new Integer(DataSet.MAXDAY), new Integer(1)));
		hourSpin.setModel(new SpinnerNumberModel(new Integer(1), new Integer(1), new Integer(DataSet.MAXHOUR), new Integer(1)));

		daySpin.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				EnableJTF(jtfDay, Integer.parseInt(daySpin.getValue().toString()));
			}
		});

		hourSpin.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				EnableJTF(jtfHour, Integer.parseInt(hourSpin.getValue().toString()));
			}
		});

		addBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				UpadateTime();
			}
		});

		c.gridx = 1;
		c.gridy = 1;
		spinPanel.add(new JLabel("No.of Days/Week : "), c);
		c.gridy = 10;
		spinPanel.add(daySpin, c);
		c.gridy = 20;
		spinPanel.add(new JLabel("No.of Hours/Day : "), c);
		c.gridy = 30;
		spinPanel.add(hourSpin, c);
		c.gridy = 40;
		spinPanel.add(addBtn, c);

		c.gridx = 1;
		c.gridy = 1;
		c.anchor = GridBagConstraints.WEST;
		TimeContentPanel.add(dayPanel, c);
		c.anchor = GridBagConstraints.LINE_END;
		TimeContentPanel.add(spinPanel, c);
		c.gridx = 1;
		c.gridy = 10;
		c.anchor = GridBagConstraints.WEST;
		TimeContentPanel.add(hourPanel, c);

		return TimeContentPanel;
	}

	public void ValidateTime() {
		if (!getDTTGData().getTimeSet().getDay().isEmpty()) {
			for (int i = 0; i < getDTTGData().getTimeSet().getDay().size(); i++) {
				jtfDay.get(i).setEnabled(true);
				jtfDay.get(i).setBackground(Color.WHITE);
				jtfDay.get(i).setText(getDTTGData().getTimeSet().getDay().get(i));
			}
			daySpin.setValue(getDTTGData().getTimeSet().getDay().size());
		}
		if (!getDTTGData().getTimeSet().getHour().isEmpty()) {
			for (int i = 0; i < getDTTGData().getTimeSet().getHour().size(); i++) {
				jtfHour.get(i).setEnabled(true);
				jtfHour.get(i).setBackground(Color.WHITE);
				jtfHour.get(i).setText(getDTTGData().getTimeSet().getHour().get(i));
			}
			hourSpin.setValue(getDTTGData().getTimeSet().getHour().size());
		}
	}

	public void UpadateTime() {
		getDTTGData().getTimeSet().getDay().removeAllElements();
		getDTTGData().getTimeSet().getHour().removeAllElements();
		for (int i = 0; i < jtfDay.size(); i++)
			if (jtfDay.get(i).isEnabled())
				getDTTGData().getTimeSet().getDay().addElement(jtfDay.get(i).getText());
		for (int i = 0; i < jtfHour.size(); i++)
			if (jtfHour.get(i).isEnabled())
				getDTTGData().getTimeSet().getHour().addElement(jtfHour.get(i).getText());
	}

	private void InitTimeJTF() {
		for (int i = 0; i < DataSet.MAXDAY; i++) {
			jtfDay.add(new JTextField());
			jtfDay.get(i).setColumns(10);
			jtfDay.get(i).setBackground(Color.LIGHT_GRAY);
			jtfDay.get(i).setEnabled(false);
		}

		for (int i = 0; i < DataSet.MAXHOUR; i++) {
			jtfHour.add(new JTextField());
			jtfHour.get(i).setColumns(10);
			jtfHour.get(i).setBackground(Color.LIGHT_GRAY);
			jtfHour.get(i).setEnabled(false);
		}
	}

	public void EnableJTF(ArrayList<JTextField> jtf, int value) {
		for (int i = 0; i < jtf.size(); i++) {
			if (i <= value - 1) {
				jtf.get(i).setEnabled(true);
				jtf.get(i).setBackground(Color.WHITE);
			} else {
				jtf.get(i).setText("");
				jtf.get(i).setEnabled(false);
				jtf.get(i).setBackground(Color.LIGHT_GRAY);
			}
		}
	}

	private void InitJTF() {
		name = new JTextField();
		gname = new JTextField();
		type = new JTextField();
		pppw = new JTextField();
		tppw = new JTextField();
		strength = new JTextField();
		capacity = new JTextField();

		name.setColumns(20);
		gname.setColumns(20);
		type.setColumns(20);
		pppw.setColumns(20);
		tppw.setColumns(20);
		strength.setColumns(20);
		capacity.setColumns(20);

		jcbStud = new JComboBox();
		if (getDTTGData().getStudentLength() == 0)
			jcbStud.addItem("No Student");
		else
			jcbStud.addItem("Select Student");

		jcbStud.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (jcbStud.getItemCount() > 0)
					if (!jcbStud.getSelectedItem().toString().contains("Student"))
						name.setText(jcbStud.getSelectedItem().toString());
			}
		});

	}

	public void RefreshTable(ShowHidePanel P0,int c,String[] s){
		P0.getPanelSet(1).removeAll();
		P0.SHOWP();
		createTable(c, s);
		P0.getPanelSet(1).add(tableAggregate);
		P0.getPanelSet(1).updateUI();				
	}
	
	public JPanel getTeacharContent(String title) {
		final ShowHidePanel P0 = new ShowHidePanel();
		P0.getPanelSet(0).setLayout(new GridBagLayout());
		P0.getPanelSet(1).setLayout(new BorderLayout());
		P0.getPanelSet(2).setLayout(new BorderLayout(10, 10));
		P0.getPanelSet(2).setBorder(new TitledBorder(title + " List : "));
		cancelBtn = new JButton("Cancel");

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(5, 2, 5, 2);
		gbc.anchor = GridBagConstraints.LINE_END;
		// %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
		// Teacher
		// %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
		P0.getPanelSet(3).setLayout(new GridBagLayout());
		addBtn = new JButton("Add Teacher");
		editBtn = new JButton("Edit Teacher");
		removeBtn = new JButton("Remove Teacher");
		showBtn = new JButton("Show");
		
		gbc.gridx = 1;
		gbc.gridy = 1;
		P0.getPanelSet(3).add(editBtn, gbc);
		gbc.gridx = 10;
		P0.getPanelSet(3).add(removeBtn, gbc);
		gbc.gridx = 20;
		P0.getPanelSet(3).add(cancelBtn, gbc);

		gbc.gridx = 1;
		gbc.gridy = 1;
		P0.getPanelSet(0).add(new JLabel("Teacher Name : "), gbc);
		gbc.gridx = 10;
		P0.getPanelSet(0).add(name, gbc);
		gbc.gridy = 40;

		gbc.gridx = 1;
		gbc.gridy = 10;
		P0.getPanelSet(0).add(new JLabel("Teacher Type : "), gbc);
		gbc.gridx = 10;
		P0.getPanelSet(0).add(type, gbc);
		gbc.gridy = 40;
		P0.getPanelSet(0).add(addBtn, gbc);
		gbc.gridx = 20;
		P0.getPanelSet(0).add(showBtn, gbc);

		showBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				RefreshTable(P0, DataSet.TEACHER,tchnames);
			}
		});
		addBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!(P0.validate(name, 0) && P0.validate(type, 0))) {
					JOptionPane.showMessageDialog(null, "Fill Empty Fields!","Empty", JOptionPane.INFORMATION_MESSAGE);
				} else
					getDTTGData().addTeacher(name.getText(), type.getText());
				RefreshTable(P0, DataSet.TEACHER,tchnames);
			}
		});
		editBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (editBtn.getText().equalsIgnoreCase("Edit Teacher")) {
					if (tableView.getSelectedRow() != -1) {
						EditRow(tableView.getSelectedRow());
						editBtn.setText("Save");
						addBtn.setEnabled(false);
						JOptionPane.showMessageDialog(null,
								"You have selected Row[" + tableView.getSelectedRow() + "] from " + tableView.getName(),"Edit", JOptionPane.INFORMATION_MESSAGE);
					} else
						JOptionPane.showMessageDialog(null,"First You have to select ", "Edit",JOptionPane.INFORMATION_MESSAGE);
				} else if (editBtn.getText().equalsIgnoreCase("Save")) {
					if (tableView.getSelectedRow() != -1) {
						if (SaveRow(tableView.getSelectedRow())) {
							RefreshTable(P0, DataSet.TEACHER,tchnames);
							editBtn.setText("Edit Teacher");
							addBtn.setEnabled(true);
							JOptionPane.showMessageDialog(null,"You have successfully saved Row[" + tableView.getSelectedRow() + "] from " + tableView.getName(),"Save", JOptionPane.INFORMATION_MESSAGE);
						}
					} else
						JOptionPane.showMessageDialog(null,"First You have to select ", "Save",JOptionPane.INFORMATION_MESSAGE);
				}

			}
		});
		removeBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (tableView.getSelectedRow() != -1) {
					String tmp = tableView.getValueAt(tableView.getSelectedRow(), 0).toString();
					if (JOptionPane.showConfirmDialog(null,"Want to remove Row Element? " + tmp) == JOptionPane.YES_OPTION) {
						getDTTGData().removeTeacher(tableView.getSelectedRow());
						RefreshTable(P0, DataSet.TEACHER,tchnames);
						JOptionPane.showMessageDialog(null,"You have successfully removed Row Element " + tmp + " from " + tableView.getName(), "Remove",JOptionPane.INFORMATION_MESSAGE);
					}
				} else
					JOptionPane.showMessageDialog(null,"First You have to select ", "Remove",JOptionPane.INFORMATION_MESSAGE);
			}
		});
		cancelBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				editBtn.setText("Edit Teacher");
				addBtn.setEnabled(true);
			}
		});

		commonListDisplay.setText("");
		P0.getPanelSet(2).add(new JScrollPane(commonList), BorderLayout.CENTER);
		P0.getPanelSet(2).add(new JScrollPane(commonListDisplay),BorderLayout.LINE_END);

		return P0.addPanelSet(title);

	}

	public JPanel getSubjectContent(String title) {
		final ShowHidePanel P0 = new ShowHidePanel();
		P0.getPanelSet(0).setLayout(new GridBagLayout());
		P0.getPanelSet(1).setLayout(new BorderLayout());
		P0.getPanelSet(2).setLayout(new BorderLayout(10, 10));
		P0.getPanelSet(2).setBorder(new TitledBorder(title + " List : "));
		cancelBtn = new JButton("Cancel");

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(5, 2, 5, 2);
		gbc.anchor = GridBagConstraints.LINE_END;

		// %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
		// Subject
		// %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
		P0.getPanelSet(3).setLayout(new GridBagLayout());
		addBtn = new JButton("Add Subject");
		editBtn = new JButton("Edit Subject");
		removeBtn = new JButton("Remove Subject");
		showBtn = new JButton("Show");
		
		gbc.gridx = 1;
		gbc.gridy = 1;
		P0.getPanelSet(3).add(editBtn, gbc);
		gbc.gridx = 10;
		P0.getPanelSet(3).add(removeBtn, gbc);
		gbc.gridx = 20;
		P0.getPanelSet(3).add(cancelBtn, gbc);

		gbc.gridx = 1;
		gbc.gridy = 1;
		P0.getPanelSet(0).add(new JLabel("Subject Name : "), gbc);
		gbc.gridx = 10;
		P0.getPanelSet(0).add(name, gbc);

		gbc.gridx = 1;
		gbc.gridy = 10;
		P0.getPanelSet(0).add(new JLabel("No.Theory Period/Week : "), gbc);
		gbc.gridx = 10;
		P0.getPanelSet(0).add(tppw, gbc);

		gbc.gridx = 1;
		gbc.gridy = 20;
		P0.getPanelSet(0).add(new JLabel("No.Practical Period/Week : "), gbc);
		gbc.gridx = 10;
		P0.getPanelSet(0).add(pppw, gbc);
		gbc.gridy = 40;
		P0.getPanelSet(0).add(addBtn, gbc);
		gbc.gridx = 20;
		P0.getPanelSet(0).add(showBtn, gbc);
		
		showBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				RefreshTable(P0,DataSet.SUBJECT, subjnames);
			}
		});
		
		addBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!(P0.validate(name, 0) && P0.validate(tppw, 0) && P0.validate(pppw, 0))) {
					JOptionPane.showMessageDialog(null, "Fill Empty Fields!","Empty", JOptionPane.INFORMATION_MESSAGE);
				} else if (!(P0.validate(tppw, 1) && P0.validate(pppw, 1))) {
					JOptionPane.showMessageDialog(null, "Field is a numeric!","Number", JOptionPane.ERROR_MESSAGE);
				} else {
					getDTTGData().addSubject(name.getText(),Integer.parseInt(tppw.getText()),Integer.parseInt(pppw.getText()));
				}
				RefreshTable(P0,DataSet.SUBJECT, subjnames);
			}
		});
		editBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (editBtn.getText().equalsIgnoreCase("Edit Subject")) {
					if (tableView.getSelectedRow() != -1) {
						EditRow(tableView.getSelectedRow());
						editBtn.setText("Save");
						addBtn.setEnabled(false);
						JOptionPane.showMessageDialog(null,"You have selected Row[" + tableView.getSelectedRow() + "] from " + tableView.getName(),"Edit", JOptionPane.INFORMATION_MESSAGE);
					} else
						JOptionPane.showMessageDialog(null,"First You have to select ", "Edit",JOptionPane.INFORMATION_MESSAGE);
				} else if (editBtn.getText().equalsIgnoreCase("Save")) {
					if (tableView.getSelectedRow() != -1) {
						if (SaveRow(tableView.getSelectedRow())) {
							RefreshTable(P0,DataSet.SUBJECT, subjnames);
							editBtn.setText("Edit Subject");
							addBtn.setEnabled(true);
							JOptionPane.showMessageDialog(null,"You have successfully saved Row[" + tableView.getSelectedRow() + "] from " + tableView.getName(),"Save", JOptionPane.INFORMATION_MESSAGE);
						}
					} else
						JOptionPane.showMessageDialog(null,"First You have to select ", "Save",JOptionPane.INFORMATION_MESSAGE);
				}

			}
		});
		removeBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (tableView.getSelectedRow() != -1) {
					String tmp = tableView.getValueAt(tableView.getSelectedRow(), 0).toString();
					if (JOptionPane.showConfirmDialog(null,"Want to remove Row Element? " + tmp) == JOptionPane.YES_OPTION) {
						getDTTGData().removeSubject(tableView.getSelectedRow());
						RefreshTable(P0,DataSet.SUBJECT, subjnames);
						JOptionPane.showMessageDialog(null,"You have successfully removed Row Element " + tmp + " from " + tableView.getName(), "Remove",JOptionPane.INFORMATION_MESSAGE);
					}
				} else
					JOptionPane.showMessageDialog(null,"First You have to select ", "Remove",JOptionPane.INFORMATION_MESSAGE);
			}
		});
		cancelBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				editBtn.setText("Edit Subject");
				addBtn.setEnabled(true);
			}
		});

		commonListDisplay.setText("");
		P0.getPanelSet(2).add(new JScrollPane(commonList), BorderLayout.CENTER);
		P0.getPanelSet(2).add(new JScrollPane(commonListDisplay),BorderLayout.LINE_END);

		return P0.addPanelSet(title);

	}

	public JPanel getStudentContent(String title) {
		gPanel = new JPanel();
		gPanel.setLayout(new GridBagLayout());
		gPanel.setVisible(false);
		gname.setVisible(false);
		jcbStud.setVisible(false);

		final ShowHidePanel P0 = new ShowHidePanel();
		P0.getPanelSet(0).setLayout(new GridBagLayout());
		P0.getPanelSet(1).setLayout(new BorderLayout());
		P0.getPanelSet(2).setLayout(new BorderLayout(10, 10));
		P0.getPanelSet(2).setBorder(new TitledBorder(title + " List : "));
		cancelBtn = new JButton("Cancel");
		createBtn = new JButton("Create Group");

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(5, 2, 5, 2);
		gbc.anchor = GridBagConstraints.LINE_END;

		// %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
		// Student
		// %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
		P0.getPanelSet(3).setLayout(new GridBagLayout());
		addBtn = new JButton("Add Student");
		editBtn = new JButton("Edit Student");
		removeBtn = new JButton("Remove Student");
		showBtn = new JButton("Show");

		gbc.gridx = 1;
		gbc.gridy = 1;
		P0.getPanelSet(3).add(editBtn, gbc);
		gbc.gridx = 10;
		P0.getPanelSet(3).add(removeBtn, gbc);
		gbc.gridx = 20;
		P0.getPanelSet(3).add(cancelBtn, gbc);

		gbc.gridx = 1;
		gbc.gridy = 10;
		gPanel.add(new JLabel("Student Group Name : "), gbc);

		gbc.gridx = 1;
		gbc.gridy = 1;
		P0.getPanelSet(0).add(new JLabel("Student Name : "), gbc);
		gbc.gridx = 10;
		P0.getPanelSet(0).add(name, gbc);
		P0.getPanelSet(0).add(jcbStud, gbc);

		gbc.gridx = 1;
		gbc.gridy = 10;
		P0.getPanelSet(0).add(gPanel, gbc);
		gbc.gridx = 10;
		P0.getPanelSet(0).add(gname, gbc);

		gbc.gridx = 1;
		gbc.gridy = 20;
		P0.getPanelSet(0).add(new JLabel("No.Student : "), gbc);
		gbc.gridx = 10;
		P0.getPanelSet(0).add(strength, gbc);

		gbc.gridy = 40;
		P0.getPanelSet(0).add(addBtn, gbc);
		gbc.gridx = 20;
		P0.getPanelSet(0).add(createBtn, gbc);
		gbc.gridx = 30;
		P0.getPanelSet(0).add(showBtn, gbc);

		createBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				name.setText("");
				gname.setText("");
				strength.setText("");
				if (createBtn.getText().equals("Create Group")) {
					gPanel.setVisible(true);
					jcbStud.setVisible(true);
					gname.setVisible(true);
					name.setVisible(false);
					createBtn.setText("Cancel");
					addBtn.setText("Add Group");
				} else if (createBtn.getText().equals("Cancel")) {
					addBtn.setText("Add Student");
					createBtn.setText("Create Group");
					gPanel.setVisible(false);
					jcbStud.setVisible(false);
					gname.setVisible(false);
					name.setVisible(true);
				}
			}
		});

		showBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				RefreshTable(P0,DataSet.STUDENT, studnames);
			}
		});
		
		addBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (addBtn.getText().equals("Add Student")) {
					if (!(P0.validate(name, 0) && P0.validate(strength, 0))) {
						JOptionPane.showMessageDialog(null,"Fill Empty Fields!", "Empty",JOptionPane.INFORMATION_MESSAGE);
					} else if (!P0.validate(strength, 1)) {
						JOptionPane.showMessageDialog(null,"Field is a numeric!", "Number",JOptionPane.ERROR_MESSAGE);
					} else {
						getDTTGData().addStudent(name.getText(),Integer.parseInt(strength.getText()));
						name.setText("");
						strength.setText("");
					}
				} else if (addBtn.getText().equals("Add Group")) {
					if (!(P0.validate(name, 0) && P0.validate(gname, 0) && P0.validate(strength, 0))) {
						JOptionPane.showMessageDialog(null,"Fill Empty Fields!", "Empty",JOptionPane.INFORMATION_MESSAGE);
					} else if (!P0.validate(strength, 1)) {
						JOptionPane.showMessageDialog(null,"Field is a numeric!", "Number",JOptionPane.ERROR_MESSAGE);
					} else {
						if (getDTTGData().addGroup(name.getText(),gname.getText(),Integer.parseInt(strength.getText()))) {
							name.setText("");
							gname.setText("");
							strength.setText("");

							addBtn.setText("Add Student");
							createBtn.setText("Create Group");
							gPanel.setVisible(false);
							jcbStud.setVisible(false);
							gname.setVisible(false);
							name.setVisible(true);
						}
					}
				}
				RefreshTable(P0,DataSet.STUDENT, studnames);
			}
		});
		editBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (editBtn.getText().equalsIgnoreCase("Edit Student")) {
					if (tableView.getSelectedRow() != -1) {
						EditRow(tableView.getSelectedRow());
						editBtn.setText("Save");
						addBtn.setEnabled(false);
						JOptionPane.showMessageDialog(null,"You have selected Row[" + tableView.getSelectedRow() + "] from " + tableView.getName(),"Edit", JOptionPane.INFORMATION_MESSAGE);
					} else
						JOptionPane.showMessageDialog(null,"First You have to select ", "Edit",JOptionPane.INFORMATION_MESSAGE);
				} else if (editBtn.getText().equalsIgnoreCase("Save")) {
					if (tableView.getSelectedRow() != -1) {
						if (SaveRow(tableView.getSelectedRow())) {
							RefreshTable(P0,DataSet.STUDENT, studnames);
							editBtn.setText("Edit Student");
							addBtn.setEnabled(true);
							JOptionPane.showMessageDialog(null,"You have successfully saved Row[" + tableView.getSelectedRow() + "] from " + tableView.getName(),"Save", JOptionPane.INFORMATION_MESSAGE);
						}
					} else
						JOptionPane.showMessageDialog(null,"First You have to select ", "Save",JOptionPane.INFORMATION_MESSAGE);
				}

			}
		});
		removeBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (tableView.getSelectedRow() != -1) {
					String tmp = tableView.getValueAt(tableView.getSelectedRow(), 0).toString();
					if (RemoveStudent(tableView.getSelectedRow(), tmp)) {
						RefreshTable(P0,DataSet.STUDENT, studnames);
						JOptionPane.showMessageDialog(null,"You have successfully removed Row Element " + tmp + " from " + tableView.getName(),"Remove", JOptionPane.INFORMATION_MESSAGE);
					}
				} else
					JOptionPane.showMessageDialog(null,"First You have to select ", "Remove",JOptionPane.INFORMATION_MESSAGE);
			}

			private boolean RemoveStudent(int index, String tmp) {
				String str = tmpList.get(index);
				if (str.indexOf(":") > 0) {
					int i = Integer.parseInt(str.substring(0, str.indexOf(":")));
					int j = Integer.parseInt(str.substring(str.indexOf(":") + 1));
					if (JOptionPane.showConfirmDialog(null, "Want to remove Row Element? " + tmp) == JOptionPane.YES_OPTION) {
						getDTTGData().removeGroup(i, j);
						return true;
					}
				} else {
					int i = Integer.parseInt(str);
					if (JOptionPane.showConfirmDialog(null, "Removing " + tmp + " cause for deletion of it's groups & "	+ getDTTGData().getStudent(i).getName() + "\n Want to remove Row Element? " + tmp) == JOptionPane.YES_OPTION) {
						getDTTGData().removeStudent(i);
						return true;
					}
				}
				return false;
			}
		});
		cancelBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				editBtn.setText("Edit Student");
				addBtn.setEnabled(true);
			}
		});

		commonListDisplay.setText("");
		P0.getPanelSet(2).add(new JScrollPane(commonList), BorderLayout.CENTER);
		P0.getPanelSet(2).add(new JScrollPane(commonListDisplay),BorderLayout.LINE_END);

		return P0.addPanelSet(title);

	}

	public JPanel getRoomContent(String title) {
		final ShowHidePanel P0 = new ShowHidePanel();
		
		final JComboBox jcbATag = new JComboBox();
		jcbATag.setModel(new DefaultComboBoxModel(new String[] { "Select Room", "CLASS", "LAB"}));
		
		jcbATag.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (!jcbATag.getSelectedItem().toString().contains("Room"))
					type.setText(jcbATag.getSelectedItem().toString());
				else 
					type.setText("");
			}
		});
		
		P0.getPanelSet(0).setLayout(new GridBagLayout());
		P0.getPanelSet(1).setLayout(new BorderLayout());
		P0.getPanelSet(2).setLayout(new BorderLayout(10, 10));
		P0.getPanelSet(2).setBorder(new TitledBorder(title + " List : "));
		cancelBtn = new JButton("Cancel");

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(5, 2, 5, 2);
		gbc.anchor = GridBagConstraints.LINE_END;

		// %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
		// Room
		// %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
		P0.getPanelSet(3).setLayout(new GridBagLayout());
		addBtn = new JButton("Add Room");
		editBtn = new JButton("Edit Room");
		removeBtn = new JButton("Remove Room");
		showBtn = new JButton("Show");
		
		gbc.gridx = 1;
		gbc.gridy = 1;
		P0.getPanelSet(3).add(editBtn, gbc);
		gbc.gridx = 10;
		P0.getPanelSet(3).add(removeBtn, gbc);
		gbc.gridx = 20;
		P0.getPanelSet(3).add(cancelBtn, gbc);

		gbc.gridx = 1;
		gbc.gridy = 1;
		P0.getPanelSet(0).add(new JLabel("Room Name : "), gbc);
		gbc.gridx = 10;
		P0.getPanelSet(0).add(name, gbc);

		gbc.gridx = 1;
		gbc.gridy = 10;
		P0.getPanelSet(0).add(new JLabel("Room Type : "), gbc);
		gbc.gridx = 10;
		P0.getPanelSet(0).add(jcbATag, gbc);

		gbc.gridx = 1;
		gbc.gridy = 20;
		P0.getPanelSet(0).add(new JLabel("Room Capacity : "), gbc);
		gbc.gridx = 10;
		P0.getPanelSet(0).add(capacity, gbc);
		gbc.gridy = 40;
		P0.getPanelSet(0).add(addBtn, gbc);
		gbc.gridx = 20;
		P0.getPanelSet(0).add(showBtn, gbc);

		showBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				RefreshTable(P0,DataSet.ROOM, roomnames);
			}
		});
		addBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!(P0.validate(name, 0) && P0.validate(type, 0) && P0.validate(capacity, 0))) {
					JOptionPane.showMessageDialog(null, "Fill Empty Fields!","Empty", JOptionPane.INFORMATION_MESSAGE);
				} else if (!P0.validate(capacity, 1)) {
					JOptionPane.showMessageDialog(null, "Field is a numeric!","Number", JOptionPane.ERROR_MESSAGE);
				} else {
					getDTTGData().addRoom(name.getText(),Integer.parseInt(capacity.getText()),type.getText());
				}
				RefreshTable(P0,DataSet.ROOM, roomnames);
			}
		});
		editBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (editBtn.getText().equalsIgnoreCase("Edit Room")) {
					if (tableView.getSelectedRow() != -1) {
						EditRow(tableView.getSelectedRow());
						editBtn.setText("Save");
						addBtn.setEnabled(false);
						JOptionPane.showMessageDialog(null,"You have selected Row[" + tableView.getSelectedRow() + "] from " + tableView.getName(),"Edit", JOptionPane.INFORMATION_MESSAGE);
					} else
						JOptionPane.showMessageDialog(null,"First You have to select ", "Edit",JOptionPane.INFORMATION_MESSAGE);
				} else if (editBtn.getText().equalsIgnoreCase("Save")) {
					if (tableView.getSelectedRow() != -1) {
						if (SaveRow(tableView.getSelectedRow())) {
							RefreshTable(P0,DataSet.ROOM, roomnames);
							editBtn.setText("Edit Room");
							addBtn.setEnabled(true);
							JOptionPane.showMessageDialog(null,"You have successfully saved Row[" + tableView.getSelectedRow() + "] from " + tableView.getName(),"Save", JOptionPane.INFORMATION_MESSAGE);
						}
					} else
						JOptionPane.showMessageDialog(null,"First You have to select ", "Save",JOptionPane.INFORMATION_MESSAGE);
				}

			}
		});
		removeBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (tableView.getSelectedRow() != -1) {
					String tmp = tableView.getValueAt(tableView.getSelectedRow(), 0).toString();
					if (JOptionPane.showConfirmDialog(null,"Want to remove Row Element? " + tmp) == JOptionPane.YES_OPTION) {
						getDTTGData().removeRoom(tableView.getSelectedRow());
						RefreshTable(P0,DataSet.ROOM, roomnames);
						JOptionPane.showMessageDialog(null,"You have successfully removed Row Element " + tmp + " from " + tableView.getName(), "Remove",JOptionPane.INFORMATION_MESSAGE);
					}
				} else
					JOptionPane.showMessageDialog(null,"First You have to select ", "Remove",JOptionPane.INFORMATION_MESSAGE);
			}
		});
		cancelBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				editBtn.setText("Edit Room");
				addBtn.setEnabled(true);
			}
		});
		// %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
		commonListDisplay.setText("");
		P0.getPanelSet(2).add(new JScrollPane(commonList), BorderLayout.CENTER);
		P0.getPanelSet(2).add(new JScrollPane(commonListDisplay),BorderLayout.LINE_END);

		return P0.addPanelSet(title);

	}

	protected void EditRow(int row) {
		if (tableView.getName().equalsIgnoreCase("teacher")) {
			name.setText(tableView.getModel().getValueAt(row, 0).toString());
			type.setText(tableView.getModel().getValueAt(row, 1).toString());
		} else if (tableView.getName().equalsIgnoreCase("student")) {
			String str = tmpList.get(row);
			if (str.indexOf(":") > 0) {
				int i = Integer.parseInt(str.substring(0, str.indexOf(":")));
				jcbStud.setSelectedIndex(i+1);
				gname.setText(tableView.getModel().getValueAt(row, 0).toString());
				gPanel.setVisible(true);
				jcbStud.setVisible(true);
				gname.setVisible(true);
				name.setVisible(false);
			} else {
				name.setText(tableView.getModel().getValueAt(row, 0).toString());
				gPanel.setVisible(false);
				jcbStud.setVisible(false);
				gname.setVisible(false);
				name.setVisible(true);
			}
			strength.setText(tableView.getModel().getValueAt(row, 1).toString());
		} else if (tableView.getName().equalsIgnoreCase("subject")) {
			name.setText(tableView.getModel().getValueAt(row, 0).toString());
			tppw.setText(tableView.getModel().getValueAt(row, 1).toString());
			pppw.setText(tableView.getModel().getValueAt(row, 2).toString());
		} else if (tableView.getName().equalsIgnoreCase("room")) {
			name.setText(tableView.getModel().getValueAt(row, 0).toString());
			type.setText(tableView.getModel().getValueAt(row, 1).toString());
			capacity.setText(tableView.getModel().getValueAt(row, 2).toString());
		}

	}

	protected boolean SaveRow(int row) {
		String dup = "";
		if (tableView.getName().equalsIgnoreCase("teacher")) {
			if (getDTTGData().searchTeacher(name.getText())==row ||
				getDTTGData().searchTeacher(name.getText())==-1) {
				getDTTGData().getTeacher(row).setName(name.getText());
			} else
				dup = "Duplicate Entry for " + name.getText();
		} else if (tableView.getName().equalsIgnoreCase("student")) {
			String str = tmpList.get(row);
			if (str.indexOf(":") > 0) {
				int i = Integer.parseInt(str.substring(0, str.indexOf(":")));
				int j = Integer.parseInt(str.substring(str.indexOf(":") + 1));
				if (getDTTGData().searchGroupStudent(i,gname.getText())==j ||
					getDTTGData().searchGroupStudent(i,gname.getText())==-1) {
					getDTTGData().getStudent(i).setGroupName(j, gname.getText());
					getDTTGData().getStudent(i).setGroupSize(j, 
							Integer.parseInt(strength.getText()));
				} else
					dup = "Duplicate Entry for " + name.getText();
			} else {
				int i = Integer.parseInt(str);
				if (getDTTGData().searchStudent(name.getText())==i ||
					getDTTGData().searchStudent(name.getText())==-1) {
					getDTTGData().getStudent(i).setName(name.getText());
					getDTTGData().getStudent(i).setTotStud(
							Integer.parseInt(strength.getText()));
				} else
					dup = "Duplicate Entry for " + name.getText();
			}
			
			
		} else if (tableView.getName().equalsIgnoreCase("subject")) {
			if (getDTTGData().searchTeacher(name.getText())==row ||
				getDTTGData().searchSubject(name.getText())==-1) {
				getDTTGData().getSubject(row).setName(name.getText());
				getDTTGData().getSubject(row).setTPPW(Integer.parseInt(tppw.getText()));
				getDTTGData().getSubject(row).setPPW(Integer.parseInt(pppw.getText()));
			} else
				dup = "Duplicate Entry for " + name.getText();
		} else if (tableView.getName().equalsIgnoreCase("room")) {
			if (getDTTGData().searchTeacher(name.getText())==row ||
				getDTTGData().searchRoom(name.getText())==-1) {
				getDTTGData().getRoom(row).setName(name.getText());
				getDTTGData().getRoom(row).setType(type.getText());
				getDTTGData().getRoom(row).setCapacity(
						Integer.parseInt(capacity.getText()));
			} else
				dup = "Duplicate Entry for " + name.getText();
		}

		if (!dup.equals("")) {
			JOptionPane.showMessageDialog(null, dup, "Duplicate",
					JOptionPane.ERROR_MESSAGE);
			return false;
		} else
			tableView.clearSelection();

		return true;
	}

	@SuppressWarnings( { "serial" })
	public void createTable(int c, final String[] names) {
		String tableViewName = "";
		dataTable.clear();

		switch (c) {
		case DataSet.SUBJECT:
			tableViewName="SUBJECT";
			for (int i = 0; i < getDTTGData().getSubjectLength(); i++)
				dataTable.add(getDTTGData().getSubjectRecord(i));
			break;
		case DataSet.STUDENT:
			tableViewName="STUDENT";
			for (int i = 0; i < getDTTGData().getStudentLength(); i++) {
				dataTable.add(getDTTGData().getStudentRecord(i));
				for (int j = 0; j < getDTTGData().getStudent(i).getGroupList().size(); j++)
					dataTable.add(getDTTGData().getGroupStudentRecord(i, j));
			}
			break;
		case DataSet.TEACHER:
			tableViewName="TEACHER";
			for (int i = 0; i < getDTTGData().getTeacherLength(); i++)
				dataTable.add(getDTTGData().getTeacherRecord(i));
			break;
		case DataSet.ROOM:
			tableViewName="ROOM";
			for (int i = 0; i < getDTTGData().getRoomLength(); i++)
				dataTable.add(getDTTGData().getRoomRecord(i));
			break;
		}

		// final
		// Create a model of the data.
		TableModel dataModel = new AbstractTableModel() {

			public int getColumnCount() {
				return names.length;
			}

			public int getRowCount() {
				return dataTable.size();
			}

			public Object getValueAt(int row, int col) {
				return dataTable.get(row)[col];
			}

			public String getColumnName(int column) {
				return names[column];
			}

			@SuppressWarnings("unchecked")
			public Class getColumnClass(int c) {
				return getValueAt(0, c).getClass();
			}

			public boolean isCellEditable(int row, int col) {
				return col == -1;
			}

			public void setValueAt(Object aValue, int row, int column) {
				dataTable.get(row)[column] = aValue;
			}
		};

		// Create the table
		tableView = new JTable(dataModel);
		tableView.setRowHeight(INITIAL_ROWHEIGHT);
		tableView.setName(tableViewName);

		scrollpane = new JScrollPane(tableView);
		tableAggregate = scrollpane;
		updateData(c);
	}

	public void updateData(int c) {
		dataList.clear();
		tmpList.clear();
		commonList.removeAll();
		
		switch (c) {
		case DataSet.SUBJECT:
			for (int i = 0; i < getDTTGData().getSubjectLength(); i++)
				dataList.add(i, i + "]" + getDTTGData().getSubject(i).getName());
			break;
		case DataSet.STUDENT:
			jcbStud.removeAllItems();
			jcbStud.updateUI();
			if (getDTTGData().getStudentLength() == 0)
				jcbStud.addItem("No Student");
			else
				jcbStud.addItem("Select Student");

			for (int i = 0; i < getDTTGData().getStudentLength(); i++)
				jcbStud.addItem(getDTTGData().getStudent(i).getName());

			for (int i = 0,k=0; i < getDTTGData().getStudentLength(); i++) {
				String s = getDTTGData().getStudent(i).getName();
				int size=getDTTGData().getStudent(i).getGroupList().size();
				tmpList.add(String.valueOf(i));
				dataList.add(k++,"Class:"+ (i+1) + " " +s);
				for (int j = 0; j < size ; j++) {
					tmpList.add(String.valueOf(i) + ":" + String.valueOf(j));
					dataList.add(k++, "Batch:"+ (j+1) + " " + getDTTGData().getStudent(i).getGroupName(j));
				}
			}
			break;
		case DataSet.TEACHER:
			for (int i = 0; i < getDTTGData().getTeacherLength(); i++)
				dataList.add(i, i + "]" + getDTTGData().getTeacher(i).getName());
			break;
		case DataSet.ROOM:
			for (int i = 0; i < getDTTGData().getRoomLength(); i++)
				dataList.add(i, i + "]" + getDTTGData().getRoom(i).getName());
			break;
		}
		
		commonList.setListData(dataList);
		commonList.updateUI();
	}
}
