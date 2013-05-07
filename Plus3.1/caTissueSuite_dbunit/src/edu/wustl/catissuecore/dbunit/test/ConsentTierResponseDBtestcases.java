package edu.wustl.catissuecore.dbunit.test;
import edu.wustl.catissuecore.domain.Address;
import edu.wustl.catissuecore.domain.CancerResearchGroup;
import edu.wustl.catissuecore.domain.Department;
import edu.wustl.catissuecore.domain.Institution;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.domain.ConsentTierResponse;
import edu.wustl.common.util.global.Constants;


public class ConsentTierResponseDBtestcases extends
		DefaultCatissueDBUnitTestCase {
		
	public void testConsentTierResponseCreate()
	{
		//this.i
		try
		{
			System.out.println("---start");
			insertObjectsOf(ConsentTierResponse.class);
			System.out.println("---after insertobjectsof..");
			assertTrue("ConsentTierResponseCreated",true);
		}catch(Exception e)
		{
			e.printStackTrace();
			fail("failed to insert ConsentTierResponse...create");
			
		}
	}
	public void testConsentTierResponseUpdate()
	{
		try
		{
	
			UpdateObjects(ConsentTierResponse.class);
			assertTrue("ConsentTierResponseUpdated", true);
		}catch(Exception e)
		{
			e.printStackTrace();
			fail("failed to insert ConsentTierResponse...update");
		}
	}
}
