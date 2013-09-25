/*L
 *  Copyright Washington University in St. Louis
 *  Copyright SemanticBits
 *  Copyright Persistent Systems
 *  Copyright Krishagni
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/catissue-core/LICENSE.txt for details.
 */

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
