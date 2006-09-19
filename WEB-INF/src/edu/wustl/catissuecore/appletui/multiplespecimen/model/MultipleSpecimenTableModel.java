
package edu.wustl.catissuecore.appletui.multiplespecimen.model;

import java.util.HashMap;
import java.util.Map;

import edu.wustl.catissuecore.appletui.constant.AppletConstants;
import edu.wustl.catissuecore.appletui.model.BaseTabelModel;

/**
 * This is table model for multiple specimen functionality.
 * 
 * @author  Rahul Ner
 * @version 1.1
 *
 */
public class MultipleSpecimenTableModel extends BaseTabelModel 
{

	/**
	 * Default Serial Version ID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * attributes of the specimen for which user can specify the values. 
	 */
	static String[] specimenAttribute = {"specimenCollectionGroup", "parentSpecimen", "label",
			"barcode", "class", "type", "specimenCharacteristics:1_tissueSite",
			"specimenCharacteristics:1_tissueSide", "pathologicalStatus", "quantity",
			"concentrationInMicrogramPerMicroliter", "storageContainer", "comments",
			"specimenEventCollection", "externalIdentifierCollection", "biohazardCollection",
			"derive"};

	/**
	 * Row headers for the attributes. This corrosponds to display value for each of the  specimenAttribute in that order.
	 */
	static String[] rowHeaders = {"Specimen Group Name", "Parent", "Label", "Barcode", "Class",
			"Type", "Tissue Site", "Tissue Side", "Pathological Status", "Quantity",
			"Concentration", "Storage Position", "Comments", "Events", "External Identifier(s)",
			"Biohazards", "Derive"};

	/**
	 * Data structure maintianed by the model. Its key format is as follows:
	 * 
	 * key = Specimen:[ColumnNo]_[SpecimenAttribute]
	 * e.g for specimen in column 3 if user enter "my specimen" value for specimen label
	 * then this map will contain value as "my specimen" for the key "Specimen:3_label"
	 * 
	 */
	Map specimenMap;

	int columnCount;

	/**
	 * set default map. 
	 */
	public MultipleSpecimenTableModel(int initialColumnCount)
	{
		specimenMap = new HashMap();
		this.columnCount = initialColumnCount;

		for (int i = 0; i < rowHeaders.length; i++)
		{
			setValueAt(rowHeaders[i], i, 0);
		}

	}

	/**
	 * @see javax.swing.table.DefaultTableModel#getValueAt(int, int)
	 */
	public Object getValueAt(int row, int column)
	{
		if (column == 0)
		{
			return rowHeaders[row];
		}

		String specimenKey = "Specimen:" + String.valueOf(column) + "_" + specimenAttribute[row];

		return specimenMap.get(specimenKey);
	}

	/**
	 * @see javax.swing.table.DefaultTableModel#setValueAt(java.lang.Object, int, int)
	 */
	public void setValueAt(Object value, int row, int column)
	{
		String specimenKey = "Specimen:" + String.valueOf(column) + "_" + specimenAttribute[row];
		specimenMap.put(specimenKey, value);
	}

	/**
	 * @see javax.swing.table.DefaultTableModel#getColumnCount()
	 */
	public int getColumnCount()
	{
		return columnCount;
	}

	/** 
	 * @see javax.swing.table.DefaultTableModel#getRowCount()
	 */
	public int getRowCount()
	{
		return specimenAttribute.length;
	}

	public Map getMap()
	{
		return specimenMap;
	}

	/**
	 * @see javax.swing.table.DefaultTableModel#getColumnName(int)
	 */
	public String getColumnName(int columnNo)
	{
		if (columnNo == 0)
		{
			return "Field";
		}
		else
		{
			return "Specimen " + columnNo;
		}
	}

	/**
	 * @see javax.swing.table.AbstractTableModel#getColumnClass(int)
	 */
	public Class getColumnClass(int colNo)
	{

		if (colNo == 0)
		{
			return String.class;
		}
		else
		{
			return SpecimenColumnModel.class;
		}
	}

	/**
	 * This method return row headers.
	 * @return 
	 */
	public Object[] getRowHeaders()
	{
		return rowHeaders;
	}
}
