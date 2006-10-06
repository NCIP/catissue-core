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
import javax.swing.border.LineBorder;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;

import edu.wustl.catissuecore.applet.AppletConstants;
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
//		MultipleSpecimenTableModel model = (MultipleSpecimenTableModel)table.getModel();
		// ----------------------------
		Object rowHeaders[] = MultipleSpecimenTableModel.getRowHeaders();
		labels = new JLabel[rowHeaders.length ];
		for(int counter=0; counter<rowHeaders.length; counter++)
		{
			LineBorder border = new LineBorder(Color.black ,1,false );
	//		labels[counter] = new JLabel(AppletConstants.MULTIPLE_SPECIMEN_MANDATORY+" "+(String)rowHeaders[counter]);
			labels[counter] = new JLabel(" "+(String)rowHeaders[counter]);
			labels[counter].setBackground(Color.lightGray  );
			labels[counter].setOpaque(true);
			//labels[counter].setBorder(border);
			//labels[counter].setSize(150,25 );
		}
		// ----------------------------
		TableColumnModel columnModel = table.getColumnModel();
		columnModel.getColumn(column).setCellRenderer(this);
		columnModel.getColumn(column).setCellEditor(this);
		columnModel.getColumn(column).setResizable(false );
		columnModel.getColumn(column).setPreferredWidth(150 );
		
	}
	
	/**
	 *  This method returns the component used as cell renderer. 
	 * @see javax.swing.table.TableCellRenderer#getTableCellRendererComponent(javax.swing.JTable, java.lang.Object, boolean, boolean, int, int)
	 */
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
			boolean hasFocus, int row, int column)
	{
		text = (value == null) ? "" : value.toString();
		Component component = getComponentAt(row, column, hasFocus, isSelected);
		return component;
	}
	
	/** 
	 * This method returns the component used as cell editor.
	 * @see javax.swing.table.TableCellEditor#getTableCellEditorComponent(javax.swing.JTable, java.lang.Object, boolean, int, int)
	 */
	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected,
			int row, int column)
	{
		text = (value == null) ? "" : value.toString();
		Component component = getComponentAt(row, column, false, isSelected);
		return component;
	}

	/**
	 * This method returns the value of the current editor component. 
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
		return comp;
	}

	/* This method returns false to indicate that the cells are noneditable.
	 * @see javax.swing.CellEditor#isCellEditable(java.util.EventObject)
	 */
	public boolean isCellEditable(EventObject anEvent) {
		return false;
	}

	/* Overriding the method of superclass. 
	 * @see javax.swing.CellEditor#shouldSelectCell(java.util.EventObject)
	 */
	public boolean shouldSelectCell(EventObject anEvent) {
		return true;
	}


}
