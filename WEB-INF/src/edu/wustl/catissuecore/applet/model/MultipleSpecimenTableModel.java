
package edu.wustl.catissuecore.applet.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.wustl.catissuecore.applet.AppletConstants;
import edu.wustl.catissuecore.util.global.Constants;

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

	private int lastCellColumn = 0;
	private int lastCellRow = 0;

	/**
	 * attributes of the specimen for which user can specify the values. 
	 */
	String[] specimenAttribute = {"","SpecimenCollectionGroup_name", "ParentSpecimen_label", "label", "barcode", "class", "type",
			"SpecimenCharacteristics_tissueSite", "SpecimenCharacteristics_tissueSide", "pathologicalStatus", "Quantity_value",
			"concentrationInMicrogramPerMicroliter", "comments", "specimenEventCollection", "externalIdentifierCollection",
			"biohazardCollection", "derive"};

	/**
	 * Row headers for the attributes. This corrosponds to display value for each of the  specimenAttribute in that order.
	 */
	private static final String[] rowHeaders = {"","* Specimen Group Name", "* Parent", "* Label", "Barcode", "* Class", "* Type", "* Tissue Site",
			"* Tissue Side", "* Pathological Status", "Quantity", "Concentration", "Comments", "* Events",
			"External Identifier(s)", "Biohazards", "Derive"};

	/**
	 * Data structure maintianed by the model. Its key format is as follows:
	 * 
	 * key = Specimen:[ColumnNo]_[SpecimenAttribute]
	 * e.g for specimen in column 3 if user enter "my specimen" value for specimen label
	 * then this map will contain value as "my specimen" for the key "Specimen:3_label"
	 * 
	 */
	Map specimenMap;
	
	/**
	 * Data structure maintianed by the model. Its key format is as follows:
	 * 
	 * key = [ColumnNo]_[SpecimenAttribute]
	 * value = 'ADD' or 'EDIT' depending on lable of button 
	 * 
	 */
	Map buttonStatusMap;
	
	int columnCount;

	/** This is a map that holds options to be displayed for various attributes of the specimen
	 *
	 * It contains 
	 * 
	 * 1. MAP - specimen class ->Array of values for specimen Type
	 * 1. Array of values for  Specimen class  
	 * 2. Array of values for Tissue site
	 * 3. Array of values for Tissue side
	 * 4. Array of values for Pathological
	 * */
	Map specimenAttributeOptions;
 
	/***/
	private int columnsPerPage = 5;

	/**/
	private int currentPageIndex = 1;
	private String specimenCollectionGroupName = null;
/*	private String specimenClass = null;
	private String specimenType = null;
	private String pathologicalStatus = null;
	private String tissueSite = null;*/
	
	/**
	 * set default map. 
	 * @param specimenAttributeOptions  initialzation map.
	 */
	public MultipleSpecimenTableModel(int initialColumnCount, Map specimenAttributeOptions)
	{

		specimenMap = new HashMap();
		buttonStatusMap = new HashMap();
		this.columnCount = initialColumnCount;
		this.specimenAttributeOptions = specimenAttributeOptions;
		for (int i = 1; i <= initialColumnCount; i++)
		{
			putIdInMap(i);
			//to set radio button keys
			setActualColumnCollectionGroupRadioButtonValue(i);
			//set default value for list boxes : TissueSite,TissueSide,Pathological status.
			setNotSpecified(i);
			//****For specimen checkbox
			setActualColumnSpecimenCheckBoxValue(i);
		}
		//Setting the specimen Collection group name if add multiple specimen came form add specimen collection group name
		if (specimenAttributeOptions.get(Constants.SPECIMEN_COLL_GP_NAME) != null)
		{
			setSpecimenCollectionGroupName(specimenAttributeOptions.get(Constants.SPECIMEN_COLL_GP_NAME).toString());
//			setSpecimenClass();
//			setSpecimenType();
//			setPathologicalStatus();
//			setTissueSite();
					
			for(int count=1;count<=initialColumnCount; count++)
			{
				setCollectionGroupInModel(count);
			/*	setDataInModel(count,AppletConstants.SPECIMEN_CLASS_ROW_NO,specimenClass);
				setDataInModel(count,AppletConstants.SPECIMEN_TYPE_ROW_NO,specimenType);
				setDataInModel(count,AppletConstants.SPECIMEN_PATHOLOGICAL_STATUS_ROW_NO,pathologicalStatus);
				setDataInModel(count,AppletConstants.SPECIMEN_TISSUE_SITE_ROW_NO,tissueSite);*/
			}	
		}
		
		//mandar: to set columns per page
		this.columnsPerPage = Integer.parseInt(specimenAttributeOptions.get(Constants.MULTIPLE_SPECIMEN_COLUMNS_PER_PAGE).toString()) ;
		System.out.println("Columns per page set to : "+this.columnsPerPage);
	}

	/**
	 * @param i
	 */
	private void putIdInMap(int colNo)
	{
		specimenMap.put(AppletConstants.SPECIMEN_PREFIX + (colNo) + "_" + "id", new Long(colNo));
	}

	/**
	 * @see javax.swing.table.DefaultTableModel#getValueAt(int, int)
	 */
	public Object getValueAt(int row, int column)
	{
		/*		if (column == 0)
		 {
		 return rowHeaders[row];
		 }
		 System.out.println("getValueAt " + row + " " + column + ": " + getKey(row,column) + "  " + specimenMap.get(getKey(row,column)));
		 */
		return specimenMap.get(getKey(row, column));
	}

	/**
	 * @see javax.swing.table.DefaultTableModel#setValueAt(java.lang.Object, int, int)
	 */
	public void setValueAt(Object value, int row, int column)
	{
		/*		if (column != 0)
		 {
		 specimenMap.put(getKey(row,column), value);
		 }
		 System.out.println("setValueAt " + row + " " + column + ": " + specimenMap.get(getKey(row,column)) + value);
		 */
		/* Code to check the calling method.
		 try
		 {
		 throw new Exception("User Defined");
		 }
		 catch(Exception e){ e.printStackTrace();}
		 */
		specimenMap.put(getKey(row, column), value);
		System.out.println("In setValueAt : row : " + row + " Column : " + column);
		System.out.println("setValueAt " + getKey(row, column) + ": " + specimenMap.get(getKey(row, column)) + " - " + value);
	}

	/**
	 * @see javax.swing.table.DefaultTableModel#getColumnCount()
	 */
	public int getColumnCount()
	{

		if (currentPageIndex > (columnCount / columnsPerPage))
		{
			return columnCount % columnsPerPage;
		}

		return columnsPerPage;
	}

	/**
	 * @return Returns the currentPageIndex.
	 */
	public int getCurrentPageIndex()
	{
		return currentPageIndex;
	}

	/**
	 * @param currentPageIndex The currentPageIndex to set.
	 */
	public void setCurrentPageIndex(int currentPageIndex)
	{
		this.currentPageIndex = currentPageIndex;
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
		return "Specimen " + (getActualColumnNo(columnNo) + 1);
	}

	/**
	 * @see javax.swing.table.AbstractTableModel#getColumnClass(int)
	 */
	public Class getColumnClass(int colNo)
	{
		return SpecimenColumnModel.class;
	}

	/**
	 * This method return row headers.
	 * @return 
	 */
	public static Object[] getRowHeaders()
	{
		return rowHeaders;
	}

	//Setting the Specimen Collection Group Name For Specimen
	public void setSpecimenCollectionGroupName(String specimenCollectionGroupName)
	{
		this.specimenCollectionGroupName = specimenAttributeOptions.get(Constants.SPECIMEN_COLL_GP_NAME).toString();
	}

	//Getting the Specimen Collection Group Name for Specimen
	public String getSpecimenCollectionGroupName()
	{
		return this.specimenCollectionGroupName;
	}

	/**
	 * This method initialize data lists 
	 */
	/*	private Map initDataLists()
	 {
	 BaseAppletModel appletModel = new BaseAppletModel();
	 appletModel.setData(new HashMap());
	 try
	 {
	 appletModel = (BaseAppletModel) AppletServerCommunicator.doAppletServerCommunication(
	 "http://localhost:8080/catissuecore/MultipleSpecimenAppletAction.do?method=initData", appletModel);

	 Map tempMap = appletModel.getData();
	 System.out.println(tempMap.get(Constants.SPECIMEN_TYPE_MAP));
	 System.out.println(tempMap.get(Constants.SPECIMEN_CLASS_LIST));
	 System.out.println(tempMap.get(Constants.TISSUE_SITE_LIST));
	 System.out.println(tempMap.get(Constants.TISSUE_SIDE_LIST));
	 System.out.println(tempMap.get(Constants.PATHOLOGICAL_STATUS_LIST));
	 

	 return appletModel.getData();
	 }
	 catch (Exception e)
	 {
	 e.printStackTrace();
	 System.out.println("Exception");
	 }

	 return null;
	 }

	 *//**
	 * returns specimen type list for given specimen class.
	 * 
	 * @param specimenClass
	 * @return
	 */
	public List getSpecimenTypeList(String specimenClass)
	{
		//		Map specimenTypeMap = (Map) specimenAttributeOptions.get(Constants.SPECIMEN_TYPE_MAP);
		//		return (List) specimenTypeMap.get(specimenClass);
		ArrayList aList = new ArrayList();
		for (int i = 1; i < 5; i++)
			aList.add(specimenClass + "_" + i);

		return aList;
	}

	public Object[] getSpecimenTypeValues(String specimenClass)
	{
		System.out.println("get type values called");
		/*String specimenClass = (String) getValueAt(AppletConstants.SPECIMEN_CLASS_ROW_NO, column);*/
		if (specimenClass == null)
		{
			specimenClass = Constants.SELECT_OPTION;
		}
		Map specimenTypeMap = (Map) specimenAttributeOptions.get(Constants.SPECIMEN_TYPE_MAP);
		return (Object[]) specimenTypeMap.get(specimenClass);
	}

	/**
	 * returns specimen class list
	 * @return
	 * 
	 */
	public Object[] getSpecimenClassValues()
	{
		return (Object[]) specimenAttributeOptions.get(Constants.SPECIMEN_CLASS_LIST);
	}

	/**
	 * @return tissue site list
	 */
	public Object[] getTissueSiteValues()
	{
		return (Object[]) specimenAttributeOptions.get(Constants.TISSUE_SITE_LIST);
	}

	/**
	 * @return tissue side list
	 */
	public Object[] getTissueSideValues()
	{
		return (Object[]) specimenAttributeOptions.get(Constants.TISSUE_SIDE_LIST);
	}

	/**
	 * @return PATHOLOGICAL STATUS LIST
	 */
	public Object[] getPathologicalStatusValues()
	{
		return (Object[]) specimenAttributeOptions.get(Constants.PATHOLOGICAL_STATUS_LIST);
	}

	/**
	 * returns quantity unit for given specimen 
	 * 
	 * @param colNo spcimen column no
	 * @return unit
	 */
	public String getQuantityUnit(int colNo)
	{

		String specimenClass = (String) getValueAt(AppletConstants.SPECIMEN_CLASS_ROW_NO, colNo);
		String specimenType = (String) getValueAt(AppletConstants.SPECIMEN_TYPE_ROW_NO, colNo);

		if (specimenClass == null)
		{
			return "";
		}

		String unit = "";

		if (specimenClass.equals("Fluid"))
		{
			unit = Constants.UNIT_ML;
		}
		else if (specimenClass.equals("Cell"))
		{
			unit = Constants.UNIT_CC;

		}
		else if (specimenClass.equals("Molecular"))
		{
			unit = Constants.UNIT_MG;
		}
		else if (specimenClass.equals("Tissue"))
		{
			if (specimenType == null)
			{
				unit = Constants.UNIT_GM;
			}
			else if (specimenType.equals(Constants.MICRODISSECTED))
			{
				unit = Constants.UNIT_CL;
			}
			else if (specimenType.equals(Constants.FROZEN_TISSUE_SLIDE) || specimenType.equals(Constants.FIXED_TISSUE_BLOCK)
					|| specimenType.equals(Constants.FROZEN_TISSUE_BLOCK) || specimenType.equals(Constants.NOT_SPECIFIED)
					|| specimenType.equals(Constants.FIXED_TISSUE_SLIDE))
			{
				unit = Constants.UNIT_CN;
			}
			else
			{
				unit = Constants.UNIT_GM;
			}
		}

		return unit;

	}

	public void specimenClassUpdated(int columnNo)
	{
		//---
		//this.fireTableStructureChanged() ;
		//---
		/*		fireTableCellUpdated(AppletConstants.SPECIMEN_BARCODE_ROW_NO,columnNo);
		 fireTableCellUpdated(AppletConstants.SPECIMEN_TYPE_ROW_NO,columnNo);
		 
		 
		 System.out.println("updating type " + AppletConstants.SPECIMEN_TYPE_ROW_NO +  " "+ columnNo);
		 */}

	public String getKey(int row, int column)
	{
		int actualColumnNo = getActualColumnNo(column);
		String specimenKey = AppletConstants.SPECIMEN_PREFIX + String.valueOf(actualColumnNo + 1) + "_" + specimenAttribute[row];

		return specimenKey;
	}

	/**
	 * Concentration is enabled only in case of Molecular class.
	 * so this method returns true if Molecular class is selected for the given column.
	 * 
	 * @param column
	 * @return
	 */
	public boolean getConcentrationStatus(int column)
	{
	//	return false;
				String specimenClass = (String) getValueAt(AppletConstants.SPECIMEN_CLASS_ROW_NO, column);
				
				if(specimenClass!=null && specimenClass.equalsIgnoreCase(Constants.MOLECULAR)) {
					return true;
				} 
				
				return false;
	}

	/**
	 * set storage details in map.
	 */
	public void setStorageDetails(String specimenMapKey, String storageId, String storageLabel, String xPos, String yPos)
	{
		int colNo = getActualColumnNo(Integer.parseInt(specimenMapKey));
		specimenMap.put(AppletConstants.SPECIMEN_PREFIX + (colNo + 1) + "_" + "StorageContainer_id", new Long(storageId));
		specimenMap.put(AppletConstants.SPECIMEN_PREFIX + (colNo + 1) + "_" + "positionDimensionOne", xPos);
		specimenMap.put(AppletConstants.SPECIMEN_PREFIX + (colNo + 1) + "_" + "positionDimensionTwo", yPos);

		String storageInfo = storageLabel + "," + xPos + "," + yPos;

		//mandar added new key to hold map data
		specimenMap.put(getMapTempKey(Integer.parseInt(specimenMapKey)), storageInfo);

		//setValueAt(storageInfo, AppletConstants.SPECIMEN_STORAGE_LOCATION_ROW_NO, colNo);
		
//		System.out.println("Setting StorageInfo at : " + getKey(AppletConstants.SPECIMEN_STORAGE_LOCATION_ROW_NO, Integer.parseInt(specimenMapKey)));
//		setValueAt(storageInfo, AppletConstants.SPECIMEN_STORAGE_LOCATION_ROW_NO, Integer.parseInt(specimenMapKey));
//		//	setValueAt(storageInfo,10,10);
//
//		System.out.println("setting " + storageInfo + "to " + AppletConstants.SPECIMEN_STORAGE_LOCATION_ROW_NO + "  " + colNo);
		//		System.out.println("Getting storageInfo from : "+  getValueAt(AppletConstants.SPECIMEN_STORAGE_LOCATION_ROW_NO , colNo));
		//		System.out.println("-------------------------------------------------------\n");
		//			showMapData();
		//		System.out.println("-------------------------------------------------------\n");
	}
	
	
	/**
	 * set button caption details in map.
	 */
	public void setCaptionInMap(String specimenMapKey,String buttonType)
	{
		int colNo = getActualColumnNo(Integer.parseInt(specimenMapKey));
		specimenMap.put(AppletConstants.SPECIMEN_PREFIX + (colNo + 1) + "_" +buttonType+"_BUTTON_STATUS", "EDIT");
	}
	
	

	/**
	 * 
	 * @param column Column for which the map key should be returned.
	 * @return Key for the storage location of the column.
	 */
	public String getMapTempKey(int column)
	{
		int colNo = getActualColumnNo(column);
		String key = AppletConstants.SPECIMEN_PREFIX + (colNo + 1) + "_" + AppletConstants.MULTIPLE_SPECIMEN_LOCATION_LABEL;
		return key;
	}

	/**
	 * 
	 * @param key Key for which the value is required.
	 * @return Value for the given key.
	 */
	public String getMapTempValue(String key)
	{
		String value = "";
		try
		{
			if (key != null)
			{
				if (specimenMap.containsKey(key))
					return specimenMap.get(key).toString();
				else
					return value;
			}
			else
				return value;
		}
		catch (Exception e)
		{
			System.out.println(e.getMessage());
			return value;
		}
	}

	public void addColumn()
	{
		columnCount++;
		putIdInMap(columnCount);
		setCollectionGroupInModel(columnCount);
		//to set collection group radio button value
		setActualColumnCollectionGroupRadioButtonValue(columnCount);
		setNotSpecified(columnCount);
		//**********For CheckBox
		setActualColumnSpecimenCheckBoxValue(columnCount);
		
	}

	/**
	 * Returns actual column no of the given column  depending on page index.
	 */
	public int getActualColumnNo(int selectedColumnNo)
	{
		//System.out.println(("col converteed to " + selectedColumnNo + "--->" + ((columnsPerPage * ( currentPageIndex - 1) ) + selectedColumnNo)));
		return ((columnsPerPage * (currentPageIndex - 1)) + selectedColumnNo);
	}

	/**
	 * This method returns  the display column no of a column given its actual no in the model.
	 * @param actualColumnNo
	 * @return
	 */
	public int getDisplayColumnNo(int actualColumnNo)
	{
		return (actualColumnNo % columnsPerPage);
	}

	/**
	 * 
	 * @return This method returns the total number of pages to be created based on the number 
	 * of columns per page and total columns.
	 */
	public int getTotalPageCount()
	{
		int totalPages = 0;
		if ((columnCount % columnsPerPage) != 0)
			totalPages = (columnCount / columnsPerPage) + 1;
		else
			totalPages = (columnCount / columnsPerPage);
		return totalPages;
	}

	/**
	 * @return Returns the columnsPerPage.
	 */
	public int getColumnsPerPage()
	{
		return columnsPerPage;
	}

	/**
	 * @param columnsPerPage The columnsPerPage to set.
	 */
	public void setColumnsPerPage(int columnsPerPage)
	{
		this.columnsPerPage = columnsPerPage;
	}

	/**
	 * @return SPECIMEN COLLECTION GROUP LIST
	 */
	public Object[] getSpecimenCollectionGroupValues()
	{
		return (Object[]) specimenAttributeOptions.get(Constants.SPECIMEN_COLLECTION_GROUP_LIST);
	}

	//to remove after testing
	public void showMapData()
	{
		System.out.println("---------------------------------");
		System.out.println("in Model showMapData");
		System.out.println(specimenMap);
		System.out.println("---------------------------------");
	}

	/*
	 * Used in SpecimenSubmitButtonHandler to set number of specimens.
	 */
	/**
	 * 
	 * @return Total number of columns in the model.
	 */
	public int getTotalColumnCount()
	{
		return columnCount;
	}

	/**
	 * @return Returns the lastCellColumn.
	 */
	public int getLastCellColumn()
	{
		return lastCellColumn;
	}

	/**
	 * @param lastCellColumn The lastCellColumn to set.
	 */
	public void setLastCellColumn(int currentCellPositionX)
	{
		this.lastCellColumn = currentCellPositionX;
	}

	/**
	 * @return Returns the lastCellRow.
	 */
	public int getLastCellRow()
	{
		return lastCellRow;
	}

	/**
	 * @param lastCellRow The lastCellRow to set.
	 */
	public void setLastCellRow(int currentCellPositionY)
	{
		this.lastCellRow = currentCellPositionY;
	}
	// -----------------------POC for copy paste end
	
	private void setCollectionGroupInModel(int column)
	{//todo
		if(specimenCollectionGroupName != null)
		{
			String specimenKey = AppletConstants.SPECIMEN_PREFIX + String.valueOf(column) + "_" + specimenAttribute[AppletConstants.SPECIMEN_COLLECTION_GROUP_ROW_NO];
			specimenMap.put(specimenKey, specimenCollectionGroupName );	
		}
	}
	
	private void removeCollectionGroupFromModel(int column)
	{
		if(specimenCollectionGroupName != null)
		{
			String specimenKey = AppletConstants.SPECIMEN_PREFIX + String.valueOf(column) + "_" + specimenAttribute[AppletConstants.SPECIMEN_COLLECTION_GROUP_ROW_NO];
			specimenMap.remove(specimenKey);	
		}
	}
	
/*	private void setDataInModel(int column, short rowNo, String value)
	{//todo
		if(specimenCollectionGroupName != null)
		{
			String specimenKey = AppletConstants.SPECIMEN_PREFIX + String.valueOf(column) + "_" + specimenAttribute[rowNo];
			specimenMap.put(specimenKey,value);	
		}
	}
	
	private void removeDataFromModel(int column, short rowNo)
	{
		if(specimenCollectionGroupName != null)
		{
			String specimenKey = AppletConstants.SPECIMEN_PREFIX + String.valueOf(column) + "_" + specimenAttribute[rowNo];
			specimenMap.remove(specimenKey);	
		}
	}*/

	//-------------- For ParentSpecimen - CollectionGroup identification start
	HashMap collectionGroupRadioButtonMap = new HashMap();
	
	/**
	 * This method sets the state of collectionGroup radio button in the map. 
	 * @param column Column number of radio button
	 * @param value Radio button state of that column.
	 */
	public void setCollectionGroupRadioButtonValueAt(int column,boolean value)
	{
		int actualColumn = getActualColumnNo(column )+1;
		String key = "Specimen:"+actualColumn+"_collectionGroupRadioSelected";
		collectionGroupRadioButtonMap.put(key, new Boolean(value));
	}
	/**
	 * 
	 * @param column Column number to fetch the collection group radio button state.
	 * @return Radio Button state of collection group for the specified column.
	 */
	public boolean getCollectionGroupRadioButtonValueAt(int column)
	{
		int actualColumn = getActualColumnNo(column )+1;
		String key = "Specimen:"+actualColumn+"_collectionGroupRadioSelected";
		return ((Boolean)collectionGroupRadioButtonMap.get(key)).booleanValue() ;
	}

	private void setActualColumnCollectionGroupRadioButtonValue(int column)
	{
		String key = "Specimen:"+column+"_collectionGroupRadioSelected";
		collectionGroupRadioButtonMap.put(key, new Boolean(true));
	}

	
	/**
	 * @return Returns the collectionGroupRadioButtonMap.
	 */
	public HashMap getCollectionGroupRadioButtonMap()
	{
		return collectionGroupRadioButtonMap;
	}
	/**
	 * @param collectionGroupRadioButtonMap The collectionGroupRadioButtonMap to set.
	 */
	public void setCollectionGroupRadioButtonMap(HashMap collectionGroupRadioButtonMap)
	{
		this.collectionGroupRadioButtonMap = collectionGroupRadioButtonMap;
	}
	
	//-------------- For ParentSpecimen - CollectionGroup identification end
	
	// -----------------For Delete Last Column
	
	public void removeLastColumn()
	{
		removeIdFromMap(columnCount);
		removeCollectionGroupFromModel(columnCount);
	/*	removeDataFromModel(columnCount,AppletConstants.SPECIMEN_CLASS_ROW_NO);
		removeDataFromModel(columnCount,AppletConstants.SPECIMEN_TYPE_ROW_NO);
		removeDataFromModel(columnCount,AppletConstants.SPECIMEN_PATHOLOGICAL_STATUS_ROW_NO);
		removeDataFromModel(columnCount,AppletConstants.SPECIMEN_TISSUE_SITE_ROW_NO);*/
		removeActualColumnCollectionGroupRadioButtonValue(columnCount);
		removeDataValues(columnCount);
		// ---------------------
		//******* CheckBox
		removeActualColumnSpecimenCheckBoxValue(columnCount);
		
		columnCount--;
	}

	private void removeIdFromMap(int colNo)
	{
		specimenMap.remove(AppletConstants.SPECIMEN_PREFIX + (colNo) + "_" + "id");
	}

	private void removeActualColumnCollectionGroupRadioButtonValue(int column)
	{
		String key = "Specimen:"+column+"_collectionGroupRadioSelected";
		collectionGroupRadioButtonMap.remove(key);
	}

	private void removeDataValues(int column)
	{
		for(int row = 0; row<specimenAttribute.length; row++  )
		{
			String specimenKey = AppletConstants.SPECIMEN_PREFIX + String.valueOf(column) + "_" + specimenAttribute[row];
			System.out.println("specimenKey: "+specimenKey);
			if(specimenMap.containsKey(specimenKey))
			{
				specimenMap.remove(specimenKey); 
			}
		}
	}

	/*
	 * This method sets Not Specified for the selection boxes by default.
	 */
	private void setNotSpecified(int column)
	{
		//pathological status
		String specimenKey = AppletConstants.SPECIMEN_PREFIX + String.valueOf(column) + "_" + specimenAttribute[AppletConstants.SPECIMEN_PATHOLOGICAL_STATUS_ROW_NO];
		specimenMap.put(specimenKey, Constants.NOT_SPECIFIED);
		
		//tissueside
		specimenKey = AppletConstants.SPECIMEN_PREFIX + String.valueOf(column) + "_" + specimenAttribute[AppletConstants.SPECIMEN_TISSUE_SIDE_ROW_NO];
		specimenMap.put(specimenKey, Constants.NOT_SPECIFIED);	

		//tissuesite
		specimenKey = AppletConstants.SPECIMEN_PREFIX + String.valueOf(column) + "_" + specimenAttribute[AppletConstants.SPECIMEN_TISSUE_SITE_ROW_NO];
		specimenMap.put(specimenKey, Constants.NOT_SPECIFIED);	

	}
//	/**
//	 * @return Returns the pathologicalStatus.
//	 */
//	public String getPathologicalStatus()
//	{
//		return pathologicalStatus;
//	}
//	/**
//	 * @param pathologicalStatus The pathologicalStatus to set.
//	 */
//	public void setPathologicalStatus()
//	{
//		this.pathologicalStatus = specimenAttributeOptions.get(Constants.CDE_NAME_PATHOLOGICAL_STATUS).toString();
//	}
//	/**
//	 * @return Returns the specimenClass.
//	 */
//	public String getSpecimenClass()
//	{
//		return specimenClass;
//	}
//	/**
//	 * @param specimenClass The specimenClass to set.
//	 */
//	public void setSpecimenClass()
//	{
//		this.specimenClass = specimenAttributeOptions.get(Constants.SPECIMEN_CLASS).toString();
//	}
//	/**
//	 * @return Returns the specimenType.
//	 */
//	public String getSpecimenType()
//	{
//		return specimenType;
//	}
//	/**
//	 * @param specimenType The specimenType to set.
//	 */
//	public void setSpecimenType()
//	{
//		this.specimenType = specimenAttributeOptions.get(Constants.SPECIMEN_TYPE).toString();
//	}
//	/**
//	 * @return Returns the tissueSite.
//	 */
//	public String getTissueSite()
//	{
//		return tissueSite;
//	}
//	/**
//	 * @param tissueSite The tissueSite to set.
//	 */
//	public void setTissueSite()
//	{
//		this.tissueSite = specimenAttributeOptions.get(Constants.TISSUE_SITE).toString();
//	}
//	

	/**
	 * @return Returns the buttonStatusMap.
	 */
	public Map getButtonStatusMap()
	{
		return buttonStatusMap;
	}
//****************** Mandar : 22Dec06 For Specimen CheckBox start
	HashMap specimenCheckBoxMap = new HashMap();
	
	/**
	 * This method sets the state of collectionGroup radio button in the map. 
	 * @param column Column number of radio button
	 * @param value Radio button state of that column.
	 */
	public void setSpecimenCheckBoxValueAt(int column,boolean value)
	{
		int actualColumn = getActualColumnNo(column )+1;
		String key = ""+actualColumn;
		specimenCheckBoxMap.put(key, new Boolean(value));
	}
	/**
	 * 
	 * @param column Column number to fetch the collection group radio button state.
	 * @return Radio Button state of collection group for the specified column.
	 */
	public boolean getSpecimenCheckBoxValueAt(int column)
	{
		int actualColumn = getActualColumnNo(column)+1;
		String key = ""+actualColumn;
		if(specimenCheckBoxMap.get(key)==null)
		{
			specimenCheckBoxMap.put(key, new Boolean(false));
		}
		return ((Boolean)specimenCheckBoxMap.get(key)).booleanValue() ;
	}

	private void setActualColumnSpecimenCheckBoxValue(int column)
	{
		String key = ""+column;
		specimenCheckBoxMap.put(key, new Boolean(false));
	}

	
	/**
	 * @return Returns the collectionGroupRadioButtonMap.
	 */
	public HashMap getSpecimenCheckBoxMap()
	{
		return specimenCheckBoxMap;
	}
	/**
	 * @param specimenCheckBoxMap The specimenCheckBoxMap to set.
	 */
	public void setSpecimenCheckBoxMap(HashMap specimenCheckBoxMap)
	{
		this.specimenCheckBoxMap = specimenCheckBoxMap;
	}

	private void removeActualColumnSpecimenCheckBoxValue(int column)
	{
		String key = ""+column;
		specimenCheckBoxMap.remove(key);
	}

//	****************** Mandar : 22Dec06 For Specimen CheckBox end
	
}