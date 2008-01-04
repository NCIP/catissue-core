package edu.wustl.catissuecore.bizlogic.test;

import java.text.ParseException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.catissuecore.domain.CollectionProtocolRegistration;
import edu.wustl.catissuecore.domain.ConsentTier;
import edu.wustl.catissuecore.domain.ConsentTierResponse;
import edu.wustl.catissuecore.domain.Participant;
import edu.wustl.catissuecore.domain.Site;
import edu.wustl.catissuecore.domain.SpecimenArray;
import edu.wustl.catissuecore.domain.SpecimenArrayType;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.catissuecore.domain.StorageContainer;
import edu.wustl.catissuecore.domain.TissueSpecimen;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.util.Utility;
import edu.wustl.common.util.logger.Logger;


public class StorageContainerTestCases extends CaTissueBaseTestCase{
	AbstractDomainObject domainObject = null;
	
	public void testAddStorageContainer()
	{
		try{
			StorageContainer storageContainer= BaseTestCaseUtility.initStorageContainer();			
			System.out.println(storageContainer);
			storageContainer = (StorageContainer) appService.createObject(storageContainer); 
			TestCaseUtility.setObjectMap(storageContainer, StorageContainer.class);
			System.out.println("Object created successfully");
			assertTrue("Object added successfully", true);
		 }
		 catch(Exception e){
			 e.printStackTrace();
			 assertFalse("could not add object", true);
		 }
	}
	
	public void testSearchStorageContainer()
	{
		StorageContainer storageContainer = new StorageContainer();
    	Logger.out.info(" searching domain object");
    	storageContainer.setId(new Long(1));
   
         try {
        	 List resultList = appService.search(StorageContainer.class,storageContainer);
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
           	assertFalse("Does not find Domain Object", true);
	 		
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
	 		assertFalse("failed to update Object", true);
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
			 assertFalse("Could notclose Storage Container", true);
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
			 e.printStackTrace();
			 assertFalse("Could not disable Storage Container", true);
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
			assertFalse("Failed to create site ", true);
		}
		
		site.setActivityStatus("Closed");
		
		try{
			site =(Site)appService.updateObject(site);
		 }catch(Exception e){
			Logger.out.error(e.getMessage(),e);	
			e.printStackTrace();	
			assertFalse("Failed to close the site ", true);
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
		assertFalse("Test Failed", true);
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
			 assertFalse("Failed to create site", true);
		}		
		
			
		CollectionProtocol cp = BaseTestCaseUtility.initCollectionProtocol();
		try{
			cp = (CollectionProtocol) appService.createObject(cp);
		}
		catch(Exception e){
			Logger.out.error(e.getMessage(),e);
           	e.printStackTrace();
           	assertFalse("Failed to create collection protocol", true);
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
			 assertFalse("Failed create Storage Container", true);
		}
		
		Participant participant = BaseTestCaseUtility.initParticipant();
		
		try{
			participant = (Participant) appService.createObject(participant);
		}
		catch(Exception e){
			Logger.out.error(e.getMessage(),e);
           	e.printStackTrace();
           	assertFalse("Failed to create participant", true);
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
			assertFalse("Failed to add registration date", true);
		}
		collectionProtocolRegistration.setSignedConsentDocumentURL("F:/doc/consentDoc.doc");
		User user = (User)TestCaseUtility.getObjectMap(User.class);
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
           	assertFalse("Failed to register participant", true);
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
           	assertFalse("Failed to create SCG", true);
		}
		
		site.setActivityStatus("Closed");
		
		try{
			site = (Site) appService.updateObject(site);
		}
		catch(Exception e){
			Logger.out.error(e.getMessage(),e);
			 e.printStackTrace();
			 assertFalse("Could not update site", true);
		}
		
		TissueSpecimen ts =(TissueSpecimen) BaseTestCaseUtility.initTissueSpecimen();
		ts.setStorageContainer(storageContainer);
		ts.setPositionDimensionOne(new Integer(1));
		ts.setPositionDimensionTwo(new Integer(2));
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
	    	TestCaseUtility.setObjectMap(specimenArrayType, SpecimenArrayType.class);
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
	     	Logger.out.info(" searching domain object");		    	
	    	List resultList = appService.search(SpecimenArrayType.class,specimenArrayType);
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
		    	TestCaseUtility.setObjectMap(specimenArray, SpecimenArray.class);
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
	 
	 public void testSearchSpecimenArray()
	 {
    	try 
    	{
    		SpecimenArray cachedSpecimenArray = (SpecimenArray) TestCaseUtility.getObjectMap(SpecimenArray.class);
    		SpecimenArray specimenArray = new SpecimenArray();
    		specimenArray.setId(cachedSpecimenArray.getId());
	     	Logger.out.info(" searching domain object");		    	
	    	List resultList = appService.search(SpecimenArray.class,specimenArray);
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
				SpecimenArray specimenArray = (SpecimenArray) TestCaseUtility.getObjectMap(SpecimenArray.class);
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

}
