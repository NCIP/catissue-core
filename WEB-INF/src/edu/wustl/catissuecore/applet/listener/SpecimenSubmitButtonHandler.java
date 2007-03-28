
package edu.wustl.catissuecore.applet.listener;

import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.JViewport;

import edu.wustl.catissuecore.applet.AppletConstants;
import edu.wustl.catissuecore.applet.AppletServerCommunicator;
import edu.wustl.catissuecore.applet.model.BaseAppletModel;
import edu.wustl.catissuecore.applet.model.MultipleSpecimenTableModel;
import edu.wustl.catissuecore.applet.ui.MultipleSpecimenApplet;
import edu.wustl.catissuecore.applet.util.CommonAppletUtil;
import edu.wustl.catissuecore.util.global.Constants;

public class SpecimenSubmitButtonHandler extends ButtonHandler
{

	public SpecimenSubmitButtonHandler(JTable table)
	{
		super(table);
	}

	/**
	 * @see edu.wustl.catissuecore.applet.listener.ButtonHandler#handleAction(java.awt.event.ActionEvent)
	 */
	protected void handleAction(ActionEvent event)
	{
		MultipleSpecimenTableModel model = (MultipleSpecimenTableModel) table.getModel();

		BaseAppletModel appletModel = new BaseAppletModel();
		(table.getParent().getFocusCycleRootAncestor()).setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR)); 
		appletModel.setData(model.getMap());
		model.getMap().put(AppletConstants.NO_OF_SPECIMENS,
				String.valueOf(model.getTotalColumnCount()));
	

		model.getMap().put(AppletConstants.MULTIPLE_SPECIMEN_COLLECTION_GROUP_RADIOMAP,model.getCollectionGroupRadioButtonMap());
	
		try
		{
			MultipleSpecimenApplet applet = (MultipleSpecimenApplet) CommonAppletUtil
					.getBaseApplet(table);
			String url = applet.getServerURL()
					+ "/MultipleSpecimenAppletAction.do?method=submitSpecimens";

			appletModel = (BaseAppletModel) AppletServerCommunicator.doAppletServerCommunication(
					url, appletModel);
			
			Map resultMap = appletModel.getData();
			String target = (String) resultMap.get(Constants.MULTIPLE_SPECIMEN_RESULT);
			
			
			(table.getParent().getFocusCycleRootAncestor()).setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			
			if(target.equals(Constants.SUCCESS)) {
				Object[] parameters = new Object[]{target};
				CommonAppletUtil.callJavaScriptFunction((JButton) event.getSource(),
						"getSpecimenSubmitResult", parameters);

			}  else {
				Object[] parameters = new Object[]{resultMap.get(Constants.ERROR_DETAIL)};
				CommonAppletUtil.callJavaScriptFunction((JButton) event.getSource(),
						"showSpecimenErrorMessages", parameters);
				
			}

		}
		catch (Exception e)
		{
			e.printStackTrace();
			System.out.println("Exception");
		}

	}

	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.applet.listener.BaseActionHandler#preActionPerformed(java.awt.event.ActionEvent)
	 */
	protected void preActionPerformed(ActionEvent event)
	{
		//commented by mandar to check handling of selected data.
		CommonAppletUtil.getAllDataOnPage(table);
	}
}
