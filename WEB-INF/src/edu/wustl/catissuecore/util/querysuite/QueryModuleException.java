package edu.wustl.catissuecore.util.querysuite;

public class QueryModuleException extends Exception
{
	
	private String message;
	private QueryModuleError key;
	
	public QueryModuleException(String message)
	{
		super();
		this.message = message;
	}
	
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
