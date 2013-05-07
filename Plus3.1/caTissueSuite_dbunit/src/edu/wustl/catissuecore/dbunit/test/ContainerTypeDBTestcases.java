package edu.wustl.catissuecore.dbunit.test;
import edu.wustl.catissuecore.domain.Address;
import edu.wustl.catissuecore.domain.CancerResearchGroup;
import edu.wustl.catissuecore.domain.Department;
import edu.wustl.catissuecore.domain.Institution;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.domain.ContainerType;
import edu.wustl.common.util.global.Constants;

public class ContainerTypeDBTestcases extends DefaultCatissueDBUnitTestCase {
		
	public void testContainerTypeCreate()
	{
		//this.i
		try
		{
			System.out.println("---start");
			insertObjectsOf(ContainerType.class);
			System.out.println("---after insertobjectsof..");
			assertTrue("ContainerTypeCreated",true);
		}catch(Exception e)
		{
			e.printStackTrace();
			fail("failed to insert ContainerType...create");
			
		}
	}
	public void testContainerTypeUpdate()
	{
		try
		{
	
			UpdateObjects(ContainerType.class);
			assertTrue("ContainerTypeUpdated", true);
		}catch(Exception e)
		{
			e.printStackTrace();
			fail("failed to insert ContainerType.....update");
		}
	}
}
