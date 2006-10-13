package edu.wustl.catissuecore.applet.listener;

import java.awt.event.ActionEvent;
import java.util.HashMap;

import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.table.TableColumnModel;

import edu.wustl.catissuecore.applet.CopyPasteOperationValidatorModel;
import edu.wustl.catissuecore.applet.model.SpecimenColumnModel;
import edu.wustl.catissuecore.applet.util.CommonAppletUtil;

/**
 * <p>This class handles multiple specimen copy operation.This handler gets called
 * when user clicks on Copy button of multiple specimen.</p>
 * @author Ashwin Gupta
 * @author mandar_deshmukh
 * @version 1.1
 */
public class MultipleSpecimenCopyActionHandler extends
		AbstractCopyActionHandler {

	
	/**
	 * Empty constructor
	 */
	public MultipleSpecimenCopyActionHandler()
	{
		super();
	}
	/**
	 * @param table
	 */
	public MultipleSpecimenCopyActionHandler(JTable table)
	{
		super(table);
	}
	/**
	 * @see edu.wustl.catissuecore.applet.listener.AbstractCopyActionHandler#doActionPerformed(java.awt.event.ActionEvent)
	 */
	protected void doActionPerformed(ActionEvent e) 
	{
		//super.handleAction(event);
		System.out.println("\n>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>\n");
		System.out.println("Inside MultipleSpecimenCopyActionHandler");
		
		CopyPasteOperationValidatorModel validatorModel = new CopyPasteOperationValidatorModel();
		int[] selectedColumns = table.getSelectedColumns();
		int[] selectedRows = table.getSelectedRows();
		System.out.println("selectedRows : ");
		CommonAppletUtil.printArray(selectedRows );
		System.out.println(" \n selectedColumns : ");
		CommonAppletUtil.printArray(selectedColumns );		  
		
		validatorModel.setSelectedCopiedRows(CommonAppletUtil.createListFromArray(selectedRows));
		validatorModel.setSelectedCopiedCols(CommonAppletUtil.createListFromArray(selectedColumns));
		validatorModel.setCopiedData(getSelectedData(selectedRows,selectedColumns));
		
		CommonAppletUtil.getMultipleSpecimenTableModel(table).setCopyPasteOperationValidatorModel( validatorModel);
		System.out.println("\n >>>>>>>>>>>>>>   Copy Data Set.    >>>>>>>>>>>>");
		System.out.println("\n >>>>>>>>>>>>>>  DONE >>>>>>>>>>>>");
	}
	
	/*
	 * This method returns the map holding the selected data.
	 * The key is represented by the row@column format.
	 */
	private HashMap getSelectedData(int[] selectedRows, int[] selectedColumns)
	{
		System.out.println("\n/////////// inside getSelectedData ///////////////////\n");
		HashMap map = new HashMap();
		for(int rowIndex=0;rowIndex<selectedRows.length; rowIndex++  )
		{
			for(int columnIndex=0; columnIndex<selectedColumns.length; columnIndex++)
			{
				String key = CommonAppletUtil.getDataKey(selectedRows[rowIndex], selectedColumns[columnIndex]);
				//commented to check the values from cell editor
//				Object value = table.getValueAt(selectedRows[rowIndex],selectedColumns[columnIndex] );
				//--------
				TableColumnModel columnModel = table.getColumnModel();
				SpecimenColumnModel scm = (SpecimenColumnModel)columnModel.getColumn(selectedColumns[columnIndex]).getCellEditor();
				JComponent component = ((JComponent)scm.getTableCellEditorComponent(table,null,true,selectedRows[rowIndex],selectedColumns[columnIndex]));
				Object value =scm.getCellEditorValue(); 	
				// -------
				map.put(key,value );
			}
		}
		System.out.println("Returning Map -------------------------\n");
		System.out.println(map);
		return map;
	}
	

}
