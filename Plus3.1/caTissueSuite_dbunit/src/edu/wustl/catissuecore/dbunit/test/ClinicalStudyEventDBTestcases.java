package edu.wustl.catissuecore.dbunit.test;

import edu.wustl.catissuecore.domain.Address;
import edu.wustl.catissuecore.domain.CancerResearchGroup;
import edu.wustl.catissuecore.domain.Department;
import edu.wustl.catissuecore.domain.Institution;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.domain.ClinicalStudyEvent;
import edu.wustl.common.util.global.Constants;

public class ClinicalStudyEventDBTestcases extends
		DefaultCatissueDBUnitTestCase {
	
	public void testClinicalStudyEventCreate()
	{
		//this.i
		try
		{
			System.out.println("---start");
			insertObjectsOf(ClinicalStudyEvent.class);
			System.out.println("---after insertobjectsof..");
			assertTrue("ClinicalStudyEventCreated",true);
		}catch(Exception e)
		{
			e.printStackTrace();
			fail("failed to insert ClinicalStudyEvent....create");
			
		}
	}
	public void testClinicalStudyEventUpdate()
	{
		try
		{
	
			UpdateObjects(ClinicalStudyEvent.class);
			assertTrue("ClinicalStudyEventUpdated", true);
		}catch(Exception e)
		{
			e.printStackTrace();
			fail("failed to insert ClinicalStudyEvent....update");
		}
	}

}
