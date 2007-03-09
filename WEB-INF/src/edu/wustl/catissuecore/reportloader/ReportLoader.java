package edu.wustl.catissuecore.reportloader;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import edu.wustl.catissuecore.bizlogic.BizLogicFactory;
import edu.wustl.catissuecore.bizlogic.CollectionProtocolBizLogic;
import edu.wustl.catissuecore.bizlogic.ParticipantBizLogic;
import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.catissuecore.domain.CollectionProtocolEvent;
import edu.wustl.catissuecore.domain.CollectionProtocolRegistration;
import edu.wustl.catissuecore.domain.Participant;
import edu.wustl.catissuecore.domain.Site;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.domain.pathology.IdentifiedSurgicalPathologyReport;
import edu.wustl.catissuecore.domain.pathology.TextContent;
import edu.wustl.catissuecore.util.CatissueCoreCacheManager;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.bizlogic.DefaultBizLogic;
import edu.wustl.common.lookup.DefaultLookupResult;
import edu.wustl.common.util.XMLPropertyHandler;
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
	 * prticipant exists flag
	 */
	private boolean isParticipantExists;
	
	
	/**
	 * @param p participant 
	 * @param report identified report
	 * @param s site
	 * Constructor
	 */
	public ReportLoader(Participant p,IdentifiedSurgicalPathologyReport report,Site s)
	{
		this.participant=p;
		this.identifiedReport=report;
		this.site=s;
	}
	
	/**
	 * @param p participant
	 * @param report identified report
	 * @param s Site
	 * @param participantExists 
	 * Constructor
	 * 
	 */
	public ReportLoader(Participant p,IdentifiedSurgicalPathologyReport report,Site s, boolean participantExists)
	{
		this.participant=p;
		this.identifiedReport=report;
		this.site=s;
		this.isParticipantExists=participantExists;
	}
	
	/**
	 * @return number of matching participants
	 * @throws Exception throws exception
	 */
	public int checkForParticipant()throws Exception
	{
		int size=0;
		List participantList=null;
		BizLogicFactory bizLogicFactory = BizLogicFactory.getInstance();
		ParticipantBizLogic bizLogic =(ParticipantBizLogic) bizLogicFactory.getBizLogic(Participant.class.getName());
		participantList = bizLogic.getListOfMatchingParticipants(this.participant);
   		if(participantList!=null && participantList.size()>0)
		{
			if(participantList.size()==1)
			{
				DefaultLookupResult participantResult=(DefaultLookupResult)participantList.get(0);
			    //these probability is being removed now
				//Double probability = Double.valueOf(XMLPropertyHandler.getValue(Constants.MAX_PARTICIPANT_MATCHING_PERCENTAGE));
				//if(probability.doubleValue() <= participantResult.getProbablity().doubleValue())
				//{	
					this.participant = (Participant)participantResult.getObject();
				//}	
				size=1;
			}
		}
		return size;
	}
	
	/**
	 * @throws Exception
	 * processes the reporting data. Validats for participant,site and and report
	 * existance. It also stores data to the datastore.   
	 */
	public void process()throws Exception
	{
		boolean isExists=false;
		int size=0;
		SpecimenCollectionGroup scg=null;
		try
		{
			
		/*	if(!isParticipantExists)
			{
				size =checkForParticipant();
			}	
			if(size==0)
			{
				checkForSite();
				checkForSpecimenCollectionGroup();
				saveObject(this.participant);
				addParticipantToCache();
			}
			else if(size==1)
			{*/
				checkForSite();
				scg=checkForSpecimenCollectionGroup();
				identifiedReport.setReportStatus(Parser.PENDING_FOR_DEID);
				identifiedReport.setSource(this.site);
				if(scg!=null)
				{
					ReportLoaderUtil.saveObject(scg);
				}	
			//}
		}
		catch(Exception ex)
		{
			Logger.out.error("participant " + this.participant.getSocialSecurityNumber(), ex);
		}
	}
	
	/**
	 * @return  site for the pathology report data.
	 * @throws Exception throws exception
	 * Checks for existing site in the system.
	 */
	public Site checkForSite()throws Exception
	{
		Site s=null;
		List list = ReportLoaderUtil.getObject(site.getClass().getName(),"name",this.site.getName());
		if(list!=null && list.size()>0)
		{
			this.site=(Site)list.get(0);
		}
		else
		{
			list = ReportLoaderUtil.getObject(User.class.getName(),"loginName","admin@admin.com");
			this.site.setCoordinator((User)list.get(0));
			ReportLoaderUtil.saveObject(this.site);
		}
		return s;
	}
	
	/**
	 * @return specimen collection group
	 * @throws Exception throws exception
	 */
	public SpecimenCollectionGroup checkForSpecimenCollectionGroup()throws Exception
	{
		List scgSet=null;
		SpecimenCollectionGroup scg=null;
		Iterator scgIterator=null;
		boolean isExists=false;
		try
		{
			scgSet=ReportLoaderUtil.getSCGList(this.participant);
			if(scgSet!=null && scgSet.size()>0)
			{
				scgIterator=scgSet.iterator();
				while(scgIterator.hasNext())
				{
					scg=(SpecimenCollectionGroup)scgIterator.next();
					isExists=checkForReport(scg);
					if(isExists)
					{
						scg=null;
						break;
					}
				}
				if(!isExists)
				{
					scg=createNewSpecimenCollectionGroup();
				}
			}
			else
			{
//				this.participant.setSpecimenCollectionGroupCollection(new HashSet());
				scg=createNewSpecimenCollectionGroup();
			}
		}
		catch(Exception ex)
		{
			Logger.out.error("Error while checking specimen collection group ",ex);	
		}
		return scg;
	}
	
	/**
	 * Adds participant to the cahe of participants
	 * @throws Exception throws exception
	 */
	private void addParticipantToCache()throws Exception 
	{
		
		CatissueCoreCacheManager catissueCoreCacheManager = CatissueCoreCacheManager.getInstance();
		HashMap participantMap = (HashMap)catissueCoreCacheManager.getObjectFromCache("listOfParticipants");
		participantMap.put(this.participant.getId(), this.participant);
		catissueCoreCacheManager.addObjectToCache(Constants.MAP_OF_PARTICIPANTS, participantMap);
	}
	/**
	 * Method to create new SCG
	 * @return scg object of SpecimenColectionGroup 
	 * @throws Exception generic exception
	 */
	public SpecimenCollectionGroup createNewSpecimenCollectionGroup()throws Exception
	{
		SpecimenCollectionGroup scg = new SpecimenCollectionGroup();
		scg.setActivityStatus(Constants.ACTIVITY_STATUS_ACTIVE);
//		scg.setParticipant(this.participant);
		scg.setClinicalDiagnosis(Constants.NOT_SPECIFIED);
		scg.setClinicalStatus(Constants.NOT_SPECIFIED);
		scg.setSpecimenCollectionSite(this.site); 
		scg.setIdentifiedSurgicalPathologyReport(this.identifiedReport);
		this.identifiedReport.setSpecimenCollectionGroup(scg);
	
		scg.setName("caties_"+ this.identifiedReport.getAccessionNumber().toString());
		
		String className=CollectionProtocol.class.getName();
		String colName=new String("title");
		String colValue=XMLPropertyHandler.getValue("collectionProtocolTitle");
			
		BizLogicFactory bizLogicFactory=BizLogicFactory.getInstance();
		CollectionProtocolBizLogic cpBizLogic=(CollectionProtocolBizLogic)bizLogicFactory.getBizLogic(CollectionProtocol.class.getName());
		List cpList=cpBizLogic.retrieve(className, colName, colValue);
		CollectionProtocol collectionProtocol=(CollectionProtocol)cpList.get(0);
		Set collProtocolEventList=(Set)collectionProtocol.getCollectionProtocolEventCollection();
		Iterator cpEventIterator=collProtocolEventList.iterator();
		
		if(!cpEventIterator.hasNext())
		{
			Logger.out.info("Associated Collection Protocol Event not found for "+ collectionProtocol.getTitle());
		}
		else
		{
			CollectionProtocolEvent collProtocolEvent=(CollectionProtocolEvent)cpEventIterator.next();
			scg.setCollectionProtocolEvent(collProtocolEvent);
			CollectionProtocolRegistration collProtocolReg=new CollectionProtocolRegistration();
			
			collProtocolReg.setActivityStatus(Constants.ACTIVITY_STATUS_ACTIVE);
			collProtocolReg.setRegistrationDate(new Date());
			collProtocolReg.setParticipant(this.participant);	
			collProtocolReg.setCollectionProtocol(collProtocolEvent.getCollectionProtocol());
			((Set)this.participant.getCollectionProtocolRegistrationCollection()).add(collProtocolReg);
			try
			{
				ReportLoaderUtil.saveObject(collProtocolReg);
			}
			catch(Exception ex)
			{
				Logger.out.error("Error: Could not save object of CollectionProtocolRegistration",ex);
			}
			
			
			//((Set)collProtocolReg.getSpecimenCollectionGroupCollection()).add(scg);
			scg.setCollectionProtocolRegistration(collProtocolReg);
			
		}
			
		return scg;
	}
	
	
	
	/**
	 * @param specimenCollectionGroup specimen collection group 
	 * @return boolean value which represents existance of the report
	 * for a given specimen collection group.
	 * @throws Exception throws exception
	 * 
	 */
	public boolean checkForReport(SpecimenCollectionGroup specimenCollectionGroup)throws Exception
	{
		boolean isExists=false;
		IdentifiedSurgicalPathologyReport report=specimenCollectionGroup.getIdentifiedSurgicalPathologyReport();
		if(report !=null)
		{
			if(report.getAccessionNumber().equals(this.identifiedReport.getAccessionNumber()))
			{	isExists = checkForTextContent(report);
				if(!isExists)
				{
					report.setTextContent(this.identifiedReport.getTextContent());
					this.identifiedReport.getTextContent().setSurgicalPathologyReport(report);
					ReportLoaderUtil.updateObject(report);
				}
				return true;
			}
			return false;
		}
		return false;
	}
	
	/**
	 * @param report identified surgical pathology report
	 * @return boolean value which indicates text content is present or not
	 */
	public boolean checkForTextContent(IdentifiedSurgicalPathologyReport report)
	{
		TextContent content=report.getTextContent();
		if(content !=null)
		{
			return true;
		}
		return false;
	}
	
	

		
	
}
