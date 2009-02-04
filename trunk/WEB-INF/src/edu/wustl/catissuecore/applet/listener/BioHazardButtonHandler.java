/*
 * This class handles all the events of Map button.
 * Created on Sep 19, 2006
 * @author mandar_deshmukh
 */
package edu.wustl.catissuecore.applet.listener;

import javax.swing.JTable;

/**
 * @author vaishali_khandelwal
 * 
 * This class handles all the events of bio hazard button.
 */
public class BioHazardButtonHandler extends ButtonHandler {

	/**
	 * @param table
	 */
	public BioHazardButtonHandler(JTable table) {
		super(table);
	}

	/**
	 * @return JS method name for this button.
	 */
	protected String getJSMethodName()
	{
		return "showBioHazardDialog";
	}

}