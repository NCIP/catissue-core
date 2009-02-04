
package edu.wustl.catissuecore.applet.listener;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JTable;
import javax.swing.table.TableColumnModel;

import edu.wustl.catissuecore.applet.AppletConstants;
import edu.wustl.catissuecore.applet.AppletServerCommunicator;
import edu.wustl.catissuecore.applet.CopyPasteOperationValidatorModel;
import edu.wustl.catissuecore.applet.model.BaseAppletModel;
import edu.wustl.catissuecore.applet.model.MultipleSpecimenTableModel;
import edu.wustl.catissuecore.applet.model.SpecimenColumnModel;
import edu.wustl.catissuecore.applet.ui.MultipleSpecimenApplet;
import edu.wustl.catissuecore.applet.util.CommonAppletUtil;

/**
 * <p>This class initializes the fields of MultipleSpecimenPasteActionHandler.java</p>
 * @author Ashwin Gupta
 * @version 1.1
 */
public class MultipleSpecimenPasteActionHandler extends AbstractPasteActionHandler
{

	/**
	 * constructor with table to persist table
	 * @param table table used in applet
	 */
	public MultipleSpecimenPasteActionHandler(JTable table)
	{
		super(table);
	}

	/**
	 * @see edu.wustl.catissuecore.applet.listener.AbstractPasteActionHandler#doActionPerformed(java.awt.event.ActionEvent)
	 */
	protected void doActionPerformed(ActionEvent event)
	{
		super.doActionPerformed(event);

		if (isValidateSuccess)
		{
			MultipleSpecimenTableModel multipleSpecimenTableModel = CommonAppletUtil.getMultipleSpecimenTableModel(table);
			multipleSpecimenTableModel.showMapData();

			CopyPasteOperationValidatorModel validatorModel = multipleSpecimenTableModel.getCopyPasteOperationValidatorModel();
			List selectedCopiedRows = validatorModel.getSelectedCopiedRows();
			List selectedCopiedCols = validatorModel.getSelectedCopiedCols();

			/**
			 *  check if button(s) also copied
			 */
			boolean isButtonCopied = false;
			for (int i = 0; i < selectedCopiedRows.size(); i++)
			{
				int copiedRow = ((Integer) selectedCopiedRows.get(i)).intValue();
				if (copiedRow >= AppletConstants.SPECIMEN_COMMENTS_ROW_NO)
				{
					isButtonCopied = true;
					break;
				}
			}

			if (isButtonCopied)
			{
				//	List selectedCopiedCols = validatorModel.getSelectedCopiedCols();
				List tempSelectedCopiedCols = new ArrayList();
				for (int i = 0; i < selectedCopiedCols.size(); i++)
				{
					int copiedCol = ((Integer) selectedCopiedCols.get(i)).intValue();
					int pageIndex = validatorModel.getPageIndexWhenCopied();
					int columnsPerPage = multipleSpecimenTableModel.getColumnsPerPage();
					int colNumberWhenCopied = (columnsPerPage * (pageIndex - 1)) + copiedCol;
					Integer tempCopiedCol = new Integer(colNumberWhenCopied);
					tempSelectedCopiedCols.add(tempCopiedCol);
				}

				validatorModel.setSelectedCopiedCols(tempSelectedCopiedCols);

				List selectedPastedCols = validatorModel.getSelectedPastedCols();
				List selectedPastedRows = validatorModel.getSelectedPastedRows();
				List tempSelectedPastedCols = new ArrayList();

				for (int i = 0; i < selectedPastedCols.size(); i++)
				{
					int copiedCol = ((Integer) selectedPastedCols.get(i)).intValue();
					Integer tempCopiedCol = new Integer(multipleSpecimenTableModel.getActualColumnNo(copiedCol));
					tempSelectedPastedCols.add(tempCopiedCol);
				}
				validatorModel.setSelectedPastedCols(tempSelectedPastedCols);
				BaseAppletModel appletModel = new BaseAppletModel();

				Map validatorDataMap = new HashMap();
				validatorDataMap.put(AppletConstants.VALIDATOR_MODEL, validatorModel);
				appletModel.setData(validatorDataMap);
				try
				{
					MultipleSpecimenApplet applet = (MultipleSpecimenApplet) CommonAppletUtil.getBaseApplet(table);
					String url = applet.getServerURL() + "/MultipleSpecimenCopyPasteAction.do?method=paste";

					appletModel = (BaseAppletModel) AppletServerCommunicator.doAppletServerCommunication(url, appletModel);

				}
				catch (Exception e)
				{
					e.printStackTrace();
					System.out.println("Exception");
				}

				for (int j = 0; j < selectedCopiedCols.size(); j++)
				{
					int copiedColumn = ((Integer) tempSelectedCopiedCols.get(j)).intValue();
					int pastedColumn = ((Integer) selectedPastedCols.get(j)).intValue();
					System.out.println("\n\npastedColumn : " + pastedColumn);
					for (int i = 0; i < selectedCopiedRows.size(); i++)
					{
						int copiedRow = ((Integer) selectedCopiedRows.get(i)).intValue();
						int pastedRow = ((Integer) selectedPastedRows.get(i)).intValue();

						if (copiedRow >= AppletConstants.SPECIMEN_COMMENTS_ROW_NO)
						{
							Map buttonStatusMap = multipleSpecimenTableModel.getButtonStatusMap();

							//	SpecimenColumnModel copiedColumnModel =  (SpecimenColumnModel)table.getColumnModel().getColumn(copiedColumn).getCellRenderer();
							SpecimenColumnModel pastedColumnModel = (SpecimenColumnModel) table.getColumnModel().getColumn(pastedColumn)
									.getCellRenderer();
							// TODO
							//int displayCopiedColNo = multipleSpecimenTableModel.getDisplayColumnNo(pastedRow);
							if (copiedRow == AppletConstants.SPECIMEN_COMMENTS_ROW_NO)
							{
								String status = (String) buttonStatusMap.get(copiedColumn
										+ AppletConstants.MULTIPLE_SPECIMEN_BUTTON_MAP_KEY_SEPARATOR
										+ AppletConstants.MULTIPLE_SPECIMEN_COMMENTS_STRING);
								if (status == null)
								{
									status = AppletConstants.ADD;
								}

								pastedColumnModel.setEditCaptionFromJS("comments", status, multipleSpecimenTableModel);
							}
							if (copiedRow == AppletConstants.SPECIMEN_EVENTS_ROW_NO)
							{
								String status = (String) buttonStatusMap.get(copiedColumn
										+ AppletConstants.MULTIPLE_SPECIMEN_BUTTON_MAP_KEY_SEPARATOR
										+ AppletConstants.MULTIPLE_SPECIMEN_EVENTS_STRING);
								if (status == null)
								{
									status = AppletConstants.ADD;
								}
								pastedColumnModel.setEditCaptionFromJS("events", status, multipleSpecimenTableModel);
							}
							if (copiedRow == AppletConstants.SPECIMEN_BIOHAZARDS_ROW_NO)
							{
								String status = (String) buttonStatusMap.get(copiedColumn
										+ AppletConstants.MULTIPLE_SPECIMEN_BUTTON_MAP_KEY_SEPARATOR
										+ AppletConstants.MULTIPLE_SPECIMEN_BIOHAZARDS_STRING);
								if (status == null)
								{
									status = AppletConstants.ADD;
								}
								pastedColumnModel.setEditCaptionFromJS("biohazards", status, multipleSpecimenTableModel);
							}
							if (copiedRow == AppletConstants.SPECIMEN_EXTERNAL_IDENTIFIERS_ROW_NO)
							{
								String status = (String) buttonStatusMap.get(copiedColumn
										+ AppletConstants.MULTIPLE_SPECIMEN_BUTTON_MAP_KEY_SEPARATOR
										+ AppletConstants.MULTIPLE_SPECIMEN_EXTERNAL_IDENTIFIERS_STRING);
								if (status == null)
								{
									status = AppletConstants.ADD;
								}
								pastedColumnModel.setEditCaptionFromJS("externalIdentifier", status, multipleSpecimenTableModel);
							}
							if (copiedRow == AppletConstants.SPECIMEN_DERIVE_ROW_NO)
							{
								String status = (String) buttonStatusMap.get(copiedColumn
										+ AppletConstants.MULTIPLE_SPECIMEN_BUTTON_MAP_KEY_SEPARATOR
										+ AppletConstants.MULTIPLE_SPECIMEN_DERIVE_STRING);
								if (status == null)
								{
									status = AppletConstants.ADD;
								}
								pastedColumnModel.setEditCaptionFromJS("derived", status, multipleSpecimenTableModel);
							}

						}

					}

				}

				/*	int actualColNo = 1;
				 int displayColNo = multipleSpecimenTableModel.getDisplayColumnNo(actualColNo); 
				 SpecimenColumnModel columnModel =  (SpecimenColumnModel)table.getColumnModel().getColumn(displayColNo).getCellRenderer();
				 columnModel.setEditCaptionFromJS("comment");*/

				validatorModel.setSelectedCopiedCols(selectedCopiedCols);
				validatorModel.setSelectedPastedCols(selectedPastedCols);
			}
		}
	}

	protected void doPasteData(int selectedRow, int selectedCol, List valueList)
	{
		if (selectedRow < AppletConstants.SPECIMEN_COMMENTS_ROW_NO)
		{
			TableColumnModel columnModel = table.getColumnModel();
			Object value = valueList.get(0);
			SpecimenColumnModel scm = (SpecimenColumnModel) columnModel.getColumn(selectedCol).getCellEditor();
			System.out.println("\n\n\nselectedRow : " + selectedRow);
			if (scm.isCellEnabled(selectedRow))
			{
				table.changeSelection(selectedRow, selectedCol, false, false);
				scm.updateComponentValue(selectedRow, value.toString());
				SpecimenColumnModel scmRenderer = (SpecimenColumnModel) columnModel.getColumn(selectedCol).getCellRenderer();
				scmRenderer.updateComponent(selectedRow);
				CommonAppletUtil.getMultipleSpecimenTableModel(table).setValueAt(value, selectedRow, selectedCol);
			}
		}
	}

	protected boolean isDisabledRow(int rowNo)
	{
		boolean result = false;
		//rowNo == AppletConstants.SPECIMEN_PARENT_ROW_NO  removed after discussing with Santosh to enable Parent Specimen.
		//		if(rowNo == AppletConstants.SPECIMEN_STORAGE_LOCATION_ROW_NO   ) // rowNo == AppletConstants.SPECIMEN_PARENT_ROW_NO 
		//		{
		//			result =  true;
		//		}
		System.out.println(rowNo + " Row is disabled : " + result);
		return result;
	}

	/**
	 * @return JS method name for this button.
	 */
	protected String getJSMethodName()
	{
		return "showErrorMessage";
	}

	/**
	 * @return total coumn count
	 */
	protected int getColumnCount()
	{
		return CommonAppletUtil.getMultipleSpecimenTableModel(table).getTotalColumnCount();
	}

}