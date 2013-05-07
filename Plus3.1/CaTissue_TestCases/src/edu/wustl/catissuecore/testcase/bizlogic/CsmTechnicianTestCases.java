package edu.wustl.catissuecore.testcase.bizlogic;

import java.util.Collection;
import java.util.HashSet;

import edu.wustl.catissuecore.domain.Capacity;
import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.catissuecore.domain.ContainerPosition;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.SpecimenArray;
import edu.wustl.catissuecore.domain.SpecimenArrayContent;
import edu.wustl.catissuecore.domain.SpecimenArrayType;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.catissuecore.domain.SpecimenPosition;
import edu.wustl.catissuecore.domain.StorageContainer;
import edu.wustl.catissuecore.domain.TissueSpecimen;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.testcase.util.UniqueKeyGeneratorUtil;
import edu.wustl.common.util.logger.Logger;

public class CsmTechnicianTestCases extends CaTissueBaseTestCase{
/*	 static ApplicationService appService = null;
	  ClientSession cs = null;
	  public void setUp(){
		// Logger.configure("");
		appService = ApplicationServiceProvider.getApplicationService();
		cs = ClientSession.getInstance();
		try
		{
			cs.startSession("technician@admin.com", "Login123");
			System.out.println("Inside Csm technician setup method ");
		}

		catch (Exception ex)
		{
			System.out.println(ex.getMessage());
			ex.printStackTrace();
			fail();
			System.exit(1);
		}
	}
	  public void tearDown(){
		  System.out.println("Inside Csm supervisor teardown method");
		  cs.terminateSession();
	  }
*/
	  public void testUseStorageContainerWithAllowUsePrevilegeForTechnician(){
			try {
			   // CollectionProtocol cp= (CollectionProtocol) TestCaseUtility.getObjectMap(CollectionProtocol.class);
				CollectionProtocol cp= new CollectionProtocol();
			    cp.setId(new Long(TestCaseUtility.CP_WITH_ALLOW_READ_PRIV));
			    System.out.println("reached CP");
			    SpecimenCollectionGroup scg =  (SpecimenCollectionGroup) TestCaseUtility.getObjectMap(SpecimenCollectionGroup.class);
			    TissueSpecimen specimenObj = (TissueSpecimen) BaseTestCaseUtility.initTissueSpecimen();
			    StorageContainer sc = new StorageContainer();
			    sc.setId(new Long(TestCaseUtility.STORAGECONTAINER_WITH_ALLOWUSE_PRIV));
				specimenObj.setSpecimenCollectionGroup(scg);
				SpecimenPosition specPos = new SpecimenPosition();
				specPos.setStorageContainer(sc);
				specPos.setSpecimen(specimenObj);
				specPos.setPositionDimensionOne(new Integer(2));
				specPos.setPositionDimensionTwo(new Integer(2));
				specimenObj.setSpecimenPosition(specPos);
				Logger.out.info("Inserting domain object------->"+specimenObj);
				specimenObj =  (TissueSpecimen) appService.createObject(specimenObj);
				System.out.println("Specimen name"+specimenObj.getLabel());
				TestCaseUtility.setObjectMap(specimenObj, TissueSpecimen.class);
				assertTrue(" Domain Object is successfully added ---->    Name:: " + specimenObj.getLabel(), true);
			}
			catch(Exception e)
			{
				System.out.println("Exception thrown");
				System.out.println(e);
				Logger.out.error(e.getMessage(),e);
				e.printStackTrace();
				assertFalse(e.getMessage(), true);
			}

		}
	   public void testUseStorageContainerWithDisallowUsePrivilegeForTechnician(){
			try {
			   // CollectionProtocol cp= (CollectionProtocol) TestCaseUtility.getObjectMap(CollectionProtocol.class);
				CollectionProtocol cp= new CollectionProtocol();
			    cp.setId(new Long(TestCaseUtility.CP_WITH_ALLOW_READ_PRIV));
			    System.out.println("reached CP");
			    SpecimenCollectionGroup scg =  (SpecimenCollectionGroup) TestCaseUtility.getObjectMap(SpecimenCollectionGroup.class);
			    TissueSpecimen specimenObj = (TissueSpecimen) BaseTestCaseUtility.initTissueSpecimen();
			    StorageContainer sc = new StorageContainer();
			    sc.setId(new Long(TestCaseUtility.STORAGECONTAINER_WITH_DISALLOWUSE_PRIV));
				specimenObj.setSpecimenCollectionGroup(scg);
				SpecimenPosition specPos = new SpecimenPosition();
				specPos.setStorageContainer(sc);
				specPos.setSpecimen(specimenObj);
				specPos.setPositionDimensionOne(new Integer(1));
				specPos.setPositionDimensionTwo(new Integer(3));
				specimenObj.setSpecimenPosition(specPos);
				Logger.out.info("Inserting domain object------->"+specimenObj);
				System.out.println("Before Creating Tissue Specimen");
				specimenObj =  (TissueSpecimen) appService.createObject(specimenObj);
				TestCaseUtility.setObjectMap(specimenObj, TissueSpecimen.class);
				assertTrue(" Domain Object is successfully added ---->    Name:: " + specimenObj.getLabel(), true);
			}
			catch(Exception e)
			{
				System.out.println("Exception thrown");
				System.out.println(e);
				Logger.out.error(e.getMessage(),e);
				e.printStackTrace();
				assertTrue("Failed to create Domain Object", true);
			}

		}

	   public void testAddSpecimenArrayInStorContainerWithAllowUsePrivilegeForTechnician()
		{
			try
			{
				SpecimenArrayType specimenArrayType = (SpecimenArrayType) TestCaseUtility.getObjectMap(SpecimenArrayType.class);

				SpecimenArray specimenArray =  new SpecimenArray();
				specimenArray.setSpecimenArrayType(specimenArrayType);

				specimenArray.setBarcode("bar" + UniqueKeyGeneratorUtil.getUniqueKey());
				specimenArray.setName("sa" + UniqueKeyGeneratorUtil.getUniqueKey());

				User createdBy = new User();
				createdBy.setId(new Long(1));
				specimenArray.setCreatedBy(createdBy);

				Capacity capacity = new Capacity();
				capacity.setOneDimensionCapacity(4);
				capacity.setTwoDimensionCapacity(4);
				specimenArray.setCapacity(capacity);

				specimenArray.setComment("");
				StorageContainer storageContainer = new StorageContainer();
				storageContainer.setId(new Long(TestCaseUtility.SPECIMENARRAYCONTAINER_WITH_ALLOWUSE_PRIV));

	//			specimenArray.setStorageContainer(storageContainer);
				ContainerPosition containerPosition = new ContainerPosition();
				containerPosition.setPositionDimensionOne(new Integer(3));
				containerPosition.setPositionDimensionTwo(new Integer(1));
				containerPosition.setParentContainer(storageContainer);
				containerPosition.setOccupiedContainer(specimenArray);
				specimenArray.setLocatedAtPosition(containerPosition);

				Collection specimenArrayContentCollection = new HashSet();
				SpecimenArrayContent specimenArrayContent = new SpecimenArrayContent();

				Specimen specimen = (Specimen) TestCaseUtility.getObjectMap(TissueSpecimen.class);

				specimenArrayContent.setSpecimen(specimen);
				specimenArrayContent.setPositionDimensionOne(new Integer(1));
				specimenArrayContent.setPositionDimensionTwo(new Integer(1));

//				Quantity quantity = new Quantity();
//				quantity.setValue(new Double(1));
//				specimenArrayContent.setInitialQuantity(quantity);

				specimenArrayContentCollection.add(specimenArrayContent);
				specimenArray.setSpecimenArrayContentCollection(specimenArrayContentCollection);

				specimenArray = (SpecimenArray) appService.createObject(specimenArray);

			}
			catch(Exception e)
			{
				Logger.out.error(e.getMessage(),e);
				e.printStackTrace();
				assertFalse(e.getMessage(), true);
			}
		}


	 /*  public void testAddAliquotOfTissueSpecimen()
	    {
	        try
	          {
        		Specimen specimenParentObj =(Specimen)TestCaseUtility.getObjectMap(TissueSpecimen.class);
                Specimen aliquotSpecimenObj = (Specimen) BaseTestCaseUtility.initAliquotOfTissueSpecimen(specimenParentObj);

                StorageContainer sc = new StorageContainer();
			    sc.setId(new Long(1));

			    aliquotSpecimenObj.setStorageContainer(sc);
			    aliquotSpecimenObj.setPositionDimensionOne(new Integer(2));
			    aliquotSpecimenObj.setPositionDimensionTwo(new Integer(1));
                Logger.out.info("Inserting domain object------->"+aliquotSpecimenObj);
                aliquotSpecimenObj =  (Specimen) appService.createObject(aliquotSpecimenObj);


                Logger.out.info(" Domain Object is successfully added ---->    ID:: " + aliquotSpecimenObj.getId().toString());
                Logger.out.info(" Domain Object is successfully added ---->    Name:: " + aliquotSpecimenObj.getLabel());
	          }

	          catch(Exception e)
	          {
        	    assertFalse("Failed to create Domain Object", true);
                Logger.out.error(e.getMessage(),e);
                e.printStackTrace();
	          }

	    }*/
}
