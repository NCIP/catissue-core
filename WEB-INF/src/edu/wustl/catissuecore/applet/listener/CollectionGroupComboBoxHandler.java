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

import edu.wustl.catissuecore.applet.AppletConstants;
import edu.wustl.catissuecore.applet.model.MultipleSpecimenTableModel;
import edu.wustl.catissuecore.applet.model.SpecimenColumnModel;
import edu.wustl.catissuecore.applet.ui.ModifiedComboBox;
import edu.wustl.catissuecore.applet.util.CommonAppletUtil;
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
	/**
	 * Name : Ashish Gupta
	 * Reviewer Name : Sachin Lale 
	 * Bug ID: 2741
	 * Patch ID: 2741_7	 
	 * Description: Calling JS function to send the selected scg to the server inorder to retrieve events
	*/
		int colNo = table.getSelectedColumn();	
		int actualColumnNo = ((MultipleSpecimenTableModel) table.getModel()).getActualColumnNo(colNo);
		//Making the key
		String key = AppletConstants.SPECIMEN_PREFIX + String.valueOf(actualColumnNo + 1)+"_specimenEventCollection";
		
		Object[] parameters = {((JComboBox)(e.getSource())).getSelectedItem(),key};
		//calling javascript function to send the selected scg name to the server. 
		CommonAppletUtil.callJavaScriptFunction((ModifiedComboBox)(e.getSource()), "getEventsFromSCGForMultiple", parameters);
		
		
		super.handleAction(e);
		System.out.println("Inside CollectionGroupComboBoxHandler");

//		Mandar: 30Oct06: commented as buttons are enabled by default.
		
//		TableColumnModel columnModel = table.getColumnModel();
//		SpecimenColumnModel scm = (SpecimenColumnModel)columnModel.getColumn(table.getSelectedColumn()).getCellEditor();
//		scm.updateButtons();
		}
}
