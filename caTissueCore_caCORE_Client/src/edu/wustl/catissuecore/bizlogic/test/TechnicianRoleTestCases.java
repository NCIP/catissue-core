package edu.wustl.catissuecore.bizlogic.test;

import java.util.Iterator;
import java.util.List;

import edu.wustl.catissuecore.domain.Biohazard;
import edu.wustl.catissuecore.domain.CancerResearchGroup;
import edu.wustl.catissuecore.domain.CellSpecimen;
import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.catissuecore.domain.Department;
import edu.wustl.catissuecore.domain.DistributionProtocol;
import edu.wustl.catissuecore.domain.FluidSpecimen;
import edu.wustl.catissuecore.domain.Institution;
import edu.wustl.catissuecore.domain.MolecularSpecimen;
import edu.wustl.catissuecore.domain.Participant;
import edu.wustl.catissuecore.domain.Site;
import edu.wustl.catissuecore.domain.SpecimenArray;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.catissuecore.domain.TissueSpecimen;
import edu.wustl.common.test.BaseTestCase;
import edu.wustl.common.util.logger.Logger;
import gov.nih.nci.system.applicationservice.ApplicationService;
import gov.nih.nci.system.applicationservice.ApplicationServiceProvider;
import gov.nih.nci.system.comm.client.ClientSession;

public class TechnicianRoleTestCases extends BaseTestCase {
	 static ApplicationService appService = null;
	  public void setUp(){
		 Logger.configure("");
		appService = ApplicationServiceProvider.getApplicationService();
		ClientSession cs = ClientSession.getInstance();
		try
		{ 
			cs.startSession("technician@admin.com", "Test123");
		} 	
					
		catch (Exception ex) 
		{ 
			System.out.println(ex.getMessage()); 
			ex.printStackTrace();
			fail("Fail to create connection");
			System.exit(1);
		}		
	}
     
   public void testAddDepartmentWithTechnicianLogin()
 	  {
 		try{
 			Department dept =(Department) BaseTestCaseUtility.initDepartment();			
 			dept = (Department) appService.createObject(dept);
 			System.out.println("Object created successfully");
 			assertFalse("Test failed.Dept successfully added", true);
 		 }
 		 catch(Exception e){
 		     Logger.out.error(e.getMessage(),e);
 			 e.printStackTrace();
 			 assertTrue("Access denied: You are not authorized to perform this operation. ", true);
 		 }
 	  }
   public void testUpdateDepartmentWithTechnicianLogin()
	{ 	
	    try 
		{
	    	Department department = (Department) TestCaseUtility.getObjectMap(Department.class);
	    	BaseTestCaseUtility.updateDepartment(department);	    
	      	Logger.out.info("updating domain object------->"+department);
	    	Department updatedDepartment = (Department) appService.updateObject(department);
	       	Logger.out.info("Domain object successfully updated ---->"+updatedDepartment);
	        assertFalse("Test failed.Dept successfully updated", true);
	    } 
	    catch (Exception e) {
	       	Logger.out.error(e.getMessage(),e);
	 		e.printStackTrace();
	 		assertTrue("Access denied: You are not authorized to perform this operation. ", true);
	    }
	}
    
   public void testAddInstitutionWithTechnicianLogin()
	{
		try{
			Institution institution = BaseTestCaseUtility.initInstitution();
			System.out.println(institution);
			institution = (Institution) appService.createObject(institution); 
			System.out.println("Object created successfully");
			assertFalse("Test failed.Inst successfully added", true);
		 }
		 catch(Exception e){
			 e.printStackTrace();
			 assertTrue("Access denied: You are not authorized to perform this operation. ", true);
		 }
	}
    
  public void testUpdateInstitutionWithTechnicianLogin()
	{		
    	
	    try 
		{
	    	Institution institution = (Institution) TestCaseUtility.getObjectMap(Institution.class);
	    	BaseTestCaseUtility.updateInstitution(institution);	  
	    	Logger.out.info("updating domain object------->"+institution);
	    	Institution updatedInst = (Institution) appService.updateObject(institution);
	       	Logger.out.info("Domain object successfully updated ---->"+updatedInst);
	        assertFalse("Test failed.Inst successfully updated", true);
	    } 
	    catch (Exception e) {
	       	Logger.out.error(e.getMessage(),e);
	 		e.printStackTrace();
	 		 assertTrue("Access denied: You are not authorized to perform this operation. ", true);
	    }
	}
  public void testAddCancerResearchGrpWithTechnicianLogin()
	{
		try{
			CancerResearchGroup crg = BaseTestCaseUtility.initCancerResearchGrp();			
			crg = (CancerResearchGroup) appService.createObject(crg); 
			System.out.println("Object created successfully");
			assertFalse("Test failed.CRG successfully added", true);
		 }
		 catch(Exception e){
			 e.printStackTrace();
			 assertTrue("Access denied: You are not authorized to perform this operation. ", true);
		 }
	}
    
  public void testUpdateCancerResearchGrpWithTechnicianLogin()
	{
  
	    try 
		{
	    	CancerResearchGroup crg = (CancerResearchGroup) TestCaseUtility.getObjectMap(CancerResearchGroup.class);
	    	BaseTestCaseUtility.updateCancerResearchGrp(crg);	    	
	    	Logger.out.info("updating domain object------->"+crg);
	    	CancerResearchGroup updatedCRG = (CancerResearchGroup) appService.updateObject(crg);
	       	Logger.out.info("Domain object successfully updated ---->"+updatedCRG);
	        assertFalse("Test failed.CRG successfully updated", true);
	    } 
	    catch (Exception e) {
	       	Logger.out.error(e.getMessage(),e);
	 		e.printStackTrace();
	 		assertTrue("Access denied: You are not authorized to perform this operation. ", true);
	    }
	} 
    
     
  public void testAddSiteWithTechnicianLogin()
	{
		try{
			Site site= BaseTestCaseUtility.initSite();			
			System.out.println(site);
			site = (Site) appService.createObject(site); 
			System.out.println("Object created successfully");
			assertFalse("Test failed.Site successfully added", true);
		 }
		 catch(Exception e){
			 e.printStackTrace();
		     assertTrue("Access denied: You are not authorized to perform this operation. ", true);
		 }
	}
   public void testUpdateSiteWithTechnicianLogin()
	{
		try 
		{
	    	Site site = (Site) TestCaseUtility.getObjectMap(Site.class);
	    	BaseTestCaseUtility.updateSite(site);
	    	Logger.out.info("updating domain object------->"+site);
	    	Site updatedSite = (Site) appService.updateObject(site);
	       	Logger.out.info("Domain object successfully updated ---->"+updatedSite);
	        assertFalse("Test failed.Site successfully updated", true);
	    } 
	    catch (Exception e) {
	       	Logger.out.error(e.getMessage(),e);
	 		e.printStackTrace();
	 		assertTrue("Access denied: You are not authorized to perform this operation. ", true);
	    }
	}
   
    public void testAddBioHazardWithTechnicianLogin()
	{
		try{
			Biohazard biohazard= BaseTestCaseUtility.initBioHazard();			
			System.out.println(biohazard);
			biohazard = (Biohazard) appService.createObject(biohazard); 
			System.out.println("Object created successfully");
			Logger.out.info(" Domain Object added successfully");
			assertFalse("Test failed.Biohazard successfully added", true);
		 }
		 catch(Exception e){
			 e.printStackTrace();
			 assertTrue("Access denied: You are not authorized to perform this operation. ", true);
		 }
	}
   public void testUpdateBioHazardWithTechnicianLogin()
	{		  
	    try 
		{
	    	Biohazard biohazard = (Biohazard) TestCaseUtility.getObjectMap(Biohazard.class);
	    	BaseTestCaseUtility.updateBiohazard(biohazard);	
	    	Logger.out.info("updating domain object------->"+biohazard);
	    	Biohazard updatedBiohazard = (Biohazard) appService.updateObject(biohazard);
	       	Logger.out.info("Domain object successfully updated ---->"+updatedBiohazard);
	       	assertFalse("Test failed.Biohazard successfully updated", true);
	    } 
	    catch (Exception e) {
	       	Logger.out.error(e.getMessage(),e);
	 		e.printStackTrace();
	 		assertTrue("Access denied: You are not authorized to perform this operation. ", true);
	    }
	}
   
    public void testAddCollectionProtocolWithTechnicianLogin()
	{
		try
		 {
			CollectionProtocol collectionProtocol = BaseTestCaseUtility.initCollectionProtocol();			
			collectionProtocol = (CollectionProtocol) appService.createObject(collectionProtocol);
			TestCaseUtility.setObjectMap(collectionProtocol, CollectionProtocol.class);
			assertFalse("Test failed.Collection Protocol successfully added", true);
		 }
		 catch(Exception e)
		 {
			 Logger.out.error(e.getMessage(),e);
			 e.printStackTrace();
			 assertTrue("Access denied: You are not authorized to perform this operation. ", true);
		 }
	}
   
   public void testUpdateCollectionProtocolWithTechnicianLogin()
	{
	    try 
		{
	    	CollectionProtocol collectionProtocol = (CollectionProtocol) TestCaseUtility.getObjectMap(CollectionProtocol.class);
		   	Logger.out.info("updating domain object------->"+collectionProtocol);
	    	BaseTestCaseUtility.updateCollectionProtocol(collectionProtocol);
	    	CollectionProtocol updatedCollectionProtocol = (CollectionProtocol)appService.updateObject(collectionProtocol);
	    	Logger.out.info("Domain object successfully updated ---->"+updatedCollectionProtocol);
	    	assertFalse("Test failed.Collection Protocol successfully added", true);
	    } 
	    catch (Exception e)
	    {
	    	Logger.out.error(e.getMessage(),e);
	    	e.printStackTrace();
	    	assertTrue("Access denied: You are not authorized to perform this operation. ", true);
	    }
	}
   
   public void testAddDistributionProtocolWithTechnicianLogin()
	{
		try{
			DistributionProtocol distributionprotocol = BaseTestCaseUtility.initDistributionProtocol();			
			System.out.println(distributionprotocol);
			distributionprotocol = (DistributionProtocol) appService.createObject(distributionprotocol);
			System.out.println("Object created successfully");			
			assertFalse("Test failed.Distribution Protocol successfully added", true);
		 }
		 catch(Exception e){
			 e.printStackTrace();
			 assertTrue("Access denied: You are not authorized to perform this operation. ", true);
		 }
	}
   
   public void testUpdateDistributionProtocolWithTechnicianLogin()
	{
	    try 
	  	{
	    	DistributionProtocol distributionProtocol = (DistributionProtocol) TestCaseUtility.getObjectMap(DistributionProtocol.class);
	    	Logger.out.info("updating domain object------->"+distributionProtocol);
	    	BaseTestCaseUtility.updateDistributionProtocol(distributionProtocol);	    	
	    	DistributionProtocol updatedDistributionProtocol = (DistributionProtocol) appService.updateObject(distributionProtocol);
	       	Logger.out.info("Domain object successfully updated ---->"+updatedDistributionProtocol);
	       	assertFalse("Test failed.Distribution Protocol successfully updated", true);
	    } 
	    catch (Exception e) {
	       	Logger.out.error(e.getMessage(),e);
	 		e.printStackTrace();
	 		assertTrue("Access denied: You are not authorized to perform this operation. ", true);
	    }
	} 
   public void testSearchDepartmetWithTechnicianLogin()
	{
		Department dept = new Department();
     	Logger.out.info(" searching domain object");
   	     dept.setId(new Long(1));
         try {
       	 List resultList = appService.search(Department.class,dept);
       	 for (Iterator resultsIterator = resultList.iterator(); resultsIterator.hasNext();) 
       	 {
       		 Department returnedDepartment = (Department) resultsIterator.next();
       		 Logger.out.info(" Domain Object is successfully Found ---->  :: " + returnedDepartment.getName());
       	 }
         } 
         catch (Exception e) {
          	Logger.out.error(e.getMessage(),e);
          	e.printStackTrace();
          	assertFalse("Does not find Domain Object", true);	 		
         }
	}
   public void testSearchCancerResearchGrpWithTechnicianLogin()
	{
			CancerResearchGroup crg = new CancerResearchGroup();
	     	Logger.out.info(" searching domain object");
	    	crg.setId(new Long(1));
	   
	         try {
	        	 List resultList = appService.search(CancerResearchGroup.class,crg);
	        	 for (Iterator resultsIterator = resultList.iterator(); resultsIterator.hasNext();) {
	        		 CancerResearchGroup returnedInst = (CancerResearchGroup) resultsIterator.next();
	        		 Logger.out.info(" Domain Object is successfully Found ---->  :: " + returnedInst.getName());
	        		// System.out.println(" Domain Object is successfully Found ---->  :: " + returnedDepartment.getName());
	             }
	          } 
	          catch (Exception e) {
	           	Logger.out.error(e.getMessage(),e);
	           	e.printStackTrace();
	           	assertFalse("Does not find Domain Object", true);
		 		
	          }
	}
   public void testSearchInstitutionWithTechnicianLogin()
	{
		Institution institution = new Institution();
   	Logger.out.info(" searching domain object");
   	institution.setId(new Long(1));
  
        try {
       	 List resultList = appService.search(Institution.class,institution);
       	 for (Iterator resultsIterator = resultList.iterator(); resultsIterator.hasNext();) {
       		 Institution returnedInst = (Institution) resultsIterator.next();
       		 Logger.out.info(" Domain Object is successfully Found ---->  :: " + returnedInst.getName());
       		// System.out.println(" Domain Object is successfully Found ---->  :: " + returnedDepartment.getName());
            }
         } 
         catch (Exception e) {
          	Logger.out.error(e.getMessage(),e);
          	e.printStackTrace();
          	assertFalse("Does not find Domain Object", true);
	 		
         }
	}
   public void testSearchSiteWithTechnicianLogin()
	{
		Site site = new Site();
   	Logger.out.info(" searching domain object");
   	site.setId(new Long(1));
        try {
       	 List resultList = appService.search(Site.class,site);
       	 for (Iterator resultsIterator = resultList.iterator(); resultsIterator.hasNext();) {
       		 Site returnedSite = (Site) resultsIterator.next();
       		 Logger.out.info(" Domain Object is successfully Found ---->  :: " + returnedSite.getName());
       		// System.out.println(" Domain Object is successfully Found ---->  :: " + returnedDepartment.getName());
            }
         } 
         catch (Exception e) {
          	Logger.out.error(e.getMessage(),e);
          	e.printStackTrace();
          	assertFalse("Does not find Domain Object", true);
	 		
         }
	}
 /*public void testSearchBioHazard()
	{
       try {
       //	Biohazard cachedBiohazard = (Biohazard) TestCaseUtility.getObjectMap(Biohazard.class);
    	Biohazard biohazard =  new Biohazard();
   	   	Logger.out.info("searching domain object");
       	biohazard.setId(new Long(1));
       	List resultList = appService.search(Biohazard.class,biohazard);
       	 for (Iterator resultsIterator = resultList.iterator(); resultsIterator.hasNext();) 
       	 {
	    		 Biohazard returnedBiohazard = (Biohazard) resultsIterator.next();
	    		 Logger.out.info(" Domain Object is successfully Found ---->  :: " + returnedBiohazard.getName());
	    		 System.out.println(" Domain Object is successfully Found ---->  :: " + returnedBiohazard.getName());
	    		 assertTrue("Object added successfully", true);
            }
         } 
         catch (Exception e) {
          	Logger.out.error(e.getMessage(),e);
           System.out.println(e.getMessage());
          	e.printStackTrace();
          	assertFalse("Does not find Domain Object", true);
	 		
         }
	}*/
 public void testSearchCollectionProtocolWithTechnicianLogin()
	{
   	CollectionProtocol collectionProtocol = new CollectionProtocol();
   	CollectionProtocol cachedCollectionProtocol = (CollectionProtocol) TestCaseUtility.getObjectMap(CollectionProtocol.class);
   	cachedCollectionProtocol.setId((Long) cachedCollectionProtocol.getId());
      	Logger.out.info(" searching domain object");
   	try {
       	// collectionProtocol = (CollectionProtocol) appService.createObject(collectionProtocol);
       	 List resultList = appService.search(CollectionProtocol.class,collectionProtocol);
       	 for (Iterator resultsIterator = resultList.iterator(); resultsIterator.hasNext();)
       	 {
       		 CollectionProtocol returnedcollectionprotocol = (CollectionProtocol) resultsIterator.next();
       		 Logger.out.info(" Domain Object is successfully Found ---->  :: " + returnedcollectionprotocol.getTitle());
            }
         } 
         catch (Exception e) {
       	Logger.out.error(e.getMessage(),e);
	 		e.printStackTrace();
	 		//assertFalse("Doesnot found collection protocol", true);
	 		fail("Doesnot found collection protocol");
         }
	}
 
 public void testSearchDistributionProtocolWithTechnicianLogin()
	{
		try {		
		    DistributionProtocol distributionProtocol = new DistributionProtocol();
			DistributionProtocol cachedDistributionProtocol = new DistributionProtocol();
			Logger.out.info(" searching domain object");
	    	distributionProtocol.setId((Long) cachedDistributionProtocol.getId());
	        List resultList = appService.search(DistributionProtocol.class,distributionProtocol);
	         for (Iterator resultsIterator = resultList.iterator(); resultsIterator.hasNext();) {
	        		 DistributionProtocol returnedDP = (DistributionProtocol) resultsIterator.next();
	        		 Logger.out.info(" Domain Object is successfully Found ---->  :: " + returnedDP.getTitle());
	        		// System.out.println(" Domain Object is successfully Found ---->  :: " + returnedDepartment.getName());
	             }
	          } 
	          catch (Exception e) {
	           	Logger.out.error(e.getMessage(),e);
	           	e.printStackTrace();
	           	assertFalse("Does not find Domain Object", true);
		 		
	          }
	}
   
   public void testAddParticipantWithTechnicianLogin()
	{
		try{
			Participant participant= BaseTestCaseUtility.initParticipant();			
			System.out.println(participant);
			participant = (Participant) appService.createObject(participant); 
			TestCaseUtility.setObjectMap(participant, Participant.class);
			System.out.println("Object created successfully");
			assertFalse("Participant created successfully", true);			
		 }
		 catch(Exception e){
			 e.printStackTrace();
			 assertTrue("Failed to add participant", true);
			 
		 }
	}
  
   
   public void testUpdateParticipantWithTechnicianLogin()
	{
		Participant participant =  BaseTestCaseUtility.initParticipant();
		Logger.out.info("updating domain object------->"+participant);
	    try 
		{
	    	participant = (Participant) appService.createObject(participant);
	    	BaseTestCaseUtility.updateParticipant(participant);
	    	Participant updatedParticipant = (Participant) appService.updateObject(participant);
	       	Logger.out.info("Domain object successfully updated ---->"+updatedParticipant);
	       	assertFalse("Participant successfully updated ---->"+updatedParticipant, true);
	     } 
	    catch (Exception e) {
	       	Logger.out.error(e.getMessage(),e);
	 		e.printStackTrace();
	 		assertTrue("Failed to update Participant", true);
	 		
	    }
	}
   public void testSearchParticipantWithTechnicianLogin()
	{
		Participant participant = new Participant();
		Logger.out.info(" searching domain object");
		participant.setId(new Long(1));
  
        try {
       	 List resultList = appService.search(Participant.class,participant);
       	 for (Iterator resultsIterator = resultList.iterator(); resultsIterator.hasNext();) {
       		 Participant returnedParticipant = (Participant) resultsIterator.next();
       		 Logger.out.info(" Domain Object is successfully Found ---->  :: " + returnedParticipant.getFirstName() +" "+returnedParticipant.getLastName());
       		 }
         } 
         catch (Exception e) {
          	Logger.out.error(e.getMessage(),e);
          	e.printStackTrace();
          	assertFalse("Does not find Domain Object", true);
	 		
         }
	}
  
	 /* public void testCreateAndUpdateSpecimenCollectionGroupWithConsents()
		{
			CollectionProtocol collectionProtocol = (CollectionProtocol)TestCaseUtility.getObjectMap(CollectionProtocol.class);
			SpecimenCollectionGroup specimenCollGroup = createSCGWithConsents(collectionProtocol); 
			Participant participant = (Participant)TestCaseUtility.getObjectMap(Participant.class);
			Collection consentTierCollection = collectionProtocol.getConsentTierCollection();
			Iterator consentTierItr = consentTierCollection.iterator();
			Collection consentTierStatusCollection = new HashSet();
			while(consentTierItr.hasNext())
			{
				ConsentTier consentTier = (ConsentTier)consentTierItr.next();
				ConsentTierStatus consentStatus = new ConsentTierStatus();
				consentStatus.setConsentTier(consentTier);
				consentStatus.setStatus("Yes");
				consentTierStatusCollection.add(consentStatus);
			}
			specimenCollGroup.setConsentTierStatusCollection(consentTierStatusCollection);
			specimenCollGroup.getCollectionProtocolRegistration().getCollectionProtocol().setId(collectionProtocol.getId());
			specimenCollGroup.getCollectionProtocolRegistration().setParticipant(participant);
			Collection collectionProtocolEventList = new LinkedHashSet();
			
			
			Site site = (Site)TestCaseUtility.getObjectMap(Site.class); 
			specimenCollGroup.setSpecimenCollectionSite(site);
			BaseTestCaseUtility.setEventParameters(specimenCollGroup);
			try
			{
				System.out.println("Before Update");
				specimenCollGroup = (SpecimenCollectionGroup)appService.updateObject(specimenCollGroup);
				assertTrue("Specimen Collection Group Updated", true);
		
		   }catch(Exception e){				
				Logger.out.error(e.getMessage(),e);
				e.printStackTrace();
				assertFalse("Fail to update Specimen Collection Group", true);
			}
		}*/
		
		
   
   /*public void testAddTissueSpecimen()
	{
	   try {
		    CollectionProtocol cp= (CollectionProtocol) TestCaseUtility.getObjectMap(CollectionProtocol.class);
		    SpecimenCollectionGroup scg =  createSCGWithConsents(cp);	
		    TestCaseUtility.setObjectMap(scg, SpecimenCollectionGroup.class);
			TissueSpecimen specimenObj = (TissueSpecimen) BaseTestCaseUtility.initTissueSpecimen();				
			specimenObj.setSpecimenCollectionGroup(scg);
			Logger.out.info("Inserting domain object------->"+specimenObj);
			System.out.println("Before Creating Tissue Specimen");
			specimenObj =  (TissueSpecimen) appService.createObject(specimenObj);
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
			assertFalse("Failed to create Domain Object", true);
		}
	}
   public void testAddMolecularSpecimen()
	{
	   try {
		    MolecularSpecimen specimenObj = (MolecularSpecimen) BaseTestCaseUtility.initMolecularSpecimen();
		    SpecimenCollectionGroup scg = (SpecimenCollectionGroup) TestCaseUtility.getObjectMap(SpecimenCollectionGroup.class);
		    specimenObj.setSpecimenCollectionGroup(scg);
			Logger.out.info("Inserting domain object------->"+specimenObj);
			System.out.println("Before Creating Tissue Specimen");
			specimenObj =  (MolecularSpecimen) appService.createObject(specimenObj);
			TestCaseUtility.setObjectMap(specimenObj, MolecularSpecimen.class);
			System.out.println("Afer Creating Tissue Specimen");
			Logger.out.info(" Domain Object is successfully added ---->    ID:: " + specimenObj.getId().toString());
			Logger.out.info(" Domain Object is successfully added ---->    Name:: " + specimenObj.getLabel());
			assertTrue("Domain Object is successfully added ---->    Name:: " + specimenObj.getLabel(), true);		
	   
	   }
		catch(Exception e)
		{
			System.out.println("Exception thrown");
			System.out.println(e);
			Logger.out.error(e.getMessage(),e);
			e.printStackTrace();
			assertFalse("Failed to create Domain Object", true);
		}
	}
	
	public void testAddCellSpecimen()
	{
	   try {
		    CellSpecimen specimenObj = (CellSpecimen) BaseTestCaseUtility.initCellSpecimen();
		    SpecimenCollectionGroup scg = (SpecimenCollectionGroup) TestCaseUtility.getObjectMap(SpecimenCollectionGroup.class);
		    specimenObj.setSpecimenCollectionGroup(scg);
			Logger.out.info("Inserting domain object------->"+specimenObj);
			System.out.println("Before Creating Tissue Specimen");
			specimenObj =  (CellSpecimen) appService.createObject(specimenObj);
			TestCaseUtility.setObjectMap(specimenObj, CellSpecimen.class);
			System.out.println("Afer Creating Tissue Specimen");
			Logger.out.info(" Domain Object is successfully added ---->    ID:: " + specimenObj.getId().toString());
			Logger.out.info(" Domain Object is successfully added ---->    Name:: " + specimenObj.getLabel());
			assertTrue("Domain Object is successfully added ---->    Name:: " + specimenObj.getLabel(), true);		
	   
	   }
		catch(Exception e)
		{
			System.out.println("Exception thrown");
			System.out.println(e);
			Logger.out.error(e.getMessage(),e);
			e.printStackTrace();
			assertFalse("Failed to create Domain Object", true);
		}
	}
	public void testAddFluidSpecimen()
	{
	   try {
		    FluidSpecimen specimenObj = (FluidSpecimen) BaseTestCaseUtility.initFluidSpecimen();
		    SpecimenCollectionGroup scg = (SpecimenCollectionGroup) TestCaseUtility.getObjectMap(SpecimenCollectionGroup.class);
		    specimenObj.setSpecimenCollectionGroup(scg);
			Logger.out.info("Inserting domain object------->"+specimenObj);
			System.out.println("Before Creating Tissue Specimen");
			specimenObj =  (FluidSpecimen) appService.createObject(specimenObj);
			TestCaseUtility.setObjectMap(specimenObj, FluidSpecimen.class);
			System.out.println("Afer Creating Tissue Specimen");
			Logger.out.info(" Domain Object is successfully added ---->    ID:: " + specimenObj.getId().toString());
			Logger.out.info(" Domain Object is successfully added ---->    Name:: " + specimenObj.getLabel());
			assertTrue("Domain Object is successfully added ---->    Name:: " + specimenObj.getLabel(), true);		
	   
	   }
		catch(Exception e)
		{
			System.out.println("Exception thrown");
			System.out.println(e);
			Logger.out.error(e.getMessage(),e);
			e.printStackTrace();
			assertFalse("Failed to create Domain Object", true);
		}
	}
	
   public void testSearchSpecimen()
   {
		Specimen specimen = new TissueSpecimen();
		Specimen cachedSpecimen = (TissueSpecimen) TestCaseUtility.getObjectMap(TissueSpecimen.class);
		specimen.setId(cachedSpecimen.getId());
    	Logger.out.info(" searching domain object");
    	try {
       	 List resultList = appService.search(Specimen.class,specimen);
       	 for (Iterator resultsIterator = resultList.iterator(); resultsIterator.hasNext();) {
       		 Specimen returnedspecimen = (Specimen) resultsIterator.next();
       		 System.out.println("here-->" + returnedspecimen.getLabel() +"Id:"+returnedspecimen.getId());
       		 Logger.out.info(" Domain Object is successfully Found ---->  :: " + returnedspecimen.getLabel());
            }
       	 assertTrue("Specimen found", true);
         } 
         catch (Exception e) {
       	Logger.out.error(e.getMessage(),e);
	 		e.printStackTrace();
	 		assertFalse("Couldnot found Specimen", true);  
         }

   }
   
   
   
   
   public SpecimenCollectionGroup createSCGWithConsents(CollectionProtocol cp){
		
		Participant participant = BaseTestCaseUtility.initParticipant();
		
		try{
			participant = (Participant) appService.createObject(participant);
		}
		catch(Exception e){
			Logger.out.error(e.getMessage(),e);
          	e.printStackTrace();
          	assertFalse("Failed to create collection protocol", true);
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
		
		Collection consentTierResponseCollection = new LinkedHashSet();
		Collection consentTierCollection = new LinkedHashSet();
		consentTierCollection = cp.getConsentTierCollection();
		
		Iterator consentTierItr = consentTierCollection.iterator();
		 while(consentTierItr.hasNext())
		 {
			 ConsentTier consent= (ConsentTier) consentTierItr.next();
			 ConsentTierResponse response= new ConsentTierResponse();
			 response.setResponse("No");
			 response.setConsentTier(consent);
			 consentTierResponseCollection.add(response);				 
		 }
			
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
		TestCaseUtility.setObjectMap(collectionProtocolRegistration, CollectionProtocolRegistration.class);
		
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
 }*/
   
   public void testAddTissueSpecimenWithTechnicianWithTechnicianLogin()
	{
	   try {
		  
		   SpecimenCollectionGroup scg =(SpecimenCollectionGroup) TestCaseUtility.getObjectMap(SpecimenCollectionGroup.class);
			TissueSpecimen specimenObj = (TissueSpecimen) BaseTestCaseUtility.initTissueSpecimen();				
			specimenObj.setSpecimenCollectionGroup(scg);
			Logger.out.info("Inserting domain object------->"+specimenObj);
			System.out.println("Before Creating Tissue Specimen");
			specimenObj =  (TissueSpecimen) appService.createObject(specimenObj);
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
			assertFalse("Failed to create Domain Object", true);
		}
	}
   
   public void testAddMolecularSpecimenWithTechnicianLogin()
	{
	   try {
		    MolecularSpecimen specimenObj = (MolecularSpecimen) BaseTestCaseUtility.initMolecularSpecimen();
		    SpecimenCollectionGroup scg = (SpecimenCollectionGroup) TestCaseUtility.getObjectMap(SpecimenCollectionGroup.class);
		    specimenObj.setSpecimenCollectionGroup(scg);
			Logger.out.info("Inserting domain object------->"+specimenObj);
			System.out.println("Before Creating Tissue Specimen");
			specimenObj =  (MolecularSpecimen) appService.createObject(specimenObj);
			TestCaseUtility.setObjectMap(specimenObj, MolecularSpecimen.class);
			System.out.println("Afer Creating Tissue Specimen");
			Logger.out.info(" Domain Object is successfully added ---->    ID:: " + specimenObj.getId().toString());
			Logger.out.info(" Domain Object is successfully added ---->    Name:: " + specimenObj.getLabel());
			assertTrue("Domain Object is successfully added ---->    Name:: " + specimenObj.getLabel(), true);		
	   
	   }
		catch(Exception e)
		{
			System.out.println("Exception thrown");
			System.out.println(e);
			Logger.out.error(e.getMessage(),e);
			e.printStackTrace();
			assertFalse("Failed to create Domain Object", true);
		}
	}
	
	public void testAddCellSpecimenWithTechnicianLogin()
	{
	   try {
		    CellSpecimen specimenObj = (CellSpecimen) BaseTestCaseUtility.initCellSpecimen();
		    SpecimenCollectionGroup scg = (SpecimenCollectionGroup) TestCaseUtility.getObjectMap(SpecimenCollectionGroup.class);
		    specimenObj.setSpecimenCollectionGroup(scg);
			Logger.out.info("Inserting domain object------->"+specimenObj);
			System.out.println("Before Creating Tissue Specimen");
			specimenObj =  (CellSpecimen) appService.createObject(specimenObj);
			TestCaseUtility.setObjectMap(specimenObj, CellSpecimen.class);
			System.out.println("Afer Creating Tissue Specimen");
			Logger.out.info(" Domain Object is successfully added ---->    ID:: " + specimenObj.getId().toString());
			Logger.out.info(" Domain Object is successfully added ---->    Name:: " + specimenObj.getLabel());
			assertTrue("Domain Object is successfully added ---->    Name:: " + specimenObj.getLabel(), true);		
	   
	   }
		catch(Exception e)
		{
			System.out.println("Exception thrown");
			System.out.println(e);
			Logger.out.error(e.getMessage(),e);
			e.printStackTrace();
			assertFalse("Failed to create Domain Object", true);
		}
	}
	public void testAddFluidSpecimenWithTechnicianLogin()
	{
	   try {
		    FluidSpecimen specimenObj = (FluidSpecimen) BaseTestCaseUtility.initFluidSpecimen();
		    SpecimenCollectionGroup scg = (SpecimenCollectionGroup) TestCaseUtility.getObjectMap(SpecimenCollectionGroup.class);
		    specimenObj.setSpecimenCollectionGroup(scg);
			Logger.out.info("Inserting domain object------->"+specimenObj);
			System.out.println("Before Creating Tissue Specimen");
			specimenObj =  (FluidSpecimen) appService.createObject(specimenObj);
			TestCaseUtility.setObjectMap(specimenObj, FluidSpecimen.class);
			System.out.println("Afer Creating Tissue Specimen");
			Logger.out.info(" Domain Object is successfully added ---->    ID:: " + specimenObj.getId().toString());
			Logger.out.info(" Domain Object is successfully added ---->    Name:: " + specimenObj.getLabel());
			assertTrue("Domain Object is successfully added ---->    Name:: " + specimenObj.getLabel(), true);		
	   
	   }
		catch(Exception e)
		{
			System.out.println("Exception thrown");
			System.out.println(e);
			Logger.out.error(e.getMessage(),e);
			e.printStackTrace();
			assertFalse("Failed to create Domain Object", true);
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
			assertFalse("Security Alert! Authorization denied for user to use container" , true);
			Logger.out.info(" Specimen Collection Group is successfully added ---->    ID:: " + specimenArray.getId().toString());
		}
		catch(Exception e)
		{
			Logger.out.error(e.getMessage(),e);
			e.printStackTrace();
			assertTrue("Able to create specimen array in container which supervisor don't have access", true);
		}
	}
	 
}
   
