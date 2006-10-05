/*
 * This class handles all the events of Map button.
 * Created on Sep 29, 2006
 * @author santosh_chandak
 */
package edu.wustl.catissuecore.applet.listener;

import java.awt.event.ActionEvent;

import javax.swing.JButton;
import javax.swing.JTable;

import edu.wustl.catissuecore.applet.AppletConstants;
import edu.wustl.catissuecore.applet.model.MultipleSpecimenTableModel;
import edu.wustl.catissuecore.applet.util.CommonAppletUtil;
import edu.wustl.catissuecore.util.global.Constants;

/**
 * This class handles all the events of Map button.
 */
public class DerivedSpecimenButtonHandler extends ButtonHandler {

	/**
	 * @param table
	 */
	public DerivedSpecimenButtonHandler(JTable table) {
		super(table);
	}

	
	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.applet.listener.BaseActionHandler#handleAction(java.awt.event.ActionEvent)
	 */
	protected void handleAction(ActionEvent event)
	{
		int colNo = table.getSelectedColumn();
		int rowNo = table.getSelectedRow();
		MultipleSpecimenTableModel model = (MultipleSpecimenTableModel) table.getModel();
		String key = ((MultipleSpecimenTableModel) table.getModel()).getKey(rowNo, colNo);          
	    String collectionGroup = (String) model.getValueAt(AppletConstants.SPECIMEN_COLLECTION_GROUP_ROW_NO,colNo);
	    String specimenClass = (String) model.getValueAt(AppletConstants.SPECIMEN_CLASS_ROW_NO,colNo);
	    String parentSpecimenLabel = (String) model.getValueAt(AppletConstants.SPECIMEN_LABEL_ROW_NO,colNo);
	    String parentSpecimenBarcode = (String) model.getValueAt(AppletConstants.SPECIMEN_BARCODE_ROW_NO,colNo);
	    String parentSpecimenType = (String) model.getValueAt(AppletConstants.SPECIMEN_TYPE_ROW_NO,colNo);
		
	    Object[] parameters = new Object[]{Constants.ADD,key,collectionGroup,specimenClass,parentSpecimenLabel,parentSpecimenBarcode,parentSpecimenType}; 
			       
	    CommonAppletUtil.callJavaScriptFunction((JButton) event.getSource(),
				getJSMethodName(), parameters);
	}
	/**
	 * @return JS method name for this button.
	 */
	protected String getJSMethodName()
	{
		return "showDerivedSpecimenDialog";
	}

}