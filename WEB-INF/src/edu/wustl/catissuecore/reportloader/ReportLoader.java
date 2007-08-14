package edu.wustl.catissuecore.reportloader;

import java.sql.Clob;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import edu.wustl.catissuecore.bizlogic.BizLogicFactory;
import edu.wustl.catissuecore.bizlogic.CollectionProtocolBizLogic;
import edu.wustl.catissuecore.bizlogic.CollectionProtocolRegistrationBizLogic;
import edu.wustl.catissuecore.bizlogic.SpecimenCollectionGroupBizLogic;
import edu.wustl.catissuecore.caties.util.CaCoreAPIService;
import edu.wustl.catissuecore.caties.util.CaTIESConstants;
import edu.wustl.catissuecore.caties.util.CaTIESProperties;
import edu.wustl.catissuecore.caties.util.Utility;
import edu.wustl.catissuecore.domain.CollectionEventParameters;
import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.catissuecore.domain.CollectionProtocolEvent;
import edu.wustl.catissuecore.domain.CollectionProtocolRegistration;
import edu.wustl.catissuecore.domain.Participant;
import edu.wustl.catissuecore.domain.ReceivedEventParameters;
import edu.wustl.catissuecore.domain.Site;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.catissuecore.domain.SpecimenEventParameters;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.domain.pathology.IdentifiedSurgicalPathologyReport;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.global.DefaultValueManager;
import edu.wustl.common.bizlogic.DefaultBizLogic;
import edu.wustl.common.util.dbManager.DAOException;
import edu.wustl.common.util.logger.Logger;

/**
 * @author sandeep_ranade
 * Represents a data loader which loades the report data into datastore. 
 */

public class ReportLoader 
{

	/**
	 * Participant of the report
	 */
	private Participant participant;
	
	/**
	 *  Source of the report
	 */
	private Site site;
	
	/**
	 * Identified pathology report 
	 */
	private IdentifiedSurgicalPathologyReport identifiedReport;
	
	/**
	 * Specimen Collection Group
	 */
	private SpecimenCollectionGroup scg;
	
	/**
	 * Surgical Pathology Number for Specimen Collection Group
	 */
	private String surgicalPathologyNumber;
	
	/**
	 * Constructor
	 * @param p participant 
	 * @param report identified report
	 * @param s site
	 * 
	 */
	public ReportLoader(Participant p,IdentifiedSurgicalPathologyReport report,Site s)
	{
		this.participant=p;
		this.identifiedReport=report;
		this.site=s;
	}
	/**
	 * Constructor
	 * @param p participant 
	 * @param report identified report
	 * @param s site
	 * @param scg specimenCollectionGroup
	 * @param surgicalPathologyNumber surgicalPathologyNumber of report
	 * @throws DAOException 
	 * 
	 */
	public ReportLoader(Participant participant,IdentifiedSurgicalPathologyReport report,Site s, SpecimenCollectionGroup scg, String surgicalPathologyNumber) throws DAOException
	{
		this.participant=participant;
		this.identifiedReport=report;
		this.site=s;
		this.scg=scg;
		this.surgicalPathologyNumber=surgicalPathologyNumber;
	}
			
	/**
	 * processes the report data. Validats for participant,site and and report
	 * existance. It also stores data to the datastore.
	 * @throws Exception generic exception
	 *    
	 */
	public void process()throws Exception
	{
		
		Logger.out.debug("Processing Report ");
		identifiedReport.setReportStatus(CaTIESConstants.PENDING_FOR_DEID);
		identifiedReport.setReportSource(this.site);
		try
		{	
			// check for existing specimen collection group(scg)
			if(this.scg==null || this.scg.getId()==0)
			{
				// if scg is null create new scg
				Logger.out.debug("Null SCG found in ReportLoader, Creating New SCG");
				this.scg=createNewSpecimenCollectionGroup();
				Utility.saveObject(this.scg);
			}
			else
			{
				// use existing scg
				if(this.scg.getIdentifiedSurgicalPathologyReport()!=null)
				{
					boolean isReportExists = checkForTextContent(this.scg.getIdentifiedSurgicalPathologyReport());
					if(!isReportExists)
					{
						// if report text is null then set report text
						this.scg.getIdentifiedSurgicalPathologyReport().setTextContent(this.identifiedReport.getTextContent());
					}	
				}
				else
				{
					this.scg.setIdentifiedSurgicalPathologyReport(this.identifiedReport); 
				}
				if(this.scg.getSurgicalPathologyNumber()==null)
				{
					this.scg.setSurgicalPathologyNumber(this.surgicalPathologyNumber);
				}
				Utility.updateObject(identifiedReport);
			}	
			Logger.out.info("Processing finished for Report ");
		}
		catch(Exception ex)
		{
			Logger.out.error("Failed to process report "+ex);
			throw ex;
		}
	}

	/**
	 * Method to create new SCG
	 * @return scg object of SpecimenColectionGroup 
	 * @param participant Participant object
	 * @param identifiedReport object of IdentifiedSurgicalPathologyReport
	 * @param surgicalPathologyNumber String containing surgicalPathologyNumber for scg
	 * @throws Exception generic exception
	 */
	private SpecimenCollectionGroup createNewSpecimenCollectionGroup()throws Exception
	{
		// set default values for scg
		Logger.out.info("Creating New Specimen Collection Group");
		SpecimenCollectionGroup scg = new SpecimenCollectionGroup();
		scg.setActivityStatus(Constants.ACTIVITY_STATUS_ACTIVE);
		//SCG Particpant direct relationship is removed
//		scg.setParticipant(this.participant);
		scg.setClinicalDiagnosis(Constants.NOT_SPECIFIED);
		scg.setClinicalStatus(Constants.NOT_SPECIFIED);
		scg.setSpecimenCollectionSite(site); 
		scg.setSurgicalPathologyNumber(surgicalPathologyNumber);
		scg.setIdentifiedSurgicalPathologyReport(identifiedReport);
		identifiedReport.setSpecimenCollectionGroup(scg);
			 
		// Retrieve collection generic protocol
		String sourceObjectName=CollectionProtocol.class.getName();
		String[] selectColumnName=new String[]{Constants.SYSTEM_IDENTIFIER};
		String[] whereColumnName=new String[]{Constants.COLUMN_NAME_TITLE};
		String[] whereColumnValue=new String[]{CaTIESProperties.getValue(CaTIESConstants.COLLECTION_PROTOCOL_TITLE)};
		String[] whereColumnCondition=new String[]{"="};
		String joinCondition="";
		BizLogicFactory bizLogicFactory=BizLogicFactory.getInstance();
		CollectionProtocolBizLogic cpBizLogic=(CollectionProtocolBizLogic)bizLogicFactory.getBizLogic(CollectionProtocol.class.getName());
		List cpIDList=cpBizLogic.retrieve(sourceObjectName, selectColumnName, whereColumnName, whereColumnCondition, whereColumnValue, joinCondition);
		Long cpID=null;
		if(cpIDList!=null && cpIDList.size()>0)
		{
			cpID=(Long)cpIDList.get(0);
		}
		else
		{
			throw new Exception("CP not found with specified Titile in DB");
		}
		
		//Autogeneration of SCG name
		// SPR_<CollectionProtocol_Title>_<Participant_ID>_<Group_ID>
		int groupId=0;
		SpecimenCollectionGroupBizLogic scgBizLogic=(SpecimenCollectionGroupBizLogic)bizLogicFactory.getBizLogic(SpecimenCollectionGroup.class.getName());
		groupId=scgBizLogic.getNextGroupNumber();
		String collProtocolTitle=CaTIESProperties.getValue(CaTIESConstants.COLLECTION_PROTOCOL_TITLE);
		if(collProtocolTitle.length()>30)
		{
			collProtocolTitle=collProtocolTitle.substring(0,29);
		}
		String scgName="SPR_"+collProtocolTitle+"_"+participant.getId()+"_"+groupId; //this.identifiedReport.getAccessionNumber().toString());
		scg.setName(scgName);
		Logger.out.info("SCG name is =====>"+scgName);
		
		// retrieve collection protocol event list
		DefaultBizLogic defaultBizLogic = new DefaultBizLogic();
		Collection collProtocolEventList=(Collection)defaultBizLogic.retrieveAttribute(CollectionProtocol.class.getName(), cpID, Constants.COLUMN_COLL_PROT_EVENT_COLL);
		Iterator cpEventIterator=collProtocolEventList.iterator();
		
		if(cpEventIterator.hasNext())
		{		
			CollectionProtocolEvent collProtocolEvent=(CollectionProtocolEvent)cpEventIterator.next();
			scg.setCollectionProtocolEvent(collProtocolEvent);
			
			// check for existing CollectionProtocolRegistration, if exists then use existing
			CollectionProtocolRegistration collProtocolReg=isCPRExists(participant.getId(), cpID);
			if(collProtocolReg==null)
			{	
				// otherwise create new CollectionProtocolRegistration
				Logger.out.info("Creating New CollectionProtocolRegistration object");
				collProtocolReg=new CollectionProtocolRegistration();
				collProtocolReg.setActivityStatus(Constants.ACTIVITY_STATUS_ACTIVE);
				collProtocolReg.setRegistrationDate(new Date());
				collProtocolReg.setParticipant(participant);	
				collProtocolReg.setCollectionProtocol(collProtocolEvent.getCollectionProtocol());
				Collection<CollectionProtocolRegistration> collProtocolRegList=(Collection<CollectionProtocolRegistration>)defaultBizLogic.retrieveAttribute(Participant.class.getName(), participant.getId(), Constants.COLUMN_NAME_CPR_COLL);
				collProtocolRegList.add(collProtocolReg);
//				participant.setCollectionProtocolRegistrationCollection(collProtocolRegList);
				try
				{
					CaCoreAPIService.getAppServiceInstance().createObject(collProtocolReg);
				}
				catch(Exception ex)
				{
					Logger.out.error("Error: Could not save object of CollectionProtocolRegistration",ex);
					throw new Exception("Could not save object of CollectionProtocolRegistration :"+ex.getMessage());
				}		
			}
			scg.setCollectionProtocolRegistration(collProtocolReg);
			scg.setSpecimenEventParametersCollection(getDefaultEvents(scg));
		}
		else
		{
			Logger.out.error("Associated Collection Protocol Event not found for "+ collProtocolTitle);
			throw new Exception("Associated Collection Protocol Event not found for "+ collProtocolTitle);
		}
			
		return scg;
	}	
	
	/**
	 * Method to check for existing collectionProtocolRegistration
	 * @param participant object of Participant
	 * @param collectionProtocol object of CollectionProtocol
	 * @return object of CollectionProtocolRegistration
	 * @throws Exception generic exception
	 */
	private CollectionProtocolRegistration isCPRExists(Long participantID, Long cpID) throws Exception
	{
		try
		{
			// retrive CollectionProtocolRegistration with current participant and collectionProtocol
			String sourceObjectName=new String(CollectionProtocolRegistration.class.getName());
			String[] selectColumnName=new String[]{"participant", "collectionProtocol"};
			String[] whereColumnCondition=new String[]{"=","="};
			String[] whereColumnValue=new String[]{participantID.toString(),cpID.toString()};
			String joinCondition=Constants.AND_JOIN_CONDITION;
			
			BizLogicFactory bizLogicFactory=BizLogicFactory.getInstance();
			CollectionProtocolRegistrationBizLogic collProtRegBizLogic=(CollectionProtocolRegistrationBizLogic)bizLogicFactory.getBizLogic(CollectionProtocolRegistration.class.getName());
			List cprList=collProtRegBizLogic.retrieve(sourceObjectName, selectColumnName, whereColumnCondition, whereColumnValue,joinCondition);
			
			// check for existence
			if(cprList!=null && cprList.size()>0)
			{
				// cpr exist then return existing cpr
				Logger.out.info("Existing CPR found for participant id="+participantID+" collectionProtocol id="+cpID);
				return (CollectionProtocolRegistration)cprList.get(0);
			}
		}
		catch(DAOException ex)
		{
			Logger.out.error("DAOException occured in isCPRExists method");
			Logger.out.debug(ex.getMessage());
		}
		return null;
	}
	
	/**
	 * Method to check for the existence of textContent
	 * @param report identified surgical pathology report
	 * @return boolean value which indicates text content is present or not
	 */
	private boolean checkForTextContent(IdentifiedSurgicalPathologyReport report)throws Exception
	{
		Logger.out.info("Inside checkForTextContent function");
		Clob tempClob=report.getTextContent().getData();
		String textContent=tempClob.getSubString(1,(int) tempClob.length());
		// check for null report text content
		if(textContent!=null && textContent.length()>0)
		{
			return true;
		}
		return false;
	}	
	
	private Collection<SpecimenEventParameters> getDefaultEvents(SpecimenCollectionGroup specimenCollectionGroup) throws DAOException
	{
		Collection<SpecimenEventParameters> defaultEventCollection=new HashSet<SpecimenEventParameters>();
		DefaultBizLogic defaultBizLogic=new DefaultBizLogic();
		String loginName=CaTIESProperties.getValue(CaTIESConstants.SESSION_DATA);
		List userList=defaultBizLogic.retrieve(User.class.getName(), Constants.LOGINNAME, loginName);
		User user=(User)userList.get(0);
		CollectionEventParameters collectionEvent=new CollectionEventParameters();
		collectionEvent.setCollectionProcedure((String)DefaultValueManager.getDefaultValue(Constants.DEFAULT_COLLECTION_PROCEDURE));
		collectionEvent.setContainer((String)DefaultValueManager.getDefaultValue(Constants.DEFAULT_CONTAINER));
		collectionEvent.setSpecimenCollectionGroup(specimenCollectionGroup);
		collectionEvent.setTimestamp(new Date());
		collectionEvent.setUser(user);
		
		ReceivedEventParameters recievedEvent=new ReceivedEventParameters();
		recievedEvent.setReceivedQuality((String)DefaultValueManager.getDefaultValue(Constants.DEFAULT_RECEIVED_QUALITY));
		recievedEvent.setSpecimenCollectionGroup(specimenCollectionGroup);
		recievedEvent.setTimestamp(new Date());
		recievedEvent.setUser(user);
		
		defaultEventCollection.add(collectionEvent);
		defaultEventCollection.add(recievedEvent);
		return defaultEventCollection;
	}
}