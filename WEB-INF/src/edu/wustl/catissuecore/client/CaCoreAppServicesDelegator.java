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
import java.util.Iterator;
import java.util.List;

import edu.wustl.catissuecore.bizlogic.BizLogicFactory;
import edu.wustl.catissuecore.domainobject.ClinicalReport;
import edu.wustl.catissuecore.domainobject.CollectionProtocolRegistration;
import edu.wustl.catissuecore.domainobject.Participant;
import edu.wustl.catissuecore.domainobject.Specimen;
import edu.wustl.catissuecore.domainobject.SpecimenCollectionGroup;
import edu.wustl.catissuecore.domainobject.impl.CollectionProtocolRegistrationImpl;
import edu.wustl.catissuecore.domainobject.impl.ParticipantImpl;
import edu.wustl.catissuecore.domainobject.impl.SpecimenCollectionGroupImpl;
import edu.wustl.catissuecore.domainobject.impl.SpecimenImpl;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.bizlogic.QueryBizLogic;
import edu.wustl.common.security.SecurityManager;
import edu.wustl.common.util.Permissions;
import edu.wustl.common.util.dbManager.DAOException;
import edu.wustl.common.util.dbManager.HibernateMetaData;
import edu.wustl.common.util.logger.Logger;

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
    public String delegateLogin(String userName,String password) throws Exception
	{
		CaTissueHTTPClient httpClient = CaTissueHTTPClient.getInstance();
		String sessionID = httpClient.connect(userName,password);
		Logger.out.debug("****************** HTTP LOGIN STATUS: Username:"+ userName + " sessionID" + sessionID);
		return sessionID;
	}
	
    /**
     * Disconnects User from caTISSUE Core Application
     * @param sessionKey
     * @return returns the status of logout to caTISSUE Core Application
     */
	public boolean delegateLogout(String sessionKey)// throws Exception
	{
	    try
		{
			CaTissueHTTPClient httpClient = CaTissueHTTPClient.getInstance();
		
			boolean status = httpClient.disConnect(sessionKey);
		
			Logger.out.debug("****************** HTTP LOGOUT STATUS : " + status);
		}
		catch(Exception e)
		{
			
		}
		
		return false;
	}
	
	/**
	 * Passes caCore Like domain object to CaTissueHTTPClient to perform Add operation.
	 * @param obj the caCore Like object to add using HTTP API
	 * @param sessionKey
	 * @return returns the Added caCore Like object/Exception object if exception occurs performing Add operation
	 * @throws Exception
	 */
	public Object delegateAdd(String sessionKey, Object obj) throws Exception
	{
	    try
	    {
	        CaTissueHTTPClient httpClient = CaTissueHTTPClient.getInstance();
	        return httpClient.add(sessionKey,obj);
	    }
	    catch(Exception e)
	    {
	        Logger.out.error("Delegate Add-->" + e.getMessage());
	        throw e;
	    }
	}
	
	/**
	 * Passes caCore Like domain object to CaTissueHTTPClient to perform Edit operation.
	 * @param obj the caCore Like object to edit using HTTP API
	 * @param sessionKey 
	 * @return returns the Edited caCore Like object/Exception object if exception occurs performing Edit operation
	 * @throws Exception
	 */
	public Object delegateEdit(String sessionKey, Object obj) throws Exception
	{
		try
		{
		    CaTissueHTTPClient httpClient = CaTissueHTTPClient.getInstance();
		    return httpClient.edit(sessionKey, obj);
		}
		catch(Exception e)
		{
		    Logger.out.error("Delegate Edit"+ e.getMessage());
	        throw e;
		}
	}
	
	/**
	 * Returns Exception object as Delete operation is not supported by CaTissue Core Application.
	 * @param obj the caCore Like object to delete using HTTP API
	 * @return returns Exception object as Delete operation is not supported by CaTissue Core Application.
	 * @throws Exception
	 */
	public Object delegateDelete(Object obj) throws Exception
	{
		throw new Exception("Does not support delete");
	}
	
	public List delegateSearchFilter(String userName,List list) throws Exception
	{
	    Logger.out.debug("User Name : "+userName);
	    Logger.out.debug("list obtained from ApplicationService Search************** : "+list.getClass().getName());
	    Logger.out.debug("Super Class ApplicationService Search************** : "+list.getClass().getSuperclass().getName());
	    List filteredObjects = null;//new ArrayList();
	    
	    try
	    {
	        filteredObjects = filterObjects(userName, list);
	    }
	    catch (Exception exp)
	    {
	        exp.printStackTrace();
	        throw exp;
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
	    Logger.out.debug("In Filter Objects ......");
	    
	    // boolean that indicates whether user has READ_DENIED privilege on the main object.
		boolean hasReadDeniedForMain = false;
		
		// boolean that indicates whether user has privilege on identified data.
		boolean hasPrivilegeOnIdentifiedData = false;
		List filteredObjects = new ArrayList();
		
		Logger.out.debug("Total Objects>>>>>>>>>>>>>>>>>>>>>"+objectList.size());
		Iterator iterator = objectList.iterator();
//		Logger.out.debug("objectList iterator Class>>>>>>>>>>>>>>>>>>>>>"+objectList.iterator().getClass());
//		Logger.out.debug("Object List Class>>>>>>>>>>>>>>>>>>>>>"+objectList.getClass());
		while(iterator.hasNext())
		{
		    
		    Object abstractDomainObject = (Object) iterator.next();//objectList.get(i);
		    
		    //Get identifier of the object. 
		    Object identifier = getFieldObject(abstractDomainObject, "id");
		    Logger.out.debug("object Identifier......................: "+identifier);
		    
            String aliasName = getAliasName(abstractDomainObject);
            
            // Check the permission of the user on the main object.
		    hasReadDeniedForMain = SecurityManager.getInstance(CaCoreAppServicesDelegator.class)
		    							.checkPermission(userName, aliasName,
		    							        identifier, Permissions.READ_DENIED);
		    
		    Logger.out.debug("Main object:" + aliasName + " Has READ_DENIED privilege:" + hasReadDeniedForMain);
		    
		    // If the user has READ_DENIED privilege on the object, remove that object from the list. 
		    if (hasReadDeniedForMain)
		    {
		        Logger.out.debug("Removing Object >>>>>>>>>>>>>>>>>>>>>>>>"+identifier);
////	            iterator.remove();
//		        toBeRemoved.add(abstractDomainObject);
		    }
		    else// In case of no READ_DENIED privilege, check for privilege on identified data. 
		    {
		        //Check the permission of the user on the identified data of the object.
		        hasPrivilegeOnIdentifiedData = SecurityManager.getInstance(CaCoreAppServicesDelegator.class) 
													.checkPermission(userName, aliasName,
													     identifier, Permissions.IDENTIFIED_DATA_ACCESS);
		        
				Logger.out.debug("hasPrivilegeOnIdentifiedData:" + hasPrivilegeOnIdentifiedData);
				
				// If has no read privilege on identified data, set the identified attributes as NULL. 
				if (hasPrivilegeOnIdentifiedData == false)
				{
				    removeIdentifiedDataFromObject(abstractDomainObject);
				}
				
				Logger.out.debug("Object Added to filteredObjects List........................."+identifier);
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
        String domainClassName = domainObjectClassName.substring(0, (domainObjectClassName.length()-4));
        Logger.out.debug("Class Name >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+domainClassName);
        className = Class.forName("edu.wustl.catissuecore.domain."+domainClassName);
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
     */
	private void removeSpecimenCollectionGroupIdentifiedData(Object object)
	{
	    SpecimenCollectionGroup specimenCollGrp = (SpecimenCollectionGroup) object;
	    
	    ClinicalReport clinicalReport = specimenCollGrp.getClinicalReport();
	    clinicalReport.setSurgicalPathologyNumber(null);
	    
//	    ParticipantMedicalIdentifier participantMedicalIdentifier 
//	    						= clinicalReport.getParticipantMedicalIdentifier();
//	    if (participantMedicalIdentifier != null)
//	    {
//	        participantMedicalIdentifier.setMedicalRecordNumber(null);
//	        
//	        Participant participant = participantMedicalIdentifier.getParticipant();
//		    participant.setFirstName(null);
//		    participant.setLastName(null);
//		    participant.setMiddleName(null);
//		    participant.setBirthDate(null);
//		    participant.setSocialSecurityNumber(null);
//	    }
	}
	
	/**
     * Removes the identified data from Specimen object.
     * @param object The Specimen object.
     */
	private void removeSpecimenIdentifiedData(Object object) 
	{
	    Specimen specimen = (Specimen) object;
	    
	    SpecimenCollectionGroup specimenCollectionGroup = 
	        	(SpecimenCollectionGroup) specimen.getSpecimenCollectionGroup();
	    
	    removeSpecimenCollectionGroupIdentifiedData(specimenCollectionGroup);
	}
	
	/**
     * Removes the identified data from CollectionProtocolRegistration object.
     * @param object The CollectionProtocolRegistration object.
     */
	private void removeCollectionProtocolRegistrationIdentifiedData(Object object)
	{
	    CollectionProtocolRegistration collectionProtocolRegistration = (CollectionProtocolRegistration) object;
	    collectionProtocolRegistration.setRegistrationDate(null);
	    
	    removeParticipantIdentifiedData(collectionProtocolRegistration.getParticipant()); 
	}
	
	/**
	 * Sets value of the identified data fields as null in the passed domain object. 
	 * Checks the type of the object and calls the respective method which filters the identified data.
	 * @param object The domain object whose identified data is to be removed.
	 */
	private void removeIdentifiedDataFromObject(Object object)
	{
	    Class classObject = object.getClass();
	    Logger.out.debug("Identified Class>>>>>>>>>>>>>>>>>>>>>>"+classObject.getName());
	    if (classObject.equals(ParticipantImpl.class))
	    {
	        removeParticipantIdentifiedData(object);
	    }
	    else if (classObject.equals(SpecimenCollectionGroupImpl.class))
	    {
	        removeSpecimenCollectionGroupIdentifiedData(object);
	    }
	    else if (classObject.getSuperclass().equals(SpecimenImpl.class))
	    {
	        removeSpecimenIdentifiedData(object);
	    }
	    else if (classObject.equals(CollectionProtocolRegistrationImpl.class))
	    {
	        removeCollectionProtocolRegistrationIdentifiedData(object);
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
}