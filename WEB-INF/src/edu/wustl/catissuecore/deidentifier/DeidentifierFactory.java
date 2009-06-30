
package edu.wustl.catissuecore.deidentifier;

import edu.wustl.catissuecore.caties.util.CaTIESConstants;
import edu.wustl.catissuecore.caties.util.CaTIESProperties;

public class DeidentifierFactory
{

	public static AbstractDeidentifier getDeidentiifier() throws ClassNotFoundException,
			InstantiationException, IllegalAccessException
	{
		AbstractDeidentifier deidentifier = null;

		Class<AbstractDeidentifier> deidentifierClass = (Class<AbstractDeidentifier>) Class
				.forName(CaTIESProperties.getValue(CaTIESConstants.DEIDENTIFIER_CLASSNAME));
		deidentifier = (AbstractDeidentifier) deidentifierClass.newInstance();

		return deidentifier;
	}
}
