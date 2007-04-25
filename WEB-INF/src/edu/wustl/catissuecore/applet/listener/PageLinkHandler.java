/*
 * Created on Sep 29, 2006
 * @author mandar_deshmukh
 * 
 * This class handles the events related to Page links.
 *  
 */
package edu.wustl.catissuecore.applet.listener;

import java.awt.event.ActionEvent;

import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.table.TableColumn;

import edu.wustl.catissuecore.applet.component.BaseTable;
import edu.wustl.catissuecore.applet.model.SpecimenColumnModel;
import edu.wustl.catissuecore.applet.util.CommonAppletUtil;

/**
 * @author mandar_deshmukh
 *
 * This class handles the events related to Page links.
 */
public class PageLinkHandler extends ButtonHandler 
{

	/**
	 * @param table
	 */
	public PageLinkHandler(JTable table)
	{
		super(table);
	}

	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.applet.listener.BaseActionHandler#handleAction(java.awt.event.ActionEvent)
	 */
	protected void handleAction(ActionEvent e)
	{
		String pageNo = ((JButton)e.getSource()).getActionCommand() ;
				
		CommonAppletUtil.getMultipleSpecimenTableModel(table).setCurrentPageIndex(Integer.parseInt(pageNo));
		
		int columnCount = CommonAppletUtil.getMultipleSpecimenTableModel(table).getColumnCount();
		
		TableColumn column = null; 
		
		while(table.getColumnCount()>0) {
			column = table.getColumnModel().getColumn(0);
			table.removeColumn(column);
		}
				 
		for (int i=0; i< columnCount;i++) {
			column = new TableColumn(i);	
			table.addColumn(column);
		}
			
		for (int i=0; i< columnCount;i++) {
			SpecimenColumnModel specimenColumnModel = new SpecimenColumnModel((BaseTable) table,i);
			specimenColumnModel.updateColumn();
		}        		
	}
	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.applet.listener.BaseActionHandler#preActionPerformed(java.awt.event.ActionEvent)
	 */
	protected void preActionPerformed(ActionEvent event)
	{
		//commented by mandar to check handling of selected data.
		CommonAppletUtil.getAllDataOnPage(table);
	}
}
