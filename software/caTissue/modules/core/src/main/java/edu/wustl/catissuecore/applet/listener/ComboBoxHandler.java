
package edu.wustl.catissuecore.applet.listener;

import java.awt.event.ActionEvent;

import javax.swing.JComboBox;
import javax.swing.JTable;

/**
 * This class is handler for JComboBox events.
 * @author Mandar Deshmukh.
 * @author Rahul Ner.
 *
 */
public class ComboBoxHandler extends BaseActionHandler
{

	/**
	 * @param table : table
	 */
	public ComboBoxHandler(JTable table)
	{
		super(table);
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	/**
	 * @param event : event
	 */
	@Override
	public void actionPerformed(ActionEvent event)
	{

		if (this.table.getSelectedColumn() >= 0)
		{
			super.actionPerformed(event);
		}
	}

	/**
	 * @see edu.wustl.catissuecore.appletui.listener.BaseActionHandler#handleAction(java.awt.event.ActionEvent)
	 * @param event : event
	 */
	@Override
	protected void handleAction(ActionEvent event)
	{
		super.handleAction(event);
		//System.out.println("Inside ComboBoxHandler");
	}

	/**
	 * This method return the selected value of the combobox.
	 * @see edu.wustl.catissuecore.applet.listener.BaseActionHandler
	 * #getSelectedValue(java.awt.event.ActionEvent)
	 * @param event : event
	 * @return Object
	 */
	@Override
	protected Object getSelectedValue(ActionEvent event)
	{
		final JComboBox selectedField = (JComboBox) event.getSource();
		return selectedField.getSelectedItem();
	}

}
