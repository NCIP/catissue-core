/*
 * <p>Title: SpecimenArrayTableModel.java</p>
 * <p>Description:	This class initializes the fields of SpecimenArrayTableModel.java</p>
 * Copyright: Copyright (c) year 2006
 * Company: Washington University, School of Medicine, St. Louis.
 * @version 1.1
 * Created on Sep 14, 2006
 */

package edu.wustl.catissuecore.applet.model;

import java.util.Map;

import edu.wustl.catissuecore.applet.AppletConstants;
import edu.wustl.catissuecore.applet.util.SpecimenArrayAppletUtil;

/**
 * <p>
 * This model is used as table model for specimen array page table component.It is extending the base table
 * model.</p>
 * @author Ashwin Gupta
 * @version 1.1
 * @see edu.wustl.catissuecore.appletui.model.BaseTabelModel
 */
public class SpecimenArrayTableModel extends BaseTabelModel
{

	/**
	 * Default Serial Version ID.
	 */
	private static final long serialVersionUID = -6435519649631034159L;

	/**
	 *  specimenArrayModelMap - map is used to pass model.
	 */
	private Map specimenArrayModelMap;

	/**
	 * List of specimen array grid content POJOs.
	 */
	//	private List specimenArrayGridContentList;
	/**
	 * row count.
	 */
	private int rowCount;

	/**
	 * column count.
	 */
	private int columnCount;

	/**
	 * column count.
	 */
	private String copySelectedOption;

	/**
	 * How specimens are entered either by Label/Barcode.
	 */
	private String enterSpecimenBy;

	/**
	 * Specify the specimenClass field.
	 */
	private String specimenClass;

	/**
	 * Constructor to initialize array table model.
	 * @param specimenArrayModelMap map of array model
	 * @param rowCount row count
	 * @param columnCount column count
	 */
	private SpecimenArrayTableModel(Map specimenArrayModelMap, int rowCount, int columnCount)
	{
		super();
		this.specimenArrayModelMap = specimenArrayModelMap;
		//		specimenArrayGridContentList = (List) specimenArrayModelMap.get
		//(AppletConstants.ARRAY_GRID_COMPONENT_KEY);
		this.rowCount = rowCount;
		this.columnCount = columnCount;
	}

	/**
	 * Constructor to initialize array table model.
	 * @param specimenArrayModelMap map of array model
	 * @param rowCount row count
	 * @param specimenClass : specimenClass
	 * @param columnCount column count
	 * @param enterSpecimenBy how specimens are entered either by Label/Barcode.
	 */
	public SpecimenArrayTableModel(Map specimenArrayModelMap, int rowCount, int columnCount,
			String enterSpecimenBy, String specimenClass)
	{
		this(specimenArrayModelMap, rowCount, columnCount);
		this.enterSpecimenBy = enterSpecimenBy;
		this.specimenClass = specimenClass;
	}

	/**
	 * @return specimenArrayModelMap.
	 */
	public Map getSpecimenArrayModelMap()
	{
		return specimenArrayModelMap;
	}

	/**
	 * set specimenArrayModelMap.
	 * @param specimenArrayModelMap : specimenArrayModelMap
	 */
	public void setSpecimenArrayModelMap(Map specimenArrayModelMap)
	{
		this.specimenArrayModelMap = specimenArrayModelMap;
	}

	/**
	 * @see javax.swing.table.TableModel#getColumnCount()
	 * @return int
	 */
	public int getColumnCount()
	{
		return columnCount;
	}

	/**
	 * @see javax.swing.table.TableModel#getRowCount()
	 * @return int
	 */
	public int getRowCount()
	{
		return rowCount;
	}

	/**
	 * @see javax.swing.table.TableModel#getValueAt(int, int)
	 * @param rowIndex : rowindex
	 * @param columnIndex : columnIndex
	 * @return Object
	 */
	public Object getValueAt(int rowIndex, int columnIndex)
	{
		return specimenArrayModelMap.get(SpecimenArrayAppletUtil.getArrayMapKey(rowIndex,
				columnIndex, columnCount, getAttributeIndex()));
	}

	/**
	 * @see javax.swing.table.TableModel#setValueAt(java.lang.Object, int, int)
	 * @param aValue : aValue
	 * @param rowIndex : rowindex
	 * @param columnIndex : columnIndex
	 */
	public void setValueAt(Object aValue, int rowIndex, int columnIndex)
	{
		specimenArrayModelMap.put(SpecimenArrayAppletUtil.getArrayMapKey(rowIndex, columnIndex,
				columnCount, getAttributeIndex()), aValue.toString());
		// if one dimension & two dimension position is not set
		String posOneDimkey = SpecimenArrayAppletUtil.getArrayMapKey(rowIndex, columnIndex,
				columnCount, AppletConstants.ARRAY_CONTENT_ATTR_POS_DIM_ONE_INDEX);
		String posTwoDimkey = SpecimenArrayAppletUtil.getArrayMapKey(rowIndex, columnIndex,
				columnCount, AppletConstants.ARRAY_CONTENT_ATTR_POS_DIM_TWO_INDEX);

		if ((specimenArrayModelMap.get(posOneDimkey) == null)
				|| (specimenArrayModelMap.get(posOneDimkey).toString().equals("")))
		{
			specimenArrayModelMap.put(posOneDimkey, String.valueOf(rowIndex));
			specimenArrayModelMap.put(posTwoDimkey, String.valueOf(columnIndex));
		}
	}

	/**
	 * @see javax.swing.table.TableModel#isCellEditable(int, int)
	 * @param rowIndex : rowIndex
	 * @param columnIndex : columnIndex
	 * @return boolean
	 */
	public boolean isCellEditable(int rowIndex, int columnIndex)
	{
		return true;
	}

	/**
	 * @see javax.swing.table.TableModel#getColumnName(int)
	 * @param column : column
	 * @return String
	 */
	public String getColumnName(int column)
	{
		return String.valueOf(column + 1);
	}

	/**
	 * @return attribute index of array content attributes
	 */
	private int getAttributeIndex()
	{
		int attrIndex = 0;
		if (enterSpecimenBy.equals("Label"))
		{
			attrIndex = AppletConstants.ARRAY_CONTENT_ATTR_LABEL_INDEX;
		}
		else
		{
			attrIndex = AppletConstants.ARRAY_CONTENT_ATTR_BARCODE_INDEX;
		}
		return attrIndex;
	}

	/**
	 * create specimen array grid content.
	 * @return array grid content
	private SpecimenArrayGridContent createSpecimenArrayGridContent() {
		SpecimenArrayGridContent arrayGridContent = new SpecimenArrayGridContent();
		return arrayGridContent;
	}
	*/

	/**
	 * @return Returns the specimenClass.
	 */
	public String getSpecimenClass()
	{
		return specimenClass;
	}

	/**
	 * @return Returns the copySelectedOption.
	 */
	public String getCopySelectedOption()
	{
		return copySelectedOption;
	}

	/**
	 * @param copySelectedOption The copySelectedOption to set.
	 */
	public void setCopySelectedOption(String copySelectedOption)
	{
		this.copySelectedOption = copySelectedOption;
	}

	/**
	 * @return Returns the enterSpecimenBy.
	 */
	public String getEnterSpecimenBy()
	{
		return enterSpecimenBy;
	}

	/**
	 * @param enterSpecimenBy The enterSpecimenBy to set.
	 */
	public void setEnterSpecimenBy(String enterSpecimenBy)
	{
		this.enterSpecimenBy = enterSpecimenBy;
	}

	/**
	 * change enter specimen by.
	 * @param enterSpecimenBy enter specimen by
	 */
	public void changeEnterSpecimenBy(String enterSpecimenBy)
	{
		int attrIndex = 0;
		if (enterSpecimenBy.equals("Label"))
		{
			attrIndex = AppletConstants.ARRAY_CONTENT_ATTR_LABEL_INDEX;
		}
		else
		{
			attrIndex = AppletConstants.ARRAY_CONTENT_ATTR_BARCODE_INDEX;
		}
		String value = null;
		Object valueObj = null;
		for (int i = 0; i < rowCount; i++)
		{
			for (int j = 0; j < columnCount; j++)
			{
				valueObj = getValueAt(i, j);
				if (valueObj == null)
				{
					value = String.valueOf("");
				}
				else
				{
					value = valueObj.toString();
				}
				specimenArrayModelMap.put(SpecimenArrayAppletUtil.getArrayMapKey(i, j, columnCount,
						attrIndex), value);
				// change old selction to ""
				specimenArrayModelMap.put(SpecimenArrayAppletUtil.getArrayMapKey(i, j, columnCount,
						getAttributeIndex()), "");
			}
		}
		this.enterSpecimenBy = enterSpecimenBy;
	}
}
