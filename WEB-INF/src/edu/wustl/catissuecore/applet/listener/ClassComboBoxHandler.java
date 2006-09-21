/**
 * @author mandar_deshmukh
 * Created on Sep 20, 2006
 * 
 * This class handles specimen class specific events.
 * 
 */
package edu.wustl.catissuecore.applet.listener;

import java.awt.event.ActionEvent;

import javax.swing.JTable;

/**
 * @author mandar_deshmukh
 * Created on Sep 20, 2006
 * 
 * This class handles specimen class specific events.
 * 
 */
public class ClassComboBoxHandler extends ComboBoxHandler {

	/**
	 * @param table
	 */
	public ClassComboBoxHandler(JTable table) {
		super(table);
	}
	

	protected void handleAction(ActionEvent e)
	{
		super.handleAction(e);
		System.out.println("Inside ClassComboBoxHandler");
	}
}
