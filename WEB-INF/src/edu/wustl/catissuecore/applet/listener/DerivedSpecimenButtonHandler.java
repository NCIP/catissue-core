/*
 * This class handles all the events of Map button.
 * Created on Sep 29, 2006
 * @author santosh_chandak
 */

package edu.wustl.catissuecore.applet.listener;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JRadioButton;
import javax.swing.JTable;

import edu.wustl.catissuecore.applet.AppletConstants;
import edu.wustl.catissuecore.applet.AppletServerCommunicator;
import edu.wustl.catissuecore.applet.model.BaseAppletModel;
import edu.wustl.catissuecore.applet.model.MultipleSpecimenTableModel;
import edu.wustl.catissuecore.applet.ui.MultipleSpecimenApplet;
import edu.wustl.catissuecore.applet.util.CommonAppletUtil;
import edu.wustl.catissuecore.util.global.Constants;

/**
 * This class handles all the events of Map button.
 */
public class DerivedSpecimenButtonHandler extends ButtonHandler
{

	
	/**
	 * @param table
	 */
	public DerivedSpecimenButtonHandler(JTable table, JRadioButton radioButton)
	{
		super(table,radioButton);
	}

	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.applet.listener.BaseActionHandler#handleAction(java.awt.event.ActionEvent)
	 */
	protected void handleAction(ActionEvent event)
	{
		if(event.getModifiers() == KeyEvent.VK_SHIFT )
	     {
		int colNo = table.getSelectedColumn();
		int rowNo = table.getSelectedRow();
		MultipleSpecimenTableModel model = (MultipleSpecimenTableModel) table.getModel();
		String key = ((MultipleSpecimenTableModel) table.getModel()).getKey(rowNo, colNo);
		String collectionGroup = (String) model.getValueAt(AppletConstants.SPECIMEN_COLLECTION_GROUP_ROW_NO, colNo);
		String specimenClass = (String) model.getValueAt(AppletConstants.SPECIMEN_CLASS_ROW_NO, colNo);
		// This is label of parent Speciemen for a Specimen (not derived)
		String parentSpecimenLabel = (String) model.getValueAt(AppletConstants.SPECIMEN_PARENT_ROW_NO, colNo);
		String parentSpecimenBarcode = (String) model.getValueAt(AppletConstants.SPECIMEN_BARCODE_ROW_NO, colNo);
		String parentSpecimenType = (String) model.getValueAt(AppletConstants.SPECIMEN_TYPE_ROW_NO, colNo);

		BaseAppletModel appletModel = new BaseAppletModel();
		Map data = new HashMap();
		// TODO check which radio is selected and set data accordingly
		data.put("parentSpecimenLabel", parentSpecimenLabel);
		appletModel.setData(data);
		MultipleSpecimenApplet applet = (MultipleSpecimenApplet) CommonAppletUtil.getBaseApplet(table);
		String url = applet.getServerURL() + "/MultipleSpecimenAppletAction.do?method=checkParentPresent";

		try
		{
			appletModel = (BaseAppletModel) AppletServerCommunicator.doAppletServerCommunication(url, appletModel);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		Map resultMap = appletModel.getData();
		Boolean isParentPresent = (Boolean) resultMap.get(Constants.MULTIPLE_SPECIMEN_RESULT);
		//parent enabled start
		String parentCollectionGroup =(String) resultMap.get(Constants.MULTIPLE_SPECIMEN_PARENT_COLLECTION_GROUP);
		if(parentCollectionGroup.trim().length() > 0  )
			collectionGroup = parentCollectionGroup;
		//parent enabled end
		
		Object[] parameters = new Object[]{Constants.ADD, key, collectionGroup, specimenClass, parentSpecimenLabel, parentSpecimenBarcode,
				parentSpecimenType, isParentPresent};

		CommonAppletUtil.callJavaScriptFunction((JButton) event.getSource(), getJSMethodName(), parameters);
	    }
	}

	/**
	 * @return JS method name for this button.
	 */
	protected String getJSMethodName()
	{
		return "showDerivedSpecimenDialog";
	}

}