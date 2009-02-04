/**
 * This is a Handler class for item events on the Parent Specimen radio button.
 * 
 * @author Mandar Deshmukh
 * Created on Sep 20, 2006
 * 
 */
package edu.wustl.catissuecore.applet.listener;

import java.awt.event.ItemEvent;

import javax.swing.JRadioButton;
import javax.swing.JTable;

import edu.wustl.catissuecore.applet.util.CommonAppletUtil;

/**
 * @author mandar_deshmukh
 *
 * This is a Handler class for item events on the Parent Specimen radio button.
 * 
 */
public class ParentSpecimenItemHandler extends BaseItemHandler {

	/**
	 * @param table
	 */
	public ParentSpecimenItemHandler(JTable table)
	{
		super(table);
	}
	
	

	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.applet.listener.BaseItemHandler#handleAction(java.awt.event.ItemEvent)
	 */
	protected void handleAction(ItemEvent event)
	{
		super.handleAction(event);
		System.out.println("In ParentSpecimenItemHandler");
		if(table.getSelectedColumn() != -1)
		{
			CommonAppletUtil.getMultipleSpecimenTableModel(table).setCollectionGroupRadioButtonValueAt(table.getSelectedColumn(),false); 
		}

	}
}
