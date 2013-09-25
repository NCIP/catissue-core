/*L
 *  Copyright Washington University in St. Louis
 *  Copyright SemanticBits
 *  Copyright Persistent Systems
 *  Copyright Krishagni
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/catissue-core/LICENSE.txt for details.
 */

/**
 * @Class UserDBTestcases.java
 * @Author abhijit_naik
 * @Created on Aug 12, 2008
 */
package edu.wustl.catissuecore.dbunit.test;

import edu.wustl.catissuecore.domain.Address;
import edu.wustl.catissuecore.domain.CancerResearchGroup;
import edu.wustl.catissuecore.domain.Department;
import edu.wustl.catissuecore.domain.Institution;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.common.util.global.Constants;


/**
 * @author abhijit_naik
 *
 */
public class UserDBTestcases extends DefaultCatissueDBUnitTestCase
{


	public void testUserCreate()
	{
		try
		{
			insertObjectsOf(User.class);
			assertTrue("UserCreated",true);
		}catch(Exception e)
		{
			e.printStackTrace();
			fail("failed to insert user");
			
		}
	}
	public void testUserUpdate()
	{
		try
		{
	
			UpdateObjects(User.class);
			assertTrue("UserUpdated", true);
		}catch(Exception e)
		{
			e.printStackTrace();
			fail("failed to insert user");
		}
	}


}
