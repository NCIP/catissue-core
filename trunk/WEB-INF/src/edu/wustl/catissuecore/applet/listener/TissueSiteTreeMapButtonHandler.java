/*
 * Created on Oct 30, 2006
 * @author mandar_deshmukh
 * 
 * This class handles the TissueSite TreeMap button events.
 */
package edu.wustl.catissuecore.applet.listener;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JTable;

import edu.wustl.catissuecore.applet.model.MultipleSpecimenTableModel;
import edu.wustl.catissuecore.applet.util.CommonAppletUtil;
import edu.wustl.catissuecore.util.global.Constants;


/*
 * Created on Oct 30, 2006
 * @author mandar_deshmukh
 * 
 * This class handles the TissueSite TreeMap button events.
 */
public class TissueSiteTreeMapButtonHandler extends ButtonHandler
{

	/**
	 * @param table
	 */
	public TissueSiteTreeMapButtonHandler(JTable table)
	{
		super(table);
	}

	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.applet.listener.BaseActionHandler#handleAction(java.awt.event.ActionEvent)
	 */
	protected void handleAction(ActionEvent event)
	{
		System.out.println("Inside TissueSiteTreeMapButtonHandler\n");
		if(event.getModifiers() == KeyEvent.VK_SHIFT )
	     {
			Object[] parameters = {new Integer(table.getSelectedColumn())};
			CommonAppletUtil.callJavaScriptFunction((JButton) event.getSource(),
					getJSMethodName(), parameters);
        }
	}

		/**
		 * @return JS method name for this button.
		 */
		protected String getJSMethodName()
		{
			return "showTreeMap";
		}
}
