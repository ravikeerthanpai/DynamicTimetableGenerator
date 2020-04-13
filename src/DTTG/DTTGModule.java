package DTTG;

/*
 * @(#)TTGSModule.java	1.21 05/03/25
 */

import java.awt.BorderLayout;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import javax.swing.Icon;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;

import DataSource.DataSet;
import DTTG.Timetable.DTTGGenerator;

/**
 * A generic com.TTGS demo module
 * 
 * @version 1.21 03/25/05
 * @author Jeff Dinkins
 */
public class DTTGModule{
 
	protected DTTG dttg = null;
	private JPanel panel = null;
	private String resourceName = null;
	private String iconPath = null;

	// Resource bundle for internationalized and accessible text
	private ResourceBundle bundle = null;

	public DTTGModule(DTTG dttg) {
		this(dttg, null, null);
	}

	public DTTGModule(DTTG dttg, String resourceName, String iconPath) {
		UIManager.put("swing.boldMetal", Boolean.FALSE);
		panel = new JPanel();
		panel.setLayout(new BorderLayout());

		this.resourceName = resourceName;
		this.iconPath = iconPath;
		this.dttg = dttg;
	}

	public String getResourceName() {
		return resourceName;
	}

	public JPanel getDemoPanel() {
		return panel;
	}

	public DTTG getDTTG() {
		return dttg;
	}

	public DataSet getDTTGData() {
		return dttg.MAIN;
	}

	public DTTGGenerator getDTTGGenerator() {
		dttg.dttgGen.SetData(getDTTGData());
		return dttg.dttgGen;
	}

	public void setDTTGData() {
		dttg.MAIN = new DataSet();
	}

	public String getString(String key) {
		String value = "nada";
		if (bundle == null) {
			if (getDTTG() != null) {
				bundle = getDTTG().getResourceBundle();
			} else {
				bundle = ResourceBundle.getBundle("DTTG.resources.dttg");
			}
		}
		try {
			value = bundle.getString(key);
		} catch (MissingResourceException e) {
			System.out.println("java.util.MissingResourceException: Couldn't find value for: "
							+ key);
		}
		return value;
	}

	public char getMnemonic(String key) {
		return (getString(key)).charAt(0);
	}

	public String getName() {
		return getString(getResourceName() + ".name");
	};

	public Icon getIcon() {
		return getDTTG().createImageIcon(iconPath, getResourceName() + ".name");
	};

	public String getToolTip() {
		return getString(getResourceName() + ".tooltip");
	};

	void updateDragEnabled(boolean dragEnabled) {
	}
	
	public void MSGBOX(String title, String msg,int type){
		JOptionPane.showMessageDialog(null,  msg, title, type);		
	}
}
