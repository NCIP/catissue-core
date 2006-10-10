package edu.wustl.catissuecore.applet.listener;

import java.awt.event.ActionEvent;

import javax.swing.JMenuItem;
import javax.swing.JTable;


/**
 * <p>This class is used to handle Array level copy operation.</p>
 * @author Ashwin Gupta
 * @version 1.1
 */
public class ArrayCopyOptionActionHandler extends AbstractCopyActionHandler 
{

	/**
	 * constructor with table to persist table objet.
	 * @param table table used in applet
	 */
	public ArrayCopyOptionActionHandler(JTable table)
	{
		super(table);
	}
	
	/**
	 * @see edu.wustl.catissuecore.applet.listener.AbstractCopyActionHandler#doActionPerformed()
	 */
	protected void doActionPerformed(ActionEvent e) {
		JMenuItem menuItem = (JMenuItem) e.getSource();
		System.out.println(menuItem.getText());
	}
}
