/*L
 *  Copyright Washington University in St. Louis
 *  Copyright SemanticBits
 *  Copyright Persistent Systems
 *  Copyright Krishagni
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/catissue-core/LICENSE.txt for details.
 */

package edu.wustl.catissuecore.testcase.util;

import java.util.HashMap;
import java.util.Map;
/**
 * This class keeps the track of all domain objects in an objectMap
 * @author Himanshu Aseeja
 */
public class TestCaseUtility 
{
	private static Map objectMap = new HashMap();

	public static Map getObjectMap() 
	{
		return objectMap;
	}

	public static void setObjectMap(Map objectMap) 
	{
		TestCaseUtility.objectMap = objectMap;
	}
	
	public static Object getNameObjectMap(String objName)
	{
		return objectMap.get(objName);
	}

	public static void setNameObjectMap(String ObjName, Object object)
	{
		objectMap.put(ObjName, object);
	} 
	
	public static void removeObjectFromMap(String ObjName)
	{
		objectMap.remove(ObjName);
	} 
	
}
