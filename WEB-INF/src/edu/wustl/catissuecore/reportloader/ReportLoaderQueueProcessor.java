package edu.wustl.catissuecore.reportloader;

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
		List queue=null;
		Set participantSet=null;
		ReportLoaderQueue reportLoaderQueue=null;
		ParserManager pMgr=null;
		HL7Parser parser=null;
		while(true)
		{	
			try
			{
				queue=ReportLoaderUtil.getObject(ReportLoaderQueue.class.getName(),"status" ,Parser.NEW);
				if(queue!=null && queue.size()>0)
				{
					for(int i=0;i<queue.size();i++)
					{
						reportLoaderQueue=(ReportLoaderQueue)queue.get(i);
						participantSet=(Set)reportLoaderQueue.getParticipantCollection();
						Iterator it = participantSet.iterator();
						if(it.hasNext())
						{
							pMgr=ParserManager.getInstance();
							parser=(HL7Parser)pMgr.getParser(Parser.HL7_PARSER);
							parser.parseString((Participant)it.next(),reportLoaderQueue.getReportText());
							ReportLoaderUtil.deleteObject(reportLoaderQueue);
						}	
					}
				}
				Thread.sleep(Long.parseLong(XMLPropertyHandler.getValue(Parser.REPORTLOADER_QUEUE_SLEEP)));
			}
			catch(Exception ex)
			{
				Logger.out.error("Error while adding report data",ex);
			}
			
		}	
	}
	
	
}
