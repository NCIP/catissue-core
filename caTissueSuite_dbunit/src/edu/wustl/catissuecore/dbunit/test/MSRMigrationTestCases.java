/**
 * 
 */

package edu.wustl.catissuecore.dbunit.test;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
				user.getSiteCollection().clear();
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
	
	/**
	 * for the time being, checking that 
	 * 1. there is at least one technician or supervisor, so that our core code is executed
	 * 2. the site collection for each technician and supervisor equals the set of all sites in the database
	 * 
	 */
	
	public void testVerifyMigration()
	{
		
		List users = null;
		Set sites = null;

		try
		{
			users = search(User.class);
			sites = new HashSet(search(Site.class));
		}
		catch (BizLogicException e)
		{
			fail("failed to retrieve all the users and sites");
		}

		int techsOrSups = 0;
		for (Object o : users)
		{
			User user = (User) o;
			if (user.getRoleId().equals("2") || user.getRoleId().equals("3"))
			{
				techsOrSups++;
				
				if(!CompareSiteSets(sites, user.getSiteCollection()))
				{
					fail();
				}
			}
			
		}
		
		
		if(techsOrSups==0)
		{
			fail();
		}

	}

	/**
	 * Due to absence of equals() method in the domain class hierarchy
	 * 	we have to implement the comparison, based on site ids
	 * 
	 * @return true if they are equal
	 */
	private boolean CompareSiteSets(Set sites, Collection<Site> siteCollection)
	{
		if(siteCollection.size()!=sites.size())
		{
			return false;
		}
		
		Set<Long> siteIds = new HashSet<Long>(siteCollection.size());
		
		for(Site site : siteCollection)
		{
			siteIds.add(site.getId());
		}
		
		for(Object o : sites)
		{
			Long id = ((Site) o).getId();
			if(!siteIds.contains(id))
			{
				return false;
			}
		}
		
		return true;

	}
	
}
