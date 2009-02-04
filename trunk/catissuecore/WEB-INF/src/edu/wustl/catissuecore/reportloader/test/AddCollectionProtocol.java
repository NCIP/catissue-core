package edu.wustl.catissuecore.reportloader.test;


import edu.wustl.catissuecore.bizlogic.BizLogicFactory;
import edu.wustl.catissuecore.bizlogic.CollectionProtocolRegistrationBizLogic;
import edu.wustl.catissuecore.bizlogic.ParticipantBizLogic;
import edu.wustl.catissuecore.domain.Address;
import edu.wustl.catissuecore.domain.CancerResearchGroup;
import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.catissuecore.domain.CollectionProtocolEvent;
import edu.wustl.catissuecore.domain.CollectionProtocolRegistration;
import edu.wustl.catissuecore.domain.Department;
import edu.wustl.catissuecore.domain.Institution;
import edu.wustl.catissuecore.domain.Participant;
import edu.wustl.catissuecore.domain.Quantity;
import edu.wustl.catissuecore.domain.SpecimenCharacteristics;
import edu.wustl.catissuecore.domain.SpecimenRequirement;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.reportloader.Parser;
import edu.wustl.catissuecore.util.CatissueCoreCacheManager;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.bizlogic.IBizLogic;
import edu.wustl.common.cde.CDEManager;
import edu.wustl.common.util.Utility;
import edu.wustl.common.util.XMLPropertyHandler;
import edu.wustl.common.util.global.ApplicationProperties;
import edu.wustl.common.util.global.Constants;
import edu.wustl.common.util.global.Variables;
import edu.wustl.common.util.logger.Logger;
import gov.nih.nci.system.applicationservice.ApplicationService;

import java.io.File;
import java.text.ParseException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import net.sf.ehcache.CacheException;

import org.apache.log4j.PropertyConfigurator;

/**
 * @author caBIO Team
 * @version 1.0
 */



/**
	Program to add default Collection Protocol for caTIES
*/

public class AddCollectionProtocol
{
		static ApplicationService appService = null;
		
		public static Map dataModelObjectMap = new HashMap();
		
		private static int totalOperations;
		private static int successfullOperations;
		private static int failureOperations;
		private static String tabSpacing = "\t\t\t";
		private static String newLine = System.getProperty("line.separator");
		private String separator = ",";
		private static StringBuffer reportContents = null;
		private String insertOperation = " insert, Positive testcase Name: ";
		private String insertValidateOperation = " insert, Negative testcase Name: ";
		private String updateOperation = " update, Positive testcase Name: ";
		private String updateValidateOperation = "update, Negative testcase Name: ";
		private String searchOperation = " serach, Positive testcase Name: ";
		private String successMessage = " pass ";
		private String failureMessage = " fail ";
		private static String filePath = "";
	 
    public static void main(String[] args) 
	{
	
		System.out.println("*** Add Collection protocol...");
		try
		{
			try
			{
				//ApplicationServiceProvider applicationServiceProvider = new ApplicationServiceProvider(); 
				Variables.applicationHome = System.getProperty("user.dir");
				Logger.out = org.apache.log4j.Logger.getLogger("");
				Logger.configure(Variables.applicationHome + File.separator+"ApplicationResources.properties");
				PropertyConfigurator.configure(Variables.applicationHome + File.separator+"ApplicationResources.properties");
				System.setProperty("gov.nih.nci.security.configFile",
						"./catissuecore-properties"+File.separator+"ApplicationSecurityConfig.xml");
				CDEManager.init();
				XMLPropertyHandler.init("./catissuecore-properties"+File.separator+"caTissueCore_Properties.xml");
				ApplicationProperties.initBundle("ApplicationResources");

			} 
			catch (Exception ex) 
			{ 
				System.out.println(ex.getMessage()); 
				ex.printStackTrace();
				return;
			}
			CatissueCoreCacheManager catissueCoreCacheManager=null;
			initialize(catissueCoreCacheManager);
			try
			{				
				reportContents = new StringBuffer();
				AddCollectionProtocol testClient = new AddCollectionProtocol();				
				testClient.createObjects();		
			}
			catch(Exception ex)
			{
				ex.printStackTrace();
			}
		
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			System.out.println("Test client throws Exception = "+ ex);
		}
	}
    
	public static void initialize(CatissueCoreCacheManager catissueCoreCacheManager)
	{
		
		Map participantMap = null;
		ParticipantBizLogic bizlogic = (ParticipantBizLogic) BizLogicFactory.getInstance()
				.getBizLogic(Participant.class.getName());
		Map cpMap = null;
		BizLogicFactory bizFactory=BizLogicFactory.getInstance();
		CollectionProtocolRegistrationBizLogic cpbizlogic= (CollectionProtocolRegistrationBizLogic) bizFactory.getBizLogic(CollectionProtocolRegistration.class.getName());
		List cprList=null;
		try
		{
			cprList=cpbizlogic.getAllParticipantRegistrationInfo();
			participantMap = bizlogic.getAllParticipants();
			Iterator it = participantMap.keySet().iterator();
			while (it.hasNext())
			{
				Long str = (Long) it.next();
			}
		}
		catch (Exception ex)
		{
			Logger.out.error("Exception occured getting List of Participants " ,ex);
		}
		// getting instance of catissueCoreCacheManager and adding participantMap to cache
		try
		{
			catissueCoreCacheManager = CatissueCoreCacheManager
			.getInstance();
			catissueCoreCacheManager.addObjectToCache(edu.wustl.catissuecore.util.global.Constants.MAP_OF_PARTICIPANTS, (HashMap) participantMap);
			catissueCoreCacheManager.addObjectToCache(edu.wustl.catissuecore.util.global.Constants.LIST_OF_REGISTRATION_INFO, (Vector)cprList);
			
		}
		catch (CacheException e)
		{
			Logger.out
					.error("Exception occured while creating instance of CatissueCoreCacheManager" , e);
		}	
	}
    //////////////////////// Start Add operation //////
	private void createObjects()
	{
	try
	{
		   // Add sub domain objects	
	 	   Object obj = initSpecimenCharacteristics();
	 	   saveObject(obj);
	 	  SpecimenCharacteristics specimenCharacteristics=(SpecimenCharacteristics)obj;
	 	   dataModelObjectMap.put("SpecimenCharacteristics",specimenCharacteristics);
	 	   
	 	   obj = initSpecimenRequirement();
	 	   saveObject(obj);
	 	  SpecimenRequirement specimenRequirement=(SpecimenRequirement)obj;
	 	   dataModelObjectMap.put("SpecimenRequirement",specimenRequirement);
	
	 	   obj = initCollectionProtocolEvent();
	 	   saveObject(obj);
	 	   CollectionProtocolEvent collectionProtocolEvent=(CollectionProtocolEvent)obj;
	 	   dataModelObjectMap.put("CollectionProtocolEvent",collectionProtocolEvent);
	 	    
	 	   
	 	   testAddInstitution();
	 	   testAddDepartment();
	 	   testAddCancerResearchGroup();
	 	   testAddUser();
	 	   testAddCollectionProtocol();
		

		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			System.out.println("Test client throws Exception = "+ ex);
		}
	}	
	

	
	public SpecimenCharacteristics initSpecimenCharacteristics()
	{
		SpecimenCharacteristics specimenCharacteristics = new SpecimenCharacteristics();
		specimenCharacteristics.setTissueSide("Left");
		specimenCharacteristics.setTissueSite("Placenta");
		specimenCharacteristics.setId(new Long(1));
		
		return specimenCharacteristics;
	}
	public SpecimenRequirement initSpecimenRequirement()
	{
		SpecimenRequirement specimenRequirement = new SpecimenRequirement();
		specimenRequirement.setSpecimenClass("Molecular");
		specimenRequirement.setSpecimenType("DNA");
		specimenRequirement.setTissueSite("Placenta");
		specimenRequirement.setPathologyStatus("Malignant");
		Quantity quantity = new Quantity();
		quantity.setValue(new Double(10));
		specimenRequirement.setQuantity(quantity);
		return specimenRequirement;
	}
	public CollectionProtocolEvent initCollectionProtocolEvent()
	{
		CollectionProtocolEvent collectionProtocolEvent = new CollectionProtocolEvent();
		collectionProtocolEvent.setId(new Long(1));
		return collectionProtocolEvent;
	}
	public Department initDepartment()
	{
		Department dept = new Department();
		dept.setName("dt" + UniqueKeyGeneratorUtil.getUniqueKey());
		return dept;
	}

	private void testAddCollectionProtocol()
	{
		try
		{
			CollectionProtocol collectionProtocolObj = (CollectionProtocol)initCollectionProtocol();
			setLogger(collectionProtocolObj);
			Logger.out.info("Inserting domain object------->"+collectionProtocolObj);
			saveObject(collectionProtocolObj);
			dataModelObjectMap.put("CollectionProtocol",collectionProtocolObj);
			
			Logger.out.info(" Domain Object is successfully added ---->  ID:: " + collectionProtocolObj.getId().toString());
		}
		catch(Exception e)
		{
			
			Logger.out.error(e.getMessage(),e);
			e.printStackTrace();
		}
	}
	
	private void testAddUser()
	{
		try
		{
			User userObj = (User) initAdminUser();
	    	setLogger(userObj);
	    	saveObject(userObj);
			dataModelObjectMap.put("User",userObj);
			
			Logger.out.info(" Domain Object is successfully added ---->  LoginName:: " + userObj.getId().longValue() + " ::  Name :: " + userObj.getFirstName());
			
			User userObj1 = (User)initAdminUser();
			setLogger(userObj1);
			saveObject(userObj1);
			dataModelObjectMap.put("User1",userObj1);
			Logger.out.info(" Domain Object is successfully added ---->  LoginName:: " + userObj1.getId().longValue() + " ::  Name :: " + userObj1.getFirstName());
		
		}
		catch(Exception e)
		{
			
			Logger.out.error(e.getMessage(),e);
			e.printStackTrace();
		}
	}
	
	public CollectionProtocol initCollectionProtocol()
	{
		CollectionProtocol collectionProtocol = new CollectionProtocol();

				
		collectionProtocol.setAliqoutInSameContainer(new Boolean(false));
		collectionProtocol.setDescriptionURL("");
		collectionProtocol.setActivityStatus("Active");
		collectionProtocol.setEndDate(null);
		collectionProtocol.setEnrollment(null);
		collectionProtocol.setIrbIdentifier("1111");
		collectionProtocol.setTitle("Generic Collection Protocol");
		collectionProtocol.setShortTitle("gcp");
				
		try
		{
			collectionProtocol.setStartDate(Utility.parseDate("08/15/1975", Utility
					.datePattern("08/15/1975")));
		}
		catch (ParseException e)
		{
			e.printStackTrace();
		}

		Collection collectionProtocolEventCollectionSet = new HashSet();
	 
//		CollectionProtocolEvent collectionProtocolEvent = new CollectionProtocolEvent();
		CollectionProtocolEvent collectionProtocolEvent = (CollectionProtocolEvent)AddCollectionProtocol.dataModelObjectMap.get("CollectionProtocolEvent");

		collectionProtocolEvent.setClinicalStatus("New Diagnosis");
		collectionProtocolEvent.setStudyCalendarEventPoint(new Double(0));
	 

		Collection specimenRequirementCollection = new HashSet();
//		SpecimenRequirement specimenRequirement = new SpecimenRequirement();
//		specimenRequirement.setSpecimenClass("Molecular");
//		specimenRequirement.setSpecimenType("DNA");
//		specimenRequirement.setTissueSite("Placenta");
//		specimenRequirement.setPathologyStatus("Malignant");
//		Quantity quantity = new Quantity();
//		quantity.setValue(new Double(10));
//		specimenRequirement.setQuantity(quantity);
	
		
		SpecimenRequirement specimenRequirement  =(SpecimenRequirement)AddCollectionProtocol.dataModelObjectMap.get("SpecimenRequirement");
		specimenRequirementCollection.add(specimenRequirement);
		
		collectionProtocolEvent.setSpecimenRequirementCollection(specimenRequirementCollection);

		collectionProtocolEventCollectionSet.add(collectionProtocolEvent);
		collectionProtocol.setCollectionProtocolEventCollection(collectionProtocolEventCollectionSet);

//		User principalInvestigator = new User();
//		principalInvestigator.setId(new Long(1));
		User principalInvestigator = (User)AddCollectionProtocol.dataModelObjectMap.get("User");
		collectionProtocol.setPrincipalInvestigator(principalInvestigator);
		
//		User protocolCordinator = new User();	
//		protocolCordinator.setId(new Long(2));
//		protocolCordinator.setId(new Long(principalInvestigator.getId().longValue()-1));
		User protocolCordinator = (User)AddCollectionProtocol.dataModelObjectMap.get("User1");
		Collection protocolCordinatorCollection = new HashSet();
		protocolCordinatorCollection.add(protocolCordinator);
		collectionProtocol.setCoordinatorCollection(protocolCordinatorCollection);
		
		return collectionProtocol;
	}
	
	private void testAddDepartment()
	{
		try
		{
			Department departmentObj = (Department)initDepartment();
	    	setLogger(departmentObj);
	    	saveObject(departmentObj);
	    	dataModelObjectMap.put("Department",departmentObj);
	    	
			Logger.out.info(" Domain Object is successfully added ----> Name:: " + departmentObj.getName());
		//+ departmentObj.getId().longValue() + " ::  Name :: " + departmentObj.getName());
		}
		catch(Exception e)
		{
			
			Logger.out.error(e.getMessage(),e);
			e.printStackTrace();
		}
	}
	
	private void testAddCancerResearchGroup()
	{
		try
		{
			CancerResearchGroup cancerResearchGroupObj = (CancerResearchGroup)initCancerResearchGroup();
	    	setLogger(cancerResearchGroupObj);
	    	Logger.out.info("Inserting domain object------->"+cancerResearchGroupObj);
	    	saveObject(cancerResearchGroupObj);
	    	dataModelObjectMap.put("CancerResearchGroup",cancerResearchGroupObj);
	    	
			Logger.out.info(" Domain Object is successfully added ---->  Name:: " + cancerResearchGroupObj.getName());
		//+ cancerResearchGroupObj.getId().longValue() + " ::  Name :: " + cancerResearchGroupObj.getName());
		}
		catch(Exception e)
		{
			
			
			Logger.out.error(e.getMessage(),e);
			e.printStackTrace();
		}
	}
	public CancerResearchGroup initCancerResearchGroup()
	{
		CancerResearchGroup cancerResearchGroup = new CancerResearchGroup();
		cancerResearchGroup.setName("crg" + UniqueKeyGeneratorUtil.getUniqueKey());
		return cancerResearchGroup;
	}
	private void testAddInstitution()
	{
		try
		{
			Institution institutionObj = (Institution)initInstitution();			
	    	setLogger(institutionObj);
	    	Logger.out.info("Inserting domain object------->"+institutionObj);
	    	saveObject(institutionObj);
	    	dataModelObjectMap.put("Institution",institutionObj);
			Logger.out.info(" Domain Object is successfully added ---->  Name:: " + institutionObj.getName());
			
			//+ institutionObj.getId().longValue() + " ::  Name :: " + institutionObj.getName());
		}
		catch(Exception e)
		{
			
			Logger.out.error(e.getMessage(),e);
			e.printStackTrace();
		}
	}
	
	public User initAdminUser()
	{
		User userObj = new User();
		userObj.setEmailAddress(UniqueKeyGeneratorUtil.getUniqueKey()+ "@admin.com");
		userObj.setLoginName(userObj.getEmailAddress());
		userObj.setLastName("last" + UniqueKeyGeneratorUtil.getUniqueKey());
		userObj.setFirstName("name" + UniqueKeyGeneratorUtil.getUniqueKey());

		Address address = new Address();
		address.setStreet("street");
		address.setCity("city");
		address.setState("D.C.");
		address.setZipCode("12345");
		address.setCountry("United States");
		address.setPhoneNumber("12345");
		address.setFaxNumber("12345");		
 
		userObj.setAddress(address);

//		Institution institution = new Institution();
//		institution.setId(new Long(1));
		Institution institution = (Institution) AddCollectionProtocol.dataModelObjectMap.get("Institution");
		userObj.setInstitution(institution);

//		Department department = new Department();
//		department.setId(new Long(1));
		Department department = (Department)AddCollectionProtocol.dataModelObjectMap.get("Department");
		userObj.setDepartment(department);

//		CancerResearchGroup cancerResearchGroup = new CancerResearchGroup();
//		cancerResearchGroup.setId(new Long(1));
		CancerResearchGroup cancerResearchGroup = (CancerResearchGroup)AddCollectionProtocol.dataModelObjectMap.get("CancerResearchGroup");
		userObj.setCancerResearchGroup(cancerResearchGroup);

		userObj.setRoleId("1");
		userObj.setActivityStatus("Active");
		//userObj.setComments("");
		userObj.setPageOf(Constants.PAGEOF_USER_ADMIN);		
		//userObj.setCsmUserId(new Long(1));
		//userObj.setFirstTimeLogin(Boolean.valueOf(false));
		return userObj;
	}
	public User initUser()
	{
		User userObj = new User();
		userObj.setEmailAddress(UniqueKeyGeneratorUtil.getUniqueKey()+ "@admin.com");
		userObj.setLoginName(userObj.getEmailAddress());
		userObj.setLastName("last" + UniqueKeyGeneratorUtil.getUniqueKey());
		userObj.setFirstName("name" + UniqueKeyGeneratorUtil.getUniqueKey());

		Address address = new Address();
		address.setStreet("Street");
		address.setCity("City");
		address.setState("D.C.");
		address.setZipCode("12345");
		address.setCountry("United States");
		address.setPhoneNumber("12345");
		address.setFaxNumber("12345");		
 
		userObj.setAddress(address);

//		Institution institution = new Institution();
//		institution.setId(new Long(1));
		Institution institution = (Institution) AddCollectionProtocol.dataModelObjectMap.get("Institution");
		userObj.setInstitution(institution);

//		Department department = new Department();
//		department.setId(new Long(1));
		Department department = (Department)AddCollectionProtocol.dataModelObjectMap.get("Department");
		userObj.setDepartment(department);

//		CancerResearchGroup cancerResearchGroup = new CancerResearchGroup();
//		cancerResearchGroup.setId(new Long(1));
		CancerResearchGroup cancerResearchGroup = (CancerResearchGroup)AddCollectionProtocol.dataModelObjectMap.get("CancerResearchGroup");
		userObj.setCancerResearchGroup(cancerResearchGroup);

		//userObj.setRoleId("1");
		//userObj.setComments("");
		userObj.setPageOf(Constants.PAGEOF_SIGNUP);
		//userObj.setActivityStatus("Active");
		//userObj.setCsmUserId(new Long(1));
		//userObj.setFirstTimeLogin(Boolean.valueOf(false));
		return userObj;
	}
	
	public Institution initInstitution()
	{
		Institution institutionObj = new Institution();
		institutionObj.setName("inst" + UniqueKeyGeneratorUtil.getUniqueKey());
		return institutionObj;
	}
	
	 private static void setLogger(Object object)
	{
			Logger.out = org.apache.log4j.Logger.getLogger(object.getClass());
	}

	 /**
		 * Saves object tothe datastore
		 * @param obj object
		 * @throws Exception throws exception  
		 */
		public static void saveObject(Object obj)throws Exception
		{
			BizLogicFactory bizLogicFactory = BizLogicFactory.getInstance();
			IBizLogic bizLogic = bizLogicFactory.getBizLogic(obj.getClass().getName());
			SessionDataBean sessionDataBean = new SessionDataBean();
			sessionDataBean.setUserName(XMLPropertyHandler.getValue(Parser.SESSION_DATA));
			bizLogic.insert(obj,sessionDataBean,Constants.HIBERNATE_DAO);
		}
	
}


