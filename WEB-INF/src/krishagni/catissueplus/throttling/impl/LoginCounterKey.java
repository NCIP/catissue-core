
package krishagni.catissueplus.throttling.impl;

import krishagni.catissueplus.throttling.CounterKey;
import edu.wustl.common.util.global.Validator;

public class LoginCounterKey implements CounterKey
{

	private String ipAddress;

	public LoginCounterKey(String iPAddress)
	{
		super();
		ipAddress = iPAddress;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ipAddress == null) ? 0 : ipAddress.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
		{
			return true;
		}
		if (obj == null)
		{
			return false;
		}
		if (obj instanceof LoginCounterKey)
		{
			LoginCounterKey loginCounterKey = (LoginCounterKey) obj;
			if (Validator.isEmpty(loginCounterKey.ipAddress) || Validator.isEmpty(this.ipAddress))
				return false;
			else if (this.ipAddress.compareTo(loginCounterKey.ipAddress) == 0)
				return true;
		}
		return false;
	}

}
