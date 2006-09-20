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

import edu.wustl.catissuecore.applet.util.SpecimenArrayAppletUtil;

/**
 * <p>
 * This model is used as table model for specimen array page table component.It is extending the base table
 * model.</p>
 * @author Ashwin Gupta
 * @version 1.1
 * @see edu.wustl.catissuecore.appletui.model.BaseTabelModel
 */
public class SpecimenArrayTableModel extends BaseTabelModel {

	/**
	 * Default Serial Version ID
	 */
	private static final long serialVersionUID = -6435519649631034159L;
	
	/**
	 *  specimenArrayModelMap - map is used to pass model 
	 */
	private Map specimenArrayModelMap;
	
	/**
	 * List of specimen array grid content POJOs 
	 */
//	private List specimenArrayGridContentList;
	
	/**
	 * row count 
	 */
	private int rowCount;
	
	/**
	 * column count 
	 */
	private int columnCount;
	
	/**
	 * How specimens are entered either by Label/Barcode.
	 */
	private String enterSpecimenBy;
	
	/**
	 * Constructor to initialize array table model.
	 * @param specimenArrayModelMap map of array model
	 * @param rowCount row count
	 * @param columnCount column count
	 */
	private SpecimenArrayTableModel(Map specimenArrayModelMap,int rowCount,int columnCount) {
		super();
		this.specimenArrayModelMap = specimenArrayModelMap;
//		specimenArrayGridContentList = (List) specimenArrayModelMap.get(AppletConstants.ARRAY_GRID_COMPONENT_KEY);
		this.rowCount = rowCount;
		this.columnCount = columnCount;
	}

	/**
	 * Constructor to initialize array table model.
	 * @param specimenArrayModelMap map of array model
	 * @param rowCount row count
	 * @param columnCount column count
	 * @param enterSpecimenBy how specimens are entered either by Label/Barcode. 
	 */
	public SpecimenArrayTableModel(Map specimenArrayModelMap,int rowCount,int columnCount,String enterSpecimenBy) {
		this(specimenArrayModelMap,rowCount,columnCount);
		this.enterSpecimenBy = enterSpecimenBy;
	}
	
	/**
	 * @return specimenArrayModelMap
	 */
	public Map getSpecimenArrayModelMap() {
		return specimenArrayModelMap;
	}

	/**
	 * set specimenArrayModelMap
	 * @param specimenArrayModelMap
	 */
	public void setSpecimenArrayModelMap(Map specimenArrayModelMap) {
		this.specimenArrayModelMap = specimenArrayModelMap;
	}
	
	/**
	 * @see javax.swing.table.TableModel#getColumnCount()
	 */
	public int getColumnCount() {
		return columnCount; 
	}

	/**
	 * @see javax.swing.table.TableModel#getRowCount()
	 */
	public int getRowCount() {
		return rowCount;
	}

	/**
	 * @see javax.swing.table.TableModel#getValueAt(int, int)
	 */
	public Object getValueAt(int rowIndex, int columnIndex) {
		return specimenArrayModelMap.get(SpecimenArrayAppletUtil.getArrayMapKey(rowIndex,columnIndex,columnCount,getAttributeIndex()));
		
/*		if (specimenArrayModelMap != null) {
			SpecimenArrayGridContent arrayGridContent = ((SpecimenArrayGridContent) specimenArrayModelMap.get(rowIndex + AppletConstants.delimiter + columnIndex));
			System.out.println("get value at method Not null");
			System.out.println(arrayGridContent.getSpecimenLabel());
			if (enterSpecimenBy.equals("Label")) {
				return arrayGridContent.getSpecimenLabel();
			} else {
				return arrayGridContent.getSpecimenBarcode();
			}
		}
*/	/*	
		System.out.println( "  rowIndex :: " + rowIndex +  "  columnIndex :: "+ columnIndex +" Position " + position);
		if ( (specimenArrayGridContentList != null) && (!specimenArrayGridContentList.isEmpty()) && specimenArrayGridContentList.size() > position) {
			if (enterSpecimenBy.equals("Label")) {
				return ((SpecimenArrayGridContent) specimenArrayGridContentList.get(position)).getSpecimenLabel();
			} else {
				return ((SpecimenArrayGridContent) specimenArrayGridContentList.get(position)).getSpecimenBarcode();
			}
		}
	*/
	}

	/**
	 * @see javax.swing.table.TableModel#setValueAt(java.lang.Object, int, int)
	 */
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		specimenArrayModelMap.put(SpecimenArrayAppletUtil.getArrayMapKey(rowIndex,columnIndex,columnCount,getAttributeIndex()),aValue.toString());
		
/*		if (specimenArrayModelMap != null) {
			System.out.println("set value at Not null");
			SpecimenArrayGridContent arrayGridContent = ((SpecimenArrayGridContent) specimenArrayModelMap.get(rowIndex + AppletConstants.delimiter + columnIndex));
			System.out.println(arrayGridContent.getSpecimenLabel());
			
			if (enterSpecimenBy.equals("Label")) {
				arrayGridContent.setSpecimenLabel(aValue.toString());
			} else {
				arrayGridContent.setSpecimenBarcode(aValue.toString());
			}
		}
*/		//fireTableCellUpdated(rowIndex,columnIndex);
		/*
		if (specimenArrayGridContentList == null) {
			specimenArrayGridContentList = new ArrayList();
		}
		
		if ((!specimenArrayGridContentList.isEmpty()) && specimenArrayGridContentList.size() > position) {
			if (enterSpecimenBy.equals("Label")) {
				((SpecimenArrayGridContent) specimenArrayGridContentList.get(position)).setSpecimenLabel(aValue.toString());
			} else {
				((SpecimenArrayGridContent) specimenArrayGridContentList.get(position)).setSpecimenBarcode(aValue.toString());
			}
		} else {
		}
		*/
		//fireTableCellUpdated(rowIndex,columnIndex);
	}
	
	/**
	 * @see javax.swing.table.TableModel#isCellEditable(int, int)
	 */
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return true;
	}

	/**
	 * @see javax.swing.table.TableModel#getColumnName(int)
	 */
	public String getColumnName(int column) {
		return String.valueOf(column + 1);
	}

	/**
	 * @return attribute index of array content attributes
	 */
	private int getAttributeIndex() {
		int attrIndex = 0;
		if (enterSpecimenBy.equals("Label")) {
			attrIndex = 0;
		} else {
			attrIndex = 1;
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
}
