/*
 * Created on Sep 19, 2006
 *
 * This class handles all the events related to TextFields.
 *
 */

package edu.wustl.catissuecore.applet.listener;

import java.awt.event.ActionEvent;

import javax.swing.JTable;
import javax.swing.JTextField;

/**
 * @author mandar_deshmukh
 * This class handles all the events related to TextFields.
 */
public class TextFieldHandler extends BaseActionHandler
{

	/*
	 * (non-Javadoc)
	 * @see edu.wustl.catissuecore.appletui.listener.caTissueHandler#handleAction(java.awt.event.ActionEvent)
	 */
	/**
	 * @param event : event
	 */
	protected void handleAction(ActionEvent event)
	{
		super.handleAction(event);
		System.out.println("Inside TextFieldHandler");
	}

	/**
	 * @param table : table
	 */
	public TextFieldHandler(JTable table)
	{
		super(table);
	}

	/**
	 * This method return the selected value of the combobox.
	 * @see edu.wustl.catissuecore.applet.listener.BaseActionHandler#getSelectedValue
	 * (java.awt.event.ActionEvent)
	 * @param event : event
	 * @return Object
	 */
	protected Object getSelectedValue(ActionEvent event)
	{
		JTextField selectedField = (JTextField) event.getSource();
		return selectedField.getText();
	}

}