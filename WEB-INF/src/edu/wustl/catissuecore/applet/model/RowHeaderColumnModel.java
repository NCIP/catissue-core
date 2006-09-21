/**
 * This class provides the functionality of row header.
 * It is a Column Model for the zeroth column.
 * 
 * @author mandar_deshmukh
 * Created on Sep 21, 2006
 * 
 */

package edu.wustl.catissuecore.applet.model;

import java.awt.Color;
import java.awt.Component;
import java.util.EventObject;

import javax.swing.AbstractCellEditor;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;

import edu.wustl.catissuecore.applet.component.BaseTable;

/**
 * @author mandar_deshmukh
 *
 * This class provides the functionality of row header.
 * It is a Column Model for the zeroth column.
 * 
 */
public class RowHeaderColumnModel extends AbstractCellEditor
		implements TableCellEditor, TableCellRenderer
{
	/**
	 * Default Serial Version ID
	 */
	private static final long serialVersionUID = 1L;
	private final int column = 0;
	//Reference of Table
	BaseTable table;
	JLabel labels[];
	String text="";
	public RowHeaderColumnModel(BaseTable table)
	{
		super();
		this.table = table;
		MultipleSpecimenTableModel model = (MultipleSpecimenTableModel)table.getModel();
		// ----------------------------
		Object rowHeaders[] = model.getRowHeaders();
		labels = new JLabel[rowHeaders.length ];
		for(int counter=0; counter<rowHeaders.length; counter++)
		{
			labels[counter] = new JLabel((String)rowHeaders[counter]);
			labels[counter].setBackground(Color.GRAY);
		}
		// ----------------------------
		TableColumnModel columnModel = table.getColumnModel();
		columnModel.getColumn(column).setCellRenderer(this);
		columnModel.getColumn(column).setCellEditor(this);
	}
	
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
			boolean hasFocus, int row, int column)
	{
		text = (value == null) ? "" : value.toString();
		Component component = getComponentAt(row, column, hasFocus, isSelected);
		return component;
	}

	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected,
			int row, int column)
	{
		text = (value == null) ? "" : value.toString();
		Component component = getComponentAt(row, column, false, isSelected);
		return component;
	}

	/**
	 * @see javax.swing.CellEditor#getCellEditorValue()
	 */
	public Object getCellEditorValue()
	{
		return text;
	}

	/**
	 * Returns a component at specified co-ordinate and sets appropriate foregroung and background.
	 * 
	 * @param row Row No
	 * @param col Col No
	 * @param hasFocus 
	 * @param isSelected
	 * @return Component
	 */
	private Component getComponentAt(int row, int col, boolean hasFocus, boolean isSelected)
	{
		Component comp = null; 

		comp = labels[row];	
		// ------------------
		comp.setBackground(Color.GRAY  );

//		if (hasFocus)
//		{
//			comp.setForeground(table.getForeground());
//			comp.setBackground(UIManager.getColor("List.background"));
//		}
//		else if (isSelected)
//		{
//			comp.setForeground(table.getSelectionForeground());
//			comp.setBackground(table.getSelectionBackground());
//		}
//		else
//		{
//			comp.setForeground(table.getForeground());
//			comp.setBackground(UIManager.getColor("List.background"));
//		}
		return comp;
	}

	/* (non-Javadoc)
	 * @see javax.swing.CellEditor#isCellEditable(java.util.EventObject)
	 */
	public boolean isCellEditable(EventObject anEvent) {
		return false;
	}

	/* (non-Javadoc)
	 * @see javax.swing.CellEditor#shouldSelectCell(java.util.EventObject)
	 */
	public boolean shouldSelectCell(EventObject anEvent) {
		return false;
	}


}
