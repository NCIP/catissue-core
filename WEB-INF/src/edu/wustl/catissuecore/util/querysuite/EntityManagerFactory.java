
package edu.wustl.catissuecore.util.querysuite;

import edu.common.dynamicextensions.entitymanager.EntityManager;
import edu.common.dynamicextensions.entitymanager.EntityManagerInterface;

/**
 * Returns the instance of EntityManager.
 * @author deepti_shelar
 *
 */
public final class EntityManagerFactory
{
	private EntityManagerFactory() {}
	/**
	 *
	 * @return EntityManager EntityManagerInstance
	 */
	public static EntityManagerInterface getEntityManager()
	{
		//return EntityManager.getInstance();
		return EntityManager.getInstance();
	}
}
