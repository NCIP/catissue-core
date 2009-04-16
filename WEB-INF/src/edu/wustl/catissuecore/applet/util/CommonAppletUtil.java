/*
 * <p>Title: CommonAppletUtil.java</p>
 * <p>Description:	This class initializes the fields of CommonAppletUtil.java</p>
 * Copyright: Copyright (c) year 2006
 * Company: Washington University, School of Medicine, St. Louis.
 * @version 1.1
 * Created on Sep 20, 2006
 */

package edu.wustl.catissuecore.applet.util;

import java.awt.Component;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JApplet;
import javax.swing.JTable;

import netscape.javascript.JSObject;
import edu.wustl.catissuecore.applet.AppletConstants;
import edu.wustl.catissuecore.applet.component.BaseTable;
import edu.wustl.catissuecore.applet.model.BaseTabelModel;
import edu.wustl.common.util.logger.Logger;




/**
 * <p> This util class is used to specify common applet level operations.
 * Here all applet related util methods should reside.</p>
 * @author Ashwin Gupta
 * @version 1.1
 */
public final class CommonAppletUtil
{

	private static Logger logger = Logger.getCommonLogger(CommonAppletUtil.class);
	/**
	 * gets the base applet from a given component
	 * @param component component
	 * @return applet  
	 */
	public static JApplet getBaseApplet(Component component)
	{
		while (component != null)
		{
			if (component instanceof JApplet)
			{
				return ((JApplet) component);
			}
			component = component.getParent();
		}
		return null;
	}

	/**
	 * This method calls given javascript function.
	 *  
	 * @param component
	 * @param functionName
	 * @param parameters
	 */
	public static void callJavaScriptFunction(Component component, String functionName,
			Object[] parameters)
	{
		JApplet applet = getBaseApplet(component);

		if (applet != null)
		{
			JSObject jsObject = JSObject.getWindow(applet);
			jsObject.call(functionName, parameters);
		}
	}
	
    /**
     * Checks that the input String contains only numeric digits.
     * @param numString The string whose characters are to be checked.
     * @return Returns false if the String contains any alphabet else returns true. 
     * */
    public static boolean isDoubleNumeric(String numString)
    {
        try
        {
            double doubleValue = Double.parseDouble(numString);
            if (doubleValue <= 0)
            {
                return false;
            }
            
            return true;
        }
        catch(NumberFormatException exp)
        {
        	logger.debug(exp.getMessage(), exp);
            return false;
        }
    }

    //Mandar : 11Oct06 used to get the model in various handler classes.
	/**
	 * This method checks the instance of JTable and its model and then returns the model. 
	 * It is specific to MultipleSpecimen.
	 * @return MultipleSpecimenTableModel 
	 */
	/*public static MultipleSpecimenTableModel getMultipleSpecimenTableModel(JTable table) 
	{	
		if(table != null)
		{
			if(table instanceof BaseTable && table.getModel() instanceof MultipleSpecimenTableModel)
				return (MultipleSpecimenTableModel)table.getModel();
			else
				return null;
		}
		else
			return null;
	}*/

	/**
	 * This method checks the instance of JTable and its model and then returns the model.
	 * @param table base table in applet 
	 * @return BaseTableModel 
	 */
	public static BaseTabelModel getBaseTableModel(JTable table) 
	{
		if(table != null)
		{
			if(table instanceof BaseTable && table.getModel() instanceof BaseTabelModel)
				return (BaseTabelModel)table.getModel();
			else
				return null;
		}
		else
			return null;
	}
	
	/*
	 * For testing of flow.
	 */
	public static boolean validateCell()
	{
		boolean result = true;
		
		return result;
	}
	
	/**
	 * This method returns an List from int array. 
	 * @param array int array.
	 * @return This method returns an List from int array.
	 */
	public static List createListFromArray(int[] array)
	{
		List list = new ArrayList();
		if(array != null)
		{
			for(int index=0; index<array.length; index++ )
			{
				list.add(new Integer(array[index]));
			}
		}
		return list;
	}
	
	public static void printArray(int []array)
	{
		System.out.println("\n------- Printing Array -------\n");
		for(int i=0;i<array.length;i++)
		{
			System.out.print("  "+ array[i] );
		}
		System.out.println("\n-------Printing Array Done ------\n");
	}
	
	public static String getDataKey(int row, int col)
	{
		return String.valueOf(row)+AppletConstants.MULTIPLE_SPECIMEN_ROW_COLUMN_SEPARATOR+String.valueOf(col );	
	}

	
	/**
	 * This method returns the HashMap of data of selected cells.
	 * @param table Table to get the selected cells.
	 * @return HashMap containing data of selected cells.
	 */
	/*public static HashMap getAllDataOnPage(JTable table)
	{
		int numberOfColumns = table.getColumnCount();
		int numberOfRows = table.getRowCount();		
		
		HashMap map = new HashMap();
		for(int rowIndex=1;rowIndex<AppletConstants.SPECIMEN_COMMENTS_ROW_NO; rowIndex++  )
		{
			for(int columnIndex=0; columnIndex<numberOfColumns; columnIndex++)
			{
				String key = CommonAppletUtil.getDataKey(rowIndex, columnIndex);
				//commented to check the values from cell editor
//				Object value = table.getValueAt(selectedRows[rowIndex],selectedColumns[columnIndex] );
				//--------
				TableColumnModel columnModel = table.getColumnModel();
				SpecimenColumnModel scm = (SpecimenColumnModel)columnModel.getColumn(columnIndex).getCellEditor();
				JComponent component = ((JComponent)scm.getTableCellEditorComponent(table,null,true,rowIndex,columnIndex));
				Object value =scm.getCellEditorValue();
				
				getMultipleSpecimenTableModel(table).setValueAt(value,rowIndex,columnIndex);
				// -------
				map.put(key,value );
			}
		}
	//	System.out.println("Returning Map from getAllDataOnPage-------------------------\n");
	//	System.out.println(map);
		return map;
	}*/
	/**
	 * This method returns the HashMap of data of selected cells.
	 * @param table Table to get the selected cells.
	 * @return HashMap containing data of selected cells.
	 */
	/*public static HashMap getSelectedData(JTable table)
	{
		int[] selectedColumns = table.getSelectedColumns();
		int[] selectedRows = table.getSelectedRows();		
		System.out.println("\n/////////// inside getSelectedData ///////////////////\n");
		HashMap map = new HashMap();
		for(int rowIndex=0;rowIndex<selectedRows.length; rowIndex++  )
		{
			for(int columnIndex=0; columnIndex<selectedColumns.length; columnIndex++)
			{
				String key = CommonAppletUtil.getDataKey(selectedRows[rowIndex], selectedColumns[columnIndex]);
				//commented to check the values from cell editor
//				Object value = table.getValueAt(selectedRows[rowIndex],selectedColumns[columnIndex] );
				//--------
				TableColumnModel columnModel = table.getColumnModel();
				SpecimenColumnModel scm = (SpecimenColumnModel)columnModel.getColumn(selectedColumns[columnIndex]).getCellEditor();
				JComponent component = ((JComponent)scm.getTableCellEditorComponent(table,null,true,selectedRows[rowIndex],selectedColumns[columnIndex]));
				Object value =scm.getCellEditorValue();
				
				getMultipleSpecimenTableModel(table).setValueAt(value,selectedRows[rowIndex],selectedColumns[columnIndex]);
				// -------
				map.put(key,value );
			}
		}
		System.out.println("Returning Map -------------------------\n");
		System.out.println(map);
		return map;
	}*/

	public static boolean isNull(Object obj)
	{
		if(obj == null)
			return true;
		else
			return false;
	}
}
