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

import edu.wustl.common.util.logger.Logger;

/**
 * @author mandar_deshmukh
 * This class handles all the events related to TextFields.
 */
public class TextFieldHandler extends BaseActionHandler
{
	/**
	 * Logger instance.
	 */
	private static final Logger LOGGER =
			Logger.getCommonLogger(TextFieldHandler.class);
	/*
	 * (non-Javadoc)
	 * @see edu.wustl.catissuecore.appletui.listener.caTissueHandler#handleAction(java.awt.event.ActionEvent)
	 */
	/**
	 * @param event : event
	 */
	@Override
	protected void handleAction(ActionEvent event)
	{
		super.handleAction(event);
		TextFieldHandler.LOGGER.info("Inside TextFieldHandler");
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
	@Override
	protected Object getSelectedValue(ActionEvent event)
	{
		final JTextField selectedField = (JTextField) event.getSource();
		return selectedField.getText();
	}

}