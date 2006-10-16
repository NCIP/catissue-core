package edu.wustl.catissuecore.applet.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.JTable;

import edu.wustl.catissuecore.applet.AppletConstants;
import edu.wustl.catissuecore.applet.BaseCopyPasteValidator;
import edu.wustl.catissuecore.applet.CopyPasteOperationValidatorModel;
import edu.wustl.catissuecore.applet.MultipleSpecimenCopyPasteValidator;
import edu.wustl.catissuecore.applet.SpecimenArrayCopyPasteValidator;
import edu.wustl.catissuecore.applet.model.MultipleSpecimenTableModel;
import edu.wustl.catissuecore.applet.model.SpecimenArrayTableModel;
import edu.wustl.catissuecore.applet.util.CommonAppletUtil;


/**
 * <p>This class handles common copy operation among objects which have 
 * copy/paste operation.</p>
 * @author Ashwin Gupta
 * @version 1.1
 */
public abstract class AbstractCopyActionHandler implements ActionListener 
{

	/**
	 * Table component used in applet 
	 */
	protected JTable table; 
	
	/**
	 * Default constructor 
	 */
	public AbstractCopyActionHandler()
	{
	}
	
	/**
	 * constructor with table to persist table
	 * @param table table used in applet
	 */
	public AbstractCopyActionHandler(JTable table)
	{
		this.table = table;
	}
	
	/**
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		preActionPerformed(e);
		doActionPerformed(e);
		postActionPerformed(e);
	}
	
	/**
	 * Pre action performed method for copy operation 
	 */
	protected void preActionPerformed(ActionEvent e)
	{
	}
	
	/**
	 * do action performed method for copy operation.
	 * Other copy listeners should override this method for specific operations. 
	 */
	protected void doActionPerformed(ActionEvent e)
	{
		populateValidatorModel();
	}
	
	/**
	 * Post action performed method for copy operation 
	 */
	protected void postActionPerformed(ActionEvent e)
	{
	}
	
	protected void populateValidatorModel()
	{
		//super.handleAction(event);
		CopyPasteOperationValidatorModel validatorModel = new CopyPasteOperationValidatorModel();
		int[] selectedColumns = table.getSelectedColumns();
		int[] selectedRows = table.getSelectedRows();
/*		CommonAppletUtil.printArray(selectedRows );
		CommonAppletUtil.printArray(selectedColumns );		  
*/
		// poulate validator model
		populateValidatorModel(validatorModel,selectedRows,selectedColumns);

/*		validatorModel.setSelectedCopiedRows(CommonAppletUtil.createListFromArray(selectedRows));
		validatorModel.setSelectedCopiedCols(CommonAppletUtil.createListFromArray(selectedColumns));
		validatorModel.setCopiedData(getSelectedData(selectedRows,selectedColumns));
		validatorModel.setOperation(AppletConstants.COPY_OPERATION);
*/		
		BaseCopyPasteValidator validator = null;
		String validationMessage = null;
		
		if(table != null)
		{
			if (table.getModel() instanceof SpecimenArrayTableModel)
			{
				validator = new SpecimenArrayCopyPasteValidator(validatorModel);
			}
			else if (table.getModel() instanceof MultipleSpecimenTableModel)
			{
				//TODO put same as above construstor
				validator = new MultipleSpecimenCopyPasteValidator(table,validatorModel);
			}
		}
		
		if (validator != null)
		{
			validationMessage = validator.validate();
			if (validationMessage.equals(""))
			{
				CommonAppletUtil.getBaseTableModel(table).setCopyPasteOperationValidatorModel(validatorModel);
			}
			else
			{
				System.out.println(" validationMessage:: " + validationMessage);
				Object[] paramArray = {validationMessage};
				CommonAppletUtil.callJavaScriptFunction(table,getJSMethodName(),paramArray);
			}
		}
	}
	
	/**
	 * @param validatorModel model
	 * @param selectedRows rows
	 * @param selectedColumns columns
	 */
	private void populateValidatorModel(CopyPasteOperationValidatorModel validatorModel, int[] selectedRows, int[] selectedColumns)
	{
		validatorModel.setSelectedCopiedRows(CommonAppletUtil.createListFromArray(selectedRows));
		validatorModel.setSelectedCopiedCols(CommonAppletUtil.createListFromArray(selectedColumns));
		validatorModel.setOperation(AppletConstants.COPY_OPERATION);
		validatorModel.setCopiedData(getSelectedData(selectedRows,selectedColumns));
		validatorModel.setRowCount(table.getRowCount());
		validatorModel.setColumnCount(getColumnCount());
	}
	/*
	 * This method returns the map holding the selected data.
	 * The key is represented by the row@column format.
	 */
	private HashMap getSelectedData(int[] selectedRows, int[] selectedColumns)
	{
		HashMap map = new HashMap();
		for(int rowIndex=0;rowIndex<selectedRows.length; rowIndex++  )
		{
			for(int columnIndex=0; columnIndex<selectedColumns.length; columnIndex++)
			{
				String key = CommonAppletUtil.getDataKey(selectedRows[rowIndex], selectedColumns[columnIndex]);
				List valueList = getValueList(selectedRows[rowIndex],selectedColumns[columnIndex]);
				map.put(key,valueList);
			}
		}
		return map;
	}
	
	/**
	 * @param rowIndex
	 * @param columnIndex
	 * @return
	 */
	protected List getValueList(int rowIndex,int columnIndex)
	{
		List valueList = new ArrayList();
		Object value = table.getValueAt(rowIndex,columnIndex);
		valueList.add(value.toString());
		return valueList;
	}
	
	/**
	 * get total no. of columns
	 * @return total coumn count
	 */
	protected abstract int getColumnCount();
	
	/**
	* @return JS method name for this button.
	*/
	protected String getJSMethodName()
	{
		return "showErrorMessage";
	}
	
}
