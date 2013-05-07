package edu.wustl.catissuecore.dbunit.test;
import edu.wustl.catissuecore.domain.Address;
import edu.wustl.catissuecore.domain.CancerResearchGroup;
import edu.wustl.catissuecore.domain.Department;
import edu.wustl.catissuecore.domain.Institution;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.domain.DerivedSpecimenOrderItem;
import edu.wustl.common.util.global.Constants;


public class DerivedSpecimenOrderItemDBTestcases extends
		DefaultCatissueDBUnitTestCase {
		
	public void testDerivedSpecimenOrderItemCreate()
	{
		//this.i
		try
		{
			System.out.println("---start");
			insertObjectsOf(DerivedSpecimenOrderItem.class);
			System.out.println("---after insertobjectsof..");
			assertTrue("DerivedSpecimenOrderItemCreated",true);
		}catch(Exception e)
		{
			e.printStackTrace();
			fail("failed to insert DerivedSpecimenOrderItem");
			
		}
	}
	public void testDerivedSpecimenOrderItemUpdate()
	{
		try
		{
	
			UpdateObjects(DerivedSpecimenOrderItem.class);
			assertTrue("DerivedSpecimenOrderItemUpdated", true);
		}catch(Exception e)
		{
			e.printStackTrace();
			fail("failed to insert DerivedSpecimenOrderItem");
		}
	}


}
