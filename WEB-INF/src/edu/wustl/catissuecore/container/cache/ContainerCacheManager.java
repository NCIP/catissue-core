
package edu.wustl.catissuecore.container.cache;

import java.io.Serializable;

import edu.wustl.catissuecore.container.cache.domaininterface.IContainerCacheManager;

public class ContainerCacheManager implements Serializable
{

	private static final long serialVersionUID = 1L;

	/* Singleton instance of ContainerCacheManager
	 */
	private static IContainerCacheManager instance;

	public static IContainerCacheManager getInstance()
	{
		if (instance == null)
		{
			instance = new ContainerCacheManagerImpl();
		}
		return instance;
	}

}