
package krishagni.catissueplus.throttling.impl;

import krishagni.catissueplus.throttling.ThrottleService;
import krishagni.catissueplus.throttling.ThrottleServiceFactory;

public class ThrottleServiceFactoryImpl implements ThrottleServiceFactory
{

	@Override
	public ThrottleService getThrottleServiceImpl()
	{
		return ThrottleServiceImpl.getInstance();
	}

}
