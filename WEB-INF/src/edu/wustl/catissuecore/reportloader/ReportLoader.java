package edu.wustl.catissuecore.reportloader;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import edu.wustl.catissuecore.caties.util.CaCoreAPIService;
import edu.wustl.catissuecore.caties.util.CaTIESConstants;
import edu.wustl.catissuecore.caties.util.CaTIESProperties;
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
import edu.wustl.catissuecore.domain.pathology.DeidentifiedSurgicalPathologyReport;
import edu.wustl.catissuecore.domain.pathology.IdentifiedSurgicalPathologyReport;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.util.dbManager.DAOException;
import edu.wustl.common.util.logger.Logger;
import gov.nih.nci.system.applicationservice.ApplicationException;

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
				CaCoreAPIService.getAppServiceInstance().createObject(this.scg);
			}
			else
			{
				// use existing scg
				retrieveAndSetSCG();
				CaCoreAPIService.getAppServiceInstance().createObject(this.identifiedReport);
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
		scg.setClinicalDiagnosis(Constants.NOT_SPECIFIED);
		scg.setClinicalStatus(Constants.NOT_SPECIFIED);
		scg.setSpecimenCollectionSite(site); 
		scg.setSurgicalPathologyNumber(surgicalPathologyNumber);
		scg.setIdentifiedSurgicalPathologyReport(identifiedReport);
		identifiedReport.setSpecimenCollectionGroup(scg);
			 
		// Retrieve collection generic protocol
		CollectionProtocol collectionProtocol = (CollectionProtocol)CaCoreAPIService.getObject(CollectionProtocol.class, Constants.COLUMN_NAME_TITLE, CaTIESProperties.getValue(CaTIESConstants.COLLECTION_PROTOCOL_TITLE));
		if(collectionProtocol==null)
		{
			throw new Exception(CaTIESConstants.CP_NOT_FOUND_ERROR_MSG);
		}
		
		//Autogeneration of SCG name
		// SPR_<CollectionProtocol_Title>_<Participant_ID>_<Group_ID>
		long groupId=0;
		groupId=CaCoreAPIService.getAppServiceInstance().getSpecimenCollectionGroupLabel();
		
		String collProtocolTitle=CaTIESProperties.getValue(CaTIESConstants.COLLECTION_PROTOCOL_TITLE);
		if(collProtocolTitle.length()>30)
		{
			collProtocolTitle=collProtocolTitle.substring(0,29);
		}
		String scgName="SPR_"+collProtocolTitle+"_"+participant.getId()+"_"+groupId; //this.identifiedReport.getAccessionNumber().toString());
		scg.setName(scgName);
		Logger.out.info("SCG name is =====>"+scgName);
		
		// retrieve collection protocol event list
		Collection collProtocolEventList=(Collection)CaCoreAPIService.getList(CollectionProtocolEvent.class, "collectionProtocol", collectionProtocol);
		Iterator cpEventIterator=collProtocolEventList.iterator();
		
		if(cpEventIterator.hasNext())
		{		
			CollectionProtocolEvent collProtocolEvent=(CollectionProtocolEvent)cpEventIterator.next();
			scg.setCollectionProtocolEvent(collProtocolEvent);
			
			// check for existing CollectionProtocolRegistration, if exists then use existing
			CollectionProtocolRegistration collProtocolReg=getExistingCPR(participant, collectionProtocol);
			if(collProtocolReg==null)
			{	
				// otherwise create new CollectionProtocolRegistration
				Logger.out.info("Creating New CollectionProtocolRegistration object");
				collProtocolReg=new CollectionProtocolRegistration();
				collProtocolReg.setActivityStatus(Constants.ACTIVITY_STATUS_ACTIVE);
				collProtocolReg.setRegistrationDate(new Date());
				collProtocolReg.setParticipant(participant);	
				collProtocolReg.setCollectionProtocol(collProtocolEvent.getCollectionProtocol());
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
	private CollectionProtocolRegistration getExistingCPR(Participant participant, CollectionProtocol cp) throws Exception
	{
		List cprList=null;
		
		// retrive CollectionProtocolRegistration with current participant and collectionProtocol
		DetachedCriteria criteria = DetachedCriteria.forClass(CollectionProtocolRegistration.class);
		criteria.add(Restrictions.eq("participant", participant));
		criteria.add(Restrictions.eq("collectionProtocol", cp));
		try 
		{
			cprList = CaCoreAPIService.getAppServiceInstance().query(criteria, CollectionProtocolRegistration.class.getName());
			// check for existence
			if(cprList!=null && cprList.size()>0)
			{
				// cpr exist then return existing cpr
				Logger.out.info("Existing CPR found for participant id="+participant.getId()+" collectionProtocol id="+cp.getId());
				return (CollectionProtocolRegistration)cprList.get(0);
			}
		} 
		catch (ApplicationException appEx) 
		{
			Logger.out.error("Error while retrieving List for "+ CollectionProtocolRegistration.class.getName()+appEx);
		}
		Logger.out.info("No Existing CPR found for participant id="+participant.getId()+" collectionProtocol id="+cp.getId());
		return null;
	}
	
	private Collection<SpecimenEventParameters> getDefaultEvents(SpecimenCollectionGroup specimenCollectionGroup) throws DAOException, ApplicationException
	{
		Collection<SpecimenEventParameters> defaultEventCollection=new HashSet<SpecimenEventParameters>();
		String loginName=CaTIESProperties.getValue(CaTIESConstants.USER_NAME);
		User user=(User)CaCoreAPIService.getObject(User.class, Constants.LOGINNAME, loginName);
		
		CollectionEventParameters collectionEvent=new CollectionEventParameters();
		collectionEvent.setCollectionProcedure((String)CaCoreAPIService.getAppServiceInstance().getDefaultValue(Constants.DEFAULT_COLLECTION_PROCEDURE));
		collectionEvent.setContainer((String)CaCoreAPIService.getAppServiceInstance().getDefaultValue(Constants.DEFAULT_CONTAINER));
		collectionEvent.setSpecimenCollectionGroup(specimenCollectionGroup);
		collectionEvent.setTimestamp(new Date());
		collectionEvent.setUser(user);
		
		ReceivedEventParameters recievedEvent=new ReceivedEventParameters();
		recievedEvent.setReceivedQuality((String)CaCoreAPIService.getAppServiceInstance().getDefaultValue(Constants.DEFAULT_RECEIVED_QUALITY));
		recievedEvent.setSpecimenCollectionGroup(specimenCollectionGroup);
		recievedEvent.setTimestamp(new Date());
		recievedEvent.setUser(user);
		
		defaultEventCollection.add(collectionEvent);
		defaultEventCollection.add(recievedEvent);
		return defaultEventCollection;
	}
	
	private void retrieveAndSetSCG() throws Exception
	{
		
		// set identified report
		if(this.scg.getIdentifiedSurgicalPathologyReport()!=null  && this.scg.getIdentifiedSurgicalPathologyReport().getId()!=null)
		{
			Logger.out.info("inside"+this.scg.getIdentifiedSurgicalPathologyReport().getId());
			IdentifiedSurgicalPathologyReport existingReport=scg.getIdentifiedSurgicalPathologyReport();
		
			existingReport.setSpecimenCollectionGroup(null);
			this.scg.setIdentifiedSurgicalPathologyReport(null);
			this.scg.setDeIdentifiedSurgicalPathologyReport(null);
			try 
			{
				existingReport=(IdentifiedSurgicalPathologyReport)CaCoreAPIService.getAppServiceInstance().updateObject(existingReport);
				Logger.out.info("existingReport updated:"+existingReport.getId());
				if(existingReport.getDeIdentifiedSurgicalPathologyReport()!=null)
				{
					existingReport.getDeIdentifiedSurgicalPathologyReport().setSpecimenCollectionGroup(null);
					DeidentifiedSurgicalPathologyReport deidreport=(DeidentifiedSurgicalPathologyReport)CaCoreAPIService.getAppServiceInstance().updateObject(existingReport.getDeIdentifiedSurgicalPathologyReport());
					Logger.out.info("deid report updated: "+deidreport.getId());
				}
			}
			catch (ApplicationException ex) 
			{
				Logger.out.error("Error while updating old report!",ex);
				throw new Exception(ex.getMessage());
			}
			/*}
			else
			{
				this.scg.setIdentifiedSurgicalPathologyReport(this.identifiedReport);
				this.identifiedReport.setSpecimenCollectionGroup(this.scg);
			}*/
		}
		
			Logger.out.info("inside else");
			this.scg.setIdentifiedSurgicalPathologyReport(this.identifiedReport);
			this.identifiedReport.setSpecimenCollectionGroup(this.scg);
		
		if(this.scg.getSurgicalPathologyNumber()==null)
		{
			this.scg.setSurgicalPathologyNumber(this.surgicalPathologyNumber);
		}
	}
}