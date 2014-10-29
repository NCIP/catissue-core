/*L
 *  Copyright Washington University in St. Louis
 *  Copyright SemanticBits
 *  Copyright Persistent Systems
 *  Copyright Krishagni
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/catissue-migration-tool/LICENSE.txt for details.
 */

package com.krishagni.catissueplus.bulkoperator.metadata;

import java.io.FileReader;
import java.io.IOException;

import org.exolab.castor.mapping.Mapping;
import org.exolab.castor.mapping.MappingException;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.xml.sax.InputSource;

import com.krishagni.catissueplus.bulkoperator.util.BulkOperationConstants;
import com.krishagni.catissueplus.bulkoperator.util.BulkOperationException;
import edu.wustl.common.exception.ErrorKey;
import edu.wustl.common.util.logger.Logger;

public class BulkOperationMetadataUtil
{

	/**
	 * logger.
	 */
	private static final Logger logger = Logger.getCommonLogger(BulkOperationMetadataUtil.class);

	public BulkOperationMetaData unmarshall(String bulkOperationMetaDataXmlFile,
			String xmlMappingFile) throws BulkOperationException
	{
		BulkOperationMetaData bulkOperationMetaData = null;
		try
		{
			// -- Load a mapping file
			Mapping mapping = new Mapping();
			mapping.loadMapping(xmlMappingFile);

			Unmarshaller unmarshaller = new Unmarshaller(BulkOperationMetaData.class);
			unmarshaller.setMapping(mapping);
			
			// -- Read in the migration.xml using the mapping
			FileReader fileReader = new FileReader(bulkOperationMetaDataXmlFile);
			bulkOperationMetaData = (BulkOperationMetaData) unmarshaller.unmarshal(fileReader);
			fileReader.close();
		}
		catch (MarshalException exp)
		{
			logger.debug(exp.getMessage(), exp);
			//logger.info(exp.getMessage());
			String editedExceptionMsg = exp.getMessage().replaceAll(":", " ");
			ErrorKey errorkey = ErrorKey
					.getErrorKey(BulkOperationConstants.COMMON_ISSUES_ERROR_KEY);
			throw new BulkOperationException(errorkey, exp, editedExceptionMsg);
		}
		catch (ValidationException exp)
		{
			logger.debug(exp.getMessage(), exp);
			String editedExceptionMsg = exp.getMessage().replaceAll(":", " ");
			ErrorKey errorkey = ErrorKey
					.getErrorKey(BulkOperationConstants.COMMON_ISSUES_ERROR_KEY);
			throw new BulkOperationException(errorkey, exp, editedExceptionMsg);
		}
		catch (IOException exp)
		{
			logger.debug(exp.getMessage(), exp);
			String editedExceptionMsg = exp.getMessage().replaceAll(":", " ");
			ErrorKey errorkey = ErrorKey
					.getErrorKey(BulkOperationConstants.COMMON_ISSUES_ERROR_KEY);
			throw new BulkOperationException(errorkey, exp, editedExceptionMsg);
		}
		catch (MappingException exp)
		{
			logger.debug(exp.getMessage(), exp);
			String editedExceptionMsg = exp.getMessage().replaceAll(":", " ");
			ErrorKey errorkey = ErrorKey
					.getErrorKey(BulkOperationConstants.COMMON_ISSUES_ERROR_KEY);
			throw new BulkOperationException(errorkey, exp, editedExceptionMsg);
		}
		catch (Exception exp)
		{
			logger.debug(exp.getMessage(), exp);
			String editedExceptionMsg = exp.getMessage().replaceAll(":", " ");
			ErrorKey errorkey = ErrorKey
					.getErrorKey(BulkOperationConstants.COMMON_ISSUES_ERROR_KEY);
			throw new BulkOperationException(errorkey, exp, editedExceptionMsg);
		}
		return bulkOperationMetaData;
	}

	public BulkOperationMetaData unmarshall(InputSource bulkOperationMetaDataXml,
			InputSource xmlMappingFile) throws BulkOperationException
	{
		BulkOperationMetaData bulkOperationMetaData = null;
		try
		{
			// -- Load a mapping file
			Mapping mapping = new Mapping();
			mapping.loadMapping(xmlMappingFile);

			Unmarshaller unmarshaller = new Unmarshaller(BulkOperationMetaData.class);
			unmarshaller.setMapping(mapping);
			//unmarshaller.setProperty("org.exolab.castor.xml.naming", "mixed");

			// -- Read in the migration.xml using the mapping

			bulkOperationMetaData = (BulkOperationMetaData) unmarshaller
					.unmarshal(bulkOperationMetaDataXml);
		}
		catch (MarshalException exp)
		{
			logger.debug(exp.getMessage(), exp);
			//logger.info(exp.getMessage());
			String editedExceptionMsg = exp.getMessage().replaceAll(":", " ");
			ErrorKey errorkey = ErrorKey
					.getErrorKey(BulkOperationConstants.COMMON_ISSUES_ERROR_KEY);
			throw new BulkOperationException(errorkey, exp, editedExceptionMsg);
		}
		catch (ValidationException exp)
		{
			logger.debug(exp.getMessage(), exp);
			String editedExceptionMsg = exp.getMessage().replaceAll(":", " ");
			ErrorKey errorkey = ErrorKey
					.getErrorKey(BulkOperationConstants.COMMON_ISSUES_ERROR_KEY);
			throw new BulkOperationException(errorkey, exp, editedExceptionMsg);
		}
		catch (MappingException exp)
		{
			logger.debug(exp.getMessage(), exp);
			String editedExceptionMsg = exp.getMessage().replaceAll(":", " ");
			ErrorKey errorkey = ErrorKey
					.getErrorKey(BulkOperationConstants.COMMON_ISSUES_ERROR_KEY);
			throw new BulkOperationException(errorkey, exp, editedExceptionMsg);
		}
		catch (Exception exp)
		{
			logger.debug(exp.getMessage(), exp);
			String editedExceptionMsg = exp.getMessage().replaceAll(":", " ");
			ErrorKey errorkey = ErrorKey
					.getErrorKey(BulkOperationConstants.COMMON_ISSUES_ERROR_KEY);
			throw new BulkOperationException(errorkey, exp, editedExceptionMsg);
		}
		return bulkOperationMetaData;
	}
}