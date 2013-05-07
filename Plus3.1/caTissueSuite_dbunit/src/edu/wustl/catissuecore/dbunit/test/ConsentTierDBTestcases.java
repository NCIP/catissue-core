package edu.wustl.catissuecore.dbunit.test;
import edu.wustl.catissuecore.domain.Address;
import edu.wustl.catissuecore.domain.CancerResearchGroup;
import edu.wustl.catissuecore.domain.Department;
import edu.wustl.catissuecore.domain.Institution;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.domain.ConsentTier;
import edu.wustl.common.util.global.Constants;


public class ConsentTierDBTestcases extends DefaultCatissueDBUnitTestCase {
		
	public void testConsentTierCreate()
	{
		//this.i
		try
		{
			System.out.println("---start");
			insertObjectsOf(ConsentTier.class);
			System.out.println("---after insertobjectsof..");
			assertTrue("ConsentTierCreated",true);
		}catch(Exception e)
		{
			e.printStackTrace();
			fail("failed to insert ConsentTier...create");
			
		}
	}
	public void testConsentTierUpdate()
	{
		try
		{
	
			UpdateObjects(ConsentTier.class);
			assertTrue("ConsentTierUpdated", true);
		}catch(Exception e)
		{
			e.printStackTrace();
			fail("failed to insert ConsentTier....update");
		}
	}

}
