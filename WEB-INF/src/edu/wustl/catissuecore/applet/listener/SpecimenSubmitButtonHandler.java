
package edu.wustl.catissuecore.applet.listener;

import java.awt.event.ActionEvent;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JTable;

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

		appletModel.setData(model.getMap());
		model.getMap().put(AppletConstants.NO_OF_SPECIMENS,
				String.valueOf(model.getTotalColumnCount()));

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

}
