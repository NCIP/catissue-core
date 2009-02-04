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
		System.out.println("In PageLinkHandler");
		String pageNo = ((JButton)e.getSource()).getActionCommand() ;
		System.out.println("Page Clicked : "+ pageNo);
		System.out.println("------------------------------------");
		CommonAppletUtil.getMultipleSpecimenTableModel(table).showMapData();
		System.out.println("-------------------B4 removing columns-----------------");
		
		CommonAppletUtil.getMultipleSpecimenTableModel(table).setCurrentPageIndex(Integer.parseInt(pageNo));
		
		int columnCount = CommonAppletUtil.getMultipleSpecimenTableModel(table).getColumnCount();
		
		TableColumn column = null; 
		
		while(table.getColumnCount()>0) {
			column = table.getColumnModel().getColumn(0);
			table.removeColumn(column);
		}
		System.out.println("------------------------------------");
		CommonAppletUtil.getMultipleSpecimenTableModel(table).showMapData();
		System.out.println("-------------------After removing columns-----------------");
		 
		System.out.println("Columns removed : "+table.getColumnCount() ); 
		for (int i=0; i< columnCount;i++) {
			column = new TableColumn(i);	
			table.addColumn(column);
		}
		System.out.println("Columns added : "+table.getColumnCount() ); 
		System.out.println("------------------------------------");
		CommonAppletUtil.getMultipleSpecimenTableModel(table).showMapData();
		System.out.println("-------------------After adding columns-----------------");
		
		for (int i=0; i< columnCount;i++) {
			SpecimenColumnModel specimenColumnModel = new SpecimenColumnModel((BaseTable) table,i);
			specimenColumnModel.updateColumn();
		}        		
		System.out.println("Table updated  " ); 
		CommonAppletUtil.getMultipleSpecimenTableModel(table).showMapData();
		System.out.println("-------------------Done-----------------");
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
