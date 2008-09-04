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
import edu.wustl.catissuecore.domain.CellSpecimenRequirement;
import edu.wustl.common.util.global.Constants;


public class CellSpecimenRequirementDBTestcases extends
		DefaultCatissueDBUnitTestCase {
	
	public CellSpecimenRequirementDBTestcases() throws Exception
	{
		super();
		
	}
	public void testCellSpecimenRequirementCreate()
	{
		
		try
		{
			insertObjectsOf(CellSpecimenRequirement.class);
			System.out.println("Inside CellSpecimenRequirement...after insert...");
			assertTrue("CellSpecimenRequirement",true);
		}catch(Exception e)
		{
			e.printStackTrace();
			fail("failed to insert CellSpecimenRequirement...create");
			
		}
	}
	public void testCellSpecimenRequirementUpdate()
	{
		try
		{
	
			UpdateObjects(CellSpecimenRequirement.class);
			assertTrue("CellSpecimenRequirement", true);
		}catch(Exception e)
		{
			e.printStackTrace();
			fail("failed to insert CellSpecimenRequirement....update");
		}
	}


}
