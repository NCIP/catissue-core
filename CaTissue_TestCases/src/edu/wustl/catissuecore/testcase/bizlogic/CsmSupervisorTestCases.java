package edu.wustl.catissuecore.testcase.bizlogic;

import java.text.ParseException;
import java.util.Collection;
import java.util.HashSet;

import edu.wustl.catissuecore.domain.Capacity;
import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.catissuecore.domain.CollectionProtocolRegistration;
import edu.wustl.catissuecore.domain.ContainerPosition;
import edu.wustl.catissuecore.domain.Participant;
import edu.wustl.catissuecore.domain.Site;
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
import edu.wustl.common.util.Utility;
import edu.wustl.common.util.logger.Logger;

public class CsmSupervisorTestCases extends CaTissueBaseTestCase {
//	static ApplicationService appService = null;
//	ClientSession cs = null;
//	
//	public void setUp(){
//		
//		//Logger.configure("");
//		appService = ApplicationServiceProvider.getApplicationService();
//		cs = ClientSession.getInstance();
//		try
//		{ 
//			cs.startSession("supervisor@admin.com", "Test123");
//			System.out.println("Inside CaTissue supervisor setup ");
//		} 	
//					
//		catch (Exception ex) 
//		{ 
//			System.out.println(ex.getMessage()); 
//			ex.printStackTrace();
//			fail();
//			System.exit(1);
//		}		
//	}
//	
//	
//	public void tearDown(){
//		System.out.println("Inside CaTissue supervisor teardown");
//		cs.terminateSession();
//	}
	
	public void testUseStorageContainerOfSiteWithAllowUsePrivilegeForSupervisor()
		{
		   try {
			    System.out.println("Creating Tissue Specimen");
			    CollectionProtocol cp= new CollectionProtocol();
			    cp.setId(new Long(TestCaseUtility.CP_WITH_ALLOW_READ_PRIV));
			 
			    SpecimenCollectionGroup scg =  createSCGWithConsents(cp);	
				TissueSpecimen specimenObj = (TissueSpecimen) BaseTestCaseUtility.initTissueSpecimen();				
				specimenObj.setSpecimenCollectionGroup(scg);
				StorageContainer sc = new StorageContainer();
				sc.setId(new Long(TestCaseUtility.STORAGECONTAINER_WITH_ALLOWUSE_PRIV));
				SpecimenPosition specPosition = new SpecimenPosition();
				specPosition.setStorageContainer(sc);
				specPosition.setSpecimen(specimenObj);
				specPosition.setPositionDimensionOne(new Integer(1));
				specPosition.setPositionDimensionTwo(new Integer(1));
				specimenObj.setSpecimenPosition(specPosition);
				Logger.out.info("Inserting domain object------->"+specimenObj);
				specimenObj =  (TissueSpecimen) appService.createObject(specimenObj);
				System.out.println("Specimen name"+specimenObj.getLabel());
				TestCaseUtility.setObjectMap(specimenObj, TissueSpecimen.class);
				Logger.out.info(" Domain Object is successfully added ---->    ID:: " + specimenObj.getId().toString());
				Logger.out.info(" Domain Object is successfully added ---->    Name:: " + specimenObj.getLabel());
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
	 
	 public void testUseStorageContainerOfSiteWithDisallowUsePrivilegeForSupervisor()
		{
		   try {
			    System.out.println("Creating Tissue Specimen");
			    CollectionProtocol cp= new CollectionProtocol();
			    cp.setId(new Long(TestCaseUtility.CP_WITH_ALLOW_READ_PRIV));
			 
			    SpecimenCollectionGroup scg =  createSCGWithConsents(cp);	
			    
			    TestCaseUtility.setObjectMap(scg, SpecimenCollectionGroup.class);
				TissueSpecimen specimenObj = (TissueSpecimen) BaseTestCaseUtility.initTissueSpecimen();				
				specimenObj.setSpecimenCollectionGroup(scg);
				StorageContainer sc = new StorageContainer();
				sc.setId(new Long(TestCaseUtility.STORAGECONTAINER_WITH_DISALLOWUSE_PRIV));
				SpecimenPosition specPosition = new SpecimenPosition();
				specPosition.setStorageContainer(sc);
				specPosition.setSpecimen(specimenObj);
				specPosition.setPositionDimensionOne(new Integer(1));
				specPosition.setPositionDimensionTwo(new Integer(1));
				specimenObj.setSpecimenPosition(specPosition);
				Logger.out.info("Inserting domain object------->"+specimenObj);
				System.out.println("Before Creating Tissue Specimen");
				specimenObj =  (TissueSpecimen) appService.createObject(specimenObj);
				TestCaseUtility.setObjectMap(specimenObj, TissueSpecimen.class);
				Logger.out.info(" Domain Object is successfully added ---->    ID:: " + specimenObj.getId().toString());
				Logger.out.info(" Domain Object is successfully added ---->    Name:: " + specimenObj.getLabel());
				assertFalse(" Domain Object is successfully added ---->    Name:: " + specimenObj.getLabel(), true);			
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
	 
	 public void testAddSpecimenArrayInContainerWithAllowUsePrivilegeForSupervisor()
		{
			try
			{
				System.out.println("reached");
				SpecimenArrayType specimenArrayType = (SpecimenArrayType) TestCaseUtility.getObjectMap(SpecimenArrayType.class);
				System.out.println("reached");
				SpecimenArray specimenArray =  new SpecimenArray();
				specimenArray.setSpecimenArrayType(specimenArrayType);
				
				
				specimenArray.setBarcode("bar" + UniqueKeyGeneratorUtil.getUniqueKey());
				specimenArray.setName("sa" + UniqueKeyGeneratorUtil.getUniqueKey()); 
				
				User createdBy = new User();
				createdBy.setId(new Long(1));
				specimenArray.setCreatedBy(createdBy);
				
				System.out.println("reached");
				
				Capacity capacity = new Capacity();
				capacity.setOneDimensionCapacity(4);
				capacity.setTwoDimensionCapacity(4);
				specimenArray.setCapacity(capacity);
				System.out.println("reached");
				
				specimenArray.setComment("");
				StorageContainer storageContainer = new StorageContainer();
				storageContainer.setId(new Long(TestCaseUtility.SPECIMENARRAYCONTAINER_WITH_ALLOWUSE_PRIV));	
				
				System.out.println("reached");
				
	//			specimenArray.setStorageContainer(storageContainer);
				ContainerPosition cntPos = new ContainerPosition();
				cntPos.setPositionDimensionOne(new Integer(1));
				cntPos.setPositionDimensionTwo(new Integer(1));
				cntPos.setOccupiedContainer(specimenArray);
				cntPos.setParentContainer(storageContainer);
				specimenArray.setLocatedAtPosition(cntPos);
				System.out.println("reached");
				
				Collection specimenArrayContentCollection = new HashSet();
				SpecimenArrayContent specimenArrayContent = new SpecimenArrayContent();
				
				System.out.println("reached");
				
				Specimen specimen = (Specimen) TestCaseUtility.getObjectMap(TissueSpecimen.class);
				
				System.out.println("reached");
				
				specimenArrayContent.setSpecimen(specimen);
				specimenArrayContent.setPositionDimensionOne(new Integer(2));
				specimenArrayContent.setPositionDimensionTwo(new Integer(4));
				
				System.out.println("reached");
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

		public static SpecimenCollectionGroup createSCGWithConsents(CollectionProtocol cp){
				
				Participant participant = BaseTestCaseUtility.initParticipant();
			   try{
					participant = (Participant) appService.createObject(participant);
				}
				catch(Exception e){
					Logger.out.error(e.getMessage(),e);
		           	e.printStackTrace();
		           	assertFalse(e.getMessage(), true);
				}
				TestCaseUtility.setObjectMap(participant, Participant.class);
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
				}
				collectionProtocolRegistration.setSignedConsentDocumentURL("F:/doc/consentDoc.doc");
				User user = (User)TestCaseUtility.getObjectMap(User.class);
				collectionProtocolRegistration.setConsentWitness(user);
				
//				Collection consentTierResponseCollection = new LinkedHashSet();
//				Collection consentTierCollection = new LinkedHashSet();
//				consentTierCollection = cp.getConsentTierCollection();
//				
//				Iterator consentTierItr = consentTierCollection.iterator();
//				 while(consentTierItr.hasNext())
//				 {
//					 ConsentTier consent= (ConsentTier) consentTierItr.next();
//					 ConsentTierResponse response= new ConsentTierResponse();
//					 response.setResponse("No");
//					 response.setConsentTier(consent);
//					 consentTierResponseCollection.add(response);				 
//				 }
//					
//				collectionProtocolRegistration.setConsentTierResponseCollection(consentTierResponseCollection);
			
				System.out.println("Creating CPR");
				try{
					collectionProtocolRegistration = (CollectionProtocolRegistration) appService.createObject(collectionProtocolRegistration);
				}
				catch(Exception e){
					Logger.out.error(e.getMessage(),e);
		           	e.printStackTrace();
		           	assertFalse(e.getMessage(), true);
				}
				System.out.println("CPR::"+collectionProtocolRegistration);
				TestCaseUtility.setObjectMap(collectionProtocolRegistration, CollectionProtocolRegistration.class);
				
				SpecimenCollectionGroup scg = new SpecimenCollectionGroup();
				scg =(SpecimenCollectionGroup) BaseTestCaseUtility.createSCG(collectionProtocolRegistration);
				Site site = new Site();
				site.setId(new Long(1));
				scg.setSpecimenCollectionSite(site);
				scg.setName("New SCG"+UniqueKeyGeneratorUtil.getUniqueKey());		    
				scg = (SpecimenCollectionGroup) BaseTestCaseUtility.setEventParameters(scg);
				System.out.println("Creating SCG");
				try{
					scg = (SpecimenCollectionGroup) appService.createObject(scg);
					System.out.println("SCG::"+ scg.getName());
				}
				catch(Exception e){
					Logger.out.error(e.getMessage(),e);
		           	e.printStackTrace();
		           	assertFalse(e.getMessage(), true);
				}
				return scg;				
		  }
		}


