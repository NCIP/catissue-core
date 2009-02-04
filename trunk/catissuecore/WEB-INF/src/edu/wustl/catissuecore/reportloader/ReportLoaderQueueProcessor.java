package edu.wustl.catissuecore.reportloader;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import edu.wustl.catissuecore.domain.Participant;
import edu.wustl.catissuecore.domain.pathology.ReportLoaderQueue;
import edu.wustl.common.util.XMLPropertyHandler;
import edu.wustl.common.util.logger.Logger;

/**
 * @author sandeep_ranade
 * This class represents a thread which polls on report queue entries and
 * add report data to database. 
 * 
 */
public class ReportLoaderQueueProcessor extends Thread
{
	/**
	 * @see java.lang.Thread#run()
	 */
	public void run()
	{
		CSVLogger.configure(Parser.LOGGER_QUEUE_PROCESSOR);
		List queue=null;
		Set participantSet=null;
		ReportLoaderQueue reportLoaderQueue=null;
		ParserManager pMgr=null;
		HL7Parser parser=null;
		while(true)
		{	
			try
			{
				// retrieve records from report queue for processing
				queue=ReportLoaderUtil.getObject(ReportLoaderQueue.class.getName(),"status" ,Parser.NEW);
				Logger.out.info("Processing report Queue: Total "+queue.size()+" Reports found in queue");
				CSVLogger.info(Parser.LOGGER_QUEUE_PROCESSOR,"Processing report Queue: Total "+queue.size()+" Reports found in queue");
				CSVLogger.info(Parser.LOGGER_QUEUE_PROCESSOR," Date/Time, Report Loder Queue ID, Status, Message");
				if(queue!=null && queue.size()>0)
				{
					//	Initializing SiteInfoHandler to avoid restart of server to get new site names added to file at run time
					SiteInfoHandler.init(XMLPropertyHandler.getValue("site.info.filename"));
					for(int i=0;i<queue.size();i++)
					{
						try
						{
							reportLoaderQueue=(ReportLoaderQueue)queue.get(i);
							Logger.out.info("Processing report from Queue with serial no="+reportLoaderQueue.getId());
							participantSet=(Set)reportLoaderQueue.getParticipantCollection();
							Iterator it = participantSet.iterator();
							if(it.hasNext())
							{
								// get instance  of parser
								pMgr=ParserManager.getInstance();
								parser=(HL7Parser)pMgr.getParser(Parser.HL7_PARSER);
								try
								{
									// parse report text 
									parser.parseString((Participant)it.next(),reportLoaderQueue.getReportText(), reportLoaderQueue.getSpecimenCollectionGroup());
									// delete record from queue
									ReportLoaderUtil.deleteObject(reportLoaderQueue);
									CSVLogger.info(Parser.LOGGER_QUEUE_PROCESSOR,new Date().toString()+","+reportLoaderQueue.getId()+","+"SUCCESS"+",Report Loaded SuccessFully  ");
									Logger.out.info("Processed report from Queue with serial no="+reportLoaderQueue.getId());
								}
								catch(Exception ex)
								{
									CSVLogger.info(Parser.LOGGER_QUEUE_PROCESSOR,new Date().toString()+","+reportLoaderQueue.getId()+","+","+"FAILED"+","+ex.getMessage());
									reportLoaderQueue.setStatus(Parser.FAILURE);
									ReportLoaderUtil.updateObject(reportLoaderQueue);
								}
							}
						}
						catch(Exception ex)
						{
							reportLoaderQueue.setStatus(Parser.FAILURE);
							Logger.out.error("Error in parsing queue "+i);
						}
					}
				}
				Logger.out.info("Report Loader Queue is going to sleep for "+XMLPropertyHandler.getValue(Parser.POLLER_SLEEP)+"ms");
				Thread.sleep(Long.parseLong(XMLPropertyHandler.getValue(Parser.POLLER_SLEEP)));
			}
			catch(Exception ex)
			{
				Logger.out.error("Error while adding report data",ex);
			}			
		}	
	}	
}
