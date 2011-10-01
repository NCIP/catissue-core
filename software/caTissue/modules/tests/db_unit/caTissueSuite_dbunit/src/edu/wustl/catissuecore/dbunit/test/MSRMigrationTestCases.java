/**
 * 
 */

package edu.wustl.catissuecore.dbunit.test;

import java.util.ArrayList;
import java.util.List;

import edu.wustl.catissuecore.domain.Site;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.global.Variables;
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
		Variables.isPhoneNumberToBeValidated = false;
		List<User> userList = null;
		List<Site> siteList = null;

		int noOfSupervisorsOrTechnicians = 0;
		
		try
		{
			userList = search(User.class);
			siteList = getRepositorySites();
		}
		catch (BizLogicException e)
		{
			fail("failed to retrieve all the users and sites");
		}
		
		for (User user1 : userList)
		{
			User user = (User) user1;
			try
			{
				if (user.getRoleId().equals("2") || user.getRoleId().equals("3"))
				{
					System.out.println("#####  INSIDE GET ROLE ID  ##########");
					System.out.println("SITE COLLECTION "+user.getSiteCollection());
					user.getSiteCollection().clear();
					user.getSiteCollection().addAll(siteList);
					System.out.println("BEFORE UPDATE USER");
						update(user);
					System.out.println("AFTER UPDATE");
						noOfSupervisorsOrTechnicians++;
				}
			}
			catch (Exception e)
			{
				e.printStackTrace();
				fail("failed to update User with email id " + user.getEmailAddress());
			}

		}
		System.out.println();
		System.out.println("######## NO OF NON ADMIN USERS :: "+noOfSupervisorsOrTechnicians+"      #############");
		System.out.println();
		Variables.isPhoneNumberToBeValidated = true;
	}
	
	/**
	 * for the time being, checking that 
	 * 1. there is at least one technician or supervisor, so that our core code is executed
	 * 2. the site collection for each technician and supervisor equals the set of all sites in the database
	 * 
	 */
	/*public void testVerifyMigration()
	{	
		List userList = null;
		List siteList = null;

		try
		{
			userList = search(User.class);
			siteList = getRepositorySites();
		}
		catch (BizLogicException e)
		{
			fail("failed to retrieve all the users and sites");
		}
		
		int techsOrSups = 0;
		for (Object o : userList)
		{
			User user = (User) o;
			if (user.getRoleId().equals("2") || user.getRoleId().equals("3"))
			{
				techsOrSups++;
				
				if(!CompareSiteLists(siteList, user.getSiteCollection()))
				{
					fail();
				}
			}
		}
		
		if(techsOrSups==0)
		{
			fail();
		}
	}*/

	private List<Site> getRepositorySites() 
	{
		List<Site> siteList = new ArrayList<Site>();
		List<Site> repositorySiteList = new ArrayList<Site>();
		
		try 
		{
			siteList = search(Site.class);
			
			for(Site site : siteList)
			{
				if(Constants.REPOSITORY.equals(site.getType()))
				{
					repositorySiteList.add(site);
				}
			}
		} 
		catch (BizLogicException e) 
		{
			e.printStackTrace();
		}
		return repositorySiteList;
	}

	/**
	 * Due to absence of equals() method in the domain class hierarchy
	 * 	we have to implement the comparison, based on site ids
	 * 
	 * @return true if they are equal
	 */
	/*private boolean CompareSiteLists(List<Site> siteList, Collection<Site> siteCollection)
	{
		Set<Long> siteSet = new HashSet<Long>();
		
		for(Site site : siteList)
		{
			siteSet.add(site.getId());
		}
		
		if(siteCollection.size()!=siteSet.size())
		{
			return false;
		}
		
		for(Object o : siteCollection)
		{
			Long id = ((Site) o).getId();
			if(!siteSet.contains(id))
			{
				return false;
			}
		}
		
		return true;
	}*/
}
