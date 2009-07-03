
package edu.wustl.catissuecore.deidentifier;

import edu.wustl.catissuecore.caties.util.CaTIESConstants;
import edu.wustl.catissuecore.caties.util.CaTIESProperties;
/**
 * @author
 *
 */
public class DeidentifierFactory
{
	/**
	 * @return AbstractDeidentifier
	 * @throws ClassNotFoundException : ClassNotFoundException
	 * @throws InstantiationException : InstantiationException
	 * @throws IllegalAccessException : IllegalAccessException
	 */
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
