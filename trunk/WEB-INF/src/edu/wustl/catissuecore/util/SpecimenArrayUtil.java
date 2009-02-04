
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
        	int i = 0;
        	for (Iterator iter = specimenTypeCollection.iterator(); iter.hasNext();i++) {
				specimenTypeStr = (String) iter.next();
				specimenTypes[i] = specimenTypeStr;
			}
        }
		return specimenTypes;
	}
}
