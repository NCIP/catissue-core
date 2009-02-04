/*
 * Created on Oct 16, 2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package edu.wustl.catissuecore.applet.listener;

import java.awt.Component;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JComboBox;
import javax.swing.JTable;

import edu.wustl.catissuecore.applet.AppletConstants;


/**
 * @author mandar_deshmukh
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class MultipleSpecimenTableKeyHandler extends KeyAdapter
{
	JTable table;
	/**
	 * 
	 */
	public MultipleSpecimenTableKeyHandler(JTable table)
	{
		super();
		this.table = table;
		// TODO Auto-generated constructor stub
	}
	

	/* (non-Javadoc)
	 * @see java.awt.event.KeyListener#keyPressed(java.awt.event.KeyEvent)
	 */
	public void keyPressed(KeyEvent e)
	{
		System.out.println("\n>>>>>>>>>>>>>>> Inside MultipleSpecimenTableKeyHandler keyPressed >>>>>>>>>>>>>>>>>\n");
//		super.keyPressed(e);
		int keyCode = e.getKeyCode();
		if(keyCode == KeyEvent.VK_ENTER)
			table.getColumnModel().getColumn(table.getSelectedColumn()).getCellEditor().stopCellEditing();
//		int keyCode = e.getKeyCode();
//		System.out.println("Key : "+keyCode + " , KeyEvent.VK_TAB : "+ KeyEvent.VK_TAB );
//		if(keyCode == KeyEvent.VK_TAB)
//		{
//			System.out.println("Tab key pressed");
//			int row = table.getSelectedRow();
//			int col = table.getSelectedColumn();
//			System.out.println("Selected Row: "+ row + " , Col : "+ col);
//			//to stop the default handling.
//			e.consume();
//			table.changeSelection(row+1,col,false,false );
//		}
//		else if(e.getKeyCode() == KeyEvent.VK_DOWN && e.isAltDown())
//		{
//			int row = table.getSelectedRow();
//			int col = table.getSelectedColumn(); 
//			if(row == AppletConstants.SPECIMEN_TISSUE_SIDE_ROW_NO)
//			{
//				Object obj = table.getCellEditor().getTableCellEditorComponent(table,null,true,row,col );
//				if(obj instanceof JComboBox)
//				{
//					((JComboBox)obj).firePopupMenuWillBecomeVisible(); 
//				}
//			}
//		}
		System.out.println("<<<<<<<<<<<<<<<<<<<< Key Pressed Done >>>>>>>>>>>>>");
	}
}
