
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
import edu.wustl.catissuecore.applet.SpecimenArrayCopyPasteValidator;
import edu.wustl.catissuecore.applet.model.SpecimenArrayTableModel;
import edu.wustl.catissuecore.applet.util.CommonAppletUtil;

/**
 * <p>This class handles common copy operation among objects which have.
 * copy/paste operation.</p>
 * @author Ashwin Gupta
 * @version 1.1
 */
public abstract class AbstractCopyActionHandler implements ActionListener
{

	/**
	 * table component used in applet.
	 */
	protected JTable table;
	/**
	 * isValidateSuccess.
	 */
	protected boolean isValidateSuccess = true;
	/**
	 * populateValidatorModel.
	 */
	protected boolean populateValidatorModel = true;

	/**
	 * Default constructor.
	 */
	public AbstractCopyActionHandler()
	{
	}

	/**
	 * constructor with table to persist table.
	 * @param table table used in applet
	 */
	public AbstractCopyActionHandler(JTable table)
	{
		this.table = table;
	}

	/**
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 * @param e : e
	 */
	public void actionPerformed(ActionEvent e)
	{
		this.preActionPerformed(e);
		this.doActionPerformed(e);
		this.postActionPerformed(e);
	}

	/**
	 * Pre action performed method for copy operation.
	 * @param e : e
	 */
	protected void preActionPerformed(ActionEvent e)
	{
	}

	/**
	 * do action performed method for copy operation.
	 * Other copy listeners should override this method for specific operations.
	 * @param e : e
	 */
	protected void doActionPerformed(ActionEvent e)
	{
		this.populateValidatorModel();
	}

	/**
	 * Post action performed method for copy operation.
	 * @param e : e
	 */
	protected void postActionPerformed(ActionEvent e)
	{
	}

	/**
	 * Protected method populateValidatorModel.
	 */
	protected void populateValidatorModel()
	{
		//Following code is added for checkbox
		this.populateValidatorModel = true;
		final CopyPasteOperationValidatorModel validatorModel = new CopyPasteOperationValidatorModel();

		if (this.populateValidatorModel)
		{

			final int[] selectedColumns = this.table.getSelectedColumns();
			final int[] selectedRows = this.table.getSelectedRows();

			// poulate validator model
			this.populateValidatorModel(validatorModel, selectedRows, selectedColumns);

		}
		/*else
		 {
		 populateValidatorModel(validatorModel, intSelectedRows, intSelectedCols);
		 List selectedCopiedColumns = validatorModel.getSelectedCopiedCols();
		 TableColumnModel columnModel = table.getColumnModel();
		 for (int i = 0; i < selectedCopiedColumns.size(); i++)
		 {
		 int copiedColumn = ((Integer) selectedCopiedColumns.get(i)).intValue();
		 SpecimenColumnModel scm =
		  (SpecimenColumnModel) columnModel.getColumn(copiedColumn).getCellEditor();
		 scm.updateComponentValue(AppletConstants.SPECIMEN_CHECKBOX_ROW_NO, "false");
		 SpecimenColumnModel scmRenderer =
		  (SpecimenColumnModel) columnModel.getColumn(copiedColumn).getCellRenderer();
		 scmRenderer.updateComponent(AppletConstants.SPECIMEN_CHECKBOX_ROW_NO);
		 }
		 SwingUtilities.updateComponentTreeUI(table);

		 }*/
		BaseCopyPasteValidator validator = null;
		String validationMessage = null;

		if (this.table != null)
		{
			if (this.table.getModel() instanceof SpecimenArrayTableModel)
			{
				validator = new SpecimenArrayCopyPasteValidator(validatorModel);
			}
			/*else if (table.getModel() instanceof MultipleSpecimenTableModel)
			 {
			 //TODO put same as above construstor
			 validator = new MultipleSpecimenCopyPasteValidator(table, validatorModel);
			 }*/
		}

		if (validator != null)
		{
			validationMessage = validator.validate();
			if (validationMessage.equals(""))
			{
				this.isValidateSuccess = true;
				CommonAppletUtil.getBaseTableModel(this.table).setCopyPasteOperationValidatorModel(
						validatorModel);
			}
			else
			{
				this.isValidateSuccess = false;
				System.out.println(" validationMessage:: " + validationMessage);
				final Object[] paramArray = {validationMessage};
				CommonAppletUtil.callJavaScriptFunction(this.table, this.getJSMethodName(),
						paramArray);
			}
		}
	}

	/**
	 * @param validatorModel model
	 * @param selectedRows rows
	 * @param selectedColumns columns
	 */
	private void populateValidatorModel(CopyPasteOperationValidatorModel validatorModel,
			int[] selectedRows, int[] selectedColumns)
	{
		validatorModel.setSelectedCopiedRows(CommonAppletUtil.createListFromArray(selectedRows));
		validatorModel.setSelectedCopiedCols(CommonAppletUtil.createListFromArray(selectedColumns));
		validatorModel.setOperation(AppletConstants.COPY_OPERATION);
		validatorModel.setCopiedData(this.getSelectedData(selectedRows, selectedColumns));
		validatorModel.setRowCount(this.table.getRowCount());
		validatorModel.setColumnCount(this.getColumnCount());
	}

	/**
	 * This method returns the map holding the selected data.
	 * The key is represented by the row@column format.
	 * @param selectedRows : selectedRows
	 * @param selectedColumns : selectedColumns
	 * @return HashMap : HashMap
	 */
	protected HashMap getSelectedData(int[] selectedRows, int[] selectedColumns)
	{
		final HashMap map = new HashMap();
		for (final int selectedRow : selectedRows)
		{
			for (final int selectedColumn : selectedColumns)
			{
				final String key = CommonAppletUtil.getDataKey(selectedRow, selectedColumn);
				final List valueList = this.getValueList(selectedRow, selectedColumn);
				map.put(key, valueList);
			}
		}

		/**
		 * Smita Kadam
		 * Reviewer: Sachin
		 * Bug ID: 4574
		 * Patch ID: 4574_1
		 * Description: Tooltip related processing done only for Multiple specimen table
		 */
		/*	if (table.getModel() instanceof MultipleSpecimenTableModel)
		 {
		 for (int columnIndex = 0; columnIndex < selectedColumns.length; columnIndex++)
		 {
		 String key = CommonAppletUtil.getDataKey(AppletConstants.SPECIMEN_EVENTS_ROW_NO,
		  selectedColumns[columnIndex]);
		 String value = getEventTooltip(selectedColumns[columnIndex]);
		 map.put(key, value);
		 }
		 }*/
		return map;
	}

	/**
	 * gets event tool tip for given column
	 * @param columnIndex for which tool tip to retrieve
	 * @return tooltip of event button for given column
	 */
	/*protected String getEventTooltip(int columnIndex)
	 {
	 TableColumnModel columnModel = table.getColumnModel();
	 TableColumn tm = columnModel.getColumn(columnIndex);
	 SpecimenColumnModel scm = (SpecimenColumnModel) tm.getCellEditor();
	 if (scm == null || scm.getToolTipToEventButton()==null)
	 return "";
	 else
	 return scm.getToolTipToEventButton();
	 }*/
	/** -- patch ends here -- */
	/**
	 * @param rowIndex : rowIndex
	 * @param columnIndex : columnIndex
	 * @return List
	 */
	protected List getValueList(int rowIndex, int columnIndex)
	{
		final List valueList = new ArrayList();
		Object value = this.table.getValueAt(rowIndex, columnIndex);
		if (value == null)
		{
			value = "";
		}
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