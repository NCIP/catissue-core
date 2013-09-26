/*L
 *  Copyright Washington University in St. Louis
 *  Copyright SemanticBits
 *  Copyright Persistent Systems
 *  Copyright Krishagni
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/catissue-core/LICENSE.txt for details.
 */

package edu.wustl.catissuecore.testcase.bizlogic;

import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.catissuecore.testcase.CaTissueSuiteBaseTest;
import edu.wustl.common.util.logger.Logger;

public class ComplexCollectionProtocolTestCases extends CaTissueSuiteBaseTest
{
	public void testAddComplexCollectionProtocol()
	{
		try
		 {
			CollectionProtocol collectionProtocol = ComplexCollectionProtocolUtility.initComplexCollectionProtocol();			
			collectionProtocol = (CollectionProtocol) appService.createObject(collectionProtocol);
			BizTestCaseUtility.setObjectMap(collectionProtocol, CollectionProtocol.class);
			assertTrue("Object added successfully", true);
			
			CollectionProtocol collectionProtocol1 = ComplexCollectionProtocolUtility.initComplexCollectionProtocol1();			
			collectionProtocol1 = (CollectionProtocol) appService.createObject(collectionProtocol1);
			BizTestCaseUtility.setObjectMap(collectionProtocol1, CollectionProtocol.class);
			assertTrue("Object added successfully", true);
		 }
		 catch(Exception e)
		 {
			 Logger.out.error(e.getMessage(),e);
			 e.printStackTrace();
			 //assertFalse("could not add object", true);
			 fail("could not add object"+e.getMessage());
		 }
	}

}
