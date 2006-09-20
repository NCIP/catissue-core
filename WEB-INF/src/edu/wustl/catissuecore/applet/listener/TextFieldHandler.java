/*
 * Created on Sep 19, 2006
 *
 * This class will handle all the events related to TextFields.
 * 
 */
package edu.wustl.catissuecore.applet.listener;

import java.awt.event.ActionEvent;

import javax.swing.JTable;
import javax.swing.JTextField;

/**
 * @author mandar_deshmukh
 * 
 * This class will handle all the events related to TextFields.
 */
public class TextFieldHandler extends BaseActionHandler {
	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.wustl.catissuecore.appletui.listener.caTissueHandler#handleAction(java.awt.event.ActionEvent)
	 */
	protected void handleAction(ActionEvent event)
	{
		super.handleAction(event);
		System.out.println("Inside TextFieldHandler");
		Object selectedValue = "";
		selectedValue = ((JTextField) (event.getSource())).getText();
	}

	/**
	 * @param table
	 */
	public TextFieldHandler(JTable table)
	{
		super(table);
	}
}