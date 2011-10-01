package edu.wustl.catissuecore.testcase.bizlogic;

import java.text.ParseException;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import edu.wustl.catissuecore.domain.Capacity;
import edu.wustl.catissuecore.domain.CellSpecimen;
import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.catissuecore.domain.CollectionProtocolRegistration;
import edu.wustl.catissuecore.domain.ConsentTier;
import edu.wustl.catissuecore.domain.ConsentTierResponse;
import edu.wustl.catissuecore.domain.Container;
import edu.wustl.catissuecore.domain.ContainerPosition;
import edu.wustl.catissuecore.domain.DisposalEventParameters;
import edu.wustl.catissuecore.domain.FluidSpecimen;
import edu.wustl.catissuecore.domain.MolecularSpecimen;
import edu.wustl.catissuecore.domain.Participant;
import edu.wustl.catissuecore.domain.Site;
import edu.wustl.catissuecore.domain.SpecimenArray;
import edu.wustl.catissuecore.domain.SpecimenArrayType;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.catissuecore.domain.SpecimenPosition;
import edu.wustl.catissuecore.domain.StorageContainer;
import edu.wustl.catissuecore.domain.StorageType;
import edu.wustl.catissuecore.domain.TissueSpecimen;
import edu.wustl.catissuecore.domain.TransferEventParameters;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.testcase.CaTissueSuiteBaseTest;
import edu.wustl.catissuecore.testcase.util.UniqueKeyGeneratorUtil;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.util.Utility;
import edu.wustl.common.util.global.Status;
import edu.wustl.common.util.logger.Logger;


public class StorageContainerBizTestCases extends CaTissueSuiteBaseTest{
	AbstractDomainObject domainObject = null;
	
	public void testAddStorageContainer()
	{
		try{
			StorageContainer storageContainer= BaseTestCaseUtility.initStorageContainer();			
			System.out.println(storageContainer);
			storageContainer = (StorageContainer) appService.createObject(storageContainer); 
			BizTestCaseUtility.setObjectMap(storageContainer, StorageContainer.class);
			System.out.println("Object created successfully");
			assertTrue("Object added successfully", true);
		 }
		 catch(Exception e){
			 e.printStackTrace();
			 assertFalse("could not add object", true);
		 }
	}
	
	public void testAddStorageContaierWithDuplicateName()
	{

		try{
			StorageContainer storageContainer= BaseTestCaseUtility.initStorageContainer();			
			storageContainer.setName(((StorageContainer)BizTestCaseUtility.getObjectMap(StorageContainer.class)).getName());
			storageContainer = (StorageContainer) appService.createObject(storageContainer); 
			 assertTrue("Add container with new Label ass label generator is active", true);
			
		 }
		 catch(Exception e){
			 e.printStackTrace();
			 System.out.println("Can not create Container as container with same name already exist");
			 assertFalse(e.getMessage(), true);
			
		 }
	}
	
	public void testUpdatedStorageContaierWithDuplicateName()
	{

		try{
			StorageContainer storageContainer= BaseTestCaseUtility.initStorageContainer();		
			storageContainer = (StorageContainer) appService.createObject(storageContainer); 
			storageContainer.setName(((StorageContainer)BizTestCaseUtility.getObjectMap(StorageContainer.class)).getName());
			storageContainer=(StorageContainer) appService.updateObject(storageContainer);
			assertFalse("could not add object", true);
			
		 }
		 catch(Exception e){
			 e.printStackTrace();
			 System.out.println("Can not create Container as container with same name already exist");
			 
			 assertTrue("Add container with new Label ass label generator is active", true);
		 }
	}
	/**
	 * Test case to edit the capacity of the Storage Container
	 */
	public void testUpdateCapacityOFStorageContainer()
	{
		StorageContainer storageContainer = (StorageContainer)BizTestCaseUtility.getObjectMap(StorageContainer.class);
		System.out.println("Before Update");
	    try 
		{
	    	Capacity capacity = storageContainer.getCapacity();
			capacity.setOneDimensionCapacity(new Integer(4));
			capacity.setTwoDimensionCapacity(new Integer(4));
			storageContainer.setCapacity(capacity);
	    	System.out.println("After Update");
	    	StorageContainer updatedStorageContainer = (StorageContainer) appService.updateObject(storageContainer);
	       	assertTrue("Domain object successfully updated ---->"+updatedStorageContainer, true);
	    } 
	    catch (Exception e) 
	    {
	 		e.printStackTrace();
	 		System.out
					.println("StorageContainerTestCases.testUpdateCapacityOFStorageContainer()"+e.getMessage() );
	 		assertFalse("failed to update Object:"+e.getMessage(), true);
	    }
	}
	
	/**
	 * Test case to edit the Name and Temperature  capacity of the Storage Container
	 */
	public void testUpdateNameAndTempOFStorageContainer()
	{
		StorageContainer storageContainer = (StorageContainer)BizTestCaseUtility.getObjectMap(StorageContainer.class);
		System.out.println("Before Update");
	    try 
		{
	    	storageContainer.setTempratureInCentigrade(new Double(-70));
	    	storageContainer.setName("UpdatedSC" + UniqueKeyGeneratorUtil.getUniqueKey());
	    	System.out.println("After Update");
	    	StorageContainer updatedStorageContainer = (StorageContainer) appService.updateObject(storageContainer);
	       	assertTrue("Domain object successfully updated ---->"+updatedStorageContainer, true);
	    } 
	    catch (Exception e) 
	    {
	 		e.printStackTrace();
	 		System.out
					.println("StorageContainerTestCases.testUpdateNameAndTempOFStorageContainer()"+e.getMessage() );
	 		assertFalse("failed to update Object:"+e.getMessage(), true);
	    }
	}
	/**
	 * test case to add parent container 
	 *
	 */
	public void testAddParentStorageContainer()
	{
		try{
			StorageContainer storageContainer= BaseTestCaseUtility.initStorageContainer();
			/**
			 * Set all collection protocol
			 */
			Collection collectionProtocolCollection = new HashSet();
			storageContainer.setCollectionProtocolCollection(collectionProtocolCollection);
			/**
			 * Set hiolds all Storage Type
			 */
			Collection holdsStorageTypeCollection = new HashSet();
			StorageType sttype = new StorageType();
			sttype.setId(1L);
			holdsStorageTypeCollection.add(sttype);
			storageContainer.setHoldsStorageTypeCollection(holdsStorageTypeCollection);
			
			System.out.println(storageContainer);
			
			
			storageContainer = (StorageContainer) appService.createObject(storageContainer); 
			BizTestCaseUtility.setNameObjectMap("ParentContainer", storageContainer);
			System.out.println("Object created successfully");
			assertTrue("Object added successfully", true);
		 }
		 catch(Exception e){
			 e.printStackTrace();
			 System.out
					.println("StorageContainerTestCases.testAddParentStorageContainer()");
			 System.out.println(e.getMessage());
			 assertFalse(e.getMessage(), true);
		 }
	}
	/**
	 * Add child containers rin above container
	 *
	 */
	public void testAddChildStorageContainer()
	{
		try
		{
			StorageContainer parent = (StorageContainer)BizTestCaseUtility.getNameObjectMap("ParentContainer");
			
			StorageContainer storageContainer1= BaseTestCaseUtility.initStorageContainer();
			Collection collectionProtocolCollection = new HashSet();
			storageContainer1.setCollectionProtocolCollection(collectionProtocolCollection);
			ContainerPosition containerPosition1 = new ContainerPosition();
			containerPosition1.setPositionDimensionOne(1);
			containerPosition1.setPositionDimensionTwo(1);
			containerPosition1.setParentContainer(parent);
			storageContainer1.setLocatedAtPosition(containerPosition1);
			System.out.println(storageContainer1);
			storageContainer1 = (StorageContainer) appService.createObject(storageContainer1); 
			System.out.println("ChildStorageContainer created successfully");
			
			StorageContainer storageContainer2= BaseTestCaseUtility.initStorageContainer();
			Collection collectionProtocolCollection1 = new HashSet();
			storageContainer2.setCollectionProtocolCollection(collectionProtocolCollection1);
			ContainerPosition containerPosition2 = new ContainerPosition();
			containerPosition2.setPositionDimensionOne(1);
			containerPosition2.setPositionDimensionTwo(2);
			containerPosition2.setParentContainer(parent);
			storageContainer2.setLocatedAtPosition(containerPosition2);
			System.out.println(storageContainer2);
			storageContainer2 = (StorageContainer) appService.createObject(storageContainer2); 
			System.out.println("ChildStorageContainer created successfully");
			
			assertTrue("Object added successfully", true);
		 }
		 catch(Exception e){
			 e.printStackTrace();
			 System.out
					.println("StorageContainerTestCases.testAddChildStorageContainer()");
			 System.out.println(e.getMessage());
			 assertFalse(e.getMessage(), true);
		 }
	}
	/**
	 * negative Test case to ad container at occupied position 
	 *
	 */
	public void testAddChildStorageContainerOnOccupiedPosition()
	{
		try
		{
			StorageContainer parent = (StorageContainer)BizTestCaseUtility.getNameObjectMap("ParentContainer");
			
			StorageContainer storageContainer1= BaseTestCaseUtility.initStorageContainer();
			ContainerPosition containerPosition1 = new ContainerPosition();
			containerPosition1.setPositionDimensionOne(1);
			containerPosition1.setPositionDimensionTwo(1);
			containerPosition1.setParentContainer(parent);
			storageContainer1.setLocatedAtPosition(containerPosition1);
			System.out.println(storageContainer1);
			storageContainer1 = (StorageContainer) appService.createObject(storageContainer1); 
			System.out.println("Object created successfully");

			assertFalse("Object added successfully", true);
		 }
		 catch(Exception e){
			 e.printStackTrace();
			 System.out
					.println("StorageContainerTestCases.testAddChildStorageContainerOnOccupiedPosition()");
			 System.out.println(e.getMessage());
			 assertTrue("Negative test case could not add object: "+e.getMessage(), true);
		 }
	}
	/**
	 * Search Container which is located at given position of parent container
	 *
	 */
	public void testSearchStorageContainerLocatedAtPosition()
	{
//			try
//			{
//				SessionDataBean bean = (SessionDataBean)getSession().getAttribute("sessionData");
//				StorageContainer storageContainer = (StorageContainer)TestCaseUtility.getNameObjectMap("ParentContainer");
//
//				
//				StorageContainer parent = new StorageContainer();
//				parent.setId(storageContainer.getId());
//				
//				ContainerPosition containerPosition = new ContainerPosition();
//				containerPosition.setPositionDimensionOne(1);
//				containerPosition.setPositionDimensionTwo(2);
//				containerPosition.setParentContainer(parent);
//
//				List result = appService.search(Container.class, containerPosition);
//				if(result.size()>1||result.size()<1)
//				{
//					assertFalse("Could not find Storage Container Object", true);
//				}
//				assertTrue("Storage Container successfully found. Size:" +result.size(), true);
//			}
//			catch(Exception e)
//			{
//				Logger.out.error(e.getMessage(),e);
//				System.out
//						.println("StorageContainerTestCases.testSearchStorageContainerLocatedAtPosition()");
//				System.out.println(e.getMessage());
//				e.printStackTrace();
//				assertFalse("Could not find Storage Container Object", true);
//			}
	}

	public void testSearchStorageContainer()
	{
		SessionDataBean bean = (SessionDataBean)getSession().getAttribute("sessionData");
		StorageContainer storageContainer = new StorageContainer();
    	Logger.out.info(" searching domain object");
    	storageContainer.setId(new Long(1));
    	String query = "from edu.wustl.catissuecore.domain.StorageContainer as storageContainer where "
				+ "storageContainer.id= 1";	
   
         try {
        	 List resultList = appService.search(query);
        	 for (Iterator resultsIterator = resultList.iterator(); resultsIterator.hasNext();) {
        		 StorageContainer returnedStorageContainer = (StorageContainer) resultsIterator.next();
        		 Logger.out.info(" Domain Object is successfully Found ---->  :: " 
        				 + returnedStorageContainer.getName());
        		// System.out.println(" Domain Object is successfully Found ---->  :: " + returnedDepartment.getName());
             }
          } 
          catch (Exception e) {
           	Logger.out.error(e.getMessage(),e);
           	e.printStackTrace();
           	assertFalse(e.getMessage(), true);
	 		
          }
	}
	
	public void testUpdateContainerWithCaseSensitiveBarcode()
	{
		StorageContainer storageContainer1 =  BaseTestCaseUtility.initStorageContainer();
		System.out.println("Before Update");
    	Logger.out.info("updating domain object------->"+storageContainer1);
	    try 
		{
	    	String  uniqueKey=UniqueKeyGeneratorUtil.getUniqueKey();
	    	storageContainer1 = (StorageContainer) appService.createObject(storageContainer1);
	    	BaseTestCaseUtility.updateStorageContainer(storageContainer1);
	    	storageContainer1.setBarcode("CONTAINER BAORCODE"+uniqueKey);
	    	System.out.println("After Update");
	    	StorageContainer updatedStorageContainer1 = (StorageContainer) appService.updateObject(storageContainer1);
	       	Logger.out.info("Domain object successfully updated ---->"+updatedStorageContainer1);
	
	       	StorageContainer storageContainer2 =  BaseTestCaseUtility.initStorageContainer();
	       	System.out.println("Before Update");
	       	Logger.out.info("updating domain object------->"+storageContainer2);
	   
	    	storageContainer2 = (StorageContainer) appService.createObject(storageContainer2);
	    	BaseTestCaseUtility.updateStorageContainer(storageContainer2);
	    	storageContainer1.setBarcode("container barcode"+uniqueKey);
	    	System.out.println("After Update");
	    	StorageContainer updatedStorageContainer2 = (StorageContainer) appService.updateObject(storageContainer2);
	       	Logger.out.info("Domain object successfully updated ---->"+updatedStorageContainer2);
	       	assertTrue("Domain object successfully updated ---->"+updatedStorageContainer2, true);
		}
	     catch (Exception e) {
	       	Logger.out.error(e.getMessage(),e);
	 		e.printStackTrace();
	 		System.out
					.println("StorageContainerTestCases.testUpdateStorageContainer()"+e.getMessage() );
	 		assertFalse(e.getMessage(), true);
	    }
	}
	
	    
		public void testSearchContainerWithCaseSensitiveBarcode()
		{
			StorageContainer storageContainer1 =  BaseTestCaseUtility.initStorageContainer();
			System.out.println("Before Update");
	    	Logger.out.info("updating domain object------->"+storageContainer1);
	    	String  uniqueKey=UniqueKeyGeneratorUtil.getUniqueKey();
		    try 
			{
		    	
		    	storageContainer1 = (StorageContainer) appService.createObject(storageContainer1);
		    	BaseTestCaseUtility.updateStorageContainer(storageContainer1);
		    	storageContainer1.setBarcode("CONTAINER BAORCODE"+uniqueKey);
		    	System.out.println("After Update");
		    	StorageContainer updatedStorageContainer1 = (StorageContainer) appService.updateObject(storageContainer1);
		       	Logger.out.info("Domain object successfully updated ---->"+updatedStorageContainer1);
		
		       	StorageContainer storageContainer2 =  BaseTestCaseUtility.initStorageContainer();
		       	System.out.println("Before Update");
		       	Logger.out.info("updating domain object------->"+storageContainer2);
		   
		    	storageContainer2 = (StorageContainer) appService.createObject(storageContainer2);
		    	BaseTestCaseUtility.updateStorageContainer(storageContainer2);
		    	storageContainer1.setBarcode("container barcode"+uniqueKey);
		    	System.out.println("After Update");
		    	StorageContainer updatedStorageContainer2 = (StorageContainer) appService.updateObject(storageContainer2);
		       	Logger.out.info("Domain object successfully updated ---->"+updatedStorageContainer2);
		       	assertTrue("Domain object successfully updated ---->"+updatedStorageContainer2, true);
			}
		     catch (Exception e) {
		       	Logger.out.error(e.getMessage(),e);
		 		e.printStackTrace();
		 		System.out
						.println("StorageContainerTestCases.testUpdateStorageContainer()"+e.getMessage() );
		 		assertFalse(e.getMessage(), true);
		    }
		     
			StorageContainer storageContainer = new StorageContainer();
	    	Logger.out.info(" searching domain object");
	    	storageContainer.setBarcode("CONTAINER BAORCODE"+uniqueKey);
	   
	    	String query = "from edu.wustl.catissuecore.domain.StorageContainer as storageContainer where "
 				+ "storageContainer.barcode= 'CONTAINER BAORCODE"+uniqueKey+"'" ;	
	         try {
	        	 List resultList = appService.search(query);
	        	if(resultList.size()==1)
	        	{
	        		assertTrue("Storage Container successfully found. Size:" +resultList.size(), true);
	        	}
	        	else{
	        		assertFalse("Does not find Domain Object", true);
	        	}
	          } 
	          catch (Exception e) {
	           	Logger.out.error(e.getMessage(),e);
	           	e.printStackTrace();
	           	assertFalse(e.getMessage(), true);
		 		
	          }
		}
	public void testUpdateStorageContainer()
	{
		StorageContainer storageContainer =  BaseTestCaseUtility.initStorageContainer();
		System.out.println("Before Update");
    	Logger.out.info("updating domain object------->"+storageContainer);
	    try 
		{
	    	storageContainer = (StorageContainer) appService.createObject(storageContainer);
	    	BaseTestCaseUtility.updateStorageContainer(storageContainer);
	    	System.out.println("After Update");
	    	StorageContainer updatedStorageContainer = (StorageContainer) appService.updateObject(storageContainer);
	       	Logger.out.info("Domain object successfully updated ---->"+updatedStorageContainer);
	       	assertTrue("Domain object successfully updated ---->"+updatedStorageContainer, true);
	    } 
	    catch (Exception e) {
	       	Logger.out.error(e.getMessage(),e);
	 		e.printStackTrace();
	 		System.out
					.println("StorageContainerTestCases.testUpdateStorageContainer()"+e.getMessage() );
	 		assertFalse(e.getMessage(), true);
	    }
	}
	
	public void testUpdateStorageContainerWithParentChanged()
	{
		StorageContainer storageContainer =  BaseTestCaseUtility.initStorageContainer();
		System.out.println("Before Update");
    	Logger.out.info("updating domain object------->"+storageContainer);
	    try 
		{
	    	storageContainer = (StorageContainer) appService.createObject(storageContainer);
	    	StorageContainer cachedContainer = (StorageContainer) BizTestCaseUtility.getObjectMap(StorageContainer.class);
	    	Container parent = new StorageContainer(); 
	    	parent.setId(cachedContainer.getId());
//	    	storageContainer.setParentChanged(true);
	    	ContainerPosition cntPos = new ContainerPosition();
	    	cntPos.setParentContainer(parent);    
	    	System.out.println("After Update");
	    	StorageContainer updatedStorageContainer = (StorageContainer) appService.updateObject(storageContainer);
	       	Logger.out.info("Domain object successfully updated ---->"+updatedStorageContainer);
	       	assertTrue("Domain object successfully updated ---->"+updatedStorageContainer, true);
	    } 
	    catch (Exception e) {
	       	Logger.out.error(e.getMessage(),e);
	 		e.printStackTrace();
	 		System.out
					.println("StorageContainerTestCases.testUpdateStorageContainerWithParentChanged()"+e.getMessage());
	 		assertFalse(e.getMessage(), true);
	    }
	}
	
	public void testUpdateStorageContainerToClosedActivityStatus()
	{
		try{
			StorageContainer storageContainer= BaseTestCaseUtility.initStorageContainer();			
			System.out.println(storageContainer);
			storageContainer = (StorageContainer) appService.createObject(storageContainer); 
			System.out.println("Object created successfully");
			storageContainer.setActivityStatus("Closed");
			StorageContainer updatedStorageContainer = (StorageContainer) appService.updateObject(storageContainer);
			assertTrue("Object updated successfully", true);
		 }
		 catch(Exception e){
			 Logger.out.error(e.getMessage(),e);
			 e.printStackTrace();
			 System.out
					.println("StorageContainerTestCases.testUpdateStorageContainerToClosedActivityStatus()"+e.getMessage());
			 assertFalse(e.getMessage(), true);
		 }
	}
	
	public void testUpdateStorageContainerToDisabledActivityStatus()
	{
		try{
			StorageContainer storageContainer= BaseTestCaseUtility.initStorageContainer();			
			System.out.println(storageContainer);
			storageContainer = (StorageContainer) appService.createObject(storageContainer); 
			System.out.println("Object created successfully");
			storageContainer.setActivityStatus("Disabled");
			StorageContainer updatedStorageContainer = (StorageContainer) appService.updateObject(storageContainer);
			assertTrue("Object updated successfully", true);
		 }
		 catch(Exception e){
			 Logger.out.error(e.getMessage(),e);
			 System.out
					.println("StorageContainerTestCases.testUpdateStorageContainerToDisabledActivityStatus()"+e.getMessage());
			 e.printStackTrace();
			 assertFalse(e.getMessage(), true);
		 }
	}
	
	public void testAddStorageContainerToClosedSite()
	{
	try{
		Site site= BaseTestCaseUtility.initSite(); 	
		System.out.println(site);
		try{
			site = (Site) appService.createObject(site); 
		}catch(Exception e){
			Logger.out.error(e.getMessage(),e);	
			e.printStackTrace();	
			assertFalse(e.getMessage(), true);
		}
		
		site.setActivityStatus("Closed");
		
		try{
			site =(Site)appService.updateObject(site);
		 }catch(Exception e){
			Logger.out.error(e.getMessage(),e);	
			e.printStackTrace();	
			assertFalse(e.getMessage(), true);
		 }
		
		StorageContainer storageContainer= BaseTestCaseUtility.initStorageContainer(); 
		System.out.println(storageContainer);
		storageContainer.setSite(site);
		
		try{
			storageContainer = (StorageContainer) appService.createObject(storageContainer); 
			assertFalse("Storage Container successfully created", true);
	
		}catch(Exception e){
			Logger.out.error(e.getMessage(),e);	
			e.printStackTrace();	
			assertTrue("Could not add Storage Container to close site ", true);
		 }
			
	}catch(Exception e){
		Logger.out.error(e.getMessage(),e);	
		e.printStackTrace();	
		assertFalse(e.getMessage(), true);
	}
  }	
	public void testAddTissueSpecimenInStorageContainerWithClosedSite()
	{ 
		Site site = BaseTestCaseUtility.initSite();
		try{
			site = (Site) appService.createObject(site);
		}
		catch(Exception e){
			Logger.out.error(e.getMessage(),e);
			 e.printStackTrace();
			 assertFalse(e.getMessage(), true);
		}		
		
			
		CollectionProtocol cp = BaseTestCaseUtility.initCollectionProtocol();
		try{
			cp = (CollectionProtocol) appService.createObject(cp);
		}
		catch(Exception e){
			Logger.out.error(e.getMessage(),e);
           	e.printStackTrace();
           	assertFalse(e.getMessage(), true);
		}
		StorageContainer storageContainer= BaseTestCaseUtility.initStorageContainer();			
		storageContainer.setSite(site);
		Collection cpCollection = new HashSet();
		cpCollection.add(cp);
		storageContainer.setCollectionProtocolCollection(cpCollection);
		try{			
			storageContainer = (StorageContainer) appService.createObject(storageContainer); 			
		}catch(Exception e){
			 Logger.out.error(e.getMessage(),e);
			 e.printStackTrace();
			 assertFalse(e.getMessage(), true);
		}
		
		Participant participant = BaseTestCaseUtility.initParticipant();
		
		try{
			participant = (Participant) appService.createObject(participant);
		}
		catch(Exception e){
			Logger.out.error(e.getMessage(),e);
           	e.printStackTrace();
           	assertFalse(e.getMessage(), true);
		}
		System.out.println("Participant:"+participant.getFirstName());
		CollectionProtocolRegistration collectionProtocolRegistration = new CollectionProtocolRegistration();
		collectionProtocolRegistration.setCollectionProtocol(cp);
		collectionProtocolRegistration.setParticipant(participant);
		collectionProtocolRegistration.setProtocolParticipantIdentifier("");
		collectionProtocolRegistration.setActivityStatus("Active");
		try
		{
			collectionProtocolRegistration.setRegistrationDate(Utility.parseDate("08/15/1975",
					Utility.datePattern("08/15/1975")));
			collectionProtocolRegistration.setConsentSignatureDate(Utility.parseDate("11/23/2006",Utility.datePattern("11/23/2006")));
			
		}
		catch (ParseException e)
		{			
			e.printStackTrace();
			assertFalse(e.getMessage(), true);
		}
		collectionProtocolRegistration.setSignedConsentDocumentURL("F:/doc/consentDoc.doc");
		User user = (User)BizTestCaseUtility.getObjectMap(User.class);
		collectionProtocolRegistration.setConsentWitness(user);
		
		Collection consentTierResponseCollection = new HashSet();
		Collection consentTierCollection = cp.getConsentTierCollection();
		
		Iterator ConsentierItr = consentTierCollection.iterator();
		Iterator ConsentierResponseItr = consentTierResponseCollection.iterator();
		
		while(ConsentierItr.hasNext())
		{
			ConsentTier consentTier = (ConsentTier)ConsentierItr.next();
			ConsentTierResponse consentResponse = new ConsentTierResponse();
			consentResponse.setResponse("Yes");
			consentResponse.setConsentTier(consentTier);		
		}
	
		collectionProtocolRegistration.setConsentTierResponseCollection(consentTierResponseCollection);
	
		collectionProtocolRegistration.setConsentTierResponseCollection(consentTierResponseCollection);
		System.out.println("Creating CPR");
		try{
			collectionProtocolRegistration = (CollectionProtocolRegistration) appService.createObject(collectionProtocolRegistration);
		}
		catch(Exception e){
			Logger.out.error(e.getMessage(),e);
           	e.printStackTrace();
           	assertFalse(e.getMessage(), true);
		}
		
		SpecimenCollectionGroup scg = new SpecimenCollectionGroup();
		scg =(SpecimenCollectionGroup) BaseTestCaseUtility.createSCG(collectionProtocolRegistration);
		scg.setSpecimenCollectionSite(site);
		scg.setName("New SCG"+UniqueKeyGeneratorUtil.getUniqueKey());		    
		scg = (SpecimenCollectionGroup) BaseTestCaseUtility.setEventParameters(scg);
		System.out.println("Creating SCG");
		
		
		try{
			scg = (SpecimenCollectionGroup) appService.createObject(scg);
		}
		catch(Exception e){
			Logger.out.error(e.getMessage(),e);
           	e.printStackTrace();
           	assertFalse(e.getMessage(), true);
		}
		
		site.setActivityStatus("Closed");
		
		try{
			site = (Site) appService.updateObject(site);
		}
		catch(Exception e){
			Logger.out.error(e.getMessage(),e);
			 e.printStackTrace();
			 assertFalse(e.getMessage(), true);
		}
		
		TissueSpecimen ts =(TissueSpecimen) BaseTestCaseUtility.initTissueSpecimen();
		SpecimenPosition sp = new SpecimenPosition();
		sp.setStorageContainer(storageContainer);
		sp.setSpecimen(ts);
		sp.setPositionDimensionOne(new Integer(1));
		sp.setPositionDimensionTwo(new Integer(2));
		ts.setSpecimenPosition(sp);
		
		
		ts.setSpecimenCollectionGroup(scg);
		ts.setLabel("TisSpec"+UniqueKeyGeneratorUtil.getUniqueKey());
		System.out.println("Befor creating Tissue Specimen");
		
		try{
			ts = (TissueSpecimen) appService.createObject(ts);
			System.out.println("TissueSpec:"+ts.getLabel());
			assertFalse("Successfully created specimen", true);
		}
		catch(Exception e){
			assertTrue("Failed to add Specimen in container with closed site", true);
		}	
	
	
	}

	public void testAddSpecimenArrayType()
	{
		try
		{
			SpecimenArrayType specimenArrayType =  BaseTestCaseUtility.initSpecimenSpecimenArrayType();
	    	Logger.out.info("Inserting domain object------->"+specimenArrayType);
	    	specimenArrayType =  (SpecimenArrayType) appService.createObject(specimenArrayType);
	    	BizTestCaseUtility.setObjectMap(specimenArrayType, SpecimenArrayType.class);
			assertTrue("Domain Object is successfully added" , true);
			Logger.out.info(" SpecimenSpecimenArrayType is successfully added ---->    ID:: " + specimenArrayType.getId().toString());
		}
		catch(Exception e)
		{
			Logger.out.error(e.getMessage(),e);
			e.printStackTrace();
			fail("Failed to add Domain Object");
		}
	}
	
	 public void testSearchSpecimenArrayType()
	 {
    	try 
    	{
    	//	SpecimenArrayType cachedSpecimenArrayType = (SpecimenArrayType) TestCaseUtility.getObjectMap(SpecimenArrayType.class);
    		SpecimenArrayType specimenArrayType = new SpecimenArrayType();
    		specimenArrayType.setId(new Long(16));
    		String query = "from edu.wustl.catissuecore.domain.SpecimenArrayType as specimenArrayType where "
 				+ "specimenArrayType.id= 16";	
    		
	     	Logger.out.info(" searching domain object");		    	
	    	List resultList = appService.search(query);
        	for (Iterator resultsIterator = resultList.iterator(); resultsIterator.hasNext();) 
        	{
        		SpecimenArrayType returnedSpecimenArray = (SpecimenArrayType)resultsIterator.next();
        		assertTrue("Specimen Array Type is successfully Found" , true);
        		Logger.out.info(" Specimen Array type is successfully Found ---->  :: " + returnedSpecimenArray.getName());
            }
       } 
       catch (Exception e) 
       {
    	 Logger.out.error(e.getMessage(),e);
 		 e.printStackTrace();
 		 fail("Failed to search Domain Object");
       }
	}
	 public void testUpdateSpecimenArrayType()
	 {
		try 
		{
			SpecimenArrayType specimenArrayType =  BaseTestCaseUtility.initSpecimenSpecimenArrayType();
			specimenArrayType =  (SpecimenArrayType) appService.createObject(specimenArrayType);
			Logger.out.info("updating Specimen Array Type------->"+specimenArrayType);
			BaseTestCaseUtility.updateSpecimenSpecimenArrayType(specimenArrayType);
			SpecimenArrayType updateSpecimenSpecimenArrayType = (SpecimenArrayType) appService.updateObject(specimenArrayType);
			assertTrue("updateSpecimenSpecimenArrayType is successfully updated" , true);
			Logger.out.info("updateSpecimenSpecimenArrayType successfully updated ---->"+updateSpecimenSpecimenArrayType);
		} 
		catch (Exception e) 
		{
			Logger.out.error(e.getMessage(),e);
	 		e.printStackTrace();
	 		fail("Failed to update Specimen Collection Group");
		}
	}
	 public void testAddSpecimenArray()
		{
			try
			{
				SpecimenArray specimenArray =  BaseTestCaseUtility.initSpecimenArray();
		    	Logger.out.info("Inserting domain object------->"+specimenArray);
		    	specimenArray =  (SpecimenArray) appService.createObject(specimenArray);
		    	BizTestCaseUtility.setObjectMap(specimenArray, SpecimenArray.class);
				assertTrue("Domain Object is successfully added" , true);
				Logger.out.info(" Specimen Collection Group is successfully added ---->    ID:: " + specimenArray.getId().toString());
			}
			catch(Exception e)
			{
				Logger.out.error(e.getMessage(),e);
				System.out
						.println("StorageContainerTestCases.testAddSpecimenArray()");
				System.out.println(e.getMessage());
				e.printStackTrace();
				fail("Failed to add Domain Object");
			}
		}
	 
	 public void testSearchSpecimenArray()
	 {
    	try 
    	{
    		SpecimenArray cachedSpecimenArray = (SpecimenArray) BizTestCaseUtility.getObjectMap(SpecimenArray.class);
    		SpecimenArray specimenArray = new SpecimenArray();
    		specimenArray.setId(cachedSpecimenArray.getId());
	     	Logger.out.info(" searching domain object");
	     	String query = "from edu.wustl.catissuecore.domain.SpecimenArray as specimenArray where "
 				+ "specimenArray.id= "+cachedSpecimenArray.getId();	
	    	List resultList = appService.search(query);
        	for (Iterator resultsIterator = resultList.iterator(); resultsIterator.hasNext();) 
        	{
        		SpecimenArray returnedSpecimenArray = (SpecimenArray)resultsIterator.next();
        		assertTrue("Specimen Array is successfully Found" , true);
        		Logger.out.info(" Specimen Array is successfully Found ---->  :: " + returnedSpecimenArray.getName());
            }
       } 
       catch (Exception e) 
       {
    	 Logger.out.error(e.getMessage(),e);
 		 e.printStackTrace();
 		 fail("Failed to search Domain Object");
       }
   }
	 public void testUpdateSpecimenArray()
		{
			try
			{
				SpecimenArray specimenArray = (SpecimenArray) BizTestCaseUtility.getObjectMap(SpecimenArray.class);
		    	Logger.out.info("Inserting domain object------->"+specimenArray);
		       	specimenArray =  BaseTestCaseUtility.updateSpecimenArray(specimenArray);
		       	specimenArray =  (SpecimenArray) appService.updateObject(specimenArray);
				assertTrue("Domain Object is successfully added" , true);
				Logger.out.info(" Specimen Collection Group is successfully added ---->    ID:: " + specimenArray.getId().toString());
			}
			catch(Exception e)
			{
				Logger.out.error(e.getMessage(),e);
				e.printStackTrace();
				fail("Failed to add Domain Object");
			}
		}
	 
	 public void testUpdateSpecimenArrayWithDuplicateName()
		{
			try
			{
				SpecimenArray specimenArray =  BaseTestCaseUtility.initSpecimenArray();
		    	Logger.out.info("Inserting domain object------->"+specimenArray);
		    	specimenArray =  (SpecimenArray) appService.createObject(specimenArray);

	    		SpecimenArray specimenArray1 = new SpecimenArray();
	    		specimenArray1.setId(1L);
	    		String query = "from edu.wustl.catissuecore.domain.SpecimenArray as specimenArray1 where "
	 				+ "specimenArray1.id= 1";	
		     	Logger.out.info(" searching domain object");		    	
		    	List resultList = appService.search(query);
	        	for (Iterator resultsIterator = resultList.iterator(); resultsIterator.hasNext();) 
	        	{
	        		SpecimenArray returnedSpecimenArray = (SpecimenArray)resultsIterator.next();
	        		specimenArray.setName(returnedSpecimenArray.getName());
	            }
	        	specimenArray=(SpecimenArray)appService.updateObject(specimenArray);
	        	fail("Updating specimen array to an existing name should fail");
			}
			catch(Exception e)
			{
				Logger.out.error(e.getMessage(),e);
				e.printStackTrace();
				assertTrue("fails since specimen array with the same label exist",true);
				
			}
		}
		
	 /**
	  * Add Tissue specimen at given container position 
	  *
	  */
	 public void testAddTissueSpecimenAtContainerPosition()
		{
			   try {
				   TissueSpecimen specimenObj = (TissueSpecimen) BaseTestCaseUtility.initTissueSpecimen();		
				   SpecimenCollectionGroup scg = (SpecimenCollectionGroup) BizTestCaseUtility.getObjectMap(SpecimenCollectionGroup.class);
				   StorageContainer parent = (StorageContainer)BizTestCaseUtility.getNameObjectMap("ParentContainer");
				   SpecimenPosition position = new SpecimenPosition();
				   position.setPositionDimensionOne(1);
				   position.setPositionDimensionTwo(3);
				   position.setStorageContainer(parent);
				   specimenObj.setSpecimenPosition(position);
					
				   specimenObj.setSpecimenCollectionGroup(scg);
				   Logger.out.info("Inserting domain object------->"+specimenObj);
				   System.out.println("Before Creating Tissue Specimen");
				   specimenObj =  (TissueSpecimen) appService.createObject(specimenObj);
				   Logger.out.info(" Domain Object is successfully added ---->    ID:: " + specimenObj.getId().toString());
				   Logger.out.info(" Domain Object is successfully added ---->    Name:: " + specimenObj.getLabel());
				   assertTrue(" Domain Object is successfully added ---->    Name:: " + specimenObj.getLabel(), true);			
				}
				catch(Exception e)
				{
					System.out.println("Exception thrown testAddTissueSpecimenAtContainerPosition");
					System.out.println(e);
					Logger.out.error(e.getMessage(),e);
					e.printStackTrace();
					assertFalse(e.getMessage(), true);
				}
				
		}
		/**
		 * Negative Test add specimen at ocupied container position
		 *
		 */
		public void testAddTissueSpecimenAtOccupiedPosition()
		{
			   try {
				   TissueSpecimen specimenObj = (TissueSpecimen) BaseTestCaseUtility.initTissueSpecimen();		
				   SpecimenCollectionGroup scg = (SpecimenCollectionGroup) BizTestCaseUtility.getObjectMap(SpecimenCollectionGroup.class);
				   StorageContainer parent = (StorageContainer)BizTestCaseUtility.getNameObjectMap("ParentContainer");
				   SpecimenPosition position = new SpecimenPosition();
				   position.setPositionDimensionOne(1);
				   position.setPositionDimensionTwo(3);
				   position.setStorageContainer(parent);
				   specimenObj.setSpecimenPosition(position);
					
				   specimenObj.setSpecimenCollectionGroup(scg);
				   Logger.out.info("Inserting domain object------->"+specimenObj);
				   System.out.println("Before Creating Tissue Specimen");
				   specimenObj =  (TissueSpecimen) appService.createObject(specimenObj);
				   Logger.out.info(" Domain Object is successfully added ---->    ID:: " + specimenObj.getId().toString());
				   Logger.out.info(" Domain Object is successfully added ---->    Name:: " + specimenObj.getLabel());
				   assertFalse(" Domain Object is successfully added ---->    Name:: " + specimenObj.getLabel(), true);			
				}
				catch(Exception e)
				{
					System.out.println("Exception thrown testAddTissueSpecimenAtOccupiedPosition");
					System.out.println(e.getMessage());
					Logger.out.error(e.getMessage(),e);
					e.printStackTrace();
					assertTrue("Position not free: "+e.getMessage(), true);
				}
				
		}
		/*Update the storage location of anticipated specimen ,
		 * if collection status is set to collected 
		*/
		
		 public void testUpdateAnticipatedSpecimen()
			{
				   try {
					   TissueSpecimen specimenObj = (TissueSpecimen) BaseTestCaseUtility.initTissueSpecimen();		
					   SpecimenCollectionGroup scg = (SpecimenCollectionGroup) BizTestCaseUtility.getObjectMap(SpecimenCollectionGroup.class);
					
					   specimenObj.setSpecimenCollectionGroup(scg);
					   Logger.out.info("Inserting domain object------->"+specimenObj);
					   System.out.println("Before Creating Anticipated  Specimen");
					   specimenObj =  (TissueSpecimen) appService.createObject(specimenObj);
					   Logger.out.info(" Domain Object is successfully added ---->    ID:: " + specimenObj.getId().toString());
					   Logger.out.info(" Domain Object is successfully added ---->    Name:: " + specimenObj.getLabel());
					   
					   TissueSpecimen tissueSpecimen = new TissueSpecimen();
					   tissueSpecimen.setId(specimenObj.getId());
					   String query = "from edu.wustl.catissuecore.domain.TissueSpecimen as tissueSpecimen where "
			 				+ "tissueSpecimen.id="+specimenObj.getId();	
					   
					   List resultList = appService.search(query);
					   Logger.out.info(" Domain Object is successfully found ---->    Name:: " + specimenObj.getLabel());
					
					   StorageContainer storageContainer= BaseTestCaseUtility.initStorageContainer();
					   storageContainer.setCollectionProtocolCollection(new HashSet());
					   storageContainer = (StorageContainer) appService.createObject(storageContainer); 
					   assertTrue(" Domain Object is successfully added ---->    Name:: " + storageContainer.getId(), true);	
					   tissueSpecimen=(TissueSpecimen) resultList.get(0);
					  
					   tissueSpecimen.setCollectionStatus("Collected");
					   
					   SpecimenPosition position = new SpecimenPosition();
					   position.setPositionDimensionOne(1);
					   position.setPositionDimensionTwo(3);
					   position.setStorageContainer(storageContainer);
					   tissueSpecimen.setSpecimenPosition(position);
					   tissueSpecimen.setExternalIdentifierCollection(null);
					   tissueSpecimen=(TissueSpecimen) appService.updateObject(tissueSpecimen);
					   assertTrue(" Domain Object is successfully updated  ---->    Name:: " + storageContainer.getId(), true);
					  
					}
					catch(Exception e)
					{
						System.out.println("Exception thrown testUpdateAnticipatedSpecimen");
						System.out.println(e);
						Logger.out.error(e.getMessage(),e);
						e.printStackTrace();
						assertFalse(e.getMessage(), true);
					}
					
			}
		 
			/*Update the storage location of anticipated specimen ,
			 * if collection status is set to Pending 
			*/
		 public void testUpdateAnticipatedSpecimenWithPendingStatus()
			{
				   try {
					   TissueSpecimen specimenObj = (TissueSpecimen) BaseTestCaseUtility.initTissueSpecimen();		
					   SpecimenCollectionGroup scg = (SpecimenCollectionGroup) BizTestCaseUtility.getObjectMap(SpecimenCollectionGroup.class);
					
					   specimenObj.setSpecimenCollectionGroup(scg);
					   Logger.out.info("Inserting domain object------->"+specimenObj);
					   System.out.println("Before Creating Anticipated  Specimen");
					   specimenObj =  (TissueSpecimen) appService.createObject(specimenObj);
					   Logger.out.info(" Domain Object is successfully added ---->    ID:: " + specimenObj.getId().toString());
					   Logger.out.info(" Domain Object is successfully added ---->    Name:: " + specimenObj.getLabel());
					   
					   TissueSpecimen tissueSpecimen = new TissueSpecimen();
					   tissueSpecimen.setId(specimenObj.getId());
					   
					   String query = "from edu.wustl.catissuecore.domain.TissueSpecimen as tissueSpecimen where "
			 				+ "tissueSpecimen.id= "+specimenObj.getId();	
					   
					   List resultList = appService.search(query);
					   Logger.out.info(" Domain Object is successfully found ---->    Name:: " + specimenObj.getLabel());
					   System.out.println("after  searching Anticipated  Specimen");
					
					   StorageContainer storageContainer= BaseTestCaseUtility.initStorageContainer();
					   storageContainer.setCollectionProtocolCollection(new HashSet());
					   storageContainer = (StorageContainer) appService.createObject(storageContainer); 
					   assertTrue(" Domain Object is successfully added ---->    Name:: " + storageContainer.getId(), true);	
					   tissueSpecimen=(TissueSpecimen) resultList.get(0);
					  
					   tissueSpecimen.setCollectionStatus("Pending");
					   
					   SpecimenPosition position = new SpecimenPosition();
					   position.setPositionDimensionOne(1);
					   position.setPositionDimensionTwo(3);
					   position.setStorageContainer(storageContainer);
					   tissueSpecimen.setSpecimenPosition(position);
					   tissueSpecimen.setExternalIdentifierCollection(null);
					   tissueSpecimen=(TissueSpecimen) appService.updateObject(tissueSpecimen);
					   assertFalse(" Domain Object is successfully updated  ---->    Name:: " + storageContainer.getId(), true);
					  
					}
					catch(Exception e)
					{
						System.out.println("Exception thrown testUpdateAnticipatedSpecimenWithPendingStatus");
						System.out.println(e);
						Logger.out.error(e.getMessage(),e);
						e.printStackTrace();
						assertTrue("Failed to update Container Location for due to pending status of specimen  ", true);
					}
					
			}
		 
		 /**
		  * Search Specimen located at given position 
		  *
		  */
		public void testSpecimenLocatedAtPosition()
		{
				try
				{
					StorageContainer storageContainer = (StorageContainer)BizTestCaseUtility.getNameObjectMap("ParentContainer");
					
					StorageContainer parent = new StorageContainer();
					parent.setId(storageContainer.getId());
					
					SpecimenPosition position = new SpecimenPosition();
					position.setPositionDimensionOne(1);
					position.setPositionDimensionTwo(3);
					position.setStorageContainer(parent);
					String query = "from edu.wustl.catissuecore.domain.SpecimenPosition as position where "
		 				+ "position.positionDimensionOne= 1 and position.positionDimensionTwo = 3 and position.storageContainer.id = "+ parent.getId();;	
					List result = appService.search(query);
					if(result.size()>1||result.size()<1)
					{
						assertFalse("testSpecimenLocatedAtPosition Could not find Specimen Object", true);
					}
					assertTrue("Specimen successfully found. Size:" +result.size(), true);
				}
				catch(Exception e)
				{
					Logger.out.error(e.getMessage(),e);
					System.out
							.println("SpecimenTestCases.testSpecimenLocatedAtPosition()");
					System.out.println(e.getMessage());
					e.printStackTrace();
					assertFalse(e.getMessage(), true);
				}
		}
		/**
		 * Get all position of storage container occupied by specimen
		 *
		 */
		
		public void testSearchOccupiedSpecimenPositions()
		{
				try
				{
					StorageContainer parent = (StorageContainer)BizTestCaseUtility.getNameObjectMap("ParentContainer");
					StorageContainer storageContainer = new StorageContainer();
					storageContainer.setId(parent.getId());
					
					String query = "from edu.wustl.catissuecore.domain.StorageContainer as storageContainer where "
		 				+ "storageContainer.id= "+parent.getId();	
					
					List result = appService.search(query);
					if(result.size()>1||result.size()<1)
					{
						assertFalse("Could not find Specimen position ", true);
					}
					assertTrue("Specimen position  successfully found. Size:" +result.size(), true);
				}
				catch(Exception e)
				{
					Logger.out.error(e.getMessage(),e);
					System.out
							.println("SpecimenTestCases.testSearchOccupiedSpecimenPositions()");
					System.out.println(e.getMessage());
					e.printStackTrace();
					assertFalse(e.getMessage(), true);
				}
		}
		/**
		 * Negative test case : Test case for decreasing capacity of already filled Storage Container
		 */
		public void testDecreseCapacityOfFullContainer()
		 {
			 try
			 {
				   TissueSpecimen tSpecimenObj = (TissueSpecimen) BaseTestCaseUtility.initTissueSpecimen();
				   MolecularSpecimen mSpecimenObj = (MolecularSpecimen) BaseTestCaseUtility.initMolecularSpecimen();
				   FluidSpecimen fSpecimenObj = (FluidSpecimen) BaseTestCaseUtility.initFluidSpecimen();
				   CellSpecimen cSpecimenObj = (CellSpecimen) BaseTestCaseUtility.initCellSpecimen();
				   
				   SpecimenCollectionGroup scg = (SpecimenCollectionGroup) BizTestCaseUtility.getObjectMap(SpecimenCollectionGroup.class);
				   
				   StorageContainer storageContainer =  BaseTestCaseUtility.initStorageContainer();
				   Capacity capacity = new Capacity();
				   capacity.setOneDimensionCapacity(new Integer(2));
				   capacity.setTwoDimensionCapacity(new Integer(2));
				   storageContainer.setCapacity(capacity);
				   storageContainer = (StorageContainer) appService.createObject(storageContainer);
			   
				   SpecimenPosition pos1 = new SpecimenPosition();
				   pos1.setPositionDimensionOne(1);
				   pos1.setPositionDimensionTwo(1);
				   pos1.setStorageContainer(storageContainer);
				   tSpecimenObj.setSpecimenPosition(pos1);
					
				   SpecimenPosition pos2 = new SpecimenPosition();
				   pos2.setPositionDimensionOne(1);
				   pos2.setPositionDimensionTwo(2);
				   pos2.setStorageContainer(storageContainer);
				   mSpecimenObj.setSpecimenPosition(pos2);
					
				   SpecimenPosition pos3 = new SpecimenPosition();
				   pos3.setPositionDimensionOne(2);
				   pos3.setPositionDimensionTwo(1);
				   pos3.setStorageContainer(storageContainer);
				   cSpecimenObj.setSpecimenPosition(pos3);
					
				   SpecimenPosition pos4 = new SpecimenPosition();
				   pos4.setPositionDimensionOne(2);
				   pos4.setPositionDimensionTwo(2);
				   pos4.setStorageContainer(storageContainer);
				   fSpecimenObj.setSpecimenPosition(pos4);
					
				   
				   tSpecimenObj.setSpecimenCollectionGroup(scg);
				   cSpecimenObj.setSpecimenCollectionGroup(scg);
				   mSpecimenObj.setSpecimenCollectionGroup(scg);
				   fSpecimenObj.setSpecimenCollectionGroup(scg);
				   
				   System.out.println("Before Creating Specimen object");
				   
				   tSpecimenObj = (TissueSpecimen) appService.createObject(tSpecimenObj);
				   mSpecimenObj = (MolecularSpecimen) appService.createObject(mSpecimenObj);
				   fSpecimenObj = (FluidSpecimen) appService.createObject(fSpecimenObj);
				   cSpecimenObj = (CellSpecimen) appService.createObject(cSpecimenObj);
				   
				   Capacity cap = storageContainer.getCapacity();	
				   cap.setOneDimensionCapacity(new Integer(1));
				   cap.setTwoDimensionCapacity(new Integer(1));
				   storageContainer = (StorageContainer) appService.updateObject(storageContainer);
				   assertFalse("Container is already Full:" +storageContainer , true);  
			}
			catch(Exception e)
			{
				System.out.println("TestCaseTesting.testDecreseCapacityOfFullContainer()");
				e.printStackTrace();
				assertTrue("Failed to Update as Storage Container is already Full :" +e.getMessage() , true);
				
			}
		}
		
		/**
		 * Test case for Transfer Specimen Position from one position to another
		 */
		public void testTransferPositionOFSpecimen()
		 {
			 try
			 {
				   TissueSpecimen tSpecimenObj = (TissueSpecimen) BaseTestCaseUtility.initTissueSpecimen();
				   SpecimenCollectionGroup scg = (SpecimenCollectionGroup) BizTestCaseUtility.getObjectMap(SpecimenCollectionGroup.class);
				   
				   StorageContainer storageContainer =  BaseTestCaseUtility.initStorageContainer();
				   Collection collectionProtocolCollection = new HashSet();
				   storageContainer.setCollectionProtocolCollection(collectionProtocolCollection);
				   storageContainer = (StorageContainer) appService.createObject(storageContainer);
			   
				   SpecimenPosition pos1 = new SpecimenPosition();
				   pos1.setPositionDimensionOne(1);
				   pos1.setPositionDimensionTwo(1);
				   pos1.setStorageContainer(storageContainer);
				   tSpecimenObj.setSpecimenPosition(pos1);
				   tSpecimenObj.setSpecimenCollectionGroup(scg);
				   System.out.println("Before Creating Specimen object");
				   tSpecimenObj = (TissueSpecimen) appService.createObject(tSpecimenObj);
				   System.out.println("After Creating Specimen object");
				   			   
				   TransferEventParameters spe = new TransferEventParameters();
				   spe.setSpecimen(tSpecimenObj);
			       spe.setTimestamp(new Date(System.currentTimeMillis()));
			       User user = new User();
			       user.setId(1l);//admin
			       //(User)TestCaseUtility.getObjectMap(User.class);
			       spe.setUser(user);
			       spe.setComment("Transfer Event");
			       spe.setFromPositionDimensionOne(1);
				   spe.setFromPositionDimensionTwo(1);
				   spe.setFromStorageContainer(storageContainer);
				   spe.setToPositionDimensionOne(2);
				   spe.setToPositionDimensionTwo(2);
				   spe.setToStorageContainer(storageContainer);
				   System.out.println("Before Creating TransferEvent");
				   spe = (TransferEventParameters) appService.createObject(spe);
				   System.out.println("After Creating Specimen object");
				   assertTrue("Specimen Position Sucessfully Transfered :" + spe , true);
				   
			}
			catch(Exception e)
			{
				System.out.println("TestCaseTesting.testTransferPositionOFSpecimen()");
				e.printStackTrace();
				assertFalse("Container is already Full:" +e.getMessage(), true);  
				
			}
		}
		
		/**
		 * Negative test case: Test case for Transfer Specimen Position from one position to another Occupied container
		 */
		public void testTransferSpecimenToOccupiedLocation()
		 {
			 try
			 {
				   TissueSpecimen tSpecimenObj = (TissueSpecimen) BaseTestCaseUtility.initTissueSpecimen();
				   MolecularSpecimen mSpecimenObj = (MolecularSpecimen) BaseTestCaseUtility.initMolecularSpecimen();
				   SpecimenCollectionGroup scg = (SpecimenCollectionGroup) BizTestCaseUtility.getObjectMap(SpecimenCollectionGroup.class);
				   
				   StorageContainer storageContainer =  BaseTestCaseUtility.initStorageContainer();
				   Collection collectionProtocolCollection = new HashSet();
				   storageContainer.setCollectionProtocolCollection(collectionProtocolCollection);
				   storageContainer = (StorageContainer) appService.createObject(storageContainer);
			   
				   SpecimenPosition pos1 = new SpecimenPosition();
				   pos1.setPositionDimensionOne(1);
				   pos1.setPositionDimensionTwo(1);
				   pos1.setStorageContainer(storageContainer);
				   tSpecimenObj.setSpecimenPosition(pos1);
				   tSpecimenObj.setSpecimenCollectionGroup(scg);
				   System.out.println("Before Creating Tissue Specimen object");
				   tSpecimenObj = (TissueSpecimen) appService.createObject(tSpecimenObj);
				   System.out.println("After Creating Tissue Specimen object");
				   
				   
				   SpecimenPosition pos2 = new SpecimenPosition();
				   pos2.setPositionDimensionOne(2);
				   pos2.setPositionDimensionTwo(2);
				   pos2.setStorageContainer(storageContainer);
				   mSpecimenObj.setSpecimenPosition(pos2);
				   mSpecimenObj.setSpecimenCollectionGroup(scg);
				   System.out.println("Before Creating Molecular Specimen object");
				   mSpecimenObj = (MolecularSpecimen) appService.createObject(mSpecimenObj);
				   System.out.println("After Creating Molecular Specimen object");
				   
				   TransferEventParameters spe = new TransferEventParameters();
				   spe.setSpecimen(tSpecimenObj);
			       spe.setTimestamp(new Date(System.currentTimeMillis()));
			       User user = new User();
			       user.setId(1l);//admin
			       //(User)TestCaseUtility.getObjectMap(User.class);
			       spe.setUser(user);
			       spe.setComment("Transfer Event");
			       spe.setFromPositionDimensionOne(1);
				   spe.setFromPositionDimensionTwo(1);
				   spe.setFromStorageContainer(storageContainer);
				   spe.setToPositionDimensionOne(2);
				   spe.setToPositionDimensionTwo(2);
				   spe.setToStorageContainer(storageContainer);
				   System.out.println("Before Creating TransferEvent");
				   spe = (TransferEventParameters) appService.createObject(spe);
				   System.out.println("After Creating Specimen object");
				   assertFalse("Container is already Full", true);  
				   
			}
			catch(Exception e)
			{
				System.out.println("TestCaseTesting.testTransferPositionOFSpecimen()");
				e.printStackTrace();
  			    assertTrue("Specimen Position Sucessfully Transfered :" + e.getMessage() , true);
			}
		}
		/**
		 * Test case for Dispose Specimen Event
		 */
		public void testInsertDisposeSpecimenEvent()
		 {
			 try
			 {
				   TissueSpecimen tSpecimenObj = (TissueSpecimen) BaseTestCaseUtility.initTissueSpecimen();
				   SpecimenCollectionGroup scg = (SpecimenCollectionGroup) BizTestCaseUtility.getObjectMap(SpecimenCollectionGroup.class);
				   
				   StorageContainer storageContainer =  BaseTestCaseUtility.initStorageContainer();
				   Collection collectionProtocolCollection = new HashSet();
				   storageContainer.setCollectionProtocolCollection(collectionProtocolCollection);
				   storageContainer = (StorageContainer) appService.createObject(storageContainer);
			   
				   SpecimenPosition pos1 = new SpecimenPosition();
				   pos1.setPositionDimensionOne(1);
				   pos1.setPositionDimensionTwo(1);
				   pos1.setStorageContainer(storageContainer);
				   tSpecimenObj.setSpecimenPosition(pos1);
				   tSpecimenObj.setSpecimenCollectionGroup(scg);
				   System.out.println("Before Creating Specimen object");
				   tSpecimenObj = (TissueSpecimen) appService.createObject(tSpecimenObj);
				   System.out.println("After Creating Specimen object");
				   			   
				   DisposalEventParameters disposalEvent = new DisposalEventParameters();
			       disposalEvent.setActivityStatus(Status.ACTIVITY_STATUS_CLOSED.toString());
 		           disposalEvent.setSpecimen(tSpecimenObj);
			       disposalEvent.setTimestamp(new Date(System.currentTimeMillis()));
			       User user = new User();
			       user.setId(1l);//admin
			       disposalEvent.setUser(user);
			       disposalEvent.setReason("Testing API");
			       disposalEvent.setComment("Dispose Event");
			       disposalEvent.setActivityStatus(Status.ACTIVITY_STATUS_DISABLED.toString());
			       System.out.println("Before Creating DisposeEvent");
			       disposalEvent = (DisposalEventParameters) appService.createObject(disposalEvent);
			       if(Status.ACTIVITY_STATUS_DISABLED.toString().equals(disposalEvent.getSpecimen().getActivityStatus()))
			       {
			    	   assertTrue("Disposed event sucessfully fired: Activity Status Disable :" + disposalEvent , true);   
			       }
			       else
			       {
			    	   assertFalse("Disposed event Failed:" + disposalEvent , true);   
			       }
			}
			catch(Exception e)
			{
				System.out.println("TestCaseTesting.testInsertDisposeSpecimenEvent()");
				e.printStackTrace();
				assertFalse("Container is already Full:" +e.getMessage(), true);  
				
			}
		}
	
}
