package edu.wustl.catissuecore.util.querysuite;


/**
 * enumerator for Query Error codes
 * @author vijay_pande
 *
 */
public enum QueryModuleError 
{
	SUCCESS (0),
	EMPTY_DAG (1),
	MULTIPLE_ROOT (2),
	NO_RESULT_PRESENT (3),
	SQL_EXCEPTION (4),
	DAO_EXCEPTION (5),
	CLASS_NOT_FOUND (6),
	RESULTS_MORE_THAN_LIMIT (10),
	NO_MAIN_OBJECT_IN_QUERY (11);
	
	/**
	 * Variable to hold value for error code
	 */
	private final int errorCode;
	/**
	 * Default constructor for enum
	 * @param errorCode
	 */
	QueryModuleError(final int errorCode)
	{
		this.errorCode=errorCode;
	}
	/**
	 * Method to get error code of particular instance
	 * @return
	 */
	public int getErrorCode()
	{
		return errorCode;
	}
}
