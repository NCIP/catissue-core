package edu.wustl.catissuecore.comparator;

import java.util.Comparator;

import edu.wustl.catissuecore.domain.CollectionProtocolEvent;


public class CollectionProtocolEventComparator implements Comparator<CollectionProtocolEvent>
{

	public int compare(CollectionProtocolEvent arg0, CollectionProtocolEvent arg1)
	{
		int returnValue = 0;
		if (arg1 instanceof CollectionProtocolEvent)
		{
			final CollectionProtocolEvent collectionProtocolEvent = (CollectionProtocolEvent) arg1;
			if (arg0.getStudyCalendarEventPoint().doubleValue() < collectionProtocolEvent
					.getStudyCalendarEventPoint().doubleValue())
			{
				returnValue = -1;
			}
			else if (arg0.getStudyCalendarEventPoint().doubleValue() > collectionProtocolEvent
					.getStudyCalendarEventPoint().doubleValue())
			{
				returnValue = 1;
			}
			else
			{
				returnValue = 0;
			}
		}
		return returnValue;
	}

}
