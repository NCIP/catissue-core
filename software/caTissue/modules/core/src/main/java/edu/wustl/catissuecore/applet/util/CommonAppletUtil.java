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

	/**
	 * logger.
	 */
	private static final Logger LOGGER = Logger.getCommonLogger(CommonAppletUtil.class);

	/**
	 * gets the base applet from a given component.
	 * @param component component
	 * @return applet
	 */
     public static JApplet getBaseApplet(final Component component)
     {
        Component comp = null;
		while (component != null)
		{
			if (component instanceof JApplet)
			{
				//return ((JApplet) component);
				comp = ((JApplet) component);
				break ;
			}
			//component = component.getParent();
			comp = component.getParent();
		}
		//return null;
		return (JApplet)comp ;
	}

	/**
	 * This method calls given javascript function.
	 * @param component : component
	 * @param functionName : functionName
	 * @param parameters : parameters
	 */
	public static void callJavaScriptFunction(Component component, String functionName,
			Object[] parameters)
	{
		final JApplet applet = getBaseApplet(component);

		if (applet != null)
		{
			final JSObject jsObject = JSObject.getWindow(applet);
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
		boolean flag ;
		try
		{
			final double doubleValue = Double.parseDouble(numString);
			if (doubleValue <= 0)
			{
				//return false;
				flag = false ;
			}
			else
			{
				flag = true ;
			}
			//return true;
		}
		catch (final NumberFormatException exp)
		{
			CommonAppletUtil.LOGGER.error(exp.getMessage(), exp);
			exp.printStackTrace();
			//return false;
			flag = false;
		}
		return flag ;
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
		BaseTabelModel  model = null ;

		if (table != null)
		{
			if (table instanceof BaseTable && table.getModel() instanceof BaseTabelModel)
			{
				//return (BaseTabelModel) table.getModel();
				model = (BaseTabelModel) table.getModel();
			}
			else
			{
				//return null;
				model = null ;
			}
		}
		else
		{
			//return null;
			model = null;
		}
		return model ;
	}

	/**
	 * For testing of flow.
	 * @return boolean
	 */
	public static boolean validateCell()
	{
		final boolean result = true;

		return result;
	}

	/**
	 * This method returns an List from int array.
	 * @param array int array.
	 * @return This method returns an List from int array.
	 */
	public static List<Integer> createListFromArray(int[] array)
	{
		final List<Integer> list = new ArrayList<Integer>();
		if (array != null)
		{
			for (final int element : array)
			{
				//list.add(new Integer(element));
				list.add(Integer.valueOf(element));
			}
		}
		return list;
	}

	/**
	 * @param array : array
	 */
	public static void printArray(int[] array)
	{
		CommonAppletUtil.LOGGER.info("\n------- Printing Array -------\n");
		for (final int element : array)
		{
			CommonAppletUtil.LOGGER.info("  " + element);
		}
		CommonAppletUtil.LOGGER.info("\n-------Printing Array Done ------\n");
	}

	/**
	 * @param row : row
	 * @param col : col
	 * @return String
	 */
	public static String getDataKey(int row, int col)
	{
		return String.valueOf(row) + AppletConstants.MULTIPLE_SPECIMEN_ROW_COLUMN_SEPARATOR
				+ String.valueOf(col);
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
	//				Object value = table.getValueAt(selectedRows[rowIndex],
	 * selectedColumns[columnIndex] );
				//--------
				TableColumnModel columnModel = table.getColumnModel();
				SpecimenColumnModel scm = (SpecimenColumnModel)columnModel.
				getColumn(columnIndex).getCellEditor();
				JComponent component = ((JComponent)scm.
				getTableCellEditorComponent(table,null,true,rowIndex,columnIndex));
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
				String key = CommonAppletUtil.getDataKey(selectedRows[rowIndex],
				 selectedColumns[columnIndex]);
				//commented to check the values from cell editor
	//				Object value = table.getValueAt(selectedRows[rowIndex],
	 * selectedColumns[columnIndex] );
				//--------
				TableColumnModel columnModel = table.getColumnModel();
				SpecimenColumnModel scm = (SpecimenColumnModel)columnModel.
				getColumn(selectedColumns[columnIndex]).getCellEditor();
				JComponent component = ((JComponent)scm.getTableCellEditorComponent(
				table,null,true,selectedRows[rowIndex],selectedColumns[columnIndex]));
				Object value =scm.getCellEditorValue();
				getMultipleSpecimenTableModel(table).setValueAt(value,
				selectedRows[rowIndex],selectedColumns[columnIndex]);
				// -------
				map.put(key,value );
			}
		}
		System.out.println("Returning Map -------------------------\n");
		System.out.println(map);
		return map;
	}*/
	/**
	 * @return boolean
	 * @param obj : obj
	 */
	public static boolean isNull(Object obj)
	{
		boolean flag = false;
		if (obj == null)
		{
			flag = true;
		}
		return flag;
	}
}