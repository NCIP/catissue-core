/*
 * Created on Sep 20, 2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package edu.wustl.catissuecore.applet.listener;

import java.awt.event.ActionEvent;

import javax.swing.JTable;

/**
 * @author mandar_deshmukh
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
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
