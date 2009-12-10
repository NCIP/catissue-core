package edu.wustl.catissuecore.testcase.bizlogic;

import edu.wustl.catissuecore.domain.Biohazard;
import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.catissuecore.domain.Department;
import edu.wustl.catissuecore.domain.DistributionProtocol;
import edu.wustl.catissuecore.domain.Institution;
import edu.wustl.catissuecore.domain.StorageContainer;
import edu.wustl.catissuecore.domain.StorageType;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.common.beans.SessionDataBean;
/**
 * 
 * @author vipin_bansal
 *
 */
public class MSRSuperAdminTestCases extends MSRBaseTestCase {
	
	public void testAddUserBySuperAdmin()
	{
		User user = BaseTestCaseUtility.initUser();
		SessionDataBean bean = (SessionDataBean)getSession().getAttribute("sessionData");
		try
		{
			user = (User)appService.createObject(user,bean);
		}
		catch (Exception e)
		{
			fail();
		}

	}
	public void testAddInstituteBySuperAdmin()
	{
		Institution institute = BaseTestCaseUtility.initInstitution();
		SessionDataBean bean = (SessionDataBean)getSession().getAttribute("sessionData");
		try
		{
			institute = (Institution)appService.createObject(institute,bean);
		}
		catch (Exception e)
		{
			System.out.println(e.getMessage()); 
			e.printStackTrace();
			fail();
			System.exit(1);
		}

	}
	public void testAddDepartmentBySuperAdmin()
	{
		Department department = BaseTestCaseUtility.initDepartment();
		SessionDataBean bean = (SessionDataBean)getSession().getAttribute("sessionData");
		try
		{
			department = (Department)appService.createObject(department,bean);
		}
		catch (Exception e)
		{
			System.out.println(e.getMessage()); 
			e.printStackTrace();
			fail();
			System.exit(1);
		}

	}
	public void testAddSCBySuperAdmin()
	{
		StorageContainer  storageContainer = BaseTestCaseUtility.initStorageContainer();
		SessionDataBean bean = (SessionDataBean)getSession().getAttribute("sessionData");
		try
		{
			storageContainer = (StorageContainer)appService.createObject(storageContainer,bean);
		}
		catch (Exception e)
		{
			System.out.println(e.getMessage()); 
			e.printStackTrace();
			fail();
			System.exit(1);
		}

	}
	
	public void testAddStorageTypeBySuperAdmin()
	{
		StorageType storageType = BaseTestCaseUtility.initStorageType();
		SessionDataBean bean = (SessionDataBean)getSession().getAttribute("sessionData");
		try
		{
			storageType = (StorageType)appService.createObject(storageType,bean);
		}
		catch (Exception e)
		{
			System.out.println(e.getMessage()); 
			e.printStackTrace();
			fail();
			System.exit(1);
		}

	}
	public void testAddSpecimenArrayTypeBySuperAdmin()
	{
		StorageType storageType = BaseTestCaseUtility.initStorageType();
		SessionDataBean bean = (SessionDataBean)getSession().getAttribute("sessionData");
		try
		{
			storageType = (StorageType)appService.createObject(storageType,bean);
		}
		catch (Exception e)
		{
			System.out.println(e.getMessage()); 
			e.printStackTrace();
			fail();
			System.exit(1);
		}

	}
	public void testAddBiohazardBySuperAdmin()
	{
		Biohazard biohazard = BaseTestCaseUtility.initBioHazard();
		SessionDataBean bean = (SessionDataBean)getSession().getAttribute("sessionData");
		try
		{
			biohazard = (Biohazard)appService.createObject(biohazard,bean);
		}
		catch (Exception e)
		{
			System.out.println(e.getMessage()); 
			e.printStackTrace();
			fail();
			System.exit(1);
		}

	}
	public void testAddCPBySuperAdmin()
	{
		CollectionProtocol collectionProtocol = BaseTestCaseUtility.initCollectionProtocol();
		SessionDataBean bean = (SessionDataBean)getSession().getAttribute("sessionData");
		try
		{
			collectionProtocol = (CollectionProtocol)appService.createObject(collectionProtocol,bean);
		}
		catch (Exception e)
		{
			System.out.println(e.getMessage()); 
			e.printStackTrace();
			fail();
			System.exit(1);
		}

	}
	public void testAddDPBySuperAdmin()
	{
		 DistributionProtocol distributionProtocol = BaseTestCaseUtility.initDistributionProtocol();
		 SessionDataBean bean = (SessionDataBean)getSession().getAttribute("sessionData");
		try
		{
			distributionProtocol = (DistributionProtocol)appService.createObject(distributionProtocol,bean);
		}
		catch (Exception e)
		{
			System.out.println(e.getMessage()); 
			e.printStackTrace();
			fail();
			System.exit(1);
		}

	}

}
