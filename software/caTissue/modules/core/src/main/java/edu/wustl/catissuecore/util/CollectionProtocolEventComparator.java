
package edu.wustl.catissuecore.util;

import java.util.Comparator;

import edu.wustl.catissuecore.domain.CollectionProtocolEvent;



/**
 * @author kalpana_thakur
 * Used to compare collection protocol Event ,Order by StudyCalendarEventPoint
 *
 */
public class CollectionProtocolEventComparator implements Comparator{

	public int compare(Object arg0, Object arg1) 
	{
		int returnVal=0;
		if(arg0 instanceof CollectionProtocolEvent && arg1 instanceof CollectionProtocolEvent)
		{
			CollectionProtocolEvent collectionProtocolEventFirst = (CollectionProtocolEvent)arg0;
			CollectionProtocolEvent collectionProtocolEventSec = (CollectionProtocolEvent)arg1;
			
			if(collectionProtocolEventFirst !=null && collectionProtocolEventSec != null)
			{
				/**
				 * IF eventPoint are equal compare  with ID
				 */
				int compareValue = 0;
				if(collectionProtocolEventFirst.getStudyCalendarEventPoint() != null && collectionProtocolEventSec.getStudyCalendarEventPoint() != null)
				{	
					compareValue = collectionProtocolEventFirst.getStudyCalendarEventPoint().compareTo(collectionProtocolEventSec.getStudyCalendarEventPoint());
				}
				if(compareValue==0&&collectionProtocolEventFirst.getId()!=null&&collectionProtocolEventSec.getId()!=null)
				{
					compareValue = collectionProtocolEventFirst.getId().compareTo(collectionProtocolEventSec.getId());
				}
				returnVal=compareValue;
			}
			else if(collectionProtocolEventFirst ==null && collectionProtocolEventSec == null)
			{
				returnVal=0;
			}
			else if(collectionProtocolEventFirst ==null)
			{
				returnVal=1;
			}
			else if(collectionProtocolEventSec == null)
			{
				returnVal=-1;
			}					
		}
		return returnVal;
	}
	
	
}
