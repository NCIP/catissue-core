package edu.wustl.catissuecore.namegenerator;

/**
 * This is the Exception class related to NameGenerator.
 * @author abhijit_naik
 *
 */
public class NameGeneratorException extends Exception
{

	/**
	 * Serial Version UID.
	 */
	private static final long serialVersionUID = 2047363018172163498L;

	/**
	 * @param string String
	 */
	public NameGeneratorException(String string)
	{
		super(string);
	}

	/**
	 * @param string String
	 * @param th Class that is superclass of all errors
	 */
	public NameGeneratorException(String string, Throwable th)
	{
		super(string, th);
	}

}
