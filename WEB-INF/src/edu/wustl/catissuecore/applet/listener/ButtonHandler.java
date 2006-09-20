/**
 * This class will handle events related to buttons. Common functionality will be handled in this class.
 * For specific functionality a sub class of this will be used.
 * 
 * Created on Sep 20, 2006 
 * @author mandar_deshmukh
 *
 */

package edu.wustl.catissuecore.applet.listener;

import java.awt.event.ActionEvent;

import javax.swing.JTable;

/**
 * This class will handle events related to buttons. Common functionality will be handled in this class.
 * For specific functionality a sub class of this will be used.
 * 
 * Created on Sep 20, 2006 
 * @author mandar_deshmukh
 *
 */

public class ButtonHandler extends BaseActionHandler {

	/**
	 * @param table
	 */
	public ButtonHandler(JTable table)
	{
		super(table);
	}
	

	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.applet.listener.BaseActionHandler#handleAction(java.awt.event.ActionEvent)
	 */
	protected void handleAction(ActionEvent event)
	{
		super.handleAction(event);
		System.out.println("Inside ButtonHandler");
	}
}
