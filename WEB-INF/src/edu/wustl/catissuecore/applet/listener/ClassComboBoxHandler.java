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

import edu.wustl.catissuecore.applet.model.MultipleSpecimenTableModel;

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
		((MultipleSpecimenTableModel) table.getModel()).specimenClassUpdated(table.getSelectedColumn());
	}
}
