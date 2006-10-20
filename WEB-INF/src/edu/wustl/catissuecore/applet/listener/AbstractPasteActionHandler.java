package edu.wustl.catissuecore.applet.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import javax.swing.JTable;
import javax.swing.SwingUtilities;

import edu.wustl.catissuecore.applet.AppletConstants;
import edu.wustl.catissuecore.applet.BaseCopyPasteValidator;
import edu.wustl.catissuecore.applet.CopyPasteOperationValidatorModel;
import edu.wustl.catissuecore.applet.MultipleSpecimenCopyPasteValidator;
import edu.wustl.catissuecore.applet.SpecimenArrayCopyPasteValidator;
import edu.wustl.catissuecore.applet.model.MultipleSpecimenTableModel;
import edu.wustl.catissuecore.applet.model.SpecimenArrayTableModel;
import edu.wustl.catissuecore.applet.util.CommonAppletUtil;


/**
 * <p>This class initializes the fields of AbstractPasteActionHandler.java</p>
 * @author Ashwin Gupta
 * @version 1.1
 */
public abstract class AbstractPasteActionHandler implements ActionListener {

	/**
	 * Table component used in applet 
	 */
	protected JTable table; 
	
	/**
	 * Default constructor 
	 */
	public AbstractPasteActionHandler()
	{
	}
	
	/**
	 * constructor with table to persist table
	 * @param table table used in applet
	 */
	public AbstractPasteActionHandler(JTable table)
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
	 * Pre action performed method for paste operation 
	 */
	protected void preActionPerformed(ActionEvent e)
	{
		
	}
	
	/**
	 * do action performed method for paste operation.
	 * Other paste listeners should override this method. 
	 */
	protected void doActionPerformed(ActionEvent e)
	{
			//super.handleAction(event);
			System.out.println("\n<<<<<<<<<<<          PASTE         <<<<<<<<<<<<<<<<<<<<<<\n");
			System.out.println("Inside AbstractPasteActionHandler");

			CopyPasteOperationValidatorModel validatorModel = CommonAppletUtil.getBaseTableModel(table).getCopyPasteOperationValidatorModel();
			validatorModel.setOperation(AppletConstants.PASTE_OPERATION);
			int[] selectedColumns = table.getSelectedColumns();
			int[] selectedRows = table.getSelectedRows();
/*			CommonAppletUtil.printArray(selectedRows );
			CommonAppletUtil.printArray(selectedColumns );
*/			// poulate validator model
			populateValidatorModel(validatorModel,selectedRows,selectedColumns);
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
					validator = new MultipleSpecimenCopyPasteValidator(table,validatorModel);
				}
			}
			
			if (validator != null)
			{
				validationMessage = validator.validate();
				if (validationMessage.equals(""))
				{
					updateUI(validatorModel);
				}
				else
				{
					System.out.println(" validationMessage:: " + validationMessage);
					Object[] paramArray = {validationMessage};
					CommonAppletUtil.callJavaScriptFunction(table,getJSMethodName(),paramArray);
				}
			}
	}
	
	private void populateValidatorModel(CopyPasteOperationValidatorModel validatorModel, int[] selectedRows, int[] selectedColumns)
	{
		validatorModel.setSelectedPastedRows(CommonAppletUtil.createListFromArray(selectedRows));
		validatorModel.setSelectedPastedCols(CommonAppletUtil.createListFromArray(selectedColumns));
		validatorModel.setOperation(AppletConstants.PASTE_OPERATION);
		validatorModel.setRowCount(table.getRowCount());
		validatorModel.setColumnCount(getColumnCount());
	}
	
	private void updateUI(CopyPasteOperationValidatorModel validatorModel)
	{
		HashMap dataMap = validatorModel.getCopiedData();
		System.out.println("Copied Data : "+ dataMap);
		int selectedRow = table.getSelectedRow();
		int selectedCol = table.getSelectedColumn ();
		setUI(validatorModel, dataMap, selectedRow, selectedCol);
	}
	
	private void setUI(CopyPasteOperationValidatorModel validatorModel, HashMap dataMap, int selectedRow, int selectedCol)
	{
		int tmpSelectedRow = selectedRow;
		List copiedRows = validatorModel.getSelectedCopiedRows();
		List copiedCols = validatorModel.getSelectedCopiedCols();
		Collections.sort(copiedRows);
		Collections.sort(copiedCols);
		int selectedColumnIndex = selectedCol;

		for(int copiedColumnCount=0; copiedColumnCount<copiedCols.size(); copiedColumnCount++)
		{
			int copiedCol = ((Integer)(copiedCols.get(copiedColumnCount))).intValue();
			selectedRow = tmpSelectedRow;
			for(int count=0;count<copiedRows.size();count++ )
			{
				int copiedRow = ((Integer)(copiedRows.get(count))).intValue();
				//check for disabled rows
				if(!isDisabledRow(selectedRow))
				{
					String key = CommonAppletUtil.getDataKey(copiedRow, copiedCol);
					List valueList = (List) dataMap.get(key);
					doPasteData(selectedRow,selectedColumnIndex,valueList);
				}
				selectedRow = selectedRow + 1;
			}
			selectedColumnIndex++;
		}
		SwingUtilities.updateComponentTreeUI(table);
	}
	
	/**
	 * @param selectedRow selected row
	 * @param selectedCol selected col
	 * @param value value
	 */
	protected abstract void doPasteData(int selectedRow,int selectedCol,List valueList);

	/**
	 * get total no. of columns
	 * @return total coumn count
	 */
	protected abstract int getColumnCount();
	
	/**
	 * @param rowNo boolean
	 * @return boolean value
	 */
	protected boolean isDisabledRow(int rowNo)
	{
		return false;
	}
	/**
	 * Post action performed method for paste operation 
	 */
	protected void postActionPerformed(ActionEvent e)
	{
		CopyPasteOperationValidatorModel validatorModel = CommonAppletUtil.getBaseTableModel(table).getCopyPasteOperationValidatorModel();
		validatorModel.setOperation("");
		System.out.println("CopyPasteOperationValidatorModel operation set to : "+validatorModel.getOperation());
		System.out.println("\n\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@\n");
	}
	
	/**
	* @return JS method name for this button.
	*/
	protected String getJSMethodName()
	{
		return "showErrorMessage";
	}
	
}
