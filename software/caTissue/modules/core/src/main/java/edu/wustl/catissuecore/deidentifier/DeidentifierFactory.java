/*L
 *  Copyright Washington University in St. Louis
 *  Copyright SemanticBits
 *  Copyright Persistent Systems
 *  Copyright Krishagni
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/catissue-core/LICENSE.txt for details.
 */


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

		final Class<AbstractDeidentifier> deidentifierClass = (Class<AbstractDeidentifier>) Class
				.forName(CaTIESProperties.getValue(CaTIESConstants.DEIDENTIFIER_CLASSNAME));
		deidentifier = deidentifierClass.newInstance();

		return deidentifier;
	}
}
