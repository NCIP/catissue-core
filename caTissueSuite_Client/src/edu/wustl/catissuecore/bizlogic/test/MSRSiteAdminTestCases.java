/**
 * 
 */

package edu.wustl.catissuecore.bizlogic.test;

import edu.wustl.catissuecore.domain.Site;
import edu.wustl.catissuecore.domain.StorageContainer;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.common.security.exceptions.UserNotAuthorizedException;
import gov.nih.nci.system.applicationservice.ApplicationException;

/**
 * @author juberahamad_patel
 *
 */
public class MSRSiteAdminTestCases extends MSRBaseTestCase
{

	public void testAddSC() throws ApplicationException
	{
		Site site = createNewSite();
		User user = createNewSiteAdmin(site);
		loginAs(user.getLoginName());
		StorageContainer sc = createNewSC(site);

	}
	

	public void testAddSCNagative()
	{
		Site site1 = createNewSite();
		Site site2 = createNewSite();
		
		User user = createNewSiteAdmin(site1);
		loginAs(user.getLoginName());
		
		try
		{
			// this should work
			StorageContainer sc = createNewSC(site1);
		}
		catch(Exception e)
		{
			fail();
		}
		
		try
		{
			//this must fail
			StorageContainer sc = createNewSC(site2);
		}
		catch(Exception e)
		{
			if(confirmUserNotAuthorizedException(e))
			{
				return;
			}
			
			fail("Negative test for testAddSC failed since exception was thrown but not UserNotAuthorizedException");
		}

	}


	

}
