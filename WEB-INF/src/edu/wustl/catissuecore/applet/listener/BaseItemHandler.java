/**
 * This is base Handler class for all the Item events on the components in caTissuecore.
 * 
 * @author Mandar Deshmukh
 * Created on Sep 20, 2006
 * 
 */

package edu.wustl.catissuecore.applet.listener;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JTable;

/**
 * This is base Handler class for all the Item events on the components in caTissuecore.
 * 
 * @author Mandar Deshmukh
 * 
 */
public class BaseItemHandler implements ItemListener {

	protected JTable table;
	
	public BaseItemHandler(JTable table)
	{
		this.table = table;
	}

	/**
	 * This method provides a hook to specific Listener classes that needs to do some functionality before action executed. 
	 */
	protected void preActionPerformed()
	{
	
	}

	/**
	 * This method provides a hook to specific Listener classes that needs to do some functionality after action executed. 
	 */
	protected void postActionPerformed()
	{
		//fireEditingStopped();
		table.getModel().setValueAt(getSelectedValue(),table.getSelectedRow(),table.getSelectedColumn());

	}

	protected Object getSelectedValue() {
		return null;
	}
	
	/**
	 * This method handles specific action. Each subclass can provide custom handling.
	 */
	protected void handleAction(ItemEvent event)
	{
		
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ItemListener#itemStateChanged(java.awt.event.ItemEvent)
	 */
	public void itemStateChanged(ItemEvent event) {
		preActionPerformed();		
		handleAction(event);
		postActionPerformed();

	}

}
