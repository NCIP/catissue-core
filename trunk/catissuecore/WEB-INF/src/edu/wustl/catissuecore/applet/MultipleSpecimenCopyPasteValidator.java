
package edu.wustl.catissuecore.applet;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JTable;

import edu.wustl.catissuecore.applet.model.MultipleSpecimenTableModel;
import edu.wustl.catissuecore.applet.util.CommonAppletUtil;

/**
 * <p>This class performs the actual validations required for copy-paste operation of multiple specimen.</p>
 * @author Santosh Chandak
 * @version 1.1
 */
public class MultipleSpecimenCopyPasteValidator extends BaseCopyPasteValidator
{

	/**
	 * Reference of the table.
	 */
	protected JTable table;

	public MultipleSpecimenCopyPasteValidator(JTable table, CopyPasteOperationValidatorModel validatorModel)
	{
		super(validatorModel);
		this.table = table;
	}

	/**
	 * Perform the actual validations required for copy-paste operation of multiple specimen.
	 * This method checks whether the copied data can actually be put in the selected location for pasting it.
	 * It returns error message in case it is not possible to paste the data
	 *   
	 */
	protected String doValidate()
	{
		if (validatorModel.getOperation().equals(AppletConstants.PASTE_OPERATION))
		{
			HashMap copiedData = validatorModel.getCopiedData();
			List selectedCopiedRows = validatorModel.getSelectedCopiedRows();
			List selectedCopiedCols = validatorModel.getSelectedCopiedCols();
			List selectedPastedRows = validatorModel.getSelectedPastedRows();
			List selectedPastedCols = validatorModel.getSelectedPastedCols();
			MultipleSpecimenTableModel tableModel = CommonAppletUtil.getMultipleSpecimenTableModel(table);
			Map mapDataFromModel = tableModel.getMap();
			/**
			 *  Calculate all rows to be pasted in case user has selected a single row to be pasted
			 */
			if (selectedPastedRows.size() == 1)
			{
				int rowValue = ((Integer) selectedPastedRows.get(0)).intValue();
				for (int i = 1; i < selectedCopiedRows.size(); i++)
				{
					selectedPastedRows.add(new Integer(rowValue + i));
				}
			}
			/**
			 *  Calculate all columns to be pasted in case user has selected a single column to be pasted
			 */
			if (selectedPastedCols.size() == 1)
			{
				int rowValue = ((Integer) selectedPastedCols.get(0)).intValue();
				for (int i = 1; i < selectedCopiedCols.size(); i++)
				{
					selectedPastedCols.add(new Integer(rowValue + i));
				}
			}

			MultipleSpecimenTableModel multipleSpecimenTableModel = CommonAppletUtil.getMultipleSpecimenTableModel(table);

			for (int j = 0; j < selectedPastedCols.size(); j++)
			{
				boolean isClassCopied = false;
				int columnToBePasted = ((Integer) selectedPastedCols.get(j)).intValue();
				int copiedColumn = ((Integer) selectedCopiedCols.get(j)).intValue();
			/*	if (selectedCopiedCols.size() > 1)
				{
					copiedColumn = ((Integer) selectedCopiedCols.get(j)).intValue();
				}*/

				for (int i = 0; i < selectedPastedRows.size(); i++)
				{
					int rowToBePasted = ((Integer) selectedPastedRows.get(i)).intValue();
					int copiedRow = ((Integer) selectedCopiedRows.get(i)).intValue();

					/**
					 *  This condition is to check whether an attempt is made to copy from button to text or text to button
					 */
					if (copiedRow >= AppletConstants.SPECIMEN_COMMENTS_ROW_NO && rowToBePasted < AppletConstants.SPECIMEN_COMMENTS_ROW_NO
							|| copiedRow < AppletConstants.SPECIMEN_COMMENTS_ROW_NO && rowToBePasted >= AppletConstants.SPECIMEN_COMMENTS_ROW_NO)
					{
						return "You can not copy from button to text or text to button";
					}

					/**
					 *  Check whether button from which Object is copied and button to which the object is pasted match. Return error if they do not match
					 */
					if (selectedCopiedRows.indexOf(new Integer(AppletConstants.SPECIMEN_COMMENTS_ROW_NO)) != selectedPastedRows.indexOf(new Integer(
							AppletConstants.SPECIMEN_COMMENTS_ROW_NO))
							|| selectedCopiedRows.indexOf(new Integer(AppletConstants.SPECIMEN_EXTERNAL_IDENTIFIERS_ROW_NO)) != selectedPastedRows
									.indexOf(new Integer(AppletConstants.SPECIMEN_EXTERNAL_IDENTIFIERS_ROW_NO))
							|| selectedCopiedRows.indexOf(new Integer(AppletConstants.SPECIMEN_BIOHAZARDS_ROW_NO)) != selectedPastedRows
									.indexOf(new Integer(AppletConstants.SPECIMEN_BIOHAZARDS_ROW_NO))
							|| selectedCopiedRows.indexOf(new Integer(AppletConstants.SPECIMEN_EVENTS_ROW_NO)) != selectedPastedRows
									.indexOf(new Integer(AppletConstants.SPECIMEN_EVENTS_ROW_NO))
							|| selectedCopiedRows.indexOf(new Integer(AppletConstants.SPECIMEN_DERIVE_ROW_NO)) != selectedPastedRows
									.indexOf(new Integer(AppletConstants.SPECIMEN_DERIVE_ROW_NO)))
					{
						return "The Object should be of same type when you do copy-paste on button";
					}

					/**
					 *  This condtion is to check whether row at which data is to be pasted is text
					 */
					if (rowToBePasted == AppletConstants.SPECIMEN_COLLECTION_GROUP_ROW_NO || rowToBePasted == AppletConstants.SPECIMEN_PARENT_ROW_NO
							|| rowToBePasted == AppletConstants.SPECIMEN_LABEL_ROW_NO || rowToBePasted == AppletConstants.SPECIMEN_BARCODE_ROW_NO
							|| rowToBePasted == AppletConstants.SPECIMEN_QUANTITY_ROW_NO
							|| rowToBePasted == AppletConstants.SPECIMEN_CONCENTRATION_ROW_NO)
					{
						continue;
					}

					if (rowToBePasted < AppletConstants.SPECIMEN_COMMENTS_ROW_NO)
					{
						if (rowToBePasted == AppletConstants.SPECIMEN_CLASS_ROW_NO)
						{
							isClassCopied = true;
						}
						String key = CommonAppletUtil.getDataKey(copiedRow, copiedColumn);
						String value = (String) (((List) copiedData.get(key)).get(0));
						Object values[] = null;

						if (rowToBePasted == AppletConstants.SPECIMEN_TYPE_ROW_NO)
						{
							/**
							 *  The value of type array is dependent on class value. So check whether class is also copied
							 */
							String classValue = null;
							if (isClassCopied)
							{
								key = CommonAppletUtil.getDataKey(AppletConstants.SPECIMEN_CLASS_ROW_NO, copiedColumn);
								classValue = (String) (((List) copiedData.get(key)).get(0));
							}
							else
							{
								key = tableModel.getKey(AppletConstants.SPECIMEN_CLASS_ROW_NO, columnToBePasted);
								classValue = (String) mapDataFromModel.get(key);
							}
							values = multipleSpecimenTableModel.getSpecimenTypeValues(classValue); // TODO
						}
						else
						{
							values = getObjectArray(multipleSpecimenTableModel, rowToBePasted);
						}

						/**
						 *  Following code checks whether value copied at source exist at destination in case destination is dropdown
						 */
						boolean flag = false;
						if (value.equals(""))
						{
							flag = true;
						}
						else
						{
							for (int k = 0; k < values.length; k++)
							{
								if (values[k].toString().equalsIgnoreCase(value))
								{
									flag = true;
								}
							}
						}

						/**
						 *  if value copied at source does not exist at destination in case destination is dropdown, return appropriate message
						 */
						if (!flag)
						{
						//	return "No match found for " + value + " in row " + (rowToBePasted + 1) + " of column " + (columnToBePasted + 1);
						}
					}
				}
			}
		}
		return "";
	}

	/**
	 * This method returns array of class, tissue type, tissue side, pathological status depending on values of attribute
	 * @param multipleSpecimenTableModel
	 * @param attribute
	 * @return - object array
	 */
	private Object[] getObjectArray(MultipleSpecimenTableModel multipleSpecimenTableModel, int attribute)
	{
		if (attribute == AppletConstants.SPECIMEN_CLASS_ROW_NO)
		{
			return multipleSpecimenTableModel.getSpecimenClassValues();
		}
		if (attribute == AppletConstants.SPECIMEN_TISSUE_SITE_ROW_NO)
		{
			return multipleSpecimenTableModel.getTissueSiteValues();
		}
		if (attribute == AppletConstants.SPECIMEN_TISSUE_SIDE_ROW_NO)
		{
			return multipleSpecimenTableModel.getTissueSideValues();
		}
		if (attribute == AppletConstants.SPECIMEN_PATHOLOGICAL_STATUS_ROW_NO)
		{
			return multipleSpecimenTableModel.getPathologicalStatusValues();
		}
		return null;
	}

	/**
	 * This method should be called for validating copy operation
	 * @return error message if any in PreValidate
	 */
	public String validateForCopy()
	{
		return preValidate();
	}

	/**
	 * This method should be called for validating paste operation
	 * @return error message if any in PreValidate
	 */
	public String validateForPaste()
	{
		String message = "";
		message = preValidate();
		if (message.equals(""))
		{
			message = doValidate();
		}
		return message;
	}

}