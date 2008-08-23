
package edu.wustl.catissuecore.util;

import java.util.Comparator;
import edu.wustl.catissuecore.domain.CollectionProtocolEvent;



/**
 * @author kalpana_thakur
 * Used to compare collection protocol Event ,Order by StudyCalendarEventPoint
 *
 */
public class CollectionProtocolEventComparator implements Comparator{

	public int compare(Object arg0, Object arg1) {
	
		if(arg0 instanceof CollectionProtocolEvent && arg1 instanceof CollectionProtocolEvent)
		{
			CollectionProtocolEvent collectionProtocolEventFirst = (CollectionProtocolEvent)arg0;
			CollectionProtocolEvent collectionProtocolEventSec = (CollectionProtocolEvent)arg1;
			
			if(collectionProtocolEventFirst !=null && collectionProtocolEventSec != null)
			{
				/**
				 * IF eventPoint are equal comparet with ID
				 */
				int compareValue = collectionProtocolEventFirst.getStudyCalendarEventPoint().compareTo(collectionProtocolEventSec.getStudyCalendarEventPoint());
				if(compareValue==0&&collectionProtocolEventFirst.getId()!=null&&collectionProtocolEventSec.getId()!=null)
				{
					compareValue = collectionProtocolEventFirst.getId().compareTo(collectionProtocolEventSec.getId());
				}
				return compareValue;
			}
			
			if(collectionProtocolEventFirst ==null && collectionProtocolEventSec == null)
			{
				return 0;
			}
			
			
			if(collectionProtocolEventFirst ==null)
				return 1;
			if(collectionProtocolEventSec == null)
				return -1;
								
		}
		
		return 0;
	}
	
	
}
