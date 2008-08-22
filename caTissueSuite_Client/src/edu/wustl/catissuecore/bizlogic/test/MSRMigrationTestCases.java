/**
 * 
 */

package edu.wustl.catissuecore.bizlogic.test;

import java.util.List;

import edu.wustl.catissuecore.domain.Site;
import edu.wustl.catissuecore.domain.User;
import gov.nih.nci.system.applicationservice.ApplicationException;

/**
 * This class is specifically for the migration code required to move legacy data to 
 * caTissueSuite with MSR functionality
 * 
 * @author juberahamad_patel
 *
 */
public class MSRMigrationTestCases extends MSRBaseTestCase
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
			users = appService.search(User.class, new User());
			sites = appService.search(Site.class, new Site());
		}
		catch (ApplicationException e)
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
					appService.updateObject(user);
				}
				catch (ApplicationException e)
				{
					fail("failed to update User with email id " + user.getEmailAddress());
				}
			}

		}
	}

}
