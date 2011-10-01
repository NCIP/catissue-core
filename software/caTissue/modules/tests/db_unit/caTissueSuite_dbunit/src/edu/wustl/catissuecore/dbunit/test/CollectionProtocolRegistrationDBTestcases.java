package edu.wustl.catissuecore.dbunit.test;
import edu.wustl.catissuecore.domain.Address;
import edu.wustl.catissuecore.domain.CancerResearchGroup;
import edu.wustl.catissuecore.domain.Department;
import edu.wustl.catissuecore.domain.Institution;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.domain.CollectionProtocolRegistration;
import edu.wustl.common.util.global.Constants;


public class CollectionProtocolRegistrationDBTestcases extends
		DefaultCatissueDBUnitTestCase {
		
	
	public void testCollectionProtocolRegistrationCreate()
		{
			//this.i
			try
			{
				System.out.println("---start");
				insertObjectsOf(CollectionProtocolRegistration.class);
				System.out.println("---after insertobjectsof..");
				assertTrue("CollectionProtocolRegistrationCreated",true);
			}catch(Exception e)
			{
				e.printStackTrace();
				fail("failed to insert CollectionProtocolRegistration....create");
				
			}
		}
	public void testCollectionProtocolRegistrationUpdate()
		{
			try
			{
		
				UpdateObjects(CollectionProtocolRegistration.class);
				assertTrue("CollectionProtocolRegistrationUpdated", true);
			}catch(Exception e)
			{
				e.printStackTrace();
				fail("failed to insert CollectionProtocolRegistration..update");
			}
		}


	}
	
	
