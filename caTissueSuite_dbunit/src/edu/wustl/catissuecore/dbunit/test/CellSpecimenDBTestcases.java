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
import edu.wustl.catissuecore.domain.CellSpecimen;
import edu.wustl.common.util.global.Constants;



public class CellSpecimenDBTestcases extends DefaultCatissueDBUnitTestCase {

	public void testCellSpecimenCreate()
	{
		
		try
		{
			insertObjectsOf(CellSpecimen.class);
			System.out.println("Inside CellSpecimen...after insert...");
			assertTrue("CellSpecimenCreated",true);
		}catch(Exception e)
		{
			e.printStackTrace();
			fail("failed to insert CellSpecimen...create");
			
		}
	}
	public void testCellSpecimenUpdate()
	{
		try
		{
	
			UpdateObjects(CellSpecimen.class);
			assertTrue("CellSpecimenUpdated", true);
		}catch(Exception e)
		{
			e.printStackTrace();
			fail("failed to insert CellSpecimen....update");
		}
	}

}
