package edu.wustl.catissuecore.util;

import java.util.Comparator;
/**
 * @author Hassan
 *
 */

public class StringComprator implements Comparator {
	public int compare(Object obj1, Object obj2)
	{
		int returnVal=obj1.toString().toLowerCase().compareTo(obj2.toString().toLowerCase());
		
		return returnVal;
		
	}
}
