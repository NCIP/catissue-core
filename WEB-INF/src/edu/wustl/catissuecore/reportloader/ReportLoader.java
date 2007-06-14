package edu.wustl.catissuecore.reportloader;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import edu.wustl.catissuecore.bizlogic.BizLogicFactory;
import edu.wustl.catissuecore.bizlogic.CollectionProtocolBizLogic;
import edu.wustl.catissuecore.bizlogic.CollectionProtocolRegistrationBizLogic;
import edu.wustl.catissuecore.bizlogic.SpecimenCollectionGroupBizLogic;
import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.catissuecore.domain.CollectionProtocolEvent;
import edu.wustl.catissuecore.domain.CollectionProtocolRegistration;
import edu.wustl.catissuecore.domain.Participant;
import edu.wustl.catissuecore.domain.Site;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.catissuecore.domain.pathology.IdentifiedSurgicalPathologyReport;
import edu.wustl.catissuecore.domain.pathology.TextContent;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.util.XMLPropertyHandler;
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
	 * 
	 */
	public ReportLoader(Participant p,IdentifiedSurgicalPathologyReport report,Site s, SpecimenCollectionGroup scg, String surgicalPathologyNumber)
	{
		this.participant=p;
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
		Logger.out.info("Processing Report ");
		identifiedReport.setReportStatus(Parser.PENDING_FOR_DEID);
		identifiedReport.setSource(this.site);
		try
		{	
			// check for existing specimen collection group(scg)
			if(this.scg==null || this.scg.getId()==0)
			{
				// if scg is null create new scg
				Logger.out.info("Null SCG found in ReportLoader, Creating New SCG");
				this.scg=createNewSpecimenCollectionGroup(this.participant, this.identifiedReport,this.site, this.surgicalPathologyNumber);
				ReportLoaderUtil.saveObject(scg);
			}
			else
			{
				// use existing scg
				if(this.scg.getIdentifiedSurgicalPathologyReport()!=null)
				{
					boolean isExists = checkForTextContent(this.scg.getIdentifiedSurgicalPathologyReport());
					if(!isExists)
					{
						// if report text is null then set report text
						this.scg.getIdentifiedSurgicalPathologyReport().setTextContent(this.identifiedReport.getTextContent());
					}	
				}
				else
				{
					this.scg.setIdentifiedSurgicalPathologyReport(this.identifiedReport); 
				}
				ReportLoaderUtil.updateObject(scg);
			}	
			Logger.out.info("Processing finished for Report ");
		}
		catch(Exception ex)
		{
			Logger.out.error("Failed to process report ");
			throw new Exception(ex.getMessage());
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
	public static SpecimenCollectionGroup createNewSpecimenCollectionGroup(Participant participant, IdentifiedSurgicalPathologyReport identifiedReport, Site site, String surgicalPathologyNumber)throws Exception
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
		String className=CollectionProtocol.class.getName();
		String colName=new String("title");
		String colValue=XMLPropertyHandler.getValue("collectionProtocolTitle");
		BizLogicFactory bizLogicFactory=BizLogicFactory.getInstance();
		CollectionProtocolBizLogic cpBizLogic=(CollectionProtocolBizLogic)bizLogicFactory.getBizLogic(CollectionProtocol.class.getName());
		List cpList=cpBizLogic.retrieve(className, colName, colValue);
		CollectionProtocol collectionProtocol=null;
		if(cpList!=null && cpList.size()>0)
		{
			collectionProtocol=(CollectionProtocol)cpList.get(0);
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
		String collProtocolTitle=collectionProtocol.getTitle();
		if(collProtocolTitle.length()>30)
		{
			collProtocolTitle=collProtocolTitle.substring(0,29);
		}
		String scgName="SPR_"+collProtocolTitle+"_"+participant.getId()+"_"+groupId; //this.identifiedReport.getAccessionNumber().toString());
		scg.setName(scgName);
		Logger.out.info("SCG name is =====>"+scgName);
		
		// retrieve collection protocol event list
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
			
			// check for existing CollectionProtocolRegistration, if exists then use existing
			CollectionProtocolRegistration collProtocolReg=isCPRExists(participant, collectionProtocol);
			if(collProtocolReg==null)
			{	
				// otherwise create new CollectionProtocolRegistration
				Logger.out.info("Creating New CollectionProtocolRegistration object");
				collProtocolReg=new CollectionProtocolRegistration();
				collProtocolReg.setActivityStatus(Constants.ACTIVITY_STATUS_ACTIVE);
				collProtocolReg.setRegistrationDate(new Date());
				collProtocolReg.setParticipant(participant);	
				collProtocolReg.setCollectionProtocol(collProtocolEvent.getCollectionProtocol());
				((Set)participant.getCollectionProtocolRegistrationCollection()).add(collProtocolReg);
				try
				{
					ReportLoaderUtil.saveObject(collProtocolReg);
				}
				catch(Exception ex)
				{
					Logger.out.error("Error: Could not save object of CollectionProtocolRegistration",ex);
					throw new Exception("Could not save object of CollectionProtocolRegistration :"+ex.getMessage());
				}		
			}
//			((Set)collProtocolReg.getSpecimenCollectionGroupCollection()).add(scg);
			// set CollectionProtocolRegistration to scg
			scg.setCollectionProtocolRegistration(collProtocolReg);
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
	public static CollectionProtocolRegistration isCPRExists(Participant participant, CollectionProtocol collectionProtocol) throws Exception
	{
		try
		{
			// retrive CollectionProtocolRegistration with current participant and collectionProtocol
			String sourceObjectName=new String(CollectionProtocolRegistration.class.getName());
			String[] selectColumnName=new String[]{"participant", "collectionProtocol"};
			String[] whereColumnCondition=new String[]{"=","="};
			String[] whereColumnValue=new String[]{participant.getId().toString(), collectionProtocol.getId().toString()};
			String joinCondition=Constants.AND_JOIN_CONDITION;
			
			BizLogicFactory bizLogicFactory=BizLogicFactory.getInstance();
			CollectionProtocolRegistrationBizLogic collProtRegBizLogic=(CollectionProtocolRegistrationBizLogic)bizLogicFactory.getBizLogic(CollectionProtocolRegistration.class.getName());
			List cprList=collProtRegBizLogic.retrieve(sourceObjectName, selectColumnName, whereColumnCondition, whereColumnValue,joinCondition);
			
			// check for existence
			if(cprList!=null && cprList.size()>0)
			{
				// cpr exist then return existing cpr
				Logger.out.info("Existing CPR found for participant id="+participant.getId()+" collectionProtocol id="+collectionProtocol.getId());
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
	public boolean checkForTextContent(IdentifiedSurgicalPathologyReport report)
	{
		Logger.out.info("Inside checkForTextContent function");
		TextContent content=report.getTextContent();
		// check for null report text content
		if(content !=null && content.getData()!=null && content.getData().length()>0)
		{
			return true;
		}
		return false;
	}	
}
