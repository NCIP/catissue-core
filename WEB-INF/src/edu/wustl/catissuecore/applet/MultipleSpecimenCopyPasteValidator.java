
package edu.wustl.catissuecore.applet;

import java.util.HashMap;
import java.util.List;

import edu.wustl.catissuecore.applet.model.MultipleSpecimenTableModel;

/**
 * <p>This class performs the actual validations required for copy-paste operation of multiple specimen.</p>
 * @author Santosh Chandak
 * @version 1.1
 */
public abstract class MultipleSpecimenCopyPasteValidator extends BaseCopyPasteValidator
{

	/**
	 * Perform the actual validations required for copy-paste operation of multiple specimen.
	 * This method checks whether the copied data can actually be put in the selected location for pasting it.
	 * It returns error message in case it is not possible to paste the data
	 *   
	 */
	protected String doValidate()
	{

		HashMap copiedData = validatorModel.getCopiedData();
		List selectedCopiedRows = validatorModel.getSelectedCopiedRows();
		List selectedCopiedCols = validatorModel.getSelectedCopiedCols();
		List selectedPastedRows = validatorModel.getSelectedPastedRows();
		List selectedPastedCols = validatorModel.getSelectedPastedCols();

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

		// TODO
		MultipleSpecimenTableModel multipleSpecimenTableModel = new MultipleSpecimenTableModel(0, new HashMap());

		for (int j = 0; j < selectedPastedCols.size(); j++)
		{
			boolean isClassCopied = false;
			for (int i = 0; i < selectedPastedRows.size(); i++)
			{
				int rowToBePasted = ((Integer) selectedPastedRows.get(i)).intValue();
				int columnToBePasted = ((Integer) selectedPastedCols.get(j)).intValue();

				/**
				 *  Check whether button from which Object is copied and button to which the object is pasted match. Return error if they do not match
				 */
				if (selectedCopiedRows.indexOf(new Integer(AppletConstants.MULTIPLE_SPECIMEN_COMMENTS)) != selectedPastedRows.indexOf(new Integer(
						AppletConstants.MULTIPLE_SPECIMEN_COMMENTS))
						|| selectedCopiedRows.indexOf(new Integer(AppletConstants.MULTIPLE_SPECIMEN_EXTERNAL_IDENTIFIERS)) != selectedPastedRows
								.indexOf(new Integer(AppletConstants.MULTIPLE_SPECIMEN_EXTERNAL_IDENTIFIERS))
						|| selectedCopiedRows.indexOf(new Integer(AppletConstants.MULTIPLE_SPECIMEN_BIOHAZARDS)) != selectedPastedRows
								.indexOf(new Integer(AppletConstants.MULTIPLE_SPECIMEN_BIOHAZARDS))
						|| selectedCopiedRows.indexOf(new Integer(AppletConstants.MULTIPLE_SPECIMEN_EVENTS)) != selectedPastedRows
								.indexOf(new Integer(AppletConstants.MULTIPLE_SPECIMEN_EVENTS))
						|| selectedCopiedRows.indexOf(new Integer(AppletConstants.MULTIPLE_SPECIMEN_DERIVE)) != selectedPastedRows
								.indexOf(new Integer(AppletConstants.MULTIPLE_SPECIMEN_DERIVE)))
				{
					return "Button from which Object is copied and button to which the object is pasted do not match";
				}

				/**
				 *  This condtion is to check whether row at which data is to be pasted is text
				 */
				if (rowToBePasted == AppletConstants.SPECIMEN_COLLECTION_GROUP_ROW_NO || rowToBePasted == AppletConstants.SPECIMEN_PARENT_ROW_NO
						|| rowToBePasted == AppletConstants.SPECIMEN_LABEL_ROW_NO || rowToBePasted == AppletConstants.SPECIMEN_BARCODE_ROW_NO
						|| rowToBePasted == AppletConstants.SPECIMEN_QUANTITY_ROW_NO
						|| rowToBePasted == AppletConstants.SPECIMEN_CONCENTRATION_ROW_NO
						|| rowToBePasted == AppletConstants.SPECIMEN_STORAGE_LOCATION_ROW_NO)
				{
					continue;
				}

				int copiedRow = ((Integer) selectedCopiedRows.get(i)).intValue();
				int copiedColumn = ((Integer) selectedCopiedCols.get(i)).intValue();
				String key = copiedRow + "@" + copiedColumn; // TODO @

				/**
				 *  This condition is to check whether an attempt is made to copy from button to text or text to button
				 */
				if (copiedRow >= AppletConstants.SPECIMEN_COMMENTS_ROW_NO && rowToBePasted < AppletConstants.SPECIMEN_COMMENTS_ROW_NO
						|| copiedRow < AppletConstants.SPECIMEN_COMMENTS_ROW_NO && rowToBePasted >= AppletConstants.SPECIMEN_COMMENTS_ROW_NO)
				{
					return "You can not copy from button to text or text to button";
				}

				if (copiedRow == AppletConstants.SPECIMEN_CLASS_ROW_NO)
				{
					isClassCopied = true;
				}
				String value = (String) copiedData.get(key);
				Object values[] = null;

				if (rowToBePasted == AppletConstants.SPECIMEN_TYPE_ROW_NO)
				{
					/**
					 *  The value of type array is dependent on class value. So check whether class is also copied
					 */
					String classValue = null;
					if (isClassCopied)
					{
						classValue = (String) multipleSpecimenTableModel.getValueAt(AppletConstants.SPECIMEN_CLASS_ROW_NO, copiedColumn);
					}
					else
					{
						classValue = (String) multipleSpecimenTableModel.getValueAt(AppletConstants.SPECIMEN_CLASS_ROW_NO, columnToBePasted);
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
				for (int k = 0; k < values.length; k++)
				{
					if (values[k].toString().equalsIgnoreCase(value))
					{
						flag = true;
					}
				}

				/**
				 *  if value copied at source does not exist at destination in case destination is dropdown, return appropriate message
				 */
				if (!flag)
				{
					return "No match found for " + value + "in row " + (rowToBePasted + 1) + "of column " + (columnToBePasted + 1);
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

}