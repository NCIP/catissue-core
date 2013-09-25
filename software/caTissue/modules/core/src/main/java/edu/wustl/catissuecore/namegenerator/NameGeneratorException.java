/*L
 *  Copyright Washington University in St. Louis
 *  Copyright SemanticBits
 *  Copyright Persistent Systems
 *  Copyright Krishagni
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/catissue-core/LICENSE.txt for details.
 */


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
	 * @param throwable Class that is superclass of all errors
	 */
	public NameGeneratorException(String string, Throwable throwable)
	{
		super(string, throwable);
	}

}
