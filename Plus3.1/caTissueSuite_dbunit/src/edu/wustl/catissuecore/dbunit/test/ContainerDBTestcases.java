package edu.wustl.catissuecore.dbunit.test;
import edu.wustl.catissuecore.domain.Address;
import edu.wustl.catissuecore.domain.CancerResearchGroup;
import edu.wustl.catissuecore.domain.Department;
import edu.wustl.catissuecore.domain.Institution;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.domain.Container;
import edu.wustl.common.util.global.Constants;

public class ContainerDBTestcases extends DefaultCatissueDBUnitTestCase {
		
	public void testContainerCreate()
	{
		//this.i
		try
		{
			System.out.println("---start");
			insertObjectsOf(Container.class);
			System.out.println("---after insertobjectsof..");
			assertTrue("ContainerCreated",true);
		}catch(Exception e)
		{
			e.printStackTrace();
			fail("failed to insert Container....create");
			
		}
	}
	public void testContainerUpdate()
	{
		try
		{
	
			UpdateObjects(Container.class);
			assertTrue("ContainerUpdated", true);
		}catch(Exception e)
		{
			e.printStackTrace();
			fail("failed to insert Container...update");
		}
	}
}
