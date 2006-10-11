
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
	// TODO buttons
	protected String doValidate()
	{

		HashMap copiedData = validatorModel.getCopiedData();
		List selectedCopiedRows = validatorModel.getSelectedCopiedRows();
		List selectedCopiedCols = validatorModel.getSelectedCopiedCols();
		List selectedPastedRows = validatorModel.getSelectedPastedRows();
		List selectedPastedCols = validatorModel.getSelectedPastedCols();
		// TODO
		MultipleSpecimenTableModel multipleSpecimenTableModel = new MultipleSpecimenTableModel(0, new HashMap());

		for (int i = 0; i < selectedPastedRows.size(); i++)
		{
			for (int j = 0; j < selectedPastedCols.size(); j++)
			{
				if (i == AppletConstants.SPECIMEN_COLLECTION_GROUP_ROW_NO || i == AppletConstants.SPECIMEN_PARENT_ROW_NO
						|| i == AppletConstants.SPECIMEN_LABEL_ROW_NO || i == AppletConstants.SPECIMEN_BARCODE_ROW_NO
						|| i == AppletConstants.SPECIMEN_QUANTITY_ROW_NO || i == AppletConstants.SPECIMEN_CONCENTRATION_ROW_NO
						|| i == AppletConstants.SPECIMEN_STORAGE_LOCATION_ROW_NO)
				{
					continue;
				}

				String key = selectedCopiedRows.get(i) + "@" + selectedCopiedCols.get(j); // TODO @
				String value = (String) copiedData.get(key);
				Object values[] = null;
				if (i == AppletConstants.SPECIMEN_TYPE_ROW_NO)
				{
					values = multipleSpecimenTableModel.getSpecimenTypeValues(""); // TODO
				}
				else
				{
					values = getObjectArray(multipleSpecimenTableModel, i);
				}

				boolean flag = false;
				for (int k = 0; k < values.length; k++)
				{
					if (values[k].toString().equalsIgnoreCase(value))
					{
						flag = true;
					}
				}

				if (flag == false)
				{
					return "No match found for " + value + "in row " + (i+1) + "of column " + (j+1);
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