
package edu.wustl.catissuecore.container.cache.junit;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import junit.framework.TestCase;
import edu.wustl.catissuecore.container.cache.ContainerCache;
import edu.wustl.catissuecore.container.cache.ContainerCacheManager;
import edu.wustl.catissuecore.container.cache.ContainerPositionDetails;
import edu.wustl.catissuecore.container.cache.domaininterface.IContainerCacheManager;


/**
 * @author ashish_gupta
 *
 */
public class TestContainerCacheManager extends TestCase
{

	IContainerCacheManager containerCacheManager = ContainerCacheManager.getInstance();

	//	public void testLoadContainerCache()
	//	{
	//		
	//		
	//	}
	@Override
	@Before
	protected void setUp() throws Exception
	{
		Long start = Long.valueOf(System.currentTimeMillis());
		containerCacheManager.loadCache();
		Long end = Long.valueOf(System.currentTimeMillis());
		System.out
				.println("Time required to populate container cache "
						+ "for 200 CPs, 50 users and 4 Specimen classes(ie 40K keys)and 2K containers for each key, is "
						+ (end - start) + "milliseconds");
		super.setUp();
	}

	@Override
	@After
	protected void tearDown() throws Exception
	{
		// TODO Auto-generated method stub
		super.tearDown();
	}

	/**
	 * 
	 */
	@Test
	public void testAddPosition()
	{
		int xPos = 10;
		int yPos = 5;
		Long containerId = Long.valueOf(1);
		String containerName = "1";

		containerCacheManager.addPosition(containerId, xPos, yPos);

		boolean isPositionFree = containerCacheManager.isPositionFree(containerId, xPos, yPos);

		if (!isPositionFree)
		{
			fail();
		}

		//		containerCacheManager.addPosition(containerName, xPos , yPos);
		//		isPositionFree = containerCacheManager.isPositionFree(containerName, xPos, yPos);
		//		
		//		if(!isPositionFree)
		//		{
		//			fail();
		//		}
	}

	/**
	 * 
	 */
	@Test
	public void testGetContainerCacheObjects()
	{

		//TODO add container cache objects
		Long cpId = 1L;
		Long userId = 1L;
		String spClass = "4";
		List<ContainerCache> cntCacheList = containerCacheManager.getContainerCacheObjects(cpId,
				userId, spClass);

		if (cntCacheList.size() == 0)
		{
			fail();
		}
	}

	/**
	 * 
	 */
	@Test
	public void testIsPositionFree()
	{
		int  xPos = 11;
		int  yPos = 23;
		Long containerId = 18L;

		containerCacheManager.addPosition(containerId, xPos, yPos);

		boolean isPositionFree = containerCacheManager.isPositionFree(containerId, xPos, yPos);

		if (!isPositionFree)
		{
			fail();
		}
	}

	/**
	 * 
	 */
	@Test
	public void testRemovePosition()
	{
		int xPos = 18;
		int yPos = 26;
		Long containerId = 10L;
		containerCacheManager.addPosition(containerId, xPos, yPos);

		containerCacheManager.removePosition(containerId, xPos, yPos);

		boolean isPositionFree = containerCacheManager.isPositionFree(containerId, xPos, yPos);
		if (isPositionFree)
		{
			fail();
		}
	}

	/**
	 * This test case gets the specimfied number of free positions from one or multiple containers 
	 */
	@Test
	public void testGetContainerFreePositions()
	{
		Long cpId = 1L;
		Long userId = 1L;
		String spClass = "4";

		int count = 2;
		List<ContainerPositionDetails> containerPos = containerCacheManager
				.getContainerFreePositions(cpId, spClass, userId, count);

		if (containerPos.size() != 2)
		{
			fail();
		}

	}

	/**
	 * This test case gets the next free position
	 */
	@Test
	public void testGetNextFreePosition()
	{
		Long cpId = 1L;
		Long userId = 1L;
		String spClass = "4";

		ContainerPositionDetails containerPos = containerCacheManager.getNextFreePosition(cpId,
				spClass, userId);

		if (containerPos == null)
		{
			fail();
		}
	}

}
