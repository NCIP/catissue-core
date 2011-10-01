package edu.wustl.catissuecore.testcase.bizlogic;

import edu.wustl.catissuecore.domain.Biohazard;
import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.catissuecore.domain.Department;
import edu.wustl.catissuecore.domain.DistributionProtocol;
import edu.wustl.catissuecore.domain.Institution;
import edu.wustl.catissuecore.domain.StorageContainer;
import edu.wustl.catissuecore.domain.StorageType;
import edu.wustl.catissuecore.domain.User;
/**
 * 
 * @author vipin_bansal
 *
 */
public class MSRSuperAdminTestCases extends MSRBaseTestCase {
	
	public void testAddUserBySuperAdmin()
	{
		User user = BaseTestCaseUtility.initUser();
		try
		{
			user = (User)appService.createObject(user);
		}
		catch (Exception e)
		{
			fail();
		}

	}
	public void testAddInstituteBySuperAdmin()
	{
		Institution institute = BaseTestCaseUtility.initInstitution();
		try
		{
			institute = (Institution)appService.createObject(institute);
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
		try
		{
			department = (Department)appService.createObject(department);
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
		try
		{
			storageContainer = (StorageContainer)appService.createObject(storageContainer);
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
		try
		{
			storageType = (StorageType)appService.createObject(storageType);
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
		try
		{
			storageType = (StorageType)appService.createObject(storageType);
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
		try
		{
			biohazard = (Biohazard)appService.createObject(biohazard);
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
		try
		{
			collectionProtocol = (CollectionProtocol)appService.createObject(collectionProtocol);
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
		try
		{
			distributionProtocol = (DistributionProtocol)appService.createObject(distributionProtocol);
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
