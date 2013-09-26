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
import edu.wustl.catissuecore.domain.Address;
import edu.wustl.catissuecore.domain.CancerResearchGroup;
import edu.wustl.catissuecore.domain.Department;
import edu.wustl.catissuecore.domain.Institution;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.domain.ClinicalStudy;
import edu.wustl.common.util.global.Constants;


public class ClinicalStudyDBTestcases extends DefaultCatissueDBUnitTestCase {
	
	public void testClinicalStudyCreate()
	{
		//this.i
		try
		{
			System.out.println("---start");
			insertObjectsOf(ClinicalStudy.class);
			System.out.println("---after insertobjectsof..");
			assertTrue("ClinicalStudy",true);
		}catch(Exception e)
		{
			e.printStackTrace();
			fail("failed to insert ClinicalStudy....create");
			
		}
	}
	public void testClinicalStudyUpdate()
	{
		try
		{
	
			UpdateObjects(ClinicalStudy.class);
			assertTrue("ClinicalStudy Updated", true);
		}catch(Exception e)
		{
			e.printStackTrace();
			fail("failed to insert ClinicalStudy....false");
		}
	}

}
