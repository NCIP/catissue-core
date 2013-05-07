
package edu.wustl.catissuecore.util;

import java.util.Collection;
import java.util.Iterator;

/**
 * <p>This class is used to provide utility methods for specimen array related
 * operations.</p>
 * @author Ashwin Gupta
 * @version 1.1
 */
public final class SpecimenArrayUtil {
	
	/*
	 * create a singleton object 
	 */
	private static SpecimenArrayUtil speciArrayUtil= new SpecimenArrayUtil();
	
	/*
	 * Private constructor
	 */
	private SpecimenArrayUtil()
	{
		
	}
	/*
	 * returns single object 
	 */
	
	public static SpecimenArrayUtil getInsatnce()
	{
		return speciArrayUtil;
	}
	
	
	/**
	 * Gets the string array of specimen types from specimen type collection.
	 * @param specimenTypeCollection specimen type collection
	 * @return string array of specimen types
	 */
	public static String[] getSpecimenTypesFromCollection(Collection specimenTypeCollection) {
		String[] specimenTypes = null;
        
        if ((specimenTypeCollection != null) && (!specimenTypeCollection.isEmpty())) {
        	specimenTypes = new String[specimenTypeCollection.size()];
        	String specimenTypeStr = null;
        	int counter = 0;
        	for (Iterator iter = specimenTypeCollection.iterator(); iter.hasNext();counter++) {
				specimenTypeStr = (String) iter.next();
				specimenTypes[counter] = specimenTypeStr;
			}
        }
		return specimenTypes;
	}
}
