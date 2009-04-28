package edu.wustl.catissuecore.bizlogic.test;

import java.util.List;

import edu.wustl.catissuecore.domain.CollectionProtocolRegistration;
import edu.wustl.catissuecore.domain.Participant;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.common.util.logger.Logger;
import gov.nih.nci.system.applicationservice.ApplicationService;
import gov.nih.nci.system.applicationservice.ApplicationServiceProvider;
import gov.nih.nci.system.comm.client.ClientSession;

public class ScientistRoleTestCases extends CaTissueBaseTestCase {
		 static ApplicationService appService = null;
		  public void setUp(){
			appService = ApplicationServiceProvider.getApplicationService();
			System.out.println("appService !!!!"+appService);
			ClientSession cs = ClientSession.getInstance();
			System.out.println("cs !!!!"+cs);
			//System.setProperty("javax.net.ssl.trustStore", "E://jboss//server//default//conf//chap8.keystore");
			try
			{ 
				cs.startSession("scientist@admin.com", "Test123");
				System.out.println("Session started!!!!");
			} 	
						
			catch (Exception ex) 
			{ 
				System.out.println(ex.getMessage()); 
				ex.printStackTrace();
				fail("Fail to create connection");
				System.exit(1);
			}		
		}
		 /**
		  * Search all particpant and check if PHI data is visible
		  *
		  */ 
		  public void testSearchParticipantWithScientistLogin()
		  {
			try{
				Participant p = new Participant();
				List l = appService.search(Participant.class.getName(), p);
				System.out.println("Size : "+l.size());
				for(int i=0;i<l.size();i++)
				{
					Participant retutnParticpant = (Participant)l.get(i);
					if(retutnParticpant.getFirstName()!=null||retutnParticpant.getLastName()!=null||
							retutnParticpant.getMiddleName()!=null||retutnParticpant.getBirthDate()!=null||
							retutnParticpant.getSocialSecurityNumber()!=null)
					{
						fail("Participant PHI data is visible to scientist");
					}
						
				}
				
			 }
			 catch(Exception e){
				 System.out
						.println("ScientistRoleTestCases.testAddDepartmentWithScientistLogin() "+e.getMessage());
			     Logger.out.error(e.getMessage(),e);
				 e.printStackTrace();
				 assertFalse("Test failed. to search Particpant", true);
			 }
		  }
		  /**
			  * Search all scg and check if PHI data is visible
			  *
			  */ 
			  public void testSearchProtocolRegistrationWithScientistLogin()
			  {
				try{
					CollectionProtocolRegistration reg = new CollectionProtocolRegistration();
					List l = appService.search(CollectionProtocolRegistration.class.getName(), reg);
					System.out.println("Size : "+l.size());
					for(int i=0;i<l.size();i++)
					{
						CollectionProtocolRegistration returnedReg = (CollectionProtocolRegistration)l.get(i);
						if(returnedReg.getBarcode()!=null||returnedReg.getRegistrationDate()!=null||
								returnedReg.getSignedConsentDocumentURL()!=null||returnedReg.getConsentSignatureDate()!=null||
								returnedReg.getConsentWitness()!=null)
						{
							fail("CollectionProtocolRegistration PHI data is visible to scientist");
						}
					}
				 }
				 catch(Exception e){
					 System.out
							.println("ScientistRoleTestCases.testSearchProtocolRegistrationWithScientistLogin() "+e.getMessage());
				     Logger.out.error(e.getMessage(),e);
					 e.printStackTrace();
					 assertFalse("Test failed. to search SpecimenCollectionGroup", true);
				 }
			  }
		  /**
			  * Search all scg and check if PHI data is visible
			  *
			  */ 
			  public void testSearchSpecimenCollectionGroupWithScientistLogin()
			  {
				try{
					SpecimenCollectionGroup scg = new SpecimenCollectionGroup();
					List l = appService.search(SpecimenCollectionGroup.class.getName(), scg);
					System.out.println("Size : "+l.size());
					for(int i=0;i<l.size();i++)
					{
						SpecimenCollectionGroup returnedSCG = (SpecimenCollectionGroup)l.get(i);
						if(returnedSCG.getSurgicalPathologyNumber()!=null)
						{
							fail("SpecimenCollectionGroup PHI data is visible to scientist");
						}
					}
				 }
				 catch(Exception e){
					 System.out
							.println("ScientistRoleTestCases.testSearchSpecimenCollectionGroupWithScientistLogin() "+e.getMessage());
				     Logger.out.error(e.getMessage(),e);
					 e.printStackTrace();
					 assertFalse("Test failed. to search SpecimenCollectionGroup", true);
				 }
			  }
	  
		  
/*
	  public void testAddDepartmentWithScientistLogin()
		  {
			try{
				Department dept =(Department) BaseTestCaseUtility.initDepartment();			
				dept = (Department) appService.createObject(dept);
				System.out.println("Object created successfully");
				assertFalse("Test failed.Dept successfully added", true);
			 }
			 catch(Exception e){
				 System.out
						.println("ScientistRoleTestCases.testAddDepartmentWithScientistLogin() "+e.getMessage());
			     Logger.out.error(e.getMessage(),e);
				 e.printStackTrace();
				 assertTrue("Access denied: You are not authorized to perform this operation. ", true);
			 }
		  }

	  public void testUpdateDepartmentWithScientistLogin()
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
	   
	  public void testAddInstitutionWithScientistLogin()
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
	   
	 public void testUpdateInstitutionWithScientistLogin()
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
	 public void testAddCancerResearchGrpWithScientistLogin()
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
	   
	 public void testUpdateCancerResearchGrpWithScientistLogin()
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
	   
	 
	   
	 public void testAddSiteWithScientistLogin()
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
	  public void testUpdateSiteWithScientistLogin()
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
	  
	   public void testAddBioHazardWithScientistLogin()
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
	  public void testUpdateBioHazardWithScientistLogin()
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
	  
	   public void testAddCollectionProtocolWithScientistLogin()
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
	  
	  public void testUpdateCollectionProtocolWithScientistLogin()
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
	  
	  public void testAddDistributionProtocolWithScientistLogin()
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
	  
	  public void testUpdateDistributionProtocolWithScientistLogin()
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
	  public void testSearchDepartmetWithScientistLogin()
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
	  public void testSearchCancerResearchGrpWithScientistLogin()
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
	public void testSearchInstitutionWithScientistLogin()
		{
			Institution institution = new Institution();
		  	Logger.out.info(" searching domain object");
		  	institution.setId(new Long(1));
	 
	       try {
		      	List resultList = appService.search(Institution.class,institution);
		      	for (Iterator resultsIterator = resultList.iterator(); resultsIterator.hasNext();) {
		      	Institution returnedInst = (Institution) resultsIterator.next();
		      	Logger.out.info(" Domain Object is successfully Found ---->  :: " + returnedInst.getName());
		      	System.out.println(" Domain Object is successfully Found ---->  :: " + returnedInst.getName());
	           }
	        } 
	        catch (Exception e) {
	         	Logger.out.error(e.getMessage(),e);
	         	e.printStackTrace();
	         	assertFalse("Does not find Domain Object", true);
		 		
	        }
		}
	public void testSearchSiteWithScientistLogin()
		{
			Site site = new Site();
		  	Logger.out.info(" searching domain object");
		  	site.setId(new Long(1));
	       try {
		      	List resultList = appService.search(Site.class,site);
		      	for (Iterator resultsIterator = resultList.iterator(); resultsIterator.hasNext();) {
		      	Site returnedSite = (Site) resultsIterator.next();
		      	Logger.out.info(" Domain Object is successfully Found ---->  :: " + returnedSite.getName());
		      	System.out.println(" Domain Object is successfully Found ---->  :: " + returnedSite.getName());
	           }
	        } 
	        catch (Exception e) {
	         	Logger.out.error(e.getMessage(),e);
	         	e.printStackTrace();
	         	assertFalse("Does not find Domain Object", true);
		 		
	        }
		} 
	
	 public void testAddStorageTypeWithScientistLogin()
		{
			try{
				StorageType storagetype = BaseTestCaseUtility.initStorageType();			
				System.out.println(storagetype);
				storagetype = (StorageType) appService.createObject(storagetype);
				TestCaseUtility.setObjectMap(storagetype, StorageType.class);
				System.out.println("Object created successfully");	
				assertFalse("Test failed.StorageType successfully created", true);
			 }
			 catch(Exception e){
				 e.printStackTrace();
				 assertTrue("Access denied: You are not authorized to perform this operation. ", true);
			 }
		}
		
		public void testSearchStorageTypeScientistLogin()
		{
			StorageType getStoragetype = (StorageType)TestCaseUtility.getObjectMap(StorageType.class);
			StorageType storagetype = new StorageType();
			storagetype.setId(getStoragetype.getId());
	    	Logger.out.info(" searching domain object");
	    	try {
	        	 List resultList = appService.search(StorageType.class,storagetype);
	        	 StorageType returnedStorageType = (StorageType) resultList.get(0);
	        	 Logger.out.info(" Domain Object is successfully Found ---->  :: " + returnedStorageType.getName());
	        	 System.out.println(" Domain Object is successfully Found ---->  :: " + returnedStorageType.getName());
	          } 
	          catch (Exception e) {
	           	Logger.out.error(e.getMessage(),e);
	           	e.printStackTrace();
	        	assertFalse("Does not find Storage Type ", true);	
	        }
		}
		
		public void testUpdateStorageTypeScientistLogin()
		{
			StorageType getStorageType = (StorageType) TestCaseUtility.getObjectMap(StorageType.class);
			StorageType storagetype =  new  StorageType();
			storagetype.setId(getStorageType.getId());
	    	Logger.out.info("updating domain object------->"+storagetype);
		    try 
			{
		    	List resultList = appService.search(StorageType.class,storagetype);
	        	StorageType returnedStorageType = (StorageType) resultList.get(0);
		    	BaseTestCaseUtility.updateStorageType(returnedStorageType);	
		    	StorageType updatedStorageType = (StorageType) appService.updateObject(returnedStorageType);
		       	Logger.out.info("Domain object successfully updated ---->"+updatedStorageType);
		       	assertFalse("Storage Type object successfully updated with supervisor login ---->"+updatedStorageType, true);
		    } 
		    catch (Exception e) {
		       	Logger.out.error(e.getMessage(),e);
		 		e.printStackTrace();
		 		assertTrue("Access denied: You are not authorized to perform this operation. ", true);
		    }
		}
		
		public void testAddStorageContainerScientistLogin()
		{
			try{
				StorageContainer storageContainer= BaseTestCaseUtility.initStorageContainer();			
				System.out.println(storageContainer);
				storageContainer = (StorageContainer) appService.createObject(storageContainer); 
				TestCaseUtility.setObjectMap(storageContainer, StorageContainer.class);
				System.out.println("Object created successfully");
				assertFalse("Storage Container added successfully with supervisor login", true);
			 }
			 catch(Exception e){
				 e.printStackTrace();
				 assertTrue("Access denied: You are not authorized to perform this operation. ", true);
			 }
		}
		
		public void testSearchStorageContainerScientistLogin()
		{
			StorageContainer getStorageContainer =(StorageContainer) TestCaseUtility.getObjectMap(StorageContainer.class);
			StorageContainer storageContainer = new StorageContainer();
	    	Logger.out.info(" searching domain object");
	    	storageContainer.setId(getStorageContainer.getId());
	   
	         try {
	        	 List resultList = appService.search(StorageContainer.class,storageContainer);        	
	    		 StorageContainer returnedStorageContainer = (StorageContainer) resultList.get(0);
	    		 Logger.out.info(" Domain Object is successfully Found ---->  :: " 
	        				 + returnedStorageContainer.getName());
	            } 
	          catch (Exception e) {
	           	Logger.out.error(e.getMessage(),e);
	           	e.printStackTrace();
	           	assertFalse("Does not find Storage Container with Supervisor Login", true);
		 		
	          }
		}
		
		/*public void testUpdateStorageContainerScientistLogin()
		{
			StorageContainer getStorageContainer = (StorageContainer) TestCaseUtility.getObjectMap(StorageContainer.class);
			StorageContainer storageContainer = new StorageContainer();
			storageContainer.setId(getStorageContainer.getId());
			System.out.println("Before Update");
	    	Logger.out.info("updating domain object------->"+storageContainer);
		    try 
			{
		    	List resultList = appService.search(StorageContainer.class ,storageContainer);
		    	getStorageContainer =(StorageContainer) resultList.get(0);
		    	BaseTestCaseUtility.updateStorageContainer(getStorageContainer);
		    	System.out.println("After Update");
		    	StorageContainer updatedStorageContainer = (StorageContainer) appService.updateObject(getStorageContainer);
		       	Logger.out.info("Domain object successfully updated ---->"+updatedStorageContainer);
		       	assertFalse("Storage Container successfully updated with with Supervisor Login  ---->"+updatedStorageContainer, true);
		    } 
		    catch (Exception e) {
		       	Logger.out.error(e.getMessage(),e);
		 		e.printStackTrace();
		 		assertTrue("Access denied: You are not authorized to perform this operation. ", true);
		    }
		}*/
	    
	
/*	public void testSearchBioHazardWithScientistLogin()
		{
	      try {
	        Biohazard cachedBiohazard = (Biohazard) TestCaseUtility.getObjectMap(Biohazard.class);
	   		Biohazard biohazard =  new Biohazard();
	  	   	Logger.out.info("searching domain object");
	      	biohazard.setId(cachedBiohazard.getId());
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
/*		  
	public void testSearchCollectionProtocolWithScientistLogin()
		{
	  	CollectionProtocol collectionProtocol = new CollectionProtocol();
	  	CollectionProtocol cachedCollectionProtocol = (CollectionProtocol) TestCaseUtility.getObjectMap(CollectionProtocol.class);
	  	cachedCollectionProtocol.setId((Long) cachedCollectionProtocol.getId());
	    Logger.out.info(" searching domain object");
	  	try {
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
	
	public void testSearchDistributionProtocolWithScientistLogin()
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
	  
	  public void testAddParticipantWithScientistLogin()
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
	  
	  public void testAddParticipantWithCPRWithScientistLogin()
		{
			try{
				Participant participant= BaseTestCaseUtility.initParticipantWithCPR();			
				System.out.println(participant);
				participant = (Participant) appService.createObject(participant); 
				Collection collectionProtocolRegistrationCollection = participant.getCollectionProtocolRegistrationCollection();
				Iterator cprItr = collectionProtocolRegistrationCollection.iterator();
				CollectionProtocolRegistration collectionProtocolRegistration = (CollectionProtocolRegistration)cprItr.next();
				
				TestCaseUtility.setObjectMap(collectionProtocolRegistration, CollectionProtocolRegistration.class);
				TestCaseUtility.setObjectMap(participant, Participant.class);
				System.out.println("Object created successfully");
				assertFalse("Participant registration created successfully", true);
			 }
			 catch(Exception e){
				 e.printStackTrace();
				 assertTrue("Failed to create Participant registration", true);
				 
			 }
		}
	  
	  public void testUpdateParticipantWithScientistLogin()
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
	  public void testSearchParticipantWithScientistLogin()
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
	 
		 
	  
	  public void testAddTissueSpecimenWithTechnicianWithScientist()
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
				assertFalse("Domain Object is successfully added ---->    Name:: " + specimenObj.getLabel(), true);			
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
	  
	  public void testAddMolecularSpecimenWithScientist()
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
				assertFalse("Domain Object is successfully added ---->    Name:: " + specimenObj.getLabel(), true);		
		   
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
		
		public void testAddCellSpecimenWithScientist()
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
				assertFalse("Domain Object is successfully added ---->    Name:: " + specimenObj.getLabel(), true);		
		   
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
		public void testAddFluidSpecimenWithScientist()
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
				assertFalse("Domain Object is successfully added", true);	   
		   }
			catch(Exception e)
			{
				System.out.println("Exception thrown");
				System.out.println(e);
				Logger.out.error(e.getMessage(),e);
				e.printStackTrace();
				assertTrue("Failed to create Domain Object, true", true);	
			}
		}
		
		public void testSearchSpecimenWithScientistLogin()
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
*/		
	}
