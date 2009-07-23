/**
 * <p>Title: CaCoreAppServicesDelegator Class>
 * <p>Description:	This class contains the basic methods that are required
 * for HTTP APIs. It just passes on the request at proper place.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Aniruddha Phadnis
 * @version 1.00
 * Created on Jan 10, 2006
 */

package edu.wustl.catissuecore.client;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import oracle.sql.CLOB;
import edu.wustl.catissuecore.bizlogic.ParticipantBizLogic;
import edu.wustl.catissuecore.domain.CollectionProtocolRegistration;
import edu.wustl.catissuecore.domain.Participant;
import edu.wustl.catissuecore.domain.ParticipantMedicalIdentifier;
import edu.wustl.catissuecore.domain.ReviewEventParameters;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.SpecimenArrayContent;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.catissuecore.domain.SpecimenEventParameters;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.domain.pathology.DeidentifiedSurgicalPathologyReport;
import edu.wustl.catissuecore.domain.pathology.IdentifiedSurgicalPathologyReport;
import edu.wustl.catissuecore.domain.pathology.ReportLoaderQueue;
import edu.wustl.catissuecore.namegenerator.LabelGenerator;
import edu.wustl.catissuecore.namegenerator.LabelGeneratorFactory;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.global.DefaultValueManager;
import edu.wustl.catissuecore.util.global.Variables;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.bizlogic.IBizLogic;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.factory.AbstractFactoryConfig;
import edu.wustl.common.factory.IFactory;
import edu.wustl.common.lookup.LookupLogic;
import edu.wustl.common.util.Utility;
import edu.wustl.common.util.XMLPropertyHandler;
import edu.wustl.common.util.global.CommonServiceLocator;
import edu.wustl.common.util.global.PasswordManager;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.dao.DAO;
import edu.wustl.dao.JDBCDAO;
import edu.wustl.dao.daofactory.DAOConfigFactory;
import edu.wustl.dao.exception.DAOException;
import edu.wustl.query.security.QueryCsmCache;
import edu.wustl.query.security.QueryCsmCacheManager;
import edu.wustl.security.exception.SMException;
import edu.wustl.security.manager.ISecurityManager;
import edu.wustl.security.manager.SecurityManagerFactory;
import gov.nih.nci.security.authorization.domainobjects.Role;

/**
 * This class contains the basic methods that are required for HTTP APIs. 
 * It just passes on the request at proper place.
 * @author aniruddha_phadnis
 */
public class CaCoreAppServicesDelegator
{

	private transient Logger logger = Logger.getCommonLogger(CaCoreAppServicesDelegator.class);
	/**
	 * Passes User credentials to CaTissueHTTPClient to connect User with caTISSUE Core Application
	 * @param userName userName of the User to connect to caTISSUE Core Application
	 * @param password password of the User to connect to caTISSUE Core Application
	 * @return the sessionID of user if he/she has successfullyy logged in else null
	 * @throws Exception 
	 */
	//int noPHIAccessDataCounter =0;
	public Boolean delegateLogin(String userName, String password) throws Exception
	{
		User validUser = AppUtility.getUser(userName);
		Boolean authenticated = Boolean.valueOf(false);
		if (validUser != null)
		{
			password = PasswordManager.encode(password);
			boolean loginOK = SecurityManagerFactory.getSecurityManager().login(userName, password);
			authenticated = new Boolean(loginOK);
		}
		return authenticated;
	}

	/**
	 * Disconnects User from caTISSUE Core Application
	 * @param sessionKey
	 * @return returns the status of logout to caTISSUE Core Application
	 */
	public boolean delegateLogout(String sessionKey)// throws Exception
	{
		return false;
	}

	/**
	 * Passes caCore Like domain object to  caTissue Core biz logic to perform Add operation.
	 * @param domainObject the caCore Like object to add using HTTP API
	 * @param userName user name
	 * @return returns the Added caCore Like object/Exception object if exception occurs performing Add operation
	 * @throws Exception
	 */
	public Object delegateAdd(String userName, Object domainObject) throws Exception
	{
		try
		{
			/*
			if (domainObject == null) 
			{
				throw new Exception("Please enter valid domain object!! Domain object should not be NULL");
			}
			*/
			checkNullObject(domainObject, "Domain Object");
			IBizLogic bizLogic = getBizLogic(domainObject.getClass().getName());
			bizLogic.insert(domainObject, getSessionDataBean(userName), 0);
			logger.info(" Domain Object has been successfully inserted " + domainObject);
		}
		catch (Exception e)
		{
			logger.error("Delegate Add-->" + e.getMessage());
			throw e;
		}
		return domainObject;
	}

	/**
	 * Passes caCore Like domain object to caTissue Core biz logic to perform Edit operation.
	 * @param domainObject the caCore Like object to edit using HTTP API
	 * @param userName  user name
	 * @return returns the Edited caCore Like object/Exception object if exception occurs performing Edit operation
	 * @throws Exception
	 */
	public Object delegateEdit(String userName, Object domainObject) throws Exception
	{
		DAO dao = null;
		try
		{
			/*
			if (domainObject == null) 
			{
				throw new Exception("Please enter valid domain object!! Domain object should not be NULL");
			}
			*/
			checkNullObject(domainObject, "Domain Object");
			String objectName = domainObject.getClass().getName();
			IBizLogic bizLogic = getBizLogic(objectName);
			AbstractDomainObject abstractDomainObject = (AbstractDomainObject) domainObject;
			// not null check for Id
			checkNullObject(abstractDomainObject.getId(), "Identifier");
			Object object = bizLogic.retrieve(objectName, abstractDomainObject.getId());

			if (object == null)
			{
				throw new Exception(
						"No such domain object found for update !! Please enter valid domain object for edit");
			}
			AbstractDomainObject abstractDomainOld = (AbstractDomainObject) object;
			
			dao = AppUtility.openDAOSession(null);
				
			abstractDomainOld = (AbstractDomainObject) dao.retrieveById(Class.forName(objectName).getName(),
					new Long(abstractDomainObject.getId()));
			bizLogic.update(abstractDomainObject, abstractDomainOld, 0,
					getSessionDataBean(userName));
			
			logger.info(" Domain Object has been successfully updated " + domainObject);
		}
		catch (Exception e)
		{
			logger.error("Delegate Edit" + e.getMessage());
			throw e;
		}
		finally
		{
			AppUtility.closeDAOSession(dao);
		}
		return domainObject;
	}

	/**
	 * Method is modified to allow to delete object of ReportLoaderQueue
	 * Returns Exception object as Delete operation is not supported by CaTissue Core Application.
	 * @param domainObject the caCore Like object to delete using HTTP API
	 * @param userName user name
	 * @return returns Exception object as Delete operation is not supported by CaTissue Core Application.
	 * @throws Exception
	 */
	public Object delegateDelete(String userName, Object domainObject) throws Exception
	{
		if (domainObject instanceof ReportLoaderQueue)
		{
			IFactory factory = AbstractFactoryConfig.getInstance().getBizLogicFactory();
			IBizLogic bizLogic = factory.getBizLogic(domainObject.getClass().getName());
			bizLogic.delete(domainObject, 0);
			return null;
		}
		else
		{
			throw new Exception("caTissue does not support delete");
		}
	}

	/**
	 * @param userName user name
	 * @param domainObject domain object
	 * @return list of objects
	 * @throws Exception
	 */
	public List delegateGetObjects(String userName, Object domainObject) throws Exception
	{
		List searchObjects = new ArrayList();
		checkNullObject(domainObject, "Domain Object");
		String objectName = domainObject.getClass().getName();
		IBizLogic bizLogic = getBizLogic(objectName);
		AbstractDomainObject abstractDomainObject = (AbstractDomainObject) domainObject;
		// not null check for Id
		checkNullObject(abstractDomainObject.getId(), "Identifier");
		searchObjects = bizLogic.retrieve(objectName, Constants.SYSTEM_IDENTIFIER,
				abstractDomainObject.getId());

		if (searchObjects.isEmpty())
		{
			throw new Exception("Please enter valid domain object for search operation!!");
		}
		List nonDisbaledObjectList = filterDisabledObjects(searchObjects);
		return nonDisbaledObjectList;
	}

	public List delegateSearchFilter(String userName, List list) throws Exception
	{
		List nonDisbaledObjectList = filterDisabledObjects(list);
		logger.debug("User Name : " + userName);
		logger.debug("list obtained from ApplicationService Search************** : "
				+ nonDisbaledObjectList.getClass().getName());
		logger.debug("Super Class ApplicationService Search************** : "
				+ nonDisbaledObjectList.getClass().getSuperclass().getName());
		List filteredObjects = null;//new ArrayList();
		User validUser = AppUtility.getUser(userName);
		String reviewerRole = null;
		
		List<Class> classList = new ArrayList<Class>();
	    classList.add(Participant.class);
	    classList.add(SpecimenCollectionGroup.class);
	    classList.add(Specimen.class);
	    classList.add(CollectionProtocolRegistration.class);
	    classList.add(IdentifiedSurgicalPathologyReport.class);
	    classList.add(DeidentifiedSurgicalPathologyReport.class);
	    classList.add(ParticipantMedicalIdentifier.class);
	    classList.add(SpecimenEventParameters.class);
	    classList.add(SpecimenArrayContent.class);
	    
		ISecurityManager securityManager = SecurityManagerFactory.getSecurityManager();
		try
		{ 
			Role role = securityManager.getUserRole(validUser.getCsmUserId());
			if (validUser.getRoleId().equalsIgnoreCase(Constants.ADMIN_USER))
			{
				reviewerRole = Constants.ADMINISTRATOR;
			}
			// reviewerRole=role.getName();
		}
		catch (SMException ex)
		{
			logger.info("Review Role not found!");
		}
		if (reviewerRole != null && (reviewerRole.equalsIgnoreCase(Constants.ADMINISTRATOR)))
		{
			filteredObjects = nonDisbaledObjectList;
		}
		else
		{
			JDBCDAO jdbcDAO = null;
			try
			{
				jdbcDAO = AppUtility.openJDBCSession();
				filteredObjects = filterObjects(userName, nonDisbaledObjectList,classList,jdbcDAO);
			}
			catch (Exception exp)
			{
				logger.debug(exp.getMessage(), exp);
				exp.printStackTrace();
				throw exp;
			}
			finally
			{
				AppUtility.closeJDBCSession(jdbcDAO);
			}
		}

		return filteredObjects;
	}

	/**
	 * Method to filter out AbstractDomain objects of which activityStatus is disabled
	 * @param list
	 * @return
	 */
	private List filterDisabledObjects(List list)
	{
		long st = System.currentTimeMillis();
		List result = new ArrayList();
		for (int i = 0; i < list.size(); i++)
		{
			Object object = list.get(i);
			/**
			 * Chech if object is of type AbstractDomainObject if not add it to final list
			 * else check call getActivityStatus of object and check if value is Disabled. 
			 */
			if (object instanceof AbstractDomainObject)
			{
				AbstractDomainObject abstractDomainObject = (AbstractDomainObject) object;
				try
				{
					Method activityStatusMethod = abstractDomainObject.getClass().getMethod(
							"getActivityStatus", null);
					String activityStatus = (String) activityStatusMethod.invoke(
							abstractDomainObject, null);
					if (!Constants.DISABLED.equalsIgnoreCase(activityStatus))
					{
						result.add(abstractDomainObject);
					}
				}
				catch (Exception ex)
				{
					// Exception occured if Domain Oject is not having ActivityStatus field
					logger.error("in CaCoreAppServicesDelegator" + ex.getMessage());
					result.add(abstractDomainObject);
				}
			}
			else
			{
				// Add object to list as it is nit caTissue domain objectFor 
				//e.g Admin user queries on get id from domain object 
				result.add(object);
			}
		}
		long et = System.currentTimeMillis();
		logger.info("Time to filer disable objects:" + (et - st) / 1000);
		return result;
	}

	/**
	 * Filters the list of objects returned by the search depending on the privilege of the user on the objects.
	 * Also sets the identified data to null if the user doesn'r has privilege to see the identified data. 
	 * @param userName The name of the user whose privilege are to be checked.
	 * @param objectList The list of the objects which are to be filtered.
	 * @return The filtered list of objects according to the privilege of the user.
	 * @throws Exception 
	 */
	private List filterObjects(String userName, List objectList, List classList,JDBCDAO jdbcDao) throws Exception
	{
	    Logger.out.debug("In Filter Objects ......" );
	    
	    SessionDataBean sessionDataBean = getSessionDataBean(userName);
	    // boolean that indicates whether user has READ_DENIED privilege on the main object.
		boolean isReadDenied = false;

		// boolean that indicates whether user has privilege on identified data.
		boolean hasPHIAccess = false;
		List filteredObjects = new ArrayList();
		QueryCsmCacheManager cacheManager = new QueryCsmCacheManager(jdbcDao);
		QueryCsmCache cache = cacheManager.getNewCsmCacheObject();
		Map<String,Boolean> accessPrivilegeMap = new HashMap<String, Boolean>();
		boolean isCheckPermissionCalled = false;
		Logger.out.debug("Total Objects>>>>>>>>>>>>>>>>>>>>>"+objectList.size());
		Iterator iterator = objectList.iterator();
		while(iterator.hasNext())
		{
		    Object abstractDomainObject = (Object) iterator.next();
		    Class classObject = abstractDomainObject.getClass();
		    String objectName = classObject.getName();
		    Long identifier = (Long)getFieldObject(abstractDomainObject, "id");
		    
		    if(classObject.getSuperclass().equals(SpecimenEventParameters.class)||
		       classObject.getSuperclass().equals(ReviewEventParameters.class))
			{
		    	classObject = SpecimenEventParameters.class;
			}
		    else if(classObject.getSuperclass().equals(Specimen.class))
			{
		    	classObject = Specimen.class;
			}

            /** Check the permission of the user on the main object.
             *  Call to SecurityManager.checkPermission bypassed &
             *  instead, call redirected to privilegeCache.hasPrivilege
             *  Check readDenied permission on particpant,SCG,CPR,Specimen,IdentifiedSPR 
             *  If the user has READ_DENIED privilege on the object, remove that object from the list. 
             */
            if(classList.contains(classObject))
            {
    			if(classObject.equals(Specimen.class))
    			{
    				objectName = Specimen.class.getName();
    			}
    			else if(classObject.equals(SpecimenEventParameters.class))
    			{
    				SpecimenEventParameters spe = (SpecimenEventParameters)abstractDomainObject;
    				if(spe.getSpecimenCollectionGroup()!=null)
    				{
    					objectName = SpecimenCollectionGroup.class.getName();
    					identifier = spe.getSpecimenCollectionGroup().getId();
    				}
    				else
    				{
    					objectName = Specimen.class.getName();
    					identifier = spe.getSpecimen().getId();
    				}
    			}
    			else if(classObject.equals(ParticipantMedicalIdentifier.class))
    			{
    				objectName = Participant.class.getName();
    				ParticipantMedicalIdentifier pmi = (ParticipantMedicalIdentifier)abstractDomainObject;
    				identifier = pmi.getParticipant().getId();
    			}
    			else if (classObject.equals(SpecimenArrayContent.class))
    			{
    				objectName = Specimen.class.getName();
    				SpecimenArrayContent sac =(SpecimenArrayContent)abstractDomainObject;
    				identifier = sac.getSpecimen().getId();
    			}
    			accessPrivilegeMap = cacheManager.getAccessPrivilegeMap(objectName,identifier, sessionDataBean, cache);
    			isReadDenied = accessPrivilegeMap.get(edu.wustl.query.util.global.AQConstants.IS_READ_DENIED);
    			isCheckPermissionCalled = true;
    		}
		    
		    /**
		     * for the objects not in classList we need to call cachemanger.
		     */
            if(!isCheckPermissionCalled)
            {
            	accessPrivilegeMap = cacheManager.getAccessPrivilegeMap(objectName,identifier, sessionDataBean, cache);
            	isReadDenied = accessPrivilegeMap.get(edu.wustl.query.util.global.AQConstants.IS_READ_DENIED);
            }
            /**
		     *  In case of no READ_DENIED privilege, check for privilege on identified data.
		     */
			if (!isReadDenied)
		    {
		        /**Check the permission of the user on the identified data of the object.
		         * Call to SecurityManager.checkPermission bypassed &
		         * instead, call redirected to privilegeCache.hasPrivilege
		         * call remove identified data
		         * If has no read privilege on identified data, set the identified attributes as NULL.
		    	**/
				hasPHIAccess = accessPrivilegeMap.get(edu.wustl.query.util.global.AQConstants.HAS_PHI_ACCESS);
				if(!hasPHIAccess)
				{
					removeIdentifiedDataFromObject(abstractDomainObject,objectName,identifier,sessionDataBean,classList);
				}
		    	filteredObjects.add(abstractDomainObject);
			    Logger.out.debug("Intermediate Size of filteredObjects .............."+filteredObjects.size());
			}
	    	
		}
		Logger.out.debug("Before Final Objects >>>>>>>>>>>>>>>>>>>>>>>>>"+filteredObjects.size());
		
		return filteredObjects;
	}

	private void removeIdentifiedDataFromObject(Object abstractDomainObject, String objectName,
			Long identifier, SessionDataBean sessionDataBean,List classList) throws BizLogicException
			{
		Class classObject = abstractDomainObject.getClass();
		if(classObject.getSuperclass().equals(SpecimenEventParameters.class)||
				classObject.getSuperclass().equals(ReviewEventParameters.class))
		{
			classObject = SpecimenEventParameters.class;
		}
		else if(classObject.getSuperclass().equals(Specimen.class))
		{
			classObject = Specimen.class;
		}
		if (classObject.equals(Participant.class))
		{
			removeParticipantIdentifiedData(abstractDomainObject);
		}
		else if(classObject.equals(ParticipantMedicalIdentifier.class))
		{
			removePMIIdentifiedData(abstractDomainObject);
		}
		else if (classObject.equals(SpecimenCollectionGroup.class))
		{
			removeSpecimenCollectionGroupIdentifiedData(abstractDomainObject);
		}
		else if (classObject.equals(Specimen.class))
		{
			removeSpecimenIdentifiedData(abstractDomainObject);
		}
		else if (classObject.equals(CollectionProtocolRegistration.class))
		{
			removeCollectionProtocolRegistrationIdentifiedData(abstractDomainObject);
		}
		else if (classObject.equals(DeidentifiedSurgicalPathologyReport.class))
		{
			removeDeIdentifiedReportIdentifiedData(abstractDomainObject);
		}
		else if (classObject.equals(IdentifiedSurgicalPathologyReport.class))
		{
			removeIdentifiedReportIdentifiedData(abstractDomainObject);
		}
		else if(classObject.equals(SpecimenEventParameters.class))
		{
			removeSpecimenEventParameters(abstractDomainObject);
		}
		else if(classObject.equals(SpecimenArrayContent.class))
		{
			removeSpecimenArrayContentIdentifiedData(abstractDomainObject);
		}
			}

	/**
	 * Returns the alias name of the domain object passed.   
	 * @param object The domain object whose alias name is to be found. 
	 * @return the alias name of the domain object passed.
	 * @throws ClassNotFoundException
	 * @throws BizLogicException 
	 * @throws DAOException 
	 * @throws DAOException
	 */
	/*private String getAliasName(Object object) throws ClassNotFoundException, BizLogicException,
			DAOException
	{
		Class className = object.getClass();
		String domainObjectClassName = edu.wustl.common.util.Utility.parseClassName(className
				.getName());
		String domainClassName = domainObjectClassName;
		//String domainClassName = domainObjectClassName.substring(0, (domainObjectClassName.length()-4));
		logger.debug("Class Name >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>" + domainClassName);
		try
		{
			className = Class.forName("edu.wustl.catissuecore.domain." + domainClassName);
		}
		catch (ClassNotFoundException ex)
		{
			logger
					.error("ClassNotFoundException in CaCoreAppServicesDelegator.getAliasName() method");
			className = Class.forName(object.getClass().getName());
		}
		String tableName = HibernateMetaData.getTableName(className);

		IFactory factory = AbstractFactoryConfig.getInstance().getBizLogicFactory();
		QueryBizLogic bizLogic = (QueryBizLogic) factory
				.getBizLogic(edu.wustl.common.util.global.Constants.QUERY_INTERFACE_ID);
		String aliasName = bizLogic.getAliasName(Constants.TABLE_NAME_COLUMN, tableName);
		return aliasName;
	}
*/
	/**
     * Removes the identified data from Participant object.
     * @param object The Particpant object.
     */
    private void removeParticipantIdentifiedData(Object object)
	{
	    Participant participant = (Participant) object;
	    participant.setFirstName(null);
	    participant.setLastName(null);
	    participant.setMiddleName(null);
	    participant.setBirthDate(null);
	    participant.setDeathDate(null);
	    participant.setSocialSecurityNumber(null);
	    
	    Collection<ParticipantMedicalIdentifier> pmiCollection 
	    				= participant.getParticipantMedicalIdentifierCollection();
	    for (Iterator<ParticipantMedicalIdentifier> iterator = pmiCollection.iterator();iterator.hasNext();)
	    {
	        ParticipantMedicalIdentifier participantMedId = (ParticipantMedicalIdentifier) iterator.next();
	        removePMIIdentifiedData(participantMedId);
	    }
	    
	    Collection<CollectionProtocolRegistration> cpCollection 
	    				= participant.getCollectionProtocolRegistrationCollection();
	    for (Iterator<CollectionProtocolRegistration> iterator=cpCollection.iterator();iterator.hasNext();)
	    {
	        CollectionProtocolRegistration collectionProtReg = (CollectionProtocolRegistration) iterator.next();
	        removeCollectionProtocolRegistrationIdentifiedData(collectionProtReg);
	    }
	}
    /**
     * Removes the identified data from SpecimenCollectionGroup object.
     * @param object The SpecimenCollectionGroup object.
     * @throws DAOException 
     */
	private void removeSpecimenCollectionGroupIdentifiedData(Object object)
	{
	    SpecimenCollectionGroup specimenCollGrp = (SpecimenCollectionGroup) object;
	    specimenCollGrp.setSurgicalPathologyNumber(null);
		removeCollectionProtocolRegistrationIdentifiedData(specimenCollGrp.getCollectionProtocolRegistration());
		Collection<SpecimenEventParameters> spEvent = specimenCollGrp.getSpecimenEventParametersCollection();
	    Iterator<SpecimenEventParameters> eveItr = spEvent.iterator();
	    while(eveItr.hasNext())
	    {
	    	SpecimenEventParameters spEventParam = (SpecimenEventParameters)eveItr.next();
	    	removeSpecimenEventParameters(spEventParam);
	    }
	}
	/**
     * Removes the identified data from Specimen object.
     * @param object The Specimen object.
	 * @throws DAOException 
     */
	private void removeSpecimenIdentifiedData(Object object)
	{
	    Specimen specimen = (Specimen) object;
	    specimen.setCreatedOn(null);    
	    removeSpecimenCollectionGroupIdentifiedData(specimen.getSpecimenCollectionGroup());
	    Collection<SpecimenEventParameters> spEvent = specimen.getSpecimenEventCollection();
	    Iterator<SpecimenEventParameters> eveItr = spEvent.iterator();
	    while(eveItr.hasNext())
	    {
	    	SpecimenEventParameters spEventParam = (SpecimenEventParameters)eveItr.next();
	    	removeSpecimenEventParameters(spEventParam);
	    }
	}

	/**
     * Removes the identified data from CollectionProtocolRegistration object.
     * @param object The CollectionProtocolRegistration object.
	 * @throws DAOException 
     */
	private void removeCollectionProtocolRegistrationIdentifiedData(Object object)
	{
		CollectionProtocolRegistration cpr = (CollectionProtocolRegistration) object;
		cpr.setRegistrationDate(null);
	    cpr.setConsentSignatureDate(null);
	    cpr.setSignedConsentDocumentURL(null);
	   	cpr.setParticipant(null);
	}

	/**
	 * Returns the field object from the class object and field name passed.
	 */
	private Object getFieldObject(Object object, String fieldName)
	{
		Object childObject = null;
		fieldName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
		logger.debug("Method Name***********************" + fieldName);

		try
		{
			childObject = (Object) object.getClass().getMethod(fieldName, null)
					.invoke(object, null);
		}
		catch (NoSuchMethodException noMetExp)
		{
			logger.debug(noMetExp.getMessage(), noMetExp);
		}
		catch (IllegalAccessException illAccExp)
		{
			logger.debug(illAccExp.getMessage(), illAccExp);
		}
		catch (InvocationTargetException invoTarExp)
		{
			logger.debug(invoTarExp.getMessage(), invoTarExp);
		}

		return childObject;
	}

	/**
	 * Get Biz Logic based on domai object class name.
	 * @param domainObjectName name of somain object
	 * @return biz logic
	 * @throws BizLogicException 
	 */
	private IBizLogic getBizLogic(String domainObjectName) throws BizLogicException
	{
		IFactory factory = AbstractFactoryConfig.getInstance().getBizLogicFactory();
		IBizLogic bizLogic = factory.getBizLogic(domainObjectName);
		return bizLogic;
	}

	/**
	 * Get seesion data bean.
	 * @param userName user name
	 * @return session data bean
	 */
	private SessionDataBean getSessionDataBean(String userName) throws Exception
	{
		// Map<String, SessionDataBean> sessionDataMap = Variables.sessionDataMap;  
		SessionDataBean sessionDataBean = null;

		if (Variables.sessionDataMap.containsKey(userName))
		{
			sessionDataBean = Variables.sessionDataMap.get(userName);
		}
		else
		{
			User user = null;
			sessionDataBean = new SessionDataBean();
			sessionDataBean.setUserName(userName);

			sessionDataBean.setAdmin(false);
			user = AppUtility.getUser(userName);
			if (user.getRoleId().equalsIgnoreCase(Constants.ADMIN_USER))
			{
				sessionDataBean.setAdmin(true);
			}

			sessionDataBean.setUserId(user.getId());
			Variables.sessionDataMap.put(userName, sessionDataBean);
		}

		return sessionDataBean;
	}

	/**
	 * check whether the object is null or not
	 * @param domainObject domain object
	 * @param messageToken  message token 
	 * @throws Exception
	 */
	private void checkNullObject(Object domainObject, String messageToken) throws Exception
	{
		if (domainObject == null)
		{
			throw new Exception("Please enter valid " + messageToken + "!! " + messageToken
					+ " should not be NULL");
		}
	}

	/**
	 * Find out the matching participant list based on the participant object provided
	 * @param userName
	 * @param domainObject
	 * @return
	 * @throws Exception
	 */
	public List delegateGetParticipantMatchingObects(String userName, Object domainObject)
			throws Exception
	{
		List matchingObjects = new ArrayList();
		checkNullObject(domainObject, "Domain Object");
		String className = domainObject.getClass().getName();
		IFactory factory = AbstractFactoryConfig.getInstance().getBizLogicFactory();
		ParticipantBizLogic bizLogic = (ParticipantBizLogic) factory.getBizLogic(className);
		AbstractDomainObject abstractDomainObject = (AbstractDomainObject) domainObject;
		// not null check for Id
		//checkNullObject(abstractDomainObject.getId(),"Identifier");
		LookupLogic participantLookupLogicForSPR = (LookupLogic) Utility
				.getObject(XMLPropertyHandler.getValue(Constants.PARTICIPANT_LOOKUP_ALGO_FOR_SPR));
		matchingObjects = bizLogic.getListOfMatchingParticipants((Participant) domainObject,
				participantLookupLogicForSPR);
		/*bug 7561*/
		List filteredMatchingObjects = null;
		if (matchingObjects != null)
		{
			filteredMatchingObjects = delegateSearchFilter(userName, matchingObjects);
		}
		else
		{
			filteredMatchingObjects = new ArrayList();
		}
		return filteredMatchingObjects;
		//return matchingObjects;
	}

	/**
	 * Method to get next Specimen Collection Group Number
	 * @param userName
	 * @return
	 * @throws Exception
	 */
	public String delegateGetSpecimenCollectionGroupLabel(String userName, Object obj)
			throws Exception
	{
		LabelGenerator specimenCollectionGroupLableGenerator = LabelGeneratorFactory
				.getInstance(Constants.SPECIMEN_COLL_GROUP_LABEL_GENERATOR_PROPERTY_NAME);
		return specimenCollectionGroupLableGenerator.getLabel((SpecimenCollectionGroup) obj);
	}

	/**
	 * Method to get default value for given key using default value manager
	 * @param userName
	 * @param obj
	 * @return
	 * @throws Exception
	 */
	public String delegateGetDefaultValue(String userName, Object obj) throws Exception
	{
		return ((String) DefaultValueManager.getDefaultValue((String) obj));
	}

	 /**
     * Removes the identified data from identified Report object
     * @param object object of IdentifiedSurgicalPathologyReport
     */
    private void  removeIdentifiedReportIdentifiedData(Object object)
    {
    	IdentifiedSurgicalPathologyReport identiPathologyReport=(IdentifiedSurgicalPathologyReport)object;
    	identiPathologyReport.setCollectionDateTime(null);
    	identiPathologyReport.setId(null);
    	identiPathologyReport.setActivityStatus(null);
    	identiPathologyReport.setIsFlagForReview(null);
    	if(identiPathologyReport.getTextContent()!=null)
    	{
    		identiPathologyReport.getTextContent().setData(null);
    	}
    }
	/**
     * Removes the identified data from de-identified Report object
     * @param object object of DeIdentifiedSurgicalPathologyReport
     */
    private void  removeDeIdentifiedReportIdentifiedData(Object object)
    {
    	DeidentifiedSurgicalPathologyReport deIdentiPathologyReport=(DeidentifiedSurgicalPathologyReport)object;
    	deIdentiPathologyReport.setCollectionDateTime(null);
    	deIdentiPathologyReport.setId(null);
    	deIdentiPathologyReport.setActivityStatus(null);
    	deIdentiPathologyReport.setIsFlagForReview(null);
    }
    
    /**
     * Removes the identified data from SpecimenEvent parameters object
     * @param object object of child classes of SpecimenEventParameters
     */
    private void  removeSpecimenEventParameters(Object object)
    {
    	SpecimenEventParameters specimenEventParameters=(SpecimenEventParameters)object;
    	specimenEventParameters.setTimestamp(null);
    }

    /**
     * Removes the identified data from PMI object
     * @param object PMI Object
     */
    private void  removePMIIdentifiedData(Object object)
    {
    	ParticipantMedicalIdentifier pmi = (ParticipantMedicalIdentifier) object;
		pmi.setMedicalRecordNumber(null);
    }
    
    /**
     * Removes the identified data from SpecimenArrayContent object
     * @param object SpecimenArrayContent object to filter
     * @throws DAOException 
     */
    private void removeSpecimenArrayContentIdentifiedData(Object object)
    {
    	SpecimenArrayContent specimenArrayContent=(SpecimenArrayContent)object;
    	removeSpecimenIdentifiedData(specimenArrayContent.getSpecimen());
    }
    
    /**
     * @param queryObject Query object to audit 
     * @param userName User Name
     * @throws Exception Exception 
     */
	public void auditAPIQuery(String queryObject, String userName) throws Exception
	{
		SessionDataBean sessionDataBean = getSessionDataBean(userName);
		insertQuery(queryObject, sessionDataBean);
	}

	/***
	 * Copy paste from Query Bizlogic class to insert API query Log
	 * @param sqlQuery
	 * @param sessionData
	 * @throws DAOException
	 * @throws ClassNotFoundException
	 */
	private void insertQuery(String sqlQuery, SessionDataBean sessionData) throws Exception
	{

		JDBCDAO jdbcDAO=null;
		try
		{

			String sqlQuery1 = sqlQuery.replaceAll("'", "''");
			long no = 1;
			jdbcDAO = AppUtility.openJDBCSession();			
			SimpleDateFormat fSDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String timeStamp = fSDateFormat.format(new Date());

			String ipAddr = sessionData.getIpAddress();

			String userId = sessionData.getUserId().toString();
			String comments = "APIQueryLog";
			String applicationName = CommonServiceLocator.getInstance().getAppName();
			String databaseType=DAOConfigFactory.getInstance().getDAOFactory(applicationName).getDataBaseType();
			if (databaseType.equals(Constants.ORACLE_DATABASE))
			{
				String sql = "select CATISSUE_AUDIT_EVENT_PARAM_SEQ.nextVal from dual";

				List list = jdbcDAO.executeQuery(sql);

				if (!list.isEmpty())
				{

					List columnList = (List) list.get(0);
					if (!columnList.isEmpty())
					{
						String str = (String) columnList.get(0);
						if (!str.equals(""))
						{
							no = Long.parseLong(str);

						}
					}
				}
				String sqlForAudiEvent = "insert into catissue_audit_event(IDENTIFIER,IP_ADDRESS,EVENT_TIMESTAMP,USER_ID ,COMMENTS) values ('"
						+ no
						+ "','"
						+ ipAddr
						+ "',to_date('"
						+ timeStamp
						+ "','yyyy-mm-dd HH24:MI:SS'),'" + userId + "','" + comments + "')";
				logger.info("sqlForAuditLog:" + sqlForAudiEvent);
				jdbcDAO.executeUpdate(sqlForAudiEvent);

				long queryNo = 1;
				sql = "select CATISSUE_AUDIT_EVENT_QUERY_SEQ.nextVal from dual";

				list = jdbcDAO.executeQuery(sql);

				if (!list.isEmpty())
				{

					List columnList = (List) list.get(0);
					if (!columnList.isEmpty())
					{
						String str = (String) columnList.get(0);
						if (!str.equals(""))
						{
							queryNo = Long.parseLong(str);

						}
					}
				}
				String sqlForQueryLog = "insert into catissue_audit_event_query_log(IDENTIFIER,QUERY_DETAILS,AUDIT_EVENT_ID) "
						+ "values (" + queryNo + ",EMPTY_CLOB(),'" + no + "')";
				jdbcDAO.executeUpdate(sqlForQueryLog);
				String sql1 = "select QUERY_DETAILS from catissue_audit_event_query_log where IDENTIFIER="
						+ queryNo + " for update";
				list = jdbcDAO.executeQuery(sql1);

				CLOB clob = null;

				if (!list.isEmpty())
				{

					List columnList = (List) list.get(0);
					if (!columnList.isEmpty())
					{
						clob = (CLOB) columnList.get(0);
					}
				}
				//				get output stream from the CLOB object
				OutputStream os = clob.getAsciiOutputStream();
				OutputStreamWriter osw = new OutputStreamWriter(os);

				//			use that output stream to write character data to the Oracle data store
				osw.write(sqlQuery1.toCharArray());
				//write data and commit
				osw.flush();
				osw.close();
				os.close();
				jdbcDAO.commit();
				logger.info("sqlForQueryLog:" + sqlForQueryLog);
			}
			else
			{
				String sqlForAudiEvent = "insert into catissue_audit_event(IP_ADDRESS,EVENT_TIMESTAMP,USER_ID ,COMMENTS) values ('"
						+ ipAddr + "','" + timeStamp + "','" + userId + "','" + comments + "')";
				jdbcDAO.executeUpdate(sqlForAudiEvent);

				String sql = "select max(identifier) from catissue_audit_event where USER_ID='"
						+ userId + "'";

				List list = jdbcDAO.executeQuery(sql);

				if (!list.isEmpty())
				{
					List columnList = (List) list.get(0);
					if (!columnList.isEmpty())
					{
						String str = (String) columnList.get(0);
						if (!str.equals(""))
						{
							no = Long.parseLong(str);

						}
					}
				}
				String sqlForQueryLog = "insert into catissue_audit_event_query_log(QUERY_DETAILS,AUDIT_EVENT_ID) values ('"
						+ sqlQuery1 + "','" + no + "')";
				logger.debug("sqlForQueryLog:" + sqlForQueryLog);
				jdbcDAO.executeUpdate(sqlForQueryLog);
				jdbcDAO.commit();
			}
		}
		catch (Exception e)
		{
			logger.debug(e.getMessage(), e);
			throw new Exception(e.getMessage());
		}
		finally
		{
			AppUtility.closeJDBCSession(jdbcDAO);
		}
		/*String sqlForQueryLog = "insert into catissue_audit_event_query_log(IDENTIFIER,QUERY_DETAILS) values ('"
		 + no + "','" + sqlQuery1 + "')";
		 jdbcDAO.executeUpdate(sqlForQueryLog);*/

	}
}