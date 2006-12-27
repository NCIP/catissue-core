
package edu.wustl.catissuecore.applet.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.TableColumnModel;

import edu.wustl.catissuecore.applet.AppletConstants;
import edu.wustl.catissuecore.applet.BaseCopyPasteValidator;
import edu.wustl.catissuecore.applet.CopyPasteOperationValidatorModel;
import edu.wustl.catissuecore.applet.MultipleSpecimenCopyPasteValidator;
import edu.wustl.catissuecore.applet.SpecimenArrayCopyPasteValidator;
import edu.wustl.catissuecore.applet.model.MultipleSpecimenTableModel;
import edu.wustl.catissuecore.applet.model.SpecimenArrayTableModel;
import edu.wustl.catissuecore.applet.model.SpecimenColumnModel;
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

	protected boolean isValidateSuccess = true;

	protected boolean populateValidatorModel = true;

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
	public void actionPerformed(ActionEvent e)
	{
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
		
		/**
		 *  Following code is added for checkbox
		 */
		populateValidatorModel = true;
		int[] intSelectedRows = null;
		int[] intSelectedCols = null;
		if (table.getModel() instanceof MultipleSpecimenTableModel)
		{
			MultipleSpecimenTableModel multipleSpecimenTableModel = CommonAppletUtil.getMultipleSpecimenTableModel(table);
			Map checkBoxMap = multipleSpecimenTableModel.getSpecimenCheckBoxMap();
			int columnsPerPage = multipleSpecimenTableModel.getColumnsPerPage();
			if (checkBoxMap != null && checkBoxMap.size() > 0)
			{
				List selectedRows = new ArrayList();
				List selectedCols = new ArrayList();
				Iterator itr = checkBoxMap.keySet().iterator();
				while (itr.hasNext())
				{
					String key = (String) itr.next();
					if (checkBoxMap.get(key) != null && ((Boolean) checkBoxMap.get(key)).booleanValue() == true)
					{
						int colNo = Integer.parseInt(key) - 1;
						selectedCols.add(new Integer(colNo % columnsPerPage));
						checkBoxMap.put(key, new Boolean(false));
						populateValidatorModel = false;
					}
				}
				if (populateValidatorModel == false)
				{
					for (int i = AppletConstants.SPECIMEN_COLLECTION_GROUP_ROW_NO; i <= AppletConstants.SPECIMEN_DERIVE_ROW_NO; i++)
					{
						selectedRows.add(new Integer(i));
					}
					Collections.sort(selectedCols);

					intSelectedRows = new int[selectedRows.size()];
					for (int i = 0; i < selectedRows.size(); i++)
					{
						intSelectedRows[i] = ((Integer) selectedRows.get(i)).intValue();
					}
					intSelectedCols = new int[selectedCols.size()];
					for (int i = 0; i < selectedCols.size(); i++)
					{
						intSelectedCols[i] = ((Integer) selectedCols.get(i)).intValue();
					}

				}

			}

		}

		CopyPasteOperationValidatorModel validatorModel = new CopyPasteOperationValidatorModel();

		if (populateValidatorModel)
		{

			int[] selectedColumns = table.getSelectedColumns();
			int[] selectedRows = table.getSelectedRows();

			// poulate validator model
			populateValidatorModel(validatorModel, selectedRows, selectedColumns);

		}
		else
		{
			populateValidatorModel(validatorModel, intSelectedRows, intSelectedCols);
			List selectedCopiedColumns = validatorModel.getSelectedCopiedCols();
			TableColumnModel columnModel = table.getColumnModel();
			for (int i = 0; i < selectedCopiedColumns.size(); i++)
			{
				int copiedColumn = ((Integer) selectedCopiedColumns.get(i)).intValue();
				SpecimenColumnModel scm = (SpecimenColumnModel) columnModel.getColumn(copiedColumn).getCellEditor();
				scm.updateComponentValue(AppletConstants.SPECIMEN_CHECKBOX_ROW_NO, "false");
				SpecimenColumnModel scmRenderer = (SpecimenColumnModel) columnModel.getColumn(copiedColumn).getCellRenderer();
				scmRenderer.updateComponent(AppletConstants.SPECIMEN_CHECKBOX_ROW_NO);
			}
			SwingUtilities.updateComponentTreeUI(table);

		}
		BaseCopyPasteValidator validator = null;
		String validationMessage = null;

		if (table != null)
		{
			if (table.getModel() instanceof SpecimenArrayTableModel)
			{
				validator = new SpecimenArrayCopyPasteValidator(validatorModel);
			}
			else if (table.getModel() instanceof MultipleSpecimenTableModel)
			{
				//TODO put same as above construstor
				validator = new MultipleSpecimenCopyPasteValidator(table, validatorModel);
			}
		}

		if (validator != null)
		{
			validationMessage = validator.validate();
			if (validationMessage.equals(""))
			{
				isValidateSuccess = true;
				CommonAppletUtil.getBaseTableModel(table).setCopyPasteOperationValidatorModel(validatorModel);
			}
			else
			{
				isValidateSuccess = false;
				System.out.println(" validationMessage:: " + validationMessage);
				Object[] paramArray = {validationMessage};
				CommonAppletUtil.callJavaScriptFunction(table, getJSMethodName(), paramArray);
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
		validatorModel.setCopiedData(getSelectedData(selectedRows, selectedColumns));
		validatorModel.setRowCount(table.getRowCount());
		validatorModel.setColumnCount(getColumnCount());
	}

	/*
	 * This method returns the map holding the selected data.
	 * The key is represented by the row@column format.
	 */
	protected HashMap getSelectedData(int[] selectedRows, int[] selectedColumns)
	{
		HashMap map = new HashMap();
		for (int rowIndex = 0; rowIndex < selectedRows.length; rowIndex++)
		{
			for (int columnIndex = 0; columnIndex < selectedColumns.length; columnIndex++)
			{
				String key = CommonAppletUtil.getDataKey(selectedRows[rowIndex], selectedColumns[columnIndex]);
				List valueList = getValueList(selectedRows[rowIndex], selectedColumns[columnIndex]);
				map.put(key, valueList);
			}
		}
		return map;
	}

	/**
	 * @param rowIndex
	 * @param columnIndex
	 * @return
	 */
	protected List getValueList(int rowIndex, int columnIndex)
	{
		List valueList = new ArrayList();
		Object value = table.getValueAt(rowIndex, columnIndex);
		if (value == null)
			value = "";
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