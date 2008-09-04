package edu.wustl.catissuecore.dbunit.test;
import edu.wustl.catissuecore.domain.User;
import java.io.FileInputStream;

import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.dbunit.operation.DatabaseOperation;

import edu.wustl.catissuecore.domain.Address;
import edu.wustl.catissuecore.domain.CancerResearchGroup;
import edu.wustl.catissuecore.domain.Department;
import edu.wustl.catissuecore.domain.Institution;
import edu.wustl.catissuecore.domain.Capacity;

import edu.wustl.common.util.global.Constants;


public class CapacityDBTestcases extends DefaultCatissueDBUnitTestCase {
	
	public void testCapacityCreate()
	{
		
		try
		{
			insertObjectsOf(Capacity.class);
			System.out.println("Inside Capacity...after insert...");
			assertTrue("CapacityCreated",true);
			System.out.println("Inside Capacity...after assert...");
		}catch(Exception e)
		{
			e.printStackTrace();
			fail("failed to insert Capacity...create");
			
		}
	}
	public void testCapacityUpdate()
	{
		try
		{
	
			UpdateObjects(Capacity.class);
			assertTrue("CapacityUpdated", true);
		}catch(Exception e)
		{
			e.printStackTrace();
			fail("failed to insert Capacity...update");
		}
	}

}
