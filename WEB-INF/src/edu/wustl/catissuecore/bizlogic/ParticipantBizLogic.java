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
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import edu.wustl.catissuecore.domain.CollectionProtocolRegistration;
import edu.wustl.catissuecore.domain.Participant;
import edu.wustl.catissuecore.domain.ParticipantMedicalIdentifier;
import edu.wustl.catissuecore.integration.IntegrationManager;
import edu.wustl.catissuecore.integration.IntegrationManagerFactory;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.global.Utility;
import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.cde.CDEManager;
import edu.wustl.common.dao.DAO;
import edu.wustl.common.dao.JDBCDAO;
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
		Participant participant = (Participant)obj;
        
		dao.insert(participant,sessionDataBean, true, true);
		
		Collection participantMedicalIdentifierCollection = participant.getParticipantMedicalIdentifierCollection();
		
		if(participantMedicalIdentifierCollection.isEmpty())
		{
			//add a dummy participant MedicalIdentifier for Query.
			ParticipantMedicalIdentifier participantMedicalIdentifier = new ParticipantMedicalIdentifier();
			participantMedicalIdentifier.setMedicalRecordNumber(null);
			participantMedicalIdentifier.setSite(null);
			participantMedicalIdentifierCollection.add(participantMedicalIdentifier);
		}
		
		//Inserting medical identifiers in the database after setting the participant associated.
		Iterator it = participantMedicalIdentifierCollection.iterator();
		while(it.hasNext())
		{
			ParticipantMedicalIdentifier pmIdentifier = (ParticipantMedicalIdentifier)it.next();
			pmIdentifier.setParticipant(participant);
			dao.insert(pmIdentifier,sessionDataBean, true, true);
		}
		
		Set protectionObjects=new HashSet();
        protectionObjects.add(participant);
        try
        {
            SecurityManager.getInstance(this.getClass()).insertAuthorizationData(
            		null, protectionObjects, null);
        }
        catch (SMException e)
        {
        	throw handleSMException(e);
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
		Participant participant = (Participant)obj;
		Participant oldParticipant = (Participant) oldObj;
		
		dao.update(participant, sessionDataBean, true, true, false);
		
		//Audit of Participant.
		dao.audit(obj, oldObj, sessionDataBean, true);
		
		Collection oldParticipantMedicalIdentifierCollection = (Collection)oldParticipant.getParticipantMedicalIdentifierCollection();
		
		Collection participantMedicalIdentifierCollection = participant.getParticipantMedicalIdentifierCollection();
		Iterator it = participantMedicalIdentifierCollection.iterator();
		
		//Updating the medical identifiers of the participant
		while(it.hasNext())
		{
			ParticipantMedicalIdentifier pmIdentifier = (ParticipantMedicalIdentifier)it.next();
			pmIdentifier.setParticipant(participant);
			dao.update(pmIdentifier, sessionDataBean, true, true, false);
			
			//Audit of ParticipantMedicalIdentifier.
			ParticipantMedicalIdentifier oldPmIdentifier 
					= (ParticipantMedicalIdentifier)getCorrespondingOldObject(oldParticipantMedicalIdentifierCollection, 
					        pmIdentifier.getSystemIdentifier());
			
			dao.audit(pmIdentifier, oldPmIdentifier, sessionDataBean, true);
		}
		
		//Disable the associate collection protocol registration
		Logger.out.debug("participant.getActivityStatus() "+participant.getActivityStatus());
		if(participant.getActivityStatus().equals(Constants.ACTIVITY_STATUS_DISABLED))
		{
			Logger.out.debug("participant.getActivityStatus() "+participant.getActivityStatus());
			Long participantIDArr[] = {participant.getSystemIdentifier()};
			
			CollectionProtocolRegistrationBizLogic bizLogic = (CollectionProtocolRegistrationBizLogic)BizLogicFactory.getInstance().getBizLogic(Constants.COLLECTION_PROTOCOL_REGISTRATION_FORM_ID);
			bizLogic.disableRelatedObjectsForParticipant(dao,participantIDArr);
		}
    }
	
	/**
	 * @see edu.wustl.common.bizlogic.IBizLogic#setPrivilege(DAO, String, Class, Long[], Long, String, boolean)
	 */
	protected void setPrivilege(DAO dao, String privilegeName, Class objectType, Long[] objectIds, Long userId, String roleId, boolean assignToUser, boolean assignOperation) throws SMException, DAOException
    {
	    Logger.out.debug(" privilegeName:"+privilegeName+" objectType:"+objectType+" objectIds:"+edu.wustl.common.util.Utility.getArrayString(objectIds)+" userId:"+userId+" roleId:"+roleId+" assignToUser:"+assignToUser);
	    super.setPrivilege(dao,privilegeName,objectType,objectIds,userId, roleId, assignToUser, assignOperation);
	    
	    CollectionProtocolRegistrationBizLogic bizLogic = (CollectionProtocolRegistrationBizLogic)BizLogicFactory.getInstance().getBizLogic(Constants.COLLECTION_PROTOCOL_REGISTRATION_FORM_ID);
		bizLogic.assignPrivilegeToRelatedObjectsForParticipant(dao,privilegeName,objectIds,userId,roleId,assignToUser, assignOperation);
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
	public void assignPrivilegeToRelatedObjectsForCPR(DAO dao, String privilegeName, Long[] objectIds, Long userId, String roleId, boolean assignToUser, boolean assignOperation) throws SMException, DAOException {
		 List listOfSubElement = super.getRelatedObjects(dao, CollectionProtocolRegistration.class, new String[] {Constants.PARTICIPANT_IDENTIFIER_IN_CPR+"."+Constants.SYSTEM_IDENTIFIER}, new String[] {Constants.SYSTEM_IDENTIFIER}, 
   			 objectIds);
    Logger.out.debug(" CPR Ids:"+edu.wustl.common.util.Utility.getArrayString(objectIds)+" Related Participant Ids:"+listOfSubElement);  
   	if(!listOfSubElement.isEmpty())
   	{
   	    super.setPrivilege(dao,privilegeName,Participant.class,Utility.toLongArray(listOfSubElement),userId, roleId, assignToUser, assignOperation);
   	}
	}
	
	/**
     * Overriding the parent class's method to validate the enumerated attribute values
     */
	protected boolean validate(Object obj, DAO dao, String operation) throws DAOException
    {
		Participant participant = (Participant)obj;
		
        List genderList = CDEManager.getCDEManager().getPermissibleValueList(Constants.CDE_NAME_GENDER,null);
        
        if(!Validator.isEnumeratedOrNullValue(genderList,participant.getGender()))
		{
			throw new DAOException(ApplicationProperties.getValue("participant.gender.errMsg"));
		}

//        NameValueBean unknownVal = new NameValueBean(Constants.UNKNOWN,Constants.UNKNOWN);
        List genotypeList = CDEManager.getCDEManager().getPermissibleValueList(Constants.CDE_NAME_GENOTYPE,null);
        if(!Validator.isEnumeratedOrNullValue(genotypeList,participant.getSexGenotype()))
		{
			throw new DAOException(ApplicationProperties.getValue("participant.genotype.errMsg"));
		}
        
        List raceList = CDEManager.getCDEManager().getPermissibleValueList(Constants.CDE_NAME_RACE,null);
        if(!Validator.isEnumeratedOrNullValue(raceList,participant.getRace()))
		{
			throw new DAOException(ApplicationProperties.getValue("participant.race.errMsg"));
		}

        List ethnicityList = CDEManager.getCDEManager().getPermissibleValueList(Constants.CDE_NAME_ETHNICITY,null);
        if(!Validator.isEnumeratedOrNullValue(ethnicityList,participant.getEthnicity()))
		{
			throw new DAOException(ApplicationProperties.getValue("participant.ethnicity.errMsg"));
		}
     
        if(operation.equals(Constants.ADD))
		{
			if(!Constants.ACTIVITY_STATUS_ACTIVE.equals(participant.getActivityStatus()))
			{
				throw new DAOException(ApplicationProperties.getValue("activityStatus.active.errMsg"));
			}
		}
		else
		{
			if(!Validator.isEnumeratedValue(Constants.ACTIVITY_STATUS_VALUES,participant.getActivityStatus()))
			{
				throw new DAOException(ApplicationProperties.getValue("activityStatus.errMsg"));
			}
		}
        
		return true;
    }
	
	/**
     * This method fetches linked data from integrated application i.e. CAE/caTies.
     */
	public List getLinkedAppData(Long systemIdentifier, String applicationID)
	{
	    Logger.out.debug("In getIntegrationData() of ParticipantBizLogic ");
	    
	    Logger.out.debug("ApplicationName in getIntegrationData() of ParticipantBizLogic==>"+applicationID);
	    
	    long identifiedPatientId = 0;
	    
	    try
	    {
		    //JDBC call to get matching identifier from database
		    Class.forName("org.gjt.mm.mysql.Driver");
		    
		    Connection connection=DriverManager.getConnection("jdbc:mysql://localhost:3306/catissuecore","catissue_core","catissue_core");
		    
		    Statement stmt= connection.createStatement();
		    
		    String identifiedPatientIdQuery = "select IDENTIFIER from CATISSUE_IDENTIFIED_PATIENT where PARTICIPANT_ID="+systemIdentifier;
		    
		    ResultSet identifiedPatientResultSet=stmt.executeQuery(identifiedPatientIdQuery);
		    
		    while(identifiedPatientResultSet.next())
		    {
		        identifiedPatientId = identifiedPatientResultSet.getLong(1);
		        break;
		    }
		    Logger.out.debug("IdentifiedPatientId==>"+identifiedPatientId);
		    identifiedPatientResultSet.close();
		    if(identifiedPatientId==0)
		    {
		        List exception=new ArrayList();
		        exception.add(new NameValueBean("IdentifiedPatientId is not available for linked Participant", "IdentifiedPatientId is not available for linked Participant"));
		        return exception;
		    }
		    
		    stmt.close();
		    
		    connection.close();
		    
	    }
	    catch(Exception e)
	    {
	        Logger.out.debug("JDBC Exception==>"+e.getMessage());
	    }
	    
	    IntegrationManager integrationManager=IntegrationManagerFactory.getIntegrationManager(applicationID);

	    return integrationManager.getLinkedAppData(new Participant(), new Long(identifiedPatientId));
	}
	
	public List getParticipantLookupData(Participant participant) throws Exception
	{
//		getting the instance of ParticipantLookupLogic class
		LookupLogic participantLookupLogic = (LookupLogic) Utility
				.getObject(XMLPropertyHandler.getValue(Constants.PARTICIPANT_LOOKUP_ALGO));

		//getting the cutoff value for participant lookup matching algo
		Double cutoff = new Double(XMLPropertyHandler.getValue(Constants.PARTICIPANT_LOOKUP_CUTOFF));
		
		//Creating the DefaultLookupParameters object to pass as argument to lookup function
		//This object contains the Participant with which matching participant are to be found and the cutoff value.
		DefaultLookupParameters params=new DefaultLookupParameters();
		params.setObject(participant);
		params.setCutoff(cutoff);
		//calling thr lookup function which returns the List of ParticipantResuld objects.
		//ParticipantResult object contains the matching participant and the probablity.
		List participantList = participantLookupLogic.lookup(params);

		return participantList;
		
	}
	public List getColumnList(List columnList) throws DAOException
	{
		List displayList=new ArrayList();
		 try
		    {
			    JDBCDAO jdbcDao = new JDBCDAO();
		        jdbcDao.openSession(null);
		        String sql="SELECT  columnData.COLUMN_NAME,displayData.DISPLAY_NAME FROM "+
		        			"CATISSUE_INTERFACE_COLUMN_DATA columnData,CATISSUE_TABLE_RELATION relationData,"+
		        			"CATISSUE_QUERY_TABLE_DATA tableData,CATISSUE_SEARCH_DISPLAY_DATA displayData "+
		        			"where relationData.CHILD_TABLE_ID = columnData.TABLE_ID and "+
		        			"relationData.PARENT_TABLE_ID = tableData.TABLE_ID and "+
		        			"relationData.RELATIONSHIP_ID = displayData.RELATIONSHIP_ID and "+
		        			"columnData.IDENTIFIER = displayData.COL_ID and tableData.ALIAS_NAME = 'Participant'";
		       
		       Logger.out.debug("DATA ELEMENT SQL : "+sql);
		       List list = jdbcDao.executeQuery(sql, null, false, null);
		       Iterator iterator1=columnList.iterator();
		       
		       while(iterator1.hasNext())
		       {
		    	   String colName1=(String)iterator1.next();
		    	   Logger.out.debug("colName1------------------------"+colName1);
		    	   Iterator iterator2 = list.iterator();
		    	   while(iterator2.hasNext())
				    {
				        List rowList = (List) iterator2.next();
				        String colName2=(String)rowList.get(0);
				        Logger.out.debug("colName2------------------------"+colName2);
				        if(colName1.equals(colName2))
				        {
				        	displayList.add((String)rowList.get(1));
				        }
				    }
		        }
			    jdbcDao.closeSession();
		    }
		    catch(ClassNotFoundException classExp)
		    {
		        throw new DAOException(classExp.getMessage(),classExp);
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