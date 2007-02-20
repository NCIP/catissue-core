import edu.wustl.catissuecore.domain.Biohazard;
import edu.wustl.catissuecore.domain.CancerResearchGroup;
import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.catissuecore.domain.CollectionProtocolEvent;
import edu.wustl.catissuecore.domain.CollectionProtocolRegistration;
import edu.wustl.catissuecore.domain.Department;
import edu.wustl.catissuecore.domain.Distribution;
import edu.wustl.catissuecore.domain.DistributionProtocol;
import edu.wustl.catissuecore.domain.ExistingSpecimenOrderItem;
import edu.wustl.catissuecore.domain.Institution;
import edu.wustl.catissuecore.domain.NewSpecimenArrayOrderItem;
import edu.wustl.catissuecore.domain.OrderDetails;
import edu.wustl.catissuecore.domain.Participant;
import edu.wustl.catissuecore.domain.Quantity;
import edu.wustl.catissuecore.domain.Site;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.SpecimenArray;
import edu.wustl.catissuecore.domain.SpecimenArrayType;
import edu.wustl.catissuecore.domain.SpecimenCharacteristics;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.catissuecore.domain.SpecimenOrderItem;
import edu.wustl.catissuecore.domain.SpecimenRequirement;
import edu.wustl.catissuecore.domain.StorageContainer;
import edu.wustl.catissuecore.domain.StorageType;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.common.util.Utility;
import edu.wustl.common.util.global.Constants;
import edu.wustl.common.util.logger.Logger;
import gov.nih.nci.system.applicationservice.ApplicationService;
import gov.nih.nci.system.applicationservice.ApplicationServiceProvider;
import gov.nih.nci.system.comm.client.ClientSession;

import java.text.ParseException;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
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
* Copyright 2001-2004 SAIC. Copyrigh
* t 2001-2003 SAIC. This software was developed in conjunction with the National Cancer Institute,
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

public class APIClientDemo 
{
		static ApplicationService appService = null;
		static APIDemo api = new APIDemo();		
		static DataGenerator dataGenerator = new DataGenerator();
		public static Map dataModelObjectMap = new HashMap();
		private static ReportWriter reportWriter = null;
		private static int totalOperations;
		private static int successfullOperations;
		private static int failureOperations;
		private static String tabSpacing = "\t\t\t";
		private static String newLine = "\n";
		private String separator = ",";
		private static StringBuffer reportContents = null;
		private String insertOperation = " insert, Positive testcase Name: ";
		private String insertValidateOperation = " insert, Negative testcase Name: ";
		private String updateOperation = " update, Positive testcase Name: ";
		private String updateValidateOperation = "update, Negative testcase Name: ";
		private String searchOperation = " search, Positive testcase Name: ";
		private String successMessage = " pass ";
		private String failureMessage = " fail ";
		private static String filePath = "";
		
    public static void main(String[] args) 
	{
	
		System.out.println("*** APIClientDemo...");
		try
		{
			//ApplicationServiceProvider applicationServiceProvider = new ApplicationServiceProvider(); 
			appService = ApplicationServiceProvider.getApplicationService();
			ClientSession cs = ClientSession.getInstance();
			try
			{ 
				cs.startSession("admin@admin.com","Login123");
			} 
			catch (Exception ex) 
			{ 
				System.out.println(ex.getMessage()); 
				ex.printStackTrace();
				return;
			}

			try 
			{
				APIClientDemo testClient = new APIClientDemo();
				reportWriter = ReportWriter.getInstance();
				String dirFullPath = reportWriter.getDirPath(); 
				reportWriter.createDir(dirFullPath);
				filePath = reportWriter.getFileName(dirFullPath);
				reportWriter.createFile(filePath);
				writeHeaderContentsToReport();
				reportContents = new StringBuffer();
				testClient.createObjects();
				//testClient.serachObject();
				//testClient.updateObjects();
				writeFooterContentsToReport();
				reportWriter.closeFile();
				//testClient.sendMail();
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
//	 	   Object obj = api.initSpecimenCharacteristics();
//	 	   SpecimenCharacteristics specimenCharacteristics = (SpecimenCharacteristics) appService.createObject(obj);
//	 	   dataModelObjectMap.put("SpecimenCharacteristics",specimenCharacteristics);
//	 	   
//	 	   obj = api.initSpecimenRequirement();
//	 	   SpecimenRequirement specimenRequirement = (SpecimenRequirement) appService.createObject(obj);
//	 	   dataModelObjectMap.put("SpecimenRequirement",specimenRequirement);
//	
//	 	   obj = api.initCollectionProtocolEvent();
//	 	   CollectionProtocolEvent collectionProtocolEvent = (CollectionProtocolEvent) appService.createObject(obj);
//	 	   dataModelObjectMap.put("CollectionProtocolEvent",collectionProtocolEvent);
	 	    
			testAddInstitution();
			testAddDepartment();			
			testAddCancerResearchGroup();
			testAddBioHazard();
			
			testAddUser();
			testAddSite();
			testAddStorageType();			
			testAddCollectionProtocol();
			testAddStorageContainer();
			testAddSpecimenArrayType();
			
			testAddDistributionProtocol();
			testAddParticipant();
			testAddCollectionProtocolRegistration();
			testAddSpecimenCollectionGroup();
			testAddSpecimen();
			testAddChildSpecimen();
			testAddDerivedSpecimen();
			//testAddOrderDetails();
			//testAddOrderUpdate();
//			testAddParticipantWithNoRequiredFields();
  			
//			
//			testAddSpecimenArray();
//			testAddDistribution();
//			
//			
//			testAddInstitutionWithWrongData();
//			testAddDepartmentWithWrongData();
//			testAddCancerResearchGroupWithWrongData();
//			testAddUserWithWrongData();
//			testAddSiteWithWrongData();
//			testAddBioHazardWithWrongData();
//			testAddCollectionProtocolWithWrongData();				
//			testAddDistributionProtocolWithWrongData();			
//			testAddParticipantWithWrongData();			
//			testAddCollectionProtocolRegistrationWithWrongData();			
//			testAddSpecimenCollectionGroupWithWrongData();			
//			//testAddStorageTypeForNullObject();
//			testAddStorageTypeForNoName();
//			testAddStorageTypeForNoOneDimensionLabel();				
//			//testAddStorageContainerForNullObject();
//			testAddStorageContainerWithNoName();
//			testAddSpecimenArrayTypeWithWrongData();
//			testAddSpecimenWithWrongData();		
//			testAddSpecimenArrayWithWrongData();
//			testAddSpecimenArrayWithClosedSpecimen();
//			testAddDistributionWithWrongData();
//			testAddDistributionWithClosedSpecimen();
//			testAddDistributionWithClosedSite();
//			testAddDistributionWithClosedDistributionProtocol();
			
								
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
			dataGenerator.initDepartment();
			Department departmentObj = (Department) dataGenerator.dataModelObjectMap.get("DEPARTMENT");
	    	setLogger(departmentObj);
	    	departmentObj =  (Department) appService.createObject(departmentObj);
	    	dataGenerator.dataModelObjectMap.put("DEPARTMENT",departmentObj);
	    	writeSuccessfullOperationToReport(departmentObj,insertOperation+"testAddDepartment");
			Logger.out.info(" Domain Object is successfully added ----> Name:: " + departmentObj.getName());
		//+ departmentObj.getId().longValue() + " ::  Name :: " + departmentObj.getName());
		}
		catch(Exception e)
		{
			writeFailureOperationsToReport("Department",insertOperation+"testAddDepartment");
			Logger.out.error(e.getMessage(),e);
			e.printStackTrace();
		}
	}
	
	
	private void testAddBioHazard()
	{
		try
		{
			dataGenerator.initBioHazard();
			Biohazard bioHazardObj = (Biohazard) dataGenerator.dataModelObjectMap.get("BIOHAZARD");
	    	setLogger(bioHazardObj);
	    	bioHazardObj =  (Biohazard) appService.createObject(bioHazardObj);
	    	System.out.println("-----------------" + bioHazardObj);
	    	dataModelObjectMap.put("BIOHAZARD",bioHazardObj);
	    	writeSuccessfullOperationToReport(bioHazardObj,insertOperation+"testAddBioHazard");
			Logger.out.info(" Domain Object is successfully added ---->  Name:: " + bioHazardObj.getName());
		//+ bioHazardObj.getId().longValue() + " ::  Name :: " + bioHazardObj.getName());
		}
		catch(Exception e)
		{
			writeFailureOperationsToReport("Biohazard",insertOperation+"testAddBioHazard");
			Logger.out.error(e.getMessage(),e);
			e.printStackTrace();
		}
	}
	
	
	
	private void testAddUser()
	{
		try
		{
			dataGenerator.initAdminUser();
			User userObj = (User) dataGenerator.dataModelObjectMap.get("ADMINUSER");			
	    	setLogger(userObj);
			userObj =  (User) appService.createObject(userObj);
			dataGenerator.dataModelObjectMap.put("ADMINUSER",userObj);
			writeSuccessfullOperationToReport(userObj,insertOperation+"testAddUser");
			Logger.out.info(" Domain Object is successfully added ---->  LoginName:: " + userObj.getId().longValue() + " ::  Name :: " + userObj.getFirstName());
			
			dataGenerator.initSupervisorUser();
			User userObj1 = (User) dataGenerator.dataModelObjectMap.get("SUPERVISORUSER");
			setLogger(userObj1);
			userObj1 =  (User) appService.createObject(userObj1);
			dataGenerator.dataModelObjectMap.put("SUPERVISORUSER",userObj1);
			Logger.out.info(" Domain Object is successfully added ---->  LoginName:: " + userObj1.getId().longValue() + " ::  Name :: " + userObj1.getFirstName());
			
			dataGenerator.initTechnicianUsers();
			User technicianUser1 = (User) dataGenerator.dataModelObjectMap.get("TECHNICIANUSER1");
			setLogger(technicianUser1);
			technicianUser1 =  (User) appService.createObject(technicianUser1);
			dataGenerator.dataModelObjectMap.put("TECHNICIANUSER1",technicianUser1);
			Logger.out.info(" Domain Object is successfully added ---->  LoginName:: " + technicianUser1.getId().longValue() + " ::  Name :: " + technicianUser1.getFirstName());
			
			User technicianUser2 = (User) dataGenerator.dataModelObjectMap.get("TECHNICIANUSER2");
			setLogger(technicianUser2);
			technicianUser2 =  (User) appService.createObject(technicianUser2);
			dataGenerator.dataModelObjectMap.put("TECHNICIANUSER2",technicianUser2);
			Logger.out.info(" Domain Object is successfully added ---->  LoginName:: " + technicianUser2.getId().longValue() + " ::  Name :: " + technicianUser2.getFirstName());
			
			
			dataGenerator.initScientistUsers();
			User scientistUser1 = (User) dataGenerator.dataModelObjectMap.get("SCIENTISTUSER1");
			setLogger(scientistUser1);
			scientistUser1 =  (User) appService.createObject(scientistUser1);
			dataGenerator.dataModelObjectMap.put("SCIENTISTUSER1",scientistUser1);
			Logger.out.info(" Domain Object is successfully added ---->  LoginName:: " + scientistUser1.getId().longValue() + " ::  Name :: " + scientistUser1.getFirstName());

			User scientistUser2 = (User) dataGenerator.dataModelObjectMap.get("SCIENTISTUSER2");
			setLogger(scientistUser2);
			scientistUser2 =  (User) appService.createObject(scientistUser2);
			dataGenerator.dataModelObjectMap.put("SCIENTISTUSER2",scientistUser2);
			Logger.out.info(" Domain Object is successfully added ---->  LoginName:: " + scientistUser2.getId().longValue() + " ::  Name :: " + scientistUser2.getFirstName());
			
			User scientistUser3 = (User) dataGenerator.dataModelObjectMap.get("SCIENTISTUSER3");
			setLogger(scientistUser3);
			scientistUser3 =  (User) appService.createObject(scientistUser3);
			dataGenerator.dataModelObjectMap.put("SCIENTISTUSER3",scientistUser3);
			Logger.out.info(" Domain Object is successfully added ---->  LoginName:: " + scientistUser3.getId().longValue() + " ::  Name :: " + scientistUser3.getFirstName());

			User scientistUser4 = (User) dataGenerator.dataModelObjectMap.get("SCIENTISTUSER4");
			setLogger(scientistUser4);
			scientistUser4 =  (User) appService.createObject(scientistUser4);
			dataGenerator.dataModelObjectMap.put("SCIENTISTUSER4",scientistUser4);
			Logger.out.info(" Domain Object is successfully added ---->  LoginName:: " + scientistUser4.getId().longValue() + " ::  Name :: " + scientistUser4.getFirstName());
							
		
		}
		catch(Exception e)
		{
			writeFailureOperationsToReport("User",insertOperation+"testAddUser");
			Logger.out.error(e.getMessage(),e);
			e.printStackTrace();
		}
	}

	private void testAddParticipant()
	{
		try
		{
			dataGenerator.initParticipant();
			Participant participantObj1 =(Participant)dataGenerator.dataModelObjectMap.get("PARTICIPANT1");
			Participant participantObj2 =(Participant)dataGenerator.dataModelObjectMap.get("PARTICIPANT2");
			Participant participantObj3 =(Participant)dataGenerator.dataModelObjectMap.get("PARTICIPANT3");
			Participant participantObj4 =(Participant)dataGenerator.dataModelObjectMap.get("PARTICIPANT4");
			Participant participantObj5 =(Participant)dataGenerator.dataModelObjectMap.get("PARTICIPANT5");
			
			
			setLogger(participantObj1);
			participantObj1=(Participant)appService.createObject(participantObj1);
			dataGenerator.dataModelObjectMap.put("PARTICIPANT1",participantObj1);
			writeSuccessfullOperationToReport(participantObj1,insertOperation+"testAddParticipant1");
			Logger.out.info(" Domain Object is successfully added ---->  ID:: " + participantObj1.getId().longValue());
			
			setLogger(participantObj2);
			participantObj2=(Participant)appService.createObject(participantObj2);
			dataGenerator.dataModelObjectMap.put("PARTICIPANT2",participantObj2);
			writeSuccessfullOperationToReport(participantObj2,insertOperation+"testAddParticipant2");
			Logger.out.info(" Domain Object is successfully added ---->  ID:: " + participantObj2.getId().longValue());
			
			setLogger(participantObj3);
			participantObj3=(Participant)appService.createObject(participantObj3);
			dataGenerator.dataModelObjectMap.put("PARTICIPANT3",participantObj3);
			writeSuccessfullOperationToReport(participantObj3,insertOperation+"testAddParticipant3");
			Logger.out.info(" Domain Object is successfully added ---->  ID:: " + participantObj3.getId().longValue());
			
			setLogger(participantObj4);
			participantObj4=(Participant)appService.createObject(participantObj4);
			dataGenerator.dataModelObjectMap.put("PARTICIPANT4",participantObj4);
			writeSuccessfullOperationToReport(participantObj4,insertOperation+"testAddParticipant4");
			Logger.out.info(" Domain Object is successfully added ---->  ID:: " + participantObj4.getId().longValue());
			
			setLogger(participantObj5);
			participantObj2=(Participant)appService.createObject(participantObj5);
			dataGenerator.dataModelObjectMap.put("PARTICIPANT5",participantObj5);
			writeSuccessfullOperationToReport(participantObj5,insertOperation+"testAddParticipant5");
			Logger.out.info(" Domain Object is successfully added ---->  ID:: " + participantObj5.getId().longValue());
		}
		catch(Exception e)
		{
			writeFailureOperationsToReport("Participant",insertOperation+"testAddParticipant");
			Logger.out.error(e.getMessage(),e);
			e.printStackTrace();
		}
		
	}

	private void testAddInstitution()
	{
		try
		{
			dataGenerator.initInstitution();
			Institution institutionObj = (Institution) dataGenerator.dataModelObjectMap.get("INSTITUTION");			
	    	setLogger(institutionObj);
	    	Logger.out.info("Inserting domain object------->"+institutionObj);
	    	institutionObj =  (Institution) appService.createObject(institutionObj);
	    	dataGenerator.dataModelObjectMap.put("INSTITUTION",institutionObj);
			Logger.out.info(" Domain Object is successfully added ---->  Name:: " + institutionObj.getName());
			writeSuccessfullOperationToReport(institutionObj,insertOperation+"testAddInstitution");
			//+ institutionObj.getId().longValue() + " ::  Name :: " + institutionObj.getName());
		}
		catch(Exception e)
		{
			writeFailureOperationsToReport("Institution",insertOperation+"testAddInstitution");
			Logger.out.error(e.getMessage(),e);
			e.printStackTrace();
		}
	}
	
	
	
	private void testAddCancerResearchGroup()
	{
		try
		{
			dataGenerator.initCancerResearchGroup();
			CancerResearchGroup cancerResearchGroupObj = (CancerResearchGroup) dataGenerator.dataModelObjectMap.get("CANCERRESEARCHGROUP");			
	    	setLogger(cancerResearchGroupObj);
	    	Logger.out.info("Inserting domain object------->"+cancerResearchGroupObj);
	    	cancerResearchGroupObj =  (CancerResearchGroup) appService.createObject(cancerResearchGroupObj);
	    	dataGenerator.dataModelObjectMap.put("CANCERRESEARCHGROUP",cancerResearchGroupObj);
	    	writeSuccessfullOperationToReport(cancerResearchGroupObj,insertOperation+"testAddCancerResearchGroup");
			Logger.out.info(" Domain Object is successfully added ---->  Name:: " + cancerResearchGroupObj.getName());
		//+ cancerResearchGroupObj.getId().longValue() + " ::  Name :: " + cancerResearchGroupObj.getName());
		}
		catch(Exception e)
		{
			//writeFailureOperationsToReport("Ca");
			writeFailureOperationsToReport("CancerResearchGroup",insertOperation+"testAddCancerResearchGroup");
			Logger.out.error(e.getMessage(),e);
			e.printStackTrace();
		}
	}
	
	private void testAddSite()
	{
		try
		{
			dataGenerator.initSite();
			Site site1 = (Site) dataGenerator.dataModelObjectMap.get("SITE1");
			Site site2 = (Site) dataGenerator.dataModelObjectMap.get("SITE2");			
			
	    	setLogger(site1);
	    	Logger.out.info("Inserting domain object------->"+site1);
	    	site1 =  (Site) appService.createObject(site1);
	    	dataGenerator.dataModelObjectMap.put("SITE1",site1);
			writeSuccessfullOperationToReport(site1,insertOperation+"testAddSite");
			Logger.out.info(" Domain Object is successfully added ----> ID:: " + site1.getId().toString());
			
			setLogger(site2);
	    	Logger.out.info("Inserting domain object------->"+site2);
	    	site2 =  (Site) appService.createObject(site2);
	    	dataGenerator.dataModelObjectMap.put("SITE2",site2);
			writeSuccessfullOperationToReport(site2,insertOperation+"testAddSite");
			Logger.out.info(" Domain Object is successfully added ----> ID:: " + site2.getId().toString());
			
		//+ siteObj.getId().longValue() + " ::  Name :: " + siteObj.getName());
		}
		catch(Exception e)
		{
			writeFailureOperationsToReport("Site",insertOperation+"testAddSite");
			Logger.out.error(e.getMessage(),e);
			e.printStackTrace();
		}
	}
	
	
	private void testAddDistribution()
	{
		try
		{			
			Distribution distributionObj = (Distribution)api.initDistribution();
	    	setLogger(distributionObj);
	    	Logger.out.info("Inserting domain object------->"+distributionObj);
			distributionObj =  (Distribution) appService.createObject(distributionObj);
			dataModelObjectMap.put("Distribution",distributionObj);
			writeSuccessfullOperationToReport(distributionObj,insertOperation+"testAddDistribution");
			Logger.out.info(" Domain Object is successfully added ---->  ID:: " + distributionObj.getId().toString());
		//+ distributionObj.getId().longValue() + " ::  Name :: " + distributionObj.getName());
		}
		catch(Exception e)
		{
			writeFailureOperationsToReport("Distribution",insertOperation+"testAddDistribution");
			Logger.out.error(e.getMessage(),e);
			e.printStackTrace();
		}
	}

	private void testAddDistributionProtocol()
	{
		try
		{
			dataGenerator.initDistributionProtocol();
			
			DistributionProtocol dp1 = (DistributionProtocol) dataGenerator.dataModelObjectMap.get("DISTRIBUTION_PROTOCOL1");		
			setLogger(dp1);
			Logger.out.info("Inserting domain object------->"+dp1);
			dp1 =  (DistributionProtocol) appService.createObject(dp1);
			dataGenerator.dataModelObjectMap.put("DISTRIBUTION_PROTOCOL1",dp1);
			
			DistributionProtocol dp2 = (DistributionProtocol) dataGenerator.dataModelObjectMap.get("DISTRIBUTION_PROTOCOL2");
			setLogger(dp2);
			Logger.out.info("Inserting domain object------->"+dp2);
			dp2 =  (DistributionProtocol) appService.createObject(dp2);
			dataGenerator.dataModelObjectMap.put("DISTRIBUTION_PROTOCOL2",dp2);		
			
			writeSuccessfullOperationToReport(dp1,insertOperation+"testAddDistributionProtocol");
			Logger.out.info(" Domain Object is successfully added ----> ID:: " + dp1.getId().toString());
			Logger.out.info(" Domain Object is successfully added ----> ID:: " + dp2.getId().toString());
		//+ distributionProtocolObj.getId().longValue() + " ::  Name :: " + distributionProtocolObj.getName());
		}
		catch(Exception e)
		{
			writeFailureOperationsToReport("DistributionProtocol",insertOperation+"testAddDistributionProtocol");
			Logger.out.error(e.getMessage(),e);
			e.printStackTrace();
		}
	}
	
	
	private void testAddCollectionProtocol()
	{
		try
		{
			dataGenerator.initCollectionProtocol();
			
			CollectionProtocol cp1 = (CollectionProtocol) dataGenerator.dataModelObjectMap.get("COLLECTION_PROTOCOL1");		
			setLogger(cp1);
			Logger.out.info("Inserting domain object------->"+cp1);
			cp1 =  (CollectionProtocol) appService.createObject(cp1);
			dataGenerator.dataModelObjectMap.put("COLLECTION_PROTOCOL1",cp1);
			
			CollectionProtocol cp2 = (CollectionProtocol) dataGenerator.dataModelObjectMap.get("COLLECTION_PROTOCOL2");
			setLogger(cp2);
			Logger.out.info("Inserting domain object------->"+cp2);
			cp2 =  (CollectionProtocol) appService.createObject(cp2);
			dataGenerator.dataModelObjectMap.put("COLLECTION_PROTOCOL2",cp2);
			
			CollectionProtocol cp3 = (CollectionProtocol) dataGenerator.dataModelObjectMap.get("COLLECTION_PROTOCOL3");
			setLogger(cp3);
			Logger.out.info("Inserting domain object------->"+cp3);
			cp3 = (CollectionProtocol) appService.createObject(cp3);
			dataGenerator.dataModelObjectMap.put("COLLECTION_PROTOCOL3",cp3);
			
			writeSuccessfullOperationToReport(cp2,insertOperation+"testAddCollectionProtocol");
			Logger.out.info(" Domain Object is successfully added ---->  ID:: " + cp1.getId().toString());
			Logger.out.info(" Domain Object is successfully added ---->  ID:: " + cp2.getId().toString());
			Logger.out.info(" Domain Object is successfully added ---->  ID:: " + cp3.getId().toString());
		//+ collectionProtocolObj.getId().longValue() + " ::  Name :: " + collectionProtocolObj.getName());
		}
		catch(Exception e)
		{
			writeFailureOperationsToReport("CollectionProtocol",insertOperation+"testAddCollectionProtocol");
			Logger.out.error(e.getMessage(),e);
			e.printStackTrace();
		}
	}
	
	private void testAddCollectionProtocolRegistration()
	{
		try
		{
			dataGenerator.initCollectionProtocolRegistration();
			CollectionProtocolRegistration collectionProtocolRegistrationObj1 =(CollectionProtocolRegistration)dataGenerator.dataModelObjectMap.get("CPR1");
			CollectionProtocolRegistration collectionProtocolRegistrationObj2 =(CollectionProtocolRegistration)dataGenerator.dataModelObjectMap.get("CPR2");
			CollectionProtocolRegistration collectionProtocolRegistrationObj3 =(CollectionProtocolRegistration)dataGenerator.dataModelObjectMap.get("CPR3");
			CollectionProtocolRegistration collectionProtocolRegistrationObj4 =(CollectionProtocolRegistration)dataGenerator.dataModelObjectMap.get("CPR4");
			CollectionProtocolRegistration collectionProtocolRegistrationObj5 =(CollectionProtocolRegistration)dataGenerator.dataModelObjectMap.get("CPR5");
	    	
			setLogger(collectionProtocolRegistrationObj1);
	    	collectionProtocolRegistrationObj1=(CollectionProtocolRegistration)appService.createObject(collectionProtocolRegistrationObj1);
	    	dataGenerator.dataModelObjectMap.put("CPR1",collectionProtocolRegistrationObj1);
			writeSuccessfullOperationToReport(collectionProtocolRegistrationObj1,insertOperation+"testAddCollectionProtocolRegistration");
			Logger.out.info(" Domain Object is successfully added ---->   ID:: " + collectionProtocolRegistrationObj1.getId().toString());
			
			setLogger(collectionProtocolRegistrationObj2);
	    	collectionProtocolRegistrationObj2=(CollectionProtocolRegistration)appService.createObject(collectionProtocolRegistrationObj2);
	    	dataGenerator.dataModelObjectMap.put("CPR2",collectionProtocolRegistrationObj2);
			writeSuccessfullOperationToReport(collectionProtocolRegistrationObj2,insertOperation+"testAddCollectionProtocolRegistration");
			Logger.out.info(" Domain Object is successfully added ---->   ID:: " + collectionProtocolRegistrationObj2.getId().toString());
			
			setLogger(collectionProtocolRegistrationObj3);
			collectionProtocolRegistrationObj3=(CollectionProtocolRegistration)appService.createObject(collectionProtocolRegistrationObj3);
	    	dataGenerator.dataModelObjectMap.put("CPR3",collectionProtocolRegistrationObj3);
			writeSuccessfullOperationToReport(collectionProtocolRegistrationObj3,insertOperation+"testAddCollectionProtocolRegistration");
			Logger.out.info(" Domain Object is successfully added ---->   ID:: " + collectionProtocolRegistrationObj3.getId().toString());
			
			setLogger(collectionProtocolRegistrationObj4);
	    	collectionProtocolRegistrationObj4=(CollectionProtocolRegistration)appService.createObject(collectionProtocolRegistrationObj4);
	    	dataGenerator.dataModelObjectMap.put("CPR4",collectionProtocolRegistrationObj4);
			writeSuccessfullOperationToReport(collectionProtocolRegistrationObj4,insertOperation+"testAddCollectionProtocolRegistration");
			Logger.out.info(" Domain Object is successfully added ---->   ID:: " + collectionProtocolRegistrationObj4.getId().toString());
			
			setLogger(collectionProtocolRegistrationObj5);
	    	collectionProtocolRegistrationObj5=(CollectionProtocolRegistration)appService.createObject(collectionProtocolRegistrationObj5);
	    	dataGenerator.dataModelObjectMap.put("CPR5",collectionProtocolRegistrationObj5);
	    	writeSuccessfullOperationToReport(collectionProtocolRegistrationObj5,insertOperation+"testAddCollectionProtocolRegistration");
			Logger.out.info(" Domain Object is successfully added ---->   ID:: " + collectionProtocolRegistrationObj5.getId().toString());
		
		}
		catch(Exception e)
		{
			writeFailureOperationsToReport("CollectionProtocolRegistration",insertOperation+"testAddCollectionProtocolRegistration");
			Logger.out.error(e.getMessage(),e);
			e.printStackTrace();
		}
	}
	private void testAddStorageType()
	{
		try
		{
			dataGenerator.initStorageType();
			System.out.println("dataGenerator.dataModelObjectMap size-----------"+dataGenerator.dataModelObjectMap.size());
			
			StorageType box = (StorageType) dataGenerator.dataModelObjectMap.get("STORAGE_TYPE_BOX");
			setLogger(box);
	    	Logger.out.info("Inserting domain object------->"+box);
			box =  (StorageType) appService.createObject(box);
			dataGenerator.dataModelObjectMap.put("STORAGE_TYPE_BOX",box);
			
			StorageType rack = (StorageType) dataGenerator.dataModelObjectMap.get("STORAGE_TYPE_RACK");
			setLogger(rack);
	    	Logger.out.info("Inserting domain object------->"+rack);
	    	rack =  (StorageType) appService.createObject(rack);
			dataGenerator.dataModelObjectMap.put("STORAGE_TYPE_RACK",rack);
			
			StorageType freezer = (StorageType) dataGenerator.dataModelObjectMap.get("STORAGE_TYPE_FREEZER");
			setLogger(freezer);
	    	Logger.out.info("Inserting domain object------->"+freezer);
	    	freezer =  (StorageType) appService.createObject(freezer);
			dataGenerator.dataModelObjectMap.put("STORAGE_TYPE_FREEZER",freezer);			
			
			writeSuccessfullOperationToReport(freezer,insertOperation+"testAddStorageType");
			Logger.out.info(" Domain Object is successfully added ---->  ID:: " + box.getId().toString());
			Logger.out.info(" Domain Object is successfully added ---->  ID:: " + rack.getId().toString());
			Logger.out.info(" Domain Object is successfully added ---->  ID:: " + freezer.getId().toString());
			
		//+ storageTypeObj.getId().longValue() + " ::  Name :: " + storageTypeObj.getName());
		}
		catch(Exception e)
		{
			writeFailureOperationsToReport("StorageType",insertOperation+"testAddStorageType");
			Logger.out.error(e.getMessage(),e);
			e.printStackTrace();
		}
	}


	private void testAddStorageContainer()
	{
		try
		{
			StorageType boxStorageType = (StorageType) dataGenerator.dataModelObjectMap.get("STORAGE_TYPE_BOX");	
			StorageType rackStorageType = (StorageType) dataGenerator.dataModelObjectMap.get("STORAGE_TYPE_RACK");
			StorageType freezerStorageType = (StorageType) dataGenerator.dataModelObjectMap.get("STORAGE_TYPE_FREEZER");			
			
			Integer oneDimensionCapacity = freezerStorageType.getCapacity().getOneDimensionCapacity();
			Integer twoDimensionCapacity = freezerStorageType.getCapacity().getTwoDimensionCapacity();		
			int noOfRacks = twoDimensionCapacity.intValue()*oneDimensionCapacity.intValue();
			
			oneDimensionCapacity = rackStorageType.getCapacity().getOneDimensionCapacity();
			twoDimensionCapacity = rackStorageType.getCapacity().getTwoDimensionCapacity();
			int noOfBoxesInOneRack = twoDimensionCapacity.intValue()*oneDimensionCapacity.intValue();	
			
			int totalNoOfBoxes = noOfRacks * noOfBoxesInOneRack;
			
			dataGenerator.initStorageContainerForFreezer();			
			StorageContainer freezerStorageContainer = (StorageContainer) dataGenerator.dataModelObjectMap.get("STORAGE_CONTAINER_FREEZER_1");					
			setLogger(freezerStorageContainer);
			freezerStorageContainer =  (StorageContainer) appService.createObject(freezerStorageContainer);
			Logger.out.info("After insert domain object Storage Container ******* ------->"+freezerStorageContainer.getId());
			dataGenerator.dataModelObjectMap.put("STORAGE_CONTAINER_FREEZER_1",freezerStorageContainer);			
			StorageContainer freezerStorageContainerForSA = (StorageContainer) dataGenerator.dataModelObjectMap.get("STORAGE_CONTAINER_FREEZER_2");					
			setLogger(freezerStorageContainerForSA);
			freezerStorageContainerForSA =  (StorageContainer) appService.createObject(freezerStorageContainerForSA);
			Logger.out.info("After insert domain object Storage Container ******* ------->"+freezerStorageContainerForSA.getId());
			dataGenerator.dataModelObjectMap.put("STORAGE_CONTAINER_FREEZER_2",freezerStorageContainerForSA);
			
			
			dataGenerator.initStorageContainerForRack();			
			for(int i=1; i<=2*noOfRacks; i++)
			{
				StorageContainer rackStorageContainer = (StorageContainer) dataGenerator.dataModelObjectMap.get("STORAGE_CONTAINER_RACK_"+i);			
				setLogger(rackStorageContainer);				
				rackStorageContainer =  (StorageContainer) appService.createObject(rackStorageContainer);
				Logger.out.info("After insert domain object Storage Container ******* ------->"+rackStorageContainer.getId());
				dataGenerator.dataModelObjectMap.put("STORAGE_CONTAINER_RACK_"+i,rackStorageContainer);
				
			}	
			
			dataGenerator.initStorageContainerForBox();		
			for(int i=1; i<=2*totalNoOfBoxes; i++)
			{
				StorageContainer boxStorageContainer = (StorageContainer) dataGenerator.dataModelObjectMap.get("STORAGE_CONTAINER_BOX_"+i);			
				setLogger(boxStorageContainer);				
				boxStorageContainer =  (StorageContainer) appService.createObject(boxStorageContainer);
				Logger.out.info("After insert domain object Storage Container ******* ------->"+boxStorageContainer.getId());
				dataGenerator.dataModelObjectMap.put("STORAGE_CONTAINER_BOX_"+i,boxStorageContainer);
			}
			
			
			writeSuccessfullOperationToReport(freezerStorageContainer,insertOperation+"testAddStorageContainer");
			Logger.out.info(" Domain Object is successfully added ---->    ID:: " + freezerStorageContainer.getId().toString());		
		}
		catch(Exception e)
		{
			writeFailureOperationsToReport("StorageContainer",insertOperation+"testAddStorageContainer");
			Logger.out.error(e);
			e.printStackTrace();
		}
	}
	
	
	 private void testAddSpecimen()
		{
			try
			{
				dataGenerator.initSpecimen();
				Specimen specimenObj1 = (Specimen)dataGenerator.dataModelObjectMap.get("SPECIMEN1");
				Specimen specimenObj2 = (Specimen)dataGenerator.dataModelObjectMap.get("SPECIMEN2");
				Specimen specimenObj3 = (Specimen)dataGenerator.dataModelObjectMap.get("SPECIMEN3");
				Specimen specimenObj4 = (Specimen)dataGenerator.dataModelObjectMap.get("SPECIMEN4");
				//With Specimen
				Specimen specimenObj5 = (Specimen)dataGenerator.dataModelObjectMap.get("SPECIMEN5");
				Specimen specimenObj6 = (Specimen)dataGenerator.dataModelObjectMap.get("SPECIMEN6");
				Specimen specimenObj7 = (Specimen)dataGenerator.dataModelObjectMap.get("SPECIMEN7");
				Specimen specimenObj8 = (Specimen)dataGenerator.dataModelObjectMap.get("SPECIMEN8");
								
				setLogger(specimenObj1);
				Logger.out.info("Inserting domain object------->"+specimenObj1);
				specimenObj1 =  (Specimen) appService.createObject(specimenObj1);
				dataGenerator.dataModelObjectMap.put("SPECIMEN1",specimenObj1);
				writeSuccessfullOperationToReport(specimenObj1,insertOperation+"testAddSpecimen");
				Logger.out.info(" Domain Object is successfully added ---->    ID:: " + specimenObj1.getId().toString());
				Logger.out.info(" Domain Object is successfully added ---->    Name:: " + specimenObj1.getLabel());
				
				setLogger(specimenObj2);
				Logger.out.info("Inserting domain object------->"+specimenObj2);
				specimenObj2 =  (Specimen) appService.createObject(specimenObj2);
				dataGenerator.dataModelObjectMap.put("SPECIMEN2",specimenObj2);
				writeSuccessfullOperationToReport(specimenObj2,insertOperation+"testAddSpecimen");
				Logger.out.info(" Domain Object is successfully added ---->    ID:: " + specimenObj2.getId().toString());
				Logger.out.info(" Domain Object is successfully added ---->    Name:: " + specimenObj2.getLabel());
				
				setLogger(specimenObj3);
				Logger.out.info("Inserting domain object------->"+specimenObj3);
				specimenObj3 =  (Specimen) appService.createObject(specimenObj3);
				dataGenerator.dataModelObjectMap.put("SPECIMEN3",specimenObj3);
				writeSuccessfullOperationToReport(specimenObj3,insertOperation+"testAddSpecimen");
				Logger.out.info(" Domain Object is successfully added ---->    ID:: " + specimenObj3.getId().toString());
				Logger.out.info(" Domain Object is successfully added ---->    Name:: " + specimenObj3.getLabel());
				
				setLogger(specimenObj4);
				Logger.out.info("Inserting domain object------->"+specimenObj4);
				specimenObj4 =  (Specimen) appService.createObject(specimenObj4);
				dataGenerator.dataModelObjectMap.put("SPECIMEN4",specimenObj4);
				writeSuccessfullOperationToReport(specimenObj4,insertOperation+"testAddSpecimen");
				Logger.out.info(" Domain Object is successfully added ---->    ID:: " + specimenObj4.getId().toString());
				Logger.out.info(" Domain Object is successfully added ---->    Name:: " + specimenObj4.getLabel());
				
				setLogger(specimenObj5);
				Logger.out.info("Inserting domain object------->"+specimenObj5);
				specimenObj5 =  (Specimen) appService.createObject(specimenObj5);
				dataGenerator.dataModelObjectMap.put("SPECIMEN5",specimenObj5);
				writeSuccessfullOperationToReport(specimenObj5,insertOperation+"testAddSpecimen");
				Logger.out.info(" Domain Object is successfully added ---->    ID:: " + specimenObj5.getId().toString());
				Logger.out.info(" Domain Object is successfully added ---->    Name:: " + specimenObj5.getLabel());
				
				setLogger(specimenObj6);
				Logger.out.info("Inserting domain object------->"+specimenObj6);
				specimenObj6 =  (Specimen) appService.createObject(specimenObj6);
				dataGenerator.dataModelObjectMap.put("SPECIMEN6",specimenObj6);
				writeSuccessfullOperationToReport(specimenObj6,insertOperation+"testAddSpecimen");
				Logger.out.info(" Domain Object is successfully added ---->    ID:: " + specimenObj6.getId().toString());
				Logger.out.info(" Domain Object is successfully added ---->    Name:: " + specimenObj6.getLabel());
				
				setLogger(specimenObj7);
				Logger.out.info("Inserting domain object------->"+specimenObj7);
				specimenObj7 =  (Specimen) appService.createObject(specimenObj7);
				dataGenerator.dataModelObjectMap.put("SPECIMEN7",specimenObj7);
				writeSuccessfullOperationToReport(specimenObj7,insertOperation+"testAddSpecimen");
				Logger.out.info(" Domain Object is successfully added ---->    ID:: " + specimenObj7.getId().toString());
				Logger.out.info(" Domain Object is successfully added ---->    Name:: " + specimenObj7.getLabel());
				
				setLogger(specimenObj8);
				Logger.out.info("Inserting domain object------->"+specimenObj8);
				specimenObj8 =  (Specimen) appService.createObject(specimenObj8);
				dataGenerator.dataModelObjectMap.put("SPECIMEN8",specimenObj8);
				writeSuccessfullOperationToReport(specimenObj8,insertOperation+"testAddSpecimen");
				Logger.out.info(" Domain Object is successfully added ---->    ID:: " + specimenObj8.getId().toString());
				Logger.out.info(" Domain Object is successfully added ---->    Name:: " + specimenObj8.getLabel());
			}
			catch(Exception e)
			{
				writeFailureOperationsToReport("Specimen",insertOperation+"testAddSpecimen");
				Logger.out.error(e.getMessage(),e);
				e.printStackTrace();
			}
		}	 
	
	 private void testAddChildSpecimen()
		{
			try
			{
				dataGenerator.initChildSpecimen();
				Specimen childSpecimenObj1 = (Specimen)dataGenerator.dataModelObjectMap.get("CHILD_SPECIMEN1");
				Specimen childSpecimenObj2 = (Specimen)dataGenerator.dataModelObjectMap.get("CHILD_SPECIMEN2");
				Specimen childSpecimenObj3 = (Specimen)dataGenerator.dataModelObjectMap.get("CHILD_SPECIMEN3");
				Specimen childSpecimenObj4 = (Specimen)dataGenerator.dataModelObjectMap.get("CHILD_SPECIMEN4");
				Specimen childSpecimenObj5 = (Specimen)dataGenerator.dataModelObjectMap.get("CHILD_SPECIMEN5");
				Specimen childSpecimenObj6 = (Specimen)dataGenerator.dataModelObjectMap.get("CHILD_SPECIMEN6");
				Specimen childSpecimenObj7 = (Specimen)dataGenerator.dataModelObjectMap.get("CHILD_SPECIMEN7");
								
				setLogger(childSpecimenObj1);
				Logger.out.info("Inserting domain object------->"+childSpecimenObj1);
				childSpecimenObj1 =  (Specimen) appService.createObject(childSpecimenObj1);
				dataGenerator.dataModelObjectMap.put("CHILD_SPECIMEN1",childSpecimenObj1);
				writeSuccessfullOperationToReport(childSpecimenObj1,insertOperation+"testAddSpecimen");
				Logger.out.info(" Domain Object is successfully added ---->    ID:: " + childSpecimenObj1.getId().toString());
				Logger.out.info(" Domain Object is successfully added ---->    Name:: " + childSpecimenObj1.getLabel());
				
				setLogger(childSpecimenObj2);
				Logger.out.info("Inserting domain object------->"+childSpecimenObj2);
				childSpecimenObj2 =  (Specimen) appService.createObject(childSpecimenObj2);
				dataGenerator.dataModelObjectMap.put("CHILD_SPECIMEN2",childSpecimenObj2);
				writeSuccessfullOperationToReport(childSpecimenObj2,insertOperation+"testAddSpecimen");
				Logger.out.info(" Domain Object is successfully added ---->    ID:: " + childSpecimenObj2.getId().toString());
				Logger.out.info(" Domain Object is successfully added ---->    Name:: " + childSpecimenObj2.getLabel());
				
				setLogger(childSpecimenObj3);
				Logger.out.info("Inserting domain object------->"+childSpecimenObj3);
				childSpecimenObj3 =  (Specimen) appService.createObject(childSpecimenObj3);
				dataGenerator.dataModelObjectMap.put("CHILD_SPECIMEN3",childSpecimenObj3);
				writeSuccessfullOperationToReport(childSpecimenObj3,insertOperation+"testAddSpecimen");
				Logger.out.info(" Domain Object is successfully added ---->    ID:: " + childSpecimenObj3.getId().toString());
				Logger.out.info(" Domain Object is successfully added ---->    Name:: " + childSpecimenObj3.getLabel());
				
				setLogger(childSpecimenObj4);
				Logger.out.info("Inserting domain object------->"+childSpecimenObj4);
				childSpecimenObj4 =  (Specimen) appService.createObject(childSpecimenObj4);
				dataGenerator.dataModelObjectMap.put("CHILD_SPECIMEN4",childSpecimenObj4);
				writeSuccessfullOperationToReport(childSpecimenObj4,insertOperation+"testAddSpecimen");
				Logger.out.info(" Domain Object is successfully added ---->    ID:: " + childSpecimenObj4.getId().toString());
				Logger.out.info(" Domain Object is successfully added ---->    Name:: " + childSpecimenObj4.getLabel());
				
				setLogger(childSpecimenObj5);
				Logger.out.info("Inserting domain object------->"+childSpecimenObj5);
				childSpecimenObj5 =  (Specimen) appService.createObject(childSpecimenObj5);
				dataGenerator.dataModelObjectMap.put("CHILD_SPECIMEN5",childSpecimenObj5);
				writeSuccessfullOperationToReport(childSpecimenObj5,insertOperation+"testAddSpecimen");
				Logger.out.info(" Domain Object is successfully added ---->    ID:: " + childSpecimenObj5.getId().toString());
				Logger.out.info(" Domain Object is successfully added ---->    Name:: " + childSpecimenObj5.getLabel());
				
				setLogger(childSpecimenObj6);
				Logger.out.info("Inserting domain object------->"+childSpecimenObj6);
				childSpecimenObj6 =  (Specimen) appService.createObject(childSpecimenObj6);
				dataGenerator.dataModelObjectMap.put("CHILD_SPECIMEN6",childSpecimenObj6);
				writeSuccessfullOperationToReport(childSpecimenObj6,insertOperation+"testAddSpecimen");
				Logger.out.info(" Domain Object is successfully added ---->    ID:: " + childSpecimenObj6.getId().toString());
				Logger.out.info(" Domain Object is successfully added ---->    Name:: " + childSpecimenObj6.getLabel());
				
				setLogger(childSpecimenObj7);
				Logger.out.info("Inserting domain object------->"+childSpecimenObj7);
				childSpecimenObj7 =  (Specimen) appService.createObject(childSpecimenObj7);
				dataGenerator.dataModelObjectMap.put("CHILD_SPECIMEN7",childSpecimenObj7);
				writeSuccessfullOperationToReport(childSpecimenObj7,insertOperation+"testAddSpecimen");
				Logger.out.info(" Domain Object is successfully added ---->    ID:: " + childSpecimenObj7.getId().toString());
				Logger.out.info(" Domain Object is successfully added ---->    Name:: " + childSpecimenObj7.getLabel());
			}
			catch(Exception e)
			{
				writeFailureOperationsToReport("Specimen",insertOperation+"testAddSpecimen");
				Logger.out.error(e.getMessage(),e);
				e.printStackTrace();
			}
		}	 
	 
	 private void testAddDerivedSpecimen()
	 {
		try
		{
			dataGenerator.initDerivedSpecimen();
			Specimen derivedSpecimenObj = (Specimen)dataGenerator.dataModelObjectMap.get("DERIVED_SPECIMEN");
			setLogger(derivedSpecimenObj);
			Logger.out.info("Inserting domain object------->"+derivedSpecimenObj);
			derivedSpecimenObj =  (Specimen) appService.createObject(derivedSpecimenObj);
			dataGenerator.dataModelObjectMap.put("DERIVED_SPECIMEN",derivedSpecimenObj);
			writeSuccessfullOperationToReport(derivedSpecimenObj,insertOperation+"testAddSpecimen");
			Logger.out.info(" Domain Object is successfully added ---->    ID:: " + derivedSpecimenObj.getId().toString());
			Logger.out.info(" Domain Object is successfully added ---->    Name:: " + derivedSpecimenObj.getLabel());
		}
		catch(Exception e)
			{
				writeFailureOperationsToReport("Specimen",insertOperation+"testAddSpecimen");
				Logger.out.error(e.getMessage(),e);
				e.printStackTrace();
			}
	}
	 
	 private void testAddOrderDetails()
	 	{
			try
			{
				dataGenerator.initOrder();
				OrderDetails orderObj = (OrderDetails) dataGenerator.dataModelObjectMap.get("ORDER_DETAILS");
		    	setLogger(orderObj);
		    	orderObj =  (OrderDetails) appService.createObject(orderObj);
		    	System.out.println("-----------------" + orderObj);
		    	dataModelObjectMap.put("ORDER_DETAILS",orderObj);
		    	writeSuccessfullOperationToReport(orderObj,insertOperation+"testAddOrderDetails");
				Logger.out.info(" Domain Object is successfully added ---->  Name:: " + orderObj.getName());
			}
			catch(Exception e)
			{
				writeFailureOperationsToReport("Order Details",insertOperation+"testAddOrderDetails");
				Logger.out.error(e.getMessage(),e);
				e.printStackTrace();
			}
		}
	 private void testAddOrderUpdate()
	 	{
			try
			{
				dataGenerator.initUpdateOrderDetails();
				OrderDetails orderObj = (OrderDetails) dataGenerator.dataModelObjectMap.get("ORDER_UPDATE");
		    	setLogger(orderObj);
		    	orderObj =  (OrderDetails) appService.createObject(orderObj);
		    	System.out.println("-----------------" + orderObj);
		    	dataModelObjectMap.put("ORDER_UPDATE",orderObj);
		    	writeSuccessfullOperationToReport(orderObj,insertOperation+"testAddOrderDetails");
				Logger.out.info(" Domain Object is successfully added ---->  Name:: " + orderObj.getName());
			}
			catch(Exception e)
			{
				writeFailureOperationsToReport("Order Update",insertOperation+"testAddUpdateOrderDetails");
				Logger.out.error(e.getMessage(),e);
				e.printStackTrace();
			}
		}
			
		private void testAddSpecimenArrayType()
		{
			try
			{
				dataGenerator.initSpecimenArrayType();
				SpecimenArrayType specimenArrayType1 = (SpecimenArrayType) dataGenerator.dataModelObjectMap.get("SPECIMEN_ARRAY_TYPE_1");				
				setLogger(specimenArrayType1);
				Logger.out.info("Inserting domain object------->"+specimenArrayType1);
				specimenArrayType1 =  (SpecimenArrayType) appService.createObject(specimenArrayType1);
				dataModelObjectMap.put("SpecimenArrayType",specimenArrayType1);
				
				SpecimenArrayType specimenArrayType2 = (SpecimenArrayType) dataGenerator.dataModelObjectMap.get("SPECIMEN_ARRAY_TYPE_2");
				setLogger(specimenArrayType2);
				Logger.out.info("Inserting domain object------->"+specimenArrayType2);
				specimenArrayType2 =  (SpecimenArrayType) appService.createObject(specimenArrayType2);
				dataModelObjectMap.put("SpecimenArrayType",specimenArrayType2);
				
				writeSuccessfullOperationToReport(specimenArrayType1,insertOperation+"testAddSpecimenArrayType");
				Logger.out.info(" Domain Object is successfully added ---->    ID:: " + specimenArrayType1.getId().toString());
				Logger.out.info(" Domain Object is successfully added ---->    ID:: " + specimenArrayType2.getId().toString());
				//Logger.out.info(" Domain Object is successfully added ---->  ID:: " + specimenObj.getId().longValue() + " ::  Name :: " + specimenObj.getLabel());
			}
			catch(Exception e)
			{
				writeFailureOperationsToReport("SpecimenArrayType",insertOperation+"testAddSpecimenArrayType");
				Logger.out.error(e.getMessage(),e);
				e.printStackTrace();
			}
		}
	 
	 private void testAddSpecimenArray()
		{
			try
			{
				SpecimenArray specimenArrayObj = (SpecimenArray) api.initSpecimenArray();
				setLogger(specimenArrayObj);
				Logger.out.info("Inserting domain object------->"+specimenArrayObj);
				specimenArrayObj =  (SpecimenArray) appService.createObject(specimenArrayObj);
				dataModelObjectMap.put("SpecimenArray",specimenArrayObj);
				writeSuccessfullOperationToReport(specimenArrayObj,insertOperation+"testAddSpecimenArray");
				Logger.out.info(" Domain Object is successfully added ---->    ID:: " + specimenArrayObj.getId().toString());
		//		Logger.out.info(" Domain Object is successfully added ---->  ID:: " + specimenArrayObj.getId().longValue() + " ::  Name :: " + specimenArrayObj.getName());
			}
			catch(Exception e)
			{
				writeFailureOperationsToReport("SpecimenArray",insertOperation+"testAddSpecimenArray");
				Logger.out.error(e.getMessage(),e);
				e.printStackTrace();
			}
		}

	 	private void testAddSpecimenCollectionGroup()
		{
			try
			{
				dataGenerator.initSpecimenCollectionGroup();
				SpecimenCollectionGroup specimenCollectionGroupObj1 = (SpecimenCollectionGroup) dataGenerator.dataModelObjectMap.get("SCG1");
				SpecimenCollectionGroup specimenCollectionGroupObj2 = (SpecimenCollectionGroup) dataGenerator.dataModelObjectMap.get("SCG2");
				SpecimenCollectionGroup specimenCollectionGroupObj3 = (SpecimenCollectionGroup) dataGenerator.dataModelObjectMap.get("SCG3");
				SpecimenCollectionGroup specimenCollectionGroupObj4 = (SpecimenCollectionGroup) dataGenerator.dataModelObjectMap.get("SCG4");
				SpecimenCollectionGroup specimenCollectionGroupObj5 = (SpecimenCollectionGroup) dataGenerator.dataModelObjectMap.get("SCG5");
		    	
				setLogger(specimenCollectionGroupObj1);
		    	Logger.out.info("Inserting domain object------->"+specimenCollectionGroupObj1);
		    	specimenCollectionGroupObj1 =  (SpecimenCollectionGroup) appService.createObject(specimenCollectionGroupObj1);
		    	dataGenerator.dataModelObjectMap.put("SCG1",specimenCollectionGroupObj1);
				writeSuccessfullOperationToReport(specimenCollectionGroupObj1,insertOperation+"testAddSpecimenCollectionGroup");
				Logger.out.info(" Domain Object is successfully added ---->    ID:: " + specimenCollectionGroupObj1.getId().toString());
				
				setLogger(specimenCollectionGroupObj2);
				Logger.out.info("Inserting domain object------->"+specimenCollectionGroupObj2);
				specimenCollectionGroupObj2 =  (SpecimenCollectionGroup) appService.createObject(specimenCollectionGroupObj2);
				dataGenerator.dataModelObjectMap.put("SCG2",specimenCollectionGroupObj2);
				writeSuccessfullOperationToReport(specimenCollectionGroupObj2,insertOperation+"testAddSpecimenCollectionGroup");
				Logger.out.info(" Domain Object is successfully added ---->    ID:: " + specimenCollectionGroupObj2.getId().toString());
				
				setLogger(specimenCollectionGroupObj3);
		    	Logger.out.info("Inserting domain object------->"+specimenCollectionGroupObj3);
				specimenCollectionGroupObj3 =  (SpecimenCollectionGroup) appService.createObject(specimenCollectionGroupObj3);
				dataGenerator.dataModelObjectMap.put("SCG3",specimenCollectionGroupObj3);
				writeSuccessfullOperationToReport(specimenCollectionGroupObj3,insertOperation+"testAddSpecimenCollectionGroup");
				Logger.out.info(" Domain Object is successfully added ---->    ID:: " + specimenCollectionGroupObj3.getId().toString());
				
				setLogger(specimenCollectionGroupObj4);
		    	Logger.out.info("Inserting domain object------->"+specimenCollectionGroupObj4);
				specimenCollectionGroupObj4 =  (SpecimenCollectionGroup) appService.createObject(specimenCollectionGroupObj4);
				dataGenerator.dataModelObjectMap.put("SCG4",specimenCollectionGroupObj4);
				writeSuccessfullOperationToReport(specimenCollectionGroupObj4,insertOperation+"testAddSpecimenCollectionGroup");
				Logger.out.info(" Domain Object is successfully added ---->    ID:: " + specimenCollectionGroupObj4.getId().toString());
				
				setLogger(specimenCollectionGroupObj5);
		    	Logger.out.info("Inserting domain object------->"+specimenCollectionGroupObj5);
				specimenCollectionGroupObj5 =  (SpecimenCollectionGroup) appService.createObject(specimenCollectionGroupObj5);
				dataGenerator.dataModelObjectMap.put("SCG5",specimenCollectionGroupObj5);
				writeSuccessfullOperationToReport(specimenCollectionGroupObj5,insertOperation+"testAddSpecimenCollectionGroup");
				Logger.out.info(" Domain Object is successfully added ---->    ID:: " + specimenCollectionGroupObj5.getId().toString());
			}
			catch(Exception e)
			{
				writeFailureOperationsToReport("SpecimenCollectionGroup",insertOperation+"testAddSpecimenCollectionGroup");
				Logger.out.error(e.getMessage(),e);
				e.printStackTrace();
			}
		}
	 	
	 	
	 	
////////////////////////////////  End Add operation /////////////////	 	
    
    private void serachObject()
    {
		reportContents.append(newLine);
    	api = new APIDemo();
    	testSearchInstitution();
    	testSearchDepartment();
    	testSearchCancerResearchGroup();
    	testSearchBioHazard();
    	testSearchUser();    	
    	testSearchSite();
    	testSearchCollectionProtocol();
    	testSearchDistributionProtocol();
    	testSearchStorageType();
    	testSearchStorageContainer();
    	testSearchSpecimenArrayType();    	
    	testSearchParticipant();
    	testSearchCollectionProtocolRegistration();
    	testSearchSpecimenCollectionGroup();
    	testSearchSpecimen();
    	testSearchSpecimenArray();
    	//testSearchDistribution();
    	
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
    	
//    	try
//    	{
//    		
//    	MolecularSpecimen specimen = new MolecularSpecimen();
//    	specimen.setId(new Long(1));
//    	
//    	List list = appService.search(MolecularSpecimen.class,specimen);
//    	if(list != null && list.size() != 0)
//    	{
//    		System.out.println("List Size : " + list.size());
//    		specimen = (MolecularSpecimen)list.get(0);
//    		System.out.println("Type : " + specimen.getType() + " :: Id :" + 
//    	specimen.getId());
//    		System.out.println( " "  + specimen.getSpecimenCollectionGroup().getClinicalReport().getSurgicalPathologyNumber());
//    	}
//    	else
//    	{
//    		System.out.println("List is empty.");
//    	}
//    	} 
//    	catch(ApplicationException e)
//    	{
//    		e.printStackTrace();
//    	}
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
        		 writeSuccessfullOperationToReport(returneddepartment,searchOperation);
             }
          }
          catch (Exception e) {
        	  writeFailureOperationsToReport("Department",searchOperation);
        	  Logger.out.error(e.getMessage(),e);
        	  e.printStackTrace();
          }

    }
    private void testSearchCancerResearchGroup()
    {
    	CancerResearchGroup cachedObject =(CancerResearchGroup) dataModelObjectMap.get("CancerResearchGroup");
    	CancerResearchGroup cancerResearchGroup = new CancerResearchGroup();
    	cancerResearchGroup.setId(cachedObject.getId());
    	setLogger(cancerResearchGroup);
     	Logger.out.info(" searching domain object");
     	//cancerResearchGroup.setId(new Long(1));
         try {
        	 List resultList = appService.search(CancerResearchGroup.class, cancerResearchGroup);
        	 for (Iterator resultsIterator = resultList.iterator(); resultsIterator.hasNext();) {
        		 CancerResearchGroup returnedcancerResearchGroup = (CancerResearchGroup) resultsIterator.next();
        		 writeSuccessfullOperationToReport(returnedcancerResearchGroup,searchOperation);
        		 Logger.out.info(" Domain Object is found by Serach operation:: Name --- >" + returnedcancerResearchGroup.getName());
             }
          } 
          catch (Exception e) {
        	 writeFailureOperationsToReport("CancerResearchGroup",searchOperation);
          	 Logger.out.error(e.getMessage(),e);
  	 		 e.printStackTrace();
          }

    }
	   
    private void testSearchSite()
    {
    	Site cachedObject = (Site) dataModelObjectMap.get("Site");
    	Site site = new Site();
    	site.setId(cachedObject.getId());
   	 	setLogger(site);
     	Logger.out.info(" searching domain object");
     	//site.setId(new Long(1));
         try {
        	 List resultList = appService.search(Site.class,site);
        	 for (Iterator resultsIterator = resultList.iterator(); resultsIterator.hasNext();) {
        		 Site returnedsite = (Site) resultsIterator.next();
        		 writeSuccessfullOperationToReport(returnedsite,searchOperation);
        		 Logger.out.info(" Domain Object is successfully Found ---->  :: " + returnedsite.getName());
             }
          } 
          catch (Exception e) {
        	writeFailureOperationsToReport("Site",searchOperation);
          	Logger.out.error(e.getMessage(),e);
	 		e.printStackTrace();
          }

    }
    private void testSearchUser()
    {
    	 User cachedObject = (User) dataModelObjectMap.get("User");
    	 User user = (User) new User();
     	 setLogger(user);
     	 Logger.out.info(" searching domain object");
    	 user.setId(cachedObject.getId());
    	 //user.setId(new Long(1));
    	 
         try {
        	 List resultList = appService.search(User.class,user);
        	 for (Iterator resultsIterator = resultList.iterator(); resultsIterator.hasNext();) {
        		 User returneduser = (User) resultsIterator.next();
        		 writeSuccessfullOperationToReport(returneduser,searchOperation);
        		 Logger.out.info(" Domain Object is successfully Found ---->  :: " + returneduser.getEmailAddress());
             }
          } 
          catch (Exception e) {
        	writeFailureOperationsToReport("User",searchOperation);  
          	Logger.out.error(e.getMessage(),e);
	 		e.printStackTrace();
          }

    } 
    private void testSearchParticipant()
    {
    	 Participant cachedObject = (Participant)dataModelObjectMap.get("Participant");
    	 Participant participant = new Participant();
     	 setLogger(participant);
    	 Logger.out.info(" searching domain object");
    	 participant.setId(cachedObject.getId());
    	 //participant.setId(new Long(3));
         try {
        	 List resultList = appService.search(Participant.class,participant);
        	 for (Iterator resultsIterator = resultList.iterator(); resultsIterator.hasNext();) {
        		 Participant returnedparticipant = (Participant) resultsIterator.next();
        		 writeSuccessfullOperationToReport(returnedparticipant,searchOperation);
        		 Logger.out.info(" Domain Object is successfully Found ---->  :: " + returnedparticipant.getFirstName());
             }
          } 
          catch (Exception e) {
        	writeFailureOperationsToReport("Participant",searchOperation);  
          	Logger.out.error(e.getMessage(),e);
	 		e.printStackTrace();
          }

    }
    private void testSearchInstitution()
    {
    	Institution cachedObject = (Institution)dataModelObjectMap.get("Institution");
    	Institution institution = new Institution();
    	setLogger(institution);
    	Logger.out.info(" searching domain object");
    	institution.setId(cachedObject.getId());//institution.setId(new Long(1));
         try {
        	 List resultList = appService.search(Institution.class,institution);
        	 for (Iterator resultsIterator = resultList.iterator(); resultsIterator.hasNext();) {
        		 Institution returnedinstitution = (Institution) resultsIterator.next();
        		 writeSuccessfullOperationToReport(returnedinstitution,searchOperation);
        		 Logger.out.info(" Domain Object is successfully Found ---->  :: " + returnedinstitution.getName());
             }
          } 
          catch (Exception e) {
        	writeFailureOperationsToReport("Institution",searchOperation);  
          	Logger.out.error(e.getMessage(),e);
	 		e.printStackTrace();
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
        		 writeSuccessfullOperationToReport(returnedbiohazard,searchOperation);
        		 Logger.out.info(" Domain Object is successfully Found ---->  :: " + returnedbiohazard.getName());
             }
          } 
          catch (Exception e) {
        	writeFailureOperationsToReport("Biohazard",searchOperation);    
          	Logger.out.error(e.getMessage(),e);
	 		e.printStackTrace();
          }

    }
    private void testSearchDistribution()
    {
    	Distribution cachedDistribution =(Distribution)dataModelObjectMap.get("Distribution");
    	Distribution distribution = new Distribution();
    	setLogger(distribution);
    	Logger.out.info(" searching Distribution object id-----------" + cachedDistribution.getId());
    	distribution.setId(cachedDistribution.getId());
         try {
        	 List resultList = appService.search(Distribution.class,distribution);
        	 Logger.out.info(" Domain Object is successfully Found size ---->  :: " + resultList.size());
        	 for (Iterator resultsIterator = resultList.iterator(); resultsIterator.hasNext();) {
        		 Distribution returneddistribution = (Distribution) resultsIterator.next();        		 
        		 writeSuccessfullOperationToReport(returneddistribution,searchOperation);
        		 Logger.out.info(" Domain Object is successfully Found ---->  :: " + returneddistribution.getId());        		 
        		 Logger.out.info(" Domain Object is successfully Found ---->  :: " + returneddistribution.getMessageLabel());
             }
          } 
          catch (Exception e) {
        	writeFailureOperationsToReport("Distribution",searchOperation);    
          	Logger.out.error(e.getMessage(),e);
	 		e.printStackTrace();
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
        		 writeSuccessfullOperationToReport(returneddistributionprotocol,searchOperation);
        		 Logger.out.info(" Domain Object is successfully Found ---->  :: " + returneddistributionprotocol.getTitle());
             }
          } 
          catch (Exception e) {
        	writeFailureOperationsToReport("DistributionProtocol",searchOperation);   
          	Logger.out.error(e.getMessage(),e);
	 		e.printStackTrace();
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
        		 writeSuccessfullOperationToReport(returnedcollectionprotocol,searchOperation);
        		 Logger.out.info(" Domain Object is successfully Found ---->  :: " + returnedcollectionprotocol.getTitle());
             }
          } 
          catch (Exception e) {
        	  writeFailureOperationsToReport("CollectionProtocol",searchOperation);  
        	Logger.out.error(e.getMessage(),e);
	 		e.printStackTrace();
          }

    }
    private void testSearchCollectionProtocolRegistration()
    {
    	CollectionProtocolRegistration cachedcollectionProtocolRegistration =(CollectionProtocolRegistration)dataModelObjectMap.get("CollectionProtocolRegistration");
    	CollectionProtocolRegistration collectionProtocolRegistration  =new CollectionProtocolRegistration();
    	setLogger(collectionProtocolRegistration);
    	Logger.out.info(" searching domain object");
	    collectionProtocolRegistration.setId(cachedcollectionProtocolRegistration.getId());
         try {
        	 List resultList = appService.search(CollectionProtocolRegistration.class,collectionProtocolRegistration);
        	 for (Iterator resultsIterator = resultList.iterator(); resultsIterator.hasNext();) {
        		 CollectionProtocolRegistration returnedcollectionprotocolregistration = (CollectionProtocolRegistration) resultsIterator.next();
        		 writeSuccessfullOperationToReport(returnedcollectionprotocolregistration,searchOperation);
        		 Logger.out.info(" Domain Object is successfully Found ---->  :: " + returnedcollectionprotocolregistration.getMessageLabel());
             }
          } 
          catch (Exception e) {
        	  writeFailureOperationsToReport("CollectionProtocolRegistration",searchOperation);    
          	Logger.out.error(e.getMessage(),e);
	 		e.printStackTrace();
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
        		 writeSuccessfullOperationToReport(returnedstoragetype,searchOperation);
        		 Logger.out.info(" Domain Object is successfully Found ---->  :: " +returnedstoragetype.getName());
             }
          } 
          catch (Exception e) {
          	Logger.out.error(e.getMessage(),e);
	 		e.printStackTrace();
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
        		 writeSuccessfullOperationToReport(returnedstoragecontainer,searchOperation);
        		 Logger.out.info(" Domain Object is successfully Found ---->  :: " + returnedstoragecontainer.getName());
             }
          } 
          catch (Exception e) {
        	  writeFailureOperationsToReport("StorageContainer",searchOperation);   
          	Logger.out.error(e.getMessage(),e);
	 		e.printStackTrace();
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
        		 writeSuccessfullOperationToReport(returnedspecimen,searchOperation);
        		 Logger.out.info(" Domain Object is successfully Found ---->  :: " + returnedspecimen.getLabel());
             }
          } 
          catch (Exception e) {
        	  writeFailureOperationsToReport("Specimen",searchOperation);  
          	Logger.out.error(e.getMessage(),e);
	 		e.printStackTrace();
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
        		 writeSuccessfullOperationToReport(returnedspecimenarraytype,searchOperation);
        		 Logger.out.info(" Domain Object is successfully Found ---->  :: " + returnedspecimenarraytype.getName());
             }
          } 
          catch (Exception e) {
        	  writeFailureOperationsToReport("SpecimenArrayType",searchOperation);   
          	Logger.out.error(e.getMessage(),e);
	 		e.printStackTrace();
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
        		 writeSuccessfullOperationToReport(returnedspecimenarray,searchOperation);
        		 Logger.out.info(" Domain Object is successfully Found ---->  :: " +returnedspecimenarray.getName());
             }
          } 
          catch (Exception e) {
        	  writeFailureOperationsToReport("SpecimenArray",searchOperation);     
          	Logger.out.error(e.getMessage(),e);
	 		e.printStackTrace();
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
        		 writeSuccessfullOperationToReport(returnedspecimencollectiongroup,searchOperation);
        		 Logger.out.info(" Domain Object is successfully Found ---->  :: " + returnedspecimencollectiongroup.getName());
             }
          } 
          catch (Exception e) {
        	writeFailureOperationsToReport("SpecimenCollectionGroup",searchOperation);    
          	Logger.out.error(e.getMessage(),e);
	 		e.printStackTrace();
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
	private void updateObjects()
	{
				reportContents.append(newLine);
				
				testUpdateInstitution();
				testUpdateDepartment();
				testUpdateCancerResearchGroup();
				testUpdateBiohazard();
				testUpdateSite();				
				testUpdateCollectionProtocol();
				testUpdateDistributionProtocol();				
				testUpdateParticipant();
				testUpdateCollectionProtocolRegistration();
				testUpdateSpecimenCollectionGroup();
				testUpdateSpecimen();
				testUpdateStorageType();
				testUpdateStorageContainer();
				testUpdateSpecimenArrayType();
				testUpdateSpecimenArray();
				
				
				testUpdateInstitutionWithWrongData();
				testUpdateDepartmentWithWrongData();
				testUpdateCancerResearchGroupWithWrongData();
				testUpdateBiohazardWithWrongData();
				testUpdateSiteWithWrongData();				
				testUpdateCollectionProtocolWithWrongData();				
				testUpdateDistributionProtocolWithWrongData();				
				testUpdateParticipantWithWrongData();				
				testUpdateCollectionProtocolRegistrationWithWrongData();
				testUpdateSpecimenCollectionGroupWithWrongData();						
				testUpdateSpecimenWithWrongData();
				
				/*Object obj = api.initCancerResearchGroup();
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
				appService.updateObject(domainObject);*/

	}

	/*private AbstractDomainObject setId(Object obj,Long id)
	{
		AbstractDomainObject domainObject = (AbstractDomainObject) obj;
		domainObject.setId(id);
		return domainObject;
	}*/

	private void testUpdateInstitution()
	{
		Institution institution = (Institution)dataModelObjectMap.get("Institution");
    	setLogger(institution);
    	Logger.out.info("updating domain object------->"+institution);
	    try 
		{
	    	APIDemo apiDemo = new APIDemo();
	    	apiDemo.updateInstitution(institution);	  
	    	//institution.setName("inst"+UniqueKeyGeneratorUtil.getUniqueKey());
	     	Institution updatedInstitution = (Institution) appService.updateObject(institution);
	     	writeSuccessfullOperationToReport(updatedInstitution,updateOperation+"testUpdateInstitution");
	     	Logger.out.info("Domain object successfully updated ---->"+updatedInstitution);
	    } 
	    catch (Exception e) {
	    	writeFailureOperationsToReport("Institution",updateOperation+"testUpdateInstitution");
	    	Logger.out.error(e.getMessage(),e);
	 		e.printStackTrace();
	    }
	}
	
	private void testUpdateInstitutionWithWrongData()
	{
		Institution institution = (Institution)dataModelObjectMap.get("Institution");
    	setLogger(institution);
    	Logger.out.info("updating domain object------->"+institution);
	    try 
		{
	    	APIDemo apiDemo = new APIDemo();
	    	institution.setName(null);
	    	//apiDemo.updateInstitution(institution);	  
	    	//institution.setName("inst"+UniqueKeyGeneratorUtil.getUniqueKey());
	     	Institution updatedInstitution = (Institution) appService.updateObject(institution);
	     	writeFailureOperationsToReport("Institution",updateValidateOperation + " testUpdateInstitutionWithWrongData");
	     	//Logger.out.info("Domain object successfully updated ---->"+updatedInstitution);
	    } 
	    catch (Exception e) {
	    	writeSuccessfullOperationToReport(institution,updateValidateOperation + " testUpdateInstitutionWithWrongData");
	    	Logger.out.error(e.getMessage(),e);
	 		e.printStackTrace();
	    }
	}
	
	private void testUpdateDepartment()
	{
		Department department = (Department)dataModelObjectMap.get("Department");
    	setLogger(department);
    	Logger.out.info("updating domain object------->"+department);
	    try 
		{
	    	APIDemo apiDemo = new APIDemo();
	    	apiDemo.updateDepartment(department);	    	
	    	//department.setName("dt"+UniqueKeyGeneratorUtil.getUniqueKey());
	    	Department updatedDepartment = (Department) appService.updateObject(department);
	    	writeSuccessfullOperationToReport(updatedDepartment,updateOperation+"testUpdateDepartment");
	     	Logger.out.info("Domain object successfully updated ---->"+updatedDepartment);
	    } 
	    catch (Exception e) {
	    	writeFailureOperationsToReport("Department",updateOperation+"testUpdateDepartment");
	    	Logger.out.error(e.getMessage(),e);
	 		e.printStackTrace();
	    }
	}
	
	private void testUpdateDepartmentWithWrongData()
	{
		Department department = (Department)dataModelObjectMap.get("Department");
    	setLogger(department);
    	Logger.out.info("updating domain object------->"+department);
	    try 
		{
	    	APIDemo apiDemo = new APIDemo();
	    	department.setName(null);
	    	//apiDemo.updateDepartment(department);	    	
	    	//department.setName("dt"+UniqueKeyGeneratorUtil.getUniqueKey());
	    	Department updatedDepartment = (Department) appService.updateObject(department);
	    	writeFailureOperationsToReport("Department",updateValidateOperation + " testUpdateDepartmentWithWrongData");
	     	//Logger.out.info("Domain object successfully updated ---->"+updatedDepartment);
	    } 
	    catch (Exception e) {
	    	writeSuccessfullOperationToReport(department,updateValidateOperation + " testUpdateDepartmentWithWrongData");
	    	Logger.out.error(e.getMessage(),e);
	 		e.printStackTrace();
	    }
	}
	
	private void testUpdateCancerResearchGroup()
	{
		CancerResearchGroup cancerResearchGroup = (CancerResearchGroup)dataModelObjectMap.get("CancerResearchGroup");
    	setLogger(cancerResearchGroup);
    	Logger.out.info("updating domain object------->"+cancerResearchGroup);
	    try 
		{
	    	APIDemo apiDemo = new APIDemo();
	    	apiDemo.updateCancerResearchGroup(cancerResearchGroup);	    	
	    	//cancerResearchGroup.setName("crg"+UniqueKeyGeneratorUtil.getUniqueKey());
	    	CancerResearchGroup updatedCancerResearchGroup = (CancerResearchGroup) appService.updateObject(cancerResearchGroup);
	    	writeSuccessfullOperationToReport(updatedCancerResearchGroup,updateOperation+"testUpdateCancerResearchGroup");
	    	Logger.out.info("Domain object successfully updated ---->"+updatedCancerResearchGroup);
	    } 
	    catch (Exception e) {
	    	writeFailureOperationsToReport("CancerResearchGroup",updateOperation+"testUpdateCancerResearchGroup");
	    	Logger.out.error(e.getMessage(),e);
	 		e.printStackTrace();
	    }
	}
	
	private void testUpdateCancerResearchGroupWithWrongData()
	{
		CancerResearchGroup cancerResearchGroup = (CancerResearchGroup)dataModelObjectMap.get("CancerResearchGroup");
    	setLogger(cancerResearchGroup);
    	Logger.out.info("updating domain object------->"+cancerResearchGroup);
	    try 
		{
	    	APIDemo apiDemo = new APIDemo();
	    	cancerResearchGroup.setName(null);
	    	//apiDemo.updateCancerResearchGroup(cancerResearchGroup);	    	
	    	//cancerResearchGroup.setName("crg"+UniqueKeyGeneratorUtil.getUniqueKey());
	    	CancerResearchGroup updatedCancerResearchGroup = (CancerResearchGroup) appService.updateObject(cancerResearchGroup);
	    	writeFailureOperationsToReport("CancerResearchGroup",updateValidateOperation+ " testUpdateCancerResearchGroupWithWrongData");
	     	//Logger.out.info("Domain object successfully updated ---->"+updatedCancerResearchGroup);
	    } 
	    catch (Exception e) {
	    	writeSuccessfullOperationToReport(cancerResearchGroup,updateValidateOperation + " testUpdateCancerResearchGroupWithWrongData");
	    	Logger.out.error(e.getMessage(),e);
	 		e.printStackTrace();
	    }
	}
	
	private void testUpdateBiohazard()
	{
		Biohazard biohazard = (Biohazard)dataModelObjectMap.get("Biohazard");
    	setLogger(biohazard);
    	Logger.out.info("updating domain object------->"+biohazard);
	    try 
		{
	    	APIDemo apiDemo = new APIDemo();
	    	apiDemo.updateBiohazard(biohazard);
	    	Biohazard updatedBiohazard = (Biohazard) appService.updateObject(biohazard);
	    	writeSuccessfullOperationToReport(updatedBiohazard,updateOperation+"testUpdateBiohazard");
	    	Logger.out.info("Domain object successfully updated ---->"+updatedBiohazard);
	    } 
	    catch (Exception e) {
	    	writeFailureOperationsToReport("Biohazard",updateOperation+"testUpdateBiohazard");
	    	Logger.out.error(e.getMessage(),e);
	 		e.printStackTrace();
	    }
	}
	
	
	private void testUpdateBiohazardWithWrongData()
	{
		Biohazard biohazard = (Biohazard)dataModelObjectMap.get("Biohazard");
    	setLogger(biohazard);
    	Logger.out.info("updating domain object------->"+biohazard);
	    try 
		{
	    	APIDemo apiDemo = new APIDemo();
	    	biohazard.setType("wrongData");
	    	//apiDemo.updateBiohazard(biohazard);
	    	Biohazard updatedBiohazard = (Biohazard) appService.updateObject(biohazard);
	    	writeSuccessfullOperationToReport(updatedBiohazard,updateValidateOperation + " testUpdateBiohazardWithWrongData");
	     	//Logger.out.info("Domain object successfully updated ---->"+updatedBiohazard);
	    } 
	    catch (Exception e) {
	    	writeSuccessfullOperationToReport(biohazard,updateValidateOperation + " testUpdateBiohazardWithWrongData");
	    	Logger.out.error(e.getMessage(),e);
	 		e.printStackTrace();
	    }
	}
	
	private void testUpdateSite()
	{
		Site site = (Site)dataModelObjectMap.get("Site");
    	setLogger(site);
    	Logger.out.info("updating domain object------->"+site);
	    try 
		{
	    	APIDemo apiDemo = new APIDemo();
	    	apiDemo.updateSite(site);
	    	Site updatedSite = (Site) appService.updateObject(site);
	    	writeSuccessfullOperationToReport(updatedSite,updateOperation+"testUpdateSite");
	    	Logger.out.info("Domain object successfully updated ---->"+updatedSite);
	    } 
	    catch (Exception e) {
	    	writeFailureOperationsToReport("Site",updateOperation+"testUpdateSite");
	    	Logger.out.error(e.getMessage(),e);
	 		e.printStackTrace();
	    }
	}
	
	private void testUpdateSiteWithWrongData()
	{
		Site site = (Site)dataModelObjectMap.get("Site");
    	setLogger(site);
    	Logger.out.info("updating domain object------->"+site);
	    try 
		{
	    	APIDemo apiDemo = new APIDemo();
	    	site.setType("wrongData");
	    	//apiDemo.updateSite(site);
	    	Site updatedSite = (Site) appService.updateObject(site);
	    	writeFailureOperationsToReport("Site",updateValidateOperation + " testUpdateSiteWithWrongData");
	     	//Logger.out.info("Domain object successfully updated ---->"+updatedSite);
	    } 
	    catch (Exception e) {
	    	writeSuccessfullOperationToReport(site,updateValidateOperation + " testUpdateSiteWithWrongData");
	    	Logger.out.error(e.getMessage(),e);
	 		e.printStackTrace();
	    }
	}
	
	private void testUpdateCollectionProtocol()
	{
		CollectionProtocol collectionProtocol = (CollectionProtocol)dataModelObjectMap.get("CollectionProtocol");
    	setLogger(collectionProtocol);
    	Logger.out.info("updating domain object------->"+collectionProtocol);
	    try 
		{
	    	APIDemo apiDemo = new APIDemo();
	    	apiDemo.updateCollectionProtocol(collectionProtocol);
	    	CollectionProtocol updatedCollectionProtocol = (CollectionProtocol) appService.updateObject(collectionProtocol);
	    	writeSuccessfullOperationToReport(updatedCollectionProtocol,updateOperation+"testUpdateCollectionProtocol");
	    	Logger.out.info("Domain object successfully updated ---->"+updatedCollectionProtocol);
	    } 
	    catch (Exception e) {
	    	writeFailureOperationsToReport("CollectionProtocol",updateOperation+"testUpdateCollectionProtocol");
	    	Logger.out.error(e.getMessage(),e);
	 		e.printStackTrace();
	    }
	}
	
	private void testUpdateCollectionProtocolWithWrongData()
	{
		CollectionProtocol collectionProtocol = (CollectionProtocol)dataModelObjectMap.get("CollectionProtocol");
    	setLogger(collectionProtocol);
    	Logger.out.info("updating domain object------->"+collectionProtocol);
	    try 
		{
	    	APIDemo apiDemo = new APIDemo();
	    	collectionProtocol.setTitle(null);
	    	//apiDemo.updateCollectionProtocol(collectionProtocol);
	    	CollectionProtocol updatedCollectionProtocol = (CollectionProtocol) appService.updateObject(collectionProtocol);
	    	writeFailureOperationsToReport("CollectionProtocol",updateValidateOperation + " testUpdateCollectionProtocolWithWrongData");
	    	//Logger.out.info("Domain object successfully updated ---->"+updatedCollectionProtocol);
	    } 
	    catch (Exception e) {
	    	writeSuccessfullOperationToReport(collectionProtocol,updateValidateOperation + " testUpdateCollectionProtocolWithWrongData");
	    	Logger.out.error(e.getMessage(),e);
	 		e.printStackTrace();
	    }
	}
	
	private void testUpdateDistributionProtocol()
	{
		DistributionProtocol distributionProtocol = (DistributionProtocol)dataModelObjectMap.get("DistributionProtocol");
    	setLogger(distributionProtocol);
    	Logger.out.info("updating domain object------->"+distributionProtocol);
	    try 
		{
	    	APIDemo apiDemo = new APIDemo();
	    	apiDemo.updateDistributionProtocol(distributionProtocol);
	    	DistributionProtocol updatedDistributionProtocol = (DistributionProtocol) appService.updateObject(distributionProtocol);
	    	writeSuccessfullOperationToReport(updatedDistributionProtocol,updateOperation+"testUpdateDistributionProtocol");
	    	Logger.out.info("Domain object successfully updated ---->"+updatedDistributionProtocol);
	    } 
	    catch (Exception e) {
	    	writeFailureOperationsToReport("DistributionProtocol",updateOperation+"testUpdateDistributionProtocol");
	    	Logger.out.error(e.getMessage(),e);
	 		e.printStackTrace();
	    }
	}
	
	private void testUpdateDistributionProtocolWithWrongData()
	{
		DistributionProtocol distributionProtocol = (DistributionProtocol)dataModelObjectMap.get("DistributionProtocol");
    	setLogger(distributionProtocol);
    	Logger.out.info("updating domain object------->"+distributionProtocol);
	    try 
		{
	    	APIDemo apiDemo = new APIDemo();
	    	distributionProtocol.setShortTitle(null);
	    	//apiDemo.updateDistributionProtocol(distributionProtocol);
	    	DistributionProtocol updatedDistributionProtocol = (DistributionProtocol) appService.updateObject(distributionProtocol);
	    	writeFailureOperationsToReport("DistributionProtocol",updateValidateOperation + " testUpdateDistributionProtocolWithWrongData");
	    	//Logger.out.info("Domain object successfully updated ---->"+updatedDistributionProtocol);
	    } 
	    catch (Exception e) {
	    	writeSuccessfullOperationToReport(distributionProtocol,updateValidateOperation + " testUpdateDistributionProtocolWithWrongData");
	    	Logger.out.error(e.getMessage(),e);
	 		e.printStackTrace();
	    }
	}
	
	private void testUpdateParticipant()
	{
		Participant participant = (Participant) dataModelObjectMap.get("Participant");
		setLogger(participant);
    	Logger.out.info("updating domain object------->"+participant);
	    try 
		{
	    	APIDemo apiDemo = new APIDemo();
	    	apiDemo.updateParticipant(participant);
	    	Participant updatedParticipant = (Participant) appService.updateObject(participant);
	    	writeSuccessfullOperationToReport(updatedParticipant,updateOperation+"testUpdateParticipant");
	    	Logger.out.info("Domain object successfully updated ---->"+updatedParticipant);
	    } 
	    catch (Exception e) {
	    	writeFailureOperationsToReport("Participant",updateOperation+"testUpdateParticipant");
	    	Logger.out.error(e.getMessage(),e);
	 		e.printStackTrace();
	    }
	}
	
	private void testUpdateParticipantWithWrongData()
	{
		Participant participant = (Participant) dataModelObjectMap.get("Participant");
		setLogger(participant);
    	Logger.out.info("updating domain object------->"+participant);
	    try 
		{
	    	APIDemo apiDemo = new APIDemo();
	    	participant.setGender("wrongData");
	    	//apiDemo.updateParticipant(participant);
	    	Participant updatedParticipant = (Participant) appService.updateObject(participant);
	    	writeFailureOperationsToReport("Participant",updateValidateOperation + " testUpdateParticipantWithWrongData");
	     	//Logger.out.info("Domain object successfully updated ---->"+updatedParticipant);
	    } 
	    catch (Exception e) {
	    	writeSuccessfullOperationToReport(participant,updateValidateOperation + " testUpdateParticipantWithWrongData");
	    	Logger.out.error(e.getMessage(),e);
	 		e.printStackTrace();
	    }
	}
	
	private void testUpdateSpecimenCollectionGroup()
	{
		SpecimenCollectionGroup specimenCollectionGroup = (SpecimenCollectionGroup) dataModelObjectMap.get("SpecimenCollectionGroup");
		setLogger(specimenCollectionGroup);
		Logger.out.info("updating domain object------->"+specimenCollectionGroup);
		try 
		{
			APIDemo apiDemo = new APIDemo();
			apiDemo.updateSpecimenCollectionGroup(specimenCollectionGroup);
			SpecimenCollectionGroup updatedSpecimenCollectionGroup = (SpecimenCollectionGroup) appService.updateObject(specimenCollectionGroup);
			writeSuccessfullOperationToReport(updatedSpecimenCollectionGroup,updateOperation+"testUpdateSpecimenCollectionGroup");
			Logger.out.info("Domain object successfully updated ---->"+updatedSpecimenCollectionGroup);
		} 
		catch (Exception e) {
			writeFailureOperationsToReport("SpecimenCollectionGroup",updateOperation+"testUpdateSpecimenCollectionGroup");
			Logger.out.error(e.getMessage(),e);
	 		e.printStackTrace();
		}
	}
	
	private void testUpdateSpecimenCollectionGroupWithWrongData()
	{
		SpecimenCollectionGroup specimenCollectionGroup = (SpecimenCollectionGroup) dataModelObjectMap.get("SpecimenCollectionGroup");
		setLogger(specimenCollectionGroup);
		Logger.out.info("updating domain object------->"+specimenCollectionGroup);
		try 
		{
			APIDemo apiDemo = new APIDemo();
			specimenCollectionGroup.setClinicalStatus("wrongData");
			//apiDemo.updateSpecimenCollectionGroup(specimenCollectionGroup);
			SpecimenCollectionGroup updatedSpecimenCollectionGroup = (SpecimenCollectionGroup) appService.updateObject(specimenCollectionGroup);
			writeFailureOperationsToReport("SpecimenCollectionGroup",updateValidateOperation + " testUpdateSpecimenCollectionGroupWithWrongData");
			//Logger.out.info("Domain object successfully updated ---->"+updatedSpecimenCollectionGroup);
		} 
		catch (Exception e) {
			writeSuccessfullOperationToReport(specimenCollectionGroup,updateValidateOperation + " testUpdateSpecimenCollectionGroupWithWrongData");
			Logger.out.error(e.getMessage(),e);
	 		e.printStackTrace();
		}
	}
	
	private void testUpdateCollectionProtocolRegistration()
	{		
		CollectionProtocolRegistration collectionProtocolRegistration = (CollectionProtocolRegistration) dataModelObjectMap.get("CollectionProtocolRegistration");
		setLogger(collectionProtocolRegistration);
		Logger.out.info("updating domain object------->"+collectionProtocolRegistration);
		try 
		{
			APIDemo apiDemo = new APIDemo();
			apiDemo.updateCollectionProtocolRegistration(collectionProtocolRegistration);
			CollectionProtocolRegistration updatedCollectionProtocolRegistration = (CollectionProtocolRegistration) appService.updateObject(collectionProtocolRegistration);
			writeSuccessfullOperationToReport(updatedCollectionProtocolRegistration,updateOperation+"testUpdateCollectionProtocolRegistration");
			Logger.out.info("Domain object successfully updated ---->"+updatedCollectionProtocolRegistration);
		} 
		catch (Exception e) {
			writeFailureOperationsToReport("CollectionProtocolRegistration",updateOperation+"testUpdateCollectionProtocolRegistration");
			Logger.out.error(e.getMessage(),e);
	 		e.printStackTrace();
		}
	}
	
	private void testUpdateCollectionProtocolRegistrationWithWrongData()
	{		
		CollectionProtocolRegistration collectionProtocolRegistration = (CollectionProtocolRegistration) dataModelObjectMap.get("CollectionProtocolRegistration");
		setLogger(collectionProtocolRegistration);
		Logger.out.info("updating domain object------->"+collectionProtocolRegistration);
		try 
		{
			APIDemo apiDemo = new APIDemo();	
			
			collectionProtocolRegistration.setParticipant(null);
			collectionProtocolRegistration.setProtocolParticipantIdentifier(null);
			
			//apiDemo.updateCollectionProtocolRegistration(collectionProtocolRegistration);
			CollectionProtocolRegistration updatedCollectionProtocolRegistration = (CollectionProtocolRegistration) appService.updateObject(collectionProtocolRegistration);
			writeFailureOperationsToReport("CollectionProtocolRegistration",updateValidateOperation + " testUpdateCollectionProtocolRegistrationWithWrongData");
			//Logger.out.info("Domain object successfully updated ---->"+updatedCollectionProtocolRegistration);
		} 
		catch (Exception e) {
			writeSuccessfullOperationToReport(collectionProtocolRegistration,updateValidateOperation + " testUpdateCollectionProtocolRegistrationWithWrongData");
			Logger.out.error(e.getMessage(),e);
	 		e.printStackTrace();
		}
	}
	
	private void testUpdateSpecimen()
	{
		Specimen specimen = (Specimen)dataModelObjectMap.get("Specimen");
    	setLogger(specimen);
    	Logger.out.info("updating domain object------->"+specimen);
	    try 
		{
	    	APIDemo apiDemo = new APIDemo();
	    	apiDemo.updateSpecimen(specimen);
	    	Specimen updatedSpecimen = (Specimen) appService.updateObject(specimen);
	    	writeSuccessfullOperationToReport(updatedSpecimen,updateOperation+"testUpdateSpecimen");
	    	Logger.out.info("Domain object successfully updated ---->"+updatedSpecimen);
	    } 
	    catch (Exception e) {
	    	writeFailureOperationsToReport("Specimen",updateOperation+"testUpdateSpecimen");
	    	Logger.out.error(e.getMessage(),e);
	 		e.printStackTrace();
	    }
	}
	
	private void testUpdateSpecimenWithWrongData()
	{
		Specimen specimen = (Specimen)dataModelObjectMap.get("Specimen");
    	setLogger(specimen);
    	Logger.out.info("updating domain object------->"+specimen);
	    try 
		{
	    	APIDemo apiDemo = new APIDemo();	    	
	    	specimen.setPathologicalStatus("wrongData");
	    	
	    	//apiDemo.updateSpecimen(specimen);
	    	Specimen updatedSpecimen = (Specimen) appService.updateObject(specimen);
	    	writeFailureOperationsToReport("Specimen",updateValidateOperation + " testUpdateSpecimenWithWrongData");	    	
	    	//Logger.out.info("Domain object successfully updated ---->"+updatedSpecimen);
	    } 
	    catch (Exception e) {
	    	writeSuccessfullOperationToReport(specimen,updateValidateOperation + " testUpdateSpecimenWithWrongData");
	    	Logger.out.error(e.getMessage(),e);
	 		e.printStackTrace();
	    }
	}
	
	
	private void testUpdateStorageType()
	{
		StorageType storageType = (StorageType)dataModelObjectMap.get("StorageType");
    	setLogger(storageType);
    	Logger.out.info("updating domain object------->"+storageType);
	    try 
		{
	    	APIDemo apiDemo = new APIDemo();
	    	apiDemo.updateStorageType(storageType);
	    	StorageType updatedStorageType = (StorageType) appService.updateObject(storageType);
	    	writeSuccessfullOperationToReport(updatedStorageType,updateOperation+"testUpdateStorageType");
	    	Logger.out.info("Domain object successfully updated ---->"+updatedStorageType);
	    } 
	    catch (Exception e) {
	    	writeFailureOperationsToReport("StorageType",updateOperation+"testUpdateStorageType");
	    	Logger.out.error(e.getMessage(),e);
	 		e.printStackTrace();
	    }
	}	
	
	private void testUpdateStorageContainer()
	{
		StorageContainer storageContainer = (StorageContainer)dataModelObjectMap.get("StorageContainer");
    	setLogger(storageContainer);
    	Logger.out.info("updating domain object------->"+storageContainer);
    	Logger.out.info("updating domain object Storage Container ******* ------->"+storageContainer.getCapacity().getId());
	    try 
		{
	    	APIDemo apiDemo = new APIDemo();
	    	apiDemo.updateStorageContainer(storageContainer);
	    	StorageContainer updatedStorageContainer = (StorageContainer) appService.updateObject(storageContainer);
	    	writeSuccessfullOperationToReport(updatedStorageContainer,updateOperation+"testUpdateStorageContainer");
	    	Logger.out.info("Domain object successfully updated ---->"+updatedStorageContainer);
	    } 
	    catch (Exception e) {
	    	writeFailureOperationsToReport("StorageContainer",updateOperation+"testUpdateStorageContainer");
	    	Logger.out.error(e.getMessage(),e);
	 		e.printStackTrace();
	    }
	}	
	
	private void testUpdateSpecimenArrayType()
	{
		SpecimenArrayType specimenArrayType = (SpecimenArrayType)dataModelObjectMap.get("SpecimenArrayType");
    	setLogger(specimenArrayType);
    	Logger.out.info("updating domain object------->"+specimenArrayType);
	    try 
		{
	    	APIDemo apiDemo = new APIDemo();
	    	apiDemo.updateSpecimenArrayType(specimenArrayType);
	    	SpecimenArrayType updatedSpecimenArrayType = (SpecimenArrayType) appService.updateObject(specimenArrayType);
	    	writeSuccessfullOperationToReport(updatedSpecimenArrayType,updateOperation+"testUpdateSpecimenArrayType");
	    	Logger.out.info("Domain object successfully updated ---->"+updatedSpecimenArrayType);
	    } 
	    catch (Exception e) {
	    	writeFailureOperationsToReport("SpecimenArrayType",updateOperation+"testUpdateSpecimenArrayType");
	    	Logger.out.error(e.getMessage(),e);
	 		e.printStackTrace();
	    }
	}		
	
	private void testUpdateSpecimenArray()
	{
		SpecimenArray specimenArray = (SpecimenArray)dataModelObjectMap.get("SpecimenArray");
    	setLogger(specimenArray);
    	Logger.out.info("updating domain object------->"+specimenArray);
	    try 
		{
	    	APIDemo apiDemo = new APIDemo();
	    	apiDemo.updateSpecimenArray(specimenArray);
	    	SpecimenArray updatedSpecimenArray = (SpecimenArray) appService.updateObject(specimenArray);
	    	writeSuccessfullOperationToReport(updatedSpecimenArray,updateOperation+"testUpdateSpecimenArray");
	    	Logger.out.info("Domain object successfully updated ---->"+updatedSpecimenArray);
	    } 
	    catch (Exception e) {
	    	writeFailureOperationsToReport("SpecimenArray",updateOperation+"testUpdateSpecimenArray");
	    	Logger.out.error(e.getMessage(),e);
	 		e.printStackTrace();
	    }
	}		
	
	
	
	 private static void setLogger(Object object)
	{
			Logger.out = org.apache.log4j.Logger.getLogger(object.getClass());
	}

	private static void writeHeaderContentsToReport()
	{
			Date date = new Date();
			StringBuffer headerContent = new StringBuffer( tabSpacing + "\tNightly Build Report of caTissueCore run on Date " + date.toString() + newLine);
			headerContent.append(tabSpacing + " --------------------------------------------------------------------------------" + newLine);
			reportWriter.writeToFile(headerContent.toString());
	}
	
	/**
	 * 
	 */
	private static void writeFooterContentsToReport()
	{
			//StringBuffer headerContent = new StringBuffer(tabSpacing + "-----------------------------------------------------------------------------------" + newLine); 
			StringBuffer headerContent = new StringBuffer();
			headerContent.append(newLine);
			headerContent.append("   Summary of Nightly Build report " + newLine);
			headerContent.append("-------------------------------------" + newLine);
			headerContent.append("Total Operations Perfomed ==> " + totalOperations + newLine);
			headerContent.append("No. of Successful Operations ==> " + successfullOperations + " 		Ratio(%) ==> "+ calculateRatioInPercentage(successfullOperations) + "%"+newLine );
			headerContent.append("No. of  Failure Operations ==> " + failureOperations + " 		Ratio(%) ==> "+ calculateRatioInPercentage(failureOperations) + "%" +newLine );
			headerContent.append("-------------------------------------" + newLine);
			reportWriter.writeToFile(headerContent.toString());
			reportWriter.writeToFile(newLine);
			reportWriter.writeToFile(reportContents.toString());
	}
	
	/**
	 * @param operation
	 * @return
	 */
	private static double calculateRatioInPercentage(int operation)
	{
		return (((double) operation) / totalOperations) * 100; 
	}
	
	/**
	 * @param object
	 */
	private void writeSuccessfullOperationToReport(Object object,String operation)
	{
		successfullOperations++;
		totalOperations++;
		reportContents.append(object.getClass().getSimpleName() + separator + operation + separator + successMessage + newLine);
		//reportWriter.writeToFile(object.getClass().getSimpleName() + separator + operation + separator + successMessage + newLine);
	}
	
	/**
	 * @param objectName
	 */
	private void writeFailureOperationsToReport(String objectName,String operation)
	{
		failureOperations++;
		totalOperations++;
		reportContents.append(objectName + separator + operation + separator + failureMessage + newLine);
		//reportWriter.writeToFile(objectName + separator + operation + separator + failureMessage + newLine);
	}
	
	private void sendMail()
	{
//		SendBuildReport report = SendBuildReport.getInstance();
//		String to = "catissue@persistent.co.in";
//		String from = "ashwin_gupta@persistent.co.in";
//		String cc = "munesh_gupta@persistent.co.in";
//		String host = "mail.persistent.co.in";
//        String subject = "Nightly Build Report";
//        String body = "nightly build report run for database MySQL";
//        //String filePath = "F:/nightly_build/catissuecore/caTissueCore_caCORE_Client/log/catissuecoreclient.log";
//        report.sendmail(to,cc,null,from,host,subject,body,filePath);
 	}
	
	
	//------ ------------------------------------------------------------
	
	public void testAddDistributionWithClosedSpecimen()
	{
		Distribution distributionObj = null;		
		try
		{    	
	    	Specimen insertspecimen = (Specimen) api.initSpecimen();						
	    	insertspecimen =  (Specimen) appService.createObject(insertspecimen);
	    	insertspecimen.setActivityStatus("Closed");
	    	Specimen updatespecimen =  (Specimen) appService.updateObject(insertspecimen);		
			
			distributionObj = (Distribution)api.initDistribution(updatespecimen);
			
			setLogger(distributionObj);			
	    	Logger.out.info("In test method testAddDistributionWithClosedSpecimen");	
			Logger.out.info("Inserting domain object------->"+distributionObj);
			distributionObj =  (Distribution) appService.createObject(distributionObj);
			writeFailureOperationsToReport("Distribution",insertValidateOperation+"testAddDistributionWithClosedSpecimen");
		}
		catch(Exception e)
		{	
			Logger.out.error(e.getMessage(),e);
			e.printStackTrace();
			if(distributionObj != null)
			{
				writeSuccessfullOperationToReport(distributionObj,insertValidateOperation+"testAddDistributionWithClosedSpecimen");				
			}
			else
			{
				Logger.out.info("Could not able to test testAddDistributionWithClosedSpecimen due to fail to initiaze Distribution obj");
			}
		}
	}

}


