/*
 * Created on Sep 20, 2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package edu.wustl.catissuecore.applet.listener;

import java.awt.event.ActionEvent;

import javax.swing.JTable;

/**
 * @author mandar_deshmukh
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class TypeComboBoxHandler extends ComboBoxHandler {

	/**
	 * @param table
	 */
	public TypeComboBoxHandler(JTable table)
	{
		super(table);
	}
	
	protected void handleAction(ActionEvent e)
	{
		super.handleAction(e);
		System.out.println("Inside TypeComboBoxHandler");
	}
}
