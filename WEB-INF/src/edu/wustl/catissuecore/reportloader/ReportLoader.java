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
import edu.wustl.catissuecore.caties.util.SiteInfoHandler;
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
import edu.wustl.dao.exception.DAOException;
import edu.wustl.common.util.logger.Logger;
import gov.nih.nci.system.applicationservice.ApplicationException;

/**
 * @author sandeep_ranade
 * Represents a data loader which loads the report data into datastore. 
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
	
	private static final String defaultSiteName = SiteInfoHandler.getDefaultSiteName();
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
		this.participant=(Participant)CaCoreAPIService.getObject(Participant.class, Constants.SYSTEM_IDENTIFIER, this.participant.getId());
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
				//check for exactly matching SCG
				Logger.out.info("Checking for Matching SCG. Default site name:"+defaultSiteName+ " :" +this.site.getName());
				if(this.site.getName().equalsIgnoreCase(defaultSiteName))
				{
					Logger.out.info("Inside if");
					scg = (SpecimenCollectionGroup)CaCoreAPIService.getObject(SpecimenCollectionGroup.class, "surgicalPathologyNumber", surgicalPathologyNumber);
				//	scg=ReportLoaderUtil.getExactMatchingSCG(this.site, surgicalPathologyNumber);
				}
				if(this.scg==null || this.scg.getId()==0)
				{
					// if scg is null create new scg
					Logger.out.debug("Null SCG found in ReportLoader, Creating New SCG");
					this.scg=createNewSpecimenCollectionGroup();
					this.scg=(SpecimenCollectionGroup)CaCoreAPIService.createObject(this.scg);
					this.identifiedReport.setSpecimenCollectionGroup(this.scg);
					CaCoreAPIService.createObject(this.identifiedReport);
				}
				else
				{
					// use existing scg
					retrieveAndSetSCG();
				}
			}
			else
			{
				// use existing scg
				retrieveAndSetSCG();
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
		scg.setCollectionStatus(Constants.COMPLETE);
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
		
		// retrieve collection protocol event list
		Collection collProtocolEventList=(Collection)CaCoreAPIService.getList(CollectionProtocolEvent.class, "collectionProtocol", collectionProtocol);
		Iterator cpEventIterator=collProtocolEventList.iterator();
		
		if(cpEventIterator.hasNext())
		{		
			CollectionProtocolEvent collProtocolEvent=(CollectionProtocolEvent)cpEventIterator.next();
			
			
			// check for existing CollectionProtocolRegistration, if exists then use existing
			CollectionProtocolRegistration collProtocolReg=getExistingCPR(participant, collectionProtocol);
			if(collProtocolReg==null)
			{	
				// otherwise create new CollectionProtocolRegistration
				Logger.out.info("Creating New CollectionProtocolRegistration object");
				collProtocolReg=new CollectionProtocolRegistration();
				collProtocolReg.setActivityStatus(Constants.ACTIVITY_STATUS_ACTIVE);
				collProtocolReg.setRegistrationDate(new Date());
				
				Participant transientParticipant = new Participant();
				transientParticipant.setId(participant.getId());
				
				CollectionProtocol transientCP = new CollectionProtocol();
				transientCP.setId(collectionProtocol.getId());
				transientCP.setTitle(collectionProtocol.getTitle());
				
				collProtocolReg.setParticipant(transientParticipant);
				collProtocolReg.setCollectionProtocol(transientCP);
				collProtocolReg.setProtocolParticipantIdentifier(Constants.REGISTRATION_FOR_REPORT_LOADER);
				try
				{
					collProtocolReg=(CollectionProtocolRegistration)CaCoreAPIService.createObject(collProtocolReg);
				}
				catch(Exception ex)
				{
					Logger.out.error("Error: Could not save object of CollectionProtocolRegistration",ex);
					throw new Exception("Could not save object of CollectionProtocolRegistration :"+ex.getMessage());
				}		
			}	
			scg.setCollectionProtocolEvent(collProtocolEvent);
			scg.setCollectionProtocolRegistration(collProtocolReg);
			scg.setSpecimenEventParametersCollection(getDefaultEvents(scg));
			scg.setName(Constants.REPORT_LOADER_SCG+CaCoreAPIService.getSpecimenCollectionGroupLabel(scg));
			scg.setBarcode(Constants.REPORT_LOADER_SCG);
		}
		else
		{
			Logger.out.error("Associated Collection Protocol Event not found for "+ CaTIESProperties.getValue(CaTIESConstants.COLLECTION_PROTOCOL_TITLE));
			throw new Exception("Associated Collection Protocol Event not found for "+ CaTIESProperties.getValue(CaTIESConstants.COLLECTION_PROTOCOL_TITLE));
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
			cprList = (List)CaCoreAPIService.executeQuery(criteria, CollectionProtocolRegistration.class.getName());
			// check for existence
			if(cprList!=null && cprList.size()>0)
			{
				// cpr exist then return existing cpr
				Logger.out.info("Existing CPR found for participant id="+participant.getId()+" collectionProtocol id="+cp.getId());
				CollectionProtocolRegistration collProtocolReg=(CollectionProtocolRegistration)cprList.get(0);
				collProtocolReg.setParticipant(participant);	
				collProtocolReg.setCollectionProtocol(cp);
				return collProtocolReg;
			}
		} 
		catch (ApplicationException appEx) 
		{
			Logger.out.error("Error while retrieving List for "+ CollectionProtocolRegistration.class.getName()+appEx);
		}
		Logger.out.info("No Existing CPR found for participant id="+participant.getId()+" collectionProtocol id="+cp.getId());
		return null;
	}
	
	private Collection<SpecimenEventParameters> getDefaultEvents(SpecimenCollectionGroup specimenCollectionGroup) throws DAOException, Exception
	{
		Collection<SpecimenEventParameters> defaultEventCollection=new HashSet<SpecimenEventParameters>();
		String loginName=CaTIESProperties.getValue(CaTIESConstants.USER_NAME);
		User user=(User)CaCoreAPIService.getObject(User.class, Constants.LOGINNAME, loginName);
		
		CollectionEventParameters collectionEvent=new CollectionEventParameters();
		collectionEvent.setCollectionProcedure((String)CaCoreAPIService.getDefaultValue(Constants.DEFAULT_COLLECTION_PROCEDURE));
		collectionEvent.setContainer((String)CaCoreAPIService.getDefaultValue(Constants.DEFAULT_CONTAINER));
		collectionEvent.setSpecimenCollectionGroup(specimenCollectionGroup);
		collectionEvent.setTimestamp(new Date());
		collectionEvent.setUser(user);
		
		ReceivedEventParameters recievedEvent=new ReceivedEventParameters();
		recievedEvent.setReceivedQuality((String)CaCoreAPIService.getDefaultValue(Constants.DEFAULT_RECEIVED_QUALITY));
		recievedEvent.setSpecimenCollectionGroup(specimenCollectionGroup);
		recievedEvent.setTimestamp(new Date());
		recievedEvent.setUser(user);
		
		defaultEventCollection.add(collectionEvent);
		defaultEventCollection.add(recievedEvent);
		return defaultEventCollection;
	}
	
	private void retrieveAndSetSCG() throws Exception
	{

		this.scg=(SpecimenCollectionGroup)CaCoreAPIService.getObject(SpecimenCollectionGroup.class, Constants.SYSTEM_IDENTIFIER, scg.getId());
		if(this.scg.getSpecimenCollectionSite()==null)
		{
			this.scg.setSpecimenCollectionSite(this.site);
		}
		CollectionProtocolRegistration collectionProtocolRegistration=(CollectionProtocolRegistration)CaCoreAPIService.getObject(CollectionProtocolRegistration.class, Constants.SYSTEM_IDENTIFIER, this.scg.getCollectionProtocolRegistration().getId());
		this.scg.setCollectionProtocolRegistration(collectionProtocolRegistration);
		// set identified report
		if(this.scg.getIdentifiedSurgicalPathologyReport()!=null)
		{
			
			Logger.out.info("inside"+this.scg.getIdentifiedSurgicalPathologyReport().getId());
			
			IdentifiedSurgicalPathologyReport existingReport=(IdentifiedSurgicalPathologyReport)CaCoreAPIService.getObject(IdentifiedSurgicalPathologyReport.class, Constants.SYSTEM_IDENTIFIER, this.scg.getIdentifiedSurgicalPathologyReport().getId());
			DeidentifiedSurgicalPathologyReport existingDeidReport=null;
			if(this.scg.getDeIdentifiedSurgicalPathologyReport()!=null)
			{
				existingDeidReport=(DeidentifiedSurgicalPathologyReport)CaCoreAPIService.getObject(DeidentifiedSurgicalPathologyReport.class, Constants.SYSTEM_IDENTIFIER, this.scg.getDeIdentifiedSurgicalPathologyReport().getId());
				existingReport.setDeIdentifiedSurgicalPathologyReport(null);
			}
			existingReport.getTextContent().setData(this.identifiedReport.getTextContent().getData());
			
			try 
			{
				existingReport=(IdentifiedSurgicalPathologyReport)CaCoreAPIService.updateObject(existingReport);
				Logger.out.info("existingReport updated:"+existingReport.getId());
				if(existingDeidReport!=null)
				{
					existingDeidReport.setSpecimenCollectionGroup(null);
					DeidentifiedSurgicalPathologyReport deidreport=(DeidentifiedSurgicalPathologyReport)CaCoreAPIService.updateObject(existingDeidReport);
					Logger.out.info("deid report updated: "+deidreport.getId());
				}
			}
			catch (ApplicationException ex) 
			{
				Logger.out.error("Error while updating old report!",ex);
				throw new Exception(ex.getMessage());
			}
		}
		else
		{
			Logger.out.info("No associated report with SCG");
			this.scg.setIdentifiedSurgicalPathologyReport(this.identifiedReport);
			this.identifiedReport.setSpecimenCollectionGroup(this.scg);
			CaCoreAPIService.createObject(this.identifiedReport);
		}
		this.scg.setSurgicalPathologyNumber(this.surgicalPathologyNumber);
		CaCoreAPIService.updateObject(this.scg);
	}
}