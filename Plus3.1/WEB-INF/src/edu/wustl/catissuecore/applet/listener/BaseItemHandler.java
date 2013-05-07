/**
 * This is base Handler class for all the Item events on the components in caTissuecore.
 * @author Mandar Deshmukh
 * Created on Sep 20, 2006
 */

package edu.wustl.catissuecore.applet.listener;

import java.awt.Component;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JComboBox;
import javax.swing.JRadioButton;
import javax.swing.JTable;
import javax.swing.JTextField;

import edu.wustl.common.util.logger.Logger;

/**
 * This is base Handler class for all the Item events on the components in caTissuecore.
 * @author Mandar Deshmukh
 */
public class BaseItemHandler implements ItemListener
{

	/**
	 * Logger instance..
	 */
	private static final Logger LOGGER = Logger.getCommonLogger(BaseItemHandler.class);
	/**
	 * table.
	 */
	protected JTable table;

	/**
	 * @param table : table
	 */
	public BaseItemHandler(JTable table)
	{
		this.table = table;
	}

	/**
	 * This method provides a hook to specific Listener.
	 * classes that needs to do some functionality before action executed.
	 */
	protected void preActionPerformed()
	{
		//Empty preActionPerformed method.
	}

	/**
	 * This method provides a hook to specific Listener.
	 * classes that needs to do some functionality after action executed.
	 * @param event : event
	 */
	protected void postActionPerformed(ItemEvent event)
	{
		BaseItemHandler.LOGGER.info("\n ************** In BaseItemHandler \n  : "
				+ this.getSelectedValue(event));
		BaseItemHandler.LOGGER.info("getSelectedValue(event) : " + this.getSelectedValue(event)
				+ " table.getSelectedRow() : " + this.table.getSelectedRow()
				+ " table.getSelectedColumn() : " + this.table.getSelectedColumn());
		//fireEditingStopped();
		if (this.table.getSelectedColumn() != -1)
		{
			this.table.getModel().setValueAt(this.getSelectedValue(event),
					this.table.getSelectedRow(), this.table.getSelectedColumn());
		}

	}

	/**
	 * This method returns the value of the source object on which the event occurs.
	 * This method is to be overridden by the subclasses for specific functionality.
	 * @param event Event Objcet.
	 * @return Value of source object on which the event occured.
	 */
	protected Object getSelectedValue(ItemEvent event)
	{
		return null;
	}

	/**
	 * This method handles specific action. Each subclass can provide custom handling.
	 * @param event : event
	 */
	protected void handleAction(ItemEvent event)
	{
		this.commonActions(event);
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ItemListener#itemStateChanged(java.awt.event.ItemEvent)
	 */
	/**
	 * @param event : event
	 */
	public void itemStateChanged(ItemEvent event)
	{
		this.preActionPerformed();
		this.handleAction(event);
		this.postActionPerformed(event);

	}

	/**
	 * @param event Object of event that occured.
	 * Performs common actions related to the two radio buttons of ParentSpecimen and Collection Group.
	 * It enables the respective text field and disables the other.
	 */
	protected void commonActions(ItemEvent event)
	{
		try
		{
			final JRadioButton selectedRadio = (JRadioButton) event.getSource();
			if (selectedRadio != null)
			{
				setComponents(selectedRadio);
			}
		}
		catch (final Exception excp)
		{
			LOGGER.error(excp.getMessage(), excp);
		}
		try
		{
			this.table.updateUI();
		}
		catch (final Exception excp)
		{
			LOGGER.error(excp.getMessage(), excp);
		}
	}
	/**
	 * Set the Components.
	 * @param selectedRadio Selected Radio Button
	 */
	protected void setComponents(JRadioButton selectedRadio)
	{
		try
		{
			final Component[] comps = selectedRadio.getParent().getComponents();

			for (final Component comp : comps)
			{
				if (comp instanceof JTextField) // for parent specimen
				{
					comp.setEnabled(selectedRadio.isSelected());
				}
				if (comp instanceof JComboBox) // for collection group
				{
					comp.setEnabled(selectedRadio.isSelected());
					comp.setFocusable(selectedRadio.isSelected());
				}
			}
		}
		catch(Exception exception)
		{
			LOGGER.error("EXception :" + exception.getMessage(),exception);
		}
	}
}
