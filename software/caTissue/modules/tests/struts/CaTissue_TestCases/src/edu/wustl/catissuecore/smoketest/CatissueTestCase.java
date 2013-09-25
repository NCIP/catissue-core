/*L
 *  Copyright Washington University in St. Louis
 *  Copyright SemanticBits
 *  Copyright Persistent Systems
 *  Copyright Krishagni
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/catissue-core/LICENSE.txt for details.
 */

package edu.wustl.catissuecore.smoketest;

import junit.framework.Test;
import edu.wustl.testframework.StrutsTestAutomation;

public class CatissueTestCase
{
	public static void main(String[] args)
	{
		junit.swingui.TestRunner.run(CatissueTestCase.class);
	}

	public static Test suite()
	{
//		System.out.println("Count:  "+StrutsTestAutomation.getSuite().countTestCases());
		//strutsProp.home
		return StrutsTestAutomation.getSuite();
	}

}
