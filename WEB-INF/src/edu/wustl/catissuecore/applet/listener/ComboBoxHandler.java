package edu.wustl.catissuecore.applet.listener;

import java.awt.event.ActionEvent;

import javax.swing.JComboBox;
import javax.swing.JTable;


/**
 * This class is handler for JComboBox events.
 * 
 * @author Mandar Deshmukh.
 * @author Rahul Ner.
 * 
 *
 */
public class ComboBoxHandler extends BaseActionHandler
{
	

	public ComboBoxHandler(JTable table)
	{
		super(table);
	}

	/**
	 * @see edu.wustl.catissuecore.appletui.listener.BaseActionHandler#handleAction(java.awt.event.ActionEvent)
	 */
	protected void handleAction(ActionEvent event)
	{
		
	}
	protected Object getSelectedValue(ActionEvent event)
	{
		JComboBox cbx = (JComboBox) event.getSource();
		return cbx.getSelectedItem();
		 
	}
 
}
