
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
		String timeIntervalInMinutes = XMLPropertyHandler.getValue(Constants.MAXIMUM_TREE_NODE_LIMIT);
		String maxLimits = XMLPropertyHandler.getValue(Constants.MAXIMUM_TREE_NODE_LIMIT);
		final int maximumTreeNodeLimit = Integer.parseInt(maxLimits);
		final long timeInterval = Long.parseLong(timeIntervalInMinutes)*60*1000;
		Counter counter = new Counter(timeInterval, maximumTreeNodeLimit);
		return counter;
	}
}
