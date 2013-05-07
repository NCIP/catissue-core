package edu.wustl.catissuecore.dbunit.test;

import java.io.FileInputStream;

import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.dbunit.operation.DatabaseOperation;

import edu.wustl.catissuecore.domain.Address;
import edu.wustl.catissuecore.domain.CancerResearchGroup;
import edu.wustl.catissuecore.domain.Department;
import edu.wustl.catissuecore.domain.Institution;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.common.util.global.Constants;


public class CancerResearchGroupDBTestcases extends
		DefaultCatissueDBUnitTestCase {
	
	public CancerResearchGroupDBTestcases() throws Exception
	{
		super();
		
	}

	public void testCancerResearchGroupCreate()
	{
		try
		{
			insertObjectsOf(CancerResearchGroup.class);
			System.out.println("In CRG..after insert...");
			assertTrue("CancerResearchGroupCreated",true);
			System.out.println("after verify  assert......");
		}catch(Exception e)
		{
			e.printStackTrace();
			fail("failed to insert CancerResearchGroup..create");
			
		}
	}
	public void testCancerResearchGroupUpdate()
	{
		try
		{
	
			UpdateObjects(User.class);
			assertTrue("CancerResearchGroupUpdated", true);
		}catch(Exception e)
		{
			e.printStackTrace();
			fail("failed to insert CancerResearchGroup...update");
		}
	}

}
