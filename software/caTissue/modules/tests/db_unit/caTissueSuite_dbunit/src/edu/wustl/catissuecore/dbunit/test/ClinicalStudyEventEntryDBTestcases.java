package edu.wustl.catissuecore.dbunit.test;
import edu.wustl.catissuecore.domain.Address;
import edu.wustl.catissuecore.domain.CancerResearchGroup;
import edu.wustl.catissuecore.domain.Department;
import edu.wustl.catissuecore.domain.Institution;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.domain.ClinicalStudyEventEntry;
import edu.wustl.common.util.global.Constants;


public class ClinicalStudyEventEntryDBTestcases extends
		DefaultCatissueDBUnitTestCase {
		
	public void testClinicalStudyEventEntryCreate()
	{
		//this.i
		try
		{
			System.out.println("---start");
			insertObjectsOf(ClinicalStudyEventEntry.class);
			System.out.println("---after insertobjectsof..");
			assertTrue("ClinicalStudyEventEntryCreated",true);
		}catch(Exception e)
		{
			e.printStackTrace();
			fail("failed to insert ClinicalStudyEventEntry....create");
			
		}
	}
	public void testClinicalStudyEventEntryUpdate()
	{
		try
		{
	
			UpdateObjects(ClinicalStudyEventEntry.class);
			assertTrue("ClinicalStudyEventEntryUpdated", true);
		}catch(Exception e)
		{
			e.printStackTrace();
			fail("failed to insert ClinicalStudyEventEntry....update");
		}
	}


}
