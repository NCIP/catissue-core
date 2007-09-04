package edu.wustl.catissuecore.reportloader;

import java.sql.Clob;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import edu.wustl.catissuecore.bizlogic.BizLogicFactory;
import edu.wustl.catissuecore.bizlogic.ReportLoaderQueueBizLogic;
import edu.wustl.catissuecore.caties.util.CSVLogger;
import edu.wustl.catissuecore.caties.util.CaTIESConstants;
import edu.wustl.catissuecore.caties.util.CaTIESProperties;
import edu.wustl.catissuecore.caties.util.SiteInfoHandler;
import edu.wustl.catissuecore.caties.util.Utility;
import edu.wustl.catissuecore.domain.Participant;
import edu.wustl.catissuecore.domain.pathology.ReportLoaderQueue;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.beans.NameValueBean;
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
		CSVLogger.configure(CaTIESConstants.LOGGER_QUEUE_PROCESSOR);
		List queue=null;
		Set participantSet=null;
		ReportLoaderQueue reportLoaderQueue=null;
		HL7Parser parser=null;
		Long startTime=null;
		Long endTime=null;
		while(true)
		{	
			try
			{
				// retrieve records from report queue for processing
				queue=getQueueObjects();
				Logger.out.info("Processing report Queue: Total "+(queue.size()-1)+" Reports found in queue");
				//	CONSTANT
				CSVLogger.info(CaTIESConstants.LOGGER_QUEUE_PROCESSOR,"Processing report Queue: Total "+queue.size()+" Reports found in queue");
				CSVLogger.info(CaTIESConstants.LOGGER_QUEUE_PROCESSOR,CaTIESConstants.CSVLOGGER_DATETIME+CaTIESConstants.CSVLOGGER_SEPARATOR+CaTIESConstants.CSVLOGGER_REPORTQUEUE+CaTIESConstants.CSVLOGGER_SEPARATOR+CaTIESConstants.CSVLOGGER_STATUS+CaTIESConstants.CSVLOGGER_SEPARATOR+CaTIESConstants.CSVLOGGER_MESSAGE+CaTIESConstants.CSVLOGGER_SEPARATOR+CaTIESConstants.CSVLOGGER_PROCESSING_TIME);
				if(queue!=null && queue.size()>1)
				{
					//	Initializing SiteInfoHandler to avoid restart of server to get new site names added to file at run time
					SiteInfoHandler.init(CaTIESProperties.getValue(CaTIESConstants.SITE_INFO_FILENAME));
					String id;
					NameValueBean nb;
					BizLogicFactory bizLogicFactory = BizLogicFactory.getInstance();
					ReportLoaderQueueBizLogic bizLogic =(ReportLoaderQueueBizLogic) bizLogicFactory.getBizLogic(ReportLoaderQueue.class.getName());
					
					for(int i=1;i<queue.size();i++)
					{
						Logger.out.info("Processing report serial no:"+i);
						// list contains name value bean, get name value bean from list
						nb=(NameValueBean)queue.get(i);
						// get value which is an id of the identified report
						id=nb.getValue();
						Logger.out.info("Got report id="+id);
						// retrive the identified report using its id
						reportLoaderQueue=(ReportLoaderQueue)bizLogic.getReportLoaderQueueById(Long.parseLong(id));
						try
						{
							Logger.out.debug("Processing report from Queue with serial no="+reportLoaderQueue.getId());
							participantSet=(Set)reportLoaderQueue.getParticipantCollection();
							Iterator it = participantSet.iterator();
							
							// get instance  of parser
							parser= (HL7Parser)ParserManager.getInstance().getParser();
							
							Participant participant=null;
							// parse report text
							if(it.hasNext())
							{
								participant=(Participant)it.next();
							}
							Clob tempClob=reportLoaderQueue.getReportText();
							String reportText=tempClob.getSubString(1,(int)tempClob.length());
							startTime=new Date().getTime();
							parser.parseString(participant, reportText, reportLoaderQueue.getSpecimenCollectionGroup());
							endTime=new Date().getTime();
							// delete record from queue
							Utility.deleteObject(reportLoaderQueue);
							CSVLogger.info(CaTIESConstants.LOGGER_QUEUE_PROCESSOR,new Date().toString()+","+reportLoaderQueue.getId()+","+"SUCCESS"+",Report Loaded SuccessFully  ,"+(endTime-startTime));
							Logger.out.info("Processed report from Queue with serial no="+reportLoaderQueue.getId());
						}
						catch(Exception ex)
						{
							endTime=new Date().getTime();
							reportLoaderQueue.setStatus(CaTIESConstants.FAILURE);
							if(ex.getMessage().equalsIgnoreCase(CaTIESConstants.CP_NOT_FOUND_ERROR_MSG))
							{
								reportLoaderQueue.setStatus(CaTIESConstants.CP_NOT_FOUND);
							}
							CSVLogger.info(CaTIESConstants.LOGGER_QUEUE_PROCESSOR,new Date().toString()+","+reportLoaderQueue.getId()+","+reportLoaderQueue.getStatus()+","+ex.getMessage()+","+(endTime-startTime));
							Utility.updateObject(reportLoaderQueue);
						}
					}
				}
				Logger.out.info("Report Loader Queue is going to sleep for "+CaTIESProperties.getValue(CaTIESConstants.POLLER_SLEEPTIME)+"ms");
				Thread.sleep(Long.parseLong(CaTIESProperties.getValue(CaTIESConstants.POLLER_SLEEPTIME)));
			}
			catch(Exception ex)
			{
				Logger.out.error("Error while adding report data",ex);
			}			
		}	
	}
	
	/**
	 * Method to retrieve list of all objects from report queue
	 * @return list List of objects in report queue
	 * @throws Exception Generic exception
	 */
	private List getQueueObjects() throws Exception
	{
		List queue=null;
		
		String sourceObjectName=ReportLoaderQueue.class.getName();
		String[] displayNameFields=new String[] {Constants.SYSTEM_IDENTIFIER};
		String valueField=new String(Constants.SYSTEM_IDENTIFIER);
		String[] whereColumnName = new String[]{Constants.COLUMN_NAME_STATUS,Constants.COLUMN_NAME_STATUS,Constants.COLUMN_NAME_STATUS};
		String[] whereColumnCondition = new String[]{"=","=","="};
		Object[] whereColumnValue = new String[]{CaTIESConstants.NEW,CaTIESConstants.SITE_NOT_FOUND,CaTIESConstants.CP_NOT_FOUND};
		String joinCondition = Constants.OR_JOIN_CONDITION;
		String separatorBetweenFields = ", ";	
		
		BizLogicFactory bizLogicFactory = BizLogicFactory.getInstance();
		ReportLoaderQueueBizLogic bizLogic =(ReportLoaderQueueBizLogic) bizLogicFactory.getBizLogic(ReportLoaderQueue.class.getName());
		Logger.out.info("Firing query to retriev ids of ReportLoaderQueue ");
		
		queue = bizLogic.getList(sourceObjectName, displayNameFields, valueField, whereColumnName, whereColumnCondition, whereColumnValue, joinCondition, separatorBetweenFields, false);
		return queue;
	}
}
