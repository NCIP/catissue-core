/**
 * @author mandar_deshmukh
 * Created on Oct 05, 2006
 * 
 * This class handles collection Group specific events.
 * 
 */
package edu.wustl.catissuecore.applet.listener;

import java.awt.event.ActionEvent;

import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.table.TableColumnModel;

import edu.wustl.catissuecore.applet.model.MultipleSpecimenTableModel;
import edu.wustl.catissuecore.applet.model.SpecimenColumnModel;
import edu.wustl.catissuecore.util.global.Constants;

/**
 * @author mandar_deshmukh
 * Created on Oct 05, 2006
 * 
 * This class handles collection Group specific events.
 * 
 */
public class CollectionGroupComboBoxHandler extends ComboBoxHandler {
	
	

	/**
	 * @param table
	 */
	public CollectionGroupComboBoxHandler(JTable table) {
		super(table);
	}
	
	protected void handleAction(ActionEvent e)
	{
		super.handleAction(e);
		System.out.println("Inside CollectionGroupComboBoxHandler");

//		Mandar: 30Oct06: commented as buttons are enabled by default.
		
//		TableColumnModel columnModel = table.getColumnModel();
//		SpecimenColumnModel scm = (SpecimenColumnModel)columnModel.getColumn(table.getSelectedColumn()).getCellEditor();
//		scm.updateButtons();
		}
}
