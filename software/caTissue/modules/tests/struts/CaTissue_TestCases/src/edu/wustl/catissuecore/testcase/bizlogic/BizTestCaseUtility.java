/*L
 *  Copyright Washington University in St. Louis
 *  Copyright SemanticBits
 *  Copyright Persistent Systems
 *  Copyright Krishagni
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/catissue-core/LICENSE.txt for details.
 */

package edu.wustl.catissuecore.testcase.bizlogic;

import java.util.HashMap;
import java.util.Map;


public class BizTestCaseUtility
{
	public final static long CP_WITH_ALLOW_READ_PRIV = 1;
	public final static long CP_WITH_DISALLOW_READ_PRIV = 2;
	public final static long CP_WITH_SCIENTIST_AS_PI = 3;
	public final static long SITE_WITH_ALLOWUSE_PRIV = 1;
	public final static long SITE_WITH_DISALLOWUSE_PRIV = 2;
	public final static long STORAGECONTAINER_WITH_ALLOWUSE_PRIV = 1;
	public final static long STORAGECONTAINER_WITH_DISALLOWUSE_PRIV = 2;
	public final static long SPECIMENARRAYCONTAINER_WITH_ALLOWUSE_PRIV = 3;
	
	private static Map objectMap = new HashMap();
	public static Object getObjectMap(Class className)
	{
		return objectMap.get(className.getName());
	}

	public static void setObjectMap(Object object, Class className)
	{
		objectMap.put(className.getName(), object);
	} 
	public static Object getNameObjectMap(String objName)
	{
		return objectMap.get(objName);
	}

	public static void setNameObjectMap(String ObjName, Object object)
	{
		objectMap.put(ObjName, object);
	} 
	
}
