/*
 * Created on Oct 18, 2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package edu.wustl.catissuecore.applet.component;

import javax.swing.ListSelectionModel;
import javax.swing.event.ChangeEvent;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;


/**
 * @author mandar_deshmukh
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class MultipleSpecimenTable extends BaseTable
{

	/**
	 * 
	 */
	public MultipleSpecimenTable()
	{
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param tableModel
	 */
	public MultipleSpecimenTable(TableModel tableModel)
	{
		super(tableModel);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param tableModel
	 * @param tableColumnModel
	 */
	public MultipleSpecimenTable(TableModel tableModel, TableColumnModel tableColumnModel)
	{
		super(tableModel, tableColumnModel);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param tableModel
	 * @param tableColumnModel
	 * @param listSelectionModel
	 */
	public MultipleSpecimenTable(TableModel tableModel, TableColumnModel tableColumnModel,
			ListSelectionModel listSelectionModel)
	{
		super(tableModel, tableColumnModel, listSelectionModel);
		// TODO Auto-generated constructor stub
	}

	//Mandar: to override default behaviour : 18Oct06
    public void editingStopped(ChangeEvent e) {
        // Take in the new value
        TableCellEditor editor = getCellEditor();
        if (editor != null) {
            Object value = editor.getCellEditorValue();
            //commented to prevent setting the value automatically
           // setValueAt(value, editingRow, editingColumn);
            removeEditor();
        }
    }


}
