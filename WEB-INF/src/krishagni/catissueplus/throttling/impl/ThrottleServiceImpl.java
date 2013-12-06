
package krishagni.catissueplus.throttling.impl;

import java.util.concurrent.ConcurrentHashMap;

import krishagni.catissueplus.throttling.Counter;
import krishagni.catissueplus.throttling.CounterFactory;
import krishagni.catissueplus.throttling.CounterKey;
import krishagni.catissueplus.throttling.CounterKeyFactory;
import krishagni.catissueplus.throttling.Event;
import krishagni.catissueplus.throttling.ThrottleService;

public class ThrottleServiceImpl implements ThrottleService
{

	private ConcurrentHashMap<CounterKey, Counter> counterCache = new ConcurrentHashMap<CounterKey, Counter>();
	private final static ThrottleServiceImpl throttleServiceImpl = new ThrottleServiceImpl();

	private ThrottleServiceImpl()
	{

	}

	public static ThrottleServiceImpl getInstance()
	{
		return throttleServiceImpl;
	}

	@Override
	public boolean throttle(Event event)
	{
		CounterKeyFactory counterKeyFactory = new CounterKeyFactoryImpl();
		CounterKey key = counterKeyFactory.getCounterKey(event);
		CounterFactory counterFactory;
		Counter counter = counterCache.get(key);
		if (counter == null)
		{
			counterFactory = new CounterFactoryImpl();
			counter = counterFactory.create(event);
			counterCache.putIfAbsent(key, counter);
		}

		return counter.increment();
	}

}
