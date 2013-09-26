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


public class LabelException extends LabelGenException
{

	public LabelException(String string)
	{
		super(string);
		// TODO Auto-generated constructor stub
	}

	/**
	 * The Constructor.
	 *
	 * @param string String
	 * @param throwable Class that is superclass of all errors
	 */
	public LabelException(String string, Throwable throwable)
	{
		super(string, throwable);
	}

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

}
