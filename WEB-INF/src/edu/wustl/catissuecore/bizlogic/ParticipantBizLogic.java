/**
 * <p>Title: ParticipantHDAO Class>
 * <p>Description:	ParticipantHDAO is used to add Participant's information into the database using Hibernate.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Aniruddha Phadnis
 * @version 1.00
 * Created on Jul 23, 2005
 */

package edu.wustl.catissuecore.bizlogic;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.ehcache.CacheException;
import edu.wustl.catissuecore.domain.CollectionProtocolRegistration;
import edu.wustl.catissuecore.domain.Participant;
import edu.wustl.catissuecore.domain.ParticipantMedicalIdentifier;
import edu.wustl.catissuecore.domain.Site;
import edu.wustl.catissuecore.integration.IntegrationManager;
import edu.wustl.catissuecore.integration.IntegrationManagerFactory;
import edu.wustl.catissuecore.util.ApiSearchUtil;
import edu.wustl.catissuecore.util.CatissueCoreCacheManager;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.global.Utility;
import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.bizlogic.IBizLogic;
import edu.wustl.common.cde.CDEManager;
import edu.wustl.common.dao.DAO;
import edu.wustl.common.dao.DAOFactory;
import edu.wustl.common.dao.JDBCDAO;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.lookup.DefaultLookupParameters;
import edu.wustl.common.lookup.LookupLogic;
import edu.wustl.common.security.SecurityManager;
import edu.wustl.common.security.exceptions.SMException;
import edu.wustl.common.security.exceptions.UserNotAuthorizedException;
import edu.wustl.common.util.XMLPropertyHandler;
import edu.wustl.common.util.dbManager.DAOException;
import edu.wustl.common.util.global.ApplicationProperties;
import edu.wustl.common.util.global.Validator;
import edu.wustl.common.util.logger.Logger;

/**
 * ParticipantHDAO is used to to add Participant's information into the database using Hibernate.
 * @author aniruddha_phadnis
 */
public class ParticipantBizLogic extends IntegrationBizLogic
{

	/**
	 * Saves the Participant object in the database.
	 * @param obj The storageType object to be saved.
	 * @param session The session in which the object is saved.
	 * @throws DAOException 
	 */
	protected void insert(Object obj, DAO dao, SessionDataBean sessionDataBean) throws DAOException, UserNotAuthorizedException
	{
		Participant participant = (Participant) obj;
		dao.insert(participant, sessionDataBean, true, true);
		Collection participantMedicalIdentifierCollection = participant.getParticipantMedicalIdentifierCollection();

		if(participantMedicalIdentifierCollection == null ) 
		{
			participantMedicalIdentifierCollection = new HashSet();
		}
		if (participantMedicalIdentifierCollection.isEmpty())
		{
			//add a dummy participant MedicalIdentifier for Query.
			ParticipantMedicalIdentifier participantMedicalIdentifier = new ParticipantMedicalIdentifier();
			participantMedicalIdentifier.setMedicalRecordNumber(null);
			participantMedicalIdentifier.setSite(null);
			participantMedicalIdentifierCollection.add(participantMedicalIdentifier);
		}

		//Inserting medical identifiers in the database after setting the participant associated.
		Iterator it = participantMedicalIdentifierCollection.iterator();
		while (it.hasNext())
		{
			ParticipantMedicalIdentifier pmIdentifier = (ParticipantMedicalIdentifier) it.next();	        
			pmIdentifier.setParticipant(participant);
			dao.insert(pmIdentifier, sessionDataBean, true, true);
		}

		Set protectionObjects = new HashSet();
		protectionObjects.add(participant);
		try
		{
			SecurityManager.getInstance(this.getClass()).insertAuthorizationData(null, protectionObjects, null);
		}
		catch (SMException e)
		{
			throw handleSMException(e);
		}
	}

	/**
	 * This method gets called after insert method. Any logic after insertnig object in database can be included here.
	 * @param obj The inserted object.
	 * @param dao the dao object
	 * @param sessionDataBean session specific data
	 * @throws DAOException
	 * @throws UserNotAuthorizedException 
	 * */
	protected void postInsert(Object obj, DAO dao, SessionDataBean sessionDataBean) throws DAOException, UserNotAuthorizedException
	{
			updateCache(obj);
	}
	
	/**
	 * This method gets called after update method. Any logic after updating into database can be included here.
	 * @param dao the dao object
	 * @param currentObj The object to be updated.
	 * @param oldObj The old object.
	 * @param sessionDataBean session specific data
	 * @throws DAOException
	 * @throws UserNotAuthorizedException
	 * */
	protected void postUpdate(DAO dao, Object currentObj, Object oldObj, SessionDataBean sessionDataBean) throws BizLogicException,
			UserNotAuthorizedException
	{
		   updateCache(currentObj);
	}
	/**
	 *  This method updates the cache for MAP_OF_PARTICIPANTS, shold be called in postInsert/postUpdate 
	 * @param obj - participant object
	 */
	private synchronized void updateCache(Object obj)
	{
		Participant participant = (Participant) obj;
       //  getting instance of catissueCoreCacheManager and getting participantMap from cache
		CatissueCoreCacheManager catissueCoreCacheManager = null;
		try
		{
			catissueCoreCacheManager = CatissueCoreCacheManager.getInstance();
			HashMap participantMap = (HashMap) catissueCoreCacheManager.getObjectFromCache(Constants.MAP_OF_PARTICIPANTS);
			if(participant.getActivityStatus().equalsIgnoreCase(Constants.ACTIVITY_STATUS_DISABLED))
			{
				participantMap.remove(participant.getId());
			}
			else
			{
			    participantMap.put(participant.getId(), participant);
			}
    	}
		catch (CacheException e)
		{
			Logger.out.debug("Exception occured while getting instance of cachemanager");
			e.printStackTrace();
		}
		
	}

	/**
	 * Updates the persistent object in the database.
	 * @param obj The object to be updated.
	 * @param session The session in which the object is saved.
	 * @throws DAOException 
	 */
	protected void update(DAO dao, Object obj, Object oldObj, SessionDataBean sessionDataBean) throws DAOException, UserNotAuthorizedException
	{
		Participant participant = (Participant) obj;
		Participant oldParticipant = (Participant) oldObj;

		dao.update(participant, sessionDataBean, true, true, false);
	
		//Audit of Participant.
		dao.audit(obj, oldObj, sessionDataBean, true);

		Collection oldParticipantMedicalIdentifierCollection = (Collection) oldParticipant.getParticipantMedicalIdentifierCollection();

		Collection participantMedicalIdentifierCollection = participant.getParticipantMedicalIdentifierCollection();
		Iterator it = participantMedicalIdentifierCollection.iterator();

		//Updating the medical identifiers of the participant
		while (it.hasNext())
		{
			ParticipantMedicalIdentifier pmIdentifier = (ParticipantMedicalIdentifier) it.next();
			
			/**
			 * Start: Change for API Search   --- Jitendra 06/10/2006
			 * In Case of Api Search, previoulsy it was failing since there was default class level initialization 
			 * on domain object. For example in User object, it was initialized as protected String lastName=""; 
			 * So we removed default class level initialization on domain object and are initializing in method
			 * setAllValues() of domain object. But in case of Api Search, default values will not get set 
			 * since setAllValues() method of domainObject will not get called. To avoid null pointer exception,
			 * we are setting the default values same as we were setting in setAllValues() method of domainObject.
			 */
	        ApiSearchUtil.setParticipantMedicalIdentifierDefault(pmIdentifier);
	        //End:-  Change for API Search 
	        
			pmIdentifier.setParticipant(participant);
			dao.update(pmIdentifier, sessionDataBean, true, true, false);

			//Audit of ParticipantMedicalIdentifier.
			ParticipantMedicalIdentifier oldPmIdentifier = (ParticipantMedicalIdentifier) getCorrespondingOldObject(
					oldParticipantMedicalIdentifierCollection, pmIdentifier.getId());

			dao.audit(pmIdentifier, oldPmIdentifier, sessionDataBean, true);
		}

		//Disable the associate collection protocol registration
		Logger.out.debug("participant.getActivityStatus() " + participant.getActivityStatus());
		if (participant.getActivityStatus().equals(Constants.ACTIVITY_STATUS_DISABLED))
		{
			Logger.out.debug("participant.getActivityStatus() " + participant.getActivityStatus());
			Long participantIDArr[] = {participant.getId()};

			CollectionProtocolRegistrationBizLogic bizLogic = (CollectionProtocolRegistrationBizLogic) BizLogicFactory.getInstance().getBizLogic(
					Constants.COLLECTION_PROTOCOL_REGISTRATION_FORM_ID);
			bizLogic.disableRelatedObjectsForParticipant(dao, participantIDArr);
		}
	}

	/**
	 * @see edu.wustl.common.bizlogic.IBizLogic#setPrivilege(DAO, String, Class, Long[], Long, String, boolean)
	 */
	protected void setPrivilege(DAO dao, String privilegeName, Class objectType, Long[] objectIds, Long userId, String roleId, boolean assignToUser,
			boolean assignOperation) throws SMException, DAOException
	{
		Logger.out.debug(" privilegeName:" + privilegeName + " objectType:" + objectType + " objectIds:"
				+ edu.wustl.common.util.Utility.getArrayString(objectIds) + " userId:" + userId + " roleId:" + roleId + " assignToUser:"
				+ assignToUser);
		super.setPrivilege(dao, privilegeName, objectType, objectIds, userId, roleId, assignToUser, assignOperation);

		CollectionProtocolRegistrationBizLogic bizLogic = (CollectionProtocolRegistrationBizLogic) BizLogicFactory.getInstance().getBizLogic(
				Constants.COLLECTION_PROTOCOL_REGISTRATION_FORM_ID);
		bizLogic.assignPrivilegeToRelatedObjectsForParticipant(dao, privilegeName, objectIds, userId, roleId, assignToUser, assignOperation);
	}

	/**
	 * Assigns the privilege to related objects for Collection Protocol Registration.
	 * @param dao
	 * @param privilegeName
	 * @param longs
	 * @param userId
	 * @param roleId
	 * @param assignToUser
	 * @throws DAOException
	 * @throws SMException
	 */
	public void assignPrivilegeToRelatedObjectsForCPR(DAO dao, String privilegeName, Long[] objectIds, Long userId, String roleId,
			boolean assignToUser, boolean assignOperation) throws SMException, DAOException
	{
		List listOfSubElement = super.getRelatedObjects(dao, CollectionProtocolRegistration.class,
				new String[]{Constants.PARTICIPANT_IDENTIFIER_IN_CPR + "." + Constants.SYSTEM_IDENTIFIER}, new String[]{Constants.SYSTEM_IDENTIFIER},
				objectIds);
		Logger.out.debug(" CPR Ids:" + edu.wustl.common.util.Utility.getArrayString(objectIds) + " Related Participant Ids:" + listOfSubElement);
		if (!listOfSubElement.isEmpty())
		{
			super.setPrivilege(dao, privilegeName, Participant.class, Utility.toLongArray(listOfSubElement), userId, roleId, assignToUser,
					assignOperation);
		}
	}

	/**
	 * Overriding the parent class's method to validate the enumerated attribute values
	 */
	protected boolean validate(Object obj, DAO dao, String operation) throws DAOException
	{
		Participant participant = (Participant) obj;
		Validator validator = new Validator();
		//Added by Ashish Gupta
	
		String message = "";		
		if (participant == null)
		{
			throw new DAOException(ApplicationProperties.getValue("domain.object.null.err.msg","Participant"));	
		}
		
		String errorKeyForBirthDate = "";
		String errorKeyForDeathDate = "";
		
		String birthDate = Utility.parseDateToString(participant.getBirthDate(),Constants.DATE_PATTERN_MM_DD_YYYY);
		if (!validator.isEmpty(birthDate))
		{
			errorKeyForBirthDate = validator.validateDate(birthDate,true);
			if (errorKeyForBirthDate.trim().length() > 0)
			{
				message = ApplicationProperties.getValue("participant.birthDate");
				throw new DAOException(ApplicationProperties.getValue(errorKeyForBirthDate,message));	
			}
		}
		
		String deathDate = Utility.parseDateToString(participant.getDeathDate(),Constants.DATE_PATTERN_MM_DD_YYYY);
		if (!validator.isEmpty(deathDate))
		{
			errorKeyForDeathDate = validator.validateDate(deathDate,true);
			if (errorKeyForDeathDate.trim().length() > 0)
			{
				message = ApplicationProperties.getValue("participant.deathDate");
				throw new DAOException(ApplicationProperties.getValue(errorKeyForDeathDate,message));	
			}
		}

		if(participant.getVitalStatus() == null || !participant.getVitalStatus().equals("Dead"))
		{
			if(!validator.isEmpty(deathDate))
			{
				throw new DAOException(ApplicationProperties.getValue("participant.invalid.enddate"));
			}
		}
		if ((!validator.isEmpty(birthDate) && !validator.isEmpty(deathDate))
				&& (errorKeyForDeathDate.trim().length() == 0 && errorKeyForBirthDate.trim()
						.length() == 0))
		{
			boolean errorKey1 = validator.compareDates(Utility.parseDateToString(participant.getBirthDate(),Constants.DATE_PATTERN_MM_DD_YYYY),
					Utility.parseDateToString(participant.getDeathDate(),Constants.DATE_PATTERN_MM_DD_YYYY));

			if (!errorKey1)
			{				
				throw new DAOException(ApplicationProperties.getValue("participant.invaliddate"));	
			}
		}
		
		if (!validator.isEmpty(participant.getSocialSecurityNumber()))
		{			
			if (!validator.isValidSSN(participant.getSocialSecurityNumber()))
			{				
				message = ApplicationProperties.getValue("participant.socialSecurityNumber");
				throw new DAOException(ApplicationProperties.getValue("errors.invalid",message));	
			}
		}
		
		Collection paticipantMedicicalCollection = participant.getParticipantMedicalIdentifierCollection();		
		if(paticipantMedicicalCollection != null && !paticipantMedicicalCollection.isEmpty())
		{
			Iterator itr = paticipantMedicicalCollection.iterator();
			while(itr.hasNext())
			{
				ParticipantMedicalIdentifier participantIdentifier = (ParticipantMedicalIdentifier) itr.next();
				Site site = participantIdentifier.getSite();
				String medicalRecordNo = participantIdentifier.getMedicalRecordNumber();
				if(validator.isEmpty(medicalRecordNo) || site == null || site.getId()== null)
		 		{		 			
					throw new DAOException(ApplicationProperties.getValue("errors.participant.extiden.missing"));	
		 		}
			}
		}
		//Validation for Blank Participant 
//		if (validator.isEmpty(participant.getLastName())
//				&& validator.isEmpty(participant.getFirstName())
//				&& validator.isEmpty(participant.getMiddleName())
//				&& validator.isEmpty(participant.getBirthDate().toString())
//				&& (validator.isEmpty(participant.getDeathDate().toString()))
//				&& !validator.isValidOption(participant.getGender())
//				&& !validator.isValidOption(participant.getVitalStatus())
//				&& !validator.isValidOption(participant.getSexGenotype())
//				&& participant.getEthnicity().equals("-1")
//				&& validator.isEmpty(socialSecurityNumberPartA + socialSecurityNumberPartB
//						+ socialSecurityNumberPartC))
//		{			
//			throw new DAOException(ApplicationProperties.getValue("errors.participant.atLeastOneFieldRequired"));
//		}

		//Validations for Add-More Block
//		 String className = "ParticipantMedicalIdentifier:";
//		 String key1 = "_Site_" + Constants.SYSTEM_IDENTIFIER;
//		 String key2 = "_medicalRecordNumber";
//		 String key3 = "_" + Constants.SYSTEM_IDENTIFIER;
//		 int index = 1;
//
//		 while(true)
//		 {
//		 String keyOne = className + index + key1;
//		 String keyTwo = className + index + key2;
//		 String keyThree = className + index + key3;
//		 
//		 String value1 = (String)values.get(keyOne);
//		 String value2 = (String)values.get(keyTwo);
//		 
//		 if(value1 == null || value2 == null)
//		 {
//		 break;
//		 }
//		 else if(!validator.isValidOption(value1) && value2.trim().equals(""))
//		 {
//		 values.remove(keyOne);
//		 values.remove(keyTwo);
//		 values.remove(keyThree);
//		 }
//		 else if((validator.isValidOption(value1) && value2.trim().equals("")) || (!validator.isValidOption(value1) && !value2.trim().equals("")))
//		 {
//		 errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.participant.missing",ApplicationProperties.getValue("participant.msg")));
//		 break;
//		 }
//		 index++;
//		 }

		// END
		
		if(!validator.isEmpty(participant.getVitalStatus()))
		{
			List vitalStatusList = CDEManager.getCDEManager().getPermissibleValueList(
				Constants.CDE_VITAL_STATUS, null);
			if (!Validator.isEnumeratedOrNullValue(vitalStatusList, participant.getVitalStatus()))
			{
				throw new DAOException(ApplicationProperties.getValue("participant.gender.errMsg"));
			}
		}
		
		if(!validator.isEmpty(participant.getGender()))
		{
			List genderList = CDEManager.getCDEManager().getPermissibleValueList(Constants.CDE_NAME_GENDER, null);
	
			if (!Validator.isEnumeratedOrNullValue(genderList, participant.getGender()))
			{
				throw new DAOException(ApplicationProperties.getValue("participant.gender.errMsg"));
			}
		}
		
		if(!validator.isEmpty(participant.getSexGenotype()))
		{
			List genotypeList = CDEManager.getCDEManager().getPermissibleValueList(Constants.CDE_NAME_GENOTYPE, null);
			if (!Validator.isEnumeratedOrNullValue(genotypeList, participant.getSexGenotype()))
			{
				throw new DAOException(ApplicationProperties.getValue("participant.genotype.errMsg"));
			}
		}		
		
		
		Collection raceCollection = participant.getRaceCollection();
		if(raceCollection!= null && !raceCollection.isEmpty())
		{
			List raceList = CDEManager.getCDEManager().getPermissibleValueList(Constants.CDE_NAME_RACE,null);
			Iterator itr = raceCollection.iterator();
			while(itr.hasNext())
			{
				String race = (String) itr.next();
				if(!validator.isEmpty(race) && !Validator.isEnumeratedOrNullValue(raceList,race))
				{
					throw new DAOException(ApplicationProperties.getValue("participant.race.errMsg"));
				}
			}			
		}        
        
		if(!validator.isEmpty(participant.getEthnicity()))
		{
			List ethnicityList = CDEManager.getCDEManager().getPermissibleValueList(Constants.CDE_NAME_ETHNICITY, null);
			if (!Validator.isEnumeratedOrNullValue(ethnicityList, participant.getEthnicity()))
			{
				throw new DAOException(ApplicationProperties.getValue("participant.ethnicity.errMsg"));
			}
		}

		if (operation.equals(Constants.ADD))
		{
			if (!Constants.ACTIVITY_STATUS_ACTIVE.equals(participant.getActivityStatus()))
			{
				throw new DAOException(ApplicationProperties.getValue("activityStatus.active.errMsg"));
			}
		}
		else
		{
			if (!Validator.isEnumeratedValue(Constants.ACTIVITY_STATUS_VALUES, participant.getActivityStatus()))
			{
				throw new DAOException(ApplicationProperties.getValue("activityStatus.errMsg"));
			}
		}

		return true;
	}

	/**
	 * This method fetches linked data from integrated application i.e. CAE/caTies.
	 */
	public List getLinkedAppData(Long id, String applicationID)
	{
		Logger.out.debug("In getIntegrationData() of ParticipantBizLogic ");

		Logger.out.debug("ApplicationName in getIntegrationData() of ParticipantBizLogic==>" + applicationID);

		long identifiedPatientId = 0;

		try
		{
			//JDBC call to get matching identifier from database
			Class.forName("org.gjt.mm.mysql.Driver");

			Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/catissuecore", "catissue_core", "catissue_core");

			Statement stmt = connection.createStatement();

			String identifiedPatientIdQuery = "select IDENTIFIER from CATISSUE_IDENTIFIED_PATIENT where PARTICIPANT_ID=" + id;

			ResultSet identifiedPatientResultSet = stmt.executeQuery(identifiedPatientIdQuery);

			while (identifiedPatientResultSet.next())
			{
				identifiedPatientId = identifiedPatientResultSet.getLong(1);
				break;
			}
			Logger.out.debug("IdentifiedPatientId==>" + identifiedPatientId);
			identifiedPatientResultSet.close();
			if (identifiedPatientId == 0)
			{
				List exception = new ArrayList();
				exception.add(new NameValueBean("IdentifiedPatientId is not available for linked Participant",
						"IdentifiedPatientId is not available for linked Participant"));
				return exception;
			}

			stmt.close();

			connection.close();

		}
		catch (Exception e)
		{
			Logger.out.debug("JDBC Exception==>" + e.getMessage());
		}

		IntegrationManager integrationManager = IntegrationManagerFactory.getIntegrationManager(applicationID);

		return integrationManager.getLinkedAppData(new Participant(), new Long(identifiedPatientId));
	}

	/**
	 * @param participant - participant object 
	 * @return - List 
	 * @throws Exception
	 */
	public List getListOfMatchingParticipants(Participant participant) throws Exception
	{
		// getting the instance of ParticipantLookupLogic class
		LookupLogic participantLookupLogic = (LookupLogic) Utility.getObject(XMLPropertyHandler.getValue(Constants.PARTICIPANT_LOOKUP_ALGO));

		// Creating the DefaultLookupParameters object to pass as argument to lookup function
		// This object contains the Participant with which matching participant are to be found and the cutoff value.
		DefaultLookupParameters params = new DefaultLookupParameters();
		params.setObject(participant);
		
		List listOfParticipants = new ArrayList();
		// getting instance of catissueCoreCacheManager and getting participantMap from cache
		CatissueCoreCacheManager catissueCoreCacheManager = CatissueCoreCacheManager.getInstance();
		HashMap participantMap = (HashMap) catissueCoreCacheManager.getObjectFromCache(Constants.MAP_OF_PARTICIPANTS);
		
		Object participants[] = participantMap.values().toArray();
		listOfParticipants.addAll(participantMap.values());

		params.setListOfParticipants(listOfParticipants);

		//calling thr lookup function which returns the List of ParticipantResuld objects.
		//ParticipantResult object contains the matching participant and the probablity.
		List matchingParticipantList = participantLookupLogic.lookup(params);

		return matchingParticipantList;

	}
	
	/**
	 * This function returns the list of all the objects present in the Participant table.
	 * Two queries are executed from this function to get participant data and race data.
	 * Participant objects are formed by retrieving only required data, making it time and space efficient 
	 * @return - List of participants 
	 */
	public Map getAllParticipants() throws Exception
	{
		// Initialising instance of IBizLogic
		IBizLogic bizLogic = BizLogicFactory.getInstance().getBizLogic(Constants.DEFAULT_BIZ_LOGIC);
		String sourceObjectName = Participant.class.getName();
		String selectColumnNames[] = {"id","lastName","firstName","middleName","birthDate","gender","sexGenotype","ethnicity","socialSecurityNumber","activityStatus","deathDate","vitalStatus"};
		String whereColumnName[] = {"activityStatus"};
		String whereColumnCondition[] = {"!="};
		Object whereColumnValue[] = {Constants.ACTIVITY_STATUS_DISABLED};
 
		// getting all the participants from the database 
		List listOfParticipants = bizLogic.retrieve(sourceObjectName,selectColumnNames,whereColumnName,whereColumnCondition,whereColumnValue,null);
		
		JDBCDAO dao = (JDBCDAO) DAOFactory.getInstance().getDAO(Constants.JDBC_DAO);
		dao.openSession(null);

		String queryStr = "SELECT * FROM CATISSUE_RACE WHERE PARTICIPANT_ID IN (SELECT PARTICIPANT_ID FROM CATISSUE_PARTICIPANT WHERE ACTIVITY_STATUS!='DISABLED')";
		List listOfRaceObjects = new ArrayList();

		try
		{
			listOfRaceObjects = dao.executeQuery(queryStr, null, false, null);
		}
		catch (Exception ex)
		{
			throw new DAOException(ex.getMessage());
		}
		dao.closeSession();
		Map mapOfRaceCollection = new HashMap();
		for (int i = 0; i < listOfRaceObjects.size(); i++)
		{
			List objectArray = (ArrayList) listOfRaceObjects.get(i);
			Long participantId = (new Long(objectArray.get(0).toString()));
			String race = (String) objectArray.get(1);
			if(mapOfRaceCollection.get(participantId)==null)
			{
				mapOfRaceCollection.put(participantId, new HashSet());
			}
			((HashSet)mapOfRaceCollection.get(participantId)).add(race);	
    	}
		
		
		
		Map mapOfParticipants = new HashMap();
		for (int i = 0; i < listOfParticipants.size(); i++)
		{
			Object [] obj = (Object[]) listOfParticipants.get(i);
			
			Long id = (Long) obj[0];
			String lastName = (String) obj[1];
			String firstName = (String) obj[2];
			String middleName = (String) obj[3];
			Date birthDate = (Date) obj[4];
			String gender = (String) obj[5];
			String sexGenotype = (String) obj[6];
			String ethnicity = (String) obj[7];
			String socialSecurityNumber = (String) obj[8];
			String activityStatus = (String) obj[9];
			Date deathDate = (Date) obj[10];
			String vitalStatus = (String) obj[11];
					
			Participant participant = new Participant(id,lastName,firstName,middleName,birthDate,gender,sexGenotype,(Collection)mapOfRaceCollection.get(id),ethnicity,socialSecurityNumber,activityStatus,deathDate,vitalStatus);
			mapOfParticipants.put(participant.getId(), participant);
		}
		return mapOfParticipants;

	}

	/**
	 * This function takes identifier as parameter and returns corresponding Participant
	 * @return - Participant object
	 */
	public Participant getParticipantById(Long identifier) throws Exception
	{
		// Initialising instance of IBizLogic
		IBizLogic bizLogic = BizLogicFactory.getInstance().getBizLogic(Constants.DEFAULT_BIZ_LOGIC);
		String sourceObjectName = Participant.class.getName();

		// getting all the participants from the database 
		List participantList = bizLogic.retrieve(sourceObjectName, Constants.ID, identifier);
		Participant participant = (Participant) participantList.get(0);
		return participant;

	}

	public List getColumnList(List columnList) throws DAOException
	{
		List displayList = new ArrayList();
		try
		{
			JDBCDAO jdbcDao = (JDBCDAO) DAOFactory.getInstance().getDAO(Constants.JDBC_DAO);
			jdbcDao.openSession(null);
			String sql = "SELECT  columnData.COLUMN_NAME,displayData.DISPLAY_NAME FROM "
					+ "CATISSUE_INTERFACE_COLUMN_DATA columnData,CATISSUE_TABLE_RELATION relationData,"
					+ "CATISSUE_QUERY_TABLE_DATA tableData,CATISSUE_SEARCH_DISPLAY_DATA displayData "
					+ "where relationData.CHILD_TABLE_ID = columnData.TABLE_ID and " + "relationData.PARENT_TABLE_ID = tableData.TABLE_ID and "
					+ "relationData.RELATIONSHIP_ID = displayData.RELATIONSHIP_ID and "
					+ "columnData.IDENTIFIER = displayData.COL_ID and tableData.ALIAS_NAME = 'Participant'";

			Logger.out.debug("DATA ELEMENT SQL : " + sql);
			List list = jdbcDao.executeQuery(sql, null, false, null);
			Iterator iterator1 = columnList.iterator();

			while (iterator1.hasNext())
			{
				String colName1 = (String) iterator1.next();
				Logger.out.debug("colName1------------------------" + colName1);
				Iterator iterator2 = list.iterator();
				while (iterator2.hasNext())
				{
					List rowList = (List) iterator2.next();
					String colName2 = (String) rowList.get(0);
					Logger.out.debug("colName2------------------------" + colName2);
					if (colName1.equals(colName2))
					{
						displayList.add((String) rowList.get(1));
					}
				}
			}
			jdbcDao.closeSession();
		}
		catch (ClassNotFoundException classExp)
		{
			throw new DAOException(classExp.getMessage(), classExp);
		}

		return displayList;
	}

	public String getPageToShow()
	{
		return new String();
	}

	public List getMatchingObjects()
	{
		return new ArrayList();
	}
		
}