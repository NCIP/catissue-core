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
* Copyright 2001-2004 SAIC. Copyright
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
	* APIClientDemo.java demonstartes various ways to execute searches with and without
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
				writeFooterContentsToReport();
				reportWriter.closeFile();
			
			}
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
			testAddOrderDetails();
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
		}
		catch(Exception e)
		{
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
	private void testAddSpecimenArrayType()
	{
		try
		{
			dataGenerator.initSpecimenArrayType();
			SpecimenArrayType specimenArrayType1 = (SpecimenArrayType) dataGenerator.dataModelObjectMap.get("SPECIMEN_ARRAY_TYPE_1");				
			setLogger(specimenArrayType1);
			Logger.out.info("Inserting domain object------->"+specimenArrayType1);
			specimenArrayType1 =  (SpecimenArrayType) appService.createObject(specimenArrayType1);
			dataGenerator.dataModelObjectMap.put("SPECIMEN_ARRAY_TYPE_1",specimenArrayType1);
			
			SpecimenArrayType specimenArrayType2 = (SpecimenArrayType) dataGenerator.dataModelObjectMap.get("SPECIMEN_ARRAY_TYPE_2");
			setLogger(specimenArrayType2);
			Logger.out.info("Inserting domain object------->"+specimenArrayType2);
			specimenArrayType2 =  (SpecimenArrayType) appService.createObject(specimenArrayType2);
			dataGenerator.dataModelObjectMap.put("SPECIMEN_ARRAY_TYPE_2",specimenArrayType2);
			
			writeSuccessfullOperationToReport(specimenArrayType1,insertOperation+"testAddSpecimenArrayType");
			Logger.out.info(" Domain Object is successfully added ---->    ID:: " + specimenArrayType1.getId().toString());
			Logger.out.info(" Domain Object is successfully added ---->    ID:: " + specimenArrayType2.getId().toString());
		}
		catch(Exception e)
		{
			writeFailureOperationsToReport("SpecimenArrayType",insertOperation+"testAddSpecimenArrayType");
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
		}

}


