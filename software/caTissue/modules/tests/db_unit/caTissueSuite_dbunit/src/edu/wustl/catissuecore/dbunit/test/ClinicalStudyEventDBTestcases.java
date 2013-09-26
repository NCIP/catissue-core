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
