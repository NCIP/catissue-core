
package edu.wustl.catissuecore.util.querysuite;

/**
 * Exception class for Query module.
 *
 */
public class QueryModuleException extends Exception
{

	/**
	 * message The message.
	 */
	private String message;

	/**
	 * key The key.
	 */
	private QueryModuleError key;

	/**
	 * @param message The message to set
	 */
	public QueryModuleException(String message)
	{
		super();
		this.message = message;
	}

	/**
	 * @param message The message to set.
	 * @param key The key to set
	 */
	public QueryModuleException(String message, QueryModuleError key)
	{
		super(message);
		this.key = key;
		this.message = message;
	}

	/**
	 * @return the key
	 */
	public QueryModuleError getKey()
	{
		return key;
	}

}
