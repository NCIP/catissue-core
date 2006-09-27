/**
 * @author mandar_deshmukh
 * Created on Sep 20, 2006
 * 
 * This class handles specimen class specific events.
 * 
 */
package edu.wustl.catissuecore.applet.listener;

import java.awt.event.ActionEvent;

import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.table.TableColumnModel;

import edu.wustl.catissuecore.applet.model.MultipleSpecimenTableModel;
import edu.wustl.catissuecore.applet.model.SpecimenColumnModel;

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
		((MultipleSpecimenTableModel) table.getModel()).specimenClassUpdated(table.getSelectedColumn());
		
//		((SpecimenColumnModel)table.getColumnModel()).refreshColumn() ;
		TableColumnModel columnModel = table.getColumnModel();
		SpecimenColumnModel scm = (SpecimenColumnModel)columnModel.getColumn(table.getSelectedColumn()).getCellEditor();
		System.out.println("Editor received....");
		scm.specimenClassUpdated(((JComboBox)e.getSource()).getSelectedItem().toString());
		//scm.setBarCode("bCode" );
		System.out.println("Type Set");
	}
}
