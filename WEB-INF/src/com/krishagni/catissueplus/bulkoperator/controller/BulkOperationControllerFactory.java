/*L
 *  Copyright Washington University in St. Louis
 *  Copyright SemanticBits
 *  Copyright Persistent Systems
 *  Copyright Krishagni
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/catissue-migration-tool/LICENSE.txt for details.
 */

package com.krishagni.catissueplus.bulkoperator.controller;


import edu.wustl.common.util.logger.Logger;

public class BulkOperationControllerFactory
{
	private static final Logger logger = Logger.getCommonLogger(BulkOperationControllerFactory.class);
	private static BulkOperationControllerFactory factory = null;

	private BulkOperationControllerFactory()
	{}

	public static BulkOperationControllerFactory getInstance()
	{
		if (factory == null)
		{
			factory = new BulkOperationControllerFactory();
		}
		return factory;
	}
}