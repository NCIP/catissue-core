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

import edu.wustl.common.util.global.Constants;

public class DepartmentDBTestcases extends DefaultCatissueDBUnitTestCase {

	public DepartmentDBTestcases() throws Exception
	{
		super();
		
	}
	public void testDepartmentCreate()
	{
		
		try
		{
			insertObjectsOf(Department.class);
			System.out.println("Inside Department...after insert...");
			assertTrue("DepartmentCreated",true);
		}catch(Exception e)
		{
			e.printStackTrace();
			fail("failed to insert Department");
			
		}
	}
	public void testDepartmentUpdate()
	{
		try
		{
	
			UpdateObjects(Department.class);
			assertTrue("DepartmentUpdated", true);
		}catch(Exception e)
		{
			e.printStackTrace();
			fail("failed to insert department");
		}
	}

	
}
