package edu.wustl.catissuecore.dbunit.test;
import edu.wustl.catissuecore.domain.Address;
import edu.wustl.catissuecore.domain.CancerResearchGroup;
import edu.wustl.catissuecore.domain.Department;
import edu.wustl.catissuecore.domain.Institution;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.domain.ClinicalStudyRegistration;
import edu.wustl.common.util.global.Constants;


public class ClinicalStudyRegistrationDBTestcases extends
		DefaultCatissueDBUnitTestCase {
	
	public void testClinicalStudyRegistrationCreate()
	{
		//this.i
		try
		{
			System.out.println("---start");
			insertObjectsOf(ClinicalStudyRegistration.class);
			System.out.println("---after insertobjectsof..");
			assertTrue("ClinicalStudyRegistrationCreated",true);
		}catch(Exception e)
		{
			e.printStackTrace();
			fail("failed to insert ClinicalStudyRegistration....create");
			
		}
	}
	public void testClinicalStudyRegistrationUpdate()
	{
		try
		{
	
			UpdateObjects(ClinicalStudyRegistration.class);
			assertTrue("ClinicalStudyRegistrationUpdated", true);
		}catch(Exception e)
		{
			e.printStackTrace();
			fail("failed to insert ClinicalStudyRegistration....update");
		}
	}


}
