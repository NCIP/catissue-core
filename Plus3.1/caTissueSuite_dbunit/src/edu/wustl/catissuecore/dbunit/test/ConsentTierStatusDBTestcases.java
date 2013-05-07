package edu.wustl.catissuecore.dbunit.test;
import edu.wustl.catissuecore.domain.Address;
import edu.wustl.catissuecore.domain.CancerResearchGroup;
import edu.wustl.catissuecore.domain.Department;
import edu.wustl.catissuecore.domain.Institution;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.domain.ConsentTierStatus;
import edu.wustl.common.util.global.Constants;

public class ConsentTierStatusDBTestcases extends DefaultCatissueDBUnitTestCase {
	
	public void testConsentTierStatusCreate()
	{
		//this.i
		try
		{
			System.out.println("---start");
			insertObjectsOf(ConsentTierStatus.class);
			System.out.println("---after insertobjectsof..");
			assertTrue("ConsentTierStatusCreated",true);
		}catch(Exception e)
		{
			e.printStackTrace();
			fail("failed to insert ConsentTierStatus....create");
			
		}
	}
	public void testConsentTierStatusUpdate()
	{
		try
		{
	
			UpdateObjects(ConsentTierStatus.class);
			assertTrue("ConsentTierStatusUpdated", true);
		}catch(Exception e)
		{
			e.printStackTrace();
			fail("failed to insert ConsentTierStatus....update");
		}
	}

}
