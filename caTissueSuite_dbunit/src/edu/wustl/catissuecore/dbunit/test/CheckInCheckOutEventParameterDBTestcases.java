package edu.wustl.catissuecore.dbunit.test;
import edu.wustl.catissuecore.domain.Address;
import edu.wustl.catissuecore.domain.CancerResearchGroup;
import edu.wustl.catissuecore.domain.Department;
import edu.wustl.catissuecore.domain.Institution;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.domain.CheckInCheckOutEventParameter;

import edu.wustl.common.util.global.Constants;


public class CheckInCheckOutEventParameterDBTestcases extends DefaultCatissueDBUnitTestCase {
	
	public CheckInCheckOutEventParameterDBTestcases() throws Exception
	{
		super();
		
	}
	public void testCheckInCheckOutEventParameterCreate()
	{
		
		try
		{
			insertObjectsOf(CheckInCheckOutEventParameter.class);
			System.out.println("Inside CheckInCheckOutEventParameter...after insert...");
			assertTrue("CheckInCheckOutEventParameter",true);
		}catch(Exception e)
		{
			e.printStackTrace();
			fail("failed to insert CheckInCheckOutEventParameter....create");
			
		}
	}
	public void testCheckInCheckOutEventParameterUpdate()
	{
		try
		{
	
			UpdateObjects(CheckInCheckOutEventParameter.class);
			assertTrue("CheckInCheckOutEventParameterUpdated", true);
		}catch(Exception e)
		{
			e.printStackTrace();
			fail("failed to insert CheckInCheckOutEventParameter....update");
		}
	}


}
