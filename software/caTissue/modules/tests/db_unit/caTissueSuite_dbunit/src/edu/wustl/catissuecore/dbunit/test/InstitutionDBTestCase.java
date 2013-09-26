/*L
 *  Copyright Washington University in St. Louis
 *  Copyright SemanticBits
 *  Copyright Persistent Systems
 *  Copyright Krishagni
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/catissue-core/LICENSE.txt for details.
 */

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


public class InstitutionDBTestCase extends DefaultCatissueDBUnitTestCase {
	
	public InstitutionDBTestCase() throws Exception
	{
		super();
		
	}
	public void testInstitutionCreate()
	{
		try
		{
			insertObjectsOf(Institution.class);
			System.out.println("In Inst..after insert...");
			assertTrue("InstitutionCreated",true);
			System.out.println("after verify  assert......");
		}catch(Exception e)
		{
			e.printStackTrace();
			fail("failed to insert institution");
			
		}
	}
	public void testInstitutionUpdate()
	{
		try
		{
	
			UpdateObjects(Institution.class);
			assertTrue("InstitutionUpdated", true);
		}catch(Exception e)
		{
			e.printStackTrace();
			fail("failed to insert institution");
		}
	}

}
