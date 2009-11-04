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

import edu.wustl.common.util.logger.Logger;

/**
 * @author mandar_deshmukh
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class TableModelChangeHandler implements TableModelListener
{
	/**
	 * Logger Instance.
	 */
	private static final Logger LOGGER =
				Logger.getCommonLogger(TableModelChangeHandler.class);
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
	 * @param modelEvent : e
	 */
	public void tableChanged(TableModelEvent modelEvent)
	{
		TableModelChangeHandler.LOGGER.info("in TableModelChangeHandler");
		// Get anchor cell location
		final int rAnchor = this.table.getSelectionModel().getAnchorSelectionIndex();
		final int vcAnchor = this.table.getColumnModel().getSelectionModel()
				.getAnchorSelectionIndex();

		// This method is defined in
		// e915 Converting a Column Index Between the View and Model in a JTable Component
		final int mcAnchor = this.toModel(this.table, vcAnchor);

		// Get affected rows and columns
		final int firstRow = modelEvent.getFirstRow();
		final int lastRow = modelEvent.getLastRow();
		final int mColIndex = modelEvent.getColumn();

		if (firstRow != TableModelEvent.HEADER_ROW && rAnchor >= firstRow && rAnchor <= lastRow
				&& (mColIndex == TableModelEvent.ALL_COLUMNS || mColIndex == mcAnchor))
		{
			// Set the text field with the new value
			//textField.setText((String)table.getValueAt(rAnchor, vcAnchor));
			final Object obj = this.table.getComponentAt(rAnchor, vcAnchor);
			if (obj instanceof JTextField)
			{
				((JTextField) obj).setText((String) this.table.getValueAt(rAnchor, vcAnchor));
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
		int returnValue  ;
		if (vColIndex >= table.getColumnCount())
		{
			returnValue = -1;
		}
		else
		{
			returnValue = table.getColumnModel().getColumn(vColIndex).getModelIndex();
		}
		return returnValue ;
	}

}
