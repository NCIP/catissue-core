/**
 * This is base Handler class for all the Focus events on the components in caTissuecore.
 * 
 * @author Mandar Deshmukh
 * Created on Sep 22, 2006
 * 
 */
package edu.wustl.catissuecore.applet.listener;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JTable;
import javax.swing.JTextField;

/**
 * This is base Handler class for all the Focus events on the components in caTissuecore.
 * 
 * @author Mandar Deshmukh
 * Created on Sep 22, 2006
 * 
 */
public class BaseFocusHandler implements FocusListener {

	
	protected JTable table;
	
	public BaseFocusHandler(JTable table)
	{
		this.table = table;
	}

	/* (non-Javadoc)
	 * @see java.awt.event.FocusListener#focusGained(java.awt.event.FocusEvent)
	 */
	public void focusGained(FocusEvent e)
	{
		System.out.println("In BaseFocusHandler");
	}

	/* (non-Javadoc)
	 * @see java.awt.event.FocusListener#focusLost(java.awt.event.FocusEvent)
	 */
	public void focusLost(FocusEvent e)
	{
		System.out.println("In BaseFocusHandler");
		table.getModel().setValueAt(getSelectedValue(e),table.getSelectedRow(),table.getSelectedColumn());
		System.out.println(getSelectedValue(e)+" Row: "+ table.getSelectedRow()+ " Col: " + table.getSelectedColumn()); 
	}
	
	protected Object getSelectedValue(FocusEvent e)
	{
		Object data=null;
		if(e.getSource() instanceof JTextField)
		{
			JTextField sourceObject = (JTextField)e.getSource();
			data = sourceObject.getText(); 
		}
		return  data ;
	}

}
