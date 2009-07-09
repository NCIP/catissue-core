
package edu.wustl.catissuecore.applet.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JTable;

import edu.wustl.catissuecore.applet.AppletConstants;
import edu.wustl.catissuecore.applet.CopyPasteOperationValidatorModel;
import edu.wustl.catissuecore.applet.util.CommonAppletUtil;

/**
 * This is base Handler class for all of the component in tissuecore.
 * @author Mandar Deshmukh
 * @author Rahul Ner
 *
 */
public class BaseActionHandler implements ActionListener
{

	/**
	 * table.
	 */
	protected JTable table;
	/**
	 * @param table : table
	 */
	public BaseActionHandler(JTable table)
	{
		this.table = table;
	}

	/**
	 * This method is of the ActionListener interface.
	 * We call the three methods from it.
	 * These methods provide the ability to perform some tasks after the actual event handling is done.
	 * @param event : event
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent event)
	{
		//	if(!isPasteOperation())
		{
			//getMultipleSpecimenTableModel().showMapData();
			preActionPerformed(event);
		}
		handleAction(event);
		postActionPerformed(event);
	}

	/**
	 * This method provides a hook to specific Listener classes.
	 * that needs to do some functionality before action executed.
	 * @param event : event
	 */
	protected void preActionPerformed(ActionEvent event)
	{
		System.out.println("inside BaseActionHandler: - preActionPerformed");
		int colNo = table.getSelectedColumn();
		//	 	table.getColumnModel().getColumn(colNo).getCellEditor().stopCellEditing();
		table.getModel().setValueAt(getSelectedValue(event), table.getSelectedRow(),
				table.getSelectedColumn());
		System.out.println("table.getModel().setValueAt(getSelectedValue(event) : "
				+ table.getModel().getValueAt(table.getSelectedRow(), table.getSelectedColumn())
				+ " table.getSelectedRow() : " + table.getSelectedRow()
				+ " table.getSelectedColumn() : " + table.getSelectedColumn());
		System.out.println("inside BaseActionHandler: - preActionPerformed done");
	}

	/**
	 * This method provides a hook to specific Listener classes.
	 * that needs to do some functionality after action executed.
	 * @param event : event
	 */
	protected void postActionPerformed(ActionEvent event)
	{
	}

	/**
	 * This method returns the value of the source object on which the event occurs.
	 * This method is to be overridden by the subclasses for specific functionality.
	 * @param event Event Objcet.
	 * @return Value of source object on which the event occured.
	 */
	protected Object getSelectedValue(ActionEvent event)
	{
		return "*";
	}

	/**
	 * This method handles specific action. Each subclass can provide custom handling.
	 * @param event : event
	 */
	protected void handleAction(ActionEvent event)
	{

	}
	/**
	 * @return boolean
	 */
	private boolean isPasteOperation()
	{
		boolean result = false;

		CopyPasteOperationValidatorModel validatorModel = CommonAppletUtil.getBaseTableModel(table)
				.getCopyPasteOperationValidatorModel();
		if (!CommonAppletUtil.isNull(validatorModel))
		{
			String operationInValidatorModel = validatorModel.getOperation();
			if (!CommonAppletUtil.isNull(operationInValidatorModel))
			{
				if (operationInValidatorModel.equals(AppletConstants.PASTE_OPERATION))
				{
					result = true;
				}
				else
				{
					result = false;
				}

				System.out.println("operationInValidatorModel : " + operationInValidatorModel
						+ " , Result : " + result);
			}
			else
			{
				result = false;
			}
		}
		return result;
	}
	//	/**
	//	 * specific to MultipleSpecimen.
	//	 * @return
	//	 */
	//	protected MultipleSpecimenTableModel getMultipleSpecimenTableModel() {
	//		return (MultipleSpecimenTableModel) table.getModel();
	//	}
}
