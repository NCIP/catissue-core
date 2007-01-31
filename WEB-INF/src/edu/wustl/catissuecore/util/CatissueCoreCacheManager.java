
package edu.wustl.catissuecore.util;

import java.io.Serializable;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheException;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import edu.wustl.catissuecore.util.global.Constants;

/**
 * <p>Title: CatissueCoreCacheManager Class</p>
 * <p>Description:Singleton factory class handling caching operations.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Santosh Chandak
 * @version 1.00
 * Created on Sep 26, 2006
 */

public class CatissueCoreCacheManager
{

	/**
	 * Singleton reference to the CatissueCoreCacheManager
	 */
	private static CatissueCoreCacheManager catissueCoreCacheManager;

	/**
	 *  reference to CacheManager
	 */
	private static CacheManager manager;

	/**
	 *  reference to Cache
	 */
	private static Cache cache;

	/**
	 * Protected constructor of the singleton class
	 * @throws CacheException
	 */
	protected CatissueCoreCacheManager() throws CacheException
	{
		// create singleton instance of CacheManager
		CacheManager.create();
		// get instance of cachemanager
		manager = CacheManager.getInstance();
		// get cache for CatissueCore which is configured in ehcache.xml
		cache = manager.getCache(Constants.EHCACHE_FOR_CATISSUE_CORE);
	}

	/**
	 * Method returning the singleton instance of the
	 * CatissueCoreCacheManager
	 *
	 * @return CatissueCoreCacheManager
	 * @throws CacheException
	 */
	public static synchronized CatissueCoreCacheManager getInstance() throws CacheException
	{
		if (catissueCoreCacheManager == null)
		{
			catissueCoreCacheManager = new CatissueCoreCacheManager();
		}
		return catissueCoreCacheManager;
	}

	/**
	 * Method used to add Serializable object to cache
	 * @param key - Serializable key
	 * @param value - Serializable value
	 */
	public void addObjectToCache(Serializable key, Serializable value)
	{
		// creating elemtnt from key,value
		Element element = new Element(key, value);
		// Adding element to cache
		cache.put(element);
	}
	
	/**
	 * Method used to remove Serializable object to cache
	 * @param key - Serializable key
	 */
	public void removeObjectFromCache(Serializable key)
	{
		// Adding element to cache
		cache.remove(key);
	}


	/**
	 * Method used to get Serializable object from cache
	 * @param key - Serializable Key
	 * @return - Serializable Object
	 * @throws CacheException
	 * @throws IllegalStateException
	 */
	public Serializable getObjectFromCache(Serializable key) throws IllegalStateException, CacheException
	{
		Element element = null;
		element = cache.get(key);
		if(element!=null)
		{
			return element.getValue();
		}
		return null;
	}

	public void shutdown() throws CacheException
	{
		// shutting down the instance of cacheManager
		CacheManager.getInstance().shutdown();

	}

}