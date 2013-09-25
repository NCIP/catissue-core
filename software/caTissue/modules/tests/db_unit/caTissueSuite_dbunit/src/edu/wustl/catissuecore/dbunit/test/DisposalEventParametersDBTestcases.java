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
import edu.wustl.catissuecore.domain.DisposalEventParameters;
import edu.wustl.common.util.global.Constants;

public class DisposalEventParametersDBTestcases extends
		DefaultCatissueDBUnitTestCase {
		
	public void testDisposalEventParametersCreate()
	{
		//this.i
		try
		{
			System.out.println("---start");
			insertObjectsOf(DisposalEventParameters.class);
			System.out.println("---after insertobjectsof..");
			assertTrue("DisposalEventParametersCreated",true);
		}catch(Exception e)
		{
			e.printStackTrace();
			fail("failed to insert DisposalEventParameters");
			
		}
	}
	public void testDisposalEventParametersUpdate()
	{
		try
		{
	
			UpdateObjects(DisposalEventParameters.class);
			assertTrue("DisposalEventParametersUpdated", true);
		}catch(Exception e)
		{
			e.printStackTrace();
			fail("failed to insert DisposalEventParameters");
		}
	}
}
