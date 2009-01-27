
package edu.wustl.catissuecore.util.querysuite;

import edu.wustl.cab2b.server.cache.EntityCache;

/**
 * This is a factory to get single instance of EntityCache ,
 * so that once initialised the same instance will be used everywhere.
 * @author deepti_shelar
 *
 */
public final class EntityCacheFactory
{
	/**
	 * Empty constructor.
	 */
	private EntityCacheFactory(){}
	/**
	 * Returns the instance of EntityCache.
	 * @return EntityCache EntityCacheInstance
	 */
	public static EntityCache getInstance()
	{
		return EntityCache.getInstance();
	}
}
