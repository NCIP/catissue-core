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
import edu.wustl.catissuecore.domain.CancerResearchGroup;
import edu.wustl.catissuecore.domain.Department;
import edu.wustl.catissuecore.domain.Institution;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.common.util.global.Constants;


public class CollectionProtocolDBTestcases extends
		DefaultCatissueDBUnitTestCase {
	
	public void testCollectionProtocolCreate()
	{
		//this.i
		try
		{
			System.out.println("---start");
			insertObjectsOf(CollectionProtocol.class);
			System.out.println("---after insertobjectsof..");
			assertTrue("CollectionProtocolCreated",true);
		}catch(Exception e)
		{
			e.printStackTrace();
			fail("failed to insert CollectionProtocol....create");
			
		}
	}
	public void testCollectionProtocolUpdate()
	{
		try
		{
	
			UpdateObjects(CollectionProtocol.class);
			assertTrue("CollectionProtocolUpdated", true);
		}catch(Exception e)
		{
			e.printStackTrace();
			fail("failed to insert CollectionProtocol....update");
		}
	}


}
