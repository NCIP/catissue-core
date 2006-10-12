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
import edu.wustl.catissuecore.applet.model.MultipleSpecimenTableModel;




/**
 * <p> This util class is used to specify common applet level operations.
 * Here all applet related util methods should reside.</p>
 * @author Ashwin Gupta
 * @version 1.1
 */
public final class CommonAppletUtil
{

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
            return false;
        }
    }

    //Mandar : 11Oct06 used to get the model in various handler classes.
	/**
	 * This method checks the instance of JTable and its model and then returns the model. 
	 * It is specific to MultipleSpecimen.
	 * @return MultipleSpecimenTableModel 
	 */
	public static MultipleSpecimenTableModel getMultipleSpecimenTableModel(JTable table) 
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

}
