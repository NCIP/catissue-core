
package krishagni.catissueplus.throttling.impl;

import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.global.Variables;
import edu.wustl.common.util.XMLPropertyHandler;
import krishagni.catissueplus.throttling.Counter;
import krishagni.catissueplus.throttling.CounterFactory;
import krishagni.catissueplus.throttling.Event;

public class CounterFactoryImpl implements CounterFactory
{

	public Counter create(Event event)
	{
		String timeIntervalInMinutes = XMLPropertyHandler.getValue(Constants.THROTTLING_TIME_INTERVAL);
		String maxLimits = XMLPropertyHandler.getValue(Constants.THROTTLING_MAX_LIMIT);
		final int maximumTreeNodeLimit = Integer.parseInt(maxLimits);
		final long timeInterval = Long.parseLong(timeIntervalInMinutes)*60*1000;
		Counter counter = new Counter(timeInterval, maximumTreeNodeLimit);
		return counter;
	}
}
