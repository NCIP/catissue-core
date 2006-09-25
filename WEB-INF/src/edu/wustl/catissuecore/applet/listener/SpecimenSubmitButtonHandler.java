
package edu.wustl.catissuecore.applet.listener;

import java.awt.event.ActionEvent;

import javax.swing.JTable;

import edu.wustl.catissuecore.applet.AppletConstants;
import edu.wustl.catissuecore.applet.AppletServerCommunicator;
import edu.wustl.catissuecore.applet.model.BaseAppletModel;
import edu.wustl.catissuecore.applet.model.MultipleSpecimenTableModel;
import edu.wustl.catissuecore.applet.ui.MultipleSpecimenApplet;
import edu.wustl.catissuecore.applet.util.CommonAppletUtil;

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
				String.valueOf(model.getColumnCount() - 1));

		try
		{
			MultipleSpecimenApplet applet = (MultipleSpecimenApplet) CommonAppletUtil
					.getBaseApplet(table);
			String url = applet.getServerURL()
					+ "/MultipleSpecimenAppletAction.do?method=submitSpecimens";

			appletModel = (BaseAppletModel) AppletServerCommunicator.doAppletServerCommunication(
					url, appletModel);

		}
		catch (Exception e)
		{
			e.printStackTrace();
			System.out.println("Exception");
		}

	}

}
