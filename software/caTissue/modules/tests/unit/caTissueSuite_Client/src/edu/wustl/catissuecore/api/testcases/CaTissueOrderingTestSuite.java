/*L
 *  Copyright Washington University in St. Louis
 *  Copyright SemanticBits
 *  Copyright Persistent Systems
 *  Copyright Krishagni
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/catissue-core/LICENSE.txt for details.
 */

package edu.wustl.catissuecore.api.testcases;

import junit.framework.TestSuite;

public class CaTissueOrderingTestSuite {

	public static junit.framework.Test suite() {
		TestSuite suite = new TestSuite("TestCases for caCORE APIs for Ordering and Distribution");

		suite.addTestSuite(OrderingDistributionTestCases.class);
		
		return suite;
	}

}
