package edu.wustl.catissuecore.util.querysuite;

import edu.wustl.cab2b.server.cache.EntityCache;
/**
 * This is a factory to get single instance of EntityCache , so that once initialised the same instance will be used everywhere.
 * @author deepti_shelar
 *
 */
public class EntityCacheFactory
{
	/**
	 * private instance of EntityCache.
	 */
	private static EntityCache entityCache = null;
	
	/**
	 * Returns the same instance if not null.
	 * @return EntityCache EntityCacheInstance 
	 */
	public static EntityCache getInstance()
	{
		if(entityCache == null)
		{
			entityCache = EntityCache.getInstance();
		}
		return entityCache;
	}
}
