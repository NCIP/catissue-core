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

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;
import edu.wustl.catissuecore.bizlogic.BizLogicFactory;
import edu.wustl.catissuecore.bizlogic.ParticipantBizLogic;
import edu.wustl.catissuecore.bizlogic.UserBizLogic;
import edu.wustl.catissuecore.domain.CollectionProtocolRegistration;
import edu.wustl.catissuecore.domain.Participant;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.domain.pathology.IdentifiedSurgicalPathologyReport;
import edu.wustl.catissuecore.domain.pathology.ReportLoaderQueue;
import edu.wustl.catissuecore.namegenerator.LabelGenerator;
import edu.wustl.catissuecore.namegenerator.LabelGeneratorFactory;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.global.DefaultValueManager;
import edu.wustl.catissuecore.util.global.Utility;
import edu.wustl.catissuecore.util.global.Variables;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.bizlogic.IBizLogic;
import edu.wustl.common.bizlogic.QueryBizLogic;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.security.PrivilegeCache;
import edu.wustl.common.security.PrivilegeManager;
import edu.wustl.common.security.SecurityManager;
import edu.wustl.common.security.exceptions.SMException;
import edu.wustl.common.util.dbManager.DAOException;
import edu.wustl.common.util.dbManager.DBUtil;
import edu.wustl.common.util.dbManager.HibernateMetaData;
import edu.wustl.common.util.global.PasswordManager;
import edu.wustl.common.util.logger.Logger;
import gov.nih.nci.security.authorization.domainobjects.Role;

/**
 * This class contains the basic methods that are required for HTTP APIs. 
 * It just passes on the request at proper place.
 * @author aniruddha_phadnis
 */
public class CaCoreAppServicesDelegator
{
	
	/**
	 * Passes User credentials to CaTissueHTTPClient to connect User with caTISSUE Core Application
	 * @param userName userName of the User to connect to caTISSUE Core Application
	 * @param password password of the User to connect to caTISSUE Core Application
	 * @return the sessionID of user if he/she has successfullyy logged in else null
	 * @throws Exception 
	 */
    public Boolean delegateLogin(String userName,String password) throws Exception
	{
    	User validUser = Utility.getUser(userName);
    	Boolean authenticated = Boolean.valueOf(false);
    	if (validUser != null)
    	{	
    		password = PasswordManager.encode(password);
            boolean loginOK = SecurityManager.getInstance(CaCoreAppServicesDelegator.class).login(userName, password);
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
	    	checkNullObject(domainObject,"Domain Object");
			IBizLogic bizLogic = getBizLogic(domainObject.getClass().getName());
			bizLogic.insert(domainObject,getSessionDataBean(userName),Constants.HIBERNATE_DAO);
			Logger.out.info(" Domain Object has been successfully inserted " + domainObject);
	    }
	    catch(Exception e)
	    {
	        Logger.out.error("Delegate Add-->" + e.getMessage());
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
		try
		{
			/*
			if (domainObject == null) 
			{
				throw new Exception("Please enter valid domain object!! Domain object should not be NULL");
			}
			*/
			checkNullObject(domainObject,"Domain Object");
			String objectName = domainObject.getClass().getName();
			IBizLogic bizLogic = getBizLogic(objectName);
			AbstractDomainObject abstractDomainObject = (AbstractDomainObject) domainObject;
			// not null check for Id
			checkNullObject(abstractDomainObject.getId(),"Identifier");
			Object object = bizLogic.retrieve(objectName, abstractDomainObject.getId());
            
			if (object == null)
			{
				throw new Exception("No such domain object found for update !! Please enter valid domain object for edit");
			}
			AbstractDomainObject abstractDomainOld = (AbstractDomainObject) object;
			Session sessionClean = DBUtil.getCleanSession();
			abstractDomainOld = (AbstractDomainObject) sessionClean.load(Class.forName(objectName), new Long(abstractDomainObject.getId()));
			bizLogic.update(abstractDomainObject, abstractDomainOld, Constants.HIBERNATE_DAO, getSessionDataBean(userName));
			sessionClean.close();
			Logger.out.info(" Domain Object has been successfully updated " + domainObject);
		}
		catch(Exception e)
		{
		    Logger.out.error("Delegate Edit"+ e.getMessage());
	        throw e;
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
		if(domainObject instanceof ReportLoaderQueue)
		{
			BizLogicFactory bizLogicFactory = BizLogicFactory.getInstance();
			IBizLogic bizLogic = bizLogicFactory.getBizLogic(domainObject.getClass().getName());
			bizLogic.delete(domainObject,Constants.HIBERNATE_DAO);
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
		checkNullObject(domainObject,"Domain Object");
		String objectName = domainObject.getClass().getName();
		IBizLogic bizLogic = getBizLogic(objectName);
		AbstractDomainObject abstractDomainObject = (AbstractDomainObject) domainObject;
		// not null check for Id
		checkNullObject(abstractDomainObject.getId(),"Identifier");
		searchObjects = bizLogic.retrieve(objectName, Constants.SYSTEM_IDENTIFIER,
				  abstractDomainObject.getId());
		
		if (searchObjects.isEmpty())
		{
			throw new Exception("Please enter valid domain object for search operation!!");			
		}
		return searchObjects;
	}
	
	public List delegateSearchFilter(String userName,List list) throws Exception
	{
	    Logger.out.debug("User Name : "+userName);
	    Logger.out.debug("list obtained from ApplicationService Search************** : "+list.getClass().getName());
	    Logger.out.debug("Super Class ApplicationService Search************** : "+list.getClass().getSuperclass().getName());
	    List filteredObjects = null;//new ArrayList();
	    User validUser = Utility.getUser(userName);
	    String reviewerRole=null;
        SecurityManager securityManager=SecurityManager.getInstance(this.getClass());
        try
        {
              Role role=securityManager.getUserRole(validUser.getCsmUserId());
              if (validUser.getRoleId().equalsIgnoreCase(Constants.ADMIN_USER))
              {
            	  reviewerRole = Constants.ADMINISTRATOR;
              }
             // reviewerRole=role.getName();
        }
        catch(SMException ex)
        {
              Logger.out.info("Review Role not found!");
        }
        if(reviewerRole!=null && (reviewerRole.equalsIgnoreCase(Constants.ADMINISTRATOR)))
        {
        	filteredObjects=list;
        }
        else
        {
        	try
    	    {
    	        filteredObjects = filterObjects(userName, list);
    	    }
    	    catch (Exception exp)
    	    {
    	        exp.printStackTrace();
    	        throw exp;
    	    }
        }
        
		return filteredObjects;
	}	
	
	/**
	 * Filters the list of objects returned by the search depending on the privilege of the user on the objects.
	 * Also sets the identified data to null if the user doesn'r has privilege to see the identified data. 
	 * @param userName The name of the user whose privilege are to be checked.
	 * @param objectList The list of the objects which are to be filtered.
	 * @return The filtered list of objects according to the privilege of the user.
	 * @throws Exception 
	 */
	private List filterObjects(String userName, List objectList) throws Exception
	{
	    Logger.out.debug("In Filter Objects ......" );
	    
	    // boolean that indicates whether user has READ_DENIED privilege on the main object.
		boolean hasReadDeniedForMain = false;
		
		// boolean that indicates whether user has privilege on identified data.
		boolean hasPrivilegeOnIdentifiedData = false;
		List filteredObjects = new ArrayList();
		
		// To get privilegeCache through 
		// Singleton instance of PrivilegeManager, requires User LoginName		
		PrivilegeManager privilegeManager = PrivilegeManager.getInstance();
		PrivilegeCache privilegeCache = privilegeManager.getPrivilegeCache(userName);
		
		Logger.out.debug("Total Objects>>>>>>>>>>>>>>>>>>>>>"+objectList.size());
		Iterator iterator = objectList.iterator();
		while(iterator.hasNext())
		{
		    
		    Object abstractDomainObject = (Object) iterator.next();//objectList.get(i);
		    
		    //Get identifier of the object. 
		    Object identifier = getFieldObject(abstractDomainObject, "id");
		    Logger.out.debug("object Identifier......................: "+identifier);
		    
            String aliasName = getAliasName(abstractDomainObject);
            
            // Check the permission of the user on the main object.
			// Call to SecurityManager.checkPermission bypassed &
			// instead, call redirected to privilegeCache.hasPrivilege            
        //     hasReadDeniedForMain = privilegeCache.hasPrivilege((AbstractDomainObject)abstractDomainObject, Permissions.READ_DENIED);
            
            /*
			 * this issue, as to whose privileges are to be checked, will be resolved later
			 */
			hasReadDeniedForMain = false;
            
//		    hasReadDeniedForMain = SecurityManager.getInstance(CaCoreAppServicesDelegator.class)
//		    							.checkPermission(userName, aliasName,
//		    							        identifier, Permissions.READ_DENIED);
		    
		    Logger.out.debug("Main object:" + aliasName + " Has READ_DENIED privilege:" + hasReadDeniedForMain);
		    
		    // If the user has READ_DENIED privilege on the object, remove that object from the list. 
/*		    if (hasReadDeniedForMain)
		    {
//		        Logger.out.debug("Removing Object >>>>>>>>>>>>>>>>>>>>>>>>"+identifier);
////	            iterator.remove();
//		        toBeRemoved.add(abstractDomainObject);
		    } else
*/		  
		    if (!hasReadDeniedForMain)// In case of no READ_DENIED privilege, check for privilege on identified data. 
		    {
		        //Check the permission of the user on the identified data of the object.
				// Call to SecurityManager.checkPermission bypassed &
				// instead, call redirected to privilegeCache.hasPrivilege		    	
		  //  	hasPrivilegeOnIdentifiedData = privilegeCache.
		  //  					hasPrivilege((AbstractDomainObject)abstractDomainObject, Permissions.IDENTIFIED_DATA_ACCESS);
		    	
		    	/*
				 * this issue, as to whose privileges are to be checked, will be resolved later
				 */
				hasPrivilegeOnIdentifiedData = true;
		    	
//		        hasPrivilegeOnIdentifiedData = SecurityManager.getInstance(CaCoreAppServicesDelegator.class) 
//													.checkPermission(userName, aliasName,
//													     identifier, Permissions.IDENTIFIED_DATA_ACCESS);
		        
				Logger.out.debug("hasPrivilegeOnIdentifiedData:" + hasPrivilegeOnIdentifiedData);
				// If has no read privilege on identified data, set the identified attributes as NULL. 
				if (hasPrivilegeOnIdentifiedData == false)
				{
					// commented because of lazy initialization problem
				    removeIdentifiedDataFromObject(abstractDomainObject);
				}
				
				filteredObjects.add(abstractDomainObject);
				Logger.out.debug("Intermediate Size of filteredObjects .............."+filteredObjects.size());
			}
		}
		
//		Logger.out.debug("To Be Removed......................"+toBeRemoved.size());
		Logger.out.debug("Before Final Objects >>>>>>>>>>>>>>>>>>>>>>>>>"+filteredObjects.size());
//		boolean status = objectList.removeAll(toBeRemoved);
//		Logger.out.debug("Remove Status>>>>>>>>>>>>>>>>>>>>>>>>"+status);
//		SDKListProxy finalFilteredObjects = new SDKListProxy();
//		finalFilteredObjects.setHasAllRecords(true);
//		finalFilteredObjects.addAll(filteredObjects);
		
//		Logger.out.debug("Final Objects >>>>>>>>>>>>>>>>>>>>>>>>>"+finalFilteredObjects.size());
		
		return filteredObjects;
	}
	
	/**
	 * Returns the alias name of the domain object passed.   
	 * @param object The domain object whose alias name is to be found. 
     * @return the alias name of the domain object passed.
     * @throws ClassNotFoundException
     * @throws DAOException
     */
    private String getAliasName(Object object) throws ClassNotFoundException, DAOException
    {
        Class className = object.getClass();
        String domainObjectClassName = edu.wustl.common.util.Utility.parseClassName(className.getName());
        String domainClassName = domainObjectClassName;
        //String domainClassName = domainObjectClassName.substring(0, (domainObjectClassName.length()-4));
        Logger.out.debug("Class Name >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+domainClassName);
        System.out.println("Class Name >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+domainClassName);
        Logger.out.info("Class Name >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+domainClassName);
        try
        {
        	className = Class.forName("edu.wustl.catissuecore.domain."+domainClassName);
        }catch (ClassNotFoundException ex) 
        {
        	Logger.out.error("ClassNotFoundException in CaCoreAppServicesDelegator.getAliasName() method");
        	className = Class.forName(object.getClass().getName());
		}
        String tableName = "'" + HibernateMetaData.getTableName(className) + "'";
            
        QueryBizLogic bizLogic = (QueryBizLogic) BizLogicFactory.getInstance().getBizLogic(Constants.QUERY_INTERFACE_ID);
        String aliasName = bizLogic.getAliasName(Constants.TABLE_NAME_COLUMN, tableName);
        return aliasName;
    }

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
	    participant.setSocialSecurityNumber(null);
	    
//	    Collection participantMedicalIdentifierCollection 
//	    				= participant.getParticipantMedicalIdentifierCollection();
//	    for (Iterator iterator = participantMedicalIdentifierCollection.iterator();iterator.hasNext();)
//	    {
//	        ParticipantMedicalIdentifier participantMedId = (ParticipantMedicalIdentifier) iterator.next();
//	        participantMedId.setMedicalRecordNumber(null);
//	    }
//	    
//	    Collection collectionProtocolRegistrationCollection 
//	    				= participant.getCollectionProtocolRegistrationCollection();
//	    for (Iterator iterator=collectionProtocolRegistrationCollection.iterator();iterator.hasNext();)
//	    {
//	        CollectionProtocolRegistration collectionProtReg = (CollectionProtocolRegistration) iterator.next();
//	        collectionProtReg.setRegistrationDate(null); 
//	    }
	}
	
    /**
     * Removes the identified data from SpecimenCollectionGroup object.
     * @param object The SpecimenCollectionGroup object.
     * @throws DAOException 
     */
	private void removeSpecimenCollectionGroupIdentifiedData(Object object) throws DAOException
	{
		/**
		 * Kalpana 
		 * Bug #6076
		 * Reviewer : 
		 * Description : Because of lazy initialization problem retrieved the object.
		 */
	    SpecimenCollectionGroup specimenCollGrp = (SpecimenCollectionGroup) object;
	    Logger.out.info("specimenCollGrp getClinicalDiagnosis : " + specimenCollGrp.getClinicalDiagnosis());
	    specimenCollGrp.setSurgicalPathologyNumber(null);
	    
		IBizLogic bizLogic=getBizLogic(SpecimenCollectionGroup.class.getName());
		IdentifiedSurgicalPathologyReport identifiedSurgicalPathologyReport = (IdentifiedSurgicalPathologyReport)bizLogic.retrieveAttribute(SpecimenCollectionGroup.class.getName(),specimenCollGrp.getId(),Constants.IDENTIFIED_SURGICAL_PATHOLOGY_REPORT);
		if (identifiedSurgicalPathologyReport != null)
		{	
			removeIdentifiedReportIdentifiedData(identifiedSurgicalPathologyReport);
		}	
	}
	
	/**
     * Removes the identified data from Specimen object.
     * @param object The Specimen object.
	 * @throws DAOException 
     */
	private void removeSpecimenIdentifiedData(Object object) throws DAOException 
	{
		
		/**
		 * Kalpana 
		 * Bug #6076
		 * Reviewer : 
		 * Description : Because of lazy initialization problem retrieved the object.
		 */
		
	    Specimen specimen = (Specimen) object;
	    // call Biz logic for change in our objects
	    SpecimenCollectionGroup specimenCollectionGroup=null;
		IBizLogic bizLogic = getBizLogic(SpecimenCollectionGroup.class.getName());
		Object SCGobject =  bizLogic.retrieve(SpecimenCollectionGroup.class.getName(), specimen.getSpecimenCollectionGroup().getId());
		if(SCGobject != null)
		{
			specimenCollectionGroup = (SpecimenCollectionGroup) SCGobject;
		}
	    
		removeSpecimenCollectionGroupIdentifiedData(specimenCollectionGroup);
	    	
	}
	
	/**
     * Removes the identified data from CollectionProtocolRegistration object.
     * @param object The CollectionProtocolRegistration object.
	 * @throws DAOException 
     */
	private void removeCollectionProtocolRegistrationIdentifiedData(Object object) throws DAOException
	{
	    IBizLogic bizlogic = BizLogicFactory.getInstance().getBizLogic(Constants.DEFAULT_BIZ_LOGIC);
		CollectionProtocolRegistration collectionProtocolRegistration = (CollectionProtocolRegistration) object;
	    collectionProtocolRegistration.setRegistrationDate(null);
	    collectionProtocolRegistration.setSignedConsentDocumentURL(null);
	    collectionProtocolRegistration.setConsentSignatureDate(null);
	    collectionProtocolRegistration.setConsentWitness(null);
	    Participant participant = (Participant)bizlogic.retrieveAttribute(CollectionProtocolRegistration.class.getName(),collectionProtocolRegistration.getId(),"participant");
	    if (participant != null)
	    {	
	    	removeParticipantIdentifiedData(participant);
	    }	
	}
	
	/**
	 * Sets value of the identified data fields as null in the passed domain object. 
	 * Checks the type of the object and calls the respective method which filters the identified data.
	 * @param object The domain object whose identified data is to be removed.
	 * @throws DAOException 
	 */
	private void removeIdentifiedDataFromObject(Object object) throws DAOException
	{
	    Class classObject = object.getClass();
	    Logger.out.debug("Identified Class>>>>>>>>>>>>>>>>>>>>>>"+classObject.getName());
	    if (classObject.equals(Participant.class))
	    {
	        removeParticipantIdentifiedData(object);
	    }
	    else if (classObject.equals(SpecimenCollectionGroup.class))
	    {
	        removeSpecimenCollectionGroupIdentifiedData(object);
	    }
	    else if (classObject.getSuperclass().equals(Specimen.class))
	    {
	    	Logger.out.info(" Label :: " + ((Specimen) object).getLabel());
	        removeSpecimenIdentifiedData(object);
	    }
	    else if (classObject.equals(CollectionProtocolRegistration.class))
	    {
	        removeCollectionProtocolRegistrationIdentifiedData(object);
	    }
	    else if (classObject.equals(IdentifiedSurgicalPathologyReport.class))
	    {
	        removeIdentifiedReportIdentifiedData(object);
	    }
//	    if (Client.identifiedClassNames.contains(classObject.getName()) 
//	            || Client.identifiedFieldsMap.containsKey(classObject.getName()))
//	    {
//	        Vector identifiedFields = (Vector)Client.identifiedFieldsMap.get(classObject.getName());
//	        for (Iterator iterator = identifiedFields.iterator();iterator.hasNext();)
//	        {
//	            try
//	            {
//		            String fieldName = (String) iterator.next();
////		            Logger.out.debug("Field Name######################"+fieldName);
//		            Field identifiedField = classObject.getDeclaredField(fieldName);
//	                setFieldValue(object, fieldName, null, identifiedField.getType());
//	            }
//	            catch(NoSuchFieldException noFieldExp)
//	            {
//	                Logger.out.debug(noFieldExp.getMessage(), noFieldExp);
//	            }
//	        }
//	        
//	        Field allFields[] = classObject.getDeclaredFields();
//	        for (int i = 0; i<allFields.length; i++)
//	        {
//	            Logger.out.debug("Field Type$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$"+allFields[i].getType().getName());
//	            if (Client.identifiedClassNames.contains(allFields[i].getType().getName()) 
//	    	            || Client.identifiedFieldsMap.containsKey(allFields[i].getType().getName()))
//	            {
//                    AbstractDomainObject childObject 
//                				= (AbstractDomainObject)
//                						getFieldObject(object, allFields[i].getName());
//                    removeIdentifiedDataFromObject(childObject);
//	            }
//	            else if (allFields[i].getType().getName().equals(Collection.class.getName()))
//	            {
//	                Collection objectCollection 
//	                    		= (Collection)
//	                    			getFieldObject(object, allFields[i].getName());
//	                
//	                for (Iterator iterator = objectCollection.iterator(); iterator.hasNext();)
//	                {
//	                    AbstractDomainObject objectInCollection 
//	                    				= (AbstractDomainObject)iterator.next();
//	                    removeIdentifiedDataFromObject(objectInCollection);
//	                }
//	            }
//	        }
//	    }
	}
	
//	private void setFieldValue(AbstractDomainObject object, String methodName, String value, Class fieldType)
//	{
//	    methodName = "set" + methodName.substring(0,1).toUpperCase() 
//		   + methodName.substring(1);
//	    Class [] parameterTypes = {fieldType};
//	    
//	    try
//        {
//	        Object [] parameterValues = {value};
//            object.getClass().getMethod(methodName, parameterTypes)
//        					.invoke(object, parameterValues);
//        }
//        catch(NoSuchMethodException noMetExp)
//        {
//            Logger.out.debug(noMetExp.getMessage(), noMetExp);
//        }
//        catch(IllegalAccessException illAccExp)
//        {
//            Logger.out.debug(illAccExp.getMessage(), illAccExp);
//        }
//        catch(InvocationTargetException invoTarExp)
//        {
//            Logger.out.debug(invoTarExp.getMessage(), invoTarExp);
//        }
//	}
	
	/**
	 * Returns the field object from the class object and field name passed.
	 */
	private Object getFieldObject(Object object, String fieldName)
	{
	    Object childObject = null;
	    fieldName = "get" + fieldName.substring(0,1).toUpperCase() 
	    				   + fieldName.substring(1);
	    Logger.out.debug("Method Name***********************"+fieldName);
	    
	    try
        {
            childObject = (Object)object.getClass()
        						.getMethod(fieldName, null)
        							.invoke(object,null);
        }
        catch(NoSuchMethodException noMetExp)
        {
            Logger.out.debug(noMetExp.getMessage(), noMetExp);
        }
        catch(IllegalAccessException illAccExp)
        {
            Logger.out.debug(illAccExp.getMessage(), illAccExp);
        }
        catch(InvocationTargetException invoTarExp)
        {
            Logger.out.debug(invoTarExp.getMessage(), invoTarExp);
        }
        
        return childObject;
	}
	
	/**
	 * Get Biz Logic based on domai object class name.
	 * @param domainObjectName name of somain object
	 * @return biz logic
	 */
	private IBizLogic getBizLogic(String domainObjectName) 
	{
		BizLogicFactory factory = BizLogicFactory.getInstance();
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
		
		if(Variables.sessionDataMap.containsKey(userName))
		{
			sessionDataBean = Variables.sessionDataMap.get(userName);
		}
		else
		{
			User user = null;
			sessionDataBean = new SessionDataBean();
			sessionDataBean.setUserName(userName);

			sessionDataBean.setAdmin(false);
			user = Utility.getUser(userName);
			if(user.getRoleId().equalsIgnoreCase(Constants.ADMIN_USER))
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
	private void checkNullObject(Object domainObject,String messageToken) throws Exception
	{
		if (domainObject == null) 
		{
			throw new Exception("Please enter valid " + messageToken +"!! "+ messageToken + " should not be NULL");
		}
	}
	
   
    
    /**
     * Find out the matching participant list based on the participant object provided
     * @param userName
     * @param domainObject
     * @return
     * @throws Exception
     */
    public List delegateGetParticipantMatchingObects(String userName, Object domainObject)throws Exception
    {
    	List matchingObjects = new ArrayList();
		checkNullObject(domainObject,"Domain Object");
		String className = domainObject.getClass().getName();
		ParticipantBizLogic bizLogic =(ParticipantBizLogic)BizLogicFactory.getInstance().getBizLogic(className);
		AbstractDomainObject abstractDomainObject = (AbstractDomainObject) domainObject;
		// not null check for Id
		//checkNullObject(abstractDomainObject.getId(),"Identifier");
		matchingObjects = bizLogic.getListOfMatchingParticipants((Participant)domainObject);
		/*bug 7561*/
		List filteredMatchingObjects=null;
		if(matchingObjects!=null)
		{
			filteredMatchingObjects=delegateSearchFilter(userName, matchingObjects);
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
    public String delegateGetSpecimenCollectionGroupLabel(String userName, Object obj) throws Exception
    {
    	LabelGenerator specimenCollectionGroupLableGenerator = LabelGeneratorFactory.getInstance(Constants.SPECIMEN_COLL_GROUP_LABEL_GENERATOR_PROPERTY_NAME);
    	return specimenCollectionGroupLableGenerator.getLabel((SpecimenCollectionGroup)obj);
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
    	return((String)DefaultValueManager.getDefaultValue((String)obj));
    }
    
    /**
     * Removes the identified data from identified Report object
     * @param object object of IdentifiedSurgicalPathologyReport
     */
    private void  removeIdentifiedReportIdentifiedData(Object object)
    {
    	IdentifiedSurgicalPathologyReport identiPathologyReport=(IdentifiedSurgicalPathologyReport)object;
    	if(identiPathologyReport.getTextContent()!=null)
    	{
    		identiPathologyReport.getTextContent().setData(null);
    	}
    }
}