/**
 * 
 */

package edu.wustl.catissuecore.dbunit.test;

import java.util.List;

import edu.wustl.catissuecore.dbunit.test.DefaultCatissueDBUnitTestCase;
import edu.wustl.catissuecore.domain.Site;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.common.exception.BizLogicException;

/**
 * This class is specifically for the migration code required to move legacy data to 
 * caTissueSuite with MSR functionality
 * 
 * @author juberahamad_patel
 *
 */
public class MSRMigrationTestCases extends DefaultCatissueDBUnitTestCase
{

	/**
	 * 1. Get collection of all users in the database
	 * 2. Get collection of all sites in the database
	 * 3. Iterate over users, identify supervisors and technicians and add all sites to their site collection 
	 * 4. We are not processing admins and scientists for the time being
	 */
	public void testMigrate()
	{
		List users = null;
		List sites = null;

		try
		{
			users = search(User.class);
			sites = search(Site.class);
		}
		catch (BizLogicException e)
		{
			fail("failed to retrieve all the users and sites");
		}

		for (Object o : users)
		{
			User user = (User) o;
			if (user.getRoleId().equals("2") || user.getRoleId().equals("3"))
			{
				user.getSiteCollection().addAll(sites);
				try
				{
					update(user);
				}
				catch (BizLogicException e)
				{
					fail("failed to update User with email id " + user.getEmailAddress());
				}
			}

		}
	}

}
