/**
 * @author mandar_deshmukh
 * Created on Sep 20, 2006
 * 
 * This class handles specimen type specific events.
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
 * This class handles specimen type specific events.
 * 
 */public class TypeComboBoxHandler extends ComboBoxHandler {

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
		/**
		 * Name : Vijay_Pande
		 * Reviewer : Sachin_Lale
		 * Bug ID: 4189 
		 * Patch ID: 1
		 * See also: -
		 * Description: Specimen quantity unit label was switching from gm to count. 
		 * Actually in the bizlogic the  case of bug id 1414 was handled but not on the UI for Mulitple specimen applet.
		 * Code is added for Type comboboxhandler to consider cases in but id 1414.
		 */  		
		((MultipleSpecimenTableModel) table.getModel()).specimenClassUpdated(table.getSelectedColumn());
		TableColumnModel columnModel = table.getColumnModel();
		SpecimenColumnModel scm = (SpecimenColumnModel)columnModel.getColumn(table.getSelectedColumn()).getCellEditor();
		System.out.println("Editor received....");
		scm.specimenClassUpdated(((JComboBox)e.getSource()).getSelectedItem().toString());
		System.out.println("Type Set");
		/** --patch ends here -- */
	}
}
