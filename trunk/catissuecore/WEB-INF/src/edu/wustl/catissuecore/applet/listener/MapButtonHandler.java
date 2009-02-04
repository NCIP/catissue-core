/*
 * This class handles all the events of Map button.
 * Created on Sep 19, 2006
 * @author mandar_deshmukh
 */
package edu.wustl.catissuecore.applet.listener;

import java.awt.event.ActionEvent;

import javax.swing.JButton;
import javax.swing.JRadioButton;
import javax.swing.JTable;

import edu.wustl.catissuecore.applet.AppletConstants;
import edu.wustl.catissuecore.applet.model.MultipleSpecimenTableModel;
import edu.wustl.catissuecore.applet.util.CommonAppletUtil;

/**
 * @author mandar_deshmukh
 * 
 * This class handles all the events of Map button.
 */
public class MapButtonHandler extends ButtonHandler {

	
	
	/** (non-Javadoc)
	 * @see edu.wustl.catissuecore.applet.listener.ButtonHandler#handleAction(java.awt.event.ActionEvent)
	 */
	protected void handleAction(ActionEvent event)
	{
		int colNo = table.getSelectedColumn();
		int rowNo = table.getSelectedRow();
		MultipleSpecimenTableModel model = (MultipleSpecimenTableModel) table.getModel();
		System.out.println("MapButtonHandler in handleAction : row : "+ rowNo + " : "+ colNo);
		String key = model.getKey(rowNo, colNo);           
	    String collectionGroup = (String) model.getValueAt(AppletConstants.SPECIMEN_COLLECTION_GROUP_ROW_NO,colNo);
	    String specimenClass = (String) model.getValueAt(AppletConstants.SPECIMEN_CLASS_ROW_NO,colNo);
	    System.out.println("MapButtonHandler : key - "+key+ " | collectionGroup - "+ collectionGroup + " | specimenClass - "+specimenClass); 
	    Object[] parameters = new Object[]{String.valueOf(colNo),collectionGroup,specimenClass}; 
		
		CommonAppletUtil.callJavaScriptFunction((JButton) event.getSource(),
				getJSMethodName(), parameters);
	}

	/**
	 * @param table
	 */
	public MapButtonHandler(JTable table, JRadioButton radioButton) {
		super(table,radioButton );
	}

	/**
	 * @return JS method name for this button.
	 */
	protected String getJSMethodName()
	{
		return "showStoragePositionMap";
	}

}