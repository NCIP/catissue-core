package edu.wustl.catissuecore.testcase.bizlogic;



import java.text.ParseException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import edu.wustl.catissuecore.domain.Capacity;
import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.catissuecore.domain.CollectionProtocolRegistration;
import edu.wustl.catissuecore.domain.ConsentTier;
import edu.wustl.catissuecore.domain.ConsentTierResponse;
import edu.wustl.catissuecore.domain.ContainerPosition;
import edu.wustl.catissuecore.domain.MolecularSpecimen;
import edu.wustl.catissuecore.domain.Participant;
import edu.wustl.catissuecore.domain.Site;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.catissuecore.domain.SpecimenPosition;
import edu.wustl.catissuecore.domain.StorageContainer;
import edu.wustl.catissuecore.domain.StorageType;
import edu.wustl.catissuecore.domain.TissueSpecimen;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.testcase.CaTissueSuiteBaseTest;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.util.Utility;
import edu.wustl.common.util.logger.Logger;

public class StorageContainerRestrictionsBizTestCases extends CaTissueSuiteBaseTest {
	
	StorageType box =null;
	StorageType rack =null;
	StorageType freezer =null;
	StorageContainer freezerContainer = null;
	StorageContainer rackContainer = null;
	StorageContainer boxContainer = null;
	
	public void testAddTissueSpecInStorageContainerCanHoldTissueSpecimen()
	{
		try{
			CollectionProtocol cp = BaseTestCaseUtility.initCollectionProtocol();
			cp =(CollectionProtocol) appService.createObject(cp);
			StorageType tissueST = BaseTestCaseUtility.initTissueStorageType();
			tissueST = (StorageType) appService.createObject(tissueST);
			StorageContainer storageContainer= BaseTestCaseUtility.initStorageContainerHoldsTissueSpec();	
			storageContainer.setStorageType(tissueST);
			System.out.println(storageContainer);
			storageContainer = (StorageContainer) appService.createObject(storageContainer);
			TestCaseUtility.setObjectMap(storageContainer, StorageContainer.class);
			System.out.println("reached");
		//	SpecimenCollectionGroup scg = BaseTestCaseUtility.initSCG();
			SpecimenCollectionGroup scg = createSCGWithConsents(cp);
			TestCaseUtility.setObjectMap(scg, SpecimenCollectionGroup.class);
			TissueSpecimen ts =(TissueSpecimen) BaseTestCaseUtility.initTissueSpecimen();
			SpecimenPosition spPos = new SpecimenPosition();
			spPos.setStorageContainer(storageContainer);
			spPos.setSpecimen(ts);
			spPos.setPositionDimensionOne(new Integer(1));
			spPos.setPositionDimensionTwo(new Integer(1));
			ts.setSpecimenPosition(spPos);
			ts.setSpecimenCollectionGroup(scg);
			System.out.println("Befor creating Tissue Specimen");
			ts = (TissueSpecimen) appService.createObject(ts);
			System.out.println("Tissue Specimen successfully created with Lable"+ ts.getLabel());
			Logger.out.info("Tissue Specimen successfully created with Lable"+ ts.getLabel());
			assertTrue("Successfully added tissue specimen in container", true);
		 }
		 catch(Exception e){
			 e.printStackTrace();
			 assertFalse("could not add object", true);
		 }
	}
	
	public void testAddMolSpecInStorageContainerCanHoldTissueSpecimenNegativeTestcase()
	{
		try{
			SessionDataBean bean = (SessionDataBean)getSession().getAttribute("sessionData");
			StorageContainer storageContainer= (StorageContainer) TestCaseUtility.getObjectMap(StorageContainer.class);				
			SpecimenCollectionGroup scg = (SpecimenCollectionGroup) TestCaseUtility.getObjectMap(SpecimenCollectionGroup.class);			
			MolecularSpecimen ts =(MolecularSpecimen) BaseTestCaseUtility.initMolecularSpecimen();
			SpecimenPosition spPos = new SpecimenPosition();
			spPos.setStorageContainer(storageContainer);
			spPos.setSpecimen(ts);
			spPos.setPositionDimensionOne(new Integer(1));
			spPos.setPositionDimensionTwo(new Integer(2));
			ts.setSpecimenPosition(spPos);
			ts.setSpecimenCollectionGroup(scg);
			System.out.println("Befor creating Mol Specimen");
			ts = (MolecularSpecimen) appService.createObject(ts);
			assertFalse("Successfully added mol specimen in container which can only store tissue specimens", true);
		 }
		 catch(Exception e){
			 e.printStackTrace();
			 assertTrue("Failed to add Mol specimen in container which can only store tissue specimens", true);
		 }
	}  
	
/*	public void testAddCellSpecInStorageContainer()
	{
		try{
			
			StorageContainer storageContainer= (StorageContainer) TestCaseUtility.getObjectMap(StorageContainer.class);				
			SpecimenCollectionGroup scg = (SpecimenCollectionGroup) TestCaseUtility.getObjectMap(SpecimenCollectionGroup.class);			
			CellSpecimen ts =(CellSpecimen) BaseTestCaseUtility.initCellSpecimen();
			ts.setStorageContainer(storageContainer);
			ts.setPositionDimensionOne(new Integer(2));
			ts.setPositionDimensionTwo(new Integer(1));
			ts.setSpecimenCollectionGroup(scg);
			System.out.println("Befor creating Mol Specimen");
			ts = (CellSpecimen) appService.createObject(ts);
			assertFalse("could not add object", true);
		 }
		 catch(Exception e){
			 e.printStackTrace();
			 assertTrue("Failed to add Mol specimen in SC", true);
		 }
	}

	public void testAddFluidSpecInStorageContainer()
	{
		try{
			
			StorageContainer storageContainer= (StorageContainer) TestCaseUtility.getObjectMap(StorageContainer.class);				
			SpecimenCollectionGroup scg = (SpecimenCollectionGroup) TestCaseUtility.getObjectMap(SpecimenCollectionGroup.class);			
			FluidSpecimen ts =(FluidSpecimen) BaseTestCaseUtility.initFluidSpecimen();
			ts.setStorageContainer(storageContainer);
			ts.setPositionDimensionOne(new Integer(2));
			ts.setPositionDimensionTwo(new Integer(1));
			ts.setSpecimenCollectionGroup(scg);
			System.out.println("Befor creating Mol Specimen");
			ts = (FluidSpecimen) appService.createObject(ts);
			assertFalse("could not add object", true);
		 }
		 catch(Exception e){
			 e.printStackTrace();
			 assertTrue("Failed to add Mol specimen in SC", true);
		 }
	} */
	
	
	
		public void testAddSpecimenInStorageContainerHavingCPRestrictionPositiveTest()
	{
		
			CollectionProtocol cp = BaseTestCaseUtility.initCollectionProtocol();
			try{
				cp = (CollectionProtocol) appService.createObject(cp);
			}
			catch(Exception e){
				Logger.out.error(e.getMessage(),e);
	           	e.printStackTrace();
	           	assertFalse("Failed to create collection protocol", true);
			}
			System.out.println("CP:"+cp.getTitle());
			StorageType ST = BaseTestCaseUtility.initStorageType();			
			try{
				ST = (StorageType) appService.createObject(ST);
			}
			catch(Exception e){
				Logger.out.error(e.getMessage(),e);
	           	e.printStackTrace();
	           	assertFalse("Failed to create storage type", true);
			}
			
			StorageContainer storageContainer= BaseTestCaseUtility.initStorageContainer();
			Collection cpCollection = new HashSet();
			cpCollection.add(cp);
			storageContainer.setCollectionProtocolCollection(cpCollection);
			storageContainer.setStorageType(ST);
			
			try{
				storageContainer = (StorageContainer) appService.createObject(storageContainer);
			}
			catch(Exception e){
				Logger.out.error(e.getMessage(),e);
	           	e.printStackTrace();
	           	assertFalse("Failed to create storage container", true);
			}
			TestCaseUtility.setObjectMap(storageContainer, StorageContainer.class);
			System.out.println("Storage Container"+storageContainer.getName()+" successfully created");
			
			SpecimenCollectionGroup scg= (SpecimenCollectionGroup)createSCGWithConsents(cp);
					
			TissueSpecimen ts =(TissueSpecimen) BaseTestCaseUtility.initTissueSpecimen();
			SpecimenPosition spPos = new SpecimenPosition();
			spPos.setStorageContainer(storageContainer);
			spPos.setSpecimen(ts);
			spPos.setPositionDimensionOne(new Integer(1));
			spPos.setPositionDimensionTwo(new Integer(1));
			ts.setSpecimenPosition(spPos);
			ts.setSpecimenCollectionGroup(scg);
			ts.setLabel("CPTisSpec"+UniqueKeyGeneratorUtil.getUniqueKey());
			System.out.println("Befor creating Tissue Specimen");
			try{
			ts = (TissueSpecimen) appService.createObject(ts);
			System.out.println("CPTissueSpec:"+ts.getLabel());	
			}catch(Exception e){
				Logger.out.error(e.getMessage(),e);
				e.printStackTrace();
				assertFalse("Failed to create Tissue specimen", true);
			}
			
	}
	
	
	public void testAddSpecimenInStorageContainerHavingCPRestrictionNegativeTest()
	{
		
			StorageContainer storageContainer =(StorageContainer) TestCaseUtility.getObjectMap(StorageContainer.class);
			CollectionProtocol cp = BaseTestCaseUtility.initCollectionProtocol();
			try{
				cp = (CollectionProtocol) appService.createObject(cp);
			}
			catch(Exception e){
				Logger.out.error(e.getMessage(),e);
	           	e.printStackTrace();
	           	assertFalse("Failed to create collection protocol", true);
			}
			TestCaseUtility.setObjectMap(cp, CollectionProtocol.class);
			System.out.println("CP:"+cp.getTitle());
			SpecimenCollectionGroup scg= (SpecimenCollectionGroup)createSCGWithConsents(cp);
							
			TissueSpecimen ts =(TissueSpecimen) BaseTestCaseUtility.initTissueSpecimen();
			SpecimenPosition spPos = new SpecimenPosition();
			spPos.setStorageContainer(storageContainer);
			spPos.setSpecimen(ts);
			spPos.setPositionDimensionOne(new Integer(1));
			spPos.setPositionDimensionTwo(new Integer(2));
			ts.setSpecimenPosition(spPos);
			ts.setSpecimenCollectionGroup(scg);
			ts.setLabel("WithDiffCPTisSpec"+UniqueKeyGeneratorUtil.getUniqueKey());
			System.out.println("Befor creating Tissue Specimen");
			
			try{
				ts = (TissueSpecimen) appService.createObject(ts);
				System.out.println("TissueSpec:"+ts.getLabel());
				assertFalse("Successfully created specimen", true);
			}
			catch(Exception e){
				assertTrue("Failed to add Specimen in container which has restriction to hold diff. CP", true);
			}
				
			
		}
	
	public SpecimenCollectionGroup createSCGWithConsents(CollectionProtocol cp)
	{
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
			assertFalse("Failed to registration date", true);
		}
		collectionProtocolRegistration.setSignedConsentDocumentURL("F:/doc/consentDoc.doc");
		User user = (User)TestCaseUtility.getObjectMap(User.class);
		collectionProtocolRegistration.setConsentWitness(user);
		
		Collection consentTierResponseCollection = new HashSet();
		Collection consentTierCollection = cp.getConsentTierCollection();
	
//		ConsentTierResponse r1 = new ConsentTierResponse();
//		r1.setResponse("Yes");
//		consentTierResponseCollection.add(r1);
//		ConsentTierResponse r2 = new ConsentTierResponse();
//		r2.setResponse("No");
//		consentTierResponseCollection.add(r2);
//		ConsentTierResponse r3 = new ConsentTierResponse();
//		r3.setResponse("Not Applicable");
//		consentTierResponseCollection.add(r3);
//		
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
		Site site = (Site) TestCaseUtility.getObjectMap(Site.class);
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
           	assertFalse("Failed to register participant", true);
		}		
		return scg;		
	}
	  
	
	
	

	public void createStorageTypes()
	{
		box = new StorageType();
		Capacity capacity = new Capacity();
		box.setName("Box" + UniqueKeyGeneratorUtil.getUniqueKey());
		box.setDefaultTempratureInCentigrade(new Double(-30));
		box.setOneDimensionLabel("label 1");
		box.setTwoDimensionLabel("label 2");
		capacity.setOneDimensionCapacity(new Integer(5));
		capacity.setTwoDimensionCapacity(new Integer(3));
		box.setCapacity(capacity);
		box.setActivityStatus("Active");
		Collection holdsSpecimenClassCollection = new HashSet();
		holdsSpecimenClassCollection.add("Tissue");
		holdsSpecimenClassCollection.add("Fluid");
		holdsSpecimenClassCollection.add("Molecular");
		holdsSpecimenClassCollection.add("Cell");
		box.setHoldsSpecimenClassCollection(holdsSpecimenClassCollection);
		
		try{
			box = (StorageType) appService.createObject(box);
		}
		catch(Exception e){
			assertFalse("",true);
		}
		
		rack = new StorageType();
		Capacity rackCapacity = new Capacity();
		rack.setName("Rack" + UniqueKeyGeneratorUtil.getUniqueKey());
		rack.setDefaultTempratureInCentigrade(new Double(-30));
		rack.setOneDimensionLabel("label 1");
		rack.setTwoDimensionLabel("label 2");
		rackCapacity.setOneDimensionCapacity(new Integer(5));
		rackCapacity.setTwoDimensionCapacity(new Integer(4));
		rack.setCapacity(rackCapacity);
		rack.setActivityStatus("Active");
		Collection holdsBox = new HashSet();
		holdsBox.add(box);
		rack.setHoldsStorageTypeCollection(holdsBox);
		
		try{
			rack = (StorageType) appService.createObject(rack);
		}
		catch(Exception e){
			assertFalse("",true);
		}
		
		
		freezer = new StorageType();
		Capacity freezerCapacity = new Capacity();
		freezer.setName("Freezer" + UniqueKeyGeneratorUtil.getUniqueKey());
		freezer.setDefaultTempratureInCentigrade(new Double(-30));
		freezer.setOneDimensionLabel("label 1");
		freezer.setTwoDimensionLabel("label 2");
		freezerCapacity.setOneDimensionCapacity(new Integer(5));
		freezerCapacity.setTwoDimensionCapacity(new Integer(4));
		freezer.setCapacity(freezerCapacity);
		freezer.setActivityStatus("Active");
		Collection holdsRack = new HashSet();
		holdsRack.add(rack);
		freezer.setHoldsStorageTypeCollection(holdsRack);
		
		try{
			freezer = (StorageType) appService.createObject(freezer);
		}
		catch(Exception e){
			assertFalse("",true);
		}
		
	}
	
	public void createStorageContainers()
	{
		freezerContainer = new StorageContainer();
		freezerContainer.setStorageType(freezer);
		Site site = (Site) TestCaseUtility.getObjectMap(Site.class);
		freezerContainer.setSite(site);
		freezerContainer.setNoOfContainers(3);
		freezerContainer.setActivityStatus("Active");
		Capacity capacity1 = new Capacity();
		capacity1.setOneDimensionCapacity(new Integer(3));
		capacity1.setTwoDimensionCapacity(new Integer(3));
		freezerContainer.setCapacity(capacity1);
		freezerContainer.setFull(Boolean.valueOf(false));
		Collection holdsRackCollection = new HashSet();
		holdsRackCollection.add(rack);
		freezerContainer.setHoldsStorageTypeCollection(holdsRackCollection);
		
		try{
			freezerContainer = (StorageContainer) appService.createObject(freezerContainer);
		}
		catch(Exception e){
			assertFalse("",true);
		}
		
		
		rackContainer = new StorageContainer();
		rackContainer.setStorageType(rack);
		
		ContainerPosition cp = new ContainerPosition();
		cp.setParentContainer(freezerContainer);	
		cp.setPositionDimensionOne(new Integer(1));
		cp.setPositionDimensionOne(new Integer(1));
		cp.setOccupiedContainer(rackContainer);
		rackContainer.setLocatedAtPosition(cp);
		rackContainer.setNoOfContainers(3);
		rackContainer.setActivityStatus("Active");
		rackContainer.setCapacity(capacity1);
		rackContainer.setFull(Boolean.valueOf(false));
		Collection holdsBoxCollection = new HashSet();
		holdsBoxCollection.add(box);
		rackContainer.setHoldsStorageTypeCollection(holdsBoxCollection);
		try{
			rackContainer = (StorageContainer) appService.createObject(rackContainer);
		}
		catch(Exception e){
			assertFalse("",true);
		}
		

		boxContainer = new StorageContainer();
		boxContainer.setStorageType(box);
		cp.setParentContainer(rackContainer);
		cp.setOccupiedContainer(boxContainer);
		cp.setPositionDimensionOne(new Integer(1));
		cp.setPositionDimensionOne(new Integer(2));
		boxContainer.setLocatedAtPosition(cp);
		boxContainer.setActivityStatus("Active");
		boxContainer.setCapacity(capacity1);
		boxContainer.setFull(Boolean.valueOf(false));
		try{
			boxContainer = (StorageContainer) appService.createObject(boxContainer);
			assertTrue("created box SC",true);
		}
		catch(Exception e){
			assertFalse("Should not allow",true);
		}
		
	}
	/*

	public void testAddBoxInRackContainerPositiveTestcase()
	{
		createStorageTypes();
		createStorageContainers();
		StorageContainer sc = new StorageContainer();
		sc.setStorageType(box);
		
		ContainerPosition cp = new ContainerPosition();
		cp.setParentContainer(rackContainer);
		cp.setPositionDimensionOne(new Integer(1));
		cp.setPositionDimensionOne(new Integer(1));
		cp.setOccupiedContainer(sc);
		sc.setLocatedAtPosition(cp);
		
		sc.setNoOfContainers(1);
		sc.setActivityStatus("Active");
		Capacity capacity1 = new Capacity();
		capacity1.setOneDimensionCapacity(new Integer(3));
		capacity1.setTwoDimensionCapacity(new Integer(3));
		sc.setCapacity(capacity1);
		sc.setFull(Boolean.valueOf(false));
			
		try{
			sc = (StorageContainer) appService.createObject(sc);
			assertTrue("Successfully added box container in rack container",true);
		}catch(Exception e){
			 Logger.out.error(e.getMessage(),e);
			 e.printStackTrace();
			 assertFalse("Failed to add the box container in rack container ",true);
			
		}
	}
	
	public void testAddBoxInFreezerContainerNegativeTestCase()
	{
		createStorageTypes();
		createStorageContainers();
		StorageContainer sc = new StorageContainer();
		sc.setStorageType(box);
	//	sc.setParent(freezerContainer);
		
		ContainerPosition cp = new ContainerPosition();
		cp.setPositionDimensionOne(new Integer(1));
		cp.setPositionDimensionOne(new Integer(2));
		cp.setParentContainer(freezerContainer);
		cp.setOccupiedContainer(sc);
		sc.setLocatedAtPosition(cp);
		
		sc.setNoOfContainers(1);
		sc.setActivityStatus("Active");
		Capacity capacity1 = new Capacity();
		capacity1.setOneDimensionCapacity(new Integer(3));
		capacity1.setTwoDimensionCapacity(new Integer(3));
		sc.setCapacity(capacity1);
		sc.setFull(Boolean.valueOf(false));
			
		try{
			sc = (StorageContainer) appService.createObject(sc);
			assertFalse("Successfully added box container in freezer container",true);
		}
		catch(Exception e){
			 Logger.out.error(e.getMessage(),e);
			 e.printStackTrace();
			 assertTrue("Shoudnot allow to create container ",true);
			
		}
		
	} 
	*/
  }
