package edu.wustl.catissuecore.namegenerator;


public class TokenNotFoundException extends LabelGenException
{

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	public TokenNotFoundException(String string)
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
	public TokenNotFoundException(String string, Throwable throwable)
	{
		super(string, throwable);
	}
}
