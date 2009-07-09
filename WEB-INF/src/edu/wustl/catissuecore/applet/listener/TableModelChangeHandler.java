/*
 * Created on Sep 22, 2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

package edu.wustl.catissuecore.applet.listener;

import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

/**
 * @author mandar_deshmukh
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class TableModelChangeHandler implements TableModelListener
{
	/**
	 * table.
	 */
	protected JTable table;

	/**
	 * @param table : table
	 */
	public TableModelChangeHandler(JTable table)
	{
		this.table = table;
	}

	/* (non-Javadoc)
	 * @see javax.swing.event.TableModelListener#tableChanged(javax.swing.event.TableModelEvent)
	 */
	/**
	 * @param e : e
	 */
	public void tableChanged(TableModelEvent e)
	{
		System.out.println("in TableModelChangeHandler");
		// Get anchor cell location
		int rAnchor = table.getSelectionModel().getAnchorSelectionIndex();
		int vcAnchor = table.getColumnModel().getSelectionModel().getAnchorSelectionIndex();

		// This method is defined in
		// e915 Converting a Column Index Between the View and Model in a JTable Component
		int mcAnchor = toModel(table, vcAnchor);

		// Get affected rows and columns
		int firstRow = e.getFirstRow();
		int lastRow = e.getLastRow();
		int mColIndex = e.getColumn();

		if (firstRow != TableModelEvent.HEADER_ROW && rAnchor >= firstRow && rAnchor <= lastRow
				&& (mColIndex == TableModelEvent.ALL_COLUMNS || mColIndex == mcAnchor))
		{
			// Set the text field with the new value
			//textField.setText((String)table.getValueAt(rAnchor, vcAnchor));
			Object obj = table.getComponentAt(rAnchor, vcAnchor);
			if (obj instanceof JTextField)
			{
				((JTextField) obj).setText((String) table.getValueAt(rAnchor, vcAnchor));
			}
			//                else if(obj instanceof JComboBox )
			//                {
			//                	JComboBox combo = (JComboBox)obj;
			//                	combo.setModel( )
			//                }

		}
	}
	/**
	 * @param table : table
	 * @param vColIndex : vColIndex
	 * @return int
	 */
	public int toModel(JTable table, int vColIndex)
	{
		if (vColIndex >= table.getColumnCount())
		{
			return -1;
		}
		return table.getColumnModel().getColumn(vColIndex).getModelIndex();
	}

}
