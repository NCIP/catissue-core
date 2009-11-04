
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
import edu.wustl.catissuecore.applet.SpecimenArrayCopyPasteValidator;
import edu.wustl.catissuecore.applet.model.SpecimenArrayTableModel;
import edu.wustl.catissuecore.applet.util.CommonAppletUtil;
import edu.wustl.common.util.logger.Logger;

/**
 * <p>This class initializes the fields of AbstractPasteActionHandler.java</p>.
 * @author Ashwin Gupta
 * @version 1.1
 */
public abstract class AbstractPasteActionHandler implements ActionListener
{
	/**
	 * Logger instance.
	 */
	private static final Logger LOGGER =
				Logger.getCommonLogger(AbstractPasteActionHandler.class);
	/**
	 * Table component used in applet.
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
	public AbstractPasteActionHandler()
	{
		//Empty AbstractPasteActionHandler.
	}

	/**
	 * constructor with table to persist table.
	 * @param table table used in applet
	 */
	public AbstractPasteActionHandler(JTable table)
	{
		this.table = table;
	}

	/**
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent).
	 * @param actionEvent : e
	 */
	public void actionPerformed(ActionEvent actionEvent)
	{
		this.preActionPerformed(actionEvent);
		this.doActionPerformed(actionEvent);
		this.postActionPerformed(actionEvent);
	}

	/**
	 * Pre action performed method for paste operation.
	 * @param actionEvent : e
	 */
	protected abstract void preActionPerformed(ActionEvent actionEvent);
	/**
	 * do action performed method for paste operation.
	 * Other paste listeners should override this method.
	 * @param actionEvent : e
	 */
	protected void doActionPerformed(ActionEvent actionEvent)
	{
		//super.handleAction(event);
		AbstractPasteActionHandler.LOGGER.info
			("\n<<<<<<<<<<<          PASTE         <<<<<<<<<<<<<<<<<<<<<<\n");
		AbstractPasteActionHandler.LOGGER.info("Inside AbstractPasteActionHandler");

		this.populateValidatorModel = true;
		final CopyPasteOperationValidatorModel validatorModel = CommonAppletUtil.getBaseTableModel(
				this.table).getCopyPasteOperationValidatorModel();
		validatorModel.setOperation(AppletConstants.PASTE_OPERATION);
		if (this.populateValidatorModel)
		{
			final int[] selectedColumns = this.table.getSelectedColumns();
			final int[] selectedRows = this.table.getSelectedRows();
			this.populateValidatorModel(validatorModel, selectedRows, selectedColumns);
		}

		BaseCopyPasteValidator validator = null;
		String validationMessage = null;
		validator = getValidator(validatorModel);
		if (validator != null)
		{
			validationMessage = validator.validate();
			if ("".equals(validationMessage))
			{
				this.isValidateSuccess = true;
				this.updateUI(validatorModel);
			}
			else
			{
				this.isValidateSuccess = false;
				AbstractPasteActionHandler.LOGGER.info
				(" validationMessage:: " + validationMessage);
				final Object[] paramArray = {validationMessage};
				CommonAppletUtil.callJavaScriptFunction(this.table, this.getJSMethodName(),
						paramArray);
			}
		}
	}
	/**
	 *
	 * @param validatorModel CopyPasteOperationValidatorModel object.
	 * @return BaseCopyPasteValidator object.
	 */
	private BaseCopyPasteValidator getValidator(CopyPasteOperationValidatorModel validatorModel)
	{
		BaseCopyPasteValidator validator = null;
		if ( (this.table != null) &&
				(this.table.getModel() instanceof SpecimenArrayTableModel))
		{
				validator = new SpecimenArrayCopyPasteValidator(validatorModel);
		}
		return validator ;
	}
	/**
	 * @param validatorModel : validatorModel
	 * @param selectedRows : selectedRows
	 * @param selectedColumns : selectedColumns
	 */
	private void populateValidatorModel(CopyPasteOperationValidatorModel validatorModel,
			int[] selectedRows, int[] selectedColumns)
	{
		validatorModel.setSelectedPastedRows(CommonAppletUtil.createListFromArray(selectedRows));
		validatorModel.setSelectedPastedCols(CommonAppletUtil.createListFromArray(selectedColumns));
		validatorModel.setOperation(AppletConstants.PASTE_OPERATION);
		validatorModel.setRowCount(this.table.getRowCount());
		validatorModel.setColumnCount(this.getColumnCount());
	}

	/**
	 * @param validatorModel : validatorModel
	 */
	private void updateUI(CopyPasteOperationValidatorModel validatorModel)
	{
		final HashMap dataMap = validatorModel.getCopiedData();
		AbstractPasteActionHandler.LOGGER.info("Copied Data : " + dataMap);
		int selectedRow = this.table.getSelectedRow();
		int selectedCol = this.table.getSelectedColumn();
		/**
		 *  In case of checkbox selection, selected row and selected column
		 *  should come from selected pasted rows and selected pasted columns respectively.
		 */
		if (!this.populateValidatorModel)
		{
			selectedRow = ((Integer) validatorModel.getSelectedPastedRows().get(0)).intValue();
			selectedCol = ((Integer) validatorModel.getSelectedPastedCols().get(0)).intValue();
		}
		this.setUI(validatorModel, dataMap, selectedRow, selectedCol);
	}

	/**
	 * @param validatorModel : validatorModel
	 * @param dataMap : dataMap
	 * @param selectedRow : selectedRow
	 * @param selectedCol : selectedCol
	 */
	private void setUI(CopyPasteOperationValidatorModel validatorModel, HashMap dataMap,
			int selectedRow, int selectedCol)
	{
		int selectRow = selectedRow ;
		final int tmpSelectedRow = selectedRow; // Row at which data is to be set cant be checkbox
		final List copiedRows = validatorModel.getSelectedCopiedRows();
		final List copiedCols = validatorModel.getSelectedCopiedCols();
		Collections.sort(copiedRows);
		Collections.sort(copiedCols);
		int selectedColumnIndex = selectedCol;

		for (int copiedColumnCount = 0; copiedColumnCount < copiedCols.size(); copiedColumnCount++)
		{
			final int copiedCol = ((Integer) (copiedCols.get(copiedColumnCount))).intValue();
			selectRow = tmpSelectedRow;
			for (int count = 0; count < copiedRows.size(); count++)
			{
				final int copiedRow = ((Integer) (copiedRows.get(count))).intValue();
				if (!this.isDisabledRow(selectRow)) 	//check for disabled rows
				{
					/**
					* Patch ID: Entered_Events_Need_To_Be_Visible_8  See also: 1-5
					* Description: If SPECIMEN_EVENTS_ROW_NO then dont retrive data in list
					* will throw a ClassCastException since toolTip is stored
					*  as a string and not as List finally call doPasteTooltip method
					*/
					if (copiedRow != AppletConstants.SPECIMEN_EVENTS_ROW_NO)
					{
						final String key = CommonAppletUtil.
						getDataKey(copiedRow, copiedCol);
						final List valueList = (List) dataMap.get(key);
						this.doPasteData(selectRow, selectedColumnIndex, valueList);
					}
				}
				selectRow = selectRow + 1;
			}
			selectedColumnIndex++;
		}
		/**
		 * Smita Kadam
		 * Reviewer: Sachin
		 * Bug ID: 4574
		 * Patch ID: 4574_2
		 * Description: Tooltip related processing done only for Multiple specimen table
		 */
		/** -- patch ends here -- */
		SwingUtilities.updateComponentTreeUI(this.table);
	}

	/**
	 * @param selectedRow selected row
	 * @param selectedCol selected col
	 * @param valueList : value
	 */
	protected abstract void doPasteData(int selectedRow, int selectedCol, List valueList);

	/**
	 * This method updates the tool tip of event button on all pasted columns.
	 * @param validatorModel Copy paste operation validator model
	 * @param dataMap map where toolTip is present of the coppied columns
	 */
	protected void doPasteTooltip(CopyPasteOperationValidatorModel validatorModel, HashMap dataMap)
	{
		final List copiedCols = validatorModel.getSelectedCopiedCols();
		final List pastedCols = validatorModel.getSelectedPastedCols();
		Collections.sort(pastedCols);
		Collections.sort(copiedCols);
		/**
		* Patch ID: Entered_Events_Need_To_Be_Visible_7
		* See also: 1-5
		* Description: Retrieve toolTip of copied column and paste it to selected pasted column
		*/
		/*for(int copiedColumnCount=0;copiedColumnCount<copiedCols.size();copiedColumnCount++)
		{
			int copiedCol = ((Integer) (copiedCols.get(copiedColumnCount))).intValue();
			int pastedCol = ((Integer) (pastedCols.get(copiedColumnCount))).intValue();
			String key2 = CommonAppletUtil.getDataKey
			(AppletConstants.SPECIMEN_EVENTS_ROW_NO,copiedCol);
			String toolTip=(String)dataMap.get(key2);
			TableColumnModel columnModel = table.getColumnModel();
			TableColumn tm = columnModel.getColumn(pastedCol);
			SpecimenColumnModel scm = (SpecimenColumnModel) tm.getCellEditor();
			scm.setEventstToolTipText(toolTip);
			scm.setToolTipToEventButton(toolTip);
		}*/
	}

	/** -- patch ends here -- */

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
		boolean flag = false ;
		return flag;
	}

	/**
	 * Post action performed method for paste operation.
	 * @param actionEvent : e
	 */
	protected void postActionPerformed(ActionEvent actionEvent)
	{
		final CopyPasteOperationValidatorModel validatorModel = CommonAppletUtil.getBaseTableModel(
				this.table).getCopyPasteOperationValidatorModel();
		validatorModel.setOperation("");
		AbstractPasteActionHandler.LOGGER.info
		("CopyPasteOperationValidatorModel operation set to : "
				+ validatorModel.getOperation());
		AbstractPasteActionHandler.LOGGER.info
		("\n\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@\n");
	}

	/**
	 * @return JS method name for this button.
	 */
	protected String getJSMethodName()
	{
		return "showErrorMessage";
	}

}