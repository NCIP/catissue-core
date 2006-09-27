/*
 * This class handles all the events of Map button.
 * Created on Sep 19, 2006
 * @author mandar_deshmukh
 */
package edu.wustl.catissuecore.applet.listener;

import javax.swing.JTable;

/**
 * @author mandar_deshmukh
 * 
 * This class handles all the events of Map button.
 */
public class ExternalIdentifierButtonHandler extends ButtonHandler {

	/**
	 * @param table
	 */
	public ExternalIdentifierButtonHandler(JTable table) {
		super(table);
	}

	/**
	 * @return JS method name for this button.
	 */
	protected String getJSMethodName()
	{
		return "showExtenalIdentifierDialog";
	}

}