import edu.wustl.catissuecore.domain.Biohazard;
import edu.wustl.catissuecore.domain.CancerResearchGroup;
import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.catissuecore.domain.CollectionProtocolEvent;
import edu.wustl.catissuecore.domain.CollectionProtocolRegistration;
import edu.wustl.catissuecore.domain.Department;
import edu.wustl.catissuecore.domain.Distribution;
import edu.wustl.catissuecore.domain.DistributionProtocol;
import edu.wustl.catissuecore.domain.Institution;
import edu.wustl.catissuecore.domain.MolecularSpecimen;
import edu.wustl.catissuecore.domain.Participant;
import edu.wustl.catissuecore.domain.Site;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.SpecimenArray;
import edu.wustl.catissuecore.domain.SpecimenArrayType;
import edu.wustl.catissuecore.domain.SpecimenCharacteristics;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.catissuecore.domain.SpecimenRequirement;
import edu.wustl.catissuecore.domain.StorageContainer;
import edu.wustl.catissuecore.domain.StorageType;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.common.util.logger.Logger;
import gov.nih.nci.system.applicationservice.ApplicationException;
import gov.nih.nci.system.applicationservice.ApplicationService;
import gov.nih.nci.system.applicationservice.ApplicationServiceProvider;
import gov.nih.nci.system.comm.client.ClientSession;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
/*
import edu.wustl.common.util.Utility;
import edu.wustl.common.util.XMLPropertyHandler;
import edu.wustl.common.util.global.ApplicationProperties;
import edu.wustl.common.util.global.Constants;
import edu.wustl.common.util.global.Variables;
import org.apache.log4j.PropertyConfigurator;
*/
/**
 * <!-- LICENSE_TEXT_START -->
* Copyright 2001-2004 SAIC. Copyright 2001-2003 SAIC. This software was developed in conjunction with the National Cancer Institute,
* and so to the extent government employees are co-authors, any rights in such works shall be subject to Title 17 of the United States Code, section 105.
* Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
* 1. Redistributions of source code must retain the above copyright notice, this list of conditions and the disclaimer of Article 3, below. Redistributions
* in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other
* materials provided with the distribution.
* 2. The end-user documentation included with the redistribution, if any, must include the following acknowledgment:
* "This product includes software developed by the SAIC and the National Cancer Institute."
* If no such end-user documentation is to be included, this acknowledgment shall appear in the software itself,
* wherever such third-party acknowledgments normally appear.
* 3. The names "The National Cancer Institute", "NCI" and "SAIC" must not be used to endorse or promote products derived from this software.
* 4. This license does not authorize the incorporation of this software into any third party proprietary programs. This license does not authorize
* the recipient to use any trademarks owned by either NCI or SAIC-Frederick.
* 5. THIS SOFTWARE IS PROVIDED "AS IS," AND ANY EXPRESSED OR IMPLIED WARRANTIES, (INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
* MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE) ARE DISCLAIMED. IN NO EVENT SHALL THE NATIONAL CANCER INSTITUTE,
* SAIC, OR THEIR AFFILIATES BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
* PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
* WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 * <!-- LICENSE_TEXT_END -->
 */

/**
 * @author caBIO Team
 * @version 1.0
 */



/**
	* ClientDemo.java demonstartes various ways to execute searches with and without
      * using Application Service Layer (convenience layer that abstracts building criteria
      * Uncomment different scenarios below to demonstrate the various types of searches
*/

public class ClientDemo 
{
		static ApplicationService appService = null;
		static APIDemo api = new APIDemo();
		public static Map dataModelObjectMap = new HashMap();
		
    public static void main(String[] args) 
	{
	
		System.out.println("*** ClientDemo...");
		try
		{
			ApplicationServiceProvider applicationServiceProvider = new ApplicationServiceProvider(); 
			appService = applicationServiceProvider.getApplicationService();
			ClientSession cs = ClientSession.getInstance();
			try
			{ 
				cs.startSession("admin@admin.com", "login12");
			} 
			catch (Exception ex) 
			{ 
				System.out.println(ex.getMessage()); 
				ex.printStackTrace();
				return;
			}

			try 
			{
				ClientDemo testClient = new ClientDemo();
				testClient.createObjects();
				//testClient.serachObject();
/*				testClient.testAddInstitution();
				testClient.testAddDepartment();
				testClient.testAddCancerResearchGroup();
				testClient.testAddBioHazard();
				testClient.testAddUser();
*/			}
			catch (RuntimeException e2) 
			{
				e2.printStackTrace();
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			System.out.println("Test client throws Exception = "+ ex);
		}
	}
    //////////////////////// Start Add operation //////
	private void createObjects()
	{
	try
	{
				   // Add sub domain objects	
			 	   Object obj = api.initSpecimenCharacteristics();
			 	   SpecimenCharacteristics specimenCharacteristics = (SpecimenCharacteristics) appService.createObject(obj);
			 	   dataModelObjectMap.put("SpecimenCharacteristics",specimenCharacteristics);
			 	   
			 	   obj = api.initSpecimenRequirement();
			 	   SpecimenRequirement specimenRequirement = (SpecimenRequirement) appService.createObject(obj);
			 	   dataModelObjectMap.put("SpecimenRequirement",specimenRequirement);
			
			 	   obj = api.initCollectionProtocolEvent();
			 	   CollectionProtocolEvent collectionProtocolEvent = (CollectionProtocolEvent) appService.createObject(obj);
			 	   dataModelObjectMap.put("CollectionProtocolEvent",collectionProtocolEvent);
			 	    
					testAddInstitution();
					testAddDepartment();
					testAddCancerResearchGroup();
					testAddUser();
					testAddSite();
					testAddBioHazard();
					testAddCollectionProtocol();
					testAddStorageType();
					testAddCollectionProtocolRegistration();
					testAddStorageContainer();
					/*
					testAddDistributionProtocol();					
					testAddSpecimenArrayType();
					
					testAddParticipant();
					// participant registration not found
					//testAddSpecimenCollectionGroup();
					testAddSpecimen();
					testAddDistribution();
					testAddSpecimenArray();
					*/
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			System.out.println("Test client throws Exception = "+ ex);
		}
	}
    
	private void testAddDepartment()
	{
		try
		{
			Department departmentObj = (Department) api.initDepartment();
	    	setLogger(departmentObj);
	    	departmentObj =  (Department) appService.createObject(departmentObj);
	    	dataModelObjectMap.put("Department",departmentObj);
			Logger.out.info(" Domain Object is successfully added ----> Name:: " + departmentObj.getName());
		//+ departmentObj.getId().longValue() + " ::  Name :: " + departmentObj.getName());
		}
		catch(Exception e)
		{
			Logger.out.error(e);
			e.printStackTrace();
		}
	}
	private void testAddBioHazard()
	{
		try
		{
			Biohazard bioHazardObj = (Biohazard) api.initBioHazard();
	    	setLogger(bioHazardObj);
	    	bioHazardObj =  (Biohazard) appService.createObject(bioHazardObj);
	    	dataModelObjectMap.put("Biohazard",bioHazardObj);
			Logger.out.info(" Domain Object is successfully added ---->  Name:: " + bioHazardObj.getName());
		//+ bioHazardObj.getId().longValue() + " ::  Name :: " + bioHazardObj.getName());
		}
		catch(Exception e)
		{
			Logger.out.error(e);
			e.printStackTrace();
		}
	}
	private void testAddUser()
	{
		try
		{
			User userObj = (User) api.initUser();
	    	setLogger(userObj);
			userObj =  (User) appService.createObject(userObj);
			dataModelObjectMap.put("User",userObj);
			Logger.out.info(" Domain Object is successfully added ---->  LoginName:: " + userObj.getId().longValue() + " ::  Name :: " + userObj.getFirstName());
		}
		catch(Exception e)
		{
			Logger.out.error(e);
			e.printStackTrace();
		}
	}
	private void testAddParticipant()
	{
		try
		{
			Participant participantObj = (Participant) api.initParticipant();
			setLogger(participantObj);
			participantObj =  (Participant) appService.createObject(participantObj);
			dataModelObjectMap.put("Participant",participantObj);
			Logger.out.info(" Domain Object is successfully added ---->  ID:: " + participantObj.getId().longValue());
		}
		catch(Exception e)
		{
			Logger.out.error(e);
			e.printStackTrace();
		}
	}

	private void testAddInstitution()
	{
		try
		{
			Institution institutionObj = (Institution) api.initInstitution();
	    	setLogger(institutionObj);
	    	institutionObj =  (Institution) appService.createObject(institutionObj);
	    	dataModelObjectMap.put("Institution",institutionObj);
			Logger.out.info(" Domain Object is successfully added ---->  Name:: " + institutionObj.getName());
		//+ institutionObj.getId().longValue() + " ::  Name :: " + institutionObj.getName());
		}
		catch(Exception e)
		{
			Logger.out.error(e);
			e.printStackTrace();
		}
	}
	private void testAddCancerResearchGroup()
	{
		try
		{
			CancerResearchGroup cancerResearchGroupObj = (CancerResearchGroup) api.initCancerResearchGroup();
	    	setLogger(cancerResearchGroupObj);
	    	cancerResearchGroupObj =  (CancerResearchGroup) appService.createObject(cancerResearchGroupObj);
	    	dataModelObjectMap.put("CancerResearchGroup",cancerResearchGroupObj);
			Logger.out.info(" Domain Object is successfully added ---->  Name:: " + cancerResearchGroupObj.getName());
		//+ cancerResearchGroupObj.getId().longValue() + " ::  Name :: " + cancerResearchGroupObj.getName());
		}
		catch(Exception e)
		{
			Logger.out.error(e);
			e.printStackTrace();
		}
	}
	private void testAddSite()
	{
		try
		{
			Site siteObj = (Site) api.initSite();
	    	setLogger(siteObj);
			siteObj =  (Site) appService.createObject(siteObj);
			dataModelObjectMap.put("Site",siteObj);
			Logger.out.info(" Domain Object is successfully added ----> ID:: " + siteObj.getId().toString());
		//+ siteObj.getId().longValue() + " ::  Name :: " + siteObj.getName());
		}
		catch(Exception e)
		{
			Logger.out.error(e);
			e.printStackTrace();
		}
	}
	private void testAddDistribution()
	{
		try
		{
			Distribution distributionObj = (Distribution)api.initDistribution();
	    	setLogger(distributionObj);
			distributionObj =  (Distribution) appService.createObject(distributionObj);
			dataModelObjectMap.put("Distribution",distributionObj);
			Logger.out.info(" Domain Object is successfully added ---->  ID:: " + distributionObj.getId().toString());
		//+ distributionObj.getId().longValue() + " ::  Name :: " + distributionObj.getName());
		}
		catch(Exception e)
		{
			Logger.out.error(e);
			e.printStackTrace();
		}
	}
	private void testAddDistributionProtocol()
	{
		try
		{
			DistributionProtocol distributionProtocolObj =(DistributionProtocol)api.initDistributionProtocol();
	    	setLogger(distributionProtocolObj);
			distributionProtocolObj =  (DistributionProtocol) appService.createObject(distributionProtocolObj);
			dataModelObjectMap.put("DistributionProtocol",distributionProtocolObj);
			Logger.out.info(" Domain Object is successfully added ----> ID:: " + distributionProtocolObj.getId().toString());
		//+ distributionProtocolObj.getId().longValue() + " ::  Name :: " + distributionProtocolObj.getName());
		}
		catch(Exception e)
		{
			Logger.out.error(e);
			e.printStackTrace();
		}
	}
	private void testAddCollectionProtocol()
	{
		try
		{
			CollectionProtocol collectionProtocolObj = (CollectionProtocol)api.initCollectionProtocol();
			setLogger(collectionProtocolObj);
			collectionProtocolObj =  (CollectionProtocol) appService.createObject(collectionProtocolObj);
			dataModelObjectMap.put("CollectionProtocol",collectionProtocolObj);
			Logger.out.info(" Domain Object is successfully added ---->  ID:: " + collectionProtocolObj.getId().toString());
		//+ collectionProtocolObj.getId().longValue() + " ::  Name :: " + collectionProtocolObj.getName());
		}
		catch(Exception e)
		{
			Logger.out.error(e);
			e.printStackTrace();
		}
	}
	private void testAddCollectionProtocolRegistration()
	{
		try
		{
		//	System.out.println("ClientDemo....................");
			CollectionProtocolRegistration collectionProtocolRegistrationObj =(CollectionProtocolRegistration) api.initCollectionProtocolRegistration();
	    	setLogger(collectionProtocolRegistrationObj);
			collectionProtocolRegistrationObj =  (CollectionProtocolRegistration) appService.createObject(collectionProtocolRegistrationObj);
			dataModelObjectMap.put("CollectionProtocolRegistration",collectionProtocolRegistrationObj);
			Logger.out.info(" Domain Object is successfully added ---->   ID:: " + collectionProtocolRegistrationObj.getId().toString());
		//+ collectionProtocolRegistrationObj.getId().longValue() + " ::  Name :: " + collectionProtocolRegistrationObj.getName());
		}
		catch(Exception e)
		{
			Logger.out.error(e);
			e.printStackTrace();
		}
	}
	private void testAddStorageType()
	{
		try
		{
			StorageType storageTypeObj =(StorageType) api.initStorageType();
	    	setLogger(storageTypeObj);
			storageTypeObj =  (StorageType) appService.createObject(storageTypeObj);
			dataModelObjectMap.put("StorageType",storageTypeObj);
			Logger.out.info(" Domain Object is successfully added ---->  ID:: " + storageTypeObj.getId().toString());
		//+ storageTypeObj.getId().longValue() + " ::  Name :: " + storageTypeObj.getName());
		}
		catch(Exception e)
		{
			Logger.out.error(e);
			e.printStackTrace();
		}
	}

	private void testAddStorageContainer()
	{
		try
		{
			StorageContainer storageContainerObj =(StorageContainer) api.initStorageContainer();
			
	    	setLogger(storageContainerObj);
			storageContainerObj =  (StorageContainer) appService.createObject(storageContainerObj);
			dataModelObjectMap.put("StorageContainer",storageContainerObj);
			Logger.out.info(" Domain Object is successfully added ---->    ID:: " + storageContainerObj.getId().toString());
		//+ storageContainerObj.getId().longValue() + " ::  Name :: " + storageContainerObj.getName());
		}
		catch(Exception e)
		{
			Logger.out.error(e);
			e.printStackTrace();
		}
	}
	 private void testAddSpecimen()
		{
			try
			{
				Specimen specimenObj = (Specimen) api.initSpecimen();
				setLogger(specimenObj);
				specimenObj =  (Specimen) appService.createObject(specimenObj);
				dataModelObjectMap.put("Specimen",specimenObj);
				Logger.out.info(" Domain Object is successfully added ---->    ID:: " + specimenObj.getId().toString());
				//Logger.out.info(" Domain Object is successfully added ---->  ID:: " + specimenObj.getId().longValue() + " ::  Name :: " + specimenObj.getLabel());
			}
			catch(Exception e)
			{
				Logger.out.error(e);
				e.printStackTrace();
			}
		}
	 private void testAddSpecimenArrayType()
		{
			try
			{
				SpecimenArrayType specimenArrayTypeObj = (SpecimenArrayType) api.initSpecimenArrayType();
				setLogger(specimenArrayTypeObj);
				specimenArrayTypeObj =  (SpecimenArrayType) appService.createObject(specimenArrayTypeObj);
				dataModelObjectMap.put("SpecimenArrayType",specimenArrayTypeObj);
				Logger.out.info(" Domain Object is successfully added ---->    ID:: " + specimenArrayTypeObj.getId().toString());
				//Logger.out.info(" Domain Object is successfully added ---->  ID:: " + specimenObj.getId().longValue() + " ::  Name :: " + specimenObj.getLabel());
			}
			catch(Exception e)
			{
				Logger.out.error(e);
				e.printStackTrace();
			}
		}
	 private void testAddSpecimenArray()
		{
			try
			{
				SpecimenArray specimenArrayObj = (SpecimenArray) api.initSpecimenArray();
				setLogger(specimenArrayObj);
				specimenArrayObj =  (SpecimenArray) appService.createObject(specimenArrayObj);
				dataModelObjectMap.put("SpecimenArray",specimenArrayObj);
				Logger.out.info(" Domain Object is successfully added ---->    ID:: " + specimenArrayObj.getId().toString());
		//		Logger.out.info(" Domain Object is successfully added ---->  ID:: " + specimenArrayObj.getId().longValue() + " ::  Name :: " + specimenArrayObj.getName());
			}
			catch(Exception e)
			{
				Logger.out.error(e);
				e.printStackTrace();
			}
		}
	 	private void testAddSpecimenCollectionGroup()
		{
			try
			{
				SpecimenCollectionGroup specimenCollectionGroupObj = (SpecimenCollectionGroup) api.initSpecimenCollectionGroup();
		    	setLogger(specimenCollectionGroupObj);
				specimenCollectionGroupObj =  (SpecimenCollectionGroup) appService.createObject(specimenCollectionGroupObj);
				dataModelObjectMap.put("SpecimenCollectionGroup",specimenCollectionGroupObj);
				Logger.out.info(" Domain Object is successfully added ---->    ID:: " + specimenCollectionGroupObj.getId().toString());
			//+ specimenCollectionGroupObj.getId().longValue() + " ::  Name :: " + specimenCollectionGroupObj.getName());
			}
			catch(Exception e)
			{
				Logger.out.error(e);
				e.printStackTrace();
			}
		}
	 	
////////////////////////////////  End Add operation /////////////////	 	
    
    private void serachObject()
    {
    	
    	api = new APIDemo();
    	//testSearchDepartment();
    	//testSearchSpecimenArrayType();
    	/*
    	testSearchBioHazard();
    	testSearchDepartment();
    	testSearchCancerResearchGroup();
    	testSearchInstitution();
    	
    	
   // 	testSearchCollectionProtocol();
    	testSearchSite();
    	testSearchUser();
    	testSearchParticipant();
    //	testSearchCollectionProtocolRegistration();
    	
    	
    	testSearchStorageType();
    	testSearchSpecimen();
    	testSearchSpecimenArrayType();
    	testSearchSpecimenArray();
    	testSearchSpecimenCollectionGroup();
    	testSearchStorageContainer();
    	*/
    	
/*    	Department department = api.initDepartment();
    	department.setId(new Long(2));
		try 
		{
			List resultList = appService.search(Department.class, department);
			System.out.println(" Result list " + resultList);
			for (Iterator resultsIterator = resultList.iterator(); resultsIterator.hasNext();) 
			{
				Department returneddepartment = (Department) resultsIterator.next();
				System.out.println(" Name:  " + returneddepartment.getName());
			}
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
*/    	
    	
    	try
    	{
    		
    	MolecularSpecimen specimen = new MolecularSpecimen();
    	specimen.setId(new Long(1));
    	
    	List list = appService.search(MolecularSpecimen.class,specimen);
    	if(list != null && list.size() != 0)
    	{
    		System.out.println("List Size : " + list.size());
    		specimen = (MolecularSpecimen)list.get(0);
    		System.out.println("Type : " + specimen.getType() + " :: Id :" + 
    	specimen.getId());
    		System.out.println( " "  + specimen.getSpecimenCollectionGroup().getClinicalReport().getSurgicalPathologyNumber());
    	}
    	else
    	{
    		System.out.println("List is empty.");
    	}
    	} 
    	catch(ApplicationException e)
    	{
    		e.printStackTrace();
    	}
   }
	
    /*    
    private void testSearchDepartment()
    {
    	Department department = api.initDepartment();
    	department.setId(new Long(1));
		try 
		{
			List resultList = appService.search(Department.class, department);
			for (Iterator resultsIterator = resultList.iterator(); resultsIterator.hasNext();) 
			{
				Department returneddepartment = (Department) resultsIterator.next();
				System.out.println(returneddepartment.getName());
			}
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
    }

    private void testSearchSpecimenArrayType()
    {
		SpecimenArrayType spec = new SpecimenArrayType();
		spec.setId(new Long(2));
		spec.setName("Any");
		//spec.setCapacity(null);
		//spec.setSpecimenTypeCollection(null);
		
		//spec.setId(new Long(1));
		//System.out.println(" List of TissueSpecimen objects::  " + results);
		try 
		{
			List resultList = appService.search(SpecimenArrayType.class, spec);
			System.out.println(" List of SpecimenArrayType objects::  " + resultList);
			for (Iterator resultsIterator = resultList.iterator(); resultsIterator.hasNext();) 
			{
				SpecimenArrayType returneddepartment = (SpecimenArrayType) resultsIterator.next();
				System.out.println(returneddepartment.getName());
				System.out.println(returneddepartment.getId().toString());
			}
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
    }

    private void testSearchBioHazard()
    {
		Biohazard biohazard = new Biohazard();
		biohazard.setId(new Long(1));
		//spec.setCapacity(null);
		//spec.setSpecimenTypeCollection(null);
		
		//spec.setId(new Long(1));
		//System.out.println(" List of TissueSpecimen objects::  " + results);
		try 
		{
			List resultList = appService.search(Biohazard.class, biohazard);
			System.out.println(" List of SpecimenArrayType objects::  " + resultList);
			for (Iterator resultsIterator = resultList.iterator(); resultsIterator.hasNext();) 
			{
				Biohazard returneddepartment = (Biohazard) resultsIterator.next();
				System.out.println(returneddepartment.getName());
				System.out.println(returneddepartment.getId().toString());
			}
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
    }
    */
    
    /////////////////// Start default data insertion ///////
    /////////////////////////// ----------------------- Start test Search operation
    
    private void testSearchDepartment()
    {
        //Department department = api.initDepartment();
    	Department cachedObject = (Department) dataModelObjectMap.get("Department"); 
    	Department department = new Department();
    	department.setId(cachedObject.getId());
    	setLogger(department);
     	Logger.out.info(" searching domain object");
         try {
        	 List resultList = appService.search(Department.class, department);
        	 for (Iterator resultsIterator = resultList.iterator(); resultsIterator.hasNext();) {
        		 Department returneddepartment = (Department) resultsIterator.next();
        		 Logger.out.info(" Domain Object is found by Serach operation:: Name --- >" + returneddepartment.getName());
             }
          }
          catch (Exception e) {
        	  Logger.out.error(e);
          }

    }
    private void testSearchCancerResearchGroup()
    {
    	CancerResearchGroup cachedObject =(CancerResearchGroup) dataModelObjectMap.get("CancerResearchGroup");
    	CancerResearchGroup cancerResearchGroup = new CancerResearchGroup();
    	cancerResearchGroup.setId(cachedObject.getId());
    	setLogger(cancerResearchGroup);
     	Logger.out.info(" searching domain object");
//    	cancerResearchGroup.setId(new Long(1));
         try {
        	 List resultList = appService.search(CancerResearchGroup.class, cancerResearchGroup);
        	 for (Iterator resultsIterator = resultList.iterator(); resultsIterator.hasNext();) {
        		 CancerResearchGroup returnedcancerResearchGroup = (CancerResearchGroup) resultsIterator.next();
        		 Logger.out.info(" Domain Object is found by Serach operation:: Name --- >" + returnedcancerResearchGroup.getName());
             }
          } 
          catch (Exception e) {
        	  Logger.out.error(e);
          }

    }
	   
    private void testSearchSite()
    {
    	Site cachedObject = (Site) dataModelObjectMap.get("Site");
    	Site site = new Site();
    	site.setId(cachedObject.getId());
   	 	setLogger(site);
     	Logger.out.info(" searching domain object");
//        site.setId(new Long(1));
         try {
        	 List resultList = appService.search(Site.class,site);
        	 for (Iterator resultsIterator = resultList.iterator(); resultsIterator.hasNext();) {
        		 Site returnedsite = (Site) resultsIterator.next();
        		 Logger.out.info(" Domain Object is successfully Found ---->  :: " + returnedsite.getName());
             }
          } 
          catch (Exception e) {
        	  Logger.out.error(e);
          }

    }
    private void testSearchUser()
    {
    	 User cachedObject = (User) dataModelObjectMap.get("User");
    	 User user = (User) new User();
     	 setLogger(user);
     	 Logger.out.info(" searching domain object");
    	 user.setId(cachedObject.getId());
//    	 user.setId(new Long(1));
    	 
         try {
        	 List resultList = appService.search(User.class,user);
        	 for (Iterator resultsIterator = resultList.iterator(); resultsIterator.hasNext();) {
        		 User returneduser = (User) resultsIterator.next();
        		 Logger.out.info(" Domain Object is successfully Found ---->  :: " + returneduser.getEmailAddress());
             }
          } 
          catch (Exception e) {
        	  Logger.out.error(e);
          }

    } 
    private void testSearchParticipant()
    {
    	 Participant cachedObject = (Participant)dataModelObjectMap.get("Participant");
    	 Participant participant = new Participant();
     	 setLogger(participant);
    	 Logger.out.info(" searching domain object");
    	 participant.setId(cachedObject.getId());
//    	 participant.setId(new Long(3));
         try {
        	 List resultList = appService.search(Participant.class,participant);
        	 for (Iterator resultsIterator = resultList.iterator(); resultsIterator.hasNext();) {
        		 Participant returnedparticipant = (Participant) resultsIterator.next();
        		 Logger.out.info(" Domain Object is successfully Found ---->  :: " + returnedparticipant.getFirstName());
             }
          } 
          catch (Exception e) {
        	  Logger.out.error(e);
          }

    }
    private void testSearchInstitution()
    {
    	Institution cachedObject = (Institution)dataModelObjectMap.get("Institution");
    	Institution institution = new Institution();
    	setLogger(institution);
    	Logger.out.info(" searching domain object");
    	institution.setId(cachedObject.getId());//    	institution.setId(new Long(1));
         try {
        	 List resultList = appService.search(Institution.class,institution);
        	 for (Iterator resultsIterator = resultList.iterator(); resultsIterator.hasNext();) {
        		 Institution returnedinstitution = (Institution) resultsIterator.next();
        		 Logger.out.info(" Domain Object is successfully Found ---->  :: " + returnedinstitution.getName());
             }
          } 
          catch (Exception e) {
        	  Logger.out.error(e);
          }

    }
    private void testSearchBioHazard()
    {
    	Biohazard cachedObject = (Biohazard)dataModelObjectMap.get("Biohazard");
    	Biohazard biohazard = new Biohazard();
    	setLogger(biohazard);
    	Logger.out.info(" searching domain object");
    	biohazard.setId(cachedObject.getId());    	
         try {
        	 List resultList = appService.search(Biohazard.class,biohazard);
        	 for (Iterator resultsIterator = resultList.iterator(); resultsIterator.hasNext();) {
        		 Biohazard returnedbiohazard = (Biohazard) resultsIterator.next();
        		 Logger.out.info(" Domain Object is successfully Found ---->  :: " + returnedbiohazard.getName());
             }
          } 
          catch (Exception e) {
        	  Logger.out.error(e);
          }

    }
    private void testSearchDistribution()
    {
    	Distribution cachedDistribution =(Distribution)dataModelObjectMap.get("Distribution");
    	Distribution distribution = new Distribution();
    	setLogger(distribution);
    	Logger.out.info(" searching domain object");
    	distribution.setId(cachedDistribution.getId());
         try {
        	 List resultList = appService.search(Distribution.class,distribution);
        	 for (Iterator resultsIterator = resultList.iterator(); resultsIterator.hasNext();) {
        		 Distribution returneddistribution = (Distribution) resultsIterator.next();
        		 Logger.out.info(" Domain Object is successfully Found ---->  :: " + returneddistribution.getMessageLabel());
             }
          } 
          catch (Exception e) {
        	  Logger.out.error(e);
          }

    }
    private void testSearchDistributionProtocol()
    {
    	DistributionProtocol cacheddistributionProtocol =(DistributionProtocol)dataModelObjectMap.get("DistributionProtocol");
    	DistributionProtocol distributionProtocol =new DistributionProtocol();
    	setLogger(distributionProtocol);
    	Logger.out.info(" searching domain object");
    	distributionProtocol.setId(cacheddistributionProtocol.getId());
         try {
        	 List resultList = appService.search(DistributionProtocol.class,distributionProtocol);
        	 for (Iterator resultsIterator = resultList.iterator(); resultsIterator.hasNext();) {
        		 DistributionProtocol returneddistributionprotocol = (DistributionProtocol) resultsIterator.next();
        		 Logger.out.info(" Domain Object is successfully Found ---->  :: " + returneddistributionprotocol.getTitle());
             }
          } 
          catch (Exception e) {
        	  Logger.out.error(e);
          }

    }
    private void testSearchCollectionProtocol()
    {
    	CollectionProtocol cachedcollectionProtocol = (CollectionProtocol)dataModelObjectMap.get("CollectionProtocol");
    	CollectionProtocol collectionProtocol = new CollectionProtocol();
    	setLogger(collectionProtocol);
    	Logger.out.info(" searching domain object");
    	collectionProtocol.setId(cachedcollectionProtocol.getId());
         try {
        	 List resultList = appService.search(CollectionProtocol.class,collectionProtocol);
        	 for (Iterator resultsIterator = resultList.iterator(); resultsIterator.hasNext();) {
        		 CollectionProtocol returnedcollectionprotocol = (CollectionProtocol) resultsIterator.next();
        		 Logger.out.info(" Domain Object is successfully Found ---->  :: " + returnedcollectionprotocol.getTitle());
             }
          } 
          catch (Exception e) {
        	  Logger.out.error(e);
          }

    }
    private void testSearchCollectionProtocolRegistration()
    {
    	CollectionProtocolRegistration collectionProtocolRegistration =(CollectionProtocolRegistration)dataModelObjectMap.get("CollectionProtocolRegistration");
    	CollectionProtocolRegistration cachedcollectionProtocolRegistration =new CollectionProtocolRegistration();
    	setLogger(collectionProtocolRegistration);
    	Logger.out.info(" searching domain object");
	    collectionProtocolRegistration.setId(cachedcollectionProtocolRegistration.getId());
         try {
        	 List resultList = appService.search(CollectionProtocolRegistration.class,collectionProtocolRegistration);
        	 for (Iterator resultsIterator = resultList.iterator(); resultsIterator.hasNext();) {
        		 CollectionProtocolRegistration returnedcollectionprotocolregistration = (CollectionProtocolRegistration) resultsIterator.next();
        		 Logger.out.info(" Domain Object is successfully Found ---->  :: " + returnedcollectionprotocolregistration.getMessageLabel());
             }
          } 
          catch (Exception e) {
        	  Logger.out.error(e);
          }
    }
    private void testSearchStorageType()
    {
    	StorageType cachedstorageType =(StorageType)dataModelObjectMap.get("StorageType");
    	StorageType storageType =new StorageType();
    	setLogger(storageType);
    	Logger.out.info(" searching domain object");
    	storageType.setId(cachedstorageType.getId());
    	
         try {
        	 List resultList = appService.search(StorageType.class,storageType);
        	 for (Iterator resultsIterator = resultList.iterator(); resultsIterator.hasNext();) {
        		 StorageType returnedstoragetype = (StorageType) resultsIterator.next();
        		 Logger.out.info(" Domain Object is successfully Found ---->  :: " +returnedstoragetype.getName());
             }
          } 
          catch (Exception e) {
        	  Logger.out.error(e);
          }
    }
    private void testSearchStorageContainer()
    {
    	 StorageContainer cachedstorageContainer =(StorageContainer)dataModelObjectMap.get("StorageContainer");
    	 StorageContainer storageContainer =new StorageContainer();
      	 setLogger(storageContainer);
     	Logger.out.info(" searching domain object");
     	 storageContainer.setId(cachedstorageContainer.getId());
         try {
        	 List resultList = appService.search(StorageContainer.class,storageContainer);
        	 for (Iterator resultsIterator = resultList.iterator(); resultsIterator.hasNext();) {
        		 StorageContainer returnedstoragecontainer = (StorageContainer) resultsIterator.next();
        		 Logger.out.info(" Domain Object is successfully Found ---->  :: " + returnedstoragecontainer.getName());
             }
          } 
          catch (Exception e) {
        	  Logger.out.error(e);
          }

    }
    private void testSearchSpecimen()
    {
    	Specimen cachedspecimen =(Specimen)dataModelObjectMap.get("Specimen");
    	Specimen specimen = new Specimen();
     	setLogger(specimen);
    	Logger.out.info(" searching domain object");
    	specimen.setId(cachedspecimen.getId());
         try {
        	 List resultList = appService.search(Specimen.class,specimen);
        	 for (Iterator resultsIterator = resultList.iterator(); resultsIterator.hasNext();) {
        		 Specimen returnedspecimen = (Specimen) resultsIterator.next();
        		 Logger.out.info(" Domain Object is successfully Found ---->  :: " + returnedspecimen.getLabel());
             }
          } 
          catch (Exception e) {
        	  Logger.out.error(e);
          }

    }
    private void testSearchSpecimenArrayType()
    {
    	SpecimenArrayType cachedspecimenArrayType =(SpecimenArrayType)dataModelObjectMap.get("SpecimenArrayType");
    	SpecimenArrayType specimenArrayType = new SpecimenArrayType();
     	setLogger(specimenArrayType);
    	Logger.out.info(" searching domain object");
    	specimenArrayType.setId(cachedspecimenArrayType.getId());
         try {
        	 List resultList = appService.search(SpecimenArrayType.class,specimenArrayType);
        	 for (Iterator resultsIterator = resultList.iterator(); resultsIterator.hasNext();) {
        		 SpecimenArrayType returnedspecimenarraytype = (SpecimenArrayType) resultsIterator.next();
        		 Logger.out.info(" Domain Object is successfully Found ---->  :: " + returnedspecimenarraytype.getName());
             }
          } 
          catch (Exception e) {
        	  Logger.out.error(e);
          }

    }
    private void testSearchSpecimenArray()
    {
    	SpecimenArray cachedspecimenArray = (SpecimenArray) dataModelObjectMap.get("SpecimenArray");
    	SpecimenArray specimenArray = new SpecimenArray();
     	setLogger(specimenArray);
    	Logger.out.info(" searching domain object");
		specimenArray.setId(cachedspecimenArray.getId());
         try {
        	 List resultList = appService.search(SpecimenArray.class,specimenArray);
        	 for (Iterator resultsIterator = resultList.iterator(); resultsIterator.hasNext();) {
        		 SpecimenArray returnedspecimenarray = (SpecimenArray) resultsIterator.next();
        		 Logger.out.info(" Domain Object is successfully Found ---->  :: " +returnedspecimenarray.getName());
             }
          } 
          catch (Exception e) {
        	  Logger.out.error(e);
          }

    }
    private void testSearchSpecimenCollectionGroup()
    {
    	SpecimenCollectionGroup cachedspecimenCollectionGroup = (SpecimenCollectionGroup)dataModelObjectMap.get("SpecimenCollectionGroup");
    	SpecimenCollectionGroup specimenCollectionGroup = new SpecimenCollectionGroup();
     	setLogger(specimenCollectionGroup);
    	Logger.out.info(" searching domain object");
    	specimenCollectionGroup.setId(cachedspecimenCollectionGroup.getId());
         try {
        	 List resultList = appService.search(SpecimenCollectionGroup.class,specimenCollectionGroup);
        	 for (Iterator resultsIterator = resultList.iterator(); resultsIterator.hasNext();) {
        		 SpecimenCollectionGroup returnedspecimencollectiongroup = (SpecimenCollectionGroup) resultsIterator.next();
        		 Logger.out.info(" Domain Object is successfully Found ---->  :: " + returnedspecimencollectiongroup.getName());
             }
          } 
          catch (Exception e) {
        	  Logger.out.error(e);
          }
    }

    /*
    private void testSearchSpecimenArrayType()
    {
		Specimen spec = new Specimen();
		spec.setId(new Long(2));
		//spec.setName("Any");
		//spec.setCapacity(null);
		//spec.setSpecimenTypeCollection(null);
		
		//spec.setId(new Long(1));
		//System.out.println(" List of TissueSpecimen objects::  " + results);
		try 
		{
			List resultList = appService.search(SpecimenArrayType.class, spec);
			System.out.println(" List of SpecimenArrayType objects::  " + resultList);
			for (Iterator resultsIterator = resultList.iterator(); resultsIterator.hasNext();) 
			{
				SpecimenArrayType returneddepartment = (SpecimenArrayType) resultsIterator.next();
				System.out.println(returneddepartment.getName());
				System.out.println(returneddepartment.getId().toString());
			}
		} 
		catch (Exception e) 
		{
			Logger.out.error(e);
		}
    }
    */
    
    ////////////////// --- End Search operation
/*	private void updateObjects(int id)
	{
				APIDemo api = new APIDemo();
				Object obj = api.initCancerResearchGroup();
				AbstractDomainObject domainObject = setId(obj,new Long(1)) ;
				appService.updateObject(domainObject);

				obj = api.initDepartment();
				AbstractDomainObject domainObject = setId(obj,new Long(1)) ;
				appService.updateObject(domainObject);

				obj = api.initBioHazard();
				AbstractDomainObject domainObject = setId(obj,new Long(1)) ;
				appService.updateObject(domainObject);

				obj = api.initInstitution();
				AbstractDomainObject domainObject = setId(obj,new Long(1)) ;
				appService.updateObject(domainObject);

				obj = api.initSite();
				AbstractDomainObject domainObject = setId(obj,new Long(1)) ;
				appService.updateObject(domainObject);

				obj = api.initStorageType();
				AbstractDomainObject domainObject = setId(obj,new Long(1)) ;
				appService.updateObject(domainObject);

				obj = api.initSpecimenArrayType();
				AbstractDomainObject domainObject = setId(obj,new Long(1)) ;
				appService.updateObject(domainObject);

				obj = api.initStorageContainer();
				AbstractDomainObject domainObject = setId(obj,new Long(1)) ;
				appService.updateObject(domainObject);

				obj = api.initUser();
				AbstractDomainObject domainObject = setId(obj,new Long(1)) ;
				appService.updateObject(domainObject);

				obj = api.initParticipant();
				AbstractDomainObject domainObject = setId(obj,new Long(1)) ;
				appService.updateObject(domainObject);

				obj = api.initCollectionProtocol();
				AbstractDomainObject domainObject = setId(obj,new Long(1)) ;
				appService.updateObject(domainObject);

				obj = api.initSpecimen();
				AbstractDomainObject domainObject = setId(obj,new Long(1)) ;
				appService.updateObject(domainObject);

				obj = api.initSpecimenCollectionGroup();
				AbstractDomainObject domainObject = setId(obj,new Long(1)) ;
				appService.updateObject(domainObject);

				obj = api.initDistribution();
				AbstractDomainObject domainObject = setId(obj,new Long(1)) ;
				appService.updateObject(domainObject);

				obj = api.initDistributionProtocol();
				AbstractDomainObject domainObject = setId(obj,new Long(1)) ;
				appService.updateObject(domainObject);

				obj = api.initCollectionProtocolRegistration();
				AbstractDomainObject domainObject = setId(obj,new Long(1)) ;
				appService.updateObject(domainObject);

				obj = api.initSpecimenArray();
				AbstractDomainObject domainObject = setId(obj,new Long(1)) ;
				appService.updateObject(domainObject);

	}

	private AbstractDomainObject setId(Object obj,Long id)
	{
		AbstractDomainObject domainObject = (AbstractDomainObject) obj;
		domainObject.setId(id);
		return domainObject;
	}*/

	 private static void setLogger(Object object)
	{
			Logger.out = org.apache.log4j.Logger.getLogger(object.getClass());
	}

}


