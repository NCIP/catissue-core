package edu.wustl.catissuecore.namegenerator;

import java.util.HashMap;
import java.util.Map;

import edu.wustl.common.util.logger.Logger;


// TODO: Auto-generated Javadoc
/**
 * A factory for creating Token objects.
 */
/**
 * @author nitesh_marwaha
 *
 */
public class TokenFactory
{

	/** Logger object. */
	private static final Logger LOGGER = Logger.getCommonLogger(LabelGeneratorFactory.class);

	/**
	 * Private constructor.
	 */
	private TokenFactory()
	{

	}

	/** Singleton instance of SpecimenLabelGenerator. */
	private static Map<String, Object> TokenMap = new HashMap<String, Object>();

	/**
	 * Get singleton instance of SpecimenLabelGenerator.
	 * The class name of an instance is picked up from properties file
	 *
	 * @param tokenKey Property key name for specific Object's
	 * Label generator class (eg.specimenLabelGeneratorClass)
	 *
	 * @return LabelGenerator
	 *
	 * @throws NameGeneratorException NameGeneratorException
	 */
	public static LabelTokens getInstance(String tokenKey) throws NameGeneratorException
	{
		try
		{
			LabelTokens labelToken = null;
			if (TokenMap.get(tokenKey) == null)
			{
				String className = PropertyHandler.getTokenValue(tokenKey);

				if (className == null || "".equals(className))
				{
					labelToken = new DefaultLabelTokens();
				}
				else
				{
					TokenMap.put(tokenKey, Class.forName(className).newInstance());
					labelToken = (LabelTokens)TokenMap.get(tokenKey);
				}
			}
			else
			{
				labelToken = (LabelTokens)TokenMap.get(tokenKey);
			}
			return labelToken;
		}
		catch (final IllegalAccessException e)
		{
			LOGGER.error(e.getMessage(), e);
			throw new NameGeneratorException("Could not create LabelGenerator instance: "
					+ e.getMessage());
		}
		catch (final InstantiationException e)
		{
			LOGGER.error(e.getMessage(), e);
			throw new NameGeneratorException("Could not create LabelGenerator instance: "
					+ e.getMessage());
		}
		catch (final ClassNotFoundException e)
		{
			LOGGER.error(e.getMessage(), e);
			throw new NameGeneratorException("Could not create LabelGenerator instance: "
					+ e.getMessage());
		}
		catch (final Exception ex)
		{
			LOGGER.error(ex.getMessage(), ex);
			throw new NameGeneratorException(ex.getMessage(), ex);
		}
	}
}
