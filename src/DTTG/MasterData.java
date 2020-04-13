package DTTG;

import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;

import DataSource.*;

/**
 * Internal Frames Demo
 */
public class MasterData extends DTTGModule implements ActionListener {
	JTabbedPane tabbedpane;
	RulesPanel rp;
	MasterPanel mp;
	ActivityPanel ap;
	JPanel p;
	/**
	 * MasterData Constructor
	 */

	public MasterData(DTTG dttg) {
		super(dttg, "MasterData", "toolbar/mast2 18.png");

		// create tab
		tabbedpane = new JTabbedPane();
		getDemoPanel().add(tabbedpane, BorderLayout.CENTER);

		// %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
		// Data Entry
		// %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

		String name ="Step 1 >> "+ getString("MasterData.dept");
		mp = new MasterPanel(dttg);
		tabbedpane.add(name, mp.getMasterPanel());

		// %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
		// Activity Manager
		// %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

		name ="Step 2 >> "+ getString("MasterData.activity");
		ap = new ActivityPanel(dttg);
		tabbedpane.add(name,ap.getActivityPanel());
		
		name = "Step 3 >> "+ getString("MasterData.rule");
		rp = new RulesPanel(dttg);
		tabbedpane.add(name, rp.getRulesPanel());
		
		tabbedpane.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				if (tabbedpane.getSelectedIndex() == 1) {
					ap.UploadListData(DataSet.TEACHER);
					ap.UploadListData(DataSet.STUDENT);
					ap.UploadListData(DataSet.ROOM);
					ap.UploadListData(DataSet.SUBJECT);
					ap.UploadListData(DataSet.ACTIVITY);
					ap.UploadListData(DataSet.ASSIGNSUBJECT);
				}	
			}
		});

	}

	public void actionPerformed(ActionEvent e) {

	}

}
