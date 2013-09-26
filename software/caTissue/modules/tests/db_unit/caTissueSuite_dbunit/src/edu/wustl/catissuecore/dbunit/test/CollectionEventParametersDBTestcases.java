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

import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.domain.CancerResearchGroup;
import edu.wustl.catissuecore.domain.Department;
import edu.wustl.catissuecore.domain.Institution;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.domain.CollectionEventParameters;
import edu.wustl.common.util.global.Constants;

public class CollectionEventParametersDBTestcases extends
		DefaultCatissueDBUnitTestCase {
	
	public void testCollectionEventParametersCreate()
	{
		//this.i
		try
		{
			System.out.println("---start");
			insertObjectsOf(CollectionEventParameters.class);
			System.out.println("---after insertobjectsof..");
			assertTrue("CollectionEventParametersCreated",true);
		}catch(Exception e)
		{
			e.printStackTrace();
			fail("failed to insert CollectionEventParameters....create");
			
		}
	}
	public void testCollectionEventParametersUpdate()
	{
		try
		{
	
			UpdateObjects(CollectionEventParameters.class);
			assertTrue("CollectionEventParametersUpdated", true);
		}catch(Exception e)
		{
			e.printStackTrace();
			fail("failed to insert CollectionEventParameters....update");
		}
	}


}
