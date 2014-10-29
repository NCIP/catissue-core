/*L
 *  Copyright Washington University in St. Louis
 *  Copyright SemanticBits
 *  Copyright Persistent Systems
 *  Copyright Krishagni
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/catissue-migration-tool/LICENSE.txt for details.
 */

package com.krishagni.catissueplus.bulkoperator.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Comparator;

import edu.wustl.common.util.logger.Logger;

public class SortObject implements Comparator<Object>
{

	private static final Logger logger = Logger.getCommonLogger(SortObject.class);

	public int compare(Object object1, Object object2)
	{
		int returnValue = 0;
		try
		{
			Method getId = object1.getClass().getMethod("getId", (Class<?>[])null);
			Long id1 = (Long) getId.invoke(object1, (Class<?>[])null);
			Method getId2 = object2.getClass().getMethod("getId", (Class<?>[])null);
			Long id2 = (Long) getId2.invoke(object2, (Class<?>[])null);
			if (id1 < id2)
			{
				returnValue = -1;
			}
			else if (id1 > id2)
			{
				returnValue = 1;
			}
			else if (Long.valueOf(id1) == Long.valueOf(id2))
			{
				returnValue = 0;
			}
		}
		catch (SecurityException securtyExp)
		{
			logger.error(securtyExp.getMessage(), securtyExp);
		}
		catch (NoSuchMethodException noSuchMthExp)
		{
			logger.error(noSuchMthExp.getMessage(), noSuchMthExp);
		}
		catch (IllegalArgumentException illArgExp)
		{
			logger.error(illArgExp.getMessage(), illArgExp);
		}
		catch (IllegalAccessException illAccExp)
		{
			logger.error(illAccExp.getMessage(), illAccExp);
		}
		catch (InvocationTargetException invocationTrgExp)
		{
			logger.error(invocationTrgExp.getMessage(), invocationTrgExp);
		}
		return returnValue;
	}

}
