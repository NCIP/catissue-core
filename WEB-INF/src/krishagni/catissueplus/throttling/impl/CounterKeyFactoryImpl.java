
package krishagni.catissueplus.throttling.impl;

import edu.wustl.common.util.global.Validator;
import krishagni.catissueplus.throttling.CounterKey;
import krishagni.catissueplus.throttling.CounterKeyFactory;
import krishagni.catissueplus.throttling.Event;

public class CounterKeyFactoryImpl implements CounterKeyFactory
{

	public CounterKey getCounterKey(Event event)
	{
		CounterKey counterKey = null;
		if (event != null && !Validator.isEmpty(event.getResource()))
		{
			counterKey = new LoginCounterKey(event.getIpAddress());
		}
		return counterKey;
	}
}
