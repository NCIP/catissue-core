package krishagni.catissueplus.Exception;


public class CatissueException extends RuntimeException
{
	private int errorCode;

	
	public int getErrorCode()
	{
		return errorCode;
	}


	public CatissueException(int errorCode)
	{
		this.errorCode = errorCode;
		// TODO Auto-generated constructor stub
	}
	/**
	 * @param message : message
	 */
	public CatissueException(String message,int errorCode)
	{
		super(message);
		this.errorCode = errorCode;
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message : message
	 * @param cause : cause
	 */
	public CatissueException(String message, Throwable cause, int errorCode)
	{
		super(message, cause);
		this.errorCode = errorCode;
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param cause : cause
	 */
	public CatissueException(Throwable cause,int errorCode)
	{
		super(cause);
		this.errorCode = errorCode;
		// TODO Auto-generated constructor stub
	}

}
