/*L
 *  Copyright Washington University in St. Louis
 *  Copyright SemanticBits
 *  Copyright Persistent Systems
 *  Copyright Krishagni
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/catissue-migration-tool/LICENSE.txt for details.
 */

package com.krishagni.catissueplus.bulkoperator.util;

import edu.wustl.common.exception.ApplicationException;
import edu.wustl.common.exception.ErrorKey;

public class BulkOperationException extends ApplicationException
{

	/**
	 * serialVersionUID.
	 */
	private static final long serialVersionUID = 232005740436959210L;

	/**
	 * The public constructor to restrict creating object without
	 * initializing mandatory members.
	 * It will called on occurrence of database related exception.
	 * @param errorKey : key assigned to the error
	 * @param exception :exception
	 * @param msgValues : message displayed when error occurred
	 */
	public BulkOperationException(final ErrorKey errorKey, final Exception exception,
			final String msgValues)
	{
		super(errorKey, exception, msgValues);
	}

	public BulkOperationException()
	{
		super(null, null, null);
	}

	public BulkOperationException(String message)
	{
		super(null, null, message);
	}

	public BulkOperationException(String message, Throwable throwable)
	{
		super(null, (Exception) throwable, message);
	}

	public BulkOperationException(Throwable throwable)
	{
		super(null, (Exception) throwable, null);
	}
}
