package edu.wustl.catissuecore.dbunit.test;
import edu.wustl.catissuecore.domain.Address;
import edu.wustl.catissuecore.domain.CancerResearchGroup;
import edu.wustl.catissuecore.domain.Department;
import edu.wustl.catissuecore.domain.Institution;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.domain.ContainerPosition;
import edu.wustl.common.util.global.Constants;

public class ContainerPositionDBTestcases extends DefaultCatissueDBUnitTestCase {
		
	public void testContainerPositionCreate()
	{
		//this.i
		try
		{
			System.out.println("---start");
			insertObjectsOf(ContainerPosition.class);
			System.out.println("---after insertobjectsof..");
			assertTrue("ContainerPositionCreated",true);
		}catch(Exception e)
		{
			e.printStackTrace();
			fail("failed to insert ContainerPosition");
			
		}
	}
	public void testContainerPositionUpdate()
	{
		try
		{
	
			UpdateObjects(ContainerPosition.class);
			assertTrue("ContainerPositionUpdated", true);
		}catch(Exception e)
		{
			e.printStackTrace();
			fail("failed to insert ContainerPosition");
		}
	}
}
