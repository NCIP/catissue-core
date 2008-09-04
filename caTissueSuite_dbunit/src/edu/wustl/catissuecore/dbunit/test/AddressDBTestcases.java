package edu.wustl.catissuecore.dbunit.test;

import edu.wustl.catissuecore.domain.Address;
import edu.wustl.catissuecore.domain.User;
import java.io.FileInputStream;

import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.dbunit.operation.DatabaseOperation;

import edu.wustl.catissuecore.domain.Address;
import edu.wustl.catissuecore.domain.CancerResearchGroup;
import edu.wustl.catissuecore.domain.Department;
import edu.wustl.catissuecore.domain.Institution;

import edu.wustl.common.util.global.Constants;


public class AddressDBTestcases extends DefaultCatissueDBUnitTestCase {

	/*public AddressDBTestcases() throws Exception
	{
		super();
		
	}*/
	public void testAddressCreate()
	{
		
		try
		{
			insertObjectsOf(Address.class);
			System.out.println("Inside Address...after insert...");
			assertTrue("AddressCreated",true);
			System.out.println("Inside Address...after assert..");
		}catch(Exception e)
		{
			e.printStackTrace();
			fail("failed to insert Address....create");
			
		}
	}
	public void testAddressUpdate()
	{
		try
		{
	
			UpdateObjects(Address.class);
			assertTrue("AddressUpdated", true);
		}catch(Exception e)
		{
			e.printStackTrace();
			fail("failed to insert Address.......update");
		}
	}

	

}
