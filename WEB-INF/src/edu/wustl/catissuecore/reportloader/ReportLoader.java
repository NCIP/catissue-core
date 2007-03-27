package edu.wustl.catissuecore.reportloader;

import java.util.Iterator;
import java.util.List;

import edu.wustl.catissuecore.domain.Participant;
import edu.wustl.catissuecore.domain.Site;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.catissuecore.domain.pathology.IdentifiedSurgicalPathologyReport;
import edu.wustl.catissuecore.domain.pathology.TextContent;
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
	 * @param s site
	 * Constructor
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
	 * @throws Exception
	 * processes the reporting data. Validats for participant,site and and report
	 * existance. It also stores data to the datastore.   
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
				this.scg=ReportLoaderUtil.createNewSpecimenCollectionGroup(this.participant, this.identifiedReport,this.site, this.surgicalPathologyNumber);
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
