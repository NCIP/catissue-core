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
import edu.wustl.catissuecore.domain.ConsentTierResponse;
import edu.wustl.common.util.global.Constants;


public class ConsentTierResponseDBtestcases extends
		DefaultCatissueDBUnitTestCase {
		
	public void testConsentTierResponseCreate()
	{
		//this.i
		try
		{
			System.out.println("---start");
			insertObjectsOf(ConsentTierResponse.class);
			System.out.println("---after insertobjectsof..");
			assertTrue("ConsentTierResponseCreated",true);
		}catch(Exception e)
		{
			e.printStackTrace();
			fail("failed to insert ConsentTierResponse...create");
			
		}
	}
	public void testConsentTierResponseUpdate()
	{
		try
		{
	
			UpdateObjects(ConsentTierResponse.class);
			assertTrue("ConsentTierResponseUpdated", true);
		}catch(Exception e)
		{
			e.printStackTrace();
			fail("failed to insert ConsentTierResponse...update");
		}
	}
}
